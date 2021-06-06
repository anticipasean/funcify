package funcify.factory.session;

import funcify.EnsembleKind;
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
import funcify.typedef.javaexpr.LambdaExpression;
import funcify.typedef.javaexpr.TemplatedExpression;
import funcify.typedef.javaexpr.TextExpression;
import funcify.typedef.javastatement.AssignmentStatement;
import funcify.typedef.javastatement.JavaStatement;
import funcify.typedef.javastatement.ReturnStatement;
import funcify.typedef.javatype.JavaType;
import java.nio.file.Path;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

/**
 * @author smccarron
 * @created 2021-05-31
 */
@AllArgsConstructor
@Builder
@Getter
public class DefaultEnsembleTypeGenerationSession implements
                                                  EnsembleTypeGenerationSession<JavaTypeDefinition, JavaMethod, JavaCodeBlock, JavaStatement, JavaExpression> {

    private final Path destinationDirectoryPath;

    @Default
    private final SyncList<EnsembleKind> ensembleKinds = SyncList.empty();

    private final JavaTypeDefinition baseEnsembleInterfaceTypeDefinition;

    @Default
    private final SyncMap<EnsembleKind, JavaTypeDefinition> ensembleInterfaceTypeDefinitionsByEnsembleKind = SyncMap.empty();

    private final SyncMap<String, JavaTypeDefinition> typeDefinitionsByName;

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
    public JavaTypeDefinition javaPackage(final JavaTypeDefinition typeDef,
                                          final JavaPackage javaPackage) {
        return typeDef.withJavaPackage(javaPackage);
    }

    @Override
    public JavaTypeDefinition javaImports(final JavaTypeDefinition typeDef,
                                          final SyncList<JavaImport> javaImports) {
        return typeDef.withJavaImports(javaImports);
    }

    @Override
    public JavaTypeDefinition javaAnnotations(final JavaTypeDefinition typeDef,
                                              final SyncList<JavaAnnotation> javaAnnotations) {
        return typeDef.withAnnotations(javaAnnotations);
    }

    @Override
    public JavaTypeDefinition typeModifiers(final JavaTypeDefinition typeDef,
                                            final SyncList<JavaModifier> modifiers) {
        return typeDef.withModifiers(modifiers);
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
        return typeDef.withImplementedInterfaceTypes(implementedInterfaceTypes);
    }

    @Override
    public JavaTypeDefinition fields(final JavaTypeDefinition typeDef,
                                     final SyncList<JavaField> fields) {
        return typeDef.withFields(fields);
    }

    @Override
    public JavaTypeDefinition methods(final JavaTypeDefinition typeDef,
                                      final SyncList<JavaMethod> methods) {
        return typeDef.withMethods(methods);
    }

    @Override
    public JavaTypeDefinition subTypeDefinitions(final JavaTypeDefinition typeDef,
                                                 final SyncList<JavaTypeDefinition> subTypeDefinitions) {
        return typeDef.withSubTypeDefinitions(subTypeDefinitions);
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
    public JavaExpression templateExpression(final String templateName,
                                             final SyncList<String> templateParameters) {
        return TemplatedExpression.builder()
                                  .templateCall(templateName)
                                  .templateParameters(templateParameters)
                                  .build();
    }

    @Override
    public JavaExpression lambdaExpression(final SyncList<JavaParameter> parameters,
                                           final JavaExpression... lambdaBodyExpression) {
        return LambdaExpression.builder()
                               .parameters(parameters)
                               .lambdaBody(SyncList.of(lambdaBodyExpression))
                               .build();
    }
}
