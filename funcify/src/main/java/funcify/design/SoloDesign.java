package funcify.design;

import funcify.ensemble.Solo;
import funcify.template.solo.FlattenableSoloTemplate;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-05-03
 */
public interface SoloDesign<A> {

    default <B> SoloDesign<B> flatMap(Function<A, SoloDesign<B>> flatMapper) {
        return new FlattenableSoloDesign<A, B>(flatMapper, this);
    }

    <W, B> Solo<W, B> fold(FlattenableSoloTemplate<W> template);

}
