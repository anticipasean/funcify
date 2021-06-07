package funcify.ensemble;

import funcify.JavaCodeBlockFactory;
import funcify.JavaDefinitionFactory;
import funcify.JavaMethodFactory;
import funcify.factory.session.EnsembleTypeGenerationSession;
import funcify.factory.session.EnsembleTypeGenerationSession.EnsembleTypeGenerationSessionWT;
import funcify.template.generation.TypeGenerationTemplate;
import funcify.template.session.TypeGenerationSession;
import funcify.tool.CharacterOps;
import funcify.tool.TemplateExecutor;
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import funcify.typedef.JavaAnnotation;
import funcify.typedef.JavaCodeBlock;
import funcify.typedef.JavaMethod;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaParameter;
import funcify.typedef.JavaTypeKind;
import funcify.typedef.javaexpr.TemplatedExpression;
import funcify.typedef.javastatement.ReturnStatement;
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
                                            .foldLeft(baseEnsembleInterfaceTypeCreator(template,
                                                                                       session),
                                                      (s, ek) -> {
                                                          return s;
                                                      });
    }

    private static <TD, MD, CD, SD, ED> TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> baseEnsembleInterfaceTypeCreator(final TypeGenerationTemplate<EnsembleTypeGenerationSessionWT> template,
                                                                                                                                                    final TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> session) {
        return TemplateExecutor.of(template,
                                   session,
                                   template.emptyTypeDefinition(session))
                               .definitionUpdate(TypeGenerationTemplate::typeName,
                                                 "Ensemble")
                               .definitionUpdate(TypeGenerationTemplate::javaPackage,
                                                 FUNCIFY_ENSEMBLE_PACKAGE_NAME)
                               .definitionUpdate(TypeGenerationTemplate::typeModifier,
                                                 JavaModifier.PUBLIC)
                               .definitionUpdate(TypeGenerationTemplate::typeKind,
                                                 JavaTypeKind.INTERFACE)
                               .definitionUpdate(TypeGenerationTemplate::typeDefinitionTypeVariable,
                                                 WITNESS_TYPE_VARIABLE)
                               .sessionUpdate((t, s, d) -> EnsembleTypeGenerationSession.narrowK(s)
                                                                                        .withBaseEnsembleInterfaceTypeDefinition(d))
                               .getSession();
    }

    private static <TD, MD, CD, SD, ED> TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> ensembleInterfaceTypeDefinitionForEnsembleKind(final TypeGenerationTemplate<EnsembleTypeGenerationSessionWT> template,
                                                                                                                                                                  final TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> session,
                                                                                                                                                                  final EnsembleKind ensembleKind) {
        final JavaType ensembleInterfaceSuperType = ensembleKindInterfaceTypeSuperTypeCreator(template,
                                                                                              session,
                                                                                              ensembleKind);
        return TemplateExecutor.of(template,
                                   session,
                                   template.emptyTypeDefinition(session))
                               .definitionUpdate(TypeGenerationTemplate::typeName,
                                                 ensembleKind.getSimpleClassName())
                               .definitionUpdate(TypeGenerationTemplate::javaPackage,
                                                 FUNCIFY_ENSEMBLE_PACKAGE_NAME)
                               .definitionUpdate(TypeGenerationTemplate::typeDefinitionTypeVariables,
                                                 alphabeticTypeVariablesWithLimit(ensembleKind.getNumberOfValueParameters()))
                               .definitionUpdate(TypeGenerationTemplate::typeModifier,
                                                 JavaModifier.PUBLIC)
                               .definitionUpdate(TypeGenerationTemplate::typeKind,
                                                 JavaTypeKind.INTERFACE)
                               .definitionUpdate(TypeGenerationTemplate::superType,
                                                 ensembleInterfaceSuperType)
                               .definitionUpdate(TypeGenerationTemplate::methods,
                                                 SyncList.of(createConverterMethodForEnsembleInterfaceType(template,
                                                                                                           session,
                                                                                                           ensembleKind),
                                                             createAndUpdateWithNarrowMethodIfSolo(template,
                                                                                                   session,
                                                                                                   ensembleKind)))
                               .sessionUpdate((t, s, d) -> EnsembleTypeGenerationSession.narrowK(s)
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

    private static <TD, MD, CD, SD, ED> MD createAndUpdateWithNarrowMethodIfSolo(final TypeGenerationTemplate<EnsembleTypeGenerationSessionWT> template,
                                                                                             final TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> session,
                                                                                             final EnsembleKind ensembleKind) {
        return (D definition) -> {
            if (ensembleKind != EnsembleKind.SOLO) {
                return definition;
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

            final JavaDefinitionFactory<JavaMethod> javaMethodDefinitionFactory = JavaMethodFactory.getInstance();
            return template.method(definition,
                                   JavaMethod.builder()
                                             .build()
                                             .foldUpdate(javaMethodDefinitionFactory::name,
                                                         "narrow")
                                             .foldUpdate(javaMethodDefinitionFactory::javaAnnotation,
                                                         JavaAnnotation.builder()
                                                                       .name("SuppressWarnings")
                                                                       .parameters(SyncMap.of("value",
                                                                                              "unchecked"))
                                                                       .build())
                                             .foldUpdate(javaMethodDefinitionFactory::modifier,
                                                         JavaModifier.DEFAULT)
                                             .foldUpdate(javaMethodDefinitionFactory::typeVariable,
                                                         returnTypeVariable)
                                             .foldUpdate(javaMethodDefinitionFactory::returnType,
                                                         returnTypeBaseVariable)
                                             .foldUpdate(javaMethodDefinitionFactory::codeBlock,
                                                         JavaCodeBlockFactory.getInstance()
                                                                             .statement(JavaCodeBlock.builder()
                                                                                                     .build(),
                                                                                        ReturnStatement.builder()
                                                                                                       .expressions(SyncList.of(TemplatedExpression.builder()
                                                                                                                                                   .templateCall("cast_as")
                                                                                                                                                   .templateParameters(SyncList.of("this",
                                                                                                                                                                                   "S"))
                                                                                                                                                   .build()))
                                                                                                       .build())));
        };
    }


    private static <TD, MD, CD, SD, ED> MD createConverterMethodForEnsembleInterfaceType(final TypeGenerationTemplate<EnsembleTypeGenerationSessionWT> template,
                                                                                         final TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> session,
                                                                                         final EnsembleKind ensembleKind) {
        final JavaType returnTypeVariable = simpleJavaTypeVariableByIndex(ensembleKind.getNumberOfValueParameters()).orElseThrow(IllegalStateException::new);

        return TemplateExecutor.of(template,
                                   session,
                                   template.emptyMethodDefinition(session))
                               .definitionUpdate(TypeGenerationTemplate::javaImport,
                                                 Function.class,
                                                 Objects.class)

                               .definitionUpdate(TypeGenerationTemplate::methodName,
                                                 "convert")
                               .definitionUpdate(TypeGenerationTemplate::methodModifier,
                                                 JavaModifier.DEFAULT)
                               .definitionUpdate(TypeGenerationTemplate::methodTypeVariable,
                                                 returnTypeVariable)
                               .definitionUpdate(TypeGenerationTemplate::returnType,
                                                 returnTypeVariable)
                               .definitionUpdate(TypeGenerationTemplate::parameter,
                                                 converterFunctionParameter(template,
                                                                            session,
                                                                            ensembleKind,
                                                                            returnTypeVariable))
                               .definitionUpdate(TypeGenerationTemplate::codeBlock,
                                                 converterMethodCodeBlock(template,
                                                                          session))
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
        return TemplateExecutor.of(template,
                                   session,
                                   template.emptyCodeBlockDefinition(session))
                               .definitionUpdate(TypeGenerationTemplate::statement,
                                                 template.returnStatement(session,
                                                                          SyncList.of(template.templateExpression(session,
                                                                                                                  "function_call",
                                                                                                                  SyncList.of("converter",
                                                                                                                              "this")))))
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
                                                                 ensembleInterfaceSuperType,
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
                                                                   ensembleInterfaceSuperType,
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
