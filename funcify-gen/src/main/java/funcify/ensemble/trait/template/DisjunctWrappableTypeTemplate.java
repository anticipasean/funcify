package funcify.ensemble.trait.template;

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
public class DisjunctWrappableTypeTemplate<V, R> implements TypeGenerationTemplate<V, R> {

    @Override
    public List<String> getDestinationTypePackagePathSegments() {
        return Arrays.asList("funcify",
                             "trait");
    }

    @Override
    public String getStringTemplateGroupFileName() {
        return "disjunct_wrappable_type.stg";
    }

    @Override
    public String getStringTemplateGroupFilePathString() {
        return "src/main/antlr/funcify/disjunct_wrappable_type.stg";
    }

    @Override
    public TypeGenerationSession<V, R> createTypesForSession(final TypeGenerationSession<V, R> session) {
        try {
            final StringTemplateWriter<V, R> templateWriter = session.getTemplateWriter();
            final SyncMap<EnsembleKind, R> results = session.getDisjunctWrappableEnsembleTypeResults();
            for (EnsembleKind ek : session.getEnsembleKinds()) {
                final String className = "DisjunctWrappable" + ek.getSimpleClassName();
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
                                                              .put("ensemble_type_package",
                                                                   Arrays.asList("funcify",
                                                                                 "ensemble"));
                final StringTemplateSpec spec = DefaultStringTemplateSpec.builder()
                                                                         .typeName(className)
                                                                         .templateFunctionName("disjunct_wrappable_type")
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
