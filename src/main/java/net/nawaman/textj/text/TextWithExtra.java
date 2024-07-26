package net.nawaman.textj.text;

import functionalj.result.Result;

/** A text with extra data. */
public sealed interface TextWithExtra<EXTRA> extends CharSequence permits StrWithExtra, PartWithExtra, SequenceWithExtra {
    
    /** Returns if this text has extra data or not. */
    public default boolean hasExtra() {
        return true;
    }
    
    /** Returns the extra data. */
    public EXTRA extra();
    
    /** Returns the extra data as a result. */
    public default Result<EXTRA> getExtra() {
        var extra = extra();
        return Result.valueOf(extra);
    }
}
