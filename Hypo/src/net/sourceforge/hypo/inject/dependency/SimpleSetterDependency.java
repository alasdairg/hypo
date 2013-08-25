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

import net.sourceforge.hypo.InjectionAware;
import net.sourceforge.hypo.inject.Utils;

import org.apache.log4j.Logger;

/**
 * A Dependency implementation representing a "simple" setter method. i.e. a setter
 * method returning void and accepting a single argument.
 */
public class SimpleSetterDependency implements Dependency
{
   private static Logger log = Logger.getLogger( SimpleSetterDependency.class );
   
   
   private Method setter;
   private String beanName = "";
   
   public SimpleSetterDependency( Method setter, String beanName )
   {
      this.setter = setter;
      this.setter.setAccessible( true );
      if ( beanName != null )
         this.beanName = beanName;
   }
   
   public String getAssociatedName()
   {
      return beanName;
   }

   public Member getMember()
   {
      return setter;
   }
   
   public Class getType()
   {
      return setter.getParameterTypes()[0];
   }
   
   public void injectValue( Object targetObject, Object toInject )
   {
      try
      {
         if ( toInject instanceof InjectionAware )
         {
            ( (InjectionAware) toInject ).beforeInjection( targetObject, this );            
         }
         setter.invoke( targetObject, new Object[] { toInject } );
         if ( log.isDebugEnabled() )
            log.debug( "Called " + this + " on [" + Utils.getName( targetObject ) + "] with value [" + toInject + "]." );
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
