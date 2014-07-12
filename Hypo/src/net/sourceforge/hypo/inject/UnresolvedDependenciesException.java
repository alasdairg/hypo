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

package net.sourceforge.hypo.inject;

import java.util.Collection;

import net.sourceforge.hypo.inject.dependency.Dependency;


/**
 * Thrown when an InjectionStrategy identified dependencies for an object, but
 * one or more of them could not be satisfied by it 
 */
public class UnresolvedDependenciesException extends RuntimeException
{	   
   private static final long serialVersionUID = 1L;
   private Collection<Dependency> unresolvedDependencies;
   private Object culprit;
	
   public UnresolvedDependenciesException( Object culprit, Collection<Dependency> unresolved )
   {
	   this.culprit = culprit;
      unresolvedDependencies = unresolved;
   }
   
   public String getMessage()
   {
      return "Could not resolve dependencies for " + culprit + "; specifically the following dependencies: " + unresolvedDependencies;
   }

   /**
    * @return the instance whose dependencies could not be completely satisfied
    */
   public Object getCulprit() 
   {
      return culprit;
   }

   /**
    * @return a List of just those dependencies of the instance that could
    * not be satisfied
    */
   public Collection<Dependency> getUnresolvedDependencies() 
   {
      return unresolvedDependencies;
   }      
}
