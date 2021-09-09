package funcify.trait;

/**
 * @author smccarron
 * @created 2021-09-08
 */
public enum Trait implements Sequential {

    MAPPABLE,
    FILTERABLE,
    TRAVERSABLE,
    STREAMABLE,
    PEEKABLE,
    FLATTENABLE,
    WRAPPABLE,
    CONJUNCT,
    DISJUNCT;


    @Override
    public int getIndex() {
        return ordinal();
    }
}
