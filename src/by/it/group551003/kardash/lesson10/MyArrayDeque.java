package by.it.group551003.kardash.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements;
    private int head = 0;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[16];
    }

    // Увеличивает емкость массива в 2 раза и переносит элементы с учетом циклического сдвига
    @SuppressWarnings("unchecked")
    private void grow() {
        E[] newArray = (E[]) new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            newArray[i] = elements[(head + i) % elements.length];
        }
        elements = newArray;
        head = 0;
    }

    // ========== обязательные методы ==========

    // Возвращает строковое представление элементов в формате [elem1, elem2]
    @Override
    public String toString() {
        String result = "[";
        for (int i = 0; i < size; i++) {
            result += elements[(head + i) % elements.length];
            if (i < size - 1) {
                result += ", ";
            }
        }
        return result + "]";
    }

    // Возвращает текущее количество элементов в очереди
    @Override
    public int size() {
        return size;
    }

    // Добавляет элемент в конец очереди (делегирование методу addLast)
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    // Добавляет элемент в начало очереди, сдвигая индекс head назад по кругу
    @Override
    public void addFirst(E element) {
        if (element == null) throw new NullPointerException();
        if (size == elements.length) grow();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    // Добавляет элемент в конец очереди, вычисляя индекс как (head + size) % length
    @Override
    public void addLast(E element) {
        if (element == null) throw new NullPointerException();
        if (size == elements.length) grow();
        elements[(head + size) % elements.length] = element;
        size++;
    }

    // Возвращает первый элемент без удаления (делегирование методу getFirst)
    @Override
    public E element() {
        return getFirst();
    }

    // Возвращает первый элемент, бросает исключение, если очередь пуста
    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return elements[head];
    }

    // Возвращает последний элемент, бросает исключение, если очередь пуста
    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return elements[(head + size - 1) % elements.length];
    }

    // Извлекает и удаляет первый элемент (делегирование методу pollFirst)
    @Override
    public E poll() {
        return pollFirst();
    }

    // Извлекает и удаляет первый элемент, возвращает null, если очередь пуста
    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E result = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return result;
    }

    // Извлекает и удаляет последний элемент, возвращает null, если очередь пуста
    @Override
    public E pollLast() {
        if (size == 0) return null;
        int last = (head + size - 1) % elements.length;
        E result = elements[last];
        elements[last] = null;
        size--;
        return result;
    }

    @Override public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e) { throw new UnsupportedOperationException(); }
    @Override public E removeFirst() { throw new UnsupportedOperationException(); }
    @Override public E removeLast() { throw new UnsupportedOperationException(); }
    @Override public E peekFirst() { throw new UnsupportedOperationException(); }
    @Override public E peekLast() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { throw new UnsupportedOperationException(); }
    @Override public E remove() { throw new UnsupportedOperationException(); }
    @Override public E peek() { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { throw new UnsupportedOperationException(); }
    @Override public E pop() { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public boolean isEmpty() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
}