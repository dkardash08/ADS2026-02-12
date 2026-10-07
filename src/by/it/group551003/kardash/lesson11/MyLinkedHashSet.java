package by.it.group551003.kardash.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E item;
        Node<E> next;    // следующий в цепочке коллизий
        Node<E> before;  // предыдущий по порядку добавления
        Node<E> after;   // следующий по порядку добавления

        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private Node<E> head = null;  // самый ранний добавленный элемент
    private Node<E> tail = null;  // самый поздний добавленный элемент
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[16];
    }

    private int indexFor(Object o, int length) {
        int h = (o == null) ? 0 : o.hashCode();
        return (h & 0x7fffffff) % length;
    }

    private boolean same(Object o, E item) {
        return o == null ? item == null : o.equals(item);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] newTable = (Node<E>[]) new Node[table.length * 2];
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                Node<E> next = current.next;
                int index = indexFor(current.item, newTable.length);
                current.next = newTable[index];
                newTable[index] = current;
                current = next;
            }
        }
        table = newTable;
    }

    // ========== обязательные методы ==========

    @Override
    public String toString() {
        String result = "[";
        Node<E> current = head;
        while (current != null) {
            result += current.item;
            if (current.after != null) {
                result += ", ";
            }
            current = current.after;
        }
        return result + "]";
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        if (contains(element)) return false;
        if (size >= table.length * 3 / 4) resize();
        int index = indexFor(element, table.length);
        Node<E> node = new Node<>(element, table[index]);
        table[index] = node;

        node.before = tail;
        if (tail == null) {
            head = node;
        } else {
            tail.after = node;
        }
        tail = node;

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexFor(o, table.length);
        Node<E> prev = null;
        Node<E> current = table[index];
        while (current != null) {
            if (same(o, current.item)) {
                // убираем из цепочки коллизий
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                // убираем из списка порядка добавления
                if (current.before == null) {
                    head = current.after;
                } else {
                    current.before.after = current.after;
                }
                if (current.after == null) {
                    tail = current.before;
                } else {
                    current.after.before = current.before;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        Node<E> current = table[indexFor(o, table.length)];
        while (current != null) {
            if (same(o, current.item)) {
                return true;
            }
            current = current.next;
        }
        return false;
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
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.after;
            if (c.contains(current.item)) {
                remove(current.item);
                changed = true;
            }
            current = next;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.after;
            if (!c.contains(current.item)) {
                remove(current.item);
                changed = true;
            }
            current = next;
        }
        return changed;
    }

    // ========== заглушки (требуются только для компиляции Set) ==========

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}