package net.nawaman.textj.text;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import functionalj.lens.core.LensSpec;
import functionalj.lens.core.WriteLens;
import functionalj.lens.lenses.FuncListLens;
import functionalj.list.FuncList;
import functionalj.map.FuncMap;
import functionalj.types.Generic;
import functionalj.types.Type;
import functionalj.types.choice.generator.model.CaseParam;
import net.nawaman.textj.internal.SortedAbsoluteIntArray;

/**
 * A {@link Text} that are build from a sequence of {@link Text}.
 **/
public sealed class Sequence extends Text permits SequenceWithExtra {
    
    private final FuncList<Text>         sequence;
    private final SortedAbsoluteIntArray endOffsets;
    
    /** Construct a new Sequence. **/
    public Sequence(Text ... sequence) {
        this((sequence == null) ? FuncList.empty() : FuncList.of(sequence));
    }
    
    /** Construct a new Sequence. **/
    public Sequence(FuncList<Text> sequence) {
        this.sequence
            = (sequence == null)
            ? FuncList.empty()
            : sequence
                .filterNonNull()
                .filter(s -> s.length() > 0);
        
        this.endOffsets = new SortedAbsoluteIntArray();
        this.sequence
        .map(s -> s.length())
        .accumulate((prev, current) -> prev + current)
        .forEach(endOffsets::add);
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
        
        int subsequenceIndex = endOffsets.indexOf(index + 1);
        int offsetInSubsequence
            = (subsequenceIndex == 0)
            ? index
            : index - endOffsets.get(subsequenceIndex - 1);
        
        var sequenceItem = sequence.get(subsequenceIndex);
        var charAt       = sequenceItem.charAt(offsetInSubsequence);
        return charAt;
    }
    
    @Override
    public int length() {
        return endOffsets.isEmpty() ? 0 : endOffsets.get(endOffsets.length() - 1);
    }
    
    @Override
    public Part subSequence(int start, int end) {
        return new Part(this, start, end);
    }
    
    @Override
    public String toString() {
        return sequence.map(Text::toString).join();
    }
    
    //== Extra ==
    
    /** Returns the extra data of this {@link Sequence}. **/
    public <E> SequenceWithExtra<E> withExtra(E extra) {
        return new SequenceWithExtra<E>(sequence, extra);
    }
    
    //== Functional Choice ==
    
    /** Returns the lens for {@code Sequence} **/
    public static final Sequence.SequenceLens<Sequence> theSequence = new Sequence.SequenceLens<>("theSequence", LensSpec.of(Sequence.class));
    /** Returns the lens for {@code Sequence} **/
    public static final Sequence.SequenceLens<Sequence> eachSequence = theSequence;
    
    /** Returns the sequence content of this {@link Sequence} **/
    public FuncList<Text> sequence() {
        return sequence;
    }
    /** Returns a new {@link Sequence} with the given sequence content. **/
    public Sequence withSequence(FuncList<Text> sequence) {
        return new Sequence(sequence);
    }
    
    /** Returns a new {@link Sequence} with the given sequence content. */
    public static class SequenceLens<HOST> extends TextLens<HOST, Sequence> {
        
        /** The lens for the sequence content of the {@link Sequence}. */
        public final FuncListLens<HOST, Text, TextLens<HOST, Text>> sequence = createSubFuncListLens(
                (Function<Sequence, FuncList<Text>>)Sequence::sequence,
                (WriteLens<Sequence, FuncList<Text>>)Sequence::withSequence,
                (Function<LensSpec<HOST, Text>, TextLens<HOST, Text>>)(spec -> new Text.TextLens<HOST, Text>("sequence", spec)));
        
        /** Construct a new {@link SequenceLens}. */
        public SequenceLens(String name, LensSpec<HOST, Sequence> spec) {
            super(name, spec);
        }
    }
    
    /** Returns the {@link Sequence} as a map. **/
    public Map<String, Object> __toMap() {
        var map = new HashMap<String, Object>();
        map.put("__tagged", $utils.toMapValueObject("Sequence"));
        map.put("sequence", this.sequence);
        return map;
    }
    
    static private FuncMap<String, CaseParam> __schema__ = FuncMap.<String, CaseParam>newMap()
        .with("sequence", 
                new CaseParam("sequence", 
                    new Type("functionalj.list", null, "FuncList", 
                        asList(
                            new Generic(
                                Text.class.getCanonicalName(),
                                Text.class.getCanonicalName(),
                                asList(new Type(null, null, Text.class.getCanonicalName(), emptyList())
                            )
                        )
                    )
                ),
                false,
                null))
        .build();
    
    /** Returns the schema of this object. */
    public static Map<String, CaseParam> getCaseSchema() {
        return __schema__;
    }
    
    /** Constructs a new {@link Sequence} from the given map. */
    @SuppressWarnings("unchecked")
    public static Sequence caseFromMap(Map<String, ? extends Object> map) {
        return Sequence((FuncList<Text>)$utils.extractPropertyFromMap(Sequence.class, FuncList.class, map, __schema__, "sequence")
        );
    }
    
}
