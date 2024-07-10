package net.nawaman.codej;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.nawaman.codej.formatter.RulerOneLine;

class RulerOneLineTest {
    
    private RulerOneLine rulerOneLine;
    
    @BeforeEach
    void setUp() {
        rulerOneLine = new RulerOneLine();
    }
    
    @Test
    void testAddRulerWithNoPrefix() {
        var buffer = new StringBuilder();
        rulerOneLine.addRuler(buffer, "", 50);
        
        var expected = "----+---10----+---20----+---30----+---40----+---50";
        assertEquals(expected, buffer.toString());
    }
    
    @Test
    void testAddRulerWithPrefix() {
        var buffer = new StringBuilder();
        rulerOneLine.addRuler(buffer, "    |", 50);
        
        var expected = "----|----+---10----+---20----+---30----+---40----+---50";
        assertEquals(expected, buffer.toString());
    }
    
    @Test
    void testAddRulerWithLongerLineWidth() {
        var buffer = new StringBuilder();
        rulerOneLine.addRuler(buffer, "", 100);
        
        var expected = "----+---10----+---20----+---30----+---40----+---50----+---60----+---70----+---80----+---90----+--100";
        assertEquals(expected, buffer.toString());
    }
    
    @Test
    void testAddRulerWithPrefixAndLongerLineWidth() {
        var buffer = new StringBuilder();
        rulerOneLine.addRuler(buffer, "  |", 100);
        
        var expected = "--|----+---10----+---20----+---30----+---40----+---50----+---60----+---70----+---80----+---90----+--100";
        assertEquals(expected, buffer.toString());
    }
    
    @Test
    void testAddRulerMultipleTimes() {
        var buffer = new StringBuilder();
        rulerOneLine.addRuler(buffer, "", 50);
        rulerOneLine.addRuler(buffer, "", 50);
        
        var expected = "----+---10----+---20----+---30----+---40----+---50----+---10----+---20----+---30----+---40----+---50";
        assertEquals(expected, buffer.toString());
    }
    
    @Test
    void testAddRulerWithVeryLongLineWidth() {
        var buffer = new StringBuilder();
        rulerOneLine.addRuler(buffer, "", 1000);
        assertTrue(buffer.toString().startsWith("----+---10----+---20"));
        assertTrue(buffer.toString().endsWith("----+--990----+-1000"));
        assertEquals(1000, buffer.length());
    }
    
    @Test
    void testAddRulerWithZeroLineWidth() {
        var buffer = new StringBuilder();
        rulerOneLine.addRuler(buffer, "", 0);
        assertEquals("", buffer.toString());
    }
    
    @Test
    void testOneLineRulerConstant() {
        assertNotNull(RulerOneLine.oneLineRuler);
        assertTrue(RulerOneLine.oneLineRuler instanceof RulerOneLine);
    }
}