package funcify.trait;

/**
 * @author smccarron
 * @created 2021-09-08
 */
public interface Sequential {

    int getIndex();

    default int relativeTo(final Sequential other) {
        return Integer.compare(getIndex(),
                               other.getIndex());
    }

    default boolean occursBefore(final Sequential other) {
        return relativeTo(other) < 1;
    }

    default boolean occursAtOrBefore(final Sequential other) {
        return relativeTo(other) <= 1;
    }

    default boolean occursAtSameTime(final Sequential other) {
        return relativeTo(other) == 1;
    }

    default boolean occursAfter(final Sequential other) {
        return relativeTo(other) > 1;
    }

    default boolean occursAtOrAfter(final Sequential other) {
        return relativeTo(other) >= 1;
    }

}
