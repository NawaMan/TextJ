package net.nawaman.textj.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class StrTest {
    
    @Test
    void testConstructorWithNullString() {
        var str = new Str(null);
        assertEquals("", str.toString());
        assertEquals(0, str.length());
    }
    
    @Test
    void testConstructorWithEmptyString() {
        var str = new Str("");
        assertEquals("", str.toString());
        assertEquals(0, str.length());
    }
    
    @Test
    void testConstructorWithNonEmptyString() {
        var str = new Str("Hello");
        assertEquals("Hello", str.toString());
        assertEquals(5, str.length());
    }
    
    @Test
    void testEmptyConstant() {
        assertEquals("", Str.empty.toString());
        assertEquals(0, Str.empty.length());
    }
    
    @Test
    void testCharAt() {
        var str = new Str("Hello");
        assertEquals('H', str.charAt(0));
        assertEquals('e', str.charAt(1));
        assertEquals('o', str.charAt(4));
    }
    
    @Test
    void testCharAtOutOfBounds() {
        var str = new Str("Hello");
        assertThrows(IndexOutOfBoundsException.class, () -> str.charAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> str.charAt(5));
    }
    
    @Test
    void testLength() {
        assertEquals(0, new Str("").length());
        assertEquals(5, new Str("Hello").length());
        assertEquals(10, new Str("0123456789").length());
    }
    
    @Test
    void testSubSequence() {
        var str = new Str("Hello, World!");
        Text subSeq = str.subSequence(0, 5);
        assertEquals("Hello", subSeq.toString());
        assertEquals(5, subSeq.length());
    }
    
    @Test
    void testSubSequenceOutOfBounds() {
        var str = new Str("Hello");
        assertThrows(IllegalArgumentException.class, () -> str.subSequence(-1, 3));
        assertThrows(IllegalArgumentException.class, () -> str.subSequence(0, 6));
        assertThrows(IllegalArgumentException.class, () -> str.subSequence(3, 2));
    }
}