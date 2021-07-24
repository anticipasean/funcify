package funcify.ensemble.basetype.template;

import funcify.ensemble.basetype.session.EnsembleTypeGenerationSession.ETSWT;
import funcify.base.template.CodeBlockGenerationTemplate;
import funcify.base.template.MethodGenerationTemplate;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface EnsembleMethodGenerationFactory extends MethodGenerationTemplate<ETSWT>, CodeBlockGenerationTemplate<ETSWT>,
                                                         EnsembleStatementGenerationFactory {

}
