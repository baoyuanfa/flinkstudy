package com.baoyf.study.flink.sort;

import java.util.Arrays;

public class BubbleSort {

    public static void main(String[] args) {

        int[] intArr = {3,5,2,9,7};

        arrayBubbleSort(intArr);

    }

    public static void arrayBubbleSort(int[] intArr) {
        int temp = 0;
        for (int i = 0; i < intArr.length - 1; i++) {

            for (int j = 0; j < intArr.length - 1 - i; j++) {

                if (intArr[j] > intArr[j+1]) {
                    temp = intArr[j];
                    intArr[j] = intArr[j+1];
                    intArr[j+1] = temp;
                }

            }

        }
        System.out.println(Arrays.toString(intArr));
    }


}
