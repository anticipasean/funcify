package funcify.template.generation;

import funcify.template.session.TypeGenerationSession;
import funcify.tool.container.SyncList;
import funcify.typedef.JavaAnnotation;
import funcify.typedef.JavaField;
import funcify.typedef.JavaImport;
import funcify.typedef.JavaMethod;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaPackage;
import funcify.typedef.JavaTypeDefinition;
import funcify.typedef.JavaTypeKind;
import funcify.typedef.javatype.JavaType;

/**
 * The template provides the framework for the updates that can be made to the session and what the session can and should provide
 * to those using the framework
 *
 * @param <SWT> - Session Witness Type
 * @author smccarron
 * @created 2021-05-28
 */
public interface TypeGenerationTemplate<SWT> extends MethodGenerationTemplate<SWT> {

    default JavaTypeDefinition emptyTypeDefinition(final TypeGenerationSession<SWT> session) {
        return session.emptyTypeDefinition();
    }

    default JavaTypeDefinition typeName(final TypeGenerationSession<SWT> session,
                                        final JavaTypeDefinition typeDef,
                                        final String name) {
        return session.typeName(typeDef,
                                name);
    }

    default JavaTypeDefinition typeDefinitionTypeVariables(final TypeGenerationSession<SWT> session,
                                                           final JavaTypeDefinition typeDef,
                                                           final SyncList<JavaType> typeVariables) {
        return session.typeDefinitionTypeVariables(typeDef,
                                                   typeVariables);
    }

    default JavaTypeDefinition typeDefinitionTypeVariable(final TypeGenerationSession<SWT> session,
                                                          final JavaTypeDefinition typeDef,
                                                          final JavaType... typeVariable) {
        return session.typeDefinitionTypeVariables(typeDef,
                                                   SyncList.of(typeVariable));
    }

    default JavaTypeDefinition javaPackage(final TypeGenerationSession<SWT> session,
                                           final JavaTypeDefinition typeDef,
                                           final String javaPackage) {
        return javaPackage(session,
                           typeDef,
                           JavaPackage.builder()
                                      .name(javaPackage)
                                      .build());
    }

    default JavaTypeDefinition javaPackage(final TypeGenerationSession<SWT> session,
                                           final JavaTypeDefinition typeDef,
                                           final JavaPackage javaPackage) {
        return session.javaPackage(typeDef,
                                   javaPackage);
    }

    default JavaTypeDefinition javaImport(final TypeGenerationSession<SWT> session,
                                          final JavaTypeDefinition typeDef,
                                          final Class cls) {
        return javaImport(session,
                          typeDef,
                          JavaImport.builder()
                                    .javaPackage(JavaPackage.builder()
                                                            .name(cls.getPackage()
                                                                     .getName())
                                                            .build())
                                    .simpleClassName(cls.getSimpleName())
                                    .build());
    }

    default JavaTypeDefinition javaImport(final TypeGenerationSession<SWT> session,
                                          final JavaTypeDefinition typeDef,
                                          final String javaPackage,
                                          final String simpleClassName) {
        return javaImport(session,
                          typeDef,
                          JavaPackage.builder()
                                     .name(javaPackage)
                                     .build(),
                          simpleClassName);
    }

    default JavaTypeDefinition javaImport(final TypeGenerationSession<SWT> session,
                                          final JavaTypeDefinition typeDef,
                                          final JavaPackage javaPackage,
                                          final String simpleClassName) {
        return javaImport(session,
                          typeDef,
                          JavaImport.builder()
                                    .javaPackage(javaPackage)
                                    .simpleClassName(simpleClassName)
                                    .build());
    }

    default JavaTypeDefinition javaImport(final TypeGenerationSession<SWT> session,
                                          final JavaTypeDefinition typeDef,
                                          final JavaImport... javaImport) {
        return javaImports(session,
                           typeDef,
                           SyncList.of(javaImport));
    }

    default JavaTypeDefinition javaImports(final TypeGenerationSession<SWT> session,
                                           final JavaTypeDefinition typeDef,
                                           final SyncList<JavaImport> javaImports) {
        return session.javaImports(typeDef,
                                   javaImports);
    }

    default JavaTypeDefinition typeAnnotations(final TypeGenerationSession<SWT> session,
                                               final JavaTypeDefinition typeDef,
                                               final SyncList<JavaAnnotation> javaAnnotations) {
        return session.typeAnnotations(typeDef,
                                       javaAnnotations);
    }

    default JavaTypeDefinition typeAnnotation(final TypeGenerationSession<SWT> session,
                                              final JavaTypeDefinition typeDef,
                                              final JavaAnnotation... annotation) {
        return typeAnnotations(session,
                               typeDef,
                               SyncList.of(annotation));
    }

    default JavaTypeDefinition typeModifiers(final TypeGenerationSession<SWT> session,
                                             final JavaTypeDefinition typeDef,
                                             final SyncList<JavaModifier> modifiers) {
        return session.typeModifiers(typeDef,
                                     modifiers);
    }

    default JavaTypeDefinition typeModifier(final TypeGenerationSession<SWT> session,
                                            final JavaTypeDefinition typeDef,
                                            final JavaModifier... modifier) {
        return typeModifiers(session,
                             typeDef,
                             SyncList.of(modifier));
    }

    default JavaTypeDefinition typeKind(final TypeGenerationSession<SWT> session,
                                        final JavaTypeDefinition typeDef,
                                        final JavaTypeKind typeKind) {
        return session.typeKind(typeDef,
                                typeKind);
    }

    default JavaTypeDefinition superType(final TypeGenerationSession<SWT> session,
                                         final JavaTypeDefinition typeDef,
                                         final JavaType superType) {
        return session.superType(typeDef,
                                 superType);
    }

    default JavaTypeDefinition implementedInterfaceType(final TypeGenerationSession<SWT> session,
                                                        final JavaTypeDefinition typeDef,
                                                        final JavaType... implementedInterfaceType) {
        return implementedInterfaceTypes(session,
                                         typeDef,
                                         SyncList.of(implementedInterfaceType));
    }

    default JavaTypeDefinition implementedInterfaceTypes(final TypeGenerationSession<SWT> session,
                                                         final JavaTypeDefinition typeDef,
                                                         final SyncList<JavaType> implementedInterfaceTypes) {
        return session.implementedInterfaceTypes(typeDef,
                                                 implementedInterfaceTypes);
    }

    default JavaTypeDefinition field(final TypeGenerationSession<SWT> session,
                                     final JavaTypeDefinition typeDef,
                                     final JavaField... field) {
        return fields(session,
                      typeDef,
                      SyncList.of(field));
    }

    default JavaTypeDefinition fields(final TypeGenerationSession<SWT> session,
                                      final JavaTypeDefinition typeDef,
                                      final SyncList<JavaField> fields) {
        return session.fields(typeDef,
                              fields);
    }

    default JavaTypeDefinition method(final TypeGenerationSession<SWT> session,
                                      final JavaTypeDefinition typeDef,
                                      final JavaMethod method) {
        return methods(session,
                       typeDef,
                       SyncList.of(method));
    }

    default JavaTypeDefinition methods(final TypeGenerationSession<SWT> session,
                                       final JavaTypeDefinition typeDef,
                                       final SyncList<JavaMethod> methods) {
        return session.methods(typeDef,
                               methods);
    }

    default JavaTypeDefinition subTypeDefinition(final TypeGenerationSession<SWT> session,
                                                 final JavaTypeDefinition typeDef,
                                                 final JavaTypeDefinition... subTypeDefinitions) {
        return subTypeDefinitions(session,
                                  typeDef,
                                  SyncList.of(subTypeDefinitions));
    }

    default JavaTypeDefinition subTypeDefinitions(final TypeGenerationSession<SWT> session,
                                                  final JavaTypeDefinition typeDef,
                                                  final SyncList<JavaTypeDefinition> subTypeDefinitions) {
        return session.subTypeDefinitions(typeDef,
                                          subTypeDefinitions);
    }


}
