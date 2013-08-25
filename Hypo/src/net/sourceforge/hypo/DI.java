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

import java.lang.reflect.Method;

/**
 * Convenience class for acquiring a reference to the DependencyInjector.
 * Useful if configuration is being performed outside of Spring
 */
public final class DI
{
   private DI()
   {      
   }
   
   /**
    * @return a reference to the underlying DependencyInjectionAspect as a
    * DependencyInjector instance that can be manipulated by normal Java
    * code. Makes it easier to configure and use the aspect outside of a
    * Spring container if required.
    */
   public static DependencyInjector getDependencyInjector()
   {
      try
      {
         Class clazz = Class.forName( "net.sourceforge.hypo.azpect.SpringAwareDependencyInjectionAspect" );
         Method method = clazz.getMethod( "aspectOf", new Class[] {} );
         return (DependencyInjector) method.invoke(  null );
      }
      catch( Throwable t )
      {
         throw new RuntimeException( t );
      }
   }
}
