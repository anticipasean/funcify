package funcify.trait;

import funcify.template.generation.TypeGenerationTemplate;
import funcify.template.session.TypeGenerationSession;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface Trait {


    <SWT, TD, MD, CD, SD, ED> TypeGenerationSession<SWT, TD, MD, CD, SD, ED> fold(final TypeGenerationTemplate<SWT> template,
                                                                                  final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session);

}
