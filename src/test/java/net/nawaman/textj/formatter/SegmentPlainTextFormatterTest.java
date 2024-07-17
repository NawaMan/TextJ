package net.nawaman.textj.formatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.nawaman.textj.Text;

class SegmentPlainTextFormatterTest {
    
    Text                 code      = new Text("first line\nsecond line\nthird line\nfourth line\nfifth line\nsixth line");
    SegmentFormatter formatter = new SegmentPlainTextFormatter(code);
    
    @Test
    void testFirst() {
        var expected = """
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first line
                  2 |second line
                """;
        assertEquals(expected, formatter.byLines(0, 1).toString());
    }
    
    @Test
    void testSecond() {
        var expected = """
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |second line
                  3 |third line
                """;
        assertEquals(expected, formatter.byLines(1, 2).toString());
    }
    
    @Test
    void testLast() {
        var expected = """
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  5 |fifth line
                  6 |sixth line
                """;
        assertEquals(expected, formatter.byLines(4, 5).toString());
    }
    
    @Test
    void testAll() {
        var expected = """
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first line
                  2 |second line
                  3 |third line
                  4 |fourth line
                  5 |fifth line
                  6 |sixth line
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                    |        10        20        30        40        50        60        70        80
                """;
        assertEquals(expected, formatter.byLines(0, Integer.MAX_VALUE).toString());
    }
    
    @Test
    void testOneRuler() {
        formatter = new SegmentPlainTextFormatter(code, true);
        var expected = """
                ----|----+---10----+---20----+---30----+---40----+---50----+---60----+---70----+---80
                  1 |first line
                  2 |second line
                  3 |third line
                  4 |fourth line
                  5 |fifth line
                  6 |sixth line
                ----|----+---10----+---20----+---30----+---40----+---50----+---60----+---70----+---80
                """;
        assertEquals(expected, formatter.byLines(0, Integer.MAX_VALUE).toString());
    }
    
}
