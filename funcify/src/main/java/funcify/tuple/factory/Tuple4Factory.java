package funcify.tuple.factory;

import funcify.ensemble.Quartet;
import funcify.function.Fn4;
import funcify.template.quartet.conjunct.FlattenableConjunctQuartetTemplate;
import funcify.tuple.Tuple4;
import funcify.tuple.Tuple4.Tuple4W;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public class Tuple4Factory implements FlattenableConjunctQuartetTemplate<Tuple4W> {


    @Override
    public <A, B, C, D> Quartet<Tuple4W, A, B, C, D> from(final A value1,
                                                          final B value2,
                                                          final C value3,
                                                          final D value4) {
        return null;
    }

    @Override
    public <A, B, C, D, E> E fold(final Quartet<Tuple4W, A, B, C, D> container,
                                  final Fn4<? super A, ? super B, ? super C, ? super D, ? extends E> mapper) {
        return null;
    }

    private static class DefaultTuple4<A, B, C, D> implements Tuple4<A, B, C, D> {

    }
}
