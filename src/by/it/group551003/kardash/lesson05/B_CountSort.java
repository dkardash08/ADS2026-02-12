package by.it.group551003.kardash.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Первая строка содержит число 1<=n<=10000, вторая - n натуральных чисел, не превышающих 10.
Выведите упорядоченную по неубыванию последовательность этих чисел.

При сортировке реализуйте метод со сложностью O(n)

Пример: https://karussell.wordpress.com/2010/03/01/fast-integer-sorting-algorithm-on/
Вольный перевод: http://programador.ru/sorting-positive-int-linear-time/
*/

public class B_CountSort {


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        if (!scanner.hasNextInt()) {
            return new int[0];
        }

        //размер массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        // Максимальное значение согласно условию
        int maxVal = 10;
        // Массив для подсчета частоты каждого числа (индексы 0..10)
        int[] count = new int[maxVal + 1];

        //читаем точки и сразу считаем их количество
        for (int i = 0; i < n; i++) {
            int val = scanner.nextInt();
            points[i] = val;
            if (val >= 0 && val <= maxVal) {
                count[val]++;
            }
        }

        // Формируем отсортированный массив на основе подсчетов
        int index = 0;
        for (int i = 0; i <= maxVal; i++) {
            // Добавляем число 'i' в результат 'count[i]' раз
            while (count[i] > 0) {
                points[index++] = i;
                count[i]--;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }

}
