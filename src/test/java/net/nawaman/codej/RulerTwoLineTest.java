package net.nawaman.codej;

import static net.nawaman.codej.formatter.RulerTwoLine.bottomTwoLineRuler;
import static net.nawaman.codej.formatter.RulerTwoLine.topTwoLineRuler;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.nawaman.codej.formatter.RulerTwoLine;

class RulerTwoLineTest {
    
    @Test
    void testTopTwoLineRuler() {
        var ruler  = topTwoLineRuler;
        var buffer = new StringBuilder();
        ruler.addRuler(buffer, "    |", 20);
        
        var expected = 
            "    |        10        20\n" +
            "----|----+----|----+----|";
        
        assertEquals(expected, buffer.toString());
    }
    
    @Test
    void testBottomTwoLineRuler() {
        var ruler  = bottomTwoLineRuler;
        var buffer = new StringBuilder();
        ruler.addRuler(buffer, "  |", 30);
        
        var expected = 
            "--|----+----|----+----|----+----|\n" +
            "  |        10        20        30";
        
        assertEquals(expected, buffer.toString());
    }
    
    @Test
    void testLargeWidth() {
        var ruler  = new RulerTwoLine(true);
        var buffer = new StringBuilder();
        ruler.addRuler(buffer, "", 100);
        
        var expected = 
            "        10        20        30        40        50        60        70        80        90       100\n" +
            "----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|";
        
        assertEquals(expected, buffer.toString());
    }
    
    @Test
    void testWithLongPrefix() {
        var ruler  = new RulerTwoLine(false);
        var buffer = new StringBuilder();
        ruler.addRuler(buffer, "        |", 40);
        
        var expected = 
            "--------|----+----|----+----|----+----|----+----|\n" +
            "        |        10        20        30        40";
        
        assertEquals(expected, buffer.toString());
    }
    
}