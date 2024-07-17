package net.nawaman.textj.internal;

import static net.nawaman.textj.internal.BinarySearch.findIndex;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;

import org.junit.jupiter.api.Test;

class BinarySearchTest {
    
    @Test
    void testExact() {
        var array  = new int[] { 10, 20, 30, 40, 50 };
        var values = (IntUnaryOperator)(i -> array[i]);
        assertEquals(0, findIndex(values, array.length, 10));
        assertEquals(1, findIndex(values, array.length, 20));
        assertEquals(2, findIndex(values, array.length, 30));
        assertEquals(3, findIndex(values, array.length, 40));
        assertEquals(4, findIndex(values, array.length, 50));
    }
    
    @Test
    void testInexact() {
        var array  = new int[] { 10, 20, 30, 40, 50 };
        var values = (IntUnaryOperator)(i -> array[i]);
        assertEquals(0, findIndex(values, array.length,  5));
        assertEquals(1, findIndex(values, array.length, 15));
        assertEquals(2, findIndex(values, array.length, 25));
        assertEquals(3, findIndex(values, array.length, 35));
        assertEquals(4, findIndex(values, array.length, 45));
        assertEquals(5, findIndex(values, array.length, 55));
    }
    
    @Test
    void testTwoLevels() {
        var array = new int[][] {
            new int[] { 10, 20, 30, 40,  50 },
            new int[] { 60, 70, 80, 90, 100 }
        };
        var values       = (IntFunction<IntUnaryOperator>)(i -> j -> array[i][j]);
        var length       = array.length;
        var subLength    = array[0].length;
        var subStopIndex = (IntUnaryOperator)(i -> {
            return subLength;
        });
        assertEquals( 0, findIndex(values, length, subLength, subStopIndex,   5));
        assertEquals( 0, findIndex(values, length, subLength, subStopIndex,  10));
        assertEquals( 1, findIndex(values, length, subLength, subStopIndex,  11));
        assertEquals( 1, findIndex(values, length, subLength, subStopIndex,  15));
        assertEquals( 1, findIndex(values, length, subLength, subStopIndex,  20));
        assertEquals( 2, findIndex(values, length, subLength, subStopIndex,  25));
        assertEquals( 4, findIndex(values, length, subLength, subStopIndex,  50));
        assertEquals( 5, findIndex(values, length, subLength, subStopIndex,  55));
        assertEquals( 7, findIndex(values, length, subLength, subStopIndex,  75));
        assertEquals( 7, findIndex(values, length, subLength, subStopIndex,  80));
        assertEquals( 8, findIndex(values, length, subLength, subStopIndex,  90));
        assertEquals( 9, findIndex(values, length, subLength, subStopIndex,  95));
        assertEquals( 9, findIndex(values, length, subLength, subStopIndex, 100));
        assertEquals(10, findIndex(values, length, subLength, subStopIndex, 105));
    }
    
    @Test
    void testIncomplete() {
        var array = new int[][] {
            new int[] { 10, 20, 30, 40,  50 },
            new int[] { 60, 70, 80,  0,   0 }
        };
        var values       = (IntFunction<IntUnaryOperator>)(i -> j -> array[i][j]);
        var length       = array.length;
        var subLength    = array[0].length;
        var subStopIndex = (IntUnaryOperator)(i -> {
            return (i < 1) ? subLength : 3;
        });
        assertEquals( 0, findIndex(values, length, subLength, subStopIndex,   5));
        assertEquals( 0, findIndex(values, length, subLength, subStopIndex,  10));
        
        assertEquals( 1, findIndex(values, length, subLength, subStopIndex,  11));
        assertEquals( 1, findIndex(values, length, subLength, subStopIndex,  15));
        assertEquals( 1, findIndex(values, length, subLength, subStopIndex,  20));
        
        assertEquals( 2, findIndex(values, length, subLength, subStopIndex,  25));
        assertEquals( 4, findIndex(values, length, subLength, subStopIndex,  50));
        assertEquals( 5, findIndex(values, length, subLength, subStopIndex,  55));
        
        assertEquals( 6, findIndex(values, length, subLength, subStopIndex,  65));
        assertEquals( 6, findIndex(values, length, subLength, subStopIndex,  70));
        
        assertEquals( 7, findIndex(values, length, subLength, subStopIndex,  75));
        assertEquals( 7, findIndex(values, length, subLength, subStopIndex,  80));
        
        assertEquals( 8, findIndex(values, length, subLength, subStopIndex,  85));
        assertEquals( 8, findIndex(values, length, subLength, subStopIndex,  90));
        assertEquals( 8, findIndex(values, length, subLength, subStopIndex,  95));
        assertEquals( 8, findIndex(values, length, subLength, subStopIndex, 100));
    }
    
}
