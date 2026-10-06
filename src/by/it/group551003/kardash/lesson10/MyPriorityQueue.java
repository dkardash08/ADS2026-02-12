package by.it.group551003.kardash.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] elements;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        elements = (E[]) new Object[16];
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        return ((Comparable<E>) a).compareTo(b);
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        E[] newArray = (E[]) new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            newArray[i] = elements[i];
        }
        elements = newArray;
    }

    private void siftUp(int index) {
        E item = elements[index];
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (compare(item, elements[parent]) >= 0) break;
            elements[index] = elements[parent];
            index = parent;
        }
        elements[index] = item;
    }

    private void siftDown(int index) {
        E item = elements[index];
        int half = size / 2;
        while (index < half) {
            int child = 2 * index + 1;
            int right = child + 1;
            if (right < size && compare(elements[right], elements[child]) < 0) {
                child = right;
            }
            if (compare(item, elements[child]) <= 0) break;
            elements[index] = elements[child];
            index = child;
        }
        elements[index] = item;
    }

    // оставляет в куче только нужные элементы и перестраивает кучу
    private boolean filter(Collection<?> c, boolean keepIfContained) {
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i]) == keepIfContained) {
                elements[newSize++] = elements[i];
            }
        }
        boolean changed = newSize != size;
        for (int i = newSize; i < size; i++) {
            elements[i] = null;
        }
        size = newSize;
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
        return changed;
    }

    // ========== обязательные методы ==========

    @Override
    public String toString() {
        String result = "[";
        for (int i = 0; i < size; i++) {
            result += elements[i];
            if (i < size - 1) {
                result += ", ";
            }
        }
        return result + "]";
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        return poll();
    }

    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) throw new NullPointerException();
        if (size == elements.length) grow();
        elements[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = elements[0];
        size--;
        elements[0] = elements[size];
        elements[size] = null;
        if (size > 0) siftDown(0);
        return result;
    }

    @Override
    public E peek() {
        return size == 0 ? null : elements[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return elements[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            add(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return filter(c, false);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return filter(c, true);
    }

    // ========== заглушки (требуются только для компиляции Queue) ==========

    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}