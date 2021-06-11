package funcify.trait;

import funcify.template.generation.TypeGenerationTemplate;
import funcify.template.session.TypeGenerationSession;

/**
 * @author smccarron
 * @created 2021-05-20
 */
public interface FoldableDesign<TD, MD, CD, SD, ED> extends TraitDesign<TD, MD, CD, SD, ED> {

    <SWT> TD addFoldMethodDefinitionToDesignTypeDefinition(final TypeGenerationTemplate<SWT> template,
                                                           final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                           final TD designTypeDefinition);

    <SWT> TD addFoldMethodDefinitionToTemplateTypeDefinition(final TypeGenerationTemplate<SWT> template,
                                                             final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                             final TD templateTypeDefinition);

    <SWT> TD addFoldMethodDefinitionToDefaultImplementationTypeDefinition(final TypeGenerationTemplate<SWT> template,
                                                                          final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                                          final TD defaultImplementationTypeDefinition);


}
