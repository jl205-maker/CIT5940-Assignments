package homework1.src;

import java.util.Random;

public class MergeSort {

    public static void main(String[] args) {
        int[] input = new int[1000];
        Random rand = new Random();

        for (int i = 0; i < 1000; i++) {
            input[i] = rand.nextInt(5000);
        }

        // compute average runtime
        long start;
        long end;
        long total = 0;
        // compute average runtime
        for (int i = 0; i < 10; i++) {
            start = System.nanoTime(); // start testing runtime
            mergeSort(input);
            end = System.nanoTime(); // end testing runtime
            // System.out.println("Run time was " + (end - start) / 1000000.0 + " milliseconds." ); // print runtime
            total += (end - start);
        }
        System.out.println("Average run time was " + (total / 10.0) / 1000000.0 + " milliseconds." ); // print average runtime
    }

    public static int[] mergeSort(int[] input) {

        if (input.length <= 1) {
            return input;
        }

        int mid = input.length / 2;
        int[] left = new int[mid];
        int[] right = new int[input.length - mid];

        System.arraycopy(input, 0, left, 0, mid);
        System.arraycopy(input, mid, right, 0, input.length - mid);

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left, right);
    }
    // merge two sorted arrays
    private static int[] merge(int[] arr1, int[] arr2) {
        int[] res =  new int[arr1.length + arr2.length];
        int i=0;
        int j=0;
        int k=0;
        while(i<arr1.length && j<arr2.length) {
            if (arr1[i] <= arr2[j]) {
                res[k] = arr1[i];
                i++;
            } else{
                res[k] = arr2[j];
                j++;
            }
            k++;
        }
        while(i<arr1.length) {
            res[k] = arr1[i];
            i++;
            k++;
        }
        while(j<arr2.length) {
            res[k] = arr2[j];
            j++;
            k++;
        }
        return res;
    }
}
