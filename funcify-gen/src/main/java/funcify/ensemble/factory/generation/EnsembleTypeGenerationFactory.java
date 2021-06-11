package funcify.ensemble.factory.generation;

import funcify.ensemble.EnsembleKind;
import funcify.ensemble.factory.session.EnsembleTypeGenerationSession;
import funcify.ensemble.factory.session.EnsembleTypeGenerationSession.ETSWT;
import funcify.template.generation.TypeGenerationTemplate;
import funcify.template.session.TypeGenerationSession;
import funcify.tool.CharacterOps;
import funcify.tool.TypeGenerationExecutor;
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import funcify.typedef.JavaAnnotation;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaParameter;
import funcify.typedef.JavaTypeKind;
import funcify.typedef.javatype.JavaType;
import funcify.typedef.javatype.SimpleJavaTypeVariable;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;

/**
 * @author smccarron
 * @created 2021-05-28
 */
public interface EnsembleTypeGenerationFactory extends TypeGenerationTemplate<ETSWT>, EnsembleMethodGenerationFactory {

    String FUNCIFY_ENSEMBLE_PACKAGE_NAME = "funcify.ensemble";
    SimpleJavaTypeVariable WITNESS_TYPE_VARIABLE = SimpleJavaTypeVariable.of("WT");

    static EnsembleTypeGenerationFactory of() {
        return DefaultEnsembleTypeGenerationFactory.of();
    }

    <TD, MD, CD, SD, ED> TypeGenerationSession<ETSWT, TD, MD, CD, SD, ED> generateEnsembleTypesInSession(final TypeGenerationSession<ETSWT, TD, MD, CD, SD, ED> session);

    default <TD, MD, CD, SD, ED> TypeGenerationSession<ETSWT, TD, MD, CD, SD, ED> addBaseEnsembleInterfaceTypeToSession(final TypeGenerationSession<ETSWT, TD, MD, CD, SD, ED> session) {
        return TypeGenerationExecutor.of(this,
                                         session,
                                         emptyTypeDefinition(session))
                                     .updateDefinition(TypeGenerationTemplate::typeName,
                                                       "Ensemble")
                                     .updateDefinition(TypeGenerationTemplate::javaPackage,
                                                       FUNCIFY_ENSEMBLE_PACKAGE_NAME)
                                     .updateDefinition(TypeGenerationTemplate::typeModifier,
                                                       JavaModifier.PUBLIC)
                                     .updateDefinition(TypeGenerationTemplate::typeKind,
                                                       JavaTypeKind.INTERFACE)
                                     .updateDefinition(TypeGenerationTemplate::typeDefinitionTypeVariable,
                                                       WITNESS_TYPE_VARIABLE)
                                     .updateSession((t, s, d) -> EnsembleTypeGenerationSession.narrowK(s)
                                                                                              .withBaseEnsembleInterfaceTypeDefinition(d))
                                     .getSession();
    }

    default <TD, MD, CD, SD, ED> TypeGenerationSession<ETSWT, TD, MD, CD, SD, ED> addEnsembleInterfaceTypeForEnsembleKindToSession(final TypeGenerationSession<ETSWT, TD, MD, CD, SD, ED> session,
                                                                                                                                   final EnsembleKind ensembleKind) {
        final JavaType ensembleInterfaceSuperType = createEnsembleInterfaceTypeSuperType(session,
                                                                                         ensembleKind);
        return TypeGenerationExecutor.of(this,
                                         session,
                                         emptyTypeDefinition(session))
                                     .updateDefinition(TypeGenerationTemplate::typeName,
                                                       ensembleKind.getSimpleClassName())
                                     .updateDefinition(TypeGenerationTemplate::javaPackage,
                                                       FUNCIFY_ENSEMBLE_PACKAGE_NAME)
                                     .updateDefinition(TypeGenerationTemplate::typeDefinitionTypeVariables,
                                                       alphabeticTypeVariablesWithLimit(ensembleKind.getNumberOfValueParameters()))
                                     .updateDefinition(TypeGenerationTemplate::typeModifier,
                                                       JavaModifier.PUBLIC)
                                     .updateDefinition(TypeGenerationTemplate::typeKind,
                                                       JavaTypeKind.INTERFACE)
                                     .updateDefinition(TypeGenerationTemplate::superType,
                                                       ensembleInterfaceSuperType)
                                     .updateDefinition(EnsembleTypeGenerationFactory::addConvertMethodToEnsembleInterfaceType,
                                                       ensembleKind)
                                     .updateDefinition(EnsembleTypeGenerationFactory::addNarrowMethodIfSoloEnsembleInterfaceTypeDefinition,
                                                       ensembleKind)
                                     .updateSession((t, s, d) -> EnsembleTypeGenerationSession.narrowK(s)
                                                                                              .withEnsembleInterfaceTypeDefinitionsByEnsembleKind(EnsembleTypeGenerationSession.narrowK(s)
                                                                                                                                                                               .getEnsembleInterfaceTypeDefinitionsByEnsembleKind()
                                                                                                                                                                               .put(ensembleKind,
                                                                                                                                                                                    d)))
                                     .getSession();

    }

    default <TD, MD, CD, SD, ED> JavaType createEnsembleInterfaceTypeSuperType(final TypeGenerationSession<ETSWT, TD, MD, CD, SD, ED> session,
                                                                               final EnsembleKind ensembleKind) {
        if (ensembleKind == EnsembleKind.SOLO) {
            return session.javaTypeOfTypeDefinition(EnsembleTypeGenerationSession.narrowK(session)
                                                                                 .getBaseEnsembleInterfaceTypeDefinition());
        } else {
            return firstNSimpleJavaTypeVariables(ensembleKind.getNumberOfValueParameters()).skip(1)
                                                                                           .reduce(createSoloEnsembleInterfaceJavaType(),
                                                                                                   createNestedSoloTypeVariable());
        }
    }

    default <TD, MD, CD, SD, ED> BinaryOperator<JavaType> createNestedSoloTypeVariable() {
        return (jt1, jt2) -> parameterizedJavaType(FUNCIFY_ENSEMBLE_PACKAGE_NAME,
                                                   jt1.getName(),
                                                   jt1,
                                                   jt2);
    }

    default <TD, MD, CD, SD, ED> TD addNarrowMethodIfSoloEnsembleInterfaceTypeDefinition(final TypeGenerationSession<ETSWT, TD, MD, CD, SD, ED> session,
                                                                                         final TD typeDef,
                                                                                         final EnsembleKind ensembleKind) {

        if (ensembleKind != EnsembleKind.SOLO) {
            return typeDef;
        }
        final JavaType returnTypeBaseVariable = simpleJavaTypeVariable("S");
        final JavaType lowerBoundWildcardValueTypeParameter = javaTypeVariableWithWildcardLowerBounds(firstNSimpleJavaTypeVariables(ensembleKind.getNumberOfValueParameters()).findFirst()
                                                                                                                                                                              .orElseThrow(IllegalStateException::new));
        final JavaType returnTypeBaseVariableSuperType = parameterizedJavaType(FUNCIFY_ENSEMBLE_PACKAGE_NAME,
                                                                               ensembleKind.getSimpleClassName(),
                                                                               WITNESS_TYPE_VARIABLE,
                                                                               lowerBoundWildcardValueTypeParameter);
        final JavaType returnTypeVariable = javaTypeVariableWithUpperBounds(returnTypeBaseVariable,
                                                                            returnTypeBaseVariableSuperType);
        return TypeGenerationExecutor.of(this,
                                         session,
                                         typeDef)
                                     .updateDefinition(TypeGenerationTemplate::method,
                                                       TypeGenerationExecutor.of(this,
                                                                                 session,
                                                                                 emptyMethodDefinition(session))
                                                                             .updateDefinition(TypeGenerationTemplate::methodName,
                                                                                               "narrow")
                                                                             .updateDefinition(TypeGenerationTemplate::methodAnnotation,
                                                                                               JavaAnnotation.builder()
                                                                                                             .name("SuppressWarnings")
                                                                                                             .parameters(SyncMap.of("value",
                                                                                                                                    "unchecked"))
                                                                                                             .build())
                                                                             .updateDefinition(TypeGenerationTemplate::methodModifier,
                                                                                               JavaModifier.DEFAULT)
                                                                             .updateDefinition(TypeGenerationTemplate::methodTypeVariable,
                                                                                               returnTypeVariable)
                                                                             .updateDefinition(TypeGenerationTemplate::returnType,
                                                                                               returnTypeBaseVariable)
                                                                             .addChildDefinition(TypeGenerationTemplate::codeBlock,
                                                                                                 TypeGenerationTemplate::emptyCodeBlockDefinition,
                                                                                                 TypeGenerationTemplate::statement,
                                                                                                 TypeGenerationTemplate::returnStatement,
                                                                                                 templateExpression(session,
                                                                                                                    "cast_as",
                                                                                                                    "this",
                                                                                                                    returnTypeBaseVariable.getName()))
                                                                             .getDefinition())
                                     .getDefinition();

    }

    default <TD, MD, CD, SD, ED> TD addConvertMethodToEnsembleInterfaceType(final TypeGenerationSession<ETSWT, TD, MD, CD, SD, ED> session,
                                                                            final TD typeDef,
                                                                            final EnsembleKind ensembleKind) {
        final JavaType returnTypeVariable = simpleJavaTypeVariableByIndex(ensembleKind.getNumberOfValueParameters()).orElseThrow(IllegalStateException::new);

        return TypeGenerationExecutor.of(this,
                                         session,
                                         typeDef)
                                     .updateDefinition(TypeGenerationTemplate::javaImport,
                                                       Function.class)
                                     .updateDefinition(TypeGenerationTemplate::javaImport,
                                                       Objects.class)
                                     .updateDefinition(TypeGenerationTemplate::method,
                                                       TypeGenerationExecutor.of(this,
                                                                                 session,
                                                                                 emptyMethodDefinition(session))

                                                                             .updateDefinition(TypeGenerationTemplate::methodName,
                                                                                               "convert")
                                                                             .updateDefinition(TypeGenerationTemplate::methodModifier,
                                                                                               JavaModifier.DEFAULT)
                                                                             .updateDefinition(TypeGenerationTemplate::methodTypeVariable,
                                                                                               returnTypeVariable)
                                                                             .updateDefinition(TypeGenerationTemplate::returnType,
                                                                                               returnTypeVariable)
                                                                             .updateDefinition(TypeGenerationTemplate::parameter,
                                                                                               createConvertMethodFunctionParameter(session,
                                                                                                                                    ensembleKind,
                                                                                                                                    returnTypeVariable))
                                                                             .updateDefinition(TypeGenerationTemplate::codeBlock,
                                                                                               createConvertMethodCodeBlock(session))
                                                                             .getDefinition())
                                     .getDefinition();
    }

    static Optional<JavaType> simpleJavaTypeVariableByIndex(int index) {
        return CharacterOps.uppercaseAlphabetLetterByIndex(index)
                           .map(String::valueOf)
                           .map(SimpleJavaTypeVariable::of);
    }

    default <TD, MD, CD, SD, ED> JavaParameter createConvertMethodFunctionParameter(final TypeGenerationSession<ETSWT, TD, MD, CD, SD, ED> session,
                                                                                    final EnsembleKind ensembleKind,
                                                                                    final JavaType returnTypeVariable) {
        return JavaParameter.builder()
                            .name("converter")
                            .modifiers(SyncList.of(JavaModifier.FINAL))
                            .type(createConvertMethodParameterJavaType(session,
                                                                       ensembleKind,
                                                                       returnTypeVariable))
                            .build();
    }

    default <TD, MD, CD, SD, ED> JavaType createConvertMethodParameterJavaType(final TypeGenerationSession<ETSWT, TD, MD, CD, SD, ED> session,
                                                                               final EnsembleKind ensembleKind,
                                                                               final JavaType returnTypeVariable) {
        if (ensembleKind == EnsembleKind.SOLO) {
            return covariantParameterizedFunctionJavaType(Function.class,
                                                          createSoloEnsembleInterfaceJavaType(),
                                                          returnTypeVariable);
        } else {
            return covariantParameterizedFunctionJavaType(Function.class,
                                                          session.javaTypeOfTypeDefinition(EnsembleTypeGenerationSession.narrowK(session)
                                                                                                                        .getBaseEnsembleInterfaceTypeDefinition()),
                                                          returnTypeVariable);
        }
    }

    default <TD, MD, CD, SD, ED> JavaType createSoloEnsembleInterfaceJavaType() {
        return parameterizedJavaType(FUNCIFY_ENSEMBLE_PACKAGE_NAME,
                                     EnsembleKind.SOLO.getSimpleClassName(),
                                     alphabeticTypeVariablesWithLimit(1));
    }

    static SyncList<JavaType> alphabeticTypeVariablesWithLimit(final int numberOfValueParameters) {
        return SyncList.fromStream(Stream.concat(Stream.of(WITNESS_TYPE_VARIABLE),
                                                 firstNSimpleJavaTypeVariables(numberOfValueParameters)));
    }

    static Stream<JavaType> firstNSimpleJavaTypeVariables(int n) {
        return CharacterOps.firstNAlphabetLettersAsStrings(n)
                           .map(SimpleJavaTypeVariable::of);
    }

    //TODO: Expand methods within code block def factory to streamline the creation of these expressions
    default <TD, MD, CD, SD, ED> CD createConvertMethodCodeBlock(final TypeGenerationSession<ETSWT, TD, MD, CD, SD, ED> session) {
        return TypeGenerationExecutor.of(this,
                                         session,
                                         emptyCodeBlockDefinition(session))
                                     .addChildDefinition(TypeGenerationTemplate::statement,
                                                         TypeGenerationTemplate::returnStatement,
                                                         templateExpression(session,
                                                                            "function_call",
                                                                            SyncList.of("converter",
                                                                                        "this")))
                                     .getDefinition();
    }

    @AllArgsConstructor(staticName = "of")
    static class DefaultEnsembleTypeGenerationFactory implements EnsembleTypeGenerationFactory {

        @Override
        public <TD, MD, CD, SD, ED> TypeGenerationSession<ETSWT, TD, MD, CD, SD, ED> generateEnsembleTypesInSession(final TypeGenerationSession<ETSWT, TD, MD, CD, SD, ED> session) {
            return EnsembleTypeGenerationSession.narrowK(session)
                                                .getEnsembleKinds()
                                                .sorted(Comparator.comparing(EnsembleKind::getNumberOfValueParameters))
                                                .foldLeft(addBaseEnsembleInterfaceTypeToSession(session),
                                                          (s, ek) -> {
                                                              return addEnsembleInterfaceTypeForEnsembleKindToSession(s,
                                                                                                                      ek);
                                                          });
        }
    }

}
