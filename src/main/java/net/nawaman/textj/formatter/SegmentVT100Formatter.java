package net.nawaman.textj.formatter;

import static java.lang.Math.ceil;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.stream.IntStream.rangeClosed;
import static net.nawaman.textj.formatter.RulerOneLine.oneLineRuler;
import static net.nawaman.textj.formatter.RulerTwoLine.bottomTwoLineRuler;
import static net.nawaman.textj.formatter.RulerTwoLine.topTwoLineRuler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import functionalj.function.Func2;
import functionalj.list.FuncList;
import net.nawaman.textj.Text;

/**
 * A segment formatter.which displays with VT100 text.
 */
public class SegmentVT100Formatter extends SegmentFormatter {
    
    public static final Func2<Text, Boolean, SegmentFormatter> segmentCreator 
                = (text, isOneLineRuler) -> new SegmentVT100Formatter(text, isOneLineRuler != Boolean.FALSE);
                
    public static final Func2<Text, Boolean, SegmentFormatter> vt100SegmentCreator = segmentCreator;
    
    private static final String           VT100_HIGHLIGHT_END    = "\u001B[0m";
    private static final FuncList<String> VT100_HIGHLIGHT_STARTS = FuncList.of(
                "\u001B[41;1;3;37m",
                "\u001B[42;1;3;37m",
                "\u001B[44;1;3;37m",
                "\u001B[45;1;3;37m",
                "\u001B[46;1;3;37m",
                "\u001B[100;1;3;37m",
                "\u001B[43;1;3;37m",
                "\u001B[101;1;3;200m",
                "\u001B[102;1;3;200m",
                "\u001B[104;1;3;200m",
                "\u001B[105;1;3;200m",
                "\u001B[106;1;3;200m",
                "\u001B[47;1;3;200m",
                "\u001B[103;1;3;200m"
            );
    
    private final RulerGenerator topRuler;
    private final RulerGenerator bottomRuler;
    
    public SegmentVT100Formatter(Text text) {
        this(text, false);
    }
    
    public SegmentVT100Formatter(Text text, boolean isOneLineRuler) {
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
            var textLine   = text.lineLn(i);
            
            int lineStartOffset = text.startOffset(i);
            int lineEndOffset =   (i < text.knownLineCount() - 1) ? text.startOffset(i + 1) : text.endOffset(i);
            
            textLine = highLightLine(highlights, textLine, lineStartOffset, lineEndOffset);
            
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
    
    private String highLightLine(List<HighLight> textHighlights, String textLine, int lineStartOffset, int lineEndOffset) {
        var highlights = new ArrayList<>(textHighlights);
        highlights.sort(Comparator.<HighLight>comparingInt(h -> h.startOffset()).thenComparingInt(h -> h.endOffset()));
        
        var segments = new ArrayList<HighLight>();
        int currentPos = 0;
        
        for (int h = 0; h < highlights.size(); h++) {
            var highlight = highlights.get(h);
            int startOffset = max(highlight.startOffset() - lineStartOffset, 0);
            int endOffset   = min(highlight.endOffset()   - lineStartOffset, textLine.length());
            
            if (startOffset >= textLine.length() || endOffset <= 0) continue;
            
            if (currentPos < startOffset) {
                segments.add(new HighLight(currentPos, startOffset, -1)); // -1 for no highlight
            }
            segments.add(new HighLight(startOffset, endOffset, h));
            currentPos = Math.max(currentPos, endOffset);
        }
        
        if (currentPos < textLine.length()) {
            segments.add(new HighLight(currentPos, textLine.length(), -1));
        }
        
        var result = new StringBuilder();
        for (var segment : segments) {
            var part = textLine.substring(segment.startOffset(), segment.endOffset());
            if (segment.color() == -1) {
                result.append(highlightLineSegment(part));
            } else {
                result.append(highlightSegment(part, 0, part.length(), segment.color()));
            }
        }
        
        return result.toString();
    }
        
    private String highlightSegment(String textLine, int start, int end, int color) {
        return highlightLineSegment(textLine.substring(0, start))
            + VT100_HIGHLIGHT_STARTS.get(color % VT100_HIGHLIGHT_STARTS.size()) 
            + textLine
                .substring(start, end)
                .replaceAll(" ", "·")
                .replaceAll("\t", "—————————————————".substring(0, tabSize - 1) + "→")
                .replaceAll("\r", "↵")
                .replaceAll("\n", "¶")
            + VT100_HIGHLIGHT_END 
            + highlightLineSegment(textLine.substring(end));
    }
    
    private String highlightLineSegment(String textLine) {
        return textLine
                .replaceAll(" ",  "\u001B[38;2;200;200;200m·\u001B[0m")
                .replaceAll("\t", "\u001B[38;2;200;200;200m" + "—————————————————".substring(0, tabSize - 1) + "→\u001B[0m")
                .replaceAll("\r", "\u001B[38;2;200;200;200m↵\u001B[0m")
                .replaceAll("\n", "\u001B[38;2;200;200;200m¶\u001B[0m");
    }
    
}
