package bg.nemetschek.nbastats.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FixedSortedList <E> extends AbstractList<E> {

    private int maxSize;
    private Comparator<E> comparator;
    private ArrayList<E> internalList;

    public FixedSortedList(int maxSize, Comparator<E> comparator) {
        super();

        this.maxSize = maxSize;
        this.comparator = comparator;
        this.internalList = new ArrayList<>(maxSize);
    }

    public synchronized void tryAdd(E e) {
        if (size() < maxSize) {
            internalList.add(e);
        } else if (comparator.compare(internalList.get(maxSize - 1), e) == -1) {
            internalList.remove(maxSize - 1);
            internalList.add(e);
        } else {
            return;
        }
        Collections.sort(internalList, Collections.reverseOrder(comparator));
    }

    @Override
    public E get(int i) {
        return internalList.get(i);
    }

    @Override
    public int size() {
        return internalList.size();
    }
}
