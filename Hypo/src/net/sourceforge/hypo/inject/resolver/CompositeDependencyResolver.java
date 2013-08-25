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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.hypo.inject.dependency.Dependency;


/**
 * A DependencyResolver that simply loops through a list of DependencyResolvers, 
 * trying each one in turn until a dependency is resolved or there are no
 * Inject Resolvers left to try
 */
public class CompositeDependencyResolver implements DependencyResolver
{
   private List<DependencyResolver> resolvers = new ArrayList<DependencyResolver>();
   
   public CompositeDependencyResolver()
   {	   
   }
   
   public CompositeDependencyResolver( List<DependencyResolver> list )
   {
      setResolvers( list );
   }
   
   /**
    * Loop through the list of DependencyResolvers in order, asking each one 
    * to try and inject the dependency. The method returns immediately  as soon as one 
    * of the DependencyResolvers succeeds
    * @param dep object representing the dependency to be injected
    * @param target an object eligible for dependency injection
    * @return true if one of the DependencyResolvers injected the dependency; false if
    * none of them could
    */
   public boolean resolve(Dependency dep, Object target) 
   {
      for ( DependencyResolver resolver: resolvers )
      {
         boolean resolved = resolver.resolve(dep, target);
         if ( resolved )
        	 return true;
      }
      return false;
   }
   
   /**
    * @param list A list of resolvers to be consulted in order
    */
   public void setResolvers( List<DependencyResolver> list )
   {
      resolvers = list;
   }
}
