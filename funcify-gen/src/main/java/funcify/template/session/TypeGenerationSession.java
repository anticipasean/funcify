package funcify.template.session;

/**
 * @param <SWT> - Session Witness Type
 * @author smccarron
 * @created 2021-05-28
 */
public interface TypeGenerationSession<SWT, TD, MD, CD, SD, ED> extends MethodGenerationSession<SWT, TD, MD, CD, SD, ED> {

    TD emptyTypeDefinition();


}
