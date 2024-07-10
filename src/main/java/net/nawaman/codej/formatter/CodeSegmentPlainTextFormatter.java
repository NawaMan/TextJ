package net.nawaman.codej.formatter;

import static java.lang.Math.ceil;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.stream.IntStream.rangeClosed;
import static net.nawaman.codej.formatter.RulerOneLine.oneLineRuler;
import static net.nawaman.codej.formatter.RulerTwoLine.bottomTwoLineRuler;
import static net.nawaman.codej.formatter.RulerTwoLine.topTwoLineRuler;

import java.util.List;

import functionalj.function.Func2;
import net.nawaman.codej.Code;

/**
 * Code Segment which displays with VT100 code.
 */
public class CodeSegmentPlainTextFormatter extends CodeSegmentFormatter {
    
    public static final Func2<Code, Boolean, CodeSegmentFormatter> segmentCreator 
                = (code, isOneLineRuler) -> new CodeSegmentPlainTextFormatter(code, isOneLineRuler != Boolean.FALSE);
                
    public static final Func2<Code, Boolean, CodeSegmentFormatter> plainTextSegmentCreator = segmentCreator;
                
    private final RulerGenerator topRuler;
    private final RulerGenerator bottomRuler;
    
    public CodeSegmentPlainTextFormatter(Code code) {
        this(code, false);
    }
    
    public CodeSegmentPlainTextFormatter(Code code, boolean isOneLineRuler) {
        super(code);
        topRuler    = isOneLineRuler ? oneLineRuler : topTwoLineRuler;
        bottomRuler = isOneLineRuler ? oneLineRuler : bottomTwoLineRuler;
    }
    
    @Override
    public CharSequence byLines(int firstLine, int lastLine, List<CodeHighLight> highlights) {
        code.processToLineCount(lastLine);
        lastLine = min(lastLine, code.knownLineCount());
        
        int maxColumn = maxColumn(firstLine, lastLine);
        
        var output = new StringBuilder();
        
        var prefix = "    |";
        topRuler.addRuler(output, prefix, maxColumn);
        
        output.append("\n");
        for (int i = firstLine; i <= lastLine; i++) {
            if (i >= code.lineCount()) {
                break;
            }
            
            var lineNumber = " %2d |".formatted(i + 1); // 1-based index
            var codeLine   = code.line(i).replaceAll("\t", "                         ".substring(0, tabSize));
            
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
