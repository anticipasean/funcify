package funcify;

import funcify.typedef.JavaAnnotation;
import funcify.typedef.JavaCodeBlock;
import funcify.typedef.JavaMethod;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaParameter;
import funcify.typedef.javatype.JavaType;
import java.util.List;

/**
 * @author smccarron
 * @created 2021-05-24
 */
public interface JavaMethodFactory extends JavaDefinitionFactory<JavaMethod> {

    public static JavaDefinitionFactory<JavaMethod> getInstance() {
        return new JavaMethodFactory() {
        };
    }

    @Override
    default JavaMethod name(final String name) {
        return JavaMethod.builder()
                         .name(name)
                         .build();
    }

    @Override
    default JavaMethod javaAnnotations(final JavaMethod definition,
                                       final List<JavaAnnotation> javaAnnotations) {
        final boolean added = definition.getAnnotations()
                                        .addAll(javaAnnotations);
        return definition.withAnnotations(definition.getAnnotations());
    }

    @Override
    default JavaMethod modifiers(final JavaMethod javaMethod,
                                 final List<JavaModifier> modifiers) {
        final boolean added = javaMethod.getModifiers()
                                        .addAll(modifiers);
        return javaMethod.withModifiers(javaMethod.getModifiers());
    }

    @Override
    default JavaMethod typeVariables(final JavaMethod javaMethod,
                                     final List<JavaType> typeVariables) {
        final boolean added = javaMethod.getTypeVariables()
                                        .addAll(typeVariables);
        return javaMethod.withTypeVariables(javaMethod.getTypeVariables());
    }

    @Override
    default JavaMethod returnType(final JavaMethod javaMethod,
                                  final JavaType returnType) {
        return javaMethod.withReturnType(returnType);
    }

    @Override
    default JavaMethod parameters(final JavaMethod javaMethod,
                                  final List<JavaParameter> parameters) {
        final boolean added = javaMethod.getParameters()
                                        .addAll(parameters);
        return javaMethod.withParameters(javaMethod.getParameters());
    }

    @Override
    default JavaMethod codeBlock(final JavaMethod javaMethod,
                                 final JavaCodeBlock codeBlock) {
        return javaMethod.withCodeBlock(codeBlock);
    }


}
