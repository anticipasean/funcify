package funcify.trait.design;

import funcify.template.session.TypeGenerationSession;
import funcify.trait.template.TraitTemplate;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface TraitDesign<TD, MD, CD, SD, ED> {


    default <SWT> TypeGenerationSession<SWT, TD, MD, CD, SD, ED> fold(final TraitTemplate<SWT> template,
                                                                      final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session) {
        return template.applyTrait(session);
    }

}
