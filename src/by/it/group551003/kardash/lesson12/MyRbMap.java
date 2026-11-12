package by.it.group551003.kardash.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    // Специальный черный узел-сентинел для замены всех null
    private static final Node NIL;
    static {
        NIL = new Node(null, null, false);
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.parent = NIL;
    }

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean isRed;

        Node(Integer key, String value, boolean isRed) {
            this.key = key;
            this.value = value;
            this.isRed = isRed;
            this.left = NIL;
            this.right = NIL;
            this.parent = NIL;
        }
    }

    private Node root = NIL;
    private int size = 0;

    // Возвращает компаратор (null — используется естественный порядок Integer)
    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    // Левый поворот узла x
    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != NIL) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == NIL) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    // Правый поворот узла y
    private void rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != NIL) x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == NIL) root = x;
        else if (y == y.parent.right) y.parent.right = x;
        else y.parent.left = x;
        x.right = y;
        y.parent = x;
    }

    // Балансировка после вставки
    private void fixUpInsert(Node z) {
        while (z.parent.isRed) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right;
                if (y.isRed) {
                    z.parent.isRed = false;
                    y.isRed = false;
                    z.parent.parent.isRed = true;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) { z = z.parent; rotateLeft(z); }
                    z.parent.isRed = false;
                    z.parent.parent.isRed = true;
                    rotateRight(z.parent.parent);
                }
            } else {
                Node y = z.parent.parent.left;
                if (y.isRed) {
                    z.parent.isRed = false;
                    y.isRed = false;
                    z.parent.parent.isRed = true;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) { z = z.parent; rotateRight(z); }
                    z.parent.isRed = false;
                    z.parent.parent.isRed = true;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.isRed = false;
    }

    // Замена поддерева u на v
    private void transplant(Node u, Node v) {
        if (u.parent == NIL) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        v.parent = u.parent;
    }

    // Балансировка после удаления
    private void fixUpDelete(Node x) {
        while (x != root && !x.isRed) {
            if (x == x.parent.left) {
                Node w = x.parent.right;
                if (w.isRed) {
                    w.isRed = false; x.parent.isRed = true; rotateLeft(x.parent); w = x.parent.right;
                }
                if (!w.left.isRed && !w.right.isRed) { w.isRed = true; x = x.parent; }
                else {
                    if (!w.right.isRed) { w.left.isRed = false; w.isRed = true; rotateRight(w); w = x.parent.right; }
                    w.isRed = x.parent.isRed; x.parent.isRed = false; w.right.isRed = false; rotateLeft(x.parent); x = root;
                }
            } else {
                Node w = x.parent.left;
                if (w.isRed) {
                    w.isRed = false; x.parent.isRed = true; rotateRight(x.parent); w = x.parent.left;
                }
                if (!w.right.isRed && !w.left.isRed) { w.isRed = true; x = x.parent; }
                else {
                    if (!w.left.isRed) { w.right.isRed = false; w.isRed = true; rotateLeft(w); w = x.parent.left; }
                    w.isRed = x.parent.isRed; x.parent.isRed = false; w.left.isRed = false; rotateRight(x.parent); x = root;
                }
            }
        }
        x.isRed = false;
    }

    // Поиск минимального узла
    private Node minimum(Node node) {
        while (node.left != NIL) node = node.left;
        return node;
    }

    // Поиск максимального узла
    private Node maximum(Node node) {
        while (node.right != NIL) node = node.right;
        return node;
    }

    // Добавление пары ключ-значение
    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        String oldValue = get(key);
        Node z = new Node(key, value, true);
        Node y = NIL;
        Node x = root;
        while (x != NIL) {
            y = x;
            if (z.key < x.key) x = x.left;
            else if (z.key > x.key) x = x.right;
            else { x.value = value; return oldValue; }
        }
        z.parent = y;
        if (y == NIL) root = z;
        else if (z.key < y.key) y.left = z;
        else y.right = z;
        fixUpInsert(z);
        size++;
        return oldValue;
    }

    // Получение значения по ключу
    @Override
    public String get(Object key) {
        if (key == null) throw new NullPointerException();
        Node current = root;
        int k = (Integer) key;
        while (current != NIL) {
            if (k < current.key) current = current.left;
            else if (k > current.key) current = current.right;
            else return current.value;
        }
        return null;
    }

    // Удаление по ключу
    @Override
    public String remove(Object key) {
        if (key == null) throw new NullPointerException();
        Node z = root;
        int k = (Integer) key;
        while (z != NIL && k != z.key) {
            if (k < z.key) z = z.left; else z = z.right;
        }
        if (z == NIL) return null;
        String oldValue = z.value;
        Node y = z;
        boolean yOriginalColor = y.isRed;
        Node x;
        if (z.left == NIL) { x = z.right; transplant(z, z.right); }
        else if (z.right == NIL) { x = z.left; transplant(z, z.left); }
        else {
            y = minimum(z.right);
            yOriginalColor = y.isRed;
            x = y.right;
            if (y.parent == z) x.parent = y;
            else { transplant(y, y.right); y.right = z.right; y.right.parent = y; }
            transplant(z, y);
            y.left = z.left; y.left.parent = y;
            y.isRed = z.isRed;
        }
        if (!yOriginalColor) fixUpDelete(x);
        size--;
        return oldValue;
    }

    // Проверка наличия ключа
    @Override
    public boolean containsKey(Object key) {
        if (key == null) throw new NullPointerException();
        return get(key) != null;
    }

    // Проверка наличия значения
    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(Node node, Object value) {
        if (node == NIL) return false;
        if (value == null ? node.value == null : value.equals(node.value)) return true;
        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
    }

    // Количество элементов
    @Override
    public int size() { return size; }

    // Очистка дерева
    @Override
    public void clear() { root = NIL; size = 0; }

    // Проверка на пустоту
    @Override
    public boolean isEmpty() { return size == 0; }

    // Строковое представление {key=value, ...}
    @Override
    public String toString() {
        if (root == NIL) return "{}";
        StringBuilder sb = new StringBuilder("{");
        buildString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void buildString(Node node, StringBuilder sb) {
        if (node == NIL) return;
        buildString(node.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        buildString(node.right, sb);
    }

    // Первый (наименьший) ключ
    @Override
    public Integer firstKey() {
        if (root == NIL) throw new NoSuchElementException();
        return minimum(root).key;
    }

    // Последний (наибольший) ключ
    @Override
    public Integer lastKey() {
        if (root == NIL) throw new NoSuchElementException();
        return maximum(root).key;
    }

    // Подкарта с ключами < toKey
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException();
        MyRbMap result = new MyRbMap();
        collectLessThan(root, toKey, result);
        return result;
    }

    private void collectLessThan(Node node, Integer toKey, MyRbMap result) {
        if (node == NIL) return;
        if (node.key < toKey) {
            collectLessThan(node.left, toKey, result);
            result.put(node.key, node.value);
            collectLessThan(node.right, toKey, result);
        } else {
            collectLessThan(node.left, toKey, result);
        }
    }

    // Подкарта с ключами >= fromKey
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MyRbMap result = new MyRbMap();
        collectGreaterOrEqual(root, fromKey, result);
        return result;
    }

    private void collectGreaterOrEqual(Node node, Integer fromKey, MyRbMap result) {
        if (node == NIL) return;
        if (node.key >= fromKey) {
            collectGreaterOrEqual(node.left, fromKey, result);
            result.put(node.key, node.value);
            collectGreaterOrEqual(node.right, fromKey, result);
        } else {
            collectGreaterOrEqual(node.right, fromKey, result);
        }
    }

    // Подкарта [fromKey, toKey)
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        if (fromKey == null || toKey == null) throw new NullPointerException();
        MyRbMap result = new MyRbMap();
        collectRange(root, fromKey, toKey, result);
        return result;
    }

    private void collectRange(Node node, Integer fromKey, Integer toKey, MyRbMap result) {
        if (node == NIL) return;
        if (node.key >= fromKey && node.key < toKey) {
            collectRange(node.left, fromKey, toKey, result);
            result.put(node.key, node.value);
            collectRange(node.right, fromKey, toKey, result);
        } else if (node.key < fromKey) {
            collectRange(node.right, fromKey, toKey, result);
        } else {
            collectRange(node.left, fromKey, toKey, result);
        }
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }
    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }
    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }
}