package funcify;

import funcify.typedef.JavaPackage;
import funcify.typedef.javatype.BoundedJavaTypeVariable;
import funcify.typedef.javatype.JavaType;
import funcify.typedef.javatype.SimpleJavaType;
import funcify.typedef.javatype.SimpleJavaTypeVariable;
import funcify.typedef.javatype.VariableParameterJavaType;
import funcify.typedef.javatype.WildcardJavaTypeBound;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author smccarron
 * @created 2021-05-24
 */
public interface JavaTypeFactory {

    default JavaType javaType(final Class<?> cls) {
        return javaType(JavaPackage.builder()
                                   .name(cls.getPackage()
                                            .getName())
                                   .build(),
                        cls.getSimpleName());
    }

    default JavaType javaType(final String javaPackage,
                              final String name) {
        return javaType(JavaPackage.builder()
                                   .name(javaPackage)
                                   .build(),
                        name);
    }

    default JavaType javaType(final JavaPackage javaPackage,
                              final String name) {
        return SimpleJavaType.builder()
                             .javaPackage(javaPackage)
                             .name(name)
                             .build();
    }

    default JavaType simpleJavaTypeVariable(final String name) {
        return SimpleJavaTypeVariable.of(name);
    }

    default JavaType simpleParameterizedJavaType(final String javaPackage,
                                                 final String name,
                                                 final String... typeVariable) {
        return parameterizedJavaType(JavaPackage.builder()
                                                .name(javaPackage)
                                                .build(),
                                     name,
                                     Stream.of(typeVariable)
                                           .map(SimpleJavaTypeVariable::of)
                                           .collect(Collectors.<JavaType>toList()));
    }


    default JavaType simpleParameterizedJavaType(final JavaPackage javaPackage,
                                                 final String name,
                                                 final String... typeVariable) {
        return parameterizedJavaType(javaPackage,
                                     name,
                                     Stream.of(typeVariable)
                                           .map(SimpleJavaTypeVariable::of)
                                           .collect(Collectors.toList()));
    }

    default JavaType parameterizedJavaType(final String javaPackage,
                                           final String name,
                                           final JavaType... typeVariable) {
        return parameterizedJavaType(JavaPackage.builder()
                                                .name(javaPackage)
                                                .build(),
                                     name,
                                     typeVariable);
    }

    default JavaType parameterizedJavaType(final JavaPackage javaPackage,
                                           final String name,
                                           final JavaType... typeVariable) {
        return parameterizedJavaType(javaPackage,
                                     name,
                                     Stream.of(typeVariable)
                                           .collect(Collectors.toList()));
    }

    default JavaType parameterizedJavaType(final String javaPackage,
                                           final String name,
                                           final List<JavaType> typeVariableList) {
        return VariableParameterJavaType.builder()
                                        .javaPackage(JavaPackage.builder()
                                                                .name(javaPackage)
                                                                .build())
                                        .name(name)
                                        .typeVariables(typeVariableList)
                                        .build();
    }

    default JavaType parameterizedJavaType(final JavaPackage javaPackage,
                                           final String name,
                                           final List<JavaType> typeVariableList) {
        return VariableParameterJavaType.builder()
                                        .javaPackage(javaPackage)
                                        .name(name)
                                        .typeVariables(typeVariableList)
                                        .build();
    }

    default JavaType parameterizedWildcardJavaType(final JavaPackage javaPackage,
                                                   final String name) {
        return parameterizedJavaType(javaPackage,
                                     name,
                                     Stream.of(WildcardJavaTypeBound.getInstance())
                                           .collect(Collectors.toList()));
    }

    default JavaType javaTypeVariableWithLowerBounds(final JavaType baseTypeVariable,
                                                     final JavaType... lowerBound) {
        return BoundedJavaTypeVariable.builder()
                                      .baseType(baseTypeVariable)
                                      .lowerBoundTypes(Stream.of(lowerBound)
                                                             .collect(Collectors.toList()))
                                      .build();
    }

    default JavaType javaTypeVariableWithUpperBounds(final JavaType baseTypeVariable,
                                                     final JavaType... upperBound) {
        return BoundedJavaTypeVariable.builder()
                                      .baseType(baseTypeVariable)
                                      .upperBoundTypes(Stream.of(upperBound)
                                                             .collect(Collectors.toList()))
                                      .build();
    }

    default JavaType javaTypeVariableWithLowerAndUpperBounds(final JavaType baseTypeVariable,
                                                             final List<JavaType> lowerBounds,
                                                             final List<JavaType> upperBounds) {
        return BoundedJavaTypeVariable.builder()
                                      .baseType(baseTypeVariable)
                                      .lowerBoundTypes(lowerBounds)
                                      .upperBoundTypes(upperBounds)
                                      .build();
    }

    default JavaType javaTypeVariableWithWildcardLowerBounds(final JavaType... lowerBounds) {
        return javaTypeVariableWithLowerBounds(WildcardJavaTypeBound.getInstance(),
                                               lowerBounds);
    }

    default JavaType javaTypeVariableWithWildcardUpperBounds(final JavaType... upperBounds) {
        return javaTypeVariableWithUpperBounds(WildcardJavaTypeBound.getInstance(),
                                               upperBounds);
    }

    default JavaType covariantParameterizedFunctionJavaType(final Class<?> cls,
                                                            final JavaType... typeVariable) {
        return covariantParameterizedFunctionJavaType(JavaPackage.builder()
                                                                 .name(cls.getPackage()
                                                                          .getName())
                                                                 .build(),
                                                      cls.getSimpleName(),
                                                      typeVariable);
    }

    default JavaType covariantParameterizedFunctionJavaType(final String javaPackage,
                                                            final String name,
                                                            final JavaType... typeVariable) {
        return covariantParameterizedFunctionJavaType(JavaPackage.builder()
                                                                 .name(javaPackage)
                                                                 .build(),
                                                      name,
                                                      typeVariable);
    }

    default JavaType covariantParameterizedFunctionJavaType(final JavaPackage javaPackage,
                                                            final String name,
                                                            final JavaType... typeVariable) {
        final int size = typeVariable.length;
        final Stream.Builder<JavaType> javaTypeStreamBuilder = Stream.builder();
        if (size == 0) {
            return javaType(javaPackage,
                            name);
        } else if (size == 1) {
            javaTypeStreamBuilder.add(javaTypeVariableWithLowerBounds(typeVariable[0]));
        } else {
            for (int i = 0; i < size; i++) {
                if (i != (size - 1)) {
                    javaTypeStreamBuilder.add(javaTypeVariableWithWildcardLowerBounds(typeVariable[i]));
                } else {
                    javaTypeStreamBuilder.add(javaTypeVariableWithWildcardUpperBounds(typeVariable[i]));
                }
            }
        }
        return parameterizedJavaType(javaPackage,
                                     name,
                                     javaTypeStreamBuilder.build()
                                                          .collect(Collectors.toList()));
    }

}
