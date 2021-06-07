package funcify.ensemble;

import funcify.factory.session.EnsembleTypeGenerationSession.EnsembleTypeGenerationSessionWT;
import funcify.template.generation.TypeGenerationTemplate;
import funcify.template.session.TypeGenerationSession;

/**
 * @author smccarron
 * @created 2021-06-06
 */
public interface EnsembleInterfaceTypeDesign {


    <TD, MD, CD, SD, ED> TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> fold(final TypeGenerationTemplate<EnsembleTypeGenerationSessionWT> template,
                                                                                                         final TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> session);

}
