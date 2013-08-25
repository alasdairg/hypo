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

package net.sourceforge.hypo.inject.dependency;

import java.lang.reflect.Member;

/**
 * Represents a class dependency that can be satisfied for a given object 
 * by injecting a value into it. Typically an implementation will simply 
 * be a wrapper around a java.lang.reflect.Member, but this is not a mandatory 
 * requirement
 */
public interface Dependency
{  
   /**
    * @return the name associated with the dependency (not the name of any actual
    * underlying member, but a name that has been explicitly associated with the dependency 
    * to potentially guide InjectionStrategies in their determination of a value to inject
    */
   String getAssociatedName();
   
   /**
    * @return the type of object that is required to satisfy this dependency
    */
   Class getType();
   
   /**
    * @return the underlying java.lang.reflect.Member if this Dependency represents
    * an underlying class member. This may be null if the dependency does not 
    * represent a member as such
    */
   Member getMember();
   
   /**
    * Satisfy the dependency for the specfied target object by injecting a real value
    * @param targetObject the object whose dependency is to be satisfied
    * @param toInject the object to be injected to satisfy the target object's dependency
    */
   void injectValue( Object targetObject, Object toInject );
}
