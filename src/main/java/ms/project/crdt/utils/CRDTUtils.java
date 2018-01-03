
package ms.project.crdt.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MSikder
 *
 */
public final class CRDTUtils {
	
    private CRDTUtils() {
        // No instances
    }

    /**
     * @param firstSet
     * @param secondSet
     * @return
     */
    public static <E> Set<E> diff(Set<E> firstSet, final Set<E> secondSet) {
        return filter(firstSet, new CallbackOneItem<E>() {
            @Override
            public boolean call(E element) {
                return !secondSet.contains(element);
            }
        });
    }

    /**
     * @param set
     * @param callback
     * @return
     */
    public static <E> Set<E> filter(Set<E> set, CallbackOneItem<E> callback) {
        final Set<E> newSet = new HashSet<>();
        for (E element : set) {
            if (callback.call(element)) {
                newSet.add(element);
            }
        }
        return newSet;
    }

    /**
     * @param set
     * @param callback
     * @param mapper
     * @return
     */
    public static <E, R> Set<R> filteredAndMapped(Set<E> set, CallbackOneItem<E> callback,
    		CallbackMapper<E, R> mapper) {
        final Set<R> newSet = new HashSet<>();
        for (E element : set) {
            if (callback.call(element)) {
                newSet.add(mapper.call(element));
            }
        }
        return newSet;
    }

    public interface CallbackOneItem<E> {
        boolean call(E item);
    }

    public interface CallbackTwoItem<E, F> {
        boolean call(E first, F second);
    }

    public interface CallbackMapper<E, R> {
        R call(E element);
    }
}
