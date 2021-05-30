package funcify;

import funcify.typedef.JavaAnnotation;
import funcify.typedef.JavaField;
import funcify.typedef.JavaImport;
import funcify.typedef.JavaMethod;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaPackage;
import funcify.typedef.JavaTypeDefinition;
import funcify.typedef.JavaTypeKind;
import funcify.typedef.javatype.JavaType;
import java.util.List;

/**
 * @author smccarron
 * @created 2021-05-25
 */
public interface JavaTypeDefinitionFactory extends JavaDefinitionFactory<JavaTypeDefinition> {

    public static JavaDefinitionFactory<JavaTypeDefinition> getInstance() {
        return new JavaTypeDefinitionFactory() {
        };
    }

    @Override
    default JavaTypeDefinition name(final JavaTypeDefinition definition,
                                    final String name) {
        return definition.withName(name);
    }

    @Override
    default JavaTypeDefinition javaPackage(final JavaTypeDefinition definition,
                                           final JavaPackage javaPackage) {
        return definition.withJavaPackage(javaPackage);
    }

    @Override
    default JavaTypeDefinition javaImports(final JavaTypeDefinition definition,
                                           final List<JavaImport> javaImport) {
        final boolean added = definition.getImports()
                                        .addAll(javaImport);
        return definition.withImports(definition.getImports());
    }

    @Override
    default JavaTypeDefinition javaAnnotations(final JavaTypeDefinition definition,
                                               final List<JavaAnnotation> javaAnnotations) {
        final boolean added = definition.getAnnotations()
                                        .addAll(javaAnnotations);
        return definition.withAnnotations(definition.getAnnotations());
    }

    @Override
    default JavaTypeDefinition modifiers(final JavaTypeDefinition definition,
                                         final List<JavaModifier> modifiers) {
        final boolean added = definition.getModifiers()
                                        .addAll(modifiers);
        return definition.withModifiers(definition.getModifiers());
    }

    @Override
    default JavaTypeDefinition typeKind(final JavaTypeDefinition definition,
                                        final JavaTypeKind typeKind) {
        return definition.withTypeKind(typeKind);
    }

    @Override
    default JavaTypeDefinition superType(final JavaTypeDefinition definition,
                                         final JavaType superType) {
        return definition.withSuperType(superType);
    }

    @Override
    default JavaTypeDefinition implementedInterfaceTypes(final JavaTypeDefinition definition,
                                                         final List<JavaType> implementedInterfaceType) {
        final boolean added = definition.getImplementedInterfaceTypes()
                                        .addAll(implementedInterfaceType);
        return definition.withImplementedInterfaceTypes(definition.getImplementedInterfaceTypes());
    }

    @Override
    default JavaTypeDefinition fields(final JavaTypeDefinition definition,
                                      final List<JavaField> field) {
        final boolean added = definition.getFields()
                                        .addAll(field);
        return definition.withFields(definition.getFields());
    }

    @Override
    default JavaTypeDefinition methods(final JavaTypeDefinition definition,
                                       final List<JavaMethod> methods) {
        final boolean added = definition.getMethods()
                                        .addAll(methods);
        return definition.withMethods(definition.getMethods());
    }

    @Override
    default JavaTypeDefinition subTypeDefinitions(final JavaTypeDefinition definition,
                                                  final List<JavaTypeDefinition> subTypeDefinition) {
        final boolean added = definition.getSubTypeDefinitions()
                                        .addAll(subTypeDefinition);
        return definition.withSubTypeDefinitions(definition.getSubTypeDefinitions());
    }

    @Override
    default JavaTypeDefinition typeVariables(final JavaTypeDefinition definition,
                                             final List<JavaType> typeVariables) {
        final boolean added = definition.getTypeVariables()
                                        .addAll(typeVariables);
        return definition.withTypeVariables(definition.getTypeVariables());
    }
}
