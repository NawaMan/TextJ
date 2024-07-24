package net.nawaman.textj.text;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import functionalj.lens.core.LensSpec;
import functionalj.lens.lenses.BooleanAccessPrimitive;
import functionalj.lens.lenses.ObjectLensImpl;
import functionalj.lens.lenses.ResultLens;
import functionalj.list.FuncList;
import functionalj.pipeable.Pipeable;
import functionalj.result.Result;
import functionalj.types.choice.ChoiceTypeSwitch;
import functionalj.types.choice.IChoice;

public abstract class ImmutableCharSequence implements CharSequence, IChoice<ImmutableCharSequence.ImmutableCharSequenceFirstSwitch>, Pipeable<ImmutableCharSequence> {
    
    public static final Str Str(String string) {
        return new Str(string);
    }
    public static final Part Part(ImmutableCharSequence full, int start, int end) {
        return new Part(full, start, end);
    }
    public static final Sequence Sequence(FuncList<ImmutableCharSequence> sequence) {
        return new Sequence(sequence);
    }
    
    ImmutableCharSequence() {}
    
    //== Functional Choice ==
    // Due to the limitation of creating lens for FuncList of Self,
    //    the code is generate but copied here with manual modification.
    
    
    public static final ImmutableCharSequenceLens<ImmutableCharSequence> theImmutableCharSequence = new ImmutableCharSequenceLens<>("theImmutableCharSequence", LensSpec.of(ImmutableCharSequence.class));
    public static final ImmutableCharSequenceLens<ImmutableCharSequence> eachImmutableCharSequence = theImmutableCharSequence;
    public static class ImmutableCharSequenceLens<HOST> extends ObjectLensImpl<HOST, ImmutableCharSequence> {
        public final BooleanAccessPrimitive<ImmutableCharSequence> isStr = ImmutableCharSequence::isStr;
        public final BooleanAccessPrimitive<ImmutableCharSequence> isPart = ImmutableCharSequence::isPart;
        public final BooleanAccessPrimitive<ImmutableCharSequence> isSequence = ImmutableCharSequence::isSequence;
        public final ResultLens.Impl<HOST, Str, Str.StrLens<HOST>> asStr = createSubResultLens("asStr", ImmutableCharSequence::asStr, (functionalj.lens.core.WriteLens<ImmutableCharSequence,Result<Str>>)(h,r)->r.get(), Str.StrLens::new);
        public final ResultLens.Impl<HOST, Part, Part.PartLens<HOST>> asPart = createSubResultLens("asPart", ImmutableCharSequence::asPart, (functionalj.lens.core.WriteLens<ImmutableCharSequence,Result<Part>>)(h,r)->r.get(), Part.PartLens::new);
        public final ResultLens.Impl<HOST, Sequence, Sequence.SequenceLens<HOST>> asSequence = createSubResultLens("asSequence", ImmutableCharSequence::asSequence, (functionalj.lens.core.WriteLens<ImmutableCharSequence,Result<Sequence>>)(h,r)->r.get(), Sequence.SequenceLens::new);
        public ImmutableCharSequenceLens(String name, LensSpec<HOST, ImmutableCharSequence> spec) {
            super(name, spec);
        }
    }
    
    public ImmutableCharSequence __data() throws Exception { return this; }
    public Result<ImmutableCharSequence> toResult() { return Result.valueOf(this); }
    
    @SuppressWarnings("unchecked")
    public static <T extends ImmutableCharSequence> T fromMap(Map<String, ? extends Object> map) {
        if (map == null)
            return null;
        String __tagged = (String)map.get("__tagged");
        if ("Str".equals(__tagged))
            return (T)Str.caseFromMap(map);
        if ("Part".equals(__tagged))
            return (T)Part.caseFromMap(map);
        if ("Sequence".equals(__tagged))
            return (T)Sequence.caseFromMap(map);
        throw new IllegalArgumentException("Tagged value does not represent a valid type: " + __tagged);
    }
    
    static private functionalj.map.FuncMap<String, Map<String, functionalj.types.choice.generator.model.CaseParam>> __schema__ = functionalj.map.FuncMap.<String, Map<String, functionalj.types.choice.generator.model.CaseParam>>newMap()
        .with("Str", Str.getCaseSchema())
        .with("Part", Part.getCaseSchema())
        .with("Sequence", Sequence.getCaseSchema())
        .build();
    public static Map<String, Map<String, functionalj.types.choice.generator.model.CaseParam>> getChoiceSchema() {
        return __schema__;
    }
    
    public Map<java.lang.String, Map<java.lang.String, functionalj.types.choice.generator.model.CaseParam>> __getSchema() {
        return getChoiceSchema();
    }
    
    private final transient ImmutableCharSequenceFirstSwitch __switch = new ImmutableCharSequenceFirstSwitch(this);
    @Override public ImmutableCharSequenceFirstSwitch match() {
         return __switch;
    }
    
    private volatile String toString = null;
    
    @Override
    public String toString() {
        if (toString != null)
            return toString;
        synchronized(this) {
            if (toString != null)
                return toString;
            toString = $utils.Match(this)
                    .str(str -> "Str(" + String.format("%1$s", str.string()) + ")")
                    .part(part -> "Part(" + String.format("%1$s,%2$s,%3$s", part.full(),part.start(),part.end()) + ")")
                    .sequence(sequence -> "Sequence(" + String.format("%1$s", sequence.sequence()) + ")")
            ;
            return toString;
        }
    }
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ImmutableCharSequence))
            return false;
        
        if (this == obj)
            return true;
        
        String objToString  = obj.toString();
        String thisToString = this.toString();
        return thisToString.equals(objToString);
    }
    
    
    public boolean isStr() { return this instanceof Str; }
    public Result<Str> asStr() { return Result.valueOf(this).filter(Str.class).map(Str.class::cast); }
    public ImmutableCharSequence ifStr(Consumer<Str> action) { if (isStr()) action.accept((Str)this); return this; }
    public ImmutableCharSequence ifStr(Runnable action) { if (isStr()) action.run(); return this; }
    public boolean isPart() { return this instanceof Part; }
    public Result<Part> asPart() { return Result.valueOf(this).filter(Part.class).map(Part.class::cast); }
    public ImmutableCharSequence ifPart(Consumer<Part> action) { if (isPart()) action.accept((Part)this); return this; }
    public ImmutableCharSequence ifPart(Runnable action) { if (isPart()) action.run(); return this; }
    public boolean isSequence() { return this instanceof Sequence; }
    public Result<Sequence> asSequence() { return Result.valueOf(this).filter(Sequence.class).map(Sequence.class::cast); }
    public ImmutableCharSequence ifSequence(Consumer<Sequence> action) { if (isSequence()) action.accept((Sequence)this); return this; }
    public ImmutableCharSequence ifSequence(Runnable action) { if (isSequence()) action.run(); return this; }
    
    public static class ImmutableCharSequenceFirstSwitch {
        private ImmutableCharSequence $value;
        private ImmutableCharSequenceFirstSwitch(ImmutableCharSequence theValue) { this.$value = theValue; }
        public <TARGET> ImmutableCharSequenceFirstSwitchTyped<TARGET> toA(Class<TARGET> clzz) {
            return new ImmutableCharSequenceFirstSwitchTyped<TARGET>($value);
        }
        
        public <TARGET> ImmutableCharSequenceSwitchPartSequence<TARGET> str(Function<? super Str, ? extends TARGET> theAction) {
            Function<ImmutableCharSequence, TARGET> $action = null;
            Function<ImmutableCharSequence, TARGET> oldAction = (Function<ImmutableCharSequence, TARGET>)$action;
            Function<ImmutableCharSequence, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    ($value instanceof Str)
                    ? (Function<ImmutableCharSequence, TARGET>)(d -> theAction.apply((Str)d))
                    : oldAction;
            
            return new ImmutableCharSequenceSwitchPartSequence<TARGET>($value, newAction);
        }
        public <TARGET> ImmutableCharSequenceSwitchPartSequence<TARGET> str(Supplier<? extends TARGET> theSupplier) {
            return str(d->theSupplier.get());
        }
        public <TARGET> ImmutableCharSequenceSwitchPartSequence<TARGET> str(TARGET theValue) {
            return str(d->theValue);
        }
        
        public <TARGET> ImmutableCharSequenceSwitchStrPartSequence<TARGET> str(java.util.function.Predicate<Str> check, Function<? super Str, ? extends TARGET> theAction) {
            Function<ImmutableCharSequence, TARGET> $action = null;
            Function<ImmutableCharSequence, TARGET> oldAction = (Function<ImmutableCharSequence, TARGET>)$action;
            Function<ImmutableCharSequence, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    (($value instanceof Str) && check.test((Str)$value))
                    ? (Function<ImmutableCharSequence, TARGET>)(d -> theAction.apply((Str)d))
                    : oldAction;
            
            return new ImmutableCharSequenceSwitchStrPartSequence<TARGET>($value, newAction);
        }
        public <TARGET> ImmutableCharSequenceSwitchStrPartSequence<TARGET> str(java.util.function.Predicate<Str> check, Supplier<? extends TARGET> theSupplier) {
            return str(check, d->theSupplier.get());
        }
        public <TARGET> ImmutableCharSequenceSwitchStrPartSequence<TARGET> str(java.util.function.Predicate<Str> check, TARGET theValue) {
            return str(check, d->theValue);
        }
    }
    public static class ImmutableCharSequenceFirstSwitchTyped<TARGET> {
        private ImmutableCharSequence $value;
        private ImmutableCharSequenceFirstSwitchTyped(ImmutableCharSequence theValue) { this.$value = theValue; }
        
        public ImmutableCharSequenceSwitchPartSequence<TARGET> str(Function<? super Str, ? extends TARGET> theAction) {
            Function<ImmutableCharSequence, TARGET> $action = null;
            Function<ImmutableCharSequence, TARGET> oldAction = (Function<ImmutableCharSequence, TARGET>)$action;
            Function<ImmutableCharSequence, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    ($value instanceof Str)
                    ? (Function<ImmutableCharSequence, TARGET>)(d -> theAction.apply((Str)d))
                    : oldAction;
            
            return new ImmutableCharSequenceSwitchPartSequence<TARGET>($value, newAction);
        }
        public ImmutableCharSequenceSwitchPartSequence<TARGET> str(Supplier<? extends TARGET> theSupplier) {
            return str(d->theSupplier.get());
        }
        public ImmutableCharSequenceSwitchPartSequence<TARGET> str(TARGET theValue) {
            return str(d->theValue);
        }
        
        public ImmutableCharSequenceSwitchStrPartSequence<TARGET> str(java.util.function.Predicate<Str> check, Function<? super Str, ? extends TARGET> theAction) {
            Function<ImmutableCharSequence, TARGET> $action = null;
            Function<ImmutableCharSequence, TARGET> oldAction = (Function<ImmutableCharSequence, TARGET>)$action;
            Function<ImmutableCharSequence, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    (($value instanceof Str) && check.test((Str)$value))
                    ? (Function<ImmutableCharSequence, TARGET>)(d -> theAction.apply((Str)d))
                    : oldAction;
            
            return new ImmutableCharSequenceSwitchStrPartSequence<TARGET>($value, newAction);
        }
        public ImmutableCharSequenceSwitchStrPartSequence<TARGET> str(java.util.function.Predicate<Str> check, Supplier<? extends TARGET> theSupplier) {
            return str(check, d->theSupplier.get());
        }
        public ImmutableCharSequenceSwitchStrPartSequence<TARGET> str(java.util.function.Predicate<Str> check, TARGET theValue) {
            return str(check, d->theValue);
        }
    }
    public static class ImmutableCharSequenceSwitchStrPartSequence<TARGET> extends ChoiceTypeSwitch<ImmutableCharSequence, TARGET> {
        private ImmutableCharSequenceSwitchStrPartSequence(ImmutableCharSequence theValue, Function<ImmutableCharSequence, ? extends TARGET> theAction) { super(theValue, theAction); }
        
        public ImmutableCharSequenceSwitchPartSequence<TARGET> str(Function<? super Str, ? extends TARGET> theAction) {
            @SuppressWarnings("unchecked")
            Function<ImmutableCharSequence, TARGET> oldAction = (Function<ImmutableCharSequence, TARGET>)$action;
            Function<ImmutableCharSequence, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    ($value instanceof Str)
                    ? (Function<ImmutableCharSequence, TARGET>)(d -> theAction.apply((Str)d))
                    : oldAction;
            
            return new ImmutableCharSequenceSwitchPartSequence<TARGET>($value, newAction);
        }
        public ImmutableCharSequenceSwitchPartSequence<TARGET> str(Supplier<? extends TARGET> theSupplier) {
            return str(d->theSupplier.get());
        }
        public ImmutableCharSequenceSwitchPartSequence<TARGET> str(TARGET theValue) {
            return str(d->theValue);
        }
        
        public ImmutableCharSequenceSwitchStrPartSequence<TARGET> str(java.util.function.Predicate<Str> check, Function<? super Str, ? extends TARGET> theAction) {
            @SuppressWarnings("unchecked")
            Function<ImmutableCharSequence, TARGET> oldAction = (Function<ImmutableCharSequence, TARGET>)$action;
            Function<ImmutableCharSequence, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    (($value instanceof Str) && check.test((Str)$value))
                    ? (Function<ImmutableCharSequence, TARGET>)(d -> theAction.apply((Str)d))
                    : oldAction;
            
            return new ImmutableCharSequenceSwitchStrPartSequence<TARGET>($value, newAction);
        }
        public ImmutableCharSequenceSwitchStrPartSequence<TARGET> str(java.util.function.Predicate<Str> check, Supplier<? extends TARGET> theSupplier) {
            return str(check, d->theSupplier.get());
        }
        public ImmutableCharSequenceSwitchStrPartSequence<TARGET> str(java.util.function.Predicate<Str> check, TARGET theValue) {
            return str(check, d->theValue);
        }
    }
    public static class ImmutableCharSequenceSwitchPartSequence<TARGET> extends ChoiceTypeSwitch<ImmutableCharSequence, TARGET> {
        private ImmutableCharSequenceSwitchPartSequence(ImmutableCharSequence theValue, Function<ImmutableCharSequence, ? extends TARGET> theAction) { super(theValue, theAction); }
        
        public ImmutableCharSequenceSwitchSequence<TARGET> part(Function<? super Part, ? extends TARGET> theAction) {
            @SuppressWarnings("unchecked")
            Function<ImmutableCharSequence, TARGET> oldAction = (Function<ImmutableCharSequence, TARGET>)$action;
            Function<ImmutableCharSequence, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    ($value instanceof Part)
                    ? (Function<ImmutableCharSequence, TARGET>)(d -> theAction.apply((Part)d))
                    : oldAction;
            
            return new ImmutableCharSequenceSwitchSequence<TARGET>($value, newAction);
        }
        public ImmutableCharSequenceSwitchSequence<TARGET> part(Supplier<? extends TARGET> theSupplier) {
            return part(d->theSupplier.get());
        }
        public ImmutableCharSequenceSwitchSequence<TARGET> part(TARGET theValue) {
            return part(d->theValue);
        }
        
        public ImmutableCharSequenceSwitchPartSequence<TARGET> part(java.util.function.Predicate<Part> check, Function<? super Part, ? extends TARGET> theAction) {
            @SuppressWarnings("unchecked")
            Function<ImmutableCharSequence, TARGET> oldAction = (Function<ImmutableCharSequence, TARGET>)$action;
            Function<ImmutableCharSequence, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    (($value instanceof Part) && check.test((Part)$value))
                    ? (Function<ImmutableCharSequence, TARGET>)(d -> theAction.apply((Part)d))
                    : oldAction;
            
            return new ImmutableCharSequenceSwitchPartSequence<TARGET>($value, newAction);
        }
        public ImmutableCharSequenceSwitchPartSequence<TARGET> part(java.util.function.Predicate<Part> check, Supplier<? extends TARGET> theSupplier) {
            return part(check, d->theSupplier.get());
        }
        public ImmutableCharSequenceSwitchPartSequence<TARGET> part(java.util.function.Predicate<Part> check, TARGET theValue) {
            return part(check, d->theValue);
        }
    }
    public static class ImmutableCharSequenceSwitchSequence<TARGET> extends ChoiceTypeSwitch<ImmutableCharSequence, TARGET> {
        private ImmutableCharSequenceSwitchSequence(ImmutableCharSequence theValue, Function<ImmutableCharSequence, ? extends TARGET> theAction) { super(theValue, theAction); }
        
        public TARGET sequence(Function<? super Sequence, ? extends TARGET> theAction) {
            @SuppressWarnings("unchecked")
            Function<ImmutableCharSequence, TARGET> oldAction = (Function<ImmutableCharSequence, TARGET>)$action;
            Function<ImmutableCharSequence, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    ($value instanceof Sequence)
                    ? (Function<ImmutableCharSequence, TARGET>)(d -> theAction.apply((Sequence)d))
                    : oldAction;
            
            return newAction.apply($value);
        }
        public TARGET sequence(Supplier<? extends TARGET> theSupplier) {
            return sequence(d->theSupplier.get());
        }
        public TARGET sequence(TARGET theValue) {
            return sequence(d->theValue);
        }
        
        public ImmutableCharSequenceSwitchSequence<TARGET> sequence(java.util.function.Predicate<Sequence> check, Function<? super Sequence, ? extends TARGET> theAction) {
            @SuppressWarnings("unchecked")
            Function<ImmutableCharSequence, TARGET> oldAction = (Function<ImmutableCharSequence, TARGET>)$action;
            Function<ImmutableCharSequence, TARGET> newAction =
                ($action != null)
                ? oldAction : 
                    (($value instanceof Sequence) && check.test((Sequence)$value))
                    ? (Function<ImmutableCharSequence, TARGET>)(d -> theAction.apply((Sequence)d))
                    : oldAction;
            
            return new ImmutableCharSequenceSwitchSequence<TARGET>($value, newAction);
        }
        public ImmutableCharSequenceSwitchSequence<TARGET> sequence(java.util.function.Predicate<Sequence> check, Supplier<? extends TARGET> theSupplier) {
            return sequence(check, d->theSupplier.get());
        }
        public ImmutableCharSequenceSwitchSequence<TARGET> sequence(java.util.function.Predicate<Sequence> check, TARGET theValue) {
            return sequence(check, d->theValue);
        }
    }
}
