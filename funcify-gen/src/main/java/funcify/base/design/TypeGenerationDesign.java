package funcify.base.design;

/**
 * @author smccarron
 * @created 2021-07-24
 */
public interface TypeGenerationDesign {

    TypeGenerationDesign createType(final String typeName);

    TypeGenerationDesign updateType();

    TypeGenerationDesign createMethod();

    TypeGenerationDesign updateMethod();
    
    

}
