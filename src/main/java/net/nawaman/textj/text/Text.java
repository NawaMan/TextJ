package net.nawaman.textj.text;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import functionalj.lens.core.LensSpec;
import functionalj.lens.core.WriteLens;
import functionalj.lens.lenses.BooleanAccess;
import functionalj.lens.lenses.BooleanAccessPrimitive;
import functionalj.lens.lenses.CharacterAccessPrimitive;
import functionalj.lens.lenses.IntegerAccess;
import functionalj.lens.lenses.ObjectLensImpl;
import functionalj.lens.lenses.ResultLens;
import functionalj.lens.lenses.StringAccess;
import functionalj.list.FuncList;
import functionalj.map.FuncMap;
import functionalj.pipeable.Pipeable;
import functionalj.result.Result;
import functionalj.types.choice.ChoiceTypeSwitch;
import functionalj.types.choice.IChoice;
import functionalj.types.choice.generator.model.CaseParam;

/**
 * Represents a text.
 **/
public abstract sealed class Text 
        implements CharSequence, IChoice<Text.TextFirstSwitch>, Pipeable<Text> 
        permits Str, Part, Sequence {
    
    /** Create a {@link Str} instance. */
    public static final Str Str(String string) {
        return new Str(string);
    }
    
    /** Create a {@link Part} instance. */
    public static final Part Part(Text full, int start, int end) {
        return new Part(full, start, end);
    }
    
    /** Create a {@link Sequence} instance. */
    public static final Sequence Sequence(FuncList<Text> sequence) {
        return new Sequence(sequence);
    }
    
    Text() {}
    
    
    //== Functional Choice ==
    // Due to the limitation of creating lens for FuncList of Self,
    //    the code is generate but copied here with manual modification.
    
    
    /** The lens for {@link Text} choice type. */
    public static final TextLens<Text, Text> theText = new TextLens<>("theText", LensSpec.of(Text.class));
    /** The lens for {@link Text} choice type. */
    public static final TextLens<Text, Text> eachText = theText;
    
    /**
     * The lens for this {@link Text} choice type.
     * 
     * @param <HOST>  the lens host type.
     */
    public static class TextLens<HOST, TEXT extends Text> extends ObjectLensImpl<HOST, TEXT> {
        /** The lens for the {@link Text#isStr()} method. */
        public final BooleanAccessPrimitive<Text> isStr = Text::isStr;
        /** The lens for the {@link Text#isPart()} method. */
        public final BooleanAccessPrimitive<Text> isPart = Text::isPart;
        /** The lens for the {@link Text#isSequence()} method. */
        public final BooleanAccessPrimitive<Text> isSequence = Text::isSequence;
        
        /** The lens for the {@link Text#hasExtra()} method. */
        public final BooleanAccessPrimitive<Text> hasExtra = Text::hasExtra;
        
        /** The lens for the {@link Text#charAt(int)} method. */
        public CharacterAccessPrimitive<HOST> charAt(int index) {
            return host -> {
                var strValue = apply(host);
                var charValue = strValue.charAt(index);
                return charValue;
            };
        }
        
        /** The lens for the {@link Text#length()} method. */
        public IntegerAccess<HOST> length() {
            return IntegerAccess.of(host -> {
                if (host == null)
                    return 0;
                
                var value = apply(host);
                if (value == null)
                    return 0;
                
                return value.length();
            });
        }
        
        /** The lens for the {@link Text#isEmpty()} method. */
        public BooleanAccess<HOST> isEmpty() {
            return BooleanAccess.of(host -> {
                if (host == null)
                    return true;
                var value = apply(host);
                if (value == null)
                    return true;
                return value.isEmpty();
            });
        }
        
        /** The lens for the {@link Text#subSequence(int,int)} method. */
        public StringAccess<HOST> subSequence(int start, int end) {
            return StringAccess.of(host -> {
                if (host == null)
                    return "";
                var value = apply(host);
                if (value == null)
                    return "";
                return value.subSequence(start, end).toString();
            });
        }
        
        /** The lens for the {@link Text#asStr()} method. */
        @SuppressWarnings("unchecked")
        public final ResultLens.Impl<HOST, Str, Str.StrLens<HOST>> asStr = createSubResultLens("asStr", (Function<TEXT, Result<Str>>)(h -> h.asStr()), (WriteLens<TEXT,Result<Str>>)(h,r)->(TEXT)(Object)r.get(), Str.StrLens::new);
        /** The lens for the {@link Text#asPart()} method. */
        @SuppressWarnings("unchecked")
        public final ResultLens.Impl<HOST, Part, Part.PartLens<HOST>> asPart = createSubResultLens("asPart", (Function<TEXT, Result<Part>>)(h -> h.asPart()), (WriteLens<TEXT,Result<Part>>)(h,r)->(TEXT)(Object)r.get(), Part.PartLens::new);
        @SuppressWarnings("unchecked")
        /** The lens for the {@link Text#asSequence()} method. */
        public final ResultLens.Impl<HOST, Sequence, Sequence.SequenceLens<HOST>> asSequence = createSubResultLens("asSequence", (Function<TEXT, Result<Sequence>>)(h -> h.asSequence()), (WriteLens<TEXT,Result<Sequence>>)(h,r)->(TEXT)(Object)r.get(), Sequence.SequenceLens::new);
        
        /** The lens constructor. */
        public TextLens(String name, LensSpec<HOST, TEXT> spec) {
            super(name, spec);
        }
    }
    
    /** Returns this text as a data. */
    public Text __data() throws Exception {
        return this;
    }
    /** Returns this text as a result. */
    public Result<Text> toResult() {
        return Result.valueOf(this);
    }
    
    /** Returns a text from a map. */
    @SuppressWarnings("unchecked")
    public static <T extends Text> T fromMap(Map<String, ? extends Object> map) {
        if (map == null)
            return null;
        var __tagged = (String)map.get("__tagged");
        if ("Str".equals(__tagged))
            return (T)Str.caseFromMap(map);
        if ("Part".equals(__tagged))
            return (T)Part.caseFromMap(map);
        if ("Sequence".equals(__tagged))
            return (T)Sequence.caseFromMap(map);
        throw new IllegalArgumentException("Tagged value does not represent a valid type: " + __tagged);
    }
    
    /** The schema (field information) of {@link Text}. */
    static private FuncMap<String, Map<String, CaseParam>> __schema__ = FuncMap.<String, Map<String, CaseParam>>newMap()
        .with("Str", Str.getCaseSchema())
        .with("Part", Part.getCaseSchema())
        .with("Sequence", Sequence.getCaseSchema())
        .build();
    
    /** Returns the schema (field information) of {@link Text}. */
    public static Map<String, Map<String, CaseParam>> getChoiceSchema() {
        return __schema__;
    }
    
    /** Returns the schema (field information) of this object. */
    public Map<String, Map<String, CaseParam>> __getSchema() {
        return getChoiceSchema();
    }
    
    private final transient TextFirstSwitch __switch = new TextFirstSwitch(this);
    
    /** Switch to a specific case. */
    @Override
    public TextFirstSwitch match() {
         return __switch;
    }
    
    @Override
    public abstract String toString();
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Text))
            return false;
        
        if (this == obj)
            return true;
        
        String objToString  = obj.toString();
        String thisToString = this.toString();
        return thisToString.equals(objToString);
    }
    
    //== Extra ==
    
    /** Returns <code>true</code> if this {@link Text} has extra data. */
    public boolean hasExtra() {
        return false;
    }
    
    /**
     * Run the action if it is an {@link Str}, then return this {@link Text} .
     * 
     * @param action the action to run if it is an {@link Str}.
     * @return this {@link Text}
     */
    public Text ifExtra(Runnable action) {
        if (hasExtra()) 
            action.run();
        return this;
    }
    
    /** Returns a new {@link Text} with the given extra data. */
    @SuppressWarnings("unchecked")
    public <EXTRA> Text ifExtra(Class<EXTRA> clazz, Consumer<EXTRA> action) {
        if (hasExtra()) 
            action.accept((EXTRA)this);
        return this;
    }
    
    /** Returns a new {@link Text} with the given extra data. */
    @SuppressWarnings("unchecked")
    public <EXTRA> Text ifExtra(Consumer<EXTRA> action) {
        if (hasExtra()) 
            action.accept((EXTRA)this);
        return this;
    }
    
    //== Prism methods ==
    
    /** Returns <code>true</code> if this {@link Text} is a {@link Str}. */
    public boolean isStr() {
        return this instanceof Str;
    }
    
    /** Returns {@link Text} is a {@link Str} if it is an {@link Str}. */
    public Result<Str> asStr() {
        return Result.valueOf(this).filter(Str.class).map(Str.class::cast);
        
    }
    
    /**
     * Run the action if it is an {@link Str}, then return this {@link Text} .
     * 
     * @param action  the action to run if it is an {@link Str}.
     * @return        this {@link Text}
     */
    public Text ifStr(Consumer<Str> action) {
        if (isStr()) 
            action.accept((Str)this);
        return this;
    }
    
    /**
     * Run the action if it is an {@link Str}, then return this {@link Text} .
     * 
     * @param action the action to run if it is an {@link Str}.
     * @return this {@link Text}
     */
    public Text ifStr(Runnable action) {
        if (isStr()) 
            action.run();
        return this;
    }
    
    /** Returns <code>true</code> if this {@link Text} is a {@link Part}. */
    public boolean isPart() {
        return this instanceof Part;
    }
    
    /** Returns {@link Text} is a {@link Part} if it is an {@link Part}. */
    public Result<Part> asPart() {
        return Result.valueOf(this)
                .filter(Part.class)
                .map(Part.class::cast);
    }
    
    /**
     * Run the action if it is an {@link Part}, then return this {@link Text} .
     * 
     * @param action the action to run if it is an {@link Part}.
     * @return this {@link Text}
     */
    public Text ifPart(Consumer<Part> action) {
        if (isPart())
            action.accept((Part)this);
        return this;
    }
    
    /**
     * Run the action if it is an {@link Part}, then return this {@link Text} .
     * 
     * @param action the action to run if it is an {@link Part}.
     * @return this {@link Text}
     */
    public Text ifPart(Runnable action) {
        if (isPart())
            action.run();
        return this;
    }
    
    /** Returns <code>true</code> if this {@link Text} is a {@link Sequence}. */
    public boolean isSequence() {
        return this instanceof Sequence;
    }
    
    /** Returns {@link Text} is a {@link Sequence} if it is an {@link Sequence}. */
    public Result<Sequence> asSequence() {
        return Result.valueOf(this)
                .filter(Sequence.class)
                .map(Sequence.class::cast);
    }
    
    /**
     * Run the action if it is an {@link Sequence}, then return this {@link Text} .
     * 
     * @param action the action to run if it is an {@link Sequence}.
     * @return this {@link Text}
     */
    public Text ifSequence(Consumer<Sequence> action) {
        if (isSequence())
            action.accept((Sequence)this);
        return this;
    }
    
    /**
     * Run the action if it is an {@link Sequence}, then return this {@link Text} .
     * 
     * @param action the action to run if it is an {@link Sequence}.
     * @return this {@link Text}
     */
    public Text ifSequence(Runnable action) {
        if (isSequence())
            action.run();
        return this;
    }
    
    /** Returns <code>true</code> if this {@link Text} is a {@link StrWithExtra}. */
    public boolean isStrWithExtra() {
        return this instanceof StrWithExtra;
    }
    
    /** Returns {@link Text} is a {@link StrWithExtra} if it is an {@link StrWithExtra}. */
    public <EXTRA> Result<StrWithExtra<EXTRA>> asStrWithExtra() {
        return Result.valueOf(this)
                .filter(StrWithExtra.class)
                .map(StrWithExtra.class::cast);
        
    }
    
    /**
     * Run the action if it is an {@link StrWithExtra}, then return this {@link Text} .
     * 
     * @param action  the action to run if it is an {@link StrWithExtra}.
     * @return        this {@link Text}
     */
    @SuppressWarnings("unchecked")
    public <EXTRA> Text ifStrWithExtra(Consumer<StrWithExtra<EXTRA>> action) {
        if (isStrWithExtra()) 
            action.accept((StrWithExtra<EXTRA>)this);
        return this;
    }
    
    /**
     * Run the action if it is an {@link StrWithExtra}, then return this {@link Text} .
     * 
     * @param action the action to run if it is an {@link StrWithExtra}.
     * @return this {@link Text}
     */
    public Text ifStrWithExtra(Runnable action) {
        if (isStrWithExtra()) 
            action.run();
        return this;
    }
    
    /** Returns <code>true</code> if this {@link Text} is a {@link PartWithExtra}. */
    public boolean isPartWithExtra() {
        return this instanceof Part;
    }
    
    /** Returns {@link Text} is a {@link PartWithExtra} if it is an {@link PartWithExtra}. */
    public <EXTRA> Result<PartWithExtra<EXTRA>> asPartWithExtra() {
        return Result.valueOf(this)
                .filter(PartWithExtra.class)
                .map(PartWithExtra.class::cast);
    }
    
    /**
     * Run the action if it is an {@link PartWithExtra}, then return this {@link Text} .
     * 
     * @param action the action to run if it is an {@link PartWithExtra}.
     * @return this {@link Text}
     */
    @SuppressWarnings("unchecked")
    public <EXTRA> Text ifPartWithExtra(Consumer<PartWithExtra<EXTRA>> action) {
        if (isPartWithExtra())
            action.accept((PartWithExtra<EXTRA>)this);
        return this;
    }
    
    /**
     * Run the action if it is an {@link Part}, then return this {@link Text} .
     * 
     * @param action the action to run if it is an {@link Part}.
     * @return this {@link Text}
     */
    public Text ifPartWithExtra(Runnable action) {
        if (isPartWithExtra())
            action.run();
        return this;
    }
    
    /** Returns <code>true</code> if this {@link Text} is a {@link Sequence}. */
    public boolean isSequenceWithExtra() {
        return this instanceof SequenceWithExtra;
    }
    
    /** Returns {@link Text} is a {@link SequenceWithExtra} if it is an {@link SequenceWithExtra}. */
    public <EXTRA> Result<SequenceWithExtra<EXTRA>> asSequenceWithExtra() {
        return Result.valueOf(this)
                .filter(SequenceWithExtra.class)
                .map(SequenceWithExtra.class::cast);
    }
    
    /**
     * Run the action if it is an {@link SequenceWithExtra}, then return this {@link Text} .
     * 
     * @param action the action to run if it is an {@link SequenceWithExtra}.
     * @return this {@link Text}
     */
    @SuppressWarnings("unchecked")
    public <EXTRA> Text ifSequenceWithExtra(Consumer<SequenceWithExtra<EXTRA>> action) {
        if (isSequenceWithExtra())
            action.accept((SequenceWithExtra<EXTRA>)this);
        return this;
    }
    
    /**
     * Run the action if it is an {@link SequenceWithExtra}, then return this {@link Text} .
     * 
     * @param action the action to run if it is an {@link SequenceWithExtra}.
     * @return this {@link Text}
     */
    public Text ifSequenceWithExtra(Runnable action) {
        if (isSequenceWithExtra())
            action.run();
        return this;
    }
    
    //== Switch methods ==
    
    /** Switch to a specific case. */
    public static class TextFirstSwitch {
        private Text $value;
        private TextFirstSwitch(Text theValue) {
            this.$value = theValue;
        }
        
        /** Switch to a specific case. */
        public <TARGET> TextFirstSwitchTyped<TARGET> toA(Class<TARGET> clzz) {
            return new TextFirstSwitchTyped<TARGET>($value);
        }
        
        /** Switch to a {@link Str}. */
        public <TARGET> TextSwitchPartSequence<TARGET> str(Function<? super Str, ? extends TARGET> theAction) {
            Function<Text, TARGET> $action = null;
            Function<Text, TARGET> oldAction = (Function<Text, TARGET>)$action;
            Function<Text, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    ($value instanceof Str)
                    ? (Function<Text, TARGET>)(d -> theAction.apply((Str)d))
                    : oldAction;
            
            return new TextSwitchPartSequence<TARGET>($value, newAction);
        }
        
        /** Switch to a {@link Str}. */
        public <TARGET> TextSwitchPartSequence<TARGET> str(Supplier<? extends TARGET> theSupplier) {
            return str(d->theSupplier.get());
        }
        /** Switch to a {@link Str}. */
        public <TARGET> TextSwitchPartSequence<TARGET> str(TARGET theValue) {
            return str(d->theValue);
        }
        
        /** Switch to a {@link Str}. */
        public <TARGET> TextSwitchStrPartSequence<TARGET> str(Predicate<Str> check, Function<? super Str, ? extends TARGET> theAction) {
            Function<Text, TARGET> $action = null;
            Function<Text, TARGET> oldAction = (Function<Text, TARGET>)$action;
            Function<Text, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    (($value instanceof Str) && check.test((Str)$value))
                    ? (Function<Text, TARGET>)(d -> theAction.apply((Str)d))
                    : oldAction;
            
            return new TextSwitchStrPartSequence<TARGET>($value, newAction);
        }
        /** Switch to a {@link Str}. */
        public <TARGET> TextSwitchStrPartSequence<TARGET> str(Predicate<Str> check, Supplier<? extends TARGET> theSupplier) {
            return str(check, d->theSupplier.get());
        }
        /** Switch to a {@link Str}. */
        public <TARGET> TextSwitchStrPartSequence<TARGET> str(Predicate<Str> check, TARGET theValue) {
            return str(check, d->theValue);
        }
    }
    
    /** Switch to a specific case which will result to a specific type. */
    public static class TextFirstSwitchTyped<TARGET> {
        private Text $value;
        private TextFirstSwitchTyped(Text theValue) { this.$value = theValue; }
        
        /** Switch to a {@link Str}. */
        public TextSwitchPartSequence<TARGET> str(Function<? super Str, ? extends TARGET> theAction) {
            Function<Text, TARGET> $action = null;
            Function<Text, TARGET> oldAction = (Function<Text, TARGET>)$action;
            Function<Text, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    ($value instanceof Str)
                    ? (Function<Text, TARGET>)(d -> theAction.apply((Str)d))
                    : oldAction;
            
            return new TextSwitchPartSequence<TARGET>($value, newAction);
        }
        
        /** Switch to a {@link Str}. */
        public TextSwitchPartSequence<TARGET> str(Supplier<? extends TARGET> theSupplier) {
            return str(d->theSupplier.get());
        }
        
        /** Switch to a {@link Str}. */
        public TextSwitchPartSequence<TARGET> str(TARGET theValue) {
            return str(d->theValue);
        }
        
        /** Switch to a {@link Str}. */
        public TextSwitchStrPartSequence<TARGET> str(Predicate<Str> check, Function<? super Str, ? extends TARGET> theAction) {
            Function<Text, TARGET> $action = null;
            Function<Text, TARGET> oldAction = (Function<Text, TARGET>)$action;
            Function<Text, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    (($value instanceof Str) && check.test((Str)$value))
                    ? (Function<Text, TARGET>)(d -> theAction.apply((Str)d))
                    : oldAction;
            
            return new TextSwitchStrPartSequence<TARGET>($value, newAction);
        }
        /** Switch to a {@link Str}. */
        public TextSwitchStrPartSequence<TARGET> str(Predicate<Str> check, Supplier<? extends TARGET> theSupplier) {
            return str(check, d->theSupplier.get());
        }
        /** Switch to a {@link Str}. */
        public TextSwitchStrPartSequence<TARGET> str(Predicate<Str> check, TARGET theValue) {
            return str(check, d->theValue);
        }
    }
    
    /** Switch for {@link Str} */
    public static class TextSwitchStrPartSequence<TARGET> extends ChoiceTypeSwitch<Text, TARGET> {
        private TextSwitchStrPartSequence(Text theValue, Function<Text, ? extends TARGET> theAction) { super(theValue, theAction); }
        
        /** Process the str and return the target result if the current {@link Text} is a {@link Str}. */
        public TextSwitchPartSequence<TARGET> str(Function<? super Str, ? extends TARGET> theAction) {
            @SuppressWarnings("unchecked")
            Function<Text, TARGET> oldAction = (Function<Text, TARGET>)$action;
            Function<Text, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    ($value instanceof Str)
                    ? (Function<Text, TARGET>)(d -> theAction.apply((Str)d))
                    : oldAction;
            
            return new TextSwitchPartSequence<TARGET>($value, newAction);
        }
        
        /** Returns the target from the supplier if the current {@link Text} is a {@link Str}. */
        public TextSwitchPartSequence<TARGET> str(Supplier<? extends TARGET> theSupplier) {
            return str(d->theSupplier.get());
        }
        
        /** Returns the target if the current {@link Text} is a {@link Str}. */
        public TextSwitchPartSequence<TARGET> str(TARGET theValue) {
            return str(d->theValue);
        }
        
        /**
         * Replace the value if this {@link Text} is str and match the given predicate.
         * 
         * @param check     the predicate to check the value.
         * @param theAction the function to replace the value if the predicate is
         *                  <code>true.
         * @return the switch object for str.
         */
        public TextSwitchStrPartSequence<TARGET> str(Predicate<Str> check, Function<? super Str, ? extends TARGET> theAction) {
            @SuppressWarnings("unchecked")
            Function<Text, TARGET> oldAction = (Function<Text, TARGET>)$action;
            Function<Text, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    (($value instanceof Str) && check.test((Str)$value))
                    ? (Function<Text, TARGET>)(d -> theAction.apply((Str)d))
                    : oldAction;
            
            return new TextSwitchStrPartSequence<TARGET>($value, newAction);
        }
        
        /**
         * Replace the value if this {@link Text} is str and match the given predicate.
         * 
         * @param check       the predicate to check the value.
         * @param theSupplier the supplier to replace the value if the predicate is <code>true</code>.
         * @return the switch object for str.
         */
        public TextSwitchStrPartSequence<TARGET> str(Predicate<Str> check, Supplier<? extends TARGET> theSupplier) {
            return str(check, d->theSupplier.get());
        }
        
        /**
         * Replace the value if this {@link Text} is str and match the given predicate.
         * 
         * @param check    the predicate to check the value.
         * @param theValue the value to replace if the predicate is <code>true</code>.
         * @return the switch object for str.
         */
        public TextSwitchStrPartSequence<TARGET> str(Predicate<Str> check, TARGET theValue) {
            return str(check, d->theValue);
        }
    }
    
    /** Switch for {@link Part} */
    public static class TextSwitchPartSequence<TARGET> extends ChoiceTypeSwitch<Text, TARGET> {
        private TextSwitchPartSequence(Text theValue, Function<Text, ? extends TARGET> theAction) { super(theValue, theAction); }
        
        /** Process the part and return the target result if the current {@link Text} is a {@link Part}. */
        public TextSwitchSequence<TARGET> part(Function<? super Part, ? extends TARGET> theAction) {
            @SuppressWarnings("unchecked")
            Function<Text, TARGET> oldAction = (Function<Text, TARGET>)$action;
            Function<Text, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    ($value instanceof Part)
                    ? (Function<Text, TARGET>)(d -> theAction.apply((Part)d))
                    : oldAction;
            
            return new TextSwitchSequence<TARGET>($value, newAction);
        }
        /** Returns the target from the supplier if the current {@link Text} is a {@link Part}. */
        public TextSwitchSequence<TARGET> part(Supplier<? extends TARGET> theSupplier) {
            return part(d->theSupplier.get());
        }
        
        /** Returns the target if the current {@link Text} is a {@link Part}. */
        public TextSwitchSequence<TARGET> part(TARGET theValue) {
            return part(d->theValue);
        }
        
        /**
         * Replace the value if this {@link Text} is part and match the given predicate.
         * 
         * @param check     the predicate to check the value.
         * @param theAction the function to replace the value if the predicate is <code>true.
         * @return the switch object for part.
         */
        public TextSwitchPartSequence<TARGET> part(Predicate<Part> check, Function<? super Part, ? extends TARGET> theAction) {
            @SuppressWarnings("unchecked")
            Function<Text, TARGET> oldAction = (Function<Text, TARGET>)$action;
            Function<Text, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    (($value instanceof Part) && check.test((Part)$value))
                    ? (Function<Text, TARGET>)(d -> theAction.apply((Part)d))
                    : oldAction;
            
            return new TextSwitchPartSequence<TARGET>($value, newAction);
        }
        
        /**
         * Replace the value if this {@link Text} is part and match the given predicate.
         * 
         * @param check       the predicate to check the value.
         * @param theSupplier the supplier to replace the value if the predicate is <code>true.
         * @return the switch object for part.
         */
        public TextSwitchPartSequence<TARGET> part(Predicate<Part> check, Supplier<? extends TARGET> theSupplier) {
            return part(check, d->theSupplier.get());
        }
        
        /**
         * Replace the value if this {@link Text} is part and match the given predicate.
         * 
         * @param check    the predicate to check the value.
         * @param theValue the value to replace if the predicate is <code>true</code>.
         * @return the switch object for part.
         */
        public TextSwitchPartSequence<TARGET> part(Predicate<Part> check, TARGET theValue) {
            return part(check, d->theValue);
        }
    }
    
    /** Switch for {@link Sequence}. */
    public static class TextSwitchSequence<TARGET> extends ChoiceTypeSwitch<Text, TARGET> {
        private TextSwitchSequence(Text theValue, Function<Text, ? extends TARGET> theAction) { super(theValue, theAction); }
        
        /** Process the sequence and return the target result. */
        public TARGET sequence(Function<? super Sequence, ? extends TARGET> theAction) {
            @SuppressWarnings("unchecked")
            Function<Text, TARGET> oldAction = (Function<Text, TARGET>)$action;
            Function<Text, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    ($value instanceof Sequence)
                    ? (Function<Text, TARGET>)(d -> theAction.apply((Sequence)d))
                    : oldAction;
            
            return newAction.apply($value);
        }
        
        /** Return the target result from the supper if the {@link Text} is a sequence. */
        public TARGET sequence(Supplier<? extends TARGET> theSupplier) {
            return sequence(d->theSupplier.get());
        }
        
        /** Return the target result if the {@link Text} is a sequence. */
        public TARGET sequence(TARGET theValue) {
            return sequence(d->theValue);
        }
        
        /**
         * Replace the value if this {@link Text} is sequence and match the given predicate.
         * 
         * @param check        the predicate to check the value.
         * @param theFunction  the function to replace the value if the predicate is <code>true.
         * @return             the switch object for sequence.
         */
        public TextSwitchSequence<TARGET> sequence(Predicate<Sequence> check, Function<? super Sequence, ? extends TARGET> theFunction) {
            @SuppressWarnings("unchecked")
            Function<Text, TARGET> oldAction = (Function<Text, TARGET>)$action;
            Function<Text, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    (($value instanceof Sequence) && check.test((Sequence)$value))
                    ? (Function<Text, TARGET>)(d -> theFunction.apply((Sequence)d))
                    : oldAction;
            
            return new TextSwitchSequence<TARGET>($value, newAction);
        }
        
        /**
         * Replace the value if this {@link Text} is sequence and match the given predicate.
         * 
         * @param check        the predicate to check the value.
         * @param theSupplier  the supplier to replace the value if the predicate is <code>true.
         * @return             the switch object for sequence.
         */
        public TextSwitchSequence<TARGET> sequence(Predicate<Sequence> check, Supplier<? extends TARGET> theSupplier) {
            return sequence(check, d->theSupplier.get());
        }
        
        /**
         * Replace the value if this {@link Text} is sequence and match the given predicate.
         * 
         * @param check     the predicate to check the value.
         * @param theValue  the value to replace if the predicate is <code>true</code>.
         * @return          the switch object for sequence.
         */
        public TextSwitchSequence<TARGET> sequence(Predicate<Sequence> check, TARGET theValue) {
            return sequence(check, d->theValue);
        }
    }
}
