package net.sourceforge.hypo.inject.resolver;

public class ResolutionResult {

    private static final ResolutionResult COULD_NOT_RESOLVE = new ResolutionResult(false, null);
    
    private boolean resolved;
    private Object valueToInject;
    
    private ResolutionResult(boolean resolved, Object valueToInject) {
        this.resolved = resolved;
        this.valueToInject = valueToInject;
    }
    
    public boolean isResolved() {
        return resolved;
    }
    
    public Object getValueToInject() {
        return valueToInject;
    }
    
    public static ResolutionResult couldNotResolve() {
       return COULD_NOT_RESOLVE;    
    }
    
    public static ResolutionResult resolved(Object valueToInject) {
        ResolutionResult result = new ResolutionResult(true, valueToInject);
        return result;
    }
}
