package funcify.trait.template;

import funcify.base.session.TypeGenerationSession;
import funcify.base.template.TypeGenerationTemplate;
import funcify.typedef.JavaMethod;

/**
 * @author smccarron
 * @created 2021-07-03
 */
public interface TraitTemplate<SWT> extends TypeGenerationTemplate<SWT> {

    default JavaMethod emptyTraitMethodDefinition(final TypeGenerationTemplate<SWT> template,
                                                  final TypeGenerationSession<SWT> session,
                                                  final String traitMethodName) {
        return template.methodName(session,
                                   template.emptyMethodDefinition(session),
                                   traitMethodName);
    }

    TypeGenerationSession<SWT> buildOrApplyTrait(final TypeGenerationSession<SWT> session);

}
