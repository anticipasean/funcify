package funcify.trait;

import funcify.ensemble.EnsembleKind;
import funcify.tool.StringOps;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author smccarron
 * @created 2021-09-08
 */
public enum Trait implements Sequential {

    MAPPABLE,
    FILTERABLE,
    TRAVERSABLE,
    ZIPPABLE,
    PEEKABLE,
    FLATTENABLE,
    WRAPPABLE,
    CONJUNCT,
    DISJUNCT;

    @Override
    public int getIndex() {
        return ordinal();
    }

    public static String generateTraitNameFrom(final EnsembleKind ek, final Trait... traits) {
        return Stream.concat(Stream.of(traits)
                                   .sorted(Sequential::relativeTo)
                                   .map(Trait::name)
                                   .map(String::toLowerCase)
                                   .map(StringOps.firstLetterCapitalizer()), Stream.of(ek.getSimpleClassName()))
                     .collect(Collectors.joining(""));

    }

    public static <I extends Iterable<Trait>> String generateTraitNameFrom(final EnsembleKind ek, final I traits) {
        return Stream.concat(Stream.of(traits)
                                   .flatMap(i -> StreamSupport.stream(i.spliterator(), false))
                                   .sorted(Sequential::relativeTo)
                                   .map(Trait::name)
                                   .map(String::toLowerCase)
                                   .map(StringOps.firstLetterCapitalizer()), Stream.of(ek.getSimpleClassName()))
                     .collect(Collectors.joining(""));

    }
}
