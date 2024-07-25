package net.nawaman.textj.code;

import static functionalj.lens.Access.theString;
import static functionalj.list.FuncList.listOf;
import static functionalj.list.intlist.IntFuncList.range;
import static functionalj.stream.intstream.IntStreamPlus.infinite;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import functionalj.list.intlist.IntFuncList;
import net.nawaman.textj.code.formatter.SegmentPlainTextFormatter;

class CodeTest {
    
    //== Direct ==
    
    @Test
    void testContent() {
        var content = "first line\nsecond line\nthird line";
        var code    = new Code(content);
        assertEquals(content,          code.content().toString());
        assertEquals(content.length(), code.length());
    }
    
    //== Primitive feature ==
    
    @Test
    void testLineCount() {
        // Linux
        assertEquals(4, new Code("first\nsecond\nthird\nforth").lineCount());
        assertEquals(5, new Code("first\nsecond\nthird\nforth\n").lineCount());
        
        // Mac
        assertEquals(4, new Code("first\rsecond\rthird\rforth").lineCount());
        assertEquals(5, new Code("first\rsecond\rthird\rforth\r").lineCount());
        
        // Windows
        assertEquals(4, new Code("first\r\nsecond\r\nthird\r\nforth").lineCount());
        assertEquals(5, new Code("first\r\nsecond\r\nthird\r\nforth\r\n").lineCount());
        
        // Mix
        assertEquals(4, new Code("first\rsecond\r\nthird\nforth").lineCount());
        assertEquals(5, new Code("first\rsecond\r\nthird\nforth\n").lineCount());
        assertEquals(5, new Code("first\rsecond\r\nthird\nforth\r").lineCount());
        assertEquals(5, new Code("first\rsecond\r\nthird\nforth\r\n").lineCount());
    }
    
    //== Basic ==
    
    @Test
    void testBasic_noTail() {
        var content = "first\nsecond\nthird\nforth";
        var text    = new Code(content);
        assertEquals(4,             text.lineCount());
        assertEquals(3,             text.knownLineCount());
        assertEquals("[5, 12, 18]", text.newlineOffsets().toString());
        assertEquals(
                "[first, second, third, forth]",
                text.lines().toString());
    }
    
    @Test
    void testBasic_withTail() {
        var content = "first\nsecond\nthird\nforth\n";
        var text    = new Code(content);
        assertEquals(5,                 text.lineCount());
        assertEquals(4,                 text.knownLineCount());
        assertEquals("[5, 12, 18, 24]", text.newlineOffsets().toString());
        assertEquals(
                "[first, second, third, forth, ]",
                text.lines().toString());
    }
    
    @Test
    void testBasic_noTail_ensureOffset() {
        //             000000 1111111 222222 33333
        //             012345 6789012 345678 90123
        var content = "first\nsecond\nthird\nforth";
        var text    = new Code(content);
        
        assertEquals(24, text.length());
        
        assertEquals(0, text.knownLineCount());
        
        text.processLinesToOffset(0);
        assertEquals(0, text.knownLineCount());
        
        text.processLinesToOffset(2);
        assertEquals(1, text.knownLineCount());
        
        text.processLinesToOffset(5);
        assertEquals(1, text.knownLineCount());
        text.processLinesToOffset(6);
        assertEquals(2, text.knownLineCount());
        
        text.processLinesToOffset(12);
        assertEquals(2, text.knownLineCount());
        text.processLinesToOffset(13);
        assertEquals(3, text.knownLineCount());
        
        text.processLinesToOffset(18);
        assertEquals(3, text.knownLineCount());
        text.processLinesToOffset(19);
        assertEquals(3, text.knownLineCount());
        
        text.processLinesToOffset(24);
        assertEquals(3, text.knownLineCount());
        text.processLinesToOffset(25);
        assertEquals(3, text.knownLineCount());
        
        text.processLinesToOffset(Integer.MAX_VALUE);
        assertEquals(3, text.knownLineCount());
    }
    
    @Test
    void testBasic_withTail_ensureOffset() {
        var content = "first\nsecond\nthird\nforth\n";
        var text    = new Code(content);
        
        assertEquals(25, text.length());
        
        assertEquals(0, text.knownLineCount());
        
        text.processLinesToOffset(0);
        assertEquals(0, text.knownLineCount());
        
        text.processLinesToOffset(2);
        assertEquals(1, text.knownLineCount());
        
        text.processLinesToOffset(5);
        assertEquals(1, text.knownLineCount());
        text.processLinesToOffset(6);
        assertEquals(2, text.knownLineCount());
        
        text.processLinesToOffset(12);
        assertEquals(2, text.knownLineCount());
        text.processLinesToOffset(13);
        assertEquals(3, text.knownLineCount());
        
        text.processLinesToOffset(18);
        assertEquals(3, text.knownLineCount());
        text.processLinesToOffset(19);
        assertEquals(4, text.knownLineCount());
        
        text.processLinesToOffset(24);
        assertEquals(4, text.knownLineCount());
        text.processLinesToOffset(25);
        assertEquals(4, text.knownLineCount());
        text.processLinesToOffset(26);
        assertEquals(4, text.knownLineCount());
        
        text.processLinesToOffset(Integer.MAX_VALUE);
        assertEquals(4, text.knownLineCount());
    }
    
    //== Step ==
    
    @Test
    void testStep_noTail() {
        var content = "first\nsecond\nthird\nforth";
        var text    = new Code(content);
        
        assertEquals(0, text.processedOffset());
        assertEquals(0, text.knownLineCount());
        
        assertEquals("[]", text.lines().limit(0).toString());
        assertEquals(0,    text.processedOffset());
        assertEquals(0,    text.knownLineCount());
        
        assertEquals("[first]", text.lines().limit(1).toString());
        assertEquals(6,         text.processedOffset());
        assertEquals(1,         text.knownLineCount());
        
        assertEquals("[first, second]", text.lines().limit(2).toString());
        assertEquals(13,                text.processedOffset());
        assertEquals(2,                 text.knownLineCount());
        
        assertEquals("[first, second, third]", text.lines().limit(3).toString());
        assertEquals(19,                       text.processedOffset());
        assertEquals(3,                        text.knownLineCount());
        
        assertEquals("[first, second, third, forth]", text.lines().limit(4).toString());
        assertEquals(24,                              text.processedOffset());
        assertEquals(3,                               text.knownLineCount());
        
        assertEquals("[first, second, third, forth]", text.lines().limit(5).toString());
        assertEquals(24,                              text.processedOffset());
        assertEquals(3,                               text.knownLineCount());
        
        assertEquals("[first, second, third, forth]", text.lines().toString());
        assertEquals(24,                              text.processedOffset());
        assertEquals(3,                               text.knownLineCount());
    }
    
    @Test
    void testStep_withTail() {
        var content = "first\nsecond\nthird\nforth\n";
        var text    = new Code(content);
        
        assertEquals(0, text.processedOffset());
        assertEquals(0, text.knownLineCount());
        
        assertEquals("[]", "" + text.lines().limit(0).toString());
        assertEquals(0,    text.processedOffset());
        assertEquals(0,    text.knownLineCount());
        
        assertEquals("[first]", text.lines().limit(1).toString());
        assertEquals(6,         text.processedOffset());
        assertEquals(1,         text.knownLineCount());
        
        assertEquals("[first, second]", text.lines().limit(2).toString());
        assertEquals(13,                text.processedOffset());
        assertEquals(2,                 text.knownLineCount());
        
        assertEquals("[first, second, third]", text.lines().limit(3).toString());
        assertEquals(19,                       text.processedOffset());
        assertEquals(3,                        text.knownLineCount());
        
        assertEquals("[first, second, third, forth]", text.lines().limit(4).toString());
        assertEquals(25,                              text.processedOffset());
        assertEquals(4,                               text.knownLineCount());
        
        assertEquals("[first, second, third, forth, ]", text.lines().toString());
        assertEquals(25,                                text.processedOffset());
        assertEquals(4,                                 text.knownLineCount());
    }
    
    //== Linux ==
    
    @Test
    void testBasic_linux() {
        var content = "first\nsecond\nthird\nforth";
        var text    = new Code(content);
        assertEquals(4,             text.lineCount());
        assertEquals(3,             text.knownLineCount());
        assertEquals("[5, 12, 18]", text.newlineOffsets().toString());
        assertEquals(
                "["
                + "first, "
                + "second, "
                + "third, "
                + "forth"
                + "]",
                text.lines().toString());
    }
    
    @Test
    void testBasic_linux_ensureOffset() {
        var content = "first\nsecond\nthird\n";
        var text    = new Code(content);
        assertEquals(0, text.knownLineCount());
        
        text.processLinesToOffset(3);
        assertEquals(1, text.knownLineCount());
        
        text.processLinesToOffset(5);
        assertEquals(1, text.knownLineCount());
        text.processLinesToOffset(6);
        assertEquals(2, text.knownLineCount());
        
        text.processLinesToOffset(12);
        assertEquals(2, text.knownLineCount());
        text.processLinesToOffset(13);
        assertEquals(3, text.knownLineCount());
        
        text.processLinesToOffset(18);
        assertEquals(3, text.knownLineCount());
        text.processLinesToOffset(19);
        assertEquals(3, text.knownLineCount());
    }
    
    //== Mac ==
    
    @Test
    void testBasic_mac() {
        var content = "first\rsecond\rthird\rforth\r";
        var text    = new Code(content);
        assertEquals(5,                 text.lineCount());
        assertEquals(4,                 text.knownLineCount());
        assertEquals("[5, 12, 18, 24]", text.newlineOffsets().toString());
        assertEquals(
                "["
                + "first, "
                + "second, "
                + "third, "
                + "forth, "
                + "]",
                text.lines().toString());
    }
    
    @Test
    void testBasic_mac_ensureOffset() {
        var content = "first\rsecond\rthird\rforth\r";
        var text    = new Code(content);
        assertEquals(0, text.knownLineCount());
        
        text.processLinesToOffset(3);
        assertEquals(1, text.knownLineCount());
        
        text.processLinesToOffset(5);
        assertEquals(1, text.knownLineCount());
        text.processLinesToOffset(6);
        assertEquals(2, text.knownLineCount());
        
        text.processLinesToOffset(12);
        assertEquals(2, text.knownLineCount());
        text.processLinesToOffset(13);
        assertEquals(3, text.knownLineCount());
        
        text.processLinesToOffset(18);
        assertEquals(3, text.knownLineCount());
        text.processLinesToOffset(19);
        assertEquals(4, text.knownLineCount());
        
        text.processLinesToOffset(24);
        assertEquals(4, text.knownLineCount());
        text.processLinesToOffset(25);
        assertEquals(4, text.knownLineCount());
    }
    
    //== Windows == 
    
    @Test
    void testBasic_windows() {
        var content = "first\r\nsecond\r\nthird\r\nforth\r\n";
        var text    = new Code(content);
        assertEquals(5,                 text.lineCount());
        assertEquals(4,                 text.knownLineCount());
        assertEquals("[6, 14, 21, 28]", text.newlineOffsets().toString());
        assertEquals(
                "["
                + "first, "
                + "second, "
                + "third, "
                + "forth, "
                + "]",
                text.lines().toString());
    }
    
    @Test
    void testBasic_windows_ensureOffset() {
        //             0000000001111111111222222222333333333
        //             01234 5 6789012 3 456789 0 123456 7 8
        var content = "first\r\nsecond\r\nthird\r\nforth\r\n";
        var text    = new Code(content);
        
        assertEquals(0, text.knownLineCount());
        
        text.processLinesToOffset(3);
        assertEquals(1, text.knownLineCount());
        
        // The line is recognized one character earlier because once it see '\r' it check next for the '\n'
        text.processLinesToOffset(5);
        assertEquals(1, text.knownLineCount());
        text.processLinesToOffset(6);
        assertEquals(1, text.knownLineCount());
        text.processLinesToOffset(7);
        assertEquals(2, text.knownLineCount());
        
        // The line is recognized one character earlier because once it see '\r' it check next for the '\n'
        text.processLinesToOffset(13);
        assertEquals(2, text.knownLineCount());
        text.processLinesToOffset(14);
        assertEquals(2, text.knownLineCount());
        text.processLinesToOffset(15);
        assertEquals(3, text.knownLineCount());
        
        // The line is recognized one character earlier because once it see '\r' it check next for the '\n'
        text.processLinesToOffset(20);
        assertEquals(3, text.knownLineCount());
        text.processLinesToOffset(21);
        assertEquals(3, text.knownLineCount());
        text.processLinesToOffset(22);
        assertEquals(4, text.knownLineCount());
        
        // The line is recognized one character earlier because once it see '\r' it check next for the '\n'
        text.processLinesToOffset(27);
        assertEquals(4, text.knownLineCount());
        text.processLinesToOffset(28);
        assertEquals(4, text.knownLineCount());
        text.processLinesToOffset(29);
        assertEquals(4, text.knownLineCount());
    }
    
    //== Mix == 
    
    @Test
    void testBasic_mix() {
        var content = "first\nsecond\rthird\r\nforth\r\n";
        var text    = new Code(content);
        assertEquals(5,                 text.lineCount());
        assertEquals(4,                 text.knownLineCount());
        assertEquals("[5, 12, 19, 26]", text.newlineOffsets().toString());
        assertEquals(
                "["
                + "first, "
                + "second, "
                + "third, "
                + "forth, "
                + "]",
                text.lines().toString());
    }
    
    @Test
    void testBasic_mix_ensureOffset() {
        var content = "first\nsecond\rthird\r\nforth\r\n";
        var text    = new Code(content);
        
        assertEquals(0, text.knownLineCount());
        
        text.processLinesToOffset(3);
        assertEquals(1, text.knownLineCount());
        
        text.processLinesToOffset(5);
        assertEquals(1, text.knownLineCount());
        text.processLinesToOffset(6);
        assertEquals(2, text.knownLineCount());
        
        text.processLinesToOffset(12);
        assertEquals(2, text.knownLineCount());
        text.processLinesToOffset(13);
        assertEquals(3, text.knownLineCount());
        
        text.processLinesToOffset(18);
        assertEquals(3, text.knownLineCount());
        text.processLinesToOffset(19);
        assertEquals(3, text.knownLineCount());
        text.processLinesToOffset(20);
        assertEquals(4, text.knownLineCount());
        
        text.processLinesToOffset(25);
        assertEquals(4, text.knownLineCount());
        text.processLinesToOffset(26);
        assertEquals(4, text.knownLineCount());
        text.processLinesToOffset(27);
        assertEquals(4, text.knownLineCount());
    }
    
    @Test
    void test_mix_offsets_noTail() {
        var content = "first\nsecond\rthird\r\nforth";
        var code    = new Code(content);
        assertEquals("[5, 12, 19]", code.newlineOffsets().toString());
        
        assertEquals(
                "java.lang.IndexOutOfBoundsException: "
                    + "Line number must be greater than or equal to 0: "
                        + "lineNumber=-1",
                "" + assertThrows(IndexOutOfBoundsException.class, () -> code.startOffset(-1)));
        assertEquals(
                "java.lang.IndexOutOfBoundsException: "
                    + "Line number must be greater than or equal to 0: "
                        + "lineNumber=-1",
                "" + assertThrows(IndexOutOfBoundsException.class, () -> code.endOffset(-1)));
        
        assertEquals(0, code.startOffset(0));
        assertEquals(5, code.endOffset(0));
        
        assertEquals(6,  code.startOffset(1));
        assertEquals(12, code.endOffset(1));
        
        assertEquals(13, code.startOffset(2));
        assertEquals(18, code.endOffset(2));
        
        assertEquals(20, code.startOffset(3));
        assertEquals(25, code.endOffset(3));
        
        assertEquals(
                "java.lang.IndexOutOfBoundsException: "
                    + "Line number must be less than the line count: "
                        + "lineNumber=4, "
                        + "lineCount=4",
                "" + assertThrows(IndexOutOfBoundsException.class, () -> code.startOffset(4)));
        assertEquals(
                "java.lang.IndexOutOfBoundsException: "
                    + "Line number must be less than the line count: "
                        + "lineNumber=4, "
                        + "lineCount=4",
                "" + assertThrows(IndexOutOfBoundsException.class, () -> code.endOffset(4)));
    }
    
    @Test
    void test_mix_offsets_withTail() {
        var content = "first line\nsecond line\rthird line\r\n";
        var code    = new Code(content);
        assertEquals("[10, 22, 34]", code.newlineOffsets().toString());
        
        assertEquals(
                "java.lang.IndexOutOfBoundsException: "
                    + "Line number must be greater than or equal to 0: "
                        + "lineNumber=-1",
                "" + assertThrows(IndexOutOfBoundsException.class, () -> code.startOffset(-1)));
        assertEquals(
                "java.lang.IndexOutOfBoundsException: "
                    + "Line number must be greater than or equal to 0: "
                        + "lineNumber=-1",
                "" + assertThrows(IndexOutOfBoundsException.class, () -> code.endOffset(-1)));
        
        assertEquals(0,  code.startOffset(0));
        assertEquals(10, code.endOffset(0));
        
        assertEquals(11, code.startOffset(1));
        assertEquals(22, code.endOffset(1));
        
        assertEquals(23, code.startOffset(2));
        assertEquals(33, code.endOffset(2));
        
        assertEquals(35, code.startOffset(3));
        assertEquals(35, code.endOffset(3));
        
        assertEquals(
                "java.lang.IndexOutOfBoundsException: "
                    + "Line number must be less than the line count: "
                        + "lineNumber=4, "
                        + "lineCount=4",
                "" + assertThrows(IndexOutOfBoundsException.class, () -> code.startOffset(4)));
        assertEquals(
                "java.lang.IndexOutOfBoundsException: "
                    + "Line number must be less than the line count: "
                        + "lineNumber=4, "
                        + "lineCount=4",
                "" + assertThrows(IndexOutOfBoundsException.class, () -> code.endOffset(4)
                ));
    }
    
    //== Long ==
    
    @Test
    void testLong() {
        int count   = 5;
        var newline = "\n";
        var textShort = new Code(infinite().limit(count).mapToString().reduce((s1, s2) -> s1 + newline + s2).get());
        assertEquals("0\n1\n2\n3\n4",   textShort.content().toString());
        assertEquals("[0, 1, 2, 3, 4]", textShort.lines() + "");
        assertEquals(5,                 textShort.lineCount());
        
        var newLines = listOf("\n", "\r", "\r\n");
        for (var newLine : newLines) {
            assertNewLines(1000, () -> newLine);
        }
        
        var randomize = new Random();
        assertNewLines(1000, () -> newLines.get(randomize.nextInt(3)));
    }
    
    private void assertNewLines(int count, Supplier<String> newline) {
        var content
                = infinite()
                .limit(count)
                .mapToString()
                .reduce((s1, s2) -> s1 + newline.get() + s2)
                .get();
        var textLong = new Code(content);
        assertEquals(1000, textLong.lineCount());
        
        var ints = textLong.lines().mapToInt(Integer::parseInt);
        assertEquals(0,   ints.first().getAsInt());
        assertEquals(999, ints.last().getAsInt());
    }
    
    //== LineOff ==
    
    @Test
    void testLineOf() {
        var content = "first\nsecond\nthird\nforth";
        var text    = new Code(content);
        var logs    = new StringBuilder();
        for (int offset = 0; offset < content.length(); offset++) {
            var charAt     = content.charAt(offset);
            var eachChar   = (charAt == '\n') ? "\\n" : (charAt == '\r') ? "\\r" : charAt;
            int lineNumber = text.lineNumberAtOffset(offset);
            var lineAt     = text.lineAtOffset(offset);
            logs.append(offset)
                .append(": ")
                .append(eachChar)
                .append(" - ")
                .append(lineNumber)
                .append(": ")
                .append(lineAt)
                .append("\n");
        }
        var expected = """
                0: f - 0: first
                1: i - 0: first
                2: r - 0: first
                3: s - 0: first
                4: t - 0: first
                5: \\n - 0: first
                6: s - 1: second
                7: e - 1: second
                8: c - 1: second
                9: o - 1: second
                10: n - 1: second
                11: d - 1: second
                12: \\n - 1: second
                13: t - 2: third
                14: h - 2: third
                15: i - 2: third
                16: r - 2: third
                17: d - 2: third
                18: \\n - 2: third
                19: f - 3: forth
                20: o - 3: forth
                21: r - 3: forth
                22: t - 3: forth
                23: h - 3: forth
                """;
        assertEquals(expected, logs.toString());
    }
    
    @Test
    void testLineOf_windows() {
        var content = "first\r\nsecond\r\nthird\r\nforth";
        var text    = new Code(content);
        var logs    = new StringBuilder();
        for (int offset = 0; offset < content.length(); offset++) {
            var charAt     = content.charAt(offset);
            var eachChar   = (charAt == '\n') ? "\\n" : (charAt == '\r') ? "\\r" : charAt;
            int lineNumber = text.lineNumberAtOffset(offset);
            var lineAt     = text.lineAtOffset(offset);
            logs.append(offset)
                .append(": ")
                .append(eachChar)
                .append(" - ")
                .append(lineNumber)
                .append(": ")
                .append(lineAt)
                .append("\n");
        }
        var expected = """
                0: f - 0: first
                1: i - 0: first
                2: r - 0: first
                3: s - 0: first
                4: t - 0: first
                5: \\r - 0: first
                6: \\n - 0: first
                7: s - 1: second
                8: e - 1: second
                9: c - 1: second
                10: o - 1: second
                11: n - 1: second
                12: d - 1: second
                13: \\r - 1: second
                14: \\n - 1: second
                15: t - 2: third
                16: h - 2: third
                17: i - 2: third
                18: r - 2: third
                19: d - 2: third
                20: \\r - 2: third
                21: \\n - 2: third
                22: f - 3: forth
                23: o - 3: forth
                24: r - 3: forth
                25: t - 3: forth
                26: h - 3: forth
                """;
        assertEquals(expected, logs.toString());
    }
    
    //== LineOff ==
    
    @Test
    void testLocationOf_singleLine() {
        var content = "first\r\n\tsecond\r\n  third\r\nforth";
        var text    = new Code(content);
        var segment = new SegmentPlainTextFormatter(text);
        var logs    = new StringBuilder();
        assertEquals("[6, 15, 24]", text.newlineOffsets().toString());
        
        for (int offset = 0; offset < text.length(); offset++) {
            var location = segment.byOffset(offset);
            logs
                .append("Offset: ")
                .append(offset)
                .append("\n")
                .append(location)
                .append("\n")
                .append("\n");
        }
        var expected = """
                Offset: 0
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                
                
                Offset: 1
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                
                
                Offset: 2
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                
                
                Offset: 3
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                
                
                Offset: 4
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                
                
                Offset: 5
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                
                
                Offset: 6
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                
                
                Offset: 7
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                
                
                Offset: 8
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                
                
                Offset: 9
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                
                
                Offset: 10
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                
                
                Offset: 11
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                
                
                Offset: 12
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                
                
                Offset: 13
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                
                
                Offset: 14
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                
                
                Offset: 15
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                
                
                Offset: 16
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 17
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 18
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 19
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 20
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 21
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 22
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 23
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 24
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 25
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  3 |  third
                  4 |forth
                
                
                Offset: 26
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  3 |  third
                  4 |forth
                
                
                Offset: 27
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  3 |  third
                  4 |forth
                
                
                Offset: 28
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  3 |  third
                  4 |forth
                
                
                Offset: 29
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  3 |  third
                  4 |forth
                
                
                """;
        assertEquals(expected, logs.toString());
    }
    
    @Test
    void testLocationOf_multipleLine() {
        var content = "first\r\n\tsecond\r\n  third\r\nforth";
        var text    = new Code(content);
        var segment = new SegmentPlainTextFormatter(text);
        var logs    = new StringBuilder();
        assertEquals("[6, 15, 24]", text.newlineOffsets().toString());
        
        for (int offset = 0; offset < text.length(); offset++) {
            var location = segment.byOffsets(offset, offset + 11);
            logs
                .append("Offset: ")
                .append(offset)
                .append("\n")
                .append(location)
                .append("\n")
                .append("\n");
        }
        
        var expected = """
                Offset: 0
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                
                
                Offset: 1
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                
                
                Offset: 2
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                
                
                Offset: 3
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                
                
                Offset: 4
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                
                
                Offset: 5
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 6
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 7
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 8
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 9
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 10
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 11
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 12
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 13
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 14
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 15
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  1 |first
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 16
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 17
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 18
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 19
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 20
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 21
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 22
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 23
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 24
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  2 |    second
                  3 |  third
                  4 |forth
                
                
                Offset: 25
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  3 |  third
                  4 |forth
                
                
                Offset: 26
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  3 |  third
                  4 |forth
                
                
                Offset: 27
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  3 |  third
                  4 |forth
                
                
                Offset: 28
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  3 |  third
                  4 |forth
                
                
                Offset: 29
                    |        10        20        30        40        50        60        70        80
                ----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|----+----|
                  3 |  third
                  4 |forth
                
                
                """;
        assertEquals(expected, logs.toString());
    }
    
    //== Line ==
    
    @Test
    void testLine() {
        var content = "first\nsecond\rthird\r\nforth\r\n";
        var text    = new Code(content);
        assertEquals(
                "["
                + "first, "
                + "second, "
                + "third, "
                + "forth, "
                + "]",
                IntFuncList.range(0, text.lineCount()).mapToObj(text::line).toString());
    }
    
    @Test
    void testLineLn() {
        var content = "first\nsecond\rthird\r\nforth\r\n";
        var text    = new Code(content);
        assertEquals(
                "["
                + "first\\n, "
                + "second\\r, "
                + "third\\r\\n, "
                + "forth\\r\\n, "
                + "]",
                range(0, text.lineCount())
                .mapToObj(text::lineLn)
                .map(theString
                        .replaceAll("\r", "\\\\r")
                        .replaceAll("\n", "\\\\n"))
                .toString());
        assertEquals(
                "["
                + "first\\n, "
                + "second\\r, "
                + "third\\r\\n, "
                + "forth\\r\\n, "
                + "]",
                text.lineLns()
                .map(theString
                        .replaceAll("\r", "\\\\r")
                        .replaceAll("\n", "\\\\n"))
                .toString());
    }
    
}
