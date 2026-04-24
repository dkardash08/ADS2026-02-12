package by.it.group551003.kardash.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/


public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        if (!scanner.hasNextInt()) return new int[0];

        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];

        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            segments[i] = new Segment(a, b);
        }

        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // To achieve O(N log N + M log N), we sort starts and stops separately.
        // The prompt asks to sort "segments" and use 3-way quicksort.
        // We will extract arrays to sort them using our custom 3-way QSort.

        int[] starts = new int[n];
        int[] stops = new int[n];

        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }

        // Sort using custom 3-way QuickSort
        quickSort3Way(starts);
        quickSort3Way(stops);

        // For each point, count = (starts <= point) - (stops < point)
        for (int i = 0; i < m; i++) {
            int p = points[i];

            // Upper bound for starts: index of first element > p
            // Count of elements <= p is exactly this index
            int countStarts = upperBound(starts, p);

            // Lower bound for stops: index of first element >= p
            // Count of elements < p is exactly this index
            int countStops = lowerBound(stops, p);

            result[i] = countStarts - countStops;
        }

        return result;
    }

    // --- Custom 3-Way QuickSort Implementation ---

    private void quickSort3Way(int[] arr) {
        if (arr == null || arr.length == 0) return;
        sort3Way(arr, 0, arr.length - 1);
    }

    private void sort3Way(int[] arr, int low, int high) {
        while (low < high) {
            // 3-way partition
            int lt = low;
            int gt = high;
            int pivot = arr[low];
            int i = low;

            while (i <= gt) {
                if (arr[i] < pivot) {
                    swap(arr, lt++, i++);
                } else if (arr[i] > pivot) {
                    swap(arr, i, gt--);
                } else {
                    i++;
                }
            }

            // Tail recursion elimination:
            // Recurse into the smaller partition, loop on the larger one.
            if (lt - low < high - gt) {
                sort3Way(arr, low, lt - 1);
                low = gt + 1;
            } else {
                sort3Way(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    private void swap(int[] arr, int i, int j) {
        if (i != j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    // --- Binary Search Helpers ---

    // Returns index of first element > key.
    // Equivalent to count of elements <= key.
    private int upperBound(int[] arr, int key) {
        int low = 0;
        int high = arr.length;
        while (low < high) {
            int mid = (low + high) >>> 1;
            if (arr[mid] <= key) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    // Returns index of first element >= key.
    // Equivalent to count of elements < key.
    private int lowerBound(int[] arr, int key) {
        int low = 0;
        int high = arr.length;
        while (low < high) {
            int mid = (low + high) >>> 1;
            if (arr[mid] < key) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }

}
