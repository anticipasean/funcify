package funcify.template.generation;

import funcify.template.session.TypeGenerationSession;
import funcify.tool.SyncList;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaParameter;
import funcify.typedef.javatype.JavaType;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface MethodGenerationTemplate<SWT> extends CodeBlockGenerationTemplate<SWT> {

    default <TD, MD, CD, SD, ED> MD emptyMethodDefinition(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session) {
        return session.emptyMethodDefinition();
    }

    default <TD, MD, CD, SD, ED> TD methodModifiers(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                    final MD methodDef,
                                                    final SyncList<JavaModifier> modifiers) {
        return session.methodModifiers(methodDef,
                                       modifiers);
    }

    default <TD, MD, CD, SD, ED> TD methodModifier(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                   final MD methodDef,
                                                   final JavaModifier... modifier) {
        return methodModifiers(session,
                               methodDef,
                               SyncList.of(modifier));
    }

    default <TD, MD, CD, SD, ED> MD methodTypeVariable(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                       final MD methodDef,
                                                       final JavaType... typeVariable) {
        return methodTypeVariables(session,
                                   methodDef,
                                   SyncList.of(typeVariable));
    }

    default <TD, MD, CD, SD, ED> MD methodTypeVariables(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                        final MD methodDef,
                                                        final SyncList<JavaType> typeVariables) {
        return session.methodTypeVariables(methodDef,
                                           typeVariables);
    }

    default <TD, MD, CD, SD, ED> MD returnType(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                               final MD methodDef,
                                               final JavaType returnType) {
        return session.returnType(methodDef,
                                  returnType);
    }

    default <TD, MD, CD, SD, ED> MD methodName(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                               final MD methodDef,
                                               final String name) {
        return session.methodName(methodDef,
                                  name);
    }

    default <TD, MD, CD, SD, ED> MD parameter(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                              final MD methodDef,
                                              final JavaParameter... parameter) {
        return parameters(session,
                          methodDef,
                          SyncList.of(parameter));
    }

    default <TD, MD, CD, SD, ED> MD parameters(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                               final MD methodDef,
                                               final SyncList<JavaParameter> parameters) {
        return session.parameters(methodDef,
                                  parameters);
    }

    default <TD, MD, CD, SD, ED> MD codeBlock(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                              final MD methodDef,
                                              final CD codeBlock) {
        return session.codeBlock(methodDef,
                                 codeBlock);
    }


}
