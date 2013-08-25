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

package net.sourceforge.hypo;

import net.sourceforge.hypo.inject.InjectionStrategy;


/**
 * Interface implemented by the DependencyInjectionAspect that allows it to be
 * more easily referenced and manipulated from a non-Aspect-aware application
 */
public interface DependencyInjector
{
   /**
    * @param set the InjectionStrategy that the injector should use.
    * @throws IllegalStateException if the DependencyInjector has already been started
    */
   void setInjectionStrategy( InjectionStrategy strat ) throws IllegalStateException;   
   
   /**
    * Start the DependencyInjector
    */
   void ready();
   
   void inject( Object obj );
}
