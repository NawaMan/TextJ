package net.nawaman.textj.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PartTest {
    
    private final Text testSequence = new Str("Hello, World!");
    
    @Test
    void testConstructorWithValidArguments() {
        var part = new Part(testSequence, 0, 5);
        assertEquals("Hello", part.toString());
        assertEquals(5, part.length());
    }
    
    @Test
    void testConstructorWithNullSequence() {
        var part = new Part(null, 0, 0);
        assertEquals("", part.toString());
        assertEquals(0, part.length());
    }
    
    @ParameterizedTest
    @CsvSource({ "-1, 5", "13, 5", "5, 3", "5, 14", "14, 18" })
    void testConstructorWithInvalidArguments(int start, int end) {
        assertThrows(IllegalArgumentException.class, () -> new Part(testSequence, start, end));
    }
    
    @Test
    void testCharAt() {
        var part = new Part(testSequence, 7, 12);
        assertEquals('W', part.charAt(0));
        assertEquals('d', part.charAt(4));
    }
    
    @Test
    void testCharAtOutOfBounds() {
        var part = new Part(testSequence, 7, 12);
        assertThrows(IndexOutOfBoundsException.class, () -> part.charAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> part.charAt(5));
    }
    
    @Test
    void testLength() {
        var part1 = new Part(testSequence, 0, 5);
        assertEquals(5, part1.length());
        
        var part2 = new Part(testSequence, 7, 12);
        assertEquals(5, part2.length());
    }
    
    @Test
    void testSubSequence() {
        var part = new Part(testSequence, 0, 13);
        var subSeq = part.subSequence(7, 12);
        assertEquals("World", subSeq.toString());
        assertEquals(5, subSeq.length());
    }
    
    @Test
    void testSubSequenceOutOfBounds() {
        var part = new Part(testSequence, 0, 13);
        assertThrows(IllegalArgumentException.class, () -> part.subSequence(-1, 5));
        assertThrows(IllegalArgumentException.class, () -> part.subSequence(0, 14));
        assertThrows(IllegalArgumentException.class, () -> part.subSequence(5, 3));
    }
    
    @Test
    void testToString() {
        var part1 = new Part(testSequence, 0, 5);
        assertEquals("Hello", part1.toString());
        
        var
        part2 = new Part(testSequence, 7, 12);
        assertEquals("World", part2.toString());
    }
}