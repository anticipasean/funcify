package funcify.trait.template;

import funcify.template.generation.TypeGenerationTemplate;
import funcify.template.session.TypeGenerationSession;
import funcify.typedef.JavaMethod;

/**
 * @author smccarron
 * @created 2021-07-03
 */
public interface TraitTemplate<SWT> extends TypeGenerationTemplate<SWT> {

    String getTraitMethodName();

    default JavaMethod emptyTraitMethodDefinition(final TypeGenerationTemplate<SWT> template,
                                                  final TypeGenerationSession<SWT> session) {
        return template.methodName(session,
                                   template.emptyMethodDefinition(session),
                                   getTraitMethodName());
    }

     TypeGenerationSession<SWT> applyTrait(final TypeGenerationSession<SWT> session);

}
