package net.nawaman.textj.text;

import static java.util.Collections.emptyList;

import java.util.Map;

import functionalj.lens.core.LensSpec;
import functionalj.lens.lenses.StringLens;
import functionalj.map.FuncMap;
import functionalj.types.Type;
import functionalj.types.choice.generator.model.CaseParam;

/**
 * A string text.
 **/
public final class Str extends Text {
    
    public static final Str.StrLens<Str> theStr = new Str.StrLens<>("theStr", LensSpec.of(Str.class));
    public static final Str.StrLens<Str> eachStr = theStr;
    
    public static Str empty = new Str(null);
    
    private final String string;
    
    /**
     * Construct a new Str.
     **/
    public Str(String string) {
        this.string = (string == null) ? "" : string;
    }
    
    @Override
    public char charAt(int index) {
        return string.charAt(index);
    }
    
    @Override
    public int length() {
        return string.length();
    }
    
    @Override
    public Text subSequence(int start, int end) {
        return new Part(this, start, end);
    }
    
    @Override
    public String toString() {
        return string;
    }
    
    //== Functional Choice ==
    
    /** Returns the string content of this {@link Str} **/
    public final String string() {
        return string;
    }
    
    /** Returns a new {@link Str} with the given string content. **/
    public final Str withString(String string) {
        return new Str(string);
    }
    
    /** Returns a new {@link Str} with the given string content. **/
    public static class StrLens<HOST> extends TextLens<HOST, Str> {
        
        public final StringLens<HOST> string = (StringLens<HOST>)createSubLens("string", Str::string, Str::withString, StringLens::of);
        
        public StrLens(String name, LensSpec<HOST, Str> spec) {
            super(name, spec);
        }
    }
    
    /** Returns the {@link Str} as a map. **/
    public Map<String, Object> __toMap() {
        var map = new java.util.HashMap<String, Object>();
        map.put("__tagged", $utils.toMapValueObject("Str"));
        map.put("string", this.string);
        return map;
    }
    
    static private FuncMap<String, CaseParam> __schema__ = FuncMap.<String, CaseParam>newMap()
        .with("string", new CaseParam("string", new Type("java.lang", null, "String", emptyList()), false, null))
        .build();
    
    /** Returns the schema of the @Str. **/
    public static Map<String, CaseParam> getCaseSchema() {
        return __schema__;
    }
    
    /** Creates a new {@link Str} from the given map. **/
    public static Str caseFromMap(java.util.Map<String, ? extends Object> map) {
        return Str((String)$utils.extractPropertyFromMap(Str.class, String.class, map, __schema__, "string")
        );
    }
    
}
