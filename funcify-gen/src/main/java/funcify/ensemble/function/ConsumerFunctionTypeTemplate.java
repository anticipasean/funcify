package funcify.ensemble.function;

import funcify.ensemble.EnsembleKind;
import funcify.error.FuncifyCodeGenException;
import funcify.session.TypeGenerationSession;
import funcify.spec.DefaultStringTemplateSpec;
import funcify.spec.StringTemplateSpec;
import funcify.template.TypeGenerationTemplate;
import funcify.tool.CharacterOps;
import funcify.tool.container.SyncMap;
import funcify.writer.StringTemplateWriter;
import funcify.writer.WriteResult;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author smccarron
 * @created 2021-08-29
 */
@AllArgsConstructor(staticName = "of")
public class ConsumerFunctionTypeTemplate<V, R> implements TypeGenerationTemplate<V, R> {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerFunctionTypeTemplate.class);

    @Override
    public List<String> getDestinationTypePackagePathSegments() {
        return Arrays.asList("funcify", "function", "consumer");
    }

    @Override
    public Path getStringTemplateGroupFilePath() {
        return Paths.get("antlr", "funcify", "consumer_function_type.stg");
    }

    @Override
    public TypeGenerationSession<V, R> createTypesForSession(final TypeGenerationSession<V, R> session) {
        logger.debug("create_types_for_session: [ {} ]", SyncMap.empty().put("types", "ConsumerFn[1..n]"));
        try {
            final StringTemplateWriter<V, R> templateWriter = session.getTemplateWriter();
            final SyncMap<EnsembleKind, WriteResult<R>> results = session.getFunctionTypeResults();
            for (EnsembleKind ek : session.getEnsembleKinds()) {
                final int arity = ek.getNumberOfValueParameters();
                final String className = "ConsumerFn" + arity;
                final SyncMap<String, Object> params = SyncMap.of("package",
                                                                  getDestinationTypePackagePathSegments(),
                                                                  "type_variables",
                                                                  CharacterOps.firstNUppercaseLettersWithNumericIndexExtension(ek.getNumberOfValueParameters())
                                                                              .collect(Collectors.toList()),
                                                                  "ensemble_type_name",
                                                                  ek.getSimpleClassName())
                                                              .put("arity", arity)
                                                              .put("ensemble_type_package", Arrays.asList("funcify", "ensemble"))
                                                              .put("witness_type", className + "W");
                final StringTemplateSpec spec = DefaultStringTemplateSpec.builder()
                                                                         .typeName(className)
                                                                         .typePackagePathSegments(
                                                                                 getDestinationTypePackagePathSegments())
                                                                         .templateFunctionName("consumer_function_type")
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
                results.put(ek, writeResult);
            }
            return session.withFunctionTypeResults(results);
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
