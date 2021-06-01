package funcify.factory.definition;

import funcify.factory.session.EnsembleTypeGenerationSession.EnsembleTypeGenerationSessionWT;
import funcify.template.generation.CodeBlockGenerationTemplate;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface EnsembleCodeBlockGenerationFactory extends CodeBlockGenerationTemplate<EnsembleTypeGenerationSessionWT>,
                                                            EnsembleStatementGenerationFactory {

}
