package by.it.group551003.kardash.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    // Внутренний класс узла двусвязного списка
    private static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;

        // Конструктор узла: инициализирует значение элемента
        Node(E item) {
            this.item = item;
        }
    }

    private Node<E> first = null;
    private Node<E> last = null;
    private int size = 0;

    // Удаляет указанный узел из списка, корректно обновляя связи соседей и уменьшая size
    private E unlink(Node<E> node) {
        E item = node.item;
        if (node.prev == null) {
            first = node.next;
        } else {
            node.prev.next = node.next;
        }
        if (node.next == null) {
            last = node.prev;
        } else {
            node.next.prev = node.prev;
        }
        node.item = null;
        node.prev = null;
        node.next = null;
        size--;
        return item;
    }

    // ========== обязательные методы ==========

    // Возвращает строковое представление элементов списка в формате [elem1, elem2]
    @Override
    public String toString() {
        String result = "[";
        Node<E> current = first;
        while (current != null) {
            result += current.item;
            if (current.next != null) {
                result += ", ";
            }
            current = current.next;
        }
        return result + "]";
    }

    // Добавляет элемент в конец списка (делегирование методу addLast)
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    // Удаляет элемент по заданному индексу, оптимизируя поиск (с начала или с конца списка)
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> current;
        if (index < size / 2) {
            current = first;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = last;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return unlink(current);
    }

    // Удаляет первое вхождение указанного элемента, возвращая true при успешном удалении
    @Override
    public boolean remove(Object element) {
        Node<E> current = first;
        while (current != null) {
            if (element == null ? current.item == null : element.equals(current.item)) {
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Возвращает текущее количество элементов в списке
    @Override
    public int size() {
        return size;
    }

    // Добавляет элемент в начало списка, обновляя ссылки first и prev
    @Override
    public void addFirst(E element) {
        Node<E> node = new Node<>(element);
        node.next = first;
        if (first == null) {
            last = node;
        } else {
            first.prev = node;
        }
        first = node;
        size++;
    }

    // Добавляет элемент в конец списка, обновляя ссылки last и next
    @Override
    public void addLast(E element) {
        Node<E> node = new Node<>(element);
        node.prev = last;
        if (last == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
        size++;
    }

    // Возвращает первый элемент без удаления (делегирование методу getFirst)
    @Override
    public E element() {
        return getFirst();
    }

    // Возвращает первый элемент, бросает исключение, если список пуст
    @Override
    public E getFirst() {
        if (first == null) throw new NoSuchElementException();
        return first.item;
    }

    // Возвращает последний элемент, бросает исключение, если список пуст
    @Override
    public E getLast() {
        if (last == null) throw new NoSuchElementException();
        return last.item;
    }

    // Извлекает и удаляет первый элемент (делегирование методу pollFirst)
    @Override
    public E poll() {
        return pollFirst();
    }

    // Извлекает и удаляет первый элемент, возвращает null, если список пуст
    @Override
    public E pollFirst() {
        if (first == null) return null;
        return unlink(first);
    }

    // Извлекает и удаляет последний элемент, возвращает null, если список пуст
    @Override
    public E pollLast() {
        if (last == null) return null;
        return unlink(last);
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