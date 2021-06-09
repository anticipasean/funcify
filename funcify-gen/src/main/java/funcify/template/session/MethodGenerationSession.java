package funcify.template.session;

import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import funcify.typedef.JavaAnnotation;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaParameter;
import funcify.typedef.javatype.JavaType;
import java.util.Objects;
import java.util.Optional;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface MethodGenerationSession<SWT, TD, MD, CD, SD, ED> extends CodeBlockGenerationSession<SWT, TD, MD, CD, SD, ED> {

    MD emptyMethodDefinition();

    SyncMap<String, MD> getMethodDefinitionsByName(final TD typeDef);

    default Optional<MD> findMethodDefinitionWithName(final TD typeDef,
                                                      final String name) {
        return getMethodDefinitionsByName(typeDef).get(Objects.requireNonNull(name,
                                                                              () -> "name"));
    }

    MD methodAnnotations(final MD methodDef,
                         final SyncList<JavaAnnotation> javaAnnotations);

    MD methodModifiers(final MD methodDef,
                       final SyncList<JavaModifier> modifiers);

    MD methodTypeVariables(final MD methodDef,
                           final SyncList<JavaType> typeVariables);

    MD returnType(final MD methodDef,
                  final JavaType returnType);

    MD methodName(final MD methodDef,
                  final String name);

    MD parameters(final MD methodDef,
                  final SyncList<JavaParameter> parameters);

    MD codeBlock(final MD methodDef,
                 final CD codeBlock);
}
