/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

public class QuickSort<E> {


    //object sort by toString
    public void sort(E[] array) {
        QuickSort(array, 0, array.length - 1);
    }

    private void QuickSort(E[] array, int first, int last) {
        if (first < last) {
            int mid = partition(array, first, last);
            QuickSort(array, first, mid);
            QuickSort(array, mid + 1, last);
        }
    }

    //sort by string hashCode
    private int partition(E array[], int first, int last) {
        String x = array[first].toString();
        int i = first - 1;
        int j = last + 1;
        while (true) {
            do {
                j--;
            } while (array[j].toString().hashCode() > x.toString().hashCode());
            do {
                i++;
            } while (array[i].toString().hashCode() < x.toString().hashCode());

            if (i < j) {
                E tmp = array[i];
                array[i] = array[j];
                array[j] = tmp;
            } else {
                return j;
            }
        }
    }

}
