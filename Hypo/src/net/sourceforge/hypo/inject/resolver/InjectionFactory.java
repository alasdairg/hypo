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

import net.sourceforge.hypo.inject.dependency.Dependency;

/**
 * An InjectionFactory produces instances of a type for injection. Implementations
 * can dynamically decide what to provide based on the context. If DependencyResolver.resolve()
 * returns an InjectionFactory<T> rather than an actual instance of T then the framework will
 * call get() on the factory to resolve the actual instance to be injected. This behavior is in 
 * the AbstractDependencyResolver class, so DependencyResolvers wishing to take advantage of this
 * should extends that class.
 *
 * @param <T>
 */
public interface InjectionFactory<T> {

    T get(Object targetObject, Dependency dep);
}
