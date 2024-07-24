package net.nawaman.textj.text;

import functionalj.lens.core.LensSpec;
import functionalj.lens.lenses.IntegerLens;
import functionalj.lens.lenses.ObjectLensImpl;

public class Part extends ImmutableCharSequence {
    
    private final ImmutableCharSequence full;
    private final int                   start;
    private final int                   end;
    
    public Part(ImmutableCharSequence full, int start, int end) {
        this.full = (full == null) ? Str.empty : full;
        
        int fullLength = this.full.length();
        if (start < 0) {
            var message = "'start' offset cannot be negative: start=%d".formatted(start);
            throw new IllegalArgumentException(message);
        }
        if (start > fullLength) {
            var message = "'start' offset cannot be larger than the original length: start=%d, length=%d".formatted(start, fullLength);
            throw new IllegalArgumentException(message);
        }
        if (end < start) {
            var message = "'end' offset cannot be lesser than the 'start' offset: start=%d, end=%d".formatted(start, end);
            throw new IllegalArgumentException(message);
        }
        if (end > fullLength) {
            var message = "'end' offset cannot be larger than the original length: end=%d, length=%d".formatted(end, fullLength);
            throw new IllegalArgumentException(message);
        }
        
        this.start = start;
        this.end   = end;
    }
    
    @Override
    public char charAt(int index) {
        if (index < 0) {
            var message = "'index' offset cannot be negative: index=%d".formatted(index);
            throw new IndexOutOfBoundsException(message);
        }
        if (index >= length()) {
            var message = "'index' offset cannot be larger than the length: index=%d, length=%d".formatted(index, length());
            throw new IndexOutOfBoundsException(message);
        }
        
        return full.charAt(start + index);
    }
    
    @Override
    public int length() {
        return end - start;
    }
    
    @Override
    public Part subSequence(int start, int end) {
        return new Part(full, this.start + start, this.start + end);
    }
    
    @Override
    public String toString() {
        return full.toString().substring(start, end);
    }
    
    //== Functional Choice ==
    
    public static final Part.PartLens<Part> thePart = new Part.PartLens<>("thePart", LensSpec.of(Part.class));
    public static final Part.PartLens<Part> eachPart = thePart;
    
    public ImmutableCharSequence full() {
        return full;
    }
    
    public int start() {
        return start;
    }
    public int end() {
        return end;
    }
    
    public Part withFull(ImmutableCharSequence full) { return new Part(full, start, end); }
    public Part withStart(int start) { return new Part(full, start, end); }
    public Part withEnd(int end) { return new Part(full, start, end); }
    public static class PartLens<HOST> extends ObjectLensImpl<HOST, Part> {
        
        public final ImmutableCharSequence.ImmutableCharSequenceLens<HOST> full = (ImmutableCharSequence.ImmutableCharSequenceLens<HOST>)createSubLens("full", Part::full, Part::withFull, ImmutableCharSequence.ImmutableCharSequenceLens::new);
        public final IntegerLens<HOST> start = createSubLensInt("start", Part::start, Part::withStart);
        public final IntegerLens<HOST> end = createSubLensInt("end", Part::end, Part::withEnd);
        
        public PartLens(String name, LensSpec<HOST, Part> spec) {
            super(name, spec);
        }
        
    }
    public java.util.Map<String, Object> __toMap() {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("__tagged", $utils.toMapValueObject("Part"));
        map.put("full", this.full);
        map.put("start", this.start);
        map.put("end", this.end);
        return map;
    }
    static private functionalj.map.FuncMap<String, functionalj.types.choice.generator.model.CaseParam> __schema__ = functionalj.map.FuncMap.<String, functionalj.types.choice.generator.model.CaseParam>newMap()
        .with("full", new functionalj.types.choice.generator.model.CaseParam("full", new functionalj.types.Type("net.nawaman.textj.formatter", null, "ImmutableCharSequence", java.util.Collections.emptyList()), false, null))
        .with("start", new functionalj.types.choice.generator.model.CaseParam("start", new functionalj.types.Type(null, null, "int", java.util.Collections.emptyList()), false, null))
        .with("end", new functionalj.types.choice.generator.model.CaseParam("end", new functionalj.types.Type(null, null, "int", java.util.Collections.emptyList()), false, null))
        .build();
    public static java.util.Map<String, functionalj.types.choice.generator.model.CaseParam> getCaseSchema() {
        return __schema__;
    }
    public static Part caseFromMap(java.util.Map<String, ? extends Object> map) {
        return Part(
                (ImmutableCharSequence)$utils.extractPropertyFromMap(Part.class, ImmutableCharSequence.class, map, __schema__, "full"),
                (int)$utils.extractPropertyFromMap(Part.class, int.class, map, __schema__, "start"),
                (int)$utils.extractPropertyFromMap(Part.class, int.class, map, __schema__, "end")
        );
    }
    
}
