package funcify.ensemble.factory.generation;

import funcify.ensemble.factory.session.EnsembleTypeGenerationSession.ETSWT;
import funcify.template.generation.CodeBlockGenerationTemplate;
import funcify.template.generation.MethodGenerationTemplate;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface EnsembleMethodGenerationFactory extends MethodGenerationTemplate<ETSWT>, CodeBlockGenerationTemplate<ETSWT>,
                                                         EnsembleStatementGenerationFactory {

}
