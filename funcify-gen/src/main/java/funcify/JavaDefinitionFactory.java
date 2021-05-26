package funcify;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

import funcify.typedef.JavaAnnotation;
import funcify.typedef.JavaCodeBlock;
import funcify.typedef.JavaField;
import funcify.typedef.JavaImport;
import funcify.typedef.JavaMethod;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaPackage;
import funcify.typedef.JavaParameter;
import funcify.typedef.JavaTypeKind;
import funcify.typedef.javastatement.JavaExpression;
import funcify.typedef.javastatement.JavaStatement;
import funcify.typedef.javatype.JavaType;
import java.util.List;
import java.util.Objects;

/**
 * @author smccarron
 * @created 2021-05-22
 */
public interface JavaDefinitionFactory<D> extends JavaTypeFactory {

    D name(final String name);

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
                           asList(javaImport));
    }

    default D javaImports(final D definition,
                          final List<JavaImport> javaImport) {
        return definition;
    }

    default D javaAnnotations(final D definition,
                              final List<JavaAnnotation> javaAnnotations) {
        return definition;
    }

    default D javaAnnotation(final D definition,
                             final JavaAnnotation... annotation) {
        return javaAnnotations(definition,
                               asList(annotation));
    }

    default D modifiers(final D definition,
                        final List<JavaModifier> modifiers) {
        return definition;
    }

    default D modifier(final D definition,
                       final JavaModifier... modifier) {
        return modifiers(definition,
                         asList(modifier));
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
                                         asList(implementedInterfaceType));
    }

    default D implementedInterfaceTypes(final D definition,
                                        final List<JavaType> implementedInterfaceTypes) {
        return definition;
    }

    default D field(final D definition,
                    final JavaField... field) {
        return fields(definition,
                      asList(field));
    }

    default D fields(final D definition,
                     final List<JavaField> fields) {
        return definition;
    }

    default D method(final D definition,
                     final JavaMethod... method) {
        return methods(definition,
                       asList(method));
    }

    default D methods(final D definition,
                      final List<JavaMethod> methods) {
        return definition;
    }

    default D subTypeDefinition(final D definition,
                                final D... subTypeDefinitions) {
        return subTypeDefinitions(definition,
                                  asList(subTypeDefinitions));
    }

    default D subTypeDefinitions(final D definition,
                                 final List<D> subTypeDefinitions) {
        return definition;
    }

    default D typeVariable(final D definition,
                           final JavaType... typeVariable) {
        return typeVariables(definition,
                             asList(typeVariable));
    }

    default D typeVariables(final D definition,
                            final List<JavaType> typeVariables) {
        return definition;
    }

    default D returnType(final D definition,
                         final JavaType returnType) {
        return definition;
    }

    default D parameter(final D definition,
                        final JavaParameter... parameter) {
        return parameters(definition,
                          asList(parameter));
    }

    default D parameters(final D definition,
                         final List<JavaParameter> parameters) {
        return definition;
    }

    default D codeBlock(final D definition,
                        final JavaCodeBlock codeBlock) {
        return definition;
    }

    default D statement(final D definition,
                        final JavaStatement... statement) {
        return statements(definition,
                          asList(statement));
    }

    default D statements(final D definition,
                         final List<JavaStatement> statements) {
        return definition;
    }

    default D expression(final D definition,
                         final JavaExpression expression) {
        return definition;
    }

}
