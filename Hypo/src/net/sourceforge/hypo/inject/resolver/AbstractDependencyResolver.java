/*
 * Copyright 2008 Alasdair Gilmour 
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

import net.sourceforge.hypo.inject.dependency.Dependency;

/**
 * Convenient superclass for DependencyResolvers. Provides support for InjectionFactories:
 * subclasses may return an InjectionFactory from doResolve() instead of a direct instance.
 * In this case, the InjectionFactory's get() method will be called to obtain the instance
 * to be injected.
 */
public abstract class AbstractDependencyResolver implements DependencyResolver {

    /**
     * 
     */
    @Override
    public boolean resolve(Dependency dep, Object target) {
       
       ResolutionResult result = doResolve(dep, target);
       
       if (!result.isResolved()) {
           return false;
       }
       
       Object obj = result.getValueToInject();
       if (obj instanceof InjectionFactory) {
          obj = ((InjectionFactory<?>) obj).get(target, dep);
       }
       dep.injectValue(target, obj);

       return true;
    }

    /**
     * Overridden by subclasses to provide an instance to be injected OR
     * an InjectionFactory that will actually provide the instance
     * @param dep the Dependency being satisfied
     * @param target the target Object for which the dependency is being satisfied
     * @return an instance for injection or an InjectionFactory that will supply
     * such an instance 
     */
    protected abstract ResolutionResult doResolve(Dependency dep, Object target);
    
}
