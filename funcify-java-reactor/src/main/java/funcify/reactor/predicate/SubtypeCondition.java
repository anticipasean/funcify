package funcify.reactor.predicate;

import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2/6/22
 */
public interface SubtypeCondition<PT, CT> extends Predicate<PT> {

    <CT1 extends PT> SubtypeCondition<PT, CT1> subtype(final Class<CT1> subtype,
                                                       final Predicate<? super CT1> subtypeCondition);

    @Override
    boolean test(PT parentTypeInstance);
}
