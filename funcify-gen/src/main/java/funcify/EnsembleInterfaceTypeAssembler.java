package funcify;

import funcify.typedef.JavaCodeBlock;
import funcify.typedef.JavaMethod;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaParameter;
import funcify.typedef.JavaTypeDefinition;
import funcify.typedef.JavaTypeKind;
import funcify.typedef.javastatement.ReturnStatement;
import funcify.typedef.javastatement.TemplatedExpression;
import funcify.typedef.javastatement.TextExpression;
import funcify.typedef.javatype.JavaType;
import funcify.typedef.javatype.SimpleJavaTypeVariable;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
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

    public GenerationSession assembleEnsembleInterfaceTypes(final GenerationSession generationSession) {
        final JavaTypeDefinition baseEnsembleInterfaceTypeDefinition = baseEnsembleInterfaceTypeCreator(JavaTypeDefinitionFactory.getInstance());
        return generationSession.withBaseEnsembleInterfaceTypeDefinition(baseEnsembleInterfaceTypeDefinition)
                                .withEnsembleInterfaceTypeDefinitionsByEnsembleKind(generationSession.getEnsembleKinds()
                                                                                                     .stream()
                                                                                                     .map(ek -> new SimpleImmutableEntry<>(ek,
                                                                                                                                           buildEnsembleInterfaceTypeDefinitionForEnsembleKind(JavaTypeDefinitionFactory.getInstance(),
                                                                                                                                                                                               baseEnsembleInterfaceTypeDefinition,
                                                                                                                                                                                               ek)))
                                                                                                     .collect(Collectors.toMap(Map.Entry::getKey,
                                                                                                                               Map.Entry::getValue)));

    }

    private static <D> D buildEnsembleInterfaceTypeDefinitionForEnsembleKind(final JavaDefinitionFactory<D> javaDefinitionFactory,
                                                                             final JavaTypeDefinition baseEnsembleInterfaceTypeDefinition,
                                                                             final EnsembleKind ensembleKind) {
        final JavaType ensembleInterfaceSuperType = ensembleKindInterfaceTypeSuperTypeCreator(javaDefinitionFactory,
                                                                                              baseEnsembleInterfaceTypeDefinition,
                                                                                              ensembleKind);
        return Optional.of(javaDefinitionFactory.name(ensembleKind.getSimpleClassName()))
                       .map(ap(javaDefinitionFactory::javaPackage,
                               FUNCIFY_ENSEMBLE_PACKAGE_NAME))
                       .map(ap(javaDefinitionFactory::typeVariables,
                               Stream.concat(Stream.of(WITNESS_TYPE_VARIABLE),
                                             firstNSimpleJavaTypeVariables(ensembleKind.getNumberOfValueParameters()))
                                     .collect(Collectors.toList())))
                       .map(ap(javaDefinitionFactory::modifier,
                               JavaModifier.PUBLIC))
                       .map(ap(javaDefinitionFactory::typeKind,
                               JavaTypeKind.INTERFACE))
                       .map(ap(javaDefinitionFactory::superType,
                               ensembleInterfaceSuperType))
                       .map(createAndUpdateWithConvertMethod(javaDefinitionFactory,
                                                             ensembleKind,
                                                             ensembleInterfaceSuperType))
                       .orElseThrow(IllegalStateException::new);

    }

    private static <D> Function<D, D> createAndUpdateWithConvertMethod(final JavaDefinitionFactory<D> javaDefinitionFactory,
                                                                       final EnsembleKind ensembleKind,
                                                                       final JavaType ensembleInterfaceSuperType) {
        return (D definition) -> {
            return createConverterMethodForEnsembleInterfaceType(javaDefinitionFactory,
                                                                 definition,
                                                                 ensembleKind,
                                                                 ensembleInterfaceSuperType);
        };
    }

    private static <D> D createConverterMethodForEnsembleInterfaceType(final JavaDefinitionFactory<D> javaDefinitionFactory,
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
                                            Optional.of(javaMethodDefinitionFactory.name("convert"))
                                                    .map(ap(javaMethodDefinitionFactory::modifier,
                                                            JavaModifier.DEFAULT))
                                                    .map(ap(javaMethodDefinitionFactory::typeVariable,
                                                            returnTypeVariable))
                                                    .map(ap(javaMethodDefinitionFactory::returnType,
                                                            returnTypeVariable))
                                                    .map(ap(javaMethodDefinitionFactory::parameter,
                                                            converterFunctionParameter(javaMethodDefinitionFactory,
                                                                                       ensembleKind,
                                                                                       ensembleInterfaceSuperType,
                                                                                       returnTypeVariable)))
                                                    .map(ap(javaMethodDefinitionFactory::codeBlock,
                                                            converterMethodCodeBlock(JavaCodeBlockFactory.getInstance())))
                                                    .orElseThrow(IllegalStateException::new));
    }

    private static <D> JavaType ensembleKindInterfaceTypeSuperTypeCreator(final JavaDefinitionFactory<D> javaDefinitionFactory,
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

    private static <D> D baseEnsembleInterfaceTypeCreator(final JavaDefinitionFactory<D> javaDefinitionFactory) {
        return Optional.of(javaDefinitionFactory.name("Ensemble"))
                       .map(ap(javaDefinitionFactory::javaPackage,
                               FUNCIFY_ENSEMBLE_PACKAGE_NAME))
                       .map(ap(javaDefinitionFactory::modifier,
                               JavaModifier.PUBLIC))
                       .map(ap(javaDefinitionFactory::typeKind,
                               JavaTypeKind.INTERFACE))
                       .map(ap(javaDefinitionFactory::typeVariable,
                               WITNESS_TYPE_VARIABLE))
                       .orElseThrow(IllegalStateException::new);
    }

    private static JavaCodeBlock converterMethodCodeBlock(JavaDefinitionFactory<JavaCodeBlock> javaCodeBlockDefinitionFactory) {
        return Optional.of(javaCodeBlockDefinitionFactory.name(""))
                       .map(ap(javaCodeBlockDefinitionFactory::statement,
                               ReturnStatement.builder()
                                              .expressions(Arrays.asList(TemplatedExpression.builder()
                                                                             .templateCall("null_object_check")
                                                                             .templateParameter("converter")
                                                                             .build(),
                                                                         TextExpression.of(".apply(this)")))
                                              .build()))
                       .orElseThrow(IllegalStateException::new);
    }

    private static <K, V> Map<K, V> fromPairs(final K k,
                                              final V v) {
        return Stream.of(new SimpleImmutableEntry<>(k,
                                                    v))
                     .collect(Collectors.toMap(Entry::getKey,
                                               Entry::getValue));
    }

    private static JavaParameter converterFunctionParameter(final JavaDefinitionFactory<JavaMethod> javaMethodDefinitionFactory,
                                                            final EnsembleKind ensembleKind,
                                                            final JavaType ensembleInterfaceSuperType,
                                                            final JavaType returnTypeVariable) {
        return JavaParameter.builder()
                            .name("converter")
                            .modifiers(Arrays.asList(JavaModifier.FINAL))
                            .type(converterFunctionParameterType(javaMethodDefinitionFactory,
                                                                 ensembleKind,
                                                                 ensembleInterfaceSuperType,
                                                                 returnTypeVariable))
                            .build();
    }

    private static JavaType converterFunctionParameterType(final JavaDefinitionFactory<JavaMethod> javaMethodDefinitionFactory,
                                                           final EnsembleKind ensembleKind,
                                                           final JavaType ensembleInterfaceSuperType,
                                                           final JavaType returnTypeVariable) {
        if (ensembleKind == EnsembleKind.SOLO) {
            return javaMethodDefinitionFactory.covariantParameterizedFunctionJavaType(Function.class,
                                                                                      soloEnsembleInterfaceTypeCreator(javaMethodDefinitionFactory),
                                                                                      returnTypeVariable);
        } else {
            return javaMethodDefinitionFactory.covariantParameterizedFunctionJavaType(Function.class,
                                                                                      ensembleInterfaceSuperType,
                                                                                      returnTypeVariable);
        }
    }

    private static <D> JavaType soloEnsembleInterfaceTypeCreator(final JavaDefinitionFactory<D> javaDefinitionFactory) {
        return javaDefinitionFactory.parameterizedJavaType(FUNCIFY_ENSEMBLE_PACKAGE_NAME,
                                                           EnsembleKind.SOLO.getSimpleClassName(),
                                                           Stream.concat(Stream.of(WITNESS_TYPE_VARIABLE),
                                                                         firstNSimpleJavaTypeVariables(1))
                                                                 .collect(Collectors.toList()));
    }

    private static <D> BinaryOperator<JavaType> nestingSoloTypeVariableCreator(final JavaDefinitionFactory<D> javaMethodDefinitionFactory) {
        return (jt1, jt2) -> javaMethodDefinitionFactory.parameterizedJavaType(FUNCIFY_ENSEMBLE_PACKAGE_NAME,
                                                                               jt1.getName(),
                                                                               jt1,
                                                                               jt2);
    }

    private static <D, T> Function<D, D> ap(final BiFunction<D, T, D> updater,
                                            final T value) {
        return (D d) -> {
            return updater.apply(d,
                                 value);
        };
    }

    private static Stream<JavaType> firstNSimpleJavaTypeVariables(int n) {
        return firstNLetters(n).map(SimpleJavaTypeVariable::of);
    }

    private static Stream<String> firstNLetters(int n) {
        return rangeOfCharactersFrom('A',
                                     (char) (((int) 'A') + Math.min(26,
                                                                    n)));
    }

    private static Stream<String> rangeOfCharactersFrom(char start,
                                                        char end) {
        if (start == end) {
            return Stream.of(String.valueOf(start));
        }
        if ((start - end) > 0) {
            return Stream.empty();
        } else {
            return Stream.iterate(start,
                                  (Character c) -> {
                                      return (char) (((int) c) + 1);
                                  })
                         .limit(end - start)
                         .map(String::valueOf);
        }
    }

}
