package funcify.trait.template;

import funcify.base.session.TypeGenerationSession;

/**
 * @author smccarron
 * @created 2021-07-03
 */
public interface FoldableTemplate<SWT> extends TraitTemplate<SWT> {

    @Override
    default String getTraitMethodName() {
        return "fold";
    }

    @Override
     TypeGenerationSession<SWT> applyTrait(final TypeGenerationSession<SWT> session);
}
