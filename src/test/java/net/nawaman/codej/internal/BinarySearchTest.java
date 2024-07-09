package net.nawaman.codej.internal;

import static net.nawaman.codej.internal.BinarySearch.findIndex;
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
        var values    = (IntFunction<IntUnaryOperator>)(i -> j -> array[i][j]);
        var length    = array.length;
        var subLength = array[0].length;
        assertEquals( 0, findIndex(values, length, subLength,   5));
        assertEquals( 0, findIndex(values, length, subLength,  10));
        assertEquals( 1, findIndex(values, length, subLength,  11));
        assertEquals( 1, findIndex(values, length, subLength,  15));
        assertEquals( 1, findIndex(values, length, subLength,  20));
        assertEquals( 2, findIndex(values, length, subLength,  25));
        assertEquals( 4, findIndex(values, length, subLength,  50));
        assertEquals( 5, findIndex(values, length, subLength,  55));
        assertEquals( 8, findIndex(values, length, subLength,  90));
        assertEquals( 9, findIndex(values, length, subLength,  95));
        assertEquals( 9, findIndex(values, length, subLength, 100));
        assertEquals(10, findIndex(values, length, subLength, 105));
    }
    
}
