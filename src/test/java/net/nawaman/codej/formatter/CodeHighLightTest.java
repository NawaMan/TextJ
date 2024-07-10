package net.nawaman.codej.formatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CodeHighLightTest {
    
    @Test
    void testValidConstruction() {
        var highlight = new CodeHighLight(0, 5, 0xFFFFFF);
        assertEquals(0, highlight.startOffset());
        assertEquals(5, highlight.endOffset());
        assertEquals(0xFFFFFF, highlight.color());
    }
    
    @Test
    void testNegativeStartOffset() {
        var exception = assertThrows(IllegalArgumentException.class,
                () -> new CodeHighLight(-1, 5, 0xFFFFFF));
        assertTrue(exception.getMessage().contains("Start offset must be greater than or equal to 0"));
    }
    
    @Test
    void testEndOffsetLessThanStartOffset() {
        var exception = assertThrows(IllegalArgumentException.class,
                () -> new CodeHighLight(5, 3, 0xFFFFFF));
        assertTrue(exception.getMessage().contains("End offset must be greater than or equal to the start offset"));
    }
    
    @ParameterizedTest
    @CsvSource({
        "0, -1, 0",
        "5, -1, 5",
        "10, 15, 15"
    })
    void testEndOffset(int startOffset, int endOffset, int expectedEndOffset) {
        var highlight = new CodeHighLight(startOffset, endOffset, 0xFFFFFF);
        assertEquals(expectedEndOffset, highlight.endOffset());
    }
    
    @Test
    void testColor() {
        var highlight = new CodeHighLight(0, 5, 0xABCDEF);
        assertEquals(0xABCDEF, highlight.color());
    }
    
    @Test
    void testEqualityAndHashCode() {
        var highlight1 = new CodeHighLight(0, 5, 0xFFFFFF);
        var highlight2 = new CodeHighLight(0, 5, 0xFFFFFF);
        var highlight3 = new CodeHighLight(0, 6, 0xFFFFFF);
        
        assertEquals(highlight1, highlight2);
        assertNotEquals(highlight1, highlight3);
        assertEquals(highlight1.hashCode(), highlight2.hashCode());
    }
    
    @Test
    void testToString() {
        var highlight = new CodeHighLight(0, 5, 0xFFFFFF);
        var toString  = highlight.toString();
        assertTrue(toString.contains("startOffset=0"));
        assertTrue(toString.contains("endOffset=5"));
        assertTrue(toString.contains("color=16777215")); // 0xFFFFFF in decimal
    }
}