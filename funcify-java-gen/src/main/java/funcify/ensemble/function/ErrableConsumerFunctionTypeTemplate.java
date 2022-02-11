package funcify.ensemble.function;

import funcify.ensemble.EnsembleKind;
import funcify.error.FuncifyCodeGenException;
import funcify.session.TypeGenerationSession;
import funcify.spec.DefaultStringTemplateSpec;
import funcify.spec.StringTemplateSpec;
import funcify.template.TypeGenerationTemplate;
import funcify.tool.CharacterOps;
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import funcify.writer.StringTemplateWriter;
import funcify.writer.WriteResult;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author smccarron
 * @created 2021-08-29
 */
@AllArgsConstructor(staticName = "of")
public class ErrableConsumerFunctionTypeTemplate<V, R> implements TypeGenerationTemplate<V, R> {

    private static final Logger logger = LoggerFactory.getLogger(ErrableConsumerFunctionTypeTemplate.class);

    @Override
    public List<String> getDestinationTypePackagePathSegments() {
        return Arrays.asList("funcify", "function", "consumer", "errable");
    }

    @Override
    public Path getStringTemplateGroupFilePath() {
        return Paths.get("antlr", "funcify", "ensemble", "function", "errable_consumer_function_type.stg");
    }

    @Override
    public TypeGenerationSession<V, R> createTypesForSession(final TypeGenerationSession<V, R> session) {
        logger.debug("create_types_for_session: [ {} ]", SyncMap.empty().put("types", "ErrableConsumerFn[1..n]"));
        try {
            final StringTemplateWriter<V, R> templateWriter = session.getTemplateWriter();
            final SyncMap<EnsembleKind, WriteResult<R>> results = session.getErrableConsumerFunctionTypeResults();
            final SyncList<EnsembleKind> ensembleKinds = session.getEnsembleKinds().copy();
            // remove the ek with max value param count since fn.n = ek.n + 1 and errable_fn.n = fn.n + 1
            session.getEnsembleKinds()
                   .stream()
                   .max(Comparator.comparing(EnsembleKind::getNumberOfValueParameters))
                   .ifPresent(ensembleKinds::removeValue);
            for (EnsembleKind inputEnsembleKind : ensembleKinds) {
                final int arity = inputEnsembleKind.getNumberOfValueParameters();
                final EnsembleKind ekWithErrableParam = EnsembleKind.getEnsembleKindByNumberOfValueParameters()
                                                                    .get(inputEnsembleKind.getNumberOfValueParameters() + 1);
                final String className = "ErrableConsumerFn" + arity;
                final SyncMap<String, Object> params = SyncMap.of("package",
                                                                  getDestinationTypePackagePathSegments(),
                                                                  "type_variables",
                                                                  CharacterOps.firstNUppercaseLettersWithNumericIndexExtension(
                                                                                      inputEnsembleKind.getNumberOfValueParameters())
                                                                              .collect(Collectors.toList()),
                                                                  "ensemble_type_name",
                                                                  ekWithErrableParam.getSimpleClassName())
                                                              .put("arity", arity)
                                                              .put("ensemble_type_package", Arrays.asList("funcify", "ensemble"))
                                                              .put("witness_type", className + "W");
                final StringTemplateSpec spec = DefaultStringTemplateSpec.builder()
                                                                         .typeName(className)
                                                                         .typePackagePathSegments(
                                                                                 getDestinationTypePackagePathSegments())
                                                                         .templateFunctionName("errable_consumer_function_type")
                                                                         .fileTypeExtension(".java")
                                                                         .stringTemplateGroupFilePath(
                                                                                 getStringTemplateGroupFilePath())
                                                                         .destinationParentDirectoryPath(session.getDestinationDirectoryPath())
                                                                         .templateFunctionParameterInput(params)
                                                                         .build();
                final WriteResult<R> writeResult = templateWriter.write(spec);
                if (writeResult.isFailure()) {
                    throw writeResult.getFailureValue().orElseThrow(() -> new FuncifyCodeGenException("missing throwable"));
                }
                results.put(inputEnsembleKind, writeResult);
            }
            return session.withErrableConsumerFunctionTypeResults(results);
        } catch (final Throwable t) {
            logger.debug("create_types_for_session: [ status: failed ] due to [ type: {}, message: {} ]",
                         t.getClass().getSimpleName(),
                         t.getMessage());
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new FuncifyCodeGenException(t.getMessage(), t);
            }
        }
    }
}
