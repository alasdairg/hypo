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
 * Implementations know how to determine and inject values to satisfy an Object's
 * dependencies
 */
public interface DependencyResolver
{
   /**
    * Attempt to inject a value for the specified dependency of the given object
    * @param dep object representing the dependency to be resolved
    * @param target an object eligible for dependency injection
    * @return true if the implementation was able to determine and inject a value, false
    * if no suitable value could be found to inject
    */
   boolean resolve( Dependency dep, Object target );
}
