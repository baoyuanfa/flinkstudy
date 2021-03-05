package com.baoyf.study.flink.sort;

import java.util.Arrays;

public class InsertSort {

    public static void main(String[] args) {
        int[] intArr = {3,5,2,2,9,7};

        ArrayInsertSort(intArr);
    }

    public static void ArrayInsertSort(int[] intArr) {
        int temp;
        int j;

        for (int i = 1; i < intArr.length; i++) {

            j = i;
            while (j > 0) {
                if (intArr[j] < intArr[j-1]) {
                    temp = intArr[j-1];
                    intArr[j-1] = intArr[j];
                    intArr[j] = temp;
                    j--;
                } else {
                    break;
                }
            }
        }
        System.out.println(Arrays.toString(intArr));
    }

}
