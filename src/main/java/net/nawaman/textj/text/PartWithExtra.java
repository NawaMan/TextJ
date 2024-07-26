package net.nawaman.textj.text;

/** A {@link Part} with extra data. */
public final class PartWithExtra<EXTRA> extends Part implements TextWithExtra<EXTRA> {
    
    private final EXTRA extra;
    
    public PartWithExtra(Text full, int start, int end, EXTRA extra) {
        super(full, start, end);
        this.extra = extra;
    }
    
    @Override
    public EXTRA extra() {
        return extra;
    }
    
}
