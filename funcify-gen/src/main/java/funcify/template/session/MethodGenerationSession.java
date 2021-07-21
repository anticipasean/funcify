package funcify.template.session;

import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import funcify.typedef.JavaAnnotation;
import funcify.typedef.JavaCodeBlock;
import funcify.typedef.JavaMethod;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaParameter;
import funcify.typedef.JavaTypeDefinition;
import funcify.typedef.javatype.JavaType;
import java.util.Objects;
import java.util.Optional;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface MethodGenerationSession<SWT> extends CodeBlockGenerationSession<SWT> {

    JavaMethod emptyMethodDefinition();

    SyncMap<String, JavaMethod> getMethodDefinitionsByName(final JavaTypeDefinition typeDef);

    default Optional<JavaMethod> findMethodDefinitionWithName(final JavaTypeDefinition typeDef,
                                                              final String name) {
        return getMethodDefinitionsByName(typeDef).get(Objects.requireNonNull(name,
                                                                              () -> "name"));
    }

    JavaMethod methodAnnotations(final JavaMethod methodDef,
                                 final SyncList<JavaAnnotation> javaAnnotations);

    JavaMethod methodModifiers(final JavaMethod methodDef,
                               final SyncList<JavaModifier> modifiers);

    JavaMethod methodTypeVariables(final JavaMethod methodDef,
                                   final SyncList<JavaType> typeVariables);

    JavaMethod returnType(final JavaMethod methodDef,
                          final JavaType returnType);

    JavaMethod methodName(final JavaMethod methodDef,
                          final String name);

    JavaMethod parameters(final JavaMethod methodDef,
                          final SyncList<JavaParameter> parameters);

    JavaMethod codeBlock(final JavaMethod methodDef,
                         final JavaCodeBlock codeBlock);
}
