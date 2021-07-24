package funcify.base.session;

import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import funcify.typedef.JavaAnnotation;
import funcify.typedef.JavaField;
import funcify.typedef.JavaImport;
import funcify.typedef.JavaMethod;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaPackage;
import funcify.typedef.JavaTypeDefinition;
import funcify.typedef.JavaTypeKind;
import funcify.typedef.javatype.JavaType;
import java.util.Objects;
import java.util.Optional;

/**
 * The session tracks what types are being generated and how these typeDefs can be updated
 *
 * @param <SWT> - Session Witness Type
 * @author smccarron
 * @created 2021-05-28
 */
public interface TypeGenerationSession<SWT> extends MethodGenerationSession<SWT> {

    JavaTypeDefinition emptyTypeDefinition();

    default Optional<JavaTypeDefinition> findTypeDefinitionWithName(final String name) {
        return getTypeDefinitionsByName().get(Objects.requireNonNull(name,
                                                                     () -> "name"));
    }

    SyncMap<String, JavaTypeDefinition> getTypeDefinitionsByName();

    JavaTypeDefinition typeName(final JavaTypeDefinition typeDef,
                                final String name);

    JavaTypeDefinition typeDefinitionTypeVariables(final JavaTypeDefinition typeDef,
                                                   final SyncList<JavaType> typeVariables);

    JavaTypeDefinition javaPackage(final JavaTypeDefinition typeDef,
                                   final JavaPackage javaPackage);

    JavaTypeDefinition javaImports(final JavaTypeDefinition typeDef,
                                   final SyncList<JavaImport> javaImport);

    JavaTypeDefinition typeAnnotations(final JavaTypeDefinition typeDef,
                                       final SyncList<JavaAnnotation> javaAnnotations);

    JavaTypeDefinition typeModifiers(final JavaTypeDefinition typeDef,
                                     final SyncList<JavaModifier> modifiers);

    JavaTypeDefinition typeKind(final JavaTypeDefinition typeDef,
                                final JavaTypeKind typeKind);

    JavaTypeDefinition superType(final JavaTypeDefinition typeDef,
                                 final JavaType superType);

    JavaTypeDefinition implementedInterfaceTypes(final JavaTypeDefinition typeDef,
                                                 final SyncList<JavaType> implementedInterfaceTypes);


    JavaTypeDefinition fields(final JavaTypeDefinition typeDef,
                              final SyncList<JavaField> fields);

    JavaTypeDefinition methods(final JavaTypeDefinition typeDef,
                               final SyncList<JavaMethod> methods);

    JavaTypeDefinition subTypeDefinitions(final JavaTypeDefinition typeDef,
                                          final SyncList<JavaTypeDefinition> subTypeDefinitions);

    JavaType javaTypeOfTypeDefinition(final JavaTypeDefinition typeDef);

}
