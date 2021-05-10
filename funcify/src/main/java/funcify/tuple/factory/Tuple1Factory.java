package funcify.tuple.factory;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Solo;
import funcify.template.solo.conjunct.FlattenableConjunctSoloTemplate;
import funcify.tuple.Tuple1;
import funcify.tuple.Tuple1.Tuple1W;
import java.util.function.Function;
import lombok.AllArgsConstructor;

/**
 * @author smccarron
 * @created 2021-05-05
 */
public class Tuple1Factory implements FlattenableConjunctSoloTemplate<Tuple1W> {

    private static enum FactoryHolder {

        INSTANCE(new Tuple1Factory());

        private final Tuple1Factory tuple1Factory;

        FactoryHolder(final Tuple1Factory tuple1Factory) {
            this.tuple1Factory = tuple1Factory;
        }

        public Tuple1Factory getTuple1Factory() {
            return tuple1Factory;
        }
    }

    private Tuple1Factory() {

    }

    public static Tuple1Factory getInstance() {
        return FactoryHolder.INSTANCE.getTuple1Factory();
    }

    @Override
    public <A, B> B fold(final Solo<Tuple1W, A> container,
                         final Function<? super A, ? extends B> mapper) {
        return requireNonNull(container,
                              () -> "mapper").convert(Tuple1::narrowK)
                                             .fold(mapper);
    }

    @Override
    public <B> Solo<Tuple1W, B> from(final B value) {
        return new DefaultTuple1<>(value);
    }

    @AllArgsConstructor
    private static class DefaultTuple1<A> implements Tuple1<A> {

        private final A _1;

        @Override
        public A _1() {
            return _1;
        }

        @Override
        public <B> B fold(final Function<? super A, ? extends B> mapper) {
            return requireNonNull(mapper,
                                  () -> "mapper").apply(_1);
        }
    }
}
