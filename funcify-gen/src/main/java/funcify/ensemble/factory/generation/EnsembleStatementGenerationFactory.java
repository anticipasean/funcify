package funcify.ensemble.factory.generation;

import funcify.ensemble.factory.session.EnsembleTypeGenerationSession.ETSWT;
import funcify.template.generation.StatementGenerationTemplate;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface EnsembleStatementGenerationFactory extends StatementGenerationTemplate<ETSWT>,
                                                            EnsembleExpressionGenerationFactory {

}
