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

import java.util.ArrayList;
import java.util.List;

/**
 * An InjectionStrategy that loops through a list of InjectionStrategies
 * allowing each to try to perform injection. It is possible for more than one 
 * InjectionStrategy to perform injection on a given instance
 */
public class CompositeInjectionStrategy implements InjectionStrategy
{
   private List<InjectionStrategy> strategies = new ArrayList<InjectionStrategy>();
   
   public CompositeInjectionStrategy()
   {
   }
   
   public CompositeInjectionStrategy( List<InjectionStrategy> list )
   {
      setStrategies( list );
   }
   
   public boolean performInjection(Object obj) throws UnresolvedDependenciesException
   {
	  boolean retval = false;
      for ( InjectionStrategy strat: strategies )
      {
         if ( strat.performInjection( obj ) )
            retval = true;
      }
      return retval;
   }
   
   public boolean performInjection( Object obj, Class<?> clazz ) throws UnresolvedDependenciesException
   {
     boolean retval = false;
      for ( InjectionStrategy strat: strategies )
      {
         if ( strat.performInjection( obj, clazz ) )
            retval = true;
      }
      return retval;
   }
   
   public void setStrategies( List<InjectionStrategy> list )
   {
      strategies = list;
   }
}
