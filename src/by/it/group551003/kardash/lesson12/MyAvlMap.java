package by.it.group551003.kardash.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        int height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size = 0;

    // Возвращает высоту узла (0 для null)
    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    // разница высот левого и правого поддеревьев)
    private int balanceFactor(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    // Обновляет высоту узла на основе высот его детей
    private void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    // Правый поворот для балансировки дерева
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node temp = x.right;
        x.right = y;
        y.left = temp;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    // Левый поворот для балансировки дерева
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node temp = y.left;
        y.left = x;
        x.right = temp;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    // Балансирует узел при необходимости (4 случая: LL, RR, LR, RL)
    private Node balance(Node node) {
        updateHeight(node);
        int bf = balanceFactor(node);

        if (bf > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        if (bf < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    // Добавляет пару ключ-значение в дерево, возвращает старое значение или null
    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        String oldValue = get(key);
        root = putRecursive(root, key, value);
        return oldValue;
    }

    // Рекурсивная вставка узла в дерево с последующей балансировкой
    private Node putRecursive(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = putRecursive(node.left, key, value);
        } else if (cmp > 0) {
            node.right = putRecursive(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }
        return balance(node);
    }

    // Возвращает значение по ключу или null, если ключ не найден
    @Override
    public String get(Object key) {
        if (key == null) throw new NullPointerException();
        Node current = root;
        while (current != null) {
            int cmp = ((Integer) key).compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else return current.value;
        }
        return null;
    }

    // Удаляет элемент по ключу, возвращает удалённое значение или null
    @Override
    public String remove(Object key) {
        if (key == null) throw new NullPointerException();
        String oldValue = get(key);
        if (oldValue != null) {
            root = removeRecursive(root, (Integer) key);
        }
        return oldValue;
    }

    // Рекурсивное удаление узла с балансировкой дерева
    private Node removeRecursive(Node node, Integer key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = removeRecursive(node.left, key);
        } else if (cmp > 0) {
            node.right = removeRecursive(node.right, key);
        } else {
            if (node.left == null || node.right == null) {
                size--;
                return (node.left != null) ? node.left : node.right;
            } else {
                Node min = findMin(node.right);
                node.key = min.key;
                node.value = min.value;
                node.right = removeRecursive(node.right, min.key);
            }
        }
        return balance(node);
    }

    // Находит минимальный узел в поддереве (самый левый)
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Проверяет, содержится ли ключ в дереве
    @Override
    public boolean containsKey(Object key) {
        if (key == null) throw new NullPointerException();
        return get((Integer) key) != null;
    }

    // Возвращает количество элементов в дереве
    @Override
    public int size() {
        return size;
    }

    // Очищает дерево (удаляет все элементы)
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

    // Возвращает строковое представление дерева в формате {key=value, ...}
    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        buildString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    // Рекурсивный обход дерева (in-order) для построения строки
    private void buildString(Node node, StringBuilder sb) {
        if (node == null) return;
        buildString(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);
        buildString(node.right, sb);
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
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
}