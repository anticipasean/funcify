package funcify.ensemble;

import funcify.factory.session.EnsembleTypeGenerationSession;
import funcify.factory.session.EnsembleTypeGenerationSession.EnsembleTypeGenerationSessionWT;
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
import lombok.Getter;

/**
 * @author smccarron
 * @created 2021-06-06
 */
@AllArgsConstructor(staticName = "of")
@Getter
public class DefaultEnsembleInterfaceTypeDesign implements EnsembleInterfaceTypeDesign {

    private static final String FUNCIFY_ENSEMBLE_PACKAGE_NAME = "funcify.ensemble";
    private static final SimpleJavaTypeVariable WITNESS_TYPE_VARIABLE = SimpleJavaTypeVariable.of("WT");

    @Override
    public <TD, MD, CD, SD, ED> TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> fold(final TypeGenerationTemplate<EnsembleTypeGenerationSessionWT> template,
                                                                                                                final TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> session) {
        return EnsembleTypeGenerationSession.narrowK(session)
                                            .getEnsembleKinds()
                                            .sorted(Comparator.comparing(EnsembleKind::getNumberOfValueParameters))
                                            .foldLeft(baseEnsembleInterfaceTypeSessionUpdater(template,
                                                                                              session),
                                                      (s, ek) -> {
                                                          return ensembleInterfaceTypeDefinitionForEnsembleKindUpdater(template,
                                                                                                                       s,
                                                                                                                       ek);
                                                      });
    }

    private static <TD, MD, CD, SD, ED> TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> baseEnsembleInterfaceTypeSessionUpdater(final TypeGenerationTemplate<EnsembleTypeGenerationSessionWT> template,
                                                                                                                                                           final TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> session) {
        return TypeGenerationExecutor.of(template,
                                         session,
                                         template.emptyTypeDefinition(session))
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

    private static <TD, MD, CD, SD, ED> TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> ensembleInterfaceTypeDefinitionForEnsembleKindUpdater(final TypeGenerationTemplate<EnsembleTypeGenerationSessionWT> template,
                                                                                                                                                                         final TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> session,
                                                                                                                                                                         final EnsembleKind ensembleKind) {
        final JavaType ensembleInterfaceSuperType = ensembleKindInterfaceTypeSuperTypeCreator(template,
                                                                                              session,
                                                                                              ensembleKind);
        return TypeGenerationExecutor.of(template,
                                         session,
                                         template.emptyTypeDefinition(session))
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
                                     .updateDefinition(DefaultEnsembleInterfaceTypeDesign::createConverterMethodForEnsembleInterfaceType,
                                                       ensembleKind)
                                     .updateDefinition(DefaultEnsembleInterfaceTypeDesign::createAndUpdateWithNarrowMethodIfSolo,
                                                       ensembleKind)
                                     .updateSession((t, s, d) -> EnsembleTypeGenerationSession.narrowK(s)
                                                                                              .withEnsembleInterfaceTypeDefinitionsByEnsembleKind(EnsembleTypeGenerationSession.narrowK(s)
                                                                                                                                                                               .getEnsembleInterfaceTypeDefinitionsByEnsembleKind()
                                                                                                                                                                               .put(ensembleKind,
                                                                                                                                                                                    d)))
                                     .getSession();

    }

    private static SyncList<JavaType> alphabeticTypeVariablesWithLimit(final int numberOfValueParameters) {
        return SyncList.fromStream(Stream.concat(Stream.of(WITNESS_TYPE_VARIABLE),
                                                 firstNSimpleJavaTypeVariables(numberOfValueParameters)));
    }

    private static <TD, MD, CD, SD, ED> TD createAndUpdateWithNarrowMethodIfSolo(final TypeGenerationTemplate<EnsembleTypeGenerationSessionWT> template,
                                                                                 final TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> session,
                                                                                 final TD typeDef,
                                                                                 final EnsembleKind ensembleKind) {

        if (ensembleKind != EnsembleKind.SOLO) {
            return typeDef;
        }
        final JavaType returnTypeBaseVariable = template.simpleJavaTypeVariable("S");
        final JavaType lowerBoundWildcardValueTypeParameter = template.javaTypeVariableWithWildcardLowerBounds(firstNSimpleJavaTypeVariables(ensembleKind.getNumberOfValueParameters()).findFirst()
                                                                                                                                                                                       .orElseThrow(IllegalStateException::new));
        final JavaType returnTypeBaseVariableSuperType = template.parameterizedJavaType(FUNCIFY_ENSEMBLE_PACKAGE_NAME,
                                                                                        ensembleKind.getSimpleClassName(),
                                                                                        WITNESS_TYPE_VARIABLE,
                                                                                        lowerBoundWildcardValueTypeParameter);
        final JavaType returnTypeVariable = template.javaTypeVariableWithUpperBounds(returnTypeBaseVariable,
                                                                                     returnTypeBaseVariableSuperType);
        return TypeGenerationExecutor.of(template,
                                         session,
                                         typeDef)
                                     .updateDefinition(TypeGenerationTemplate::method,
                                                       TypeGenerationExecutor.of(template,
                                                                                 session,
                                                                                 template.emptyMethodDefinition(session))
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
                                                                                                 template.templateExpression(session,
                                                                                                                             "cast_as",
                                                                                                                             "this",
                                                                                                                             returnTypeBaseVariable.getName()))
                                                                             .getDefinition())
                                     .getDefinition();

    }


    private static <TD, MD, CD, SD, ED> TD createConverterMethodForEnsembleInterfaceType(final TypeGenerationTemplate<EnsembleTypeGenerationSessionWT> template,
                                                                                         final TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> session,
                                                                                         final TD typeDef,
                                                                                         final EnsembleKind ensembleKind) {
        final JavaType returnTypeVariable = simpleJavaTypeVariableByIndex(ensembleKind.getNumberOfValueParameters()).orElseThrow(IllegalStateException::new);

        return TypeGenerationExecutor.of(template,
                                         session,
                                         typeDef)
                                     .updateDefinition(TypeGenerationTemplate::javaImport,
                                                       Function.class)
                                     .updateDefinition(TypeGenerationTemplate::javaImport,
                                                       Objects.class)
                                     .updateDefinition(TypeGenerationTemplate::method,
                                                       TypeGenerationExecutor.of(template,
                                                                                 session,
                                                                                 template.emptyMethodDefinition(session))

                                                                             .updateDefinition(TypeGenerationTemplate::methodName,
                                                                                               "convert")
                                                                             .updateDefinition(TypeGenerationTemplate::methodModifier,
                                                                                               JavaModifier.DEFAULT)
                                                                             .updateDefinition(TypeGenerationTemplate::methodTypeVariable,
                                                                                               returnTypeVariable)
                                                                             .updateDefinition(TypeGenerationTemplate::returnType,
                                                                                               returnTypeVariable)
                                                                             .updateDefinition(TypeGenerationTemplate::parameter,
                                                                                               converterFunctionParameter(template,
                                                                                                                          session,
                                                                                                                          ensembleKind,
                                                                                                                          returnTypeVariable))
                                                                             .updateDefinition(TypeGenerationTemplate::codeBlock,
                                                                                               converterMethodCodeBlock(template,
                                                                                                                        session))
                                                                             .getDefinition())
                                     .getDefinition();
    }

    private static <TD, MD, CD, SD, ED> JavaType ensembleKindInterfaceTypeSuperTypeCreator(final TypeGenerationTemplate<EnsembleTypeGenerationSessionWT> template,
                                                                                           final TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> session,
                                                                                           final EnsembleKind ensembleKind) {
        if (ensembleKind == EnsembleKind.SOLO) {
            return session.javaTypeOfTypeDefinition(EnsembleTypeGenerationSession.narrowK(session)
                                                                                 .getBaseEnsembleInterfaceTypeDefinition());
        } else {
            return firstNSimpleJavaTypeVariables(ensembleKind.getNumberOfValueParameters()).skip(1)
                                                                                           .reduce(soloEnsembleInterfaceTypeCreator(template),
                                                                                                   nestingSoloTypeVariableCreator(template));
        }
    }


    //TODO: Expand methods within code block def factory to streamline the creation of these expressions
    private static <TD, MD, CD, SD, ED> CD converterMethodCodeBlock(final TypeGenerationTemplate<EnsembleTypeGenerationSessionWT> template,
                                                                    final TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> session) {
        return TypeGenerationExecutor.of(template,
                                         session,
                                         template.emptyCodeBlockDefinition(session))
                                     .addChildDefinition(TypeGenerationTemplate::statement,
                                                         TypeGenerationTemplate::returnStatement,
                                                         template.templateExpression(session,
                                                                                     "function_call",
                                                                                     SyncList.of("converter",
                                                                                                 "this")))
                                     .getDefinition();
    }


    private static <TD, MD, CD, SD, ED> JavaParameter converterFunctionParameter(final TypeGenerationTemplate<EnsembleTypeGenerationSessionWT> template,
                                                                                 final TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> session,
                                                                                 final EnsembleKind ensembleKind,
                                                                                 final JavaType returnTypeVariable) {
        return JavaParameter.builder()
                            .name("converter")
                            .modifiers(SyncList.of(JavaModifier.FINAL))
                            .type(converterFunctionParameterType(template,
                                                                 session,
                                                                 ensembleKind,
                                                                 returnTypeVariable))
                            .build();
    }

    private static <TD, MD, CD, SD, ED> JavaType converterFunctionParameterType(final TypeGenerationTemplate<EnsembleTypeGenerationSessionWT> template,
                                                                                final TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> session,
                                                                                final EnsembleKind ensembleKind,
                                                                                final JavaType returnTypeVariable) {
        if (ensembleKind == EnsembleKind.SOLO) {
            return template.covariantParameterizedFunctionJavaType(Function.class,
                                                                   soloEnsembleInterfaceTypeCreator(template),
                                                                   returnTypeVariable);
        } else {
            return template.covariantParameterizedFunctionJavaType(Function.class,
                                                                   session.javaTypeOfTypeDefinition(EnsembleTypeGenerationSession.narrowK(session)
                                                                                                                                 .getBaseEnsembleInterfaceTypeDefinition()),
                                                                   returnTypeVariable);
        }
    }

    private static <TD, MD, CD, SD, ED> JavaType soloEnsembleInterfaceTypeCreator(final TypeGenerationTemplate<EnsembleTypeGenerationSessionWT> template) {
        return template.parameterizedJavaType(FUNCIFY_ENSEMBLE_PACKAGE_NAME,
                                              EnsembleKind.SOLO.getSimpleClassName(),
                                              alphabeticTypeVariablesWithLimit(1));
    }

    private static <TD, MD, CD, SD, ED> BinaryOperator<JavaType> nestingSoloTypeVariableCreator(final TypeGenerationTemplate<EnsembleTypeGenerationSessionWT> template) {
        return (jt1, jt2) -> template.parameterizedJavaType(FUNCIFY_ENSEMBLE_PACKAGE_NAME,
                                                            jt1.getName(),
                                                            jt1,
                                                            jt2);
    }

    private static Stream<JavaType> firstNSimpleJavaTypeVariables(int n) {
        return CharacterOps.firstNAlphabetLettersAsStrings(n)
                           .map(SimpleJavaTypeVariable::of);
    }

    private static Optional<JavaType> simpleJavaTypeVariableByIndex(int index) {
        return CharacterOps.uppercaseAlphabetLetterByIndex(index)
                           .map(String::valueOf)
                           .map(SimpleJavaTypeVariable::of);
    }

}
