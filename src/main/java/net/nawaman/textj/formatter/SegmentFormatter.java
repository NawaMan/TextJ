package net.nawaman.textj.formatter;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.List;

import functionalj.list.FuncList;
import net.nawaman.textj.Text;

/**
 * A text segment formatter.
 */
public abstract class SegmentFormatter {
    
    protected final Text   text;
    protected final String content;
    protected final int    tabSize;
    
    /** Constructs a new SegmentFormatter */
    public SegmentFormatter(Text text) {
        this.text    = text;
        this.content = text.content();
        this.tabSize = text.tabSize();
    }
    
    /** @return  the text this segment */
    public final Text text() {
        return text;
    }
    
    /** @return the content of the text. */
    public final String content() {
        return content;
    }
    
    /** @return the tab size of the text. */
    public final int tabSize() {
        return tabSize;
    }
    
    /** @return  the start offset of this segment. */
    public final CharSequence byOffset(int startOffset, HighLight ... highlights) {
        var list = FuncList.from(highlights);
        return byOffset(startOffset, list);
    }
    
    /** @return  the start offset of this segment. */
    public final CharSequence byOffset(int startOffset, List<HighLight> highlights) {
        return byOffsets(startOffset, startOffset, highlights);
    }
    
    /** @return  the segment from the start offset to the end offset. */
    public final CharSequence byOffsets(int startOffset, int endOffset, HighLight ... highlights) {
        var list = FuncList.from(highlights);
        return byOffsets(startOffset, endOffset, list);
    }
    
    /** @return  the segment from the start offset to the end offset. */
    public final CharSequence byOffsets(int startOffset, int endOffset, List<HighLight> highlights) {
        if (startOffset < 0)
            startOffset = 0;
        if (endOffset < 0)
            endOffset = startOffset;
        if (endOffset < startOffset) {
            var temp = startOffset;
            startOffset = endOffset;
            endOffset = temp;
        }
        
        var text = text();
        var lastLine  = max(0, text.lineNumberAtOffset(endOffset) + 1);
        text.processLinesToOffset(lastLine);
        text.processToLineCount(text.knownLineCount() + 1);
        
        var firstLine = max(0, text.lineNumberAtOffset(startOffset) - 1);
        lastLine      = min(lastLine, text.knownLineCount());
        
        return byLines(firstLine, lastLine, highlights);
    }
    
    /** @return  the segment from the start offset to the end offset. */
    public final CharSequence byLine(int firstLine, int lastLine) {
        if (firstLine < 0)
            firstLine = 0;
        if (lastLine < 0)
            lastLine = firstLine;
        if (lastLine < firstLine) {
            var temp = firstLine;
            firstLine = lastLine;
            lastLine = temp;
        }
        return byLines(firstLine, lastLine, FuncList.empty());
    }
    
    /** @return  the segment from the start offset to the end offset. */
    public final CharSequence byLines(int firstLine, int lastLine, HighLight ... highlights) {
        var list = FuncList.from(highlights);
        return byLines(firstLine, lastLine, list);
    }
    
    /** @return  the segment from the start offset to the end offset */
    public abstract CharSequence byLines(int firstLine, int lastLine, List<HighLight> highlights);
    
}
