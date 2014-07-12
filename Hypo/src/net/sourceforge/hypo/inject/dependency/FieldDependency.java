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

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sourceforge.hypo.InjectionAware;
import net.sourceforge.hypo.inject.Utils;

/**
 * A Dependency implementation representing a java.lang.reflect.Field.
 */
public class FieldDependency implements Dependency
{
   private static Logger log = Logger.getLogger( FieldDependency.class.getCanonicalName() );
   
   private Field field;
   private String name = "";
   
   /**
    * Constructor
    * @param field a java.lang.reflect.Field that has been identified as a dependency
    * @param name an optional name for the dependency
    */
   public FieldDependency( Field field, String name )
   {
      this.field = field;
      this.field.setAccessible( true );
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
    * @return the Field itself
    */
   public Member getMember()
   {
      return field;
   }
   
   /**
    * @return the type of the Field
    */
   public Class<?> getType()
   {
      return field.getType();
   }
   
   /**
    * Use reflection to satisfy the Dependency for 'targetObject' by reflectively
    * setting its field value to be 'toInject'
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
         field.set( targetObject, toInject );
         if ( log.isLoggable(Level.FINE) )
            log.fine( "Set field " + this + " on [" + Utils.getName( targetObject ) + "] to value [" + toInject + "]." );
      }
      catch( Exception e )
      {
         throw new RuntimeException( "Failed to inject instance field " + field, e );
      }
   }

   public String toString()
   {
      StringBuffer buff = new StringBuffer();
      buff.append( "[Field " );
      buff.append( field.getName() );
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
