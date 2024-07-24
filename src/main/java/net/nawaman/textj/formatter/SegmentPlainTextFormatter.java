package net.nawaman.textj.formatter;

import static java.lang.Math.ceil;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.stream.IntStream.rangeClosed;
import static net.nawaman.textj.formatter.RulerOneLine.oneLineRuler;
import static net.nawaman.textj.formatter.RulerTwoLine.bottomTwoLineRuler;
import static net.nawaman.textj.formatter.RulerTwoLine.topTwoLineRuler;

import java.util.List;

import functionalj.function.Func2;
import net.nawaman.textj.Text;

/**
 * A segment formatter.which displays with VT100 text.
 */
public class SegmentPlainTextFormatter extends SegmentFormatter {
    
    /** The segment creator. */
    public static final Func2<Text, Boolean, SegmentFormatter> segmentCreator 
                = (text, isOneLineRuler) -> new SegmentPlainTextFormatter(text, isOneLineRuler != Boolean.FALSE);
    
    /** The segment creator. */
    public static final Func2<Text, Boolean, SegmentFormatter> plainTextSegmentCreator = segmentCreator;
                
    private final RulerGenerator topRuler;
    private final RulerGenerator bottomRuler;
    
    public SegmentPlainTextFormatter(Text text) {
        this(text, false);
    }
    
    public SegmentPlainTextFormatter(Text text, boolean isOneLineRuler) {
        super(text);
        topRuler    = isOneLineRuler ? oneLineRuler : topTwoLineRuler;
        bottomRuler = isOneLineRuler ? oneLineRuler : bottomTwoLineRuler;
    }
    
    @Override
    public CharSequence byLines(int firstLine, int lastLine, List<HighLight> highlights) {
        text.processToLineCount(lastLine);
        lastLine = min(lastLine, text.knownLineCount());
        
        int maxColumn = maxColumn(firstLine, lastLine);
        
        var output = new StringBuilder();
        
        var prefix = "    |";
        topRuler.addRuler(output, prefix, maxColumn);
        
        output.append("\n");
        for (int i = firstLine; i <= lastLine; i++) {
            var lineNumber = " %2d |".formatted(i + 1); // 1-based index
            var textLine   = text.line(i).replaceAll("\t", "                         ".substring(0, tabSize));
            
            output.append(lineNumber).append(textLine).append("\n");
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
                .map(lineNumber -> text.endOffset(lineNumber) - text.startOffset(lineNumber))
                .max()
                .orElse(80);
        maxColumn = (int) (ceil(max(maxColumn, 80) / 10.0) * 10);
        return maxColumn;
    }
    
}
