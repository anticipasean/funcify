package funcify.ensemble.basetype.template;

import funcify.ensemble.basetype.session.EnsembleTypeGenerationSession.ETSWT;
import funcify.base.template.StatementGenerationTemplate;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface EnsembleStatementGenerationFactory extends StatementGenerationTemplate<ETSWT>,
                                                            EnsembleExpressionGenerationFactory {

}
