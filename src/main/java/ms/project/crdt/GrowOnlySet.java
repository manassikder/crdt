package ms.project.crdt;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.common.annotations.VisibleForTesting;

import ms.project.crdt.utils.CRDTUtils;

/**
 * @author MSikder
 *
 * @param <E>
 */
public class GrowOnlySet<E> {

    private Set<E> set;

    public GrowOnlySet() {
        set = new HashSet<>();
    }

    GrowOnlySet(Set<E> set) {
        this.set = new HashSet<>(set);
    }

    /**
     * @return
     */
    public Set<E> getData() {
        return Collections.unmodifiableSet(set);
    }

    /**
     * @param elem
     */
    public void add(final E elem) {
        set.add(elem);
    }

    /**
     * @param anotherGSet
     * @return
     */
    public GrowOnlySet<E> merge(GrowOnlySet<E> anotherGSet) {
        final HashSet<E> newSet = new HashSet<>(set);
        newSet.addAll(anotherGSet.getSet());
        return new GrowOnlySet<>(newSet);
    }

    /**
     * @param anotherGSet
     * @return
     */
    public GrowOnlySet<E> diff(GrowOnlySet<E> anotherGSet) {
        return new GrowOnlySet<>(CRDTUtils.diff(set, anotherGSet.getData()));
    }

    @VisibleForTesting
    Set<E> getSet() {
        return set;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GrowOnlySet<?> gSet = (GrowOnlySet<?>) o;

        return set.equals(gSet.set);

    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }
}
