package net.nawaman.textj.code.formatter;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.List;

import functionalj.list.FuncList;
import net.nawaman.textj.code.Code;

/**
 * A code segment formatter.
 */
public abstract class SegmentFormatter {
    
    /** The default tab size. */
    public static final int DEFAULT_TAB_SIZE = 4;
    
    protected final Code   code;
    protected final String content;
    protected final int    tabSize;
    
    /** Constructs a new {@link SegmentFormatter} */
    public SegmentFormatter(Code code) {
        this(code, -1);
    }
    
    /** Constructs a new {@link SegmentFormatter} */
    public SegmentFormatter(Code code, int tabSize) {
        this.code    = (code == null) ? new Code("") : code;
        this.content = this.code.content().toString();
        this.tabSize = (tabSize < 0) ? DEFAULT_TAB_SIZE : max(1, tabSize);
    }
    
    /** @return  the code this segment */
    public final Code code() {
        return code;
    }
    
    /** @return the content of the code. */
    public final String content() {
        return content;
    }
    
    /** @return the tab size of the code. */
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
        
        var code = code();
        var lastLine  = max(0, code.lineNumberAtOffset(endOffset) + 1);
        code.processLinesToOffset(lastLine);
        code.processToLineCount(code.knownLineCount() + 1);
        
        var firstLine = max(0, code.lineNumberAtOffset(startOffset) - 1);
        lastLine      = min(lastLine, code.knownLineCount());
        
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
