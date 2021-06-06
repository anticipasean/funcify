package funcify.tool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author smccarron
 * @created 2021-05-27
 */
public interface JavaMapOps {

    static <K, V> Map<K, V> fromPair(final K k,
                                     final V v) {
        Map<K, V> map = new HashMap<>();
        map.put(k,
                v);
        return map;
    }

    static <K, V, KI extends Iterable<? extends K>, VI extends Iterable<? extends V>> Map<K, V> fromIterablePairs(final KI kIterable,
                                                                                                                  final VI vIterable) {
        final Iterator<? extends K> kIterator = kIterable.iterator();
        final Iterator<? extends V> vIterator = vIterable.iterator();
        final Map<K, V> map = new HashMap<>();
        while (kIterator.hasNext() && vIterator.hasNext()) {
            map.put(kIterator.next(),
                    vIterator.next());
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    static <T> Map<T, T> fromPairsOfSameType(final T... pairs) {
        Map<T, T> map = new HashMap<>();
        final int len = pairs.length;
        T key = null;
        for (int i = 0; i < len; i++) {
            if ((i & 1) == 0) {
                key = pairs[i];
            } else {
                map.put(key,
                        pairs[i]);
            }
        }
        return map;
    }

}
