package funcify;

import funcify.tool.CharacterOps;
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import funcify.typedef.JavaAnnotation;
import funcify.typedef.JavaCodeBlock;
import funcify.typedef.JavaMethod;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaParameter;
import funcify.typedef.JavaTypeDefinition;
import funcify.typedef.JavaTypeKind;
import funcify.typedef.javaexpr.TemplatedExpression;
import funcify.typedef.javastatement.ReturnStatement;
import funcify.typedef.javatype.JavaType;
import funcify.typedef.javatype.SimpleJavaTypeVariable;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author smccarron
 * @created 2021-05-22
 */
public class EnsembleInterfaceTypeAssembler {

    public static final String FUNCIFY_ENSEMBLE_PACKAGE_NAME = "funcify.ensemble";
    public static final SimpleJavaTypeVariable WITNESS_TYPE_VARIABLE = SimpleJavaTypeVariable.of("WT");

    public EnsembleInterfaceTypeAssembler() {

    }

    public DefaultGenerationSession assembleEnsembleInterfaceTypes(final DefaultGenerationSession generationSession) {
        final JavaTypeDefinition baseEnsembleInterfaceTypeDefinition = baseEnsembleInterfaceTypeCreator(JavaTypeDefinitionFactory.getInstance());
        return generationSession.withBaseEnsembleInterfaceTypeDefinition(baseEnsembleInterfaceTypeDefinition)
                                .withEnsembleInterfaceTypeDefinitionsByEnsembleKind(SyncMap.fromIterable(generationSession.getEnsembleKinds(),
                                                                                                         ek -> ek,
                                                                                                         ek -> buildEnsembleInterfaceTypeDefinitionForEnsembleKind(JavaTypeDefinitionFactory.getInstance(),
                                                                                                                                                                   baseEnsembleInterfaceTypeDefinition,
                                                                                                                                                                   ek)));

    }

    private static JavaTypeDefinition buildEnsembleInterfaceTypeDefinitionForEnsembleKind(final JavaDefinitionFactory<JavaTypeDefinition> javaDefinitionFactory,
                                                                                          final JavaTypeDefinition baseEnsembleInterfaceTypeDefinition,
                                                                                          final EnsembleKind ensembleKind) {
        final JavaType ensembleInterfaceSuperType = ensembleKindInterfaceTypeSuperTypeCreator(javaDefinitionFactory,
                                                                                              baseEnsembleInterfaceTypeDefinition,
                                                                                              ensembleKind);
        return JavaTypeDefinition.builder()
                                 .build()
                                 .foldUpdate(javaDefinitionFactory::name,
                                             ensembleKind.getSimpleClassName())
                                 .foldUpdate(javaDefinitionFactory::javaPackage,
                                             FUNCIFY_ENSEMBLE_PACKAGE_NAME)
                                 .foldUpdate(javaDefinitionFactory::typeVariables,
                                             SyncList.fromStream(Stream.concat(Stream.of(WITNESS_TYPE_VARIABLE),
                                                                               firstNSimpleJavaTypeVariables(ensembleKind.getNumberOfValueParameters()))))
                                 .foldUpdate(javaDefinitionFactory::modifier,
                                             JavaModifier.PUBLIC)
                                 .foldUpdate(javaDefinitionFactory::typeKind,
                                             JavaTypeKind.INTERFACE)
                                 .foldUpdate(javaDefinitionFactory::superType,
                                             ensembleInterfaceSuperType)
                                 .update(createAndUpdateWithConvertMethod(javaDefinitionFactory,
                                                                          ensembleKind,
                                                                          ensembleInterfaceSuperType))
                                 .update(createAndUpdateWithNarrowMethodIfSolo(javaDefinitionFactory,
                                                                               ensembleKind));

    }

    private static <D extends Definition<D>> Function<D, D> createAndUpdateWithNarrowMethodIfSolo(final JavaDefinitionFactory<D> javaDefinitionFactory,
                                                                                                  final EnsembleKind ensembleKind) {
        return (D definition) -> {
            if (ensembleKind != EnsembleKind.SOLO) {
                return definition;
            }
            final JavaType returnTypeBaseVariable = javaDefinitionFactory.simpleJavaTypeVariable("S");
            final JavaType lowerBoundWildcardValueTypeParameter = javaDefinitionFactory.javaTypeVariableWithWildcardLowerBounds(firstNSimpleJavaTypeVariables(ensembleKind.getNumberOfValueParameters()).findFirst()
                                                                                                                                                                                                        .orElseThrow(IllegalStateException::new));
            final JavaType returnTypeBaseVariableSuperType = javaDefinitionFactory.parameterizedJavaType(FUNCIFY_ENSEMBLE_PACKAGE_NAME,
                                                                                                         ensembleKind.getSimpleClassName(),
                                                                                                         WITNESS_TYPE_VARIABLE,
                                                                                                         lowerBoundWildcardValueTypeParameter);
            final JavaType returnTypeVariable = javaDefinitionFactory.javaTypeVariableWithUpperBounds(returnTypeBaseVariable,
                                                                                                      returnTypeBaseVariableSuperType);

            final JavaDefinitionFactory<JavaMethod> javaMethodDefinitionFactory = JavaMethodFactory.getInstance();
            return javaDefinitionFactory.method(definition,
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

    private static <D extends Definition<D>> Function<D, D> createAndUpdateWithConvertMethod(final JavaDefinitionFactory<D> javaDefinitionFactory,
                                                                                             final EnsembleKind ensembleKind,
                                                                                             final JavaType ensembleInterfaceSuperType) {
        return (D definition) -> {
            return createConverterMethodForEnsembleInterfaceType(javaDefinitionFactory,
                                                                 definition,
                                                                 ensembleKind,
                                                                 ensembleInterfaceSuperType);
        };
    }

    private static <D extends Definition<D>> D createConverterMethodForEnsembleInterfaceType(final JavaDefinitionFactory<D> javaDefinitionFactory,
                                                                                             final D definition,
                                                                                             final EnsembleKind ensembleKind,
                                                                                             final JavaType ensembleInterfaceSuperType) {
        final JavaType returnTypeVariable = firstNSimpleJavaTypeVariables(
            ensembleKind.getNumberOfValueParameters() + 1).collect(Collectors.toList())
                                                          .get(ensembleKind.getNumberOfValueParameters());
        final JavaDefinitionFactory<JavaMethod> javaMethodDefinitionFactory = JavaMethodFactory.getInstance();
        return javaDefinitionFactory.method(javaDefinitionFactory.javaImport(definition,
                                                                             Function.class,
                                                                             Objects.class),
                                            JavaMethod.builder()
                                                      .build()
                                                      .foldUpdate(javaMethodDefinitionFactory::name,
                                                                  "convert")
                                                      .foldUpdate(javaMethodDefinitionFactory::modifier,
                                                                  JavaModifier.DEFAULT)
                                                      .foldUpdate(javaMethodDefinitionFactory::typeVariable,
                                                                  returnTypeVariable)
                                                      .foldUpdate(javaMethodDefinitionFactory::returnType,
                                                                  returnTypeVariable)
                                                      .foldUpdate(javaMethodDefinitionFactory::parameter,
                                                                  converterFunctionParameter(javaMethodDefinitionFactory,
                                                                                             ensembleKind,
                                                                                             ensembleInterfaceSuperType,
                                                                                             returnTypeVariable))
                                                      .foldUpdate(javaMethodDefinitionFactory::codeBlock,
                                                                  converterMethodCodeBlock(JavaCodeBlockFactory.getInstance())));
    }

    private static <D extends Definition<D>> JavaType ensembleKindInterfaceTypeSuperTypeCreator(final JavaDefinitionFactory<D> javaDefinitionFactory,
                                                                                                final JavaTypeDefinition baseEnsembleInterfaceTypeDefinition,
                                                                                                final EnsembleKind ensembleKind) {
        if (ensembleKind == EnsembleKind.SOLO) {
            return baseEnsembleInterfaceTypeDefinition.getJavaType();
        } else {
            return firstNSimpleJavaTypeVariables(ensembleKind.getNumberOfValueParameters()).skip(1)
                                                                                           .reduce(soloEnsembleInterfaceTypeCreator(javaDefinitionFactory),
                                                                                                   nestingSoloTypeVariableCreator(javaDefinitionFactory));
        }
    }

    private static JavaTypeDefinition baseEnsembleInterfaceTypeCreator(final JavaDefinitionFactory<JavaTypeDefinition> javaDefinitionFactory) {
        return JavaTypeDefinition.builder()
                                 .build()
                                 .foldUpdate(javaDefinitionFactory::name,
                                             "Ensemble")
                                 .foldUpdate(javaDefinitionFactory::javaPackage,
                                             FUNCIFY_ENSEMBLE_PACKAGE_NAME)
                                 .foldUpdate(javaDefinitionFactory::modifier,
                                             JavaModifier.PUBLIC)
                                 .foldUpdate(javaDefinitionFactory::typeKind,
                                             JavaTypeKind.INTERFACE)
                                 .foldUpdate(javaDefinitionFactory::typeVariable,
                                             WITNESS_TYPE_VARIABLE);
    }

    //TODO: Expand methods within code block def factory to streamline the creation of these expressions
    private static JavaCodeBlock converterMethodCodeBlock(JavaDefinitionFactory<JavaCodeBlock> javaCodeBlockDefinitionFactory) {
        return JavaCodeBlock.builder()
                            .build()
                            .foldUpdate(javaCodeBlockDefinitionFactory::statement,
                                        ReturnStatement.builder()
                                                       .expressions(SyncList.of(TemplatedExpression.builder()
                                                                                                   .templateCall("function_call")
                                                                                                   .templateParameters(SyncList.of("converter",
                                                                                                                                   "this"))
                                                                                                   .build()))
                                                       .build());
    }


    private static <D extends Definition<D>> JavaParameter converterFunctionParameter(final JavaDefinitionFactory<D> javaDefinitionFactory,
                                                                                      final EnsembleKind ensembleKind,
                                                                                      final JavaType ensembleInterfaceSuperType,
                                                                                      final JavaType returnTypeVariable) {
        return JavaParameter.builder()
                            .name("converter")
                            .modifiers(SyncList.of(JavaModifier.FINAL))
                            .type(converterFunctionParameterType(javaDefinitionFactory,
                                                                 ensembleKind,
                                                                 ensembleInterfaceSuperType,
                                                                 returnTypeVariable))
                            .build();
    }

    private static <D extends Definition<D>> JavaType converterFunctionParameterType(final JavaDefinitionFactory<D> javaDefinitionFactory,
                                                                                     final EnsembleKind ensembleKind,
                                                                                     final JavaType ensembleInterfaceSuperType,
                                                                                     final JavaType returnTypeVariable) {
        if (ensembleKind == EnsembleKind.SOLO) {
            return javaDefinitionFactory.covariantParameterizedFunctionJavaType(Function.class,
                                                                                soloEnsembleInterfaceTypeCreator(javaDefinitionFactory),
                                                                                returnTypeVariable);
        } else {
            return javaDefinitionFactory.covariantParameterizedFunctionJavaType(Function.class,
                                                                                ensembleInterfaceSuperType,
                                                                                returnTypeVariable);
        }
    }

    private static <D extends Definition<D>> JavaType soloEnsembleInterfaceTypeCreator(final JavaDefinitionFactory<D> javaDefinitionFactory) {
        return javaDefinitionFactory.parameterizedJavaType(FUNCIFY_ENSEMBLE_PACKAGE_NAME,
                                                           EnsembleKind.SOLO.getSimpleClassName(),
                                                           SyncList.fromStream(Stream.concat(Stream.of(WITNESS_TYPE_VARIABLE),
                                                                                             firstNSimpleJavaTypeVariables(1))));
    }

    private static <D extends Definition<D>> BinaryOperator<JavaType> nestingSoloTypeVariableCreator(final JavaDefinitionFactory<D> javaDefinitionFactory) {
        return (jt1, jt2) -> javaDefinitionFactory.parameterizedJavaType(FUNCIFY_ENSEMBLE_PACKAGE_NAME,
                                                                         jt1.getName(),
                                                                         jt1,
                                                                         jt2);
    }

    private static Stream<JavaType> firstNSimpleJavaTypeVariables(int n) {
        return CharacterOps.firstNAlphabetLettersAsStrings(n)
                           .map(SimpleJavaTypeVariable::of);
    }

}
