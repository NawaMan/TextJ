package net.nawaman.textj.text;

import static java.util.Collections.emptyList;

import java.util.HashMap;
import java.util.Map;

import functionalj.lens.core.LensSpec;
import functionalj.lens.lenses.IntegerLens;
import functionalj.map.FuncMap;
import functionalj.types.Type;
import functionalj.types.choice.generator.model.CaseParam;

/** Text that point to part of a Text . */
public sealed class Part extends Text permits PartWithExtra {
    
    private final Text full;
    private final int  start;
    private final int  end;
    
    /** Construct a new Part. **/
    public Part(Text full, int start, int end) {
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
    
    //== Extra ==
    
    /** Returns the extra data of this {@link Part}. **/
    public <E> PartWithExtra<E> withExtra(E extra) {
        return new PartWithExtra<E>(full, start, end, extra);
    }
    
    //== Functional Choice ==
    
    /** Returns the lens for the {@link Part}. */
    public static final Part.PartLens<Part> thePart = new Part.PartLens<>("thePart", LensSpec.of(Part.class));
    
    /** Returns the lens for the {@link Part}. */
    public static final Part.PartLens<Part> eachPart = thePart;
    
    /** Returns the full text of this {@link Part}. **/
    public Text full() {
        return full;
    }
    
    /** Returns the start offset of this {@link Part}. **/
    public int start() {
        return start;
    }
    
    /** Returns the end offset of this {@link Part}. **/
    public int end() {
        return end;
    }
    
    /** Returns a new {@link Part} with the given full text. **/
    public Part withFull(Text full) {
        return new Part(full, start, end);
    }
    
    /** Returns a new {@link Part} with the given start offset. **/
    public Part withStart(int start) {
        return new Part(full, start, end);
    }
    
    /** Returns a new {@link Part} with the given end offset. **/
    public Part withEnd(int end) {
        return new Part(full, start, end);
    }
    
    /** Returns a new {@link Part} with the given full text. **/
    public static class PartLens<HOST> extends TextLens<HOST, Part> {
        
        /** The lens for the full text of the {@link Part}. */
        public final Text.TextLens<HOST, Text> full = (Text.TextLens<HOST, Text>)createSubLens("full", Part::full, Part::withFull, Text.TextLens::new);
        
        /** The lens for the start offset of the {@link Part}. */
        public final IntegerLens<HOST> start = createSubLensInt("start", Part::start, Part::withStart);
        
        /** The lens for the end offset of the {@link Part}. */
        public final IntegerLens<HOST> end = createSubLensInt("end", Part::end, Part::withEnd);
        
        /** Constructs a new {@link PartLens} with the given name and spec. */
        public PartLens(String name, LensSpec<HOST, Part> spec) {
            super(name, spec);
        }
        
    }
    
    /** @return  a map representation of this object. */
    public Map<String, Object> __toMap() {
        var map = new HashMap<String, Object>();
        map.put("__tagged", $utils.toMapValueObject("Part"));
        map.put("full",   this.full);
        map.put("start", this.start);
        map.put("end",   this.end);
        return map;
    }
    
    static private FuncMap<String, CaseParam> __schema__ = FuncMap.<String, CaseParam>newMap()
        .with("full",  new CaseParam("full",  new Type("net.nawaman.textj.text", null, "ImmutableCharSequence", emptyList()), false, null))
        .with("start", new CaseParam("start", new Type(null, null, "int", emptyList()), false, null))
        .with("end",   new CaseParam("end",   new Type(null, null, "int", emptyList()), false, null))
        .build();
    
    /** @return  the schema of the @Part. */
    public static Map<String, CaseParam> getCaseSchema() {
        return __schema__;
    }
    
    /** @return  a new {@link Part} from the given map. */
    public static Part caseFromMap(Map<String, ? extends Object> map) {
        return Part(
                (Text)$utils.extractPropertyFromMap(Part.class, Text.class, map, __schema__, "full"),
                (int)$utils.extractPropertyFromMap(Part.class, int.class, map, __schema__, "start"),
                (int)$utils.extractPropertyFromMap(Part.class, int.class, map, __schema__, "end")
        );
    }
    
}
