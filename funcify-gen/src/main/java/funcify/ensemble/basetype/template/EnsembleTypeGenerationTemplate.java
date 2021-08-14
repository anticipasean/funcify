package funcify.ensemble.basetype.template;

import funcify.base.session.TypeGenerationSession;
import funcify.base.template.TypeGenerationTemplate;
import funcify.ensemble.EnsembleKind;
import funcify.ensemble.basetype.session.EnsembleTypeGenerationSession;
import funcify.ensemble.basetype.session.EnsembleTypeGenerationSession.ETSWT;
import funcify.st.template.STExpressionGenerationTemplate;
import funcify.tool.CharacterOps;
import funcify.tool.DefinitionTreeWalker;
import funcify.tool.TypeGenerationExecutor;
import funcify.tool.container.SyncList;
import funcify.typedef.JavaCodeBlock;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaParameter;
import funcify.typedef.JavaTypeDefinition;
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
public interface EnsembleTypeGenerationTemplate extends TypeGenerationTemplate<ETSWT>, STExpressionGenerationTemplate<ETSWT> {

    String FUNCIFY_ENSEMBLE_PACKAGE_NAME = "funcify.ensemble";
    SimpleJavaTypeVariable WITNESS_TYPE_VARIABLE = SimpleJavaTypeVariable.of("WT");

    static EnsembleTypeGenerationTemplate of() {
        return EnsembleTypeGenerationFactory.of();
    }

    TypeGenerationSession<ETSWT> generateEnsembleTypesInSession(final TypeGenerationSession<ETSWT> session);

    default TypeGenerationSession<ETSWT> addBaseEnsembleInterfaceTypeToSession(final TypeGenerationSession<ETSWT> session) {
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

    default TypeGenerationSession<ETSWT> addEnsembleInterfaceTypeForEnsembleKindToSession(final TypeGenerationSession<ETSWT> session,
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
                                     .updateDefinition(EnsembleTypeGenerationTemplate::addConvertMethodToEnsembleInterfaceType,
                                                       ensembleKind)
                                     .updateDefinition(EnsembleTypeGenerationTemplate::addNarrowMethodIfSoloEnsembleInterfaceTypeDefinition,
                                                       ensembleKind)
                                     .updateSession((t, s, d) -> EnsembleTypeGenerationSession.narrowK(s)
                                                                                              .withEnsembleInterfaceTypeDefinitionsByEnsembleKind(EnsembleTypeGenerationSession.narrowK(s)
                                                                                                                                                                               .getEnsembleInterfaceTypeDefinitionsByEnsembleKind()
                                                                                                                                                                               .put(ensembleKind,
                                                                                                                                                                                    d)))
                                     .getSession();

    }

    default JavaType createEnsembleInterfaceTypeSuperType(final TypeGenerationSession<ETSWT> session,
                                                          final EnsembleKind ensembleKind) {
        if (ensembleKind == EnsembleKind.SOLO) {
            return EnsembleTypeGenerationSession.narrowK(session)
                                                .getBaseEnsembleInterfaceTypeDefinition()
                                                .getJavaType();
        } else {
            return firstNSimpleJavaTypeVariables(ensembleKind.getNumberOfValueParameters()).skip(1)
                                                                                           .reduce(createSoloEnsembleInterfaceJavaType(),
                                                                                                   createNestedSoloTypeVariable());
        }
    }

    default BinaryOperator<JavaType> createNestedSoloTypeVariable() {
        return (jt1, jt2) -> parameterizedJavaType(FUNCIFY_ENSEMBLE_PACKAGE_NAME,
                                                   jt1.getName(),
                                                   SyncList.of(jt1,
                                                               jt2));
    }

    default JavaTypeDefinition addNarrowMethodIfSoloEnsembleInterfaceTypeDefinition(final TypeGenerationSession<ETSWT> session,
                                                                                    final JavaTypeDefinition typeDef,
                                                                                    final EnsembleKind ensembleKind) {

        if (ensembleKind != EnsembleKind.SOLO) {
            return typeDef;
        }
        final JavaType returnTypeBaseVariable = simpleJavaTypeVariable("S");
        final JavaType lowerBoundWildcardValueTypeParameter = javaTypeVariableWithWildcardLowerBounds(firstNSimpleJavaTypeVariables(ensembleKind.getNumberOfValueParameters()).findFirst()
                                                                                                                                                                              .orElseThrow(IllegalStateException::new));
        final JavaType returnTypeBaseVariableSuperType = parameterizedJavaType(FUNCIFY_ENSEMBLE_PACKAGE_NAME,
                                                                               ensembleKind.getSimpleClassName(),
                                                                               SyncList.of(WITNESS_TYPE_VARIABLE,
                                                                                           lowerBoundWildcardValueTypeParameter));
        final JavaType returnTypeVariable = javaTypeVariableWithUpperBound(returnTypeBaseVariable,
                                                                           returnTypeBaseVariableSuperType);
        DefinitionTreeWalker.builder()
                            .template(this)
                            .session(session)
                            .definition(typeDef)
                            .createDefaultChildDef1(TypeGenerationTemplate::emptyMethodDefinition)
                            .updateDefWithChildDef1(TypeGenerationTemplate::method);
        return TypeGenerationExecutor.of(this,
                                         session,
                                         typeDef)
                                     .updateDefinition(TypeGenerationTemplate::method,
                                                       TypeGenerationExecutor.of(this,
                                                                                 session,
                                                                                 emptyMethodDefinition(session))
                                                                             .updateDefinition(TypeGenerationTemplate::methodName,
                                                                                               "narrow")
                                                                             .addChildDefinition(TypeGenerationTemplate::methodAnnotation,
                                                                                                 EnsembleTypeGenerationTemplate::valueAnnotation,
                                                                                                 "SuppressWarnings",
                                                                                                 "unchecked")
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
                                                                                                 castAsExpression(session,
                                                                                                                  "this",
                                                                                                                  returnTypeBaseVariable))
                                                                             .getDefinition())
                                     .getDefinition();

    }

    default JavaTypeDefinition addConvertMethodToEnsembleInterfaceType(final TypeGenerationSession<ETSWT> session,
                                                                       final JavaTypeDefinition typeDef,
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
        return CharacterOps.uppercaseLetterByIndex(index)
                           .map(String::valueOf)
                           .map(SimpleJavaTypeVariable::of);
    }

    default JavaParameter createConvertMethodFunctionParameter(final TypeGenerationSession<ETSWT> session,
                                                               final EnsembleKind ensembleKind,
                                                               final JavaType returnTypeVariable) {
        return JavaParameter.builder()
                            .name("converter")
                            .modifiers(SyncList.of(JavaModifier.FINAL))
                            .type(createConvertMethodParameterJavaType(ensembleKind,
                                                                       returnTypeVariable))
                            .build();
    }

    default JavaType createConvertMethodParameterJavaType(final EnsembleKind ensembleKind,
                                                          final JavaType returnTypeVariable) {
        if (ensembleKind == EnsembleKind.SOLO) {
            return covariantParameterizedFunctionJavaType(Function.class,
                                                          SyncList.of(createSoloEnsembleInterfaceJavaType(),
                                                                      returnTypeVariable));
        } else {
            return covariantParameterizedFunctionJavaType(Function.class,
                                                          SyncList.of(parameterizedJavaType(FUNCIFY_ENSEMBLE_PACKAGE_NAME,
                                                                                            ensembleKind.getSimpleClassName(),
                                                                                            alphabeticTypeVariablesWithLimit(ensembleKind.getNumberOfValueParameters())),
                                                                      returnTypeVariable));
        }
    }

    default JavaType createSoloEnsembleInterfaceJavaType() {
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
    default JavaCodeBlock createConvertMethodCodeBlock(final TypeGenerationSession<ETSWT> session) {
        return TypeGenerationExecutor.of(this,
                                         session,
                                         emptyCodeBlockDefinition(session))
                                     .addChildDefinition(TypeGenerationTemplate::statement,
                                                         TypeGenerationTemplate::returnStatement,
                                                         EnsembleTypeGenerationTemplate::functionCallExpression,
                                                         nullCheckExpression(session,
                                                                             "converter"),
                                                         SyncList.<String>of("this"))
                                     .getDefinition();
    }

    @AllArgsConstructor(staticName = "of")
    static class EnsembleTypeGenerationFactory implements EnsembleTypeGenerationTemplate {

        @Override
        public TypeGenerationSession<ETSWT> generateEnsembleTypesInSession(final TypeGenerationSession<ETSWT> session) {
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
