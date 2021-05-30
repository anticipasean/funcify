package funcify.factory.definition;

import funcify.factory.session.EnsembleTypeGenerationSession.EnsembleTypeGenerationSessionWT;
import funcify.template.definition.MethodGenerationTemplate;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface EnsembleMethodGenerationFactory extends MethodGenerationTemplate<EnsembleTypeGenerationSessionWT>,
                                                         EnsembleCodeBlockGenerationFactory {

}
