package net.nawaman.codej.internal;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static net.nawaman.codej.internal.BinarySearch.findIndex;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

import functionalj.list.FuncList;
import functionalj.list.intlist.IntFuncList;
import functionalj.ref.Ref;

/**
 * A sorted array of ints.
 * 
 * This array only allow incremental int absolute values to be added.
 * That is the value added must have its absolute value larger than the last value added.
 * 
 * As the incremental requirement is done on the absolute value of each value,
 *   the negativity can be used as a flag to indicate a special value.
 */
public class SortedAbsoluteIntArray {
    
    /** The default initial list capacity. */
    public static int DEFAULT_INITIAL_LIST_CAPACITY = 16;
    /** The default array length. */
    public static int DEFAULT_ARRAY_LENGTH = 32;
    
    /** The reference to the initial list capacity. */
    public static Ref<Integer> DefaultInitialListCapacity = Ref.ofValue(DEFAULT_INITIAL_LIST_CAPACITY);
    
    /** The reference to the array length. */
    public static Ref<Integer> DefaultArrayLength = Ref.ofValue(DEFAULT_ARRAY_LENGTH);
    
    private final List<int[]> values;
    private final int         arrayLength;
    
    private int nextIndexInArray = 0;
    private int latestValue      = 0;
    
    private final IntFunction<IntUnaryOperator> indexAt;
    
    /** Construct a SortedAbsoluteIntArray with the default initial list capacity and array length. */
    public SortedAbsoluteIntArray() {
        this(DefaultInitialListCapacity.orElse(DEFAULT_INITIAL_LIST_CAPACITY),
             DefaultArrayLength.orElse(DEFAULT_ARRAY_LENGTH));
    }
    
    /**
     * Construct a SortedAbsoluteIntArray with the given initial list capacity and array length.
     * 
     * @param initialListCapacity  the initial list capacity.
     * @param arrayLength          the array length.
     */
    public SortedAbsoluteIntArray(int initialListCapacity, int arrayLength) {
        initialListCapacity = min(max(initialListCapacity, 2), DEFAULT_INITIAL_LIST_CAPACITY);
        this.values = new ArrayList<>(initialListCapacity);
        
        arrayLength = min(max(arrayLength, 2), DEFAULT_ARRAY_LENGTH);
        this.arrayLength = arrayLength;
        
        this.indexAt = i -> j -> abs(values.get(i)[j]);
    }
    
    /**
     * Construct a SortedAbsoluteIntArray with the given initial list capacity and
     * the default array length.
     * 
     * @param initialListCapacity  the initial list capacity.
     */
    final int arrayLength() {
        return arrayLength;
    }
    
    /**
     * Construct a SortedAbsoluteIntArray with the default initial list capacity and
     * the given array length.
     * 
     * @param arrayLength  the array length.
     */
    final int nextIndexInArray() {
        return nextIndexInArray;
    }
    
    /**
     * Construct a SortedAbsoluteIntArray with the given initial list capacity and
     * the given array length.
     * 
     * @param initialListCapacity  the initial list capacity.
     * @param arrayLength          the array length.
     */
    final String xray() {
        return FuncList.from(values)
                .map(IntFuncList::ints)
                .toString();
    }
    
    /**
     * Adds a value to the array.
     * 
     * The value added must have its absolute value larger than the last value added.
     * 
     * @param value  the value to add.
     */
    public final void add(int value) {
        if (abs(value) <= abs(latestValue)) {
            var message = "The value " + value + " is not larger than the last value " + latestValue + ".";
            throw new IllegalArgumentException(message);
        }
        
        latestValue = value;
        
        if (nextIndexInArray >= arrayLength) {
            nextIndexInArray = 0;
            values.add(new int[arrayLength]);
        }
        
        int lastLine = values.size();
        if (lastLine == 0) {
            values.add(new int[arrayLength]);
            lastLine = 1;
        }
        
        var array = values.get(lastLine - 1);
        array[nextIndexInArray] = value;
        
        nextIndexInArray++; 
    }
    
    /**
     * Returns the count of values in the array.
     * 
     * @return  the count of values in the array.
     */
    public final int count() {
        if (nextIndexInArray == 0) {
            return 0;
        }
        
        int count = (values.size() - 1)*arrayLength + nextIndexInArray;
        return count;
    }
    
    /**
     * Returns the value at the given index in the array.
     * 
     * @param index  the index of the value to get.
     * @return       the value at the given index in the array.
     */
    public final int get(int index) {
        int arrayCount = values.size();
        if (arrayCount == 0) {
            throw new ArrayIndexOutOfBoundsException("Index out of bound: " + index);
        }
        
        int lineIndex = index / arrayLength;
        int columnIndex = index % arrayLength;
        if (lineIndex >= arrayCount) {
            throw new ArrayIndexOutOfBoundsException("Index out of bound: " + index);
        }
        
        var array = values.get(lineIndex);
        return array[columnIndex];
    }
    
    /**
     * Returns the values in the array.
     * 
     * @return  the values in the array
     */
    public final IntFuncList values() {
        var lineCount = count();
        return IntFuncList.from(values.stream().flatMapToInt(array -> IntStream.of(array)))
                .limit(lineCount);
    }
    
    /**
     * Returns the index of the needle in the array 
     *   where the absolute value of the stored value at the index is just above the absolute value of the given value.
     * 
     * @param  needle  the needle to find.
     * @return         the index of the needle in the array.
     */
    public final int indexOf(int needle) {
        int valueSize = values.size();
        if (valueSize == 0) {
            return 0;
        }
        
        return findIndex(indexAt, valueSize, arrayLength, abs(needle));
    }
    
}
