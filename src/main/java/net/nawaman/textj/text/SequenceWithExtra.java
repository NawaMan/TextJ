package net.nawaman.textj.text;

import functionalj.list.FuncList;

/** A {@link Sequence} with extra data. */
public final class SequenceWithExtra<EXTRA> extends Sequence implements TextWithExtra<EXTRA> {
    
    private final EXTRA extra;
    
    public SequenceWithExtra(FuncList<Text> sequence, EXTRA extra) {
        super(sequence);
        this.extra = extra;
    }
    
    @Override
    public EXTRA extra() {
        return extra;
    }
    
}