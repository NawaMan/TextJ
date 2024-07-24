package net.nawaman.textj.code.formatter;

import static java.lang.Math.ceil;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.stream.IntStream.rangeClosed;
import static net.nawaman.textj.code.formatter.RulerOneLine.oneLineRuler;
import static net.nawaman.textj.code.formatter.RulerTwoLine.bottomTwoLineRuler;
import static net.nawaman.textj.code.formatter.RulerTwoLine.topTwoLineRuler;

import java.util.List;

import functionalj.function.Func2;
import net.nawaman.textj.code.Code;

/**
 * A segment formatter.which displays with VT100.
 */
public class SegmentPlainTextFormatter extends SegmentFormatter {
    
    private static final String SPACES = "                                                                            ";
    
    /** The segment creator. */
    public static final Func2<Code, Boolean, SegmentFormatter> segmentCreator 
                = (code, isOneLineRuler) -> new SegmentPlainTextFormatter(code, isOneLineRuler != Boolean.FALSE);
    
    /** The segment creator. */
    public static final Func2<Code, Boolean, SegmentFormatter> plainTextSegmentCreator = segmentCreator;
                
    private final RulerGenerator topRuler;
    private final RulerGenerator bottomRuler;
    
    public SegmentPlainTextFormatter(Code code) {
        this(code, false);
    }
    
    public SegmentPlainTextFormatter(Code code, boolean isOneLineRuler) {
        super(code);
        topRuler    = isOneLineRuler ? oneLineRuler : topTwoLineRuler;
        bottomRuler = isOneLineRuler ? oneLineRuler : bottomTwoLineRuler;
    }
    
    @Override
    public CharSequence byLines(int firstLine, int lastLine, List<HighLight> highlights) {
        code.processToLineCount(lastLine);
        lastLine = min(lastLine, code.knownLineCount());
        
        int maxColumn = maxColumn(firstLine, lastLine);
        
        var output = new StringBuilder();
        
        var prefix = "    |";
        topRuler.addRuler(output, prefix, maxColumn);
        
        output.append("\n");
        for (int i = firstLine; i <= lastLine; i++) {
            var lineNumber = " %2d |".formatted(i + 1); // 1-based index
            var codeLine   = code.line(i).replaceAll("\t", SPACES.substring(0, tabSize));
            
            output.append(lineNumber).append(codeLine).append("\n");
        }
        
        if ((lastLine - firstLine) >= 5) {
            bottomRuler.addRuler(output, prefix, maxColumn);
            output.append("\n");
        }
        return output;
    }
    
    private int maxColumn(int firstLine, int lastLine) {
        int maxColumn
                = rangeClosed(firstLine, lastLine)
                .map(lineNumber -> code.endOffset(lineNumber) - code.startOffset(lineNumber))
                .max()
                .orElse(80);
        maxColumn = (int) (ceil(max(maxColumn, 80) / 10.0) * 10);
        return maxColumn;
    }
    
}
