package funcify.base.template;

import funcify.base.session.TypeGenerationSession;
import funcify.tool.container.SyncList;
import funcify.typedef.JavaAnnotation;
import funcify.typedef.JavaCodeBlock;
import funcify.typedef.JavaMethod;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaParameter;
import funcify.typedef.javatype.JavaType;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface MethodGenerationTemplate<SWT> extends CodeBlockGenerationTemplate<SWT> {

    default JavaMethod emptyMethodDefinition(final TypeGenerationSession<SWT> session) {
        return session.emptyMethodDefinition();
    }

    default JavaMethod methodAnnotations(final TypeGenerationSession<SWT> session,
                                         final JavaMethod methodDef,
                                         final SyncList<JavaAnnotation> javaAnnotations) {
        return session.methodAnnotations(methodDef,
                                         javaAnnotations);
    }

    default JavaMethod methodAnnotation(final TypeGenerationSession<SWT> session,
                                        final JavaMethod methodDef,
                                        final JavaAnnotation... annotation) {
        return methodAnnotations(session,
                                 methodDef,
                                 SyncList.of(annotation));
    }

    default JavaMethod methodModifiers(final TypeGenerationSession<SWT> session,
                                       final JavaMethod methodDef,
                                       final SyncList<JavaModifier> modifiers) {
        return session.methodModifiers(methodDef,
                                       modifiers);
    }

    default JavaMethod methodModifier(final TypeGenerationSession<SWT> session,
                                      final JavaMethod methodDef,
                                      final JavaModifier... modifier) {
        return methodModifiers(session,
                               methodDef,
                               SyncList.of(modifier));
    }

    default JavaMethod methodTypeVariable(final TypeGenerationSession<SWT> session,
                                          final JavaMethod methodDef,
                                          final JavaType... typeVariable) {
        return methodTypeVariables(session,
                                   methodDef,
                                   SyncList.of(typeVariable));
    }

    default JavaMethod methodTypeVariables(final TypeGenerationSession<SWT> session,
                                           final JavaMethod methodDef,
                                           final SyncList<JavaType> typeVariables) {
        return session.methodTypeVariables(methodDef,
                                           typeVariables);
    }

    default JavaMethod returnType(final TypeGenerationSession<SWT> session,
                                  final JavaMethod methodDef,
                                  final JavaType returnType) {
        return session.returnType(methodDef,
                                  returnType);
    }

    default JavaMethod methodName(final TypeGenerationSession<SWT> session,
                                  final JavaMethod methodDef,
                                  final String name) {
        return session.methodName(methodDef,
                                  name);
    }

    default JavaMethod parameter(final TypeGenerationSession<SWT> session,
                                 final JavaMethod methodDef,
                                 final JavaParameter... parameter) {
        return parameters(session,
                          methodDef,
                          SyncList.of(parameter));
    }

    default JavaMethod parameters(final TypeGenerationSession<SWT> session,
                                  final JavaMethod methodDef,
                                  final SyncList<JavaParameter> parameters) {
        return session.parameters(methodDef,
                                  parameters);
    }

    default JavaMethod codeBlock(final TypeGenerationSession<SWT> session,
                                 final JavaMethod methodDef,
                                 final JavaCodeBlock codeBlock) {
        return session.codeBlock(methodDef,
                                 codeBlock);
    }


}
