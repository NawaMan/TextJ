package net.nawaman.textj.code;

import static functionalj.list.intlist.IntFuncList.infinite;
import static java.lang.Math.abs;

import functionalj.list.FuncList;
import functionalj.list.intlist.IntFuncList;
import net.nawaman.textj.internal.SortedAbsoluteIntArray;

/**
 * This class represents a specification for code.
 * 
 * This class processes the code in the line, column as well as the offset.
 * The processing of the line is done lazily.
 * The intention is that most of the code should not need to be processed.
 * 
 * Note: All index and offset are 0-based.
 * 
 * Note: To account for the two character new line (i.e. '\r\n'), the new line offset is stored as negative.
 * 
 * Note: Although once processed, the processing might not be the most optimize,
 *         the code only be processed when needed.
 *       And the space used is optimized. (as much as I could)
 */
public class Code {
    
    private final String content;
    
    private final SortedAbsoluteIntArray newLines = new SortedAbsoluteIntArray();
    private int processedOffset         = 0;
    private int previousProcessedOffset = 0;
    
    
    /**
     * Constructs a new instance of code with the given content and tab size.
     * 
     * If the tab size is less than 0, it will be set to the default tab size.
     * If the tab size is 0, it will be set to 1.
     * 
     * @param content  the content of the code with new lines.
     * @param tabSize  the tab size.
     */
    public Code(String content) {
        this.content = (content == null) ? "" : content;
    }
    
    /** @return  the content of the code. */
    public final String content() {
        return content;
    }
    
    /** @return  the length of the content. */
    public final int length() {
        return content.length();
    }
    
    //== Terminate ==
    
    /** @return  the number of lines in the content. */
    public final int lineCount() {
        processAllLines();
        int newlineCount = knownLineCount();
        return newlineCount + 1;
    }
    
    /**
     * Ensures that all the lines are processed.
     */
    public void processAllLines() {
        processToLineCount(Integer.MAX_VALUE);
    }
    
    //== Step ==
    
    /**
     * Ensures that the code is processed up to the given offset.
     * If the offset is beyond the length of the content, it will process up to the end of the content.
     *
     * @param offset  the offset up to which the code should be processed.
     */
    public final void processLinesToOffset(int offset) {
        processLinesUpTo(offset, Integer.MAX_VALUE);
    }
    
    /**
     * Ensures that the code is processed up to the given line count.
     * If the line count is beyond the number of lines in the content, it will process all lines.
     *
     * @param lineCount  the number of lines up to which the code should be processed.
     */
    public final void processToLineCount(int lineCount) {
        processLinesUpTo(Integer.MAX_VALUE, lineCount);
    }
    
    private void processLinesUpTo(int offset, int lines) {
        var lineCount = knownLineCount();
        var content   = content();
        int length    = content.length();
        offset = Math.min(offset, length);
        for ( ; (previousProcessedOffset < offset) && (lineCount < lines) && (processedOffset < length); processedOffset++) {
            var ch = content.charAt(processedOffset);
            if (ch == '\r') {
                if (((processedOffset + 1) < length) && (content.charAt(processedOffset + 1) == '\n')) {
                    processedOffset++;
                    addNewLine(-processedOffset);
                    lineCount++;
                } else {
                    addNewLine(processedOffset);
                    lineCount++;
                }
            } else if (ch == '\n') {
                addNewLine(processedOffset);
                lineCount++;
            }
        }
    }
    
    private void addNewLine(int newLineOffset) {
        newLines.add(newLineOffset);
        previousProcessedOffset = abs(newLineOffset);
    }
    
    /** @return  the currently known number of lines. */
    public final int knownLineCount() {
        int currentNewlineCount = newLines.length();
        return currentNewlineCount;
    }
    
    /** @return  the offset up to which the code is processed. */
    final int processedOffset() {
        return processedOffset;
    }
    
    /** @return  if all the line has been processed. */
    final boolean isProcessedAll() {
        return processedOffset >= content.length();
    }
    
    /** @return  a list of newline offsets. */
    public final IntFuncList newlineOffsets() {
        processAllLines();
        var lineCount = knownLineCount();
        return newLines.values()
                .map(Math::abs)
                .limit(lineCount);
    }
    
    /**
     * Returns the start offset of the line at the given line number.
     *
     * @param lineNumber  the line number.
     * @return            the start offset of the line at the given line number.
     */
    public final int startOffset(int lineNumber) {
        if (lineNumber < 0) {
            var message = "Line number must be greater than or equal to 0: lineNumber=" + lineNumber;
            throw new IndexOutOfBoundsException(message);
        }
        if (lineNumber == 0) {
            return 0;
        }
        if (isProcessedAll()) {
            int lineCount = lineCount();
            if (lineNumber >= lineCount) {
                var message 
                        = "Line number must be less than the line count: lineNumber=%s, lineCount=%s"
                        .formatted(lineNumber, lineCount);
                throw new IndexOutOfBoundsException(message);
            }
        }
        
        int end = rawEndOffset(lineNumber - 1);
        return ((end < 0) ? -end : end) + 1;
    }
    
    /**
     * Returns the end offset of the line at the given line number.
     *
     * @param lineNumber  the line number.
     * @return            the end offset of the line at the given line number.
     */
    public final int endOffset(int lineNumber) {
        int end = rawEndOffset(lineNumber);
        return (end < 0) ? (-end - 1) : end;
    }
    
    final int rawEndOffset(int lineNumber) {
        if (lineNumber < 0) {
            var message = "Line number must be greater than or equal to 0: lineNumber=" + lineNumber;
            throw new IndexOutOfBoundsException(message);
        }
        
        processToLineCount(lineNumber + 1);
        if (isProcessedAll()) {
            int lineCount = lineCount();
            if (lineNumber >= lineCount) {
                var message 
                        = "Line number must be less than the line count: lineNumber=%s, lineCount=%s"
                        .formatted(lineNumber, lineCount);
                throw new IndexOutOfBoundsException(message);
            }
        }
        
        int end          = content.length();
        int newlineCount = knownLineCount();
        if (lineNumber != newlineCount) {
            end = newLines.get(lineNumber);
        }
        return end;
    }
    
    /**
     * Returns the line at the given line number.
     *
     * @param lineNumber  the line number.
     * @return            the line at the given line number.
     */
    public final String line(int lineNumber) {
        var start   = startOffset(lineNumber);
        var end     = endOffset(lineNumber);
        var content = content();
        var line    = content.substring(start, end);
        return line;
    }
    
    /**
     * Returns the line at the given line number including the newline.
     * 
     * @param lineNumber  the line number.
     * @return            the line at the given line number including the newline.
     */
    public final String lineLn(int lineNumber) {
        int knownLineCount = knownLineCount();
        if (lineNumber < knownLineCount - 1) {
            processToLineCount(knownLineCount + 1);
        }
        
        var start   = startOffset(lineNumber);
        int end;
        if (lineNumber < knownLineCount - 1) {
            end = startOffset(lineNumber + 1);
        } else {
            end = rawEndOffset(lineNumber);
            if (end  < 0) {
                end = abs(end) + 1;
            }
        }
        var content = content();
        return content.substring(start, end);
    }
    
    /** @return  a stream of lines in the content. */
    public final FuncList<String> lines() {
        return infinite()
                .acceptUntil(index -> isProcessedAll() && (index > knownLineCount()))
                .mapToObj(this::line);
    }
    
    /** @return  a stream of lines with newline in the content. */
    public final FuncList<String> lineLns() {
        return infinite()
                .acceptUntil(index -> isProcessedAll() && (index > knownLineCount()))
                .mapToObj(this::lineLn);
    }
    
    /**
     * Returns the line number at the given offset.
     *
     * @param offset  the offset.
     * @return        the line number at the given offset.
     */
    public int lineNumberAtOffset(int offset) {
        processLinesToOffset(offset);
        if (newLines.length() == 0) {
            return 0;
        }
        if (offset >= length()) {
            return lineCount() - 1;
        }
        
        int index = newLines.indexOf(offset);
        return index;
    }
    
    /**
     * Returns the line at the given offset.
     *
     * @param offset  the offset.
     * @return        the line at the given offset.
     */
    public String lineAtOffset(int offset) {
        int lineNumber = lineNumberAtOffset(offset);
        var line       = line(lineNumber);
        return line;
    }
    
}
