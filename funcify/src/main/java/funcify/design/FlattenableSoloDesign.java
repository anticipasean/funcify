package funcify.design;

import funcify.ensemble.Solo;
import funcify.template.solo.FlattenableSoloTemplate;
import java.util.function.Function;
import lombok.AllArgsConstructor;

/**
 * @author smccarron
 * @created 2021-05-03
 */
@AllArgsConstructor
public class FlattenableSoloDesign<A, B> implements SoloDesign<B> {

    private final Function<A, SoloDesign<B>> flatMapper;

    private final SoloDesign<A> soloDesign;

    @Override
    public <W, B> Solo<W, B> fold(final FlattenableSoloTemplate<W> template) {
        return template.flatMap(soloDesign.fold(template),
                                flatMapper.andThen(sd -> sd.fold(template)));
    }
}
