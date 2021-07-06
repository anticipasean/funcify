package funcify.trait.template;

import funcify.template.session.TypeGenerationSession;

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
    <TD, MD, CD, SD, ED> TypeGenerationSession<SWT, TD, MD, CD, SD, ED> applyTrait(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session);
}
