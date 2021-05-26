package funcify.tuple.factory;

import funcify.ensemble.Quartet;
import funcify.function.Fn4;
import funcify.template.quartet.conjunct.FlattenableConjunctQuartetTemplate;
import funcify.tuple.Tuple4;
import funcify.tuple.Tuple4.Tuple4W;
import java.util.Objects;
import lombok.AllArgsConstructor;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public class Tuple4Factory implements FlattenableConjunctQuartetTemplate<Tuple4W> {

    private static enum FactoryHolder {
        INSTANCE(new Tuple4Factory());

        private final Tuple4Factory tuple4Factory;

        FactoryHolder(final Tuple4Factory tuple4Factory) {
            this.tuple4Factory = tuple4Factory;
        }

        public Tuple4Factory getTuple4Factory() {
            return tuple4Factory;
        }
    }

    public static Tuple4Factory getInstance() {
        return FactoryHolder.INSTANCE.getTuple4Factory();
    }


    @Override
    public <A, B, C, D> Quartet<Tuple4W, A, B, C, D> from(final A value1,
                                                          final B value2,
                                                          final C value3,
                                                          final D value4) {
        return new DefaultTuple4<>(value1,
                                   value2,
                                   value3,
                                   value4);
    }

    @Override
    public <A, B, C, D, E> E fold(final Quartet<Tuple4W, A, B, C, D> container,
                                  final Fn4<? super A, ? super B, ? super C, ? super D, ? extends E> mapper) {
        return Objects.requireNonNull(container, () -> "container").convert(Tuple4::narrowK);
    }

    @AllArgsConstructor
    private static class DefaultTuple4<A, B, C, D> implements Tuple4<A, B, C, D> {

        private final A _1;
        private final B _2;
        private final C _3;
        private final D _4;


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
        public D _4() {
            return _4;
        }
    }
}
