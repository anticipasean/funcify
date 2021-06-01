package funcify.factory.definition;

import funcify.factory.session.EnsembleTypeGenerationSession.EnsembleTypeGenerationSessionWT;
import funcify.template.generation.TypeGenerationTemplate;

/**
 * @author smccarron
 * @created 2021-05-28
 */
public interface EnsembleTypeGenerationFactory extends TypeGenerationTemplate<EnsembleTypeGenerationSessionWT>,
                                                       EnsembleMethodGenerationFactory {


}
