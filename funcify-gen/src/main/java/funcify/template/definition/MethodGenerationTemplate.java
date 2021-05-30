package funcify.template.definition;

import static java.util.Arrays.asList;

import funcify.template.session.TypeGenerationSession;
import funcify.typedef.JavaCodeBlock;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaParameter;
import funcify.typedef.javatype.JavaType;
import java.util.List;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface MethodGenerationTemplate<SWT> extends CodeBlockGenerationTemplate<SWT> {

    <TD, MD, CD, SD, ED> MD emptyMethodDefinition(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session);

    <TD, MD, CD, SD, ED> TD methodModifiers(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                            final MD methodDef,
                                            final List<JavaModifier> modifiers);

    default <TD, MD, CD, SD, ED> TD methodModifier(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                    final MD methodDef,
                                                    final JavaModifier... modifier) {
        return methodModifiers(session,
                               methodDef,
                               asList(modifier));
    }

    default <TD, MD, CD, SD, ED> MD typeVariable(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                 final MD methodDef,
                                                 final JavaType... typeVariable) {
        return typeVariables(session,
                             methodDef,
                             asList(typeVariable));
    }

    <TD, MD, CD, SD, ED> MD typeVariables(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                          final MD methodDef,
                                          final List<JavaType> typeVariables);

    <TD, MD, CD, SD, ED> MD returnType(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                       final MD methodDef,
                                       final JavaType returnType);

    <TD, MD, CD, SD, ED> MD methodName(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                       final MD methodDef,
                                       final String name);

    default <TD, MD, CD, SD, ED> MD parameter(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                              final MD methodDef,
                                              final JavaParameter... parameter) {
        return parameters(session,
                          methodDef,
                          asList(parameter));
    }

    <TD, MD, CD, SD, ED> MD parameters(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                       final MD methodDef,
                                       final List<JavaParameter> parameters);

    <TD, MD, CD, SD, ED> MD codeBlock(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                      final MD methodDef,
                                      final JavaCodeBlock codeBlock);


}
