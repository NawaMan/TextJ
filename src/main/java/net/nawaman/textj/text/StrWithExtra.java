package net.nawaman.textj.text;

/** A {@link Str} with extra data. */
public final class StrWithExtra<EXTRA> extends Str implements TextWithExtra<EXTRA> {
    
    private final EXTRA extra;
    
    public StrWithExtra(String string, EXTRA extra) {
        super(string);
        this.extra = extra;
    }
    
    @Override
    public EXTRA extra() {
        return extra;
    }
    
}
