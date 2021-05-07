package funcify.flattenable;

import funcify.ensemble.Duet;
import funcify.template.duet.FlattenableDuetTemplate;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-30
 */
public interface FlattenableDuet<W, A, B> extends Duet<W, A, B> {

    FlattenableDuetTemplate<W> factory();

    default <C> FlattenableDuet<W, C, B> mapFirst(final Function<? super A, ? extends C> mapper) {
        return factory().mapFirst(this,
                                  mapper)
                        .narrowT2();
    }

    default <C> FlattenableDuet<W, A, C> mapSecond(final Function<? super B, ? extends C> mapper) {
        return factory().mapSecond(this,
                                   mapper)
                        .narrowT1();
    }

//    default <C, D> FlattenableDuet<W, B, C> bimap(final Function<? super A, ? extends C> mapper1,
//                                                  final Function<? super B, ? extends D> mapper2) {
//        return factory().bimap(this,
//                               mapper1,
//                               mapper2)
//                        .narrowT2();
//    }

    default <C> FlattenableDuet<W, C, B> zipFirst(final FlattenableDuet<W, A, B> container,
                                                  final BiFunction<? super A, ? super A, ? extends C> combiner) {
        return factory().zipFirst(this,
                                  container,
                                  combiner)
                        .narrowT2();
    }


    default <C> FlattenableDuet<W, A, C> zipSecond(final FlattenableDuet<W, A, B> container,
                                                   final BiFunction<? super B, ? super B, ? extends C> combiner) {
        return factory().zipSecond(this,
                                   container,
                                   combiner)
                        .narrowT1();
    }

    default <C> FlattenableDuet<W, C, B> flatMapFirst(final Function<? super A, ? extends FlattenableDuet<W, C, B>> flatMapper) {
        return factory().flatMapFirst(this,
                                      flatMapper)
                        .narrowT1();
    }

    default <C> FlattenableDuet<W, A, C> flatMapSecond(final Function<? super B, ? extends FlattenableDuet<W, A, C>> flatMapper) {
        return factory().flatMapSecond(this,
                                       flatMapper)
                        .narrowT1();
    }

}
