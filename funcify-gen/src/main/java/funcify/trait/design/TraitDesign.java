package funcify.trait.design;

import funcify.base.session.TypeGenerationSession;
import funcify.trait.template.TraitTemplate;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface TraitDesign {

    String getTraitMethodName();



    default <SWT> TypeGenerationSession<SWT> fold(final TraitTemplate<SWT> template,
                                                  final TypeGenerationSession<SWT> session) {
        return template.buildOrApplyTrait(session);
    }

}
