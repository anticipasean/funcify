package funcify.template.definition;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

import funcify.template.session.TypeGenerationSession;
import funcify.typedef.JavaAnnotation;
import funcify.typedef.JavaField;
import funcify.typedef.JavaImport;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaPackage;
import funcify.typedef.JavaTypeKind;
import funcify.typedef.javatype.JavaType;
import java.util.List;
import java.util.Objects;

/**
 * @param <SWT> - Session Witness Type
 * @author smccarron
 * @created 2021-05-28
 */
public interface TypeGenerationTemplate<SWT> extends MethodGenerationTemplate<SWT> {

    <TD, MD, CD, SD, ED> TD emptyTypeDefinition(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session);

    <TD, MD, CD, SD, ED> TD typeName(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                     final TD typeDef,
                                     final String name);

    default <TD, MD, CD, SD, ED> TD javaPackage(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                final TD typeDef,
                                                final String javaPackage) {
        return javaPackage(session,
                           typeDef,
                           JavaPackage.builder()
                                      .name(javaPackage)
                                      .build());
    }

    <TD, MD, CD, SD, ED> TD javaPackage(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                        final TD typeDef,
                                        final JavaPackage javaPackage);

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
                           asList(javaImport));
    }

    <TD, MD, CD, SD, ED> TD javaImports(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                        final TD typeDef,
                                        final List<JavaImport> javaImport);

    <TD, MD, CD, SD, ED> TD javaAnnotations(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                            final TD typeDef,
                                            final List<JavaAnnotation> javaAnnotations);

    default <TD, MD, CD, SD, ED> TD javaAnnotation(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                   final TD typeDef,
                                                   final JavaAnnotation... annotation) {
        return javaAnnotations(session,
                               typeDef,
                               asList(annotation));
    }

    <TD, MD, CD, SD, ED> TD typeModifiers(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                          final TD typeDef,
                                          final List<JavaModifier> modifiers);

    default <TD, MD, CD, SD, ED> TD typeModifiers(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                  final TD typeDef,
                                                  final JavaModifier... modifier) {
        return typeModifiers(session,
                             typeDef,
                             asList(modifier));
    }

    <TD, MD, CD, SD, ED> TD typeKind(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                     final TD typeDef,
                                     final JavaTypeKind typeKind);

    <TD, MD, CD, SD, ED> TD superType(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                      final TD typeDef,
                                      final JavaType superType);

    default <TD, MD, CD, SD, ED> TD implementedInterfaceType(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                             final TD typeDef,
                                                             final JavaType... implementedInterfaceType) {
        return implementedInterfaceTypes(session,
                                         typeDef,
                                         asList(implementedInterfaceType));
    }

    <TD, MD, CD, SD, ED> TD implementedInterfaceTypes(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                      final TD typeDef,
                                                      final List<JavaType> implementedInterfaceTypes);

    default <TD, MD, CD, SD, ED> TD field(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                          final TD typeDef,
                                          final JavaField... field) {
        return fields(session,
                      typeDef,
                      asList(field));
    }

    <TD, MD, CD, SD, ED> TD fields(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                   final TD typeDef,
                                   final List<JavaField> fields);

    @SuppressWarnings("unchecked")
    default <TD, MD, CD, SD, ED> TD method(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                           final TD typeDef,
                                           final MD... method) {
        return methods(session,
                       typeDef,
                       asList(method));
    }

    <TD, MD, CD, SD, ED> TD methods(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                    final TD typeDef,
                                    final List<MD> methods);

    @SuppressWarnings("unchecked")
    default <TD, MD, CD, SD, ED> TD subTypeDefinition(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                      final TD typeDef,
                                                      final TD... subTypeDefinitions) {
        return subTypeDefinitions(session,
                                  typeDef,
                                  asList(subTypeDefinitions));
    }

    <TD, MD, CD, SD, ED> TD subTypeDefinitions(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                               final TD typeDef,
                                               final List<TD> subTypeDefinitions);


}
