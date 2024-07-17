package net.nawaman.textj.formatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.nawaman.textj.Text;


class SegmentVT100FormatterTest {
    
    Text             code      = new Text("0123\n012345678\n01234567890123\n0123456789012345678\r01234567890123456789012\r\n012345678901234567890123456789");
    SegmentFormatter formatter = new SegmentVT100Formatter(code);
    
    boolean printActual = false;
    
    @Test
    void testFirst() {
        var expected = """
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |0123[38;2;200;200;200m¶[0m
                  2 |012345678[38;2;200;200;200m¶[0m
                """;
        var actual = formatter.byLines(0, 1).toString();
        if (printActual) {
            System.out.println("-- %s --".formatted("testFirst"));
            System.out.println(actual);
        }
        assertEquals(expected, actual);
    }
    
    @Test
    void testSecond() {
        var expected = """
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |012345678[38;2;200;200;200m¶[0m
                  3 |01234567890123[38;2;200;200;200m¶[0m
                """;
        var actual = formatter.byLines(1, 2).toString();
        if (printActual) {
            System.out.println("-- %s --".formatted("testSecond"));
            System.out.println(actual);
        }
        assertEquals(expected, actual);
    }
    
    @Test
    void testThird() {
        var expected = """
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  3 |01234567890123[38;2;200;200;200m¶[0m
                  4 |0123456789012345678[38;2;200;200;200m↵[0m
                """;
        var actual = formatter.byLines(2, 3).toString();
        if (printActual) {
            System.out.println("-- %s --".formatted("testThird"));
            System.out.println(actual);
        }
        assertEquals(expected, actual);
    }
    
    @Test
    void testForth() {
        var expected = """
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  4 |0123456789012345678[38;2;200;200;200m↵[0m
                  5 |01234567890123456789012[38;2;200;200;200m↵[0m[38;2;200;200;200m¶[0m
                """;
        var actual = formatter.byLines(3, 4).toString();
        if (printActual) {
            System.out.println("-- %s --".formatted("testForth"));
            System.out.println(actual);
        }
        assertEquals(expected, actual);
    }
    
    @Test
    void testLast() {
        var expected = """
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  5 |01234567890123456789012[38;2;200;200;200m↵[0m[38;2;200;200;200m¶[0m
                  6 |012345678901234567890123456789
                """;
        var actual = formatter.byLines(4, 5).toString();
        if (printActual) {
            System.out.println("-- %s --".formatted("testLast"));
            System.out.println(actual);
        }
        assertEquals(expected, actual);
    }
    
    @Test
    void testAll() {
        var expected = """
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |0123[38;2;200;200;200m¶[0m
                  2 |012345678[38;2;200;200;200m¶[0m
                  3 |01234567890123[38;2;200;200;200m¶[0m
                  4 |0123456789012345678[38;2;200;200;200m↵[0m
                  5 |01234567890123456789012[38;2;200;200;200m↵[0m[38;2;200;200;200m¶[0m
                  6 |012345678901234567890123456789
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                    |        10        20        30        40        50        60        70        80
                """;
        var actual = formatter.byLines(0, Integer.MAX_VALUE).toString();
        if (printActual) {
            System.out.println("-- %s --".formatted("testAll"));
            System.out.println(actual);
        }
        assertEquals(expected, actual);
    }
    
    String javaContent = """
        public class PascalTriangle {
            public static void main(String[] args) {
                int rows = 10;
                for (int i = 0; i < rows; i++) {
                    int number = 1;
                    System.out.format("%" + (rows - i) * 2 + "s", "");
                    for (int j = 0; j <= i; j++) {
                        System.out.format("%4d", number);
                        number = number * (i - j) / (j + 1);
                    }
                    System.out.println();
                }
            }
        }
        """.replaceFirst("\n", "\r\n");
    
    Text             javaCode      = new Text(javaContent);
    SegmentFormatter javaFormatter = new SegmentVT100Formatter(javaCode);
    
    @Test
    void testHighLights() {
        var expected = """
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |public[38;2;200;200;200m·[0mclass[38;2;200;200;200m·[0m[41;1;3;37mPascalTriangle[0m[38;2;200;200;200m·[0m{[38;2;200;200;200m↵[0m[38;2;200;200;200m¶[0m
                  2 |[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0mpublic[38;2;200;200;200m·[0mstatic[38;2;200;200;200m·[0mvoid[38;2;200;200;200m·[0mmain(String[][38;2;200;200;200m·[0margs)[38;2;200;200;200m·[0m{[38;2;200;200;200m¶[0m
                  3 |[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[42;1;3;37m·int·rows·=·1[0m0;[38;2;200;200;200m¶[0m
                  4 |[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0mfor[38;2;200;200;200m·[0m(int[38;2;200;200;200m·[0mi[38;2;200;200;200m·[0m=[38;2;200;200;200m·[0m0;[38;2;200;200;200m·[0mi[38;2;200;200;200m·[0m<[38;2;200;200;200m·[0mrows;[38;2;200;200;200m·[0mi+[44;1;3;37m+)·{¶[0m
                  5 |[44;1;3;37m············in[0mt[38;2;200;200;200m·[0mnumber[45;1;3;37m·=·1;¶[0m
                  6 |[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0mSystem.out.format("%"[38;2;200;200;200m·[0m+[38;2;200;200;200m·[0m(rows[38;2;200;200;200m·[0m-[38;2;200;200;200m·[0mi)[38;2;200;200;200m·[0m*[38;2;200;200;200m·[0m2[38;2;200;200;200m·[0m+[38;2;200;200;200m·[0m"s",[38;2;200;200;200m·[0m"");[46;1;3;37m¶[0m
                  7 |[46;1;3;37m············[0mfor[38;2;200;200;200m·[0m(int[38;2;200;200;200m·[0mj[38;2;200;200;200m·[0m=[38;2;200;200;200m·[0m0;[38;2;200;200;200m·[0mj[38;2;200;200;200m·[0m<=[38;2;200;200;200m·[0mi;[38;2;200;200;200m·[0mj++)[38;2;200;200;200m·[0m{[38;2;200;200;200m¶[0m
                  8 |[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[100;1;3;37m··········System.out.format("%4d",·number);¶[0m
                  9 |[100;1;3;37m················number·=·number·*·(i·-·j)·/·(j·+·1);¶[0m
                 10 |[100;1;3;37m············}¶[0m
                 11 |[100;1;3;37m·········[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0mSystem.out.println();[38;2;200;200;200m¶[0m
                 12 |[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m}[38;2;200;200;200m¶[0m
                 13 |[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m[38;2;200;200;200m·[0m}[38;2;200;200;200m¶[0m
                 14 |}
                 15 |
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                    |        10        20        30        40        50        60        70        80
                """;
        var inlineHighlight        = new HighLight( 83,  96, 0);
        var crossLineHighlight     = new HighLight(135, 154, 2);
        var endAtLineHighlight     = new HighLight(162, 168, 3);
        var startAtLineHighlight   = new HighLight(230, 243, 4);
        var multipleLinesHighlight = new HighLight(280, 400, 5);
        var outOfOrderHighlight    = new HighLight( 13,  27, 6);
        var actual = javaFormatter.byLines(0, Integer.MAX_VALUE, 
                inlineHighlight,
                crossLineHighlight,
                endAtLineHighlight,
                startAtLineHighlight,
                multipleLinesHighlight,
                outOfOrderHighlight
        ).toString();
        if (printActual) {
            System.out.println("-- %s --".formatted("testHighLights"));
            System.out.println(actual);
        }
        assertEquals(expected, actual);
    }
    
}
