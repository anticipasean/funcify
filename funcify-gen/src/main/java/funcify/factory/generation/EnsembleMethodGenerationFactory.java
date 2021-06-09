package funcify.factory.generation;

import funcify.factory.session.EnsembleTypeGenerationSession.EnsembleTypeGenerationSessionWT;
import funcify.template.generation.MethodGenerationTemplate;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface EnsembleMethodGenerationFactory extends MethodGenerationTemplate<EnsembleTypeGenerationSessionWT>,
                                                         EnsembleCodeBlockGenerationFactory {

}
