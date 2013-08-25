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

import net.sourceforge.hypo.InjectionAware;
import net.sourceforge.hypo.inject.Utils;

import org.apache.log4j.Logger;

/**
 * A Dependency implementation representing a Field.
 */
public class FieldDependency implements Dependency
{
   private static Logger log = Logger.getLogger( FieldDependency.class );
   
   private Field field;
   private String beanName = "";
   
   public FieldDependency( Field field, String beanName )
   {
      this.field = field;
      this.field.setAccessible( true );
      if ( beanName != null )
         this.beanName = beanName;
   }   

   public String getAssociatedName()
   {
      return beanName;
   }

   public Member getMember()
   {
      return field;
   }
   
   public Class getType()
   {
      return field.getType();
   }
   
   public void injectValue( Object targetObject, Object toInject )
   {
      try
      {
         if ( toInject instanceof InjectionAware )
         {
            ( (InjectionAware) toInject ).beforeInjection( targetObject, this );            
         }
         field.set( targetObject, toInject );
         if ( log.isDebugEnabled() )
            log.debug( "Set field " + this + " on [" + Utils.getName( targetObject ) + "] to value [" + toInject + "]." );
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
      if ( beanName != null && beanName.length() > 0 )
      {
         buff.append( " @\"" );
         buff.append( beanName );
         buff.append( "\"" );
      }
      buff.append( ']' );
      return buff.toString();
   }   
}
