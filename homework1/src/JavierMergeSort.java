package homework1.src;

import java.util.LinkedList;
import java.util.Random;


public class JavierMergeSort {

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
        // Create an empty list of arrays
        LinkedList<int[]> queue = new LinkedList<>();


        // Turn every number into a {num} array and add to queue
        // O(N)
        for (int num : input) {
            int[] singleElementArray = {num};
            queue.add(singleElementArray);
        } // now queue contains N single-element arrays


        // Pick the first two arrays in the list and merge them
        // We check > 1, so we know it is safe to call remove() twice inside.
        // N-1 merges in total
        while (queue.size() > 1) {
            // O(1)
            int[] first = queue.remove(0);  // gets first value and removes it
            // O(1)
            int[] second = queue.remove(0); // gets next value

            // O(a + b)
            int[] combined = merge(first, second);
            // O(1)
            queue.add(combined); // add to the back of the line
        }


        // Return the result after it’s the only thing left
        if (queue.isEmpty()) {
            return new int[0]; // Return empty array if input was empty
        } else {
            return queue.remove();
        }
    }


    // Merges two sorted arrays into one new sorted array
    // O(a + b)
    private static int[] merge(int[] a, int[] b) {
        int[] result = new int[a.length + b.length];
        int i = 0; // index for a
        int j = 0; // index for b
        int k = 0; // index for result


        // Compare elements and add the smaller one
        while (i < a.length && j < b.length) {
            if (a[i] <= b[j]) {
                result[k] = a[i]; // add val from a if smaller
                i++;
            } else {
                result[k] = b[j]; // add val from b if smaller
                j++;
            }
            k++;
        }


        // Add remaining elements from a (if any)
        while (i < a.length) {
            result[k] = a[i];
            i++;
            k++;
        }
        
        // Add remaining elements from b (if any)
        while (j < b.length) {
            result[k] = b[j];
            j++;
            k++;
        }


        return result;
    }
}

