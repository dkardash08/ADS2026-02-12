package by.it.group551003.kardash.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/*
Видеорегистраторы и площадь.
На площади установлена одна или несколько камер.
Известны данные о том, когда каждая из них включалась и выключалась (отрезки работы)
Известен список событий на площади (время начала каждого события).
Вам необходимо определить для каждого события сколько камер его записали.

В первой строке задано два целых числа:
    число включений камер (отрезки) 1<=n<=50000
    число событий (точки) 1<=m<=50000.

Следующие n строк содержат по два целых числа ai и bi (ai<=bi) -
координаты концов отрезков (время работы одной какой-то камеры).
Последняя строка содержит m целых чисел - координаты точек.
Все координаты не превышают 10E8 по модулю (!).

Точка считается принадлежащей отрезку, если она находится внутри него или на границе.

Для каждой точки в порядке их появления во вводе выведите,
скольким отрезкам она принадлежит.
    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        if (!scanner.hasNextInt()) return new int[0];

        //число отрезков
        int n = scanner.nextInt();
        //число точек
        int m = scanner.nextInt();

        int[] starts = new int[n];
        int[] stops = new int[n];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            // Ensure start <= end just in case, though problem says ai <= bi
            if (a <= b) {
                starts[i] = a;
                stops[i] = b;
            } else {
                starts[i] = b;
                stops[i] = a;
            }
        }

        //читаем точки
        int[] points = new int[m];
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем массивы начал и концов отрезков
        Arrays.sort(starts);
        Arrays.sort(stops);

        int[] result = new int[m];

        // Для каждой точки считаем количество покрывающих отрезков
        // Количество отрезков, покрывающих точку p, равно:
        // (количество начал <= p) - (количество концов < p)

        for (int i = 0; i < m; i++) {
            int p = points[i];

            // upperBound возвращает индекс первого элемента > p.
            // Так как массив отсортирован, это количество элементов <= p.
            int countStarts = upperBound(starts, p);

            // lowerBound возвращает индекс первого элемента >= p.
            // Так как нам нужно количество элементов < p, это именно то, что нужно.
            int countStops = lowerBound(stops, p);

            result[i] = countStarts - countStops;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Возвращает индекс первого элемента в arr, который > key.
    // Если все элементы <= key, возвращает arr.length.
    // Эффективно считает количество элементов <= key.
    private int upperBound(int[] arr, int key) {
        int low = 0;
        int high = arr.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (arr[mid] <= key) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    // Возвращает индекс первого элемента в arr, который >= key.
    // Эффективно считает количество элементов < key.
    private int lowerBound(int[] arr, int key) {
        int low = 0;
        int high = arr.length;
        while (low < high) {
            int mid = (low + high) / 2;
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
            // Сортируем по началу отрезка, при равенстве - по концу
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }

}