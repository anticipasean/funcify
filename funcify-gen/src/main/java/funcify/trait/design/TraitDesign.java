package funcify.trait.design;

import funcify.template.session.TypeGenerationSession;
import funcify.trait.template.TraitTemplate;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface TraitDesign {


    default <SWT> TypeGenerationSession<SWT> fold(final TraitTemplate<SWT> template,
                                                                      final TypeGenerationSession<SWT> session) {
        return template.applyTrait(session);
    }

}
