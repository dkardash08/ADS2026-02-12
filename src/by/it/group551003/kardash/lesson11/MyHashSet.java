package by.it.group551003.kardash.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E item;
        Node<E> next;

        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size = 0;

    // Конструктор: создаёт хэш-таблицу из 16 корзин
    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[16];
    }

    // Вычисляет индекс корзины по хэш-коду объекта
    private int indexFor(Object o, int length) {
        int h = (o == null) ? 0 : o.hashCode();
        return (h & 0x7fffffff) % length;
    }

    // Увеличивает таблицу в 2 раза и перераспределяет элементы по новым корзинам
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

    // Возвращает текущее количество элементов во множестве
    @Override
    public int size() {
        return size;
    }

    // Очищает множество: обнуляет все корзины и сбрасывает счётчик
    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    // Проверяет, пусто ли множество
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Добавляет элемент, если его ещё нет; при заполнении 75% вызывает resize()
    @Override
    public boolean add(E element) {
        if (contains(element)) return false;
        if (size >= table.length * 3 / 4) resize();
        int index = indexFor(element, table.length);
        table[index] = new Node<>(element, table[index]);
        size++;
        return true;
    }

    // Удаляет элемент из цепочки коллизий, если он найден
    @Override
    public boolean remove(Object o) {
        int index = indexFor(o, table.length);
        Node<E> prev = null;
        Node<E> current = table[index];
        while (current != null) {
            if (o == null ? current.item == null : o.equals(current.item)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    // Проверяет, содержится ли элемент в множестве (перебором цепочки)
    @Override
    public boolean contains(Object o) {
        Node<E> current = table[indexFor(o, table.length)];
        while (current != null) {
            if (o == null ? current.item == null : o.equals(current.item)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Формирует строковое представление множества, обходя все корзины
    @Override
    public String toString() {
        String result = "[";
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                if (!first) {
                    result += ", ";
                }
                result += current.item;
                first = false;
                current = current.next;
            }
        }
        return result + "]";
    }

    // ========== заглушки (требуются только для компиляции Set) ==========

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
}