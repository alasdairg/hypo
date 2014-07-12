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

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sourceforge.hypo.inject.dependency.Dependency;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * A Spring-aware DependencyResolver that looks up the Spring ApplicationContext to find the single bean
 * which has the same type as or is a subclass of the Dependency's type
 */
public class DefaultTypeSpringBeanResolver extends AbstractDependencyResolver implements ApplicationContextAware
{
   private static Logger log = Logger.getLogger( DefaultTypeSpringBeanResolver.class.getCanonicalName() );
   
   private ApplicationContext applicationContext;
   
   /**
    * Performs injection by looking up the Spring ApplicationContext to find the single bean
    * which has the same type as or is a subclass of the Inject's type. 
    * @param dep the Dependency to be injected
    * @param target the object which is to have the member injected
    * @return true if a single matching bean existed in the context; false if no suitable 
    * bean was found
    * @throws RuntimeException if more than one bean in the Spring ApplicationContext matched
    * the Dependency
    */
   public ResolutionResult doResolve( Dependency dep, Object target )
   {
      Class<?> type = dep.getType();
      Map<?, ?> map = BeanFactoryUtils.beansOfTypeIncludingAncestors( applicationContext, type );
      if ( map.size() < 1 )
      {
    	 if ( log.isLoggable(Level.FINE) )
            log.fine( "No beans of required type found for dependency " + dep + ". Skipping." );
      }
      else if ( map.size() > 1 )
      {         
         throw new RuntimeException( "Multiple beans of required type " + type + " found. Aborting." );
      }
      else
      {
         Object bean = map.values().iterator().next();
         if ( log.isLoggable(Level.FINE) )
            log.fine( "Found bean [" + bean + "] to inject for dependency " + dep + "." );
         return ResolutionResult.resolved(bean);
      }
      return ResolutionResult.couldNotResolve();
   }
   
   public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException
   {
      this.applicationContext = applicationContext;
   }   
}
