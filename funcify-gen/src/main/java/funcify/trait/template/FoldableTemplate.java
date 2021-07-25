package funcify.trait.template;

import funcify.base.session.TypeGenerationSession;

/**
 * @author smccarron
 * @created 2021-07-03
 */
public interface FoldableTemplate<SWT> extends TraitTemplate<SWT> {


    @Override
     TypeGenerationSession<SWT> buildOrApplyTrait(final TypeGenerationSession<SWT> session);
}
