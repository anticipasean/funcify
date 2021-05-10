package funcify.tuple.factory;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Trio;
import funcify.function.Fn3;
import funcify.template.trio.conjunct.FlattenableConjunctTrioTemplate;
import funcify.tuple.Tuple3;
import funcify.tuple.Tuple3.Tuple3W;
import lombok.AllArgsConstructor;

/**
 * @author smccarron
 * @created 2021-05-07
 */
public class Tuple3Factory implements FlattenableConjunctTrioTemplate<Tuple3W> {

    private static enum FactoryHolder {

        INSTANCE(new Tuple3Factory());

        private final Tuple3Factory tuple3Factory;

        FactoryHolder(final Tuple3Factory tuple3Factory) {
            this.tuple3Factory = tuple3Factory;
        }

        public Tuple3Factory getTuple3Factory() {
            return tuple3Factory;
        }
    }

    private Tuple3Factory() {
    }

    public static Tuple3Factory getInstance() {
        return FactoryHolder.INSTANCE.getTuple3Factory();
    }

    @Override
    public <A, B, C> Trio<Tuple3W, A, B, C> from(final A value1,
                                                 final B value2,
                                                 final C value3) {
        return new DefaultTuple3<>(value1,
                                   value2,
                                   value3);
    }


    @Override
    public <A, B, C, D> D fold(final Trio<Tuple3W, A, B, C> container,
                               final Fn3<? super A, ? super B, ? super C, ? extends D> mapper) {
        return null;
    }

    @AllArgsConstructor
    private static class DefaultTuple3<A, B, C> implements Tuple3<A, B, C> {

        private final A _1;
        private final B _2;
        private final C _3;

        @Override
        public A _1() {
            return _1;
        }

        @Override
        public B _2() {
            return _2;
        }

        @Override
        public C _3() {
            return _3;
        }

        @Override
        public <D> D fold(final Fn3<? super A, ? super B, ? super C, ? extends D> mapper) {
            return requireNonNull(mapper,
                                  () -> "mapper").apply(_1,
                                                        _2,
                                                        _3);
        }
    }
}
