package funcify.design.solo;

import funcify.design.solo.disjunct.DisjunctSolo;
import funcify.ensemble.Solo;
import funcify.function.Fn0;
import funcify.function.Fn1;
import funcify.option.Option;
import funcify.template.solo.FilterableSoloTemplate;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface FilterableSolo<W, A> extends Solo<W, A> {

    FilterableSoloTemplate<W> factory();

    default Option<A> filter(final Predicate<? super A> condition) {
        return factory().filter(this,
                                condition);
    }

    default <WDS> DisjunctSolo<WDS, A> filter(final Predicate<? super A> condition,
                                              final Fn1<? super A, ? extends DisjunctSolo<WDS, A>> ifFitsCondition,
                                              final Fn0<? extends DisjunctSolo<WDS, A>> ifNotFitsCondition) {
        return factory().<A, WDS, DisjunctSolo<WDS, A>>filter(this,
                                                              ifFitsCondition,
                                                              ifNotFitsCondition,
                                                              condition).narrowT1();
    }

}
