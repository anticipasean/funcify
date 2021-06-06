package funcify;

import funcify.tool.container.SyncList;
import funcify.typedef.JavaAnnotation;
import funcify.typedef.JavaCodeBlock;
import funcify.typedef.JavaMethod;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaParameter;
import funcify.typedef.javatype.JavaType;

/**
 * @author smccarron
 * @created 2021-05-24
 */
public interface JavaMethodFactory extends JavaDefinitionFactory<JavaMethod> {

    static JavaDefinitionFactory<JavaMethod> getInstance() {
        return new JavaMethodFactory() {
        };
    }

    @Override
    default JavaMethod name(final JavaMethod definition,
                            final String name) {
        return definition.withName(name);
    }

    @Override
    default JavaMethod javaAnnotations(final JavaMethod definition,
                                       final SyncList<JavaAnnotation> javaAnnotations) {
        return definition.withAnnotations(definition.getAnnotations()
                                                    .appendAll(javaAnnotations));
    }

    @Override
    default JavaMethod modifiers(final JavaMethod javaMethod,
                                 final SyncList<JavaModifier> modifiers) {
        return javaMethod.withModifiers(javaMethod.getModifiers()
                                                  .appendAll(modifiers));
    }

    @Override
    default JavaMethod typeVariables(final JavaMethod javaMethod,
                                     final SyncList<JavaType> typeVariables) {
        return javaMethod.withTypeVariables(javaMethod.getTypeVariables()
                                                      .appendAll(typeVariables));
    }

    @Override
    default JavaMethod returnType(final JavaMethod javaMethod,
                                  final JavaType returnType) {
        return javaMethod.withReturnType(returnType);
    }

    @Override
    default JavaMethod parameters(final JavaMethod javaMethod,
                                  final SyncList<JavaParameter> parameters) {
        return javaMethod.withParameters(javaMethod.getParameters()
                                                   .appendAll(parameters));
    }

    @Override
    default JavaMethod codeBlock(final JavaMethod javaMethod,
                                 final JavaCodeBlock codeBlock) {
        return javaMethod.withCodeBlock(codeBlock);
    }


}
