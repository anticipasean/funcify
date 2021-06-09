package funcify.factory.generation;

import funcify.factory.session.EnsembleTypeGenerationSession.EnsembleTypeGenerationSessionWT;
import funcify.template.generation.StatementGenerationTemplate;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface EnsembleStatementGenerationFactory extends StatementGenerationTemplate<EnsembleTypeGenerationSessionWT>,
                                                            EnsembleExpressionGenerationFactory {

}