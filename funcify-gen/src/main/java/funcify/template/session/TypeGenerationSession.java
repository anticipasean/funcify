package funcify.template.session;

import funcify.tool.SyncList;
import funcify.tool.SyncMap;
import funcify.typedef.JavaAnnotation;
import funcify.typedef.JavaField;
import funcify.typedef.JavaImport;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaPackage;
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
public interface TypeGenerationSession<SWT, TD, MD, CD, SD, ED> extends MethodGenerationSession<SWT, TD, MD, CD, SD, ED> {

    TD emptyTypeDefinition();

    default Optional<TD> findTypeDefinitionWithName(final String name) {
        return getTypeDefinitionsByName().get(Objects.requireNonNull(name,
                                                                     () -> "name"));
    }

    SyncMap<String, TD> getTypeDefinitionsByName();

    TD typeName(final TD typeDef,
                final String name);

    TD javaPackage(final TD typeDef,
                   final JavaPackage javaPackage);

    TD javaImports(final TD typeDef,
                   final SyncList<JavaImport> javaImport);

    TD javaAnnotations(final TD typeDef,
                       final SyncList<JavaAnnotation> javaAnnotations);

    TD typeModifiers(final TD typeDef,
                     final SyncList<JavaModifier> modifiers);

    TD typeKind(final TD typeDef,
                final JavaTypeKind typeKind);

    TD superType(final TD typeDef,
                 final JavaType superType);

    TD implementedInterfaceTypes(final TD typeDef,
                                 final SyncList<JavaType> implementedInterfaceTypes);


    TD fields(final TD typeDef,
              final SyncList<JavaField> fields);

    TD methods(final TD typeDef,
               final SyncList<MD> methods);

    TD subTypeDefinitions(final TD typeDef,
                          final SyncList<TD> subTypeDefinitions);


}
