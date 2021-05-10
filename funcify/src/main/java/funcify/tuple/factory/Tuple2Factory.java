package funcify.tuple.factory;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Duet;
import funcify.template.duet.FlattenableConjunctDuetTemplate;
import funcify.tuple.Tuple2;
import funcify.tuple.Tuple2.Tuple2W;
import java.util.function.BiFunction;
import lombok.AllArgsConstructor;

/**
 * @author smccarron
 * @created 2021-05-05
 */
public class Tuple2Factory implements FlattenableConjunctDuetTemplate<Tuple2W> {

    private static enum FactoryHolder {

        INSTANCE(new Tuple2Factory());

        private final Tuple2Factory tuple2Factory;

        FactoryHolder(final Tuple2Factory tuple2Factory) {
            this.tuple2Factory = tuple2Factory;
        }

        public Tuple2Factory getTuple2Factory() {
            return tuple2Factory;
        }
    }

    private Tuple2Factory() {
    }

    public static Tuple2Factory getInstance() {
        return FactoryHolder.INSTANCE.getTuple2Factory();
    }

    @Override
    public <A, B, C> C fold(final Duet<Tuple2W, A, B> container,
                            final BiFunction<? super A, ? super B, ? extends C> mapper) {
        return requireNonNull(container,
                              () -> "container").convert(Tuple2::narrowK)
                                                .fold(mapper);
    }

    @Override
    public <A, B> Duet<Tuple2W, A, B> from(final A value1,
                                           final B value2) {
        return new DefaultTuple2<>(value1,
                                   value2);
    }

    @AllArgsConstructor
    private static class DefaultTuple2<A, B> implements Tuple2<A, B> {

        private final A _1;

        private final B _2;


        @Override
        public <C> C fold(final BiFunction<? super A, ? super B, ? extends C> mapper) {
            return requireNonNull(mapper,
                                  () -> "mapper").apply(_1,
                                                        _2);
        }

        @Override
        public A _1() {
            return _1;
        }

        @Override
        public B _2() {
            return _2;
        }
    }
}
