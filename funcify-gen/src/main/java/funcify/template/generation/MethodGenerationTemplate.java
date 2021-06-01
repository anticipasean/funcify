package funcify.template.generation;

import static java.util.Arrays.asList;

import funcify.template.session.TypeGenerationSession;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaParameter;
import funcify.typedef.javatype.JavaType;
import java.util.List;

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
                                                    final List<JavaModifier> modifiers) {
        return session.methodModifiers(methodDef,
                                       modifiers);
    }

    default <TD, MD, CD, SD, ED> TD methodModifier(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                   final MD methodDef,
                                                   final JavaModifier... modifier) {
        return methodModifiers(session,
                               methodDef,
                               asList(modifier));
    }

    default <TD, MD, CD, SD, ED> MD methodTypeVariable(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                       final MD methodDef,
                                                       final JavaType... typeVariable) {
        return methodTypeVariables(session,
                                   methodDef,
                                   asList(typeVariable));
    }

    default <TD, MD, CD, SD, ED> MD methodTypeVariables(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                        final MD methodDef,
                                                        final List<JavaType> typeVariables) {
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
                          asList(parameter));
    }

    default <TD, MD, CD, SD, ED> MD parameters(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                               final MD methodDef,
                                               final List<JavaParameter> parameters) {
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
