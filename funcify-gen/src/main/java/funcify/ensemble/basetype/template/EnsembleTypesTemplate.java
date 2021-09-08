package funcify.ensemble.basetype.template;

import funcify.ensemble.EnsembleKind;
import funcify.ensemble.basetype.session.TypeGenerationSession;
import funcify.error.FuncifyCodeGenException;
import funcify.spec.DefaultStringTemplateSpec;
import funcify.spec.StringTemplateSpec;
import funcify.template.TypeGenerationTemplate;
import funcify.tool.CharacterOps;
import funcify.tool.container.SyncMap;
import funcify.writer.StringTemplateWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author smccarron
 * @created 2021-08-28
 */
@AllArgsConstructor(staticName = "of")
public class EnsembleTypesTemplate<V, R> implements TypeGenerationTemplate<V, R> {

    private static final Logger logger = LoggerFactory.getLogger(EnsembleTypesTemplate.class);

    @Override
    public List<String> getDestinationTypePackagePathSegments() {
        return Arrays.asList("funcify",
                             "ensemble");
    }

    @Override
    public Path getStringTemplateGroupFilePath() {
        return Paths.get("antlr",
                         "funcify",
                         "ensemble_type.stg");
    }

    @Override
    public TypeGenerationSession<V, R> createTypesForSession(final TypeGenerationSession<V, R> session) {
        logger.debug("create_types_for_session: [ {} ]",
                     SyncMap.empty()
                            .put("types",
                                 "Ensemble")
                            .put("ensemble_kinds.count",
                                 session.getEnsembleKinds()
                                        .size()));
        try {
            final StringTemplateWriter<V, R> templateWriter = session.getTemplateWriter();
            final StringTemplateSpec ensembleBaseTypeSpec = DefaultStringTemplateSpec.builder()
                                                                                     .stringTemplateGroupFilePath(getStringTemplateGroupFilePath())
                                                                                     .typeName("Ensemble")
                                                                                     .fileTypeExtension(".java")
                                                                                     .templateFunctionName("base_ensemble_type")
                                                                                     .destinationParentDirectoryPath(session.getDestinationDirectoryPath())
                                                                                     .templateFunctionParameterInput(SyncMap.of("package",
                                                                                                                                getDestinationTypePackagePathSegments()))
                                                                                     .build();

            final R baseEnsembleTypeResult = templateWriter.write(ensembleBaseTypeSpec);
            final TypeGenerationSession<V, R> updatedSession = session.withBaseEnsembleTypeResult(baseEnsembleTypeResult);
            final SyncMap<EnsembleKind, R> ensembleTypeResultsByEnsembleKind = session.getEnsembleTypeResultsByEnsembleKind();
            for (EnsembleKind ek : session.getEnsembleKinds()) {
                final SyncMap<String, Object> params = SyncMap.of("package",
                                                                  getDestinationTypePackagePathSegments(),
                                                                  "class_name",
                                                                  ek.getSimpleClassName(),
                                                                  "is_solo",
                                                                  ek == EnsembleKind.SOLO,
                                                                  "type_variables",
                                                                  CharacterOps.firstNUppercaseLettersWithNumericIndexExtension(ek.getNumberOfValueParameters())
                                                                              .collect(Collectors.toList()),
                                                                  "next_type_variable",
                                                                  CharacterOps.uppercaseLetterByIndexWithNumericExtension(ek.getNumberOfValueParameters())
                                                                              .orElse(null));
                final StringTemplateSpec spec = DefaultStringTemplateSpec.builder()
                                                                         .typePackagePathSegments(getDestinationTypePackagePathSegments())
                                                                         .stringTemplateGroupFilePath(getStringTemplateGroupFilePath())
                                                                         .typeName(ek.getSimpleClassName())
                                                                         .fileTypeExtension(".java")
                                                                         .templateFunctionName("ensemble_type")
                                                                         .destinationParentDirectoryPath(session.getDestinationDirectoryPath())
                                                                         .templateFunctionParameterInput(params)
                                                                         .build();
                final R ensembleTypeResult = templateWriter.write(spec);
                ensembleTypeResultsByEnsembleKind.put(ek,
                                                      ensembleTypeResult);
            }
            return updatedSession.withEnsembleTypeResultsByEnsembleKind(ensembleTypeResultsByEnsembleKind);
        } catch (final Throwable t) {
            logger.debug("create_types_for_session: [ status: failed ] due to [ type: {}, message: {} ]",
                         t.getClass()
                          .getSimpleName(),
                         t.getMessage());
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new FuncifyCodeGenException(t.getMessage(),
                                                  t);
            }
        }
    }
}
