package funcify.base.session;

import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import funcify.typedef.JavaAnnotation;
import funcify.typedef.JavaCodeBlock;
import funcify.typedef.JavaField;
import funcify.typedef.JavaImport;
import funcify.typedef.JavaMethod;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaPackage;
import funcify.typedef.JavaParameter;
import funcify.typedef.JavaTypeDefinition;
import funcify.typedef.JavaTypeKind;
import funcify.typedef.javaexpr.JavaExpression;
import funcify.typedef.javaexpr.TemplatedExpression;
import funcify.typedef.javaexpr.TextExpression;
import funcify.typedef.javastatement.AssignmentStatement;
import funcify.typedef.javastatement.JavaStatement;
import funcify.typedef.javastatement.ReturnStatement;
import funcify.typedef.javatype.JavaType;
import java.util.Optional;

public abstract class AbstractTypeGenerationSession<SWT> implements TypeGenerationSession<SWT> {

    @Override
    public JavaTypeDefinition emptyTypeDefinition() {
        return JavaTypeDefinition.builder()
                                 .build();
    }

    @Override
    public JavaTypeDefinition typeName(final JavaTypeDefinition typeDef,
                                       final String name) {
        return typeDef.withName(name);
    }

    @Override
    public JavaTypeDefinition typeDefinitionTypeVariables(final JavaTypeDefinition typeDef,
                                                          final SyncList<JavaType> typeVariables) {
        return typeDef.withTypeVariables(typeDef.getTypeVariables()
                                                .appendAll(typeVariables));
    }

    @Override
    public JavaTypeDefinition javaPackage(final JavaTypeDefinition typeDef,
                                          final JavaPackage javaPackage) {
        return typeDef.withJavaPackage(javaPackage);
    }

    @Override
    public JavaTypeDefinition javaImports(final JavaTypeDefinition typeDef,
                                          final SyncList<JavaImport> javaImports) {
        return typeDef.withJavaImports(typeDef.getJavaImports()
                                              .appendAll(javaImports));
    }

    @Override
    public JavaTypeDefinition typeAnnotations(final JavaTypeDefinition typeDef,
                                              final SyncList<JavaAnnotation> javaAnnotations) {
        return typeDef.withAnnotations(typeDef.getAnnotations()
                                              .appendAll(javaAnnotations));
    }

    @Override
    public JavaTypeDefinition typeModifiers(final JavaTypeDefinition typeDef,
                                            final SyncList<JavaModifier> modifiers) {
        return typeDef.withModifiers(typeDef.getModifiers()
                                            .appendAll(modifiers));
    }

    @Override
    public JavaTypeDefinition typeKind(final JavaTypeDefinition typeDef,
                                       final JavaTypeKind typeKind) {
        return typeDef.withTypeKind(typeKind);
    }

    @Override
    public JavaTypeDefinition superType(final JavaTypeDefinition typeDef,
                                        final JavaType superType) {
        return typeDef.withSuperType(superType);
    }

    @Override
    public JavaTypeDefinition implementedInterfaceTypes(final JavaTypeDefinition typeDef,
                                                        final SyncList<JavaType> implementedInterfaceTypes) {
        return typeDef.withImplementedInterfaceTypes(typeDef.getImplementedInterfaceTypes()
                                                            .appendAll(implementedInterfaceTypes));
    }

    @Override
    public JavaTypeDefinition fields(final JavaTypeDefinition typeDef,
                                     final SyncList<JavaField> fields) {
        return typeDef.withFields(typeDef.getFields()
                                         .appendAll(fields));
    }

    @Override
    public JavaTypeDefinition methods(final JavaTypeDefinition typeDef,
                                      final SyncList<JavaMethod> methods) {
        return typeDef.withMethods(typeDef.getMethods()
                                          .appendAll(methods));
    }

    @Override
    public JavaTypeDefinition subTypeDefinitions(final JavaTypeDefinition typeDef,
                                                 final SyncList<JavaTypeDefinition> subTypeDefinitions) {
        return typeDef.withSubTypeDefinitions(typeDef.getSubTypeDefinitions()
                                                     .appendAll(subTypeDefinitions));
    }

    @Override
    public JavaMethod emptyMethodDefinition() {
        return JavaMethod.builder()
                         .build();
    }

    @Override
    public SyncMap<String, JavaMethod> getMethodDefinitionsByName(final JavaTypeDefinition typeDef) {
        return SyncMap.fromIterable(typeDef.getMethods(),
                                    JavaMethod::getName);
    }

    @Override
    public JavaMethod methodAnnotations(final JavaMethod methodDef,
                                        final SyncList<JavaAnnotation> javaAnnotations) {
        return methodDef.withAnnotations(methodDef.getAnnotations()
                                                  .appendAll(javaAnnotations));
    }

    @Override
    public JavaMethod methodModifiers(final JavaMethod methodDef,
                                      final SyncList<JavaModifier> modifiers) {
        return methodDef.withModifiers(methodDef.getModifiers()
                                                .appendAll(modifiers));
    }

    @Override
    public JavaMethod methodTypeVariables(final JavaMethod methodDef,
                                          final SyncList<JavaType> typeVariables) {
        return methodDef.withTypeVariables(methodDef.getTypeVariables()
                                                    .appendAll(typeVariables));
    }

    @Override
    public JavaMethod returnType(final JavaMethod methodDef,
                                 final JavaType returnType) {
        return methodDef.withReturnType(returnType);
    }

    @Override
    public JavaMethod methodName(final JavaMethod methodDef,
                                 final String name) {
        return methodDef.withName(name);
    }

    @Override
    public JavaMethod parameters(final JavaMethod methodDef,
                                 final SyncList<JavaParameter> parameters) {
        return methodDef.withParameters(methodDef.getParameters()
                                                 .appendAll(parameters));
    }

    @Override
    public JavaMethod codeBlock(final JavaMethod methodDef,
                                final JavaCodeBlock codeBlock) {
        return methodDef.withCodeBlock(codeBlock);
    }

    @Override
    public JavaCodeBlock emptyCodeBlockDefinition() {
        return JavaCodeBlock.builder()
                            .build();
    }

    @Override
    public Optional<JavaCodeBlock> getCodeBlockForMethodDefinition(final JavaMethod methodDef) {
        return Optional.ofNullable(methodDef.getCodeBlock());
    }

    @Override
    public JavaCodeBlock statements(final JavaCodeBlock codeBlockDef,
                                    final SyncList<JavaStatement> statements) {
        return codeBlockDef.withStatements(codeBlockDef.getStatements()
                                                       .appendAll(statements));
    }

    @Override
    public SyncList<JavaStatement> getStatementsForCodeBlock(final JavaCodeBlock codeBlockDef) {
        return codeBlockDef.getStatements();
    }

    @Override
    public JavaStatement assignmentStatement(final JavaType assigneeType,
                                             final String assigneeName,
                                             final SyncList<JavaExpression> expressions) {
        return AssignmentStatement.builder()
                                  .assigneeName(assigneeName)
                                  .assigneeType(assigneeType)
                                  .expressions(expressions)
                                  .build();
    }

    @Override
    public JavaStatement returnStatement(final SyncList<JavaExpression> expression) {
        return ReturnStatement.builder()
                              .expressions(expression)
                              .build();
    }

    @Override
    public SyncList<JavaExpression> getExpressionsInStatement(final JavaStatement statementDef) {
        return statementDef.getExpressions();
    }

    @Override
    public JavaExpression simpleExpression(final SyncList<String> text) {
        return TextExpression.of(text.join(""));
    }

    @Override
    public JavaExpression templateExpression(final String templateFunction,
                                             final SyncMap<String, Object> input) {
        return TemplatedExpression.builder()
                                  .templateFunction(templateFunction)
                                  .input(input)
                                  .build();
    }

}