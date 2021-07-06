package funcify.trait.template;

import funcify.template.generation.TypeGenerationTemplate;
import funcify.template.session.TypeGenerationSession;

/**
 * @author smccarron
 * @created 2021-07-03
 */
public interface TraitTemplate<SWT> extends TypeGenerationTemplate<SWT> {

    String getTraitMethodName();

    default <TD, MD, CD, SD, ED> MD emptyTraitMethodDefinition(final TypeGenerationTemplate<SWT> template,
                                                               final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session) {
        return template.methodName(session,
                                   template.emptyMethodDefinition(session),
                                   getTraitMethodName());
    }

    <TD, MD, CD, SD, ED> TypeGenerationSession<SWT, TD, MD, CD, SD, ED> applyTrait(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session);

}
