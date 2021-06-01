package funcify.template.session;

import funcify.tool.SyncMap;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaParameter;
import funcify.typedef.javatype.JavaType;
import java.util.List;
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

    TD methodModifiers(final MD methodDef,
                       final List<JavaModifier> modifiers);

    MD methodTypeVariables(final MD methodDef,
                           final List<JavaType> typeVariables);

    MD returnType(final MD methodDef,
                  final JavaType returnType);

    MD methodName(final MD methodDef,
                  final String name);

    MD parameters(final MD methodDef,
                  final List<JavaParameter> parameters);

    MD codeBlock(final MD methodDef,
                 final CD codeBlock);
}
