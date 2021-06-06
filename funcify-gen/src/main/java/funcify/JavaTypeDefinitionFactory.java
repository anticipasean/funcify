package funcify;

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
 * @author smccarron
 * @created 2021-05-25
 */
public interface JavaTypeDefinitionFactory extends JavaDefinitionFactory<JavaTypeDefinition> {

    static JavaDefinitionFactory<JavaTypeDefinition> getInstance() {
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
                                           final SyncList<JavaImport> javaImport) {
        return definition.withJavaImports(definition.getJavaImports()
                                                    .appendAll(javaImport));
    }

    @Override
    default JavaTypeDefinition javaAnnotations(final JavaTypeDefinition definition,
                                               final SyncList<JavaAnnotation> javaAnnotations) {
        return definition.withAnnotations(definition.getAnnotations()
                                                    .appendAll(javaAnnotations));
    }

    @Override
    default JavaTypeDefinition modifiers(final JavaTypeDefinition definition,
                                         final SyncList<JavaModifier> modifiers) {
        return definition.withModifiers(definition.getModifiers()
                                                  .appendAll(modifiers));
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
                                                         final SyncList<JavaType> implementedInterfaceType) {
        return definition.withImplementedInterfaceTypes(definition.getImplementedInterfaceTypes()
                                                                  .appendAll(implementedInterfaceType));
    }

    @Override
    default JavaTypeDefinition fields(final JavaTypeDefinition definition,
                                      final SyncList<JavaField> field) {
        return definition.withFields(definition.getFields()
                                               .appendAll(field));
    }

    @Override
    default JavaTypeDefinition methods(final JavaTypeDefinition definition,
                                       final SyncList<JavaMethod> methods) {
        return definition.withMethods(definition.getMethods()
                                                .appendAll(methods));
    }

    @Override
    default JavaTypeDefinition subTypeDefinitions(final JavaTypeDefinition definition,
                                                  final SyncList<JavaTypeDefinition> subTypeDefinition) {
        return definition.withSubTypeDefinitions(definition.getSubTypeDefinitions()
                                                           .appendAll(subTypeDefinition));
    }

    @Override
    default JavaTypeDefinition typeVariables(final JavaTypeDefinition definition,
                                             final SyncList<JavaType> typeVariables) {
        return definition.withTypeVariables(definition.getTypeVariables()
                                                      .appendAll(typeVariables));
    }
}
