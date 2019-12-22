import java.util.Arrays;

/**
 * Class containing all the sorting algorithms from 61B to date.
 * <p>
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 * <p>
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 * <p>
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 * <p>
 * All implementations except Distribution Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /**
     * Insertion sorts the provided data.
     */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
//            for (int i = 0; i < k; i++) {
//                int num = array[i];
//                int j = i - 1;
//                while (j >= 0 && array[j] > num) {
//                    array[j + 1] = array[j];
//                    j--;
//                }
//                array[j + 1] = num;
//            }
            for (int i = 0; i < k; i++) {
                int num = array[i];
                int j = i - 1;
                for (; j >= 0 && array[j] > num; j--) {
                    array[j + 1] = array[j];
                }
                array[j + 1] = num;
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
            for (int i = 0; i < k - 1; i++) {
                int min = i;
                for (int j = i + 1; j < k; j++) {
                    if (array[j] <= array[min]) {
                        min = j;
                    }
                }
                swap(array, i, min);
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /**
     * Your mergesort implementation. An iterative merge
     * method is easier to write than a recursive merge method.
     * Note: I'm only talking about the merge operation here,
     * not the entire algorithm, which is easier to do recursively.
     */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
            int[] result = new int[k];
            mergeSort(array, result, 0, k - 1);
        }

        // may want to add additional methods
        public void mergeSort(int[] array, int[] result, int start, int end) {
            if (start >= end) {
                return;
            }
            int mid = (start + end) / 2;
            int start1 = start, end1 = mid;
            int start2 = mid + 1, end2 = end;

            mergeSort(array, result, start1, end1);
            mergeSort(array, result, start2, end2);

            int index = start;
            while (start1 <= end1 && start2 <= end2) {
                result[index++] = array[start1] < array[start2] ? array[start1++] : array[start2++];
            }
            while (start1 <= end1) {
                result[index++] = array[start1++];
            }
            while (start2 <= end2) {
                result[index++] = array[start2++];
            }
            for (index = start; index <= end; index++) {
                array[index] = result[index];
            }
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Distribution Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class DistributionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented

            int[] arr = Arrays.copyOfRange(array, 0, k);
            int[] res = Arrays.copyOfRange(array, k, array.length);

            int maxValue = getMaxValue(arr);
            countingSort(arr, maxValue);

            System.arraycopy(arr, 0, array, 0, arr.length);
            System.arraycopy(res, 0, array, arr.length, res.length);
        }

        private void countingSort(int[] arr, int maxValue) {
            int bucketLen = maxValue + 1;
            int[] bucket = new int[bucketLen];

            for (int value : arr) {
                bucket[value]++;
            }

            int sortedIndex = 0;
            for (int j = 0; j < bucketLen; j++) {
                while (bucket[j] > 0) {
                    arr[sortedIndex++] = j;
                    bucket[j]--;
                }
            }

        }

        private int getMaxValue(int[] arr) {
            int maxValue = arr[0];
            for (int value : arr) {
                if (maxValue < value) {
                    maxValue = value;
                }
            }
            return maxValue;
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Distribution Sort";
        }
    }

    /**
     * Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
            for (int i = k / 2 - 1; i >= 0; i--) {
                heapify(array, i, k);
            }
            for (int i = k - 1; i >= 0; i--) {
                swap(array, 0, i);
                heapify(array, 0, i);
            }
        }

        public void heapify(int[] arr, int i, int n) {
            int largest = i;
            int l = 2 * i + 1;
            int r = 2 * i + 2;
            if (l < n && arr[l] > arr[largest]) {
                largest = l;
            }
            if (r < n && arr[r] > arr[largest]) {
                largest = r;
            }
            if (largest != i) {
                swap(arr, i, largest);
                heapify(arr, largest, n);
            }
        }

//        @Override
//        public void sort(int[] array, int k) {
//
//            bubbleDown(array, 0, k);
//
//            for (int i = k - 1; i >= 0; i--) {
//                swap(array, 0, i);
//                bubbleUp(array, i);
//            }
//        }

//        public void heapify(int[] arr, int i) {
//            while (i > 0) {
//                int parent = (i - 1) / 2;
//                if (arr[i] < arr[parent]) {
//                    swap(arr, i, parent);
//                    i = parent;
//                } else {
//                    break;
//                }
//            }
//        }

        public void bubbleUp(int[] array, int index) {
            while (index > 1 && Math.min(index, getParentOf(index)) == index) {
                swap(array, index, getParentOf(index));
                index = getParentOf(index);
            }
        }

        public void bubbleDown(int[] array, int index, int N) {
            if (index == N) {
                return;
            }
            int indexMin = Math.min(index, Math.min(getLeftOf(index), getRightOf(index)));
            if (index != indexMin) {
                swap(array, index, indexMin);
                bubbleDown(array, indexMin, N);
            }
        }

        private int getLeftOf(int i) {
            return i * 2;
        }

        private int getRightOf(int i) {
            return i * 2 + 1;
        }

        private int getParentOf(int i) {
            return i / 2;
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /**
     * Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
            quickSort(array, 0, k - 1);
        }

        public void quickSort(int[] arr, int left, int right) {
            if (left > right) {
                return;
            }
            int pivot = arr[left];
            int i = left;
            int j = right;

            while (i != j) {
                while (arr[j] >= pivot && i < j) {
                    j--;
                }
                while (arr[i] <= pivot && i < j) {
                    i++;
                }
                if (i < j) {
                    swap(arr, i, j);
                }
            }
            swap(arr, left, i);
            quickSort(arr, left, i - 1);
            quickSort(arr, i + 1, right);
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
            int exp = 1;
            int max = getMax(a);

            int[] arr = Arrays.copyOfRange(a, 0, k);
            int[] res = Arrays.copyOfRange(a, k, a.length);

            for (; max / exp > 0; exp *= 10) {
                LSDSort(arr, exp);
            }

            System.arraycopy(arr, 0, a, 0, arr.length);
            System.arraycopy(res, 0, a, arr.length, res.length);
        }

        public int getMax(int[] arr) {
            int max = arr[0];
            for (int i : arr) {
                if (i > max) {
                    max = i;
                }
            }
            return max;
        }

        public void LSDSort(int[] arr, int exp) {
            int[] output = new int[arr.length];
            int[] buckets = new int[10];

            for (int i : arr) {
                buckets[(i / exp) % 10]++;
            }

            for (int i = 1; i < 10; i++) {
                buckets[i] += buckets[i - 1];
            }

            for (int i = arr.length - 1; i >= 0; i--) {
                output[buckets[(arr[i] / exp) % 10] - 1] = arr[i];
                buckets[(arr[i] / exp) % 10]--;
            }

            for (int i = 0; i < arr.length; i++) {
                arr[i] = output[i];
            }

        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
            int exp = 1;
            int max = getMax(a);

            int[] arr = Arrays.copyOfRange(a, 0, k);
            int[] res = Arrays.copyOfRange(a, k, a.length);

            for (; max / exp > 0; exp *= 10) {
                LSDSort(arr, exp);
            }

            System.arraycopy(arr, 0, a, 0, arr.length);
            System.arraycopy(res, 0, a, arr.length, res.length);
        }

        public int getMax(int[] arr) {
            int max = arr[0];
            for (int i : arr) {
                if (i > max) {
                    max = i;
                }
            }
            return max;
        }

        public void LSDSort(int[] arr, int exp) {
            int[] output = new int[arr.length];
            int[] buckets = new int[10];

            for (int i : arr) {
                buckets[(i / exp) % 10]++;
            }

            for (int i = 1; i < 10; i++) {
                buckets[i] += buckets[i - 1];
            }

            for (int i = arr.length - 1; i >= 0; i--) {
                output[buckets[(arr[i] / exp) % 10] - 1] = arr[i];
                buckets[(arr[i] / exp) % 10]--;
            }

            for (int i = 0; i < arr.length; i++) {
                arr[i] = output[i];
            }

        }
        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /**
     * Exchange A[I] and A[J].
     */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
