package funcify.base.template;

import funcify.tool.container.SyncList;
import funcify.typedef.JavaPackage;
import funcify.typedef.javatype.BoundedJavaTypeVariable;
import funcify.typedef.javatype.JavaType;
import funcify.typedef.javatype.ParametricJavaType;
import funcify.typedef.javatype.SimpleJavaType;
import funcify.typedef.javatype.SimpleJavaTypeVariable;
import funcify.typedef.javatype.WildcardJavaTypeBound;

/**
 * @author smccarron
 * @created 2021-05-24
 */
public interface JavaTypeGenerationTemplate {

    default SyncList<JavaType> typeVariablesForJavaType(final JavaType javaType) {
        return javaType instanceof ParametricJavaType ? ((ParametricJavaType) javaType).getTypeVariables() : SyncList.empty();
    }

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
                                                 final String typeVariable) {
        return parameterizedJavaType(JavaPackage.builder()
                                                .name(javaPackage)
                                                .build(),
                                     name,
                                     SyncList.of(typeVariable)
                                             .map(SimpleJavaTypeVariable::of));
    }


    default JavaType simpleParameterizedJavaType(final JavaPackage javaPackage,
                                                 final String name,
                                                 final String typeVariable) {
        return parameterizedJavaType(javaPackage,
                                     name,
                                     SyncList.of(typeVariable)
                                             .map(SimpleJavaTypeVariable::of));
    }

    default JavaType parameterizedJavaType(final String javaPackage,
                                           final String name,
                                           final JavaType typeVariable) {
        return parameterizedJavaType(JavaPackage.builder()
                                                .name(javaPackage)
                                                .build(),
                                     name,
                                     typeVariable);
    }

    default JavaType parameterizedJavaType(final JavaPackage javaPackage,
                                           final String name,
                                           final JavaType typeVariable) {
        return parameterizedJavaType(javaPackage,
                                     name,
                                     SyncList.of(typeVariable));
    }

    default JavaType parameterizedJavaType(final String javaPackage,
                                           final String name,
                                           final SyncList<JavaType> typeVariableList) {
        return ParametricJavaType.builder()
                                 .javaPackage(JavaPackage.builder()
                                                         .name(javaPackage)
                                                         .build())
                                 .name(name)
                                 .typeVariables(typeVariableList)
                                 .build();
    }

    default JavaType parameterizedJavaType(final JavaPackage javaPackage,
                                           final String name,
                                           final SyncList<JavaType> typeVariableList) {
        return ParametricJavaType.builder()
                                 .javaPackage(javaPackage)
                                 .name(name)
                                 .typeVariables(typeVariableList)
                                 .build();
    }

    default JavaType parameterizedWildcardJavaType(final JavaPackage javaPackage,
                                                   final String name) {
        return parameterizedJavaType(javaPackage,
                                     name,
                                     SyncList.of(WildcardJavaTypeBound.getInstance()));
    }

    default JavaType javaTypeVariableWithLowerBounds(final JavaType baseTypeVariable,
                                                     final SyncList<JavaType> lowerBounds) {
        return BoundedJavaTypeVariable.builder()
                                      .baseType(baseTypeVariable)
                                      .lowerBoundTypes(lowerBounds)
                                      .build();
    }

    default JavaType javaTypeVariableWithUpperBounds(final JavaType baseTypeVariable,
                                                     final SyncList<JavaType> upperBounds) {
        return BoundedJavaTypeVariable.builder()
                                      .baseType(baseTypeVariable)
                                      .upperBoundTypes(upperBounds)
                                      .build();
    }

    default JavaType javaTypeVariableWithLowerBound(final JavaType baseTypeVariable,
                                                    final JavaType lowerBound) {
        return BoundedJavaTypeVariable.builder()
                                      .baseType(baseTypeVariable)
                                      .lowerBoundTypes(SyncList.of(lowerBound))
                                      .build();
    }

    default JavaType javaTypeVariableWithUpperBound(final JavaType baseTypeVariable,
                                                    final JavaType upperBound) {
        return BoundedJavaTypeVariable.builder()
                                      .baseType(baseTypeVariable)
                                      .upperBoundTypes(SyncList.of(upperBound))
                                      .build();
    }

    default JavaType javaTypeVariableWithLowerAndUpperBounds(final JavaType baseTypeVariable,
                                                             final SyncList<JavaType> lowerBounds,
                                                             final SyncList<JavaType> upperBounds) {
        return BoundedJavaTypeVariable.builder()
                                      .baseType(baseTypeVariable)
                                      .lowerBoundTypes(lowerBounds)
                                      .upperBoundTypes(upperBounds)
                                      .build();
    }

    default JavaType javaTypeVariableWithWildcardLowerBounds(final JavaType lowerBound) {
        return javaTypeVariableWithLowerBounds(WildcardJavaTypeBound.getInstance(),
                                               SyncList.of(lowerBound));
    }

    default JavaType javaTypeVariableWithWildcardUpperBounds(final JavaType upperBound) {
        return javaTypeVariableWithUpperBounds(WildcardJavaTypeBound.getInstance(),
                                               SyncList.of(upperBound));
    }

    default JavaType javaTypeVariableWithWildcardLowerBounds(final SyncList<JavaType> lowerBounds) {
        return javaTypeVariableWithLowerBounds(WildcardJavaTypeBound.getInstance(),
                                               lowerBounds);
    }

    default JavaType javaTypeVariableWithWildcardUpperBounds(final SyncList<JavaType> upperBounds) {
        return javaTypeVariableWithUpperBounds(WildcardJavaTypeBound.getInstance(),
                                               upperBounds);
    }

    default JavaType covariantParameterizedFunctionJavaType(final Class<?> cls,
                                                            final SyncList<JavaType> typeVariables) {
        return covariantParameterizedFunctionJavaType(JavaPackage.builder()
                                                                 .name(cls.getPackage()
                                                                          .getName())
                                                                 .build(),
                                                      cls.getSimpleName(),
                                                      typeVariables);
    }

    default JavaType covariantParameterizedFunctionJavaType(final String javaPackage,
                                                            final String name,
                                                            final SyncList<JavaType> typeVariables) {
        return covariantParameterizedFunctionJavaType(JavaPackage.builder()
                                                                 .name(javaPackage)
                                                                 .build(),
                                                      name,
                                                      typeVariables);
    }

    default JavaType covariantParameterizedFunctionJavaType(final JavaPackage javaPackage,
                                                            final String name,
                                                            final SyncList<JavaType> typeVariables) {
        final int size = typeVariables.size();
        final SyncList<JavaType> javaTypeVariablesList = SyncList.empty();
        if (size == 0) {
            return javaType(javaPackage,
                            name);
        } else {
            for (int i = 0; i < size; i++) {
                if (i != (size - 1)) {
                    javaTypeVariablesList.append(javaTypeVariableWithWildcardLowerBounds(typeVariables.getOrElse(i,
                                                                                                                 null)));
                } else {
                    javaTypeVariablesList.append(javaTypeVariableWithWildcardUpperBounds(typeVariables.getOrElse(i,
                                                                                                                 null)));
                }
            }
        }
        return parameterizedJavaType(javaPackage,
                                     name,
                                     javaTypeVariablesList);
    }

}
