package funcify.trait.design;

/**
 * @author smccarron
 * @created 2021-05-20
 */
public interface PeekableDesign extends TraitDesign {

    @Override
    default String getTraitMethodName() {
        return "peek";
    }
}
