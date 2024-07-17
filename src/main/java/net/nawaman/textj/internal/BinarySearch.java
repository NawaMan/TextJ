package net.nawaman.textj.internal;

import static java.lang.Math.max;

import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;

public final class BinarySearch {
    
    private BinarySearch() {
    }
    
    /**
     * Performs a binary search on a virtual array defined by an IntUnaryOperator.
     * 
     * @param values     An IntUnaryOperator that represents the virtual array to search.
     *                   It takes an index as input and returns the value at that index.
     * @param stopIndex  The exclusive upper bound of the search range.
     *                   The search will be performed on indices from 0 to stopIndex - 1.
     * @param needle     The value to search for in the virtual array.
     * @return           The index of the needle if found, or the insertion point if not found.
     *                     The insertion point is the index where the needle would be inserted
     *                     to maintain the sorted order of the virtual array.
     * 
     * @implNote This method assumes that the virtual array defined by the IntUnaryOperator
     *           is sorted in ascending order. If this condition is not met, the behavior
     *           is undefined.
     * 
     * @throws ArithmeticException if the calculation of the midpoint overflows
     */
    public static int findIndex(IntUnaryOperator values, int stopIndex, int needle) {
        if (needle <= values.applyAsInt(0)) {
            return 0;
        }
        int lastKnow = values.applyAsInt(stopIndex - 1);
        if (needle > lastKnow) {
            return stopIndex;
        }
        
        int left = 0;
        int right = stopIndex - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            int midValue = values.applyAsInt(mid);
            if (midValue == needle) {
                return mid;
            } else if (midValue < needle) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return left;
    }
    
    /**
     * Performs a two-level binary search on a 2D virtual array defined by an IntFunction<IntUnaryOperator>.
     *
     * @param values        An IntFunction that returns an IntUnaryOperator for each row of the 2D virtual array.
     *                      The outer function takes a row index, and the inner function takes a column index.
     * @param stopIndex     The exclusive upper bound for the number of rows to search.
     * @param subStopIndex  The exclusive upper bound for the number of columns in each row to search.
     * @param needle        The value to search for in the 2D virtual array.
     * @return              The flattened index of the needle if found, or the insertion point if not found.
     *                        The flattened index is calculated as (row * subStopIndex + column).
     *
     * @implNote This method assumes that:
     *           1. The 2D virtual array is sorted in ascending order, both across rows and within each row.
     *           2. The first element of each row (column 0) is used for the initial row-wise search.
     *           3. If the needle is not found, the returned index represents where the needle would be
     *              inserted to maintain the sorted order in the flattened 2D array.
     *
     * @throws ArithmeticException  if any midpoint or index calculations overflow.
     * @see #findIndex(IntUnaryOperator, int, int) This method uses the single-dimension findIndex internally.
     */
    public static int findIndex(IntFunction<IntUnaryOperator> values, int stopIndex, int maxSubStop, IntUnaryOperator subStopIndex, int needle) {
        var first = (IntUnaryOperator)(i -> {
            return values.apply(i).applyAsInt(0);
        });
        int subIndex = max(0, findIndex(first, stopIndex, needle) - 1);
        
        var subValues = values.apply(subIndex);
        var subStop   = subStopIndex.applyAsInt(subIndex);
        int index     = findIndex(subValues, subStop, needle);
        int finalIndex = subIndex*maxSubStop + index;
        return finalIndex;
    }
    
}
