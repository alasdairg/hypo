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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sourceforge.hypo.inject.dependency.Dependency;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RegExSpringBeanResolver extends AbstractDependencyResolver implements ApplicationContextAware
{
   private static Logger log = Logger.getLogger( RegExSpringBeanResolver.class.getCanonicalName() );
   
   private ApplicationContext applicationContext;
   private RegExMapper mapper;
   
   public ResolutionResult doResolve( Dependency dep, Object target )
   {
      String beanName = mapper.getMappedString( dep.getType() );
      if ( beanName != null )
      {
         Object bean = applicationContext.getBean( beanName, dep.getType() );
         if ( log.isLoggable(Level.FINE) )
            log.fine( "Found bean [" + bean + "] to inject for dependency " + dep + "." );
         return ResolutionResult.resolved(bean);
      }
      else
         return ResolutionResult.couldNotResolve();
   }
   
   public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException
   {
      this.applicationContext = applicationContext;
   }
   
   public void setPatternMappings( List<String> mappings )
   {
      mapper = new RegExMapper( mappings );
   }  
}
