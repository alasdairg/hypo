/*
 * Copyright 2014 Alasdair Gilmour 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
package net.sourceforge.hypo.inject.resolver;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.hypo.inject.dependency.Dependency;

/**
 * 
 * A very simple implementation of DependencyResolver that allows types to be
 * programmatically bound to instances or InjectionFactory's. Resolution is based 
 * on the exact declared type of the dependency only.
 */
public class SimpleTypeMappingResolver extends AbstractDependencyResolver {

    private static final Object NULL_PLACEHOLDER = new Object();
    
    private Map<Class<?>, Object> typeMap = new HashMap<Class<?>, Object>();
    
    public SimpleTypeMappingResolver() {
        bind(DependencyResolver.class, this);
    }
    
    /**
     * Bind a type to an instance of that type. The binding will immediately
     * replace any existing binding for that type
     * @param clazz the type
     * @param instance an instance that is assignable to that type
     */
    public <T> void bind(Class<T> clazz, T instance) {
        if (instance == null) {
            typeMap.put(clazz, NULL_PLACEHOLDER);
        }
        typeMap.put(clazz, instance);
    }
    
    /**
     * Bind a type to an InjectionFactory that will provide instances of that type. 
     * The binding will immediately replace any existing binding for that type
     * @param clazz the type
     * @param factory an InjectFactory that will provide instances of that type
     */
    public <T> void bind(Class<T> clazz, InjectionFactory<T> factory) {
        if (factory == null) {
            typeMap.put(clazz, NULL_PLACEHOLDER);
        }
        typeMap.put(clazz, factory);
    }
   
    /**
     * Remove a binding for the specified type
     * @param clazz the type to remove a binding for
     */
    public void clearBinding(Class<?> clazz) {
        typeMap.remove(clazz);
    }
    
    /**
     * Merely return the instance or InjectionFactory that is bound for
     * the type of the supplied Dependency. Return ResolutionStatus.couldNotResolve() 
     * if there is no binding in place. Note that NULL may be explicitly bound to a type
     * and in this case ResolutionStatus.resolved() would be returned with a NULL value to
     * be injected
     * @param dependency the dependency being satisfied
     * @param target the target Object for which the dependency is being satisfied
     */
    @Override
    public ResolutionResult doResolve(Dependency dependency, Object target) {
        Class<?> type = dependency.getType();
        Object item = typeMap.get(type);
        if (item != null) {
            if (item == NULL_PLACEHOLDER) {
                return ResolutionResult.resolved(null);
            } else {
                return ResolutionResult.resolved(item);
            }
        } else {
           return ResolutionResult.couldNotResolve();
        }
    } 
}
