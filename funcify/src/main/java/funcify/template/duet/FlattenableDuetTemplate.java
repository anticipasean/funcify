package funcify.template.duet;

import funcify.ensemble.Duet;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface FlattenableDuetTemplate<W> extends ZippableDuetTemplate<W> {

    default <A, B> Duet<W, A, B> widenK(final Duet<W, ? extends A, ? extends B> container) {
        return Duet.widen(container);
    }

    default <A, B> Duet<W, A, B> narrowK(final Duet<W, ? super A, ? super B> container) {
        return Duet.narrow(container);
    }

}
