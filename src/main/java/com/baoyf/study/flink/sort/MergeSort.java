package com.baoyf.study.flink.sort;

import java.util.Arrays;

public class MergeSort {
    public static void main(String[] args) {
        int[] arr = {3,5,2,2,9,7};
        arrayMergeSort(arr);
    }

    private static void arrayMergeSort(int[] arr) {
        int leftVal = 0;
        int rightVal = arr.length - 1;
        int[] resArr = new int[arr.length];
        System.out.println(Arrays.toString(arr));
        arrayMergeSort(arr, leftVal, rightVal, resArr);
        System.out.println(Arrays.toString(arr));
    }

    private static void arrayMergeSort(int[] arr, int leftVal, int rightVal, int[] resArr) {

        if (leftVal < rightVal) {
            int midVal = (leftVal + rightVal)/2;
            //向左递归
            arrayMergeSort(arr, leftVal, midVal, resArr);
            //向右递归
            arrayMergeSort(arr, midVal + 1, rightVal, resArr);
            //归并排序
            arrayMergeSort(arr, leftVal, midVal, rightVal,resArr);
        }

    }

    private static void arrayMergeSort(int[] arr, int leftVal, int midVal, int rightVal, int[] resArr) {

        int i = leftVal;
        int j = midVal + 1;
        int t = 0;

        while (i <= midVal && j <= rightVal) {
            if (arr[i] < arr[j]) {
                resArr[t++] = arr[i++];
            } else {
                resArr[t++] = arr[j++];
            }
        }

        //左侧剩余
        while (i <= midVal) {
            resArr[t++] = arr[i++];
        }

        //右侧剩余
        while (j <= rightVal) {
            resArr[t++] = arr[j++];
        }

        //重置指针并回写数据
        t = 0;
        while (leftVal <= rightVal) {
            arr[leftVal++] = resArr[t++];
        }

    }


//    public static void arrayMergeSort(int[] arr) {
//        int leftVal = 0;
//        int rightVal = arr.length - 1;
//        int[] resArr = new int[arr.length];
//        System.out.println(Arrays.toString(arr));
//        arrayMergeSort(arr, leftVal, rightVal, resArr);
//        System.out.println(Arrays.toString(arr));
//    }
//
//    public static void arrayMergeSort(int[] initArr, int leftVal, int rightVal, int[] resArr) {
//        if (leftVal < rightVal) {
//            int midVal = (leftVal + rightVal) / 2;
//            //向左递归
//            arrayMergeSort(initArr, leftVal, midVal, resArr);
//            //向右递归
//            arrayMergeSort(initArr, midVal + 1, rightVal, resArr);
//            //排序
//            sort(initArr, leftVal, midVal, rightVal, resArr);
//        }
//    }
//
//
//    public static void sort(int[] initArr, int leftVal, int midVal, int rightVal, int[] resArr) {
//        int i = leftVal;
//        int j = midVal + 1;
//        int t = 0;
//
//        while (i <= midVal && j <= rightVal) {
//            if (initArr[i] < initArr[j]) {
//                resArr[t++] = initArr[i++];
//            } else {
//                resArr[t++] = initArr[j++];
//            }
//        }
//
//        //将左侧全部写入临时array
//        while (i <= midVal) {
//            resArr[t++] = initArr[i++];
//        }
//
//        //将右侧全部写入临时array
//        while (j <= rightVal) {
//            resArr[t++] = initArr[j++];
//        }
//
//        t = 0;
//        while (leftVal <= rightVal) {
//            initArr[leftVal++] = resArr[t++];
//        }
//
//    }

}
