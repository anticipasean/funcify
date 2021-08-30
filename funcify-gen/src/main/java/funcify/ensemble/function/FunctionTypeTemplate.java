package funcify.ensemble.function;

import funcify.ensemble.EnsembleKind;
import funcify.ensemble.basetype.session.TypeGenerationSession;
import funcify.error.FuncifyCodeGenException;
import funcify.spec.DefaultStringTemplateSpec;
import funcify.spec.StringTemplateSpec;
import funcify.template.TypeGenerationTemplate;
import funcify.tool.CharacterOps;
import funcify.tool.container.SyncMap;
import funcify.writer.StringTemplateWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

/**
 * @author smccarron
 * @created 2021-08-29
 */
@AllArgsConstructor(staticName = "of")
public class FunctionTypeTemplate<V, R> implements TypeGenerationTemplate<V, R> {

    @Override
    public List<String> getDestinationTypePackagePathSegments() {
        return Arrays.asList("funcify",
                             "function");
    }

    @Override
    public String getStringTemplateGroupFileName() {
        return "function_type.stg";
    }

    @Override
    public String getStringTemplateGroupFilePathString() {
        return "src/main/antlr/funcify/function_type.stg";
    }

    @Override
    public TypeGenerationSession<V, R> createTypesForSession(final TypeGenerationSession<V, R> session) {
        try {
            final StringTemplateWriter<V, R> templateWriter = session.getTemplateWriter();
            final SyncMap<EnsembleKind, R> results = session.getFunctionTypeResults();
            for (EnsembleKind ek : session.getEnsembleKinds()) {
                final int arity = ek.getNumberOfValueParameters() - 1;
                final String className = "Fn" + arity;
                final SyncMap<String, Object> params = SyncMap.of("package",
                                                                  getDestinationTypePackagePathSegments(),
                                                                  "class_name",
                                                                  className,
                                                                  "type_variables",
                                                                  CharacterOps.firstNUppercaseLettersWithNumericIndexExtension(ek.getNumberOfValueParameters())
                                                                              .collect(Collectors.toList()),
                                                                  "next_type_variable",
                                                                  CharacterOps.uppercaseLetterByIndexWithNumericExtension(ek.getNumberOfValueParameters())
                                                                              .orElse(null),
                                                                  "ensemble_type_name",
                                                                  ek.getSimpleClassName())
                                                              .put("arity",
                                                                   arity)
                                                              .put("ensemble_type_package",
                                                                   Arrays.asList("funcify",
                                                                                 "ensemble"))
                                                              .put("witness_type",
                                                                   className + "W");
                if (ek.getNumberOfValueParameters() == 1) {
                    params.put("implements_supplier",
                               true);
                } else if (ek.getNumberOfValueParameters() == 2) {
                    params.put("implements_function",
                               true);
                }
                final StringTemplateSpec spec = DefaultStringTemplateSpec.builder()
                                                                         .typeName(className)
                                                                         .templateFunctionName("function_type")
                                                                         .fileTypeExtension(".java")
                                                                         .stringTemplateGroupFileName(getStringTemplateGroupFileName())
                                                                         .stringTemplateGroupFilePathString(getStringTemplateGroupFilePathString())
                                                                         .destinationParentDirectoryPath(session.getDestinationDirectoryPath())
                                                                         .templateFunctionParameterInput(params)
                                                                         .build();
                final R writeResult = templateWriter.write(spec);
                results.put(ek,
                            writeResult);
            }
            return session.withDisjunctWrappableEnsembleTypeResults(results);
        } catch (final Throwable t) {
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new FuncifyCodeGenException(t.getMessage(),
                                                  t);
            }
        }
    }
}
