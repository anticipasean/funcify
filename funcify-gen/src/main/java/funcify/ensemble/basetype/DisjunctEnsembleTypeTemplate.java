package funcify.ensemble.basetype;

import funcify.ensemble.EnsembleKind;
import funcify.ensemble.template.TraitGenerationTemplate;
import funcify.error.FuncifyCodeGenException;
import funcify.session.TypeGenerationSession;
import funcify.spec.DefaultStringTemplateSpec;
import funcify.spec.StringTemplateSpec;
import funcify.tool.CharacterOps;
import funcify.tool.container.SyncMap;
import funcify.trait.Trait;
import funcify.writer.StringTemplateWriter;
import funcify.writer.WriteResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author smccarron
 * @created 2021-08-29
 */
@AllArgsConstructor(staticName = "of")
public class DisjunctEnsembleTypeTemplate<V, R> implements TraitGenerationTemplate<V, R> {

    private static final Logger logger = LoggerFactory.getLogger(DisjunctEnsembleTypeTemplate.class);

    @Override
    public Set<Trait> getTraits() {
        return EnumSet.of(Trait.DISJUNCT);
    }

    @Override
    public List<String> getDestinationTypePackagePathSegments() {
        return Arrays.asList("funcify", "ensemble", "disjunct");
    }

    @Override
    public Path getStringTemplateGroupFilePath() {
        return Paths.get("antlr", "funcify", "disjunct_ensemble_type.stg");
    }

    @Override
    public TypeGenerationSession<V, R> createTypesForSession(final TypeGenerationSession<V, R> session) {
        logger.debug("create_types_for_session: [ {} ]",
                     SyncMap.empty()
                            .put("types", "DisjunctEnsemble[1..n]"));
        try {
            final StringTemplateWriter<V, R> templateWriter = session.getTemplateWriter();
            final SyncMap<EnsembleKind, WriteResult<R>> results = session.getDisjunctWrappableEnsembleFactoryTypeResults();
            for (EnsembleKind ek : session.getEnsembleKinds()) {
                final String className = getTraitNameForEnsembleKind(ek);
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
                                                              .put("ensemble_type_package", Arrays.asList("funcify", "ensemble"));
                final StringTemplateSpec spec = DefaultStringTemplateSpec.builder()
                                                                         .typeName(className)
                                                                         .typePackagePathSegments(
                                                                             getDestinationTypePackagePathSegments())
                                                                         .templateFunctionName(getStringTemplateGroupFileName())
                                                                         .fileTypeExtension(".java")
                                                                         .stringTemplateGroupFilePath(
                                                                             getStringTemplateGroupFilePath())
                                                                         .destinationParentDirectoryPath(session.getDestinationDirectoryPath())
                                                                         .templateFunctionParameterInput(params)
                                                                         .build();
                final WriteResult<R> writeResult = templateWriter.write(spec);
                results.put(ek, writeResult);
            }
            return session.withDisjunctWrappableEnsembleFactoryTypeResults(results);
        } catch (final Throwable t) {
            logger.debug("create_types_for_session: [ status: failed ] due to [ type: {}, message: {} ]",
                         t.getClass()
                          .getSimpleName(),
                         t.getMessage());
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new FuncifyCodeGenException(t.getMessage(), t);
            }
        }
    }
}
