package net.nawaman.codej.formatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.nawaman.codej.Code;

public class CodeSegmentFormatterTest {
    
    private Code         code         = new Code("first line\n second line\n third line\nforth line", 4);
    private StringBuffer byLinesCalls = new StringBuffer();
    
    private CodeSegmentFormatter formatter;
    
    @BeforeEach
    public void setUp() {
        formatter = new CodeSegmentFormatter(code) {
            @Override
            public CharSequence byLines(int firstLine, int lastLine, List<CodeHighLight> highlights) {
                byLinesCalls
                    .append("Call: firstLine: %d, lastLine: %d".formatted(firstLine, lastLine));
                
                if (!highlights.isEmpty()) {
                    byLinesCalls
                        .append(", highlights:")
                        .append(highlights.stream().map(h -> "  %s".formatted(h)).reduce("", (a, b) -> a + "\n" + b));
                }
                return "mocked byLines result";
            }
        };
    }
    
    @Test
    public void testCode() {
        assertEquals(code, formatter.code());
    }
    
    @Test
    public void testContent() {
        assertEquals("first line\n second line\n third line\nforth line", formatter.content());
    }
    
    @Test
    public void testTabSize() {
        assertEquals(4, formatter.tabSize());
    }
    
    @Test
    public void testByOffset() {
        formatter.byOffset(15, new CodeHighLight(0,  5, 0));
        
        var expected = """
                Call: firstLine: 0, lastLine: 2, highlights:
                  CodeHighLight[startOffset=0, endOffset=5, color=0]
                """;
        assertEquals(expected.trim(), byLinesCalls.toString());
    }
    
    @Test
    public void testByOffsets() {
        formatter.byOffsets(0, 10,
                new CodeHighLight(0,  5, 0),
                new CodeHighLight(6, 10, 1)
        );
        
        var expected = """
                Call: firstLine: 0, lastLine: 1, highlights:
                  CodeHighLight[startOffset=0, endOffset=5, color=0]
                  CodeHighLight[startOffset=6, endOffset=10, color=1]
                """;
        assertEquals(expected.trim(), byLinesCalls.toString());
    }
    
    @Test
    public void testByOffsets_negativeStartOffset() {
        formatter.byOffsets(-5, 20);
        
        var expected = "Call: firstLine: 0, lastLine: 2";   // The first line will be set to 0
        assertEquals(expected.trim(), byLinesCalls.toString());
    }
    
    @Test
    public void testByOffsets_negativeEndOffset() {
        formatter.byOffsets(5, -20);
        
        var expected = "Call: firstLine: 0, lastLine: 1";   // The last line will be set to the first line (but +1)
        assertEquals(expected.trim(), byLinesCalls.toString());
    }
    
    @Test
    public void testByOffsets_endOffsetLessThanFirstOffset() {
        formatter.byOffsets(20, 5);
        
        var expected = "Call: firstLine: 0, lastLine: 2";   // The first and last lines will be swapped.
        assertEquals(expected.trim(), byLinesCalls.toString());
    }
    
    @Test
    public void testByLine() {
        formatter.byLine(1, 3);
        
        var expected = "Call: firstLine: 1, lastLine: 3";
        assertEquals(expected.trim(), byLinesCalls.toString());
    }
    
    @Test
    public void testByLine_negativeFirstLine() {
        formatter.byLine(-1, 3);
        
        var expected = "Call: firstLine: 0, lastLine: 3";   // The first line will be set to 0
        assertEquals(expected.trim(), byLinesCalls.toString());
    }
    
    @Test
    public void testByLine_negativeLastLine() {
        formatter.byLine(1, -3);
        
        var expected = "Call: firstLine: 1, lastLine: 1";   // The last line will be set to the first line
        assertEquals(expected.trim(), byLinesCalls.toString());
    }
    
    @Test
    public void testByLine_lastLineLessThanFirstLine() {
        formatter.byLine(3, 1);
        
        var expected = "Call: firstLine: 1, lastLine: 3";   // The first and last lines will be swapped.
        assertEquals(expected.trim(), byLinesCalls.toString());
    }
    
    @Test
    public void testByLines() {
        formatter.byLines(1, 3, new CodeHighLight(0,  5, 0));
        
        var expected = """
                Call: firstLine: 1, lastLine: 3, highlights:
                  CodeHighLight[startOffset=0, endOffset=5, color=0]
                """;
        assertEquals(expected.trim(), byLinesCalls.toString());
    }
}
