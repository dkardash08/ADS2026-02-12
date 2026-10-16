package by.it.group551003.kardash.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private E[] elements;
    private int size = 0;

    // Конструктор: инициализирует внутренний массив размером 16
    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        elements = (E[]) new Object[16];
    }

    // Сравнивает два объекта, приводя их к интерфейсу Comparable
    @SuppressWarnings("unchecked")
    private int compare(Object a, Object b) {
        return ((Comparable<Object>) a).compareTo(b);
    }

    // Увеличивает размер массива в 2 раза и копирует в него существующие элементы
    @SuppressWarnings("unchecked")
    private void grow() {
        E[] newArray = (E[]) new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            newArray[i] = elements[i];
        }
        elements = newArray;
    }

    // Бинарный поиск: возвращает индекс элемента, либо -(место вставки) - 1, если не найден
    private int search(Object o) {
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = compare(elements[mid], o);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    // ========== обязательные методы ==========

    // Возвращает строковое представление элементов в отсортированном порядке
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

    // Возвращает текущее количество элементов во множестве
    @Override
    public int size() {
        return size;
    }

    // Очищает множество, обнуляя элементы массива и сбрасывая размер в 0
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    // Проверяет, является ли множество пустым
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Добавляет элемент с сохранением сортировки (со сдвигом элементов вправо)
    @Override
    public boolean add(E element) {
        if (element == null) throw new NullPointerException();
        int index = search(element);
        if (index >= 0) return false;
        index = -(index + 1);
        if (size == elements.length) grow();
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
        return true;
    }

    // Удаляет элемент, сдвигая правую часть массива влево для закрытия "дыры"
    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        int index = search(o);
        if (index < 0) return false;
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        size--;
        elements[size] = null;
        return true;
    }

    // Проверяет наличие элемента в множестве с помощью бинарного поиска
    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        return search(o) >= 0;
    }

    // Проверяет, содержатся ли все элементы коллекции c во множестве
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    // Добавляет все элементы из коллекции c во множество
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

    // Удаляет из множества все элементы, присутствующие в коллекции c (за один проход)
    @Override
    public boolean removeAll(Collection<?> c) {
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                elements[newSize++] = elements[i];
            }
        }
        boolean changed = newSize != size;
        for (int i = newSize; i < size; i++) {
            elements[i] = null;
        }
        size = newSize;
        return changed;
    }

    // Оставляет во множестве только те элементы, которые есть в коллекции c
    @Override
    public boolean retainAll(Collection<?> c) {
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                elements[newSize++] = elements[i];
            }
        }
        boolean changed = newSize != size;
        for (int i = newSize; i < size; i++) {
            elements[i] = null;
        }
        size = newSize;
        return changed;
    }

    // ========== заглушки (требуются только для компиляции Set) ==========

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}