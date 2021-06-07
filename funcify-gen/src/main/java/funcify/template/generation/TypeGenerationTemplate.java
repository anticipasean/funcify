package funcify.template.generation;

import static java.util.Arrays.stream;

import funcify.template.session.TypeGenerationSession;
import funcify.tool.container.SyncList;
import funcify.typedef.JavaAnnotation;
import funcify.typedef.JavaField;
import funcify.typedef.JavaImport;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaPackage;
import funcify.typedef.JavaTypeKind;
import funcify.typedef.javatype.JavaType;
import java.util.Objects;

/**
 * The template provides the framework for the updates that can be made to the session and what the session can and should provide
 * to those using the framework
 *
 * @param <SWT> - Session Witness Type
 * @author smccarron
 * @created 2021-05-28
 */
public interface TypeGenerationTemplate<SWT> extends MethodGenerationTemplate<SWT> {

    default <TD, MD, CD, SD, ED> TD emptyTypeDefinition(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session) {
        return session.emptyTypeDefinition();
    }

    default <TD, MD, CD, SD, ED> TD typeName(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                             final TD typeDef,
                                             final String name) {
        return session.typeName(typeDef,
                                name);
    }

    default <TD, MD, CD, SD, ED> TD typeDefinitionTypeVariables(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                                final TD typeDef,
                                                                final SyncList<JavaType> typeVariables) {
        return session.typeDefinitionTypeVariables(typeDef,
                                                   typeVariables);
    }

    default <TD, MD, CD, SD, ED> TD typeDefinitionTypeVariable(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                               final TD typeDef,
                                                               final JavaType... typeVariable) {
        return session.typeDefinitionTypeVariables(typeDef,
                                                   SyncList.of(typeVariable));
    }

    default <TD, MD, CD, SD, ED> TD javaPackage(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                final TD typeDef,
                                                final String javaPackage) {
        return javaPackage(session,
                           typeDef,
                           JavaPackage.builder()
                                      .name(javaPackage)
                                      .build());
    }

    default <TD, MD, CD, SD, ED> TD javaPackage(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                final TD typeDef,
                                                final JavaPackage javaPackage) {
        return session.javaPackage(typeDef,
                                   javaPackage);
    }

    default <TD, MD, CD, SD, ED> TD javaImport(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                               final TD typeDef,
                                               final Class<?>... cls) {
        return javaImport(session,
                          typeDef,
                          stream(cls).filter(Objects::nonNull)
                                     .map(c -> JavaImport.builder()
                                                         .javaPackage(JavaPackage.builder()
                                                                                 .name(c.getPackage()
                                                                                        .getName())
                                                                                 .build())
                                                         .simpleClassName(c.getSimpleName())
                                                         .build())
                                     .toArray(JavaImport[]::new));
    }

    default <TD, MD, CD, SD, ED> TD javaImport(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                               final TD typeDef,
                                               final String javaPackage,
                                               final String simpleClassName) {
        return javaImport(session,
                          typeDef,
                          JavaPackage.builder()
                                     .name(javaPackage)
                                     .build(),
                          simpleClassName);
    }

    default <TD, MD, CD, SD, ED> TD javaImport(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                               final TD typeDef,
                                               final JavaPackage javaPackage,
                                               final String simpleClassName) {
        return javaImport(session,
                          typeDef,
                          JavaImport.builder()
                                    .javaPackage(javaPackage)
                                    .simpleClassName(simpleClassName)
                                    .build());
    }

    default <TD, MD, CD, SD, ED> TD javaImport(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                               final TD typeDef,
                                               final JavaImport... javaImport) {
        return javaImports(session,
                           typeDef,
                           SyncList.of(javaImport));
    }

    default <TD, MD, CD, SD, ED> TD javaImports(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                final TD typeDef,
                                                final SyncList<JavaImport> javaImports) {
        return session.javaImports(typeDef,
                                   javaImports);
    }

    default <TD, MD, CD, SD, ED> TD javaAnnotations(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                    final TD typeDef,
                                                    final SyncList<JavaAnnotation> javaAnnotations) {
        return session.javaAnnotations(typeDef,
                                       javaAnnotations);
    }

    default <TD, MD, CD, SD, ED> TD javaAnnotation(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                   final TD typeDef,
                                                   final JavaAnnotation... annotation) {
        return javaAnnotations(session,
                               typeDef,
                               SyncList.of(annotation));
    }

    default <TD, MD, CD, SD, ED> TD typeModifiers(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                  final TD typeDef,
                                                  final SyncList<JavaModifier> modifiers) {
        return session.typeModifiers(typeDef,
                                     modifiers);
    }

    default <TD, MD, CD, SD, ED> TD typeModifier(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                 final TD typeDef,
                                                 final JavaModifier... modifier) {
        return typeModifiers(session,
                             typeDef,
                             SyncList.of(modifier));
    }

    default <TD, MD, CD, SD, ED> TD typeKind(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                             final TD typeDef,
                                             final JavaTypeKind typeKind) {
        return session.typeKind(typeDef,
                                typeKind);
    }

    default <TD, MD, CD, SD, ED> TD superType(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                              final TD typeDef,
                                              final JavaType superType) {
        return session.superType(typeDef,
                                 superType);
    }

    default <TD, MD, CD, SD, ED> TD implementedInterfaceType(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                             final TD typeDef,
                                                             final JavaType... implementedInterfaceType) {
        return implementedInterfaceTypes(session,
                                         typeDef,
                                         SyncList.of(implementedInterfaceType));
    }

    default <TD, MD, CD, SD, ED> TD implementedInterfaceTypes(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                              final TD typeDef,
                                                              final SyncList<JavaType> implementedInterfaceTypes) {
        return session.implementedInterfaceTypes(typeDef,
                                                 implementedInterfaceTypes);
    }

    default <TD, MD, CD, SD, ED> TD field(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                          final TD typeDef,
                                          final JavaField... field) {
        return fields(session,
                      typeDef,
                      SyncList.of(field));
    }

    default <TD, MD, CD, SD, ED> TD fields(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                           final TD typeDef,
                                           final SyncList<JavaField> fields) {
        return session.fields(typeDef,
                              fields);
    }

    @SuppressWarnings("unchecked")
    default <TD, MD, CD, SD, ED> TD method(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                           final TD typeDef,
                                           final MD... method) {
        return methods(session,
                       typeDef,
                       SyncList.of(method));
    }

    default <TD, MD, CD, SD, ED> TD methods(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                            final TD typeDef,
                                            final SyncList<MD> methods) {
        return session.methods(typeDef,
                               methods);
    }

    @SuppressWarnings("unchecked")
    default <TD, MD, CD, SD, ED> TD subTypeDefinition(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                      final TD typeDef,
                                                      final TD... subTypeDefinitions) {
        return subTypeDefinitions(session,
                                  typeDef,
                                  SyncList.of(subTypeDefinitions));
    }

    default <TD, MD, CD, SD, ED> TD subTypeDefinitions(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                       final TD typeDef,
                                                       final SyncList<TD> subTypeDefinitions) {
        return session.subTypeDefinitions(typeDef,
                                          subTypeDefinitions);
    }


}
