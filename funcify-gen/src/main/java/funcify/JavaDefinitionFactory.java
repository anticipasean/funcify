package funcify;

import static funcify.tool.SyncList.of;
import static java.util.Arrays.stream;

import funcify.tool.SyncList;
import funcify.typedef.JavaAnnotation;
import funcify.typedef.JavaCodeBlock;
import funcify.typedef.JavaField;
import funcify.typedef.JavaImport;
import funcify.typedef.JavaMethod;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaPackage;
import funcify.typedef.JavaParameter;
import funcify.typedef.JavaTypeKind;
import funcify.typedef.javaexpr.JavaExpression;
import funcify.typedef.javastatement.JavaStatement;
import funcify.typedef.javatype.JavaType;
import java.util.Objects;

/**
 * @author smccarron
 * @created 2021-05-22
 */
public interface JavaDefinitionFactory<D extends Definition<D>> extends JavaTypeFactory {

    default D name(final D definition,
                   final String name) {
        return definition;
    }

    default D javaPackage(final D definition,
                          final String javaPackage) {
        return javaPackage(definition,
                           JavaPackage.builder()
                                      .name(javaPackage)
                                      .build());
    }

    default D javaPackage(final D definition,
                          final JavaPackage javaPackage) {
        return definition;
    }

    default D javaImport(final D definition,
                         final Class<?>... cls) {
        return javaImport(definition,
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

    default D javaImport(final D definition,
                         final String javaPackage,
                         final String simpleClassName) {
        return javaImport(definition,
                          JavaPackage.builder()
                                     .name(javaPackage)
                                     .build(),
                          simpleClassName);
    }

    default D javaImport(final D definition,
                         final JavaPackage javaPackage,
                         final String simpleClassName) {
        return javaImport(definition,
                          JavaImport.builder()
                                    .javaPackage(javaPackage)
                                    .simpleClassName(simpleClassName)
                                    .build());
    }

    default D javaImport(final D definition,
                         final JavaImport... javaImport) {
        return javaImports(definition,
                           of(javaImport));
    }

    default D javaImports(final D definition,
                          final SyncList<JavaImport> javaImport) {
        return definition;
    }

    default D javaAnnotations(final D definition,
                              final SyncList<JavaAnnotation> javaAnnotations) {
        return definition;
    }

    default D javaAnnotation(final D definition,
                             final JavaAnnotation... annotation) {
        return javaAnnotations(definition,
                               of(annotation));
    }

    default D modifiers(final D definition,
                        final SyncList<JavaModifier> modifiers) {
        return definition;
    }

    default D modifier(final D definition,
                       final JavaModifier... modifier) {
        return modifiers(definition,
                         of(modifier));
    }

    default D typeKind(final D definition,
                       final JavaTypeKind typeKind) {
        return definition;
    }

    default D superType(final D definition,
                        final JavaType superType) {
        return definition;
    }

    default D implementedInterfaceType(final D definition,
                                       final JavaType... implementedInterfaceType) {
        return implementedInterfaceTypes(definition,
                                         of(implementedInterfaceType));
    }

    default D implementedInterfaceTypes(final D definition,
                                        final SyncList<JavaType> implementedInterfaceTypes) {
        return definition;
    }

    default D field(final D definition,
                    final JavaField... field) {
        return fields(definition,
                      of(field));
    }

    default D fields(final D definition,
                     final SyncList<JavaField> fields) {
        return definition;
    }

    default D method(final D definition,
                     final JavaMethod... method) {
        return methods(definition,
                       of(method));
    }

    default D methods(final D definition,
                      final SyncList<JavaMethod> methods) {
        return definition;
    }

    @SuppressWarnings("unchecked")
    default D subTypeDefinition(final D definition,
                                final D... subTypeDefinitions) {
        return subTypeDefinitions(definition,
                                  of(subTypeDefinitions));
    }

    default D subTypeDefinitions(final D definition,
                                 final SyncList<D> subTypeDefinitions) {
        return definition;
    }

    default D typeVariable(final D definition,
                           final JavaType... typeVariable) {
        return typeVariables(definition,
                             of(typeVariable));
    }

    default D typeVariables(final D definition,
                            final SyncList<JavaType> typeVariables) {
        return definition;
    }

    default D returnType(final D definition,
                         final JavaType returnType) {
        return definition;
    }

    default D parameter(final D definition,
                        final JavaParameter... parameter) {
        return parameters(definition,
                          of(parameter));
    }

    default D parameters(final D definition,
                         final SyncList<JavaParameter> parameters) {
        return definition;
    }

    default D codeBlock(final D definition,
                        final JavaCodeBlock codeBlock) {
        return definition;
    }

    default D statement(final D definition,
                        final JavaStatement... statement) {
        return statements(definition,
                          of(statement));
    }

    default D statements(final D definition,
                         final SyncList<JavaStatement> statements) {
        return definition;
    }

    default D expression(final D definition,
                         final JavaExpression... expression) {
        return expressions(definition,
                           of(expression));
    }

    default D expressions(final D definition,
                          final SyncList<JavaExpression> expressions) {
        return definition;
    }

}
