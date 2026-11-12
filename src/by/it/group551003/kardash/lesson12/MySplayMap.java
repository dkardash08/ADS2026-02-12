package by.it.group551003.kardash.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size = 0;

    // Правый поворот узла
    private Node rotateRight(Node node) {
        Node left = node.left;
        node.left = left.right;
        left.right = node;
        return left;
    }

    // Левый поворот узла
    private Node rotateLeft(Node node) {
        Node right = node.right;
        node.right = right.left;
        right.left = node;
        return right;
    }

    // Подъем узла с заданным ключом (или ближайшего к нему) в корень дерева
    private Node splay(Node node, int key) {
        if (node == null) return null;
        Node header = new Node(null, null);
        header.left = header.right = null;
        Node left = header, right = header;

        while (true) {
            if (key < node.key) {
                if (node.left == null) break;
                if (key < node.left.key) {
                    node = rotateRight(node);
                }
                if (node.left == null) break;
                right.left = node;
                right = node;
                node = node.left;
            } else if (key > node.key) {
                if (node.right == null) break;
                if (key > node.right.key) {
                    node = rotateLeft(node);
                }
                if (node.right == null) break;
                left.right = node;
                left = node;
                node = node.right;
            } else {
                break;
            }
        }
        left.right = node.left;
        right.left = node.right;
        node.left = header.right;
        node.right = header.left;
        return node;
    }

    // Поиск минимального ключа в поддереве
    private Integer minKey(Node node) {
        if (node == null) return null;
        while (node.left != null) node = node.left;
        return node.key;
    }

    // Поиск максимального ключа в поддереве
    private Integer maxKey(Node node) {
        if (node == null) return null;
        while (node.right != null) node = node.right;
        return node.key;
    }
    // Добавляет пару ключ-значение, возвращает старое значение или null
    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        root = splay(root, key);
        if (root.key.equals(key)) {
            String old = root.value;
            root.value = value;
            return old;
        }
        Node newNode = new Node(key, value);
        if (key < root.key) {
            newNode.left = root.left;
            newNode.right = root;
            root.left = null;
        } else {
            newNode.right = root.right;
            newNode.left = root;
            root.right = null;
        }
        root = newNode;
        size++;
        return null;
    }

    // Возвращает значение по ключу или null (поднимает узел в корень)
    @Override
    public String get(Object key) {
        if (key == null) throw new NullPointerException();
        if (root == null) return null;
        root = splay(root, (Integer) key);
        return root.key.equals(key) ? root.value : null;
    }

    // Удаляет элемент по ключу, возвращает удаленное значение или null
    @Override
    public String remove(Object key) {
        if (key == null) throw new NullPointerException();
        if (root == null) return null;
        root = splay(root, (Integer) key);
        if (!root.key.equals(key)) return null;

        String oldValue = root.value;
        if (root.left == null) {
            root = root.right;
        } else {
            Node rightTree = root.right;
            root = splay(root.left, (Integer) key);
            root.right = rightTree;
        }
        size--;
        return oldValue;
    }

    // Проверяет наличие ключа в дереве
    @Override
    public boolean containsKey(Object key) {
        if (key == null) throw new NullPointerException();
        return get(key) != null;
    }

    // Проверяет наличие значения в дереве (полный обход)
    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(value, node.value)) return true;
        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
    }

    // Возвращает количество элементов
    @Override
    public int size() {
        return size;
    }

    // Очищает дерево
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // Проверяет, пусто ли дерево
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Возвращает строковое представление {key=value, ...} по возрастанию ключей
    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        buildString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    // In-order обход для формирования строки
    private void buildString(Node node, StringBuilder sb) {
        if (node == null) return;
        buildString(node.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        buildString(node.right, sb);
    }

    // Возвращает наименьший ключ
    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        return minKey(root);
    }

    // Возвращает наибольший ключ
    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        return maxKey(root);
    }

    // Наибольший ключ строго меньше заданного
    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) throw new NullPointerException();
        if (root == null) return null;
        root = splay(root, key);
        if (root.key < key) return root.key;
        return maxKey(root.left);
    }

    // Наибольший ключ меньше или равный заданному
    @Override
    public Integer floorKey(Integer key) {
        if (key == null) throw new NullPointerException();
        if (root == null) return null;
        root = splay(root, key);
        if (root.key <= key) return root.key;
        return maxKey(root.left);
    }

    // Наименьший ключ больший или равный заданному
    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) throw new NullPointerException();
        if (root == null) return null;
        root = splay(root, key);
        if (root.key >= key) return root.key;
        return minKey(root.right);
    }

    // Наименьший ключ строго больше заданного
    @Override
    public Integer higherKey(Integer key) {
        if (key == null) throw new NullPointerException();
        if (root == null) return null;
        root = splay(root, key);
        if (root.key > key) return root.key;
        return minKey(root.right);
    }

    // Подкарта с ключами < toKey
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        if (toKey == null) throw new NullPointerException();
        MySplayMap result = new MySplayMap();
        collectRange(root, null, toKey, false, inclusive, result);
        return result;
    }

    // Подкарта с ключами >= fromKey
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        if (fromKey == null) throw new NullPointerException();
        MySplayMap result = new MySplayMap();
        collectRange(root, fromKey, null, inclusive, false, result);
        return result;
    }

    // Сбор элементов в заданном диапазоне (оптимизированный обход)
    private void collectRange(Node node, Integer fromKey, Integer toKey, boolean fromIncl, boolean toIncl, MySplayMap result) {
        if (node == null) return;

        if (fromKey == null || node.key > fromKey || (fromIncl && node.key.equals(fromKey))) {
            collectRange(node.left, fromKey, toKey, fromIncl, toIncl, result);
        }

        boolean include = true;
        if (fromKey != null) {
            if (node.key < fromKey || (!fromIncl && node.key.equals(fromKey))) {
                include = false;
            }
        }
        if (toKey != null) {
            if (node.key > toKey || (!toIncl && node.key.equals(toKey))) {
                include = false;
            }
        }

        if (include) {
            result.put(node.key, node.value);
        }

        if (toKey == null || node.key < toKey || (toIncl && node.key.equals(toKey))) {
            collectRange(node.right, fromKey, toKey, fromIncl, toIncl, result);
        }
    }

    // ==================== ЗАГЛУШКИ (для компиляции NavigableMap/Map) ====================

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        if (fromKey == null || toKey == null) throw new NullPointerException();
        MySplayMap result = new MySplayMap();
        collectRange(root, fromKey, toKey, fromInclusive, toInclusive, result);
        return result;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
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