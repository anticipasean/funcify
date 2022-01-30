package funcify.ensemble.trait.factory.flattenable;

import funcify.ensemble.EnsembleKind;
import funcify.ensemble.template.TraitFactoryGenerationTemplate;
import funcify.error.FuncifyCodeGenException;
import funcify.session.TypeGenerationSession;
import funcify.spec.DefaultStringTemplateSpec;
import funcify.spec.StringTemplateSpec;
import funcify.tool.CharacterOps;
import funcify.tool.container.SyncMap;
import funcify.trait.Trait;
import funcify.writer.StringTemplateWriter;
import funcify.writer.WriteResult;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author smccarron
 * @created 2021-08-29
 */
@AllArgsConstructor(staticName = "of")
public class FlattenableDisjunctFactoryTypeTemplate<V, R> implements TraitFactoryGenerationTemplate<V, R> {

    private static final Logger logger = LoggerFactory.getLogger(FlattenableDisjunctFactoryTypeTemplate.class);

    @Override
    public Set<Trait> getTraits() {
        return EnumSet.of(Trait.DISJUNCT, Trait.FLATTENABLE);
    }

    @Override
    public List<String> getDestinationTypePackagePathSegments() {
        return Arrays.asList("funcify", "trait", "factory", "flattenable", "disjunct");
    }

    @Override
    public Path getStringTemplateGroupFilePath() {
        return Paths.get("antlr", "funcify", "ensemble", "trait", "factory", "flattenable", "flattenable_disjunct_factory_type.stg");
    }

    @Override
    public TypeGenerationSession<V, R> createTypesForSession(final TypeGenerationSession<V, R> session) {
        logger.debug("create_types_for_session: [ {} ]",
                     SyncMap.empty().put("types", "FlattenableDisjunctEnsembleFactory[1..n]"));
        try {
            final StringTemplateWriter<V, R> templateWriter = session.getTemplateWriter();
            final SyncMap<EnsembleKind, WriteResult<R>> results = session.getDisjunctMappableEnsembleFactoryTypeResults();
            for (EnsembleKind ek : session.getEnsembleKinds()) {
                final String className = getTraitNameForEnsembleKind(ek) + "Factory";
                final SyncMap<String, Object> params = SyncMap.of("package",
                                                                  getDestinationTypePackagePathSegments(),
                                                                  "class_name",
                                                                  className,
                                                                  "type_variables",
                                                                  CharacterOps.firstNUppercaseLettersWithNumericIndexExtension(ek.getNumberOfValueParameters())
                                                                              .collect(Collectors.toList()),
                                                                  "next_type_variable",
                                                                  CharacterOps.uppercaseLetterByIndexWithNumericExtension(ek.getNumberOfValueParameters()))
                                                              .put("implemented_type",
                                                                   getImplementedTypeInstance(ek,
                                                                                              Trait.DISJUNCT,
                                                                                              Trait.WRAPPABLE))
                                                              .put("next_type_variable_sequences",
                                                                   nextTypeVariableSequences(ek.getNumberOfValueParameters()))
                                                              .put("flat_map_impl_sequences",
                                                                   createFlatMapImplementationSequences(CharacterOps.firstNUppercaseLettersWithNumericIndexExtension(
                                                                                                                            ek.getNumberOfValueParameters())
                                                                                                                    .collect(
                                                                                                                            Collectors.toList()),
                                                                                                        nextTypeVariableSequences(
                                                                                                                ek.getNumberOfValueParameters())))
                                                              .put("container_type",
                                                                   getContainerTypeJsonInstanceFor(ek, Trait.DISJUNCT));
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
                if (writeResult.isFailure()) {
                    throw writeResult.getFailureValue().orElseThrow(() -> new FuncifyCodeGenException("failure value missing"));
                }
                results.put(ek, writeResult);
            }
            return session.withDisjunctFlattenableEnsembleFactoryTypeResults(results);
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

    private List<List<String>> createFlatMapImplementationSequences(final List<String> typeVariables,
                                                                    final List<List<String>> nextTypeVariableSequences) {
        return nextTypeVariableSequences.stream().reduce(new ArrayList<>(), (seqList, seq) -> {
            final Iterator<String> typeVarIter = typeVariables.iterator();
            final Iterator<String> nextTypeVarSeqIter = seq.iterator();
            final List<String> currentSeq = new ArrayList<>();
            int index = 0;
            while (typeVarIter.hasNext() && nextTypeVarSeqIter.hasNext()) {
                final String typeVar = typeVarIter.next();
                final String nextTypeVar = nextTypeVarSeqIter.next();
                final String text;
                if (typeVar.equals(nextTypeVar)) {
                    text = String.format("(%s input%s) -> this.<%s>wrap%d(input%s)",
                                         typeVar,
                                         typeVar,
                                         seq.stream().collect(Collectors.joining(", ")),
                                         ++index,
                                         typeVar);
                } else {
                    ++index;
                    text = String.format("(%s input%s) -> flatMapper.<%s>apply(input%s)",
                                         typeVar,
                                         typeVar,
                                         seq.stream().collect(Collectors.joining(", ")),
                                         typeVar);
                }
                currentSeq.add(text);
            }
            seqList.add(currentSeq);
            return seqList;
        }, (l1, l2) -> {
            l1.addAll(l2);
            return l1;
        });
    }

    private List<List<String>> nextTypeVariableSequences(final int numberOfValueParameters) {
        final String nextTypeVariable = CharacterOps.uppercaseLetterByIndexWithNumericExtension(numberOfValueParameters);
        final String[] array = CharacterOps.firstNUppercaseLettersWithNumericIndexExtension(numberOfValueParameters)
                                           .toArray(String[]::new);
        return IntStream.range(0, numberOfValueParameters).mapToObj(i -> {
            return Stream.concat(Stream.concat(Arrays.stream(array, 0, i), Stream.of(nextTypeVariable)),
                                 Arrays.stream(array, i + 1, numberOfValueParameters)).collect(Collectors.toList());
        }).collect(Collectors.toList());

    }
}
