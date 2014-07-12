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
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sourceforge.hypo.InjectionAware;
import net.sourceforge.hypo.inject.Utils;

/**
 * A Dependency implementation representing a "simple" setter method. i.e. a setter
 * method returning void and accepting a single argument.
 */
public class SimpleSetterDependency implements Dependency
{
   private static Logger log = Logger.getLogger( SimpleSetterDependency.class.getCanonicalName() );
  
   private Method setter;
   private String name = "";
   
   /**
    * Constructor
    * @param field a java.lang.reflect.Method that has been identified as a dependency
    * @param name an optional name for the dependency
    */
   public SimpleSetterDependency( Method setter, String name )
   {
      this.setter = setter;
      this.setter.setAccessible( true );
      if ( name != null )
         this.name = name;
   }
   
   /**
    * @return the associated name
    */
   public String getAssociatedName()
   {
      return name;
   }

   /**
    * @return the underlying java.lang.reflect.Method itself
    */
   public Member getMember()
   {
      return setter;
   }
   
   /**
    * @return the type of the setter method's single parameter
    */
   public Class<?> getType()
   {
      return setter.getParameterTypes()[0];
   }
   
   /**
    * Use reflection to satisfy the Dependency for 'targetObject' by reflectively
    * invoking the setter method and passing 'toInject' as its single parameter
    * 
    * @param targetObject the Object whose dependency is being satisfied
    * @param toInject an object of the appropriate type that is being injected
    * to satisfy the dependency
    */
   public void injectValue( Object targetObject, Object toInject )
   {
      try
      {
         if ( toInject instanceof InjectionAware )
         {
            ( (InjectionAware) toInject ).beforeInjection( targetObject, this );            
         }
         setter.invoke( targetObject, new Object[] { toInject } );
         if ( log.isLoggable(Level.FINE) )
            log.fine( "Called " + this + " on [" + Utils.getName( targetObject ) + "] with value [" + toInject + "]." );
      }
      catch( Exception e )
      {
         throw new RuntimeException( "Failed to inject via setter method " + setter, e );
      }      
   }

   public String toString()
   {
      StringBuffer buff = new StringBuffer();
      buff.append( "[Setter " );
      buff.append( setter.getName() );
      buff.append( "()" ); 
      if ( name != null && name.length() > 0 )
      {
         buff.append( " @\"" );
         buff.append( name );
         buff.append( "\"" );
      }
      buff.append( ']' );
      return buff.toString();
   }
}
