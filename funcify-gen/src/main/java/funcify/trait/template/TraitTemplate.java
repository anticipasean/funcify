package funcify.trait.template;

import funcify.base.session.TypeGenerationSession;
import funcify.base.template.TypeGenerationTemplate;
import funcify.typedef.JavaMethod;
import funcify.typedef.JavaTypeDefinition;

/**
 * @author smccarron
 * @created 2021-07-03
 */
public interface TraitTemplate<SWT> extends TypeGenerationTemplate<SWT> {

    default JavaTypeDefinition emptyTraitTypeDefinition(final TypeGenerationSession<SWT> session,
                                                        final String traitName) {
        return typeName(session,
                        emptyTypeDefinition(session),
                        traitName);
    }

    default JavaMethod emptyTraitMethodDefinition(final TypeGenerationSession<SWT> session,
                                                  final String traitMethodName) {
        return methodName(session,
                          emptyMethodDefinition(session),
                          traitMethodName);
    }

    TypeGenerationSession<SWT> buildOrApplyTrait(final TypeGenerationSession<SWT> session);

}
