

package ms.project.crdt;


import java.util.Set;

import com.google.common.annotations.VisibleForTesting;

import ms.project.crdt.utils.CRDTUtils;

/**
 * @author MSikder
 *
 * @param <E>
 */
public class LastWriteWinSet<E> {

    private GrowOnlySet<ItemTD<E>> addSet;

    private GrowOnlySet<ItemTD<E>> deleteSet;

    public LastWriteWinSet() {
        this.addSet = new GrowOnlySet<>();
        this.deleteSet = new GrowOnlySet<>();
    }

    LastWriteWinSet(GrowOnlySet<ItemTD<E>> addSet, GrowOnlySet<ItemTD<E>> deleteSet) {
        this.addSet = addSet;
        this.deleteSet = deleteSet;
    }
    
    /**
     * Lookup the data in the LWW set
     * @return 
     */
    public Set<E> getData() {
        Set<ItemTD<E>> lookup = addSet.getData();
        return CRDTUtils.filteredAndMapped(lookup, new CRDTUtils.CallbackOneItem<ItemTD<E>>() {
            @Override
            public boolean call(ItemTD<E> item) {
                return notDeleted(item);
            }
        }, new CRDTUtils.CallbackMapper<ItemTD<E>, E>() {
            @Override
            public E call(ItemTD<E> item) {
                return item.item;
            }
        });
    }
    
    /**
     * Adds a item/element to the set 
     * @param item
     */
    public void add(E item){
    	add(new ItemTD<>(System.currentTimeMillis(), item));
    }
    
    /**
     * Removes item/element from set
     * @param item
     */
    public void delete(E item){
    	delete(new ItemTD<>(System.currentTimeMillis(), item));
    }
    
    /**
     * Merges two LWW set 
     * @param anotherLastWriteWinSet
     * @return
     */
    public LastWriteWinSet<E> merge(LastWriteWinSet<E> anotherLastWriteWinSet) {
        return new LastWriteWinSet<>(addSet.merge(anotherLastWriteWinSet.addSet), deleteSet.merge(anotherLastWriteWinSet.deleteSet));
    }

    /**
     * Returns the difference of 2 LWW set
     * @param anotherLastWriteWinSet
     * @return
     */
    public LastWriteWinSet<E> diff(LastWriteWinSet<E> anotherLastWriteWinSet) {
        final LastWriteWinSet<E> mergeResult = merge(anotherLastWriteWinSet);
        return new LastWriteWinSet<>(
                mergeResult.getAddSet().diff(anotherLastWriteWinSet.getAddSet()),
                mergeResult.getDeleteSet().diff(anotherLastWriteWinSet.getDeleteSet()));
    }
    
    @VisibleForTesting
    void add(long timestamp, E item){
    	add(new ItemTD<>(timestamp, item));
    }
    
    @VisibleForTesting
    void delete(long timestamp, E item){
    	delete(new ItemTD<>(timestamp, item));
    }

    private void add(ItemTD<E> ItemTD) {
        addSet.add(ItemTD);
    }

    private void delete(ItemTD<E> ItemTD) {
    	deleteSet.add(ItemTD);
    }

    private boolean notDeleted(final ItemTD<E> addState) {
        Set<ItemTD<E>> deleted = CRDTUtils.filter(deleteSet.getData(),
                new CRDTUtils.CallbackOneItem<ItemTD<E>>() {
                    @Override
                    public boolean call(ItemTD<E> item) {
                        return item.getItem().equals(addState.getItem())
                                && item.getTimestamp() > addState.getTimestamp();
                    }
                });
        return deleted.isEmpty();
    }

    @VisibleForTesting
    GrowOnlySet<ItemTD<E>> getAddSet() {
        return new GrowOnlySet<ItemTD<E>>().merge(addSet);
    }

    @VisibleForTesting
    GrowOnlySet<ItemTD<E>> getDeleteSet() {
        return new GrowOnlySet<ItemTD<E>>().merge(deleteSet);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LastWriteWinSet<?> LastWriteWinSet = (LastWriteWinSet<?>) o;

        return addSet.equals(LastWriteWinSet.addSet) && deleteSet.equals(LastWriteWinSet.deleteSet);
    }

    @Override
    public int hashCode() {
        int result = addSet.hashCode();
        result = 31 * result + deleteSet.hashCode();
        return result;
    }
    
    public static class ItemTD<E> {
        private long timestamp;
        private E item;

        public ItemTD(long timestamp, E item) {
            this.timestamp = timestamp;
            this.item = item;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public E getItem() {
            return item;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ItemTD<?> that = (ItemTD<?>) o;

            return timestamp == that.timestamp && !(item != null ? !item.equals(that.item) : that.item != null);
        }

        @Override
        public int hashCode() {
            int result = (int) (timestamp ^ (timestamp >>> 32));
            result = 31 * result + (item != null ? item.hashCode() : 0);
            return result;
        }
    }
}
