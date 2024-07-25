package net.nawaman.textj.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import functionalj.list.FuncList;

class SequenceTest {
    
    @Test
    void testConstructor() {
        var seq = new Sequence(new Str("Hello"), new Str(" "), new Str("World"));
        assertEquals("Hello World", seq.toString());
        
        var emptySeq = new Sequence();
        assertEquals("", emptySeq.toString());
        
        var nullArraySeq = new Sequence((Text[]) null);
        assertEquals("", nullArraySeq.toString());
        
        var nullListSeq = new Sequence((FuncList<Text>) null);
        assertEquals("", nullListSeq.toString());
    }
    
    @Test
    void testConstructorWithFuncList() {
        var list = FuncList.<Text>of(new Str("Hello"), new Str(" "), new Str("World"));
        var seq = new Sequence(list);
        assertEquals("Hello World", seq.toString());
    }
    
    @Test
    void testCharAt() {
        var seq = new Sequence(new Str("Hello"), new Str(" "), new Str("World"));
        assertEquals('H', seq.charAt(0));
        assertEquals('o', seq.charAt(4));
        assertEquals(' ', seq.charAt(5));
        assertEquals('W', seq.charAt(6));
        assertEquals('d', seq.charAt(10));
    }
    
    @Test
    void testCharAtOutOfBounds() {
        var seq = new Sequence(new Str("Hello"), new Str(" "), new Str("World"));
        assertThrows(IndexOutOfBoundsException.class, () -> seq.charAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> seq.charAt(11));
    }
    
    @Test
    void testLength() {
        var seq = new Sequence(new Str("Hello"), new Str(" "), new Str("World"));
        assertEquals(11, seq.length());
        
        var emptySeq = new Sequence();
        assertEquals(0, emptySeq.length());
    }
    
    @Test
    void testSubSequence() {
        var seq = new Sequence(new Str("Hello"), new Str(" "), new Str("World"));
        Text subSeq = seq.subSequence(0, 5);
        assertEquals("Hello", subSeq.toString());
        
        subSeq = seq.subSequence(6, 11);
        assertEquals("World", subSeq.toString());
        
        subSeq = seq.subSequence(0, 11);
        assertEquals("Hello World", subSeq.toString());
    }
    
    @Test
    void testToString() {
        var seq = new Sequence(new Str("Hello"), new Str(" "), new Str("World"));
        assertEquals("Hello World", seq.toString());
        
        var emptySeq = new Sequence();
        assertEquals("", emptySeq.toString());
    }
}