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


import java.util.logging.Level;
import java.util.logging.Logger;

import net.sourceforge.hypo.inject.dependency.Dependency;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * A Spring-aware DependencyResolver that looks up the Spring ApplicationContext to find the single bean
 * which has the name specified by the Dependency
 */
public class NamedSpringBeanResolver extends AbstractDependencyResolver implements ApplicationContextAware
{      
   private static Logger log = Logger.getLogger( NamedSpringBeanResolver.class.getCanonicalName() );
   
   private ApplicationContext applicationContext;
   
   /**
    * Performs injection by looking up the Spring ApplicationContext to find the single bean
    * which has the name specified by the InjectionMember 
    * @param dep the Dependency to be injected
    * @param target the object which is to have the member injected
    * @return true if a bean with the required name existed in the context; false if no suitable 
    * bean was found
    */
   public ResolutionResult doResolve( Dependency dep, Object target )
   {  
      String name = dep.getAssociatedName();
      
      try
      {
         Object bean = applicationContext.getBean( name, dep.getType() );      
         if ( log.isLoggable(Level.FINE) )
            log.fine( "Found bean named \"" + name + "\" to inject for dependency " + dep + "." );
         return ResolutionResult.resolved(bean);
      }
      catch( NoSuchBeanDefinitionException nsbe )
      {
         //Drop out with "could not resolve" result
      }
      return ResolutionResult.couldNotResolve();
   }

   public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException
   {
      this.applicationContext = applicationContext;
   }
}
