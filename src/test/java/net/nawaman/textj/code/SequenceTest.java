package net.nawaman.textj.code;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import functionalj.list.FuncList;
import net.nawaman.textj.text.ImmutableCharSequence;
import net.nawaman.textj.text.Sequence;
import net.nawaman.textj.text.Str;

class SequenceTest {
    
    @Test
    void testConstructor() {
        Sequence seq = new Sequence(new Str("Hello"), new Str(" "), new Str("World"));
        assertEquals("Hello World", seq.toString());
        
        Sequence emptySeq = new Sequence();
        assertEquals("", emptySeq.toString());
        
        Sequence nullArraySeq = new Sequence((ImmutableCharSequence[]) null);
        assertEquals("", nullArraySeq.toString());
        
        Sequence nullListSeq = new Sequence((FuncList<ImmutableCharSequence>) null);
        assertEquals("", nullListSeq.toString());
    }
    
    @Test
    void testConstructorWithFuncList() {
        FuncList<ImmutableCharSequence> list = FuncList.of(new Str("Hello"), new Str(" "), new Str("World"));
        Sequence seq = new Sequence(list);
        assertEquals("Hello World", seq.toString());
    }
    
    @Test
    void testCharAt() {
        Sequence seq = new Sequence(new Str("Hello"), new Str(" "), new Str("World"));
        assertEquals('H', seq.charAt(0));
        assertEquals('o', seq.charAt(4));
        assertEquals(' ', seq.charAt(5));
        assertEquals('W', seq.charAt(6));
        assertEquals('d', seq.charAt(10));
    }
    
    @Test
    void testCharAtOutOfBounds() {
        Sequence seq = new Sequence(new Str("Hello"), new Str(" "), new Str("World"));
        assertThrows(IndexOutOfBoundsException.class, () -> seq.charAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> seq.charAt(11));
    }
    
    @Test
    void testLength() {
        Sequence seq = new Sequence(new Str("Hello"), new Str(" "), new Str("World"));
        assertEquals(11, seq.length());
        
        Sequence emptySeq = new Sequence();
        assertEquals(0, emptySeq.length());
    }
    
    @Test
    void testSubSequence() {
        Sequence seq = new Sequence(new Str("Hello"), new Str(" "), new Str("World"));
        ImmutableCharSequence subSeq = seq.subSequence(0, 5);
        assertEquals("Hello", subSeq.toString());
        
        subSeq = seq.subSequence(6, 11);
        assertEquals("World", subSeq.toString());
        
        subSeq = seq.subSequence(0, 11);
        assertEquals("Hello World", subSeq.toString());
    }
    
    @Test
    void testToString() {
        Sequence seq = new Sequence(new Str("Hello"), new Str(" "), new Str("World"));
        assertEquals("Hello World", seq.toString());
        
        Sequence emptySeq = new Sequence();
        assertEquals("", emptySeq.toString());
    }
}