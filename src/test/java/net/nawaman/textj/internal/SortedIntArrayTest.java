package net.nawaman.textj.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import functionalj.ref.Run;

// White box testing of SortedAbsoluteIntArray.
class SortedIntArrayTest {
    
    @Test
    void testBasic() {
        var array = new SortedAbsoluteIntArray(4, 4);
        assertEquals("[]", array.xray());
        assertEquals("[]", array.values().toString());
        assertEquals(0, array.nextIndexInArray());
        
        array.add(10);
        assertEquals("[[10, 0, 0, 0]]", array.xray());
        assertEquals("[10]", array.values().toString());
        assertEquals(1, array.nextIndexInArray());
        
        array.add(21);
        assertEquals("[[10, 21, 0, 0]]", array.xray());
        assertEquals("[10, 21]", array.values().toString());
        assertEquals(2, array.nextIndexInArray());
    }
    
    @Test
    void testArrayLength() {
        // Given size
        assertEquals(4, new SortedAbsoluteIntArray(4, 4).arrayLength());
        
        // Default size
        assertEquals(
                SortedAbsoluteIntArray.DEFAULT_ARRAY_LENGTH,
                new SortedAbsoluteIntArray().arrayLength());
        
        // Set Ref value
        Run.with(SortedAbsoluteIntArray.DefaultArrayLength.butWith(8)).run(() -> {
            assertEquals(8, new SortedAbsoluteIntArray().arrayLength());
        });
    }
    
    @Test
    void testMoreThanArray() {
        var array = new SortedAbsoluteIntArray(4, 4);
        array.add(10);
        array.add(21);
        array.add(32);
        assertEquals("[[10, 21, 32, 0]]", array.xray());
        assertEquals("[10, 21, 32]", array.values().toString());
        assertEquals(3, array.nextIndexInArray());
        
        array.add(43);
        assertEquals("[[10, 21, 32, 43]]", array.xray());
        assertEquals("[10, 21, 32, 43]", array.values().toString());
        assertEquals(4, array.nextIndexInArray());
        
        array.add(54);
        assertEquals("[[10, 21, 32, 43], [54, 0, 0, 0]]", array.xray());
        assertEquals("[10, 21, 32, 43, 54]", array.values().toString());
        assertEquals(1, array.nextIndexInArray());
    }
    
    @Test
    void testNegative() {
        // Negative values are allowed as long as they are its absolute value larger than the previous
        var array = new SortedAbsoluteIntArray(4, 4);
        array.add(10);
        array.add(-21);
        array.add(32);
        assertEquals("[[10, -21, 32, 0]]", array.xray());
        assertEquals("[10, -21, 32]", array.values().toString());
        assertEquals(3, array.nextIndexInArray());
    }
    
    @Test
    void testInvalid() {
        var array = new SortedAbsoluteIntArray(4, 4);
        array.add(10);
        array.add(21);
        assertEquals("[[10, 21, 0, 0]]", array.xray());
        
        assertEquals(
                "java.lang.IllegalArgumentException: The value 5 is not larger than the last value 21.",
                assertThrows(IllegalArgumentException.class, () -> array.add(5)).toString());
        
        assertEquals(
                "java.lang.IllegalArgumentException: The value -15 is not larger than the last value 21.",
                assertThrows(IllegalArgumentException.class, () -> array.add(-15)).toString());
    }
    
    @Test
    void testIndexOf() {
        // Negative values are allowed as long as they are its absolute value larger than the previous
        var array = new SortedAbsoluteIntArray(4, 4);
        assertEquals(0, array.indexOf(0));
        
        array.add(10);
        array.add(-21);
        array.add(32);
        array.add(43);
        array.add(54);
        array.add(-65);
        array.add(76);
        array.add(87);
        assertEquals("[[10, -21, 32, 43], [54, -65, 76, 87]]", array.xray());
        assertEquals("[10, -21, 32, 43, 54, -65, 76, 87]",     array.values().toString());
        
        assertEquals(0, array.indexOf( 0));
        assertEquals(0, array.indexOf(-5));
        
        assertEquals(0, array.indexOf(10));
        assertEquals(1, array.indexOf(11));
        assertEquals(1, array.indexOf(12));
        
        assertEquals(1, array.indexOf(20));
        assertEquals(1, array.indexOf(21));
        assertEquals(2, array.indexOf(22));
        
        assertEquals(3, array.indexOf(42));
        assertEquals(3, array.indexOf(43));
        assertEquals(4, array.indexOf(44));
        
        assertEquals(4, array.indexOf(53));
        assertEquals(4, array.indexOf(54));
        assertEquals(5, array.indexOf(55));
        
        assertEquals(5, array.indexOf(64));
        assertEquals(5, array.indexOf(65));
        assertEquals(6, array.indexOf(66));
        
        assertEquals(7, array.indexOf(86));
        assertEquals(7, array.indexOf(87));
        assertEquals(8, array.indexOf(89));
    }
    
    @Test
    void testCount() {
        // Negative values are allowed as long as they are its absolute value larger than the previous
        var array = new SortedAbsoluteIntArray(4, 4);
        assertEquals(0, array.length());
        
        array.add(10);
        assertEquals(1, array.length());
        
        array.add(-21);
        assertEquals(2, array.length());
        
        array.add(32);
        assertEquals(3, array.length());
        
        array.add(43);
        assertEquals(4, array.length());
        
        array.add(54);
        assertEquals(5, array.length());
        
        array.add(-65);
        assertEquals(6, array.length());
        
        array.add(76);
        assertEquals(7, array.length());
        
        array.add(87);
        assertEquals(8, array.length());
        
        array.add(98);
        assertEquals(9, array.length());
        
        assertEquals("[[10, -21, 32, 43], [54, -65, 76, 87], [98, 0, 0, 0]]", array.xray());
        assertEquals("[10, -21, 32, 43, 54, -65, 76, 87, 98]",                array.values().toString());
    }
    
    @Test
    void testGet() {
        // Negative values are allowed as long as they are its absolute value larger than the previous
        var array = new SortedAbsoluteIntArray(4, 4);
        assertEquals(
                "Index out of bound: 0",
                assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> array.get(0)).getMessage());
        
        array.add(10);
        array.add(-21);
        array.add(32);
        array.add(43);
        array.add(54);
        array.add(-65);
        array.add(76);
        array.add(87);
        assertEquals("[[10, -21, 32, 43], [54, -65, 76, 87]]", array.xray());
        assertEquals("[10, -21, 32, 43, 54, -65, 76, 87]",     array.values().toString());
        
        assertEquals( 10, array.get( 0));
        assertEquals(-21, array.get( 1));
        assertEquals( 32, array.get( 2));
        assertEquals( 43, array.get( 3));
        assertEquals( 54, array.get( 4));
        assertEquals(-65, array.get( 5));
        assertEquals( 76, array.get( 6));
        assertEquals( 87, array.get( 7));
        assertEquals( 87, array.get( 7));
        
        assertEquals(
                "Index out of bound: 30",
                assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> array.get(30)).getMessage());
    }
    
}
