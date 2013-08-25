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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.hypo.inject.dependency.DefaultDependencyFactory;
import net.sourceforge.hypo.inject.dependency.Dependency;
import net.sourceforge.hypo.inject.dependency.DependencyFactory;
import net.sourceforge.hypo.inject.resolver.DependencyResolver;
import net.sourceforge.hypo.inject.resolver.NonResolver;

import org.apache.log4j.Logger;


/**
 * Convenient superclass for InjectionStrategies. Drives the workflow for injection of an
 * instance. Subclasses only need to implement the selectDependencies() method to identify what
 * dependencies (if any) need to be satisfied for a particular Object. A DependencyResolver 
 * is then consulted to determine values for those Dependencies
 */
public abstract class AbstractInjectionStrategy implements InjectionStrategy
{
   private Logger log = Logger.getLogger( this.getClass() );
   private DependencyResolver resolver = new NonResolver();
   private DependencyFactory dependencyFactory = new DefaultDependencyFactory();   
   private boolean useClassCaching = true;
   private Map<Class, List<Dependency>> cachedDependencies = new HashMap<Class, List<Dependency>>();
   private ThreadLocal<Set<Class>> cycleDetect = new ThreadLocal<Set<Class>>();
   	
  /**
   * Determine if the specified object requires dependency injection, and if it does,
   * attempt to inject each of these dependencies by consulting the DependencyResolver. 
   * An object that does not require dependencies to be injected
   * is ignored. If the object was considered eligible for dependency injection, but one
   * or more of the dependencies could not be satisfied by the DependencyResolver
   * then an exception is thrown. A simple mechanism is used to detect cyclic dependencies
   * and an exception is thrown if a cycle is detected
   * @param obj an object to consider for dependency injection
   * @return true if the object was eligible for dependency injection and has had all 
   * identified dependencies injected; false if the object was ineligible for dependency 
   * injection
   * @throws UnresolvedDependenciesException if the object was eligible for dependency 
   * injection, but not all of the identified dependencies could be resolved
   * @throws IllegalStateException if a cycle was detected trying to resolve dependencies for the object
   */
   public final boolean performInjection( Object obj, Class clazz ) throws UnresolvedDependenciesException, IllegalStateException
   {
      boolean retval = false;
      try
      {
         register( clazz );
      
         List<Dependency> members = getDependencies( clazz );
         if ( members != null && members.size() > 0 )
         {
            retval = true;
            if ( log.isDebugEnabled() )
               log.debug( "Started processing eligible object [" + Utils.getName( obj ) + "]. Class " + clazz.getName() );
            Set<Dependency> unresolved = new HashSet<Dependency>( members );
            for ( Dependency member: members )
            {
               boolean resolved = resolver.resolve( member, obj );
               if ( resolved )
                  unresolved.remove( member );
            }
            if ( unresolved.size() > 0 )
               throw new UnresolvedDependenciesException( obj, unresolved );
            else
            {
               if ( log.isDebugEnabled() )
                  log.debug( "Completed processing object [" + Utils.getName( obj ) + "]. Class " + clazz.getName() );
            }
         }
         else
         {
        	if ( log.isDebugEnabled() )
               log.debug( "Ignoring ineligible object [" + Utils.getName( obj ) + "]. Class " + clazz.getName() );
         }
      }
      finally
      {
    	  unregister( clazz );
      }
      return retval;
   }
   
   public final boolean performInjection( Object obj ) throws UnresolvedDependenciesException, IllegalStateException
   {
      Class clazz = obj.getClass();
      boolean retval = false;
      while ( clazz != Object.class )
      {
         boolean injectionPerformed = performInjection( obj, clazz );
         retval = retval || injectionPerformed;
         clazz = clazz.getSuperclass();
      }
      return retval;
   }
   
   /**
    * Return a List of Dependencies which need to be satisfied for the given instance
    * @param obj an instance which is to be assessed for dependency injection
    * by this InjectionStrategy
    * @return a list of Dependencies. An empty list signifies that the instance is 
    * ineligible for dependency injection as far as this InjectionStrategy is concerned
    */
   public abstract List<Dependency> selectDependencies( Class clazz );
   
   /**
    * Determines whether or not to cache the list of Dependencies returned
    * from selectDependencies() on a per class basis. If this method returns true
    * then selectDependencies() will only be called once for each distinct class; thereafter the 
    * List of Dependencies will be cached and returned directly for all subsequent instances of 
    * that class. Defaults to true.
    * @param cc true to enable class caching; false to disable
    */
   public void setClassCaching( boolean cc )
   {
      useClassCaching = cc;
   }
   
   /**
    * @param obj an instance to consider for dependency injection
    * @return a list of Dependencies that need to be satisfied for this instance. If 
    * this list is empty, then the instance is deemed ineligible for dependency 
    * injection. If class caching is enabled then a per-class cache is consulted first
    * and if present, a List of Dependencies is returned directly. Otherwise, this method 
    * returns the result of selectDependencies()
    */
   private List<Dependency> getDependencies( Class clazz )
   {
      List<Dependency> retval = null;
      if ( useClassCaching )
      {
         if ( cachedDependencies.containsKey( clazz ) )
            retval = cachedDependencies.get( clazz );
         else
         {
            retval = selectDependencies( clazz );
            cachedDependencies.put( clazz, retval );
         }
      }
      else
      {
         retval = selectDependencies( clazz );
      }
      return retval;
   }
   
   /**
    * Set the DependencyResolver to use for dependency resolution
    * @param resolver
    */
   public void setDependencyResolver( DependencyResolver resolver )
   {
      this.resolver = resolver;
   }

   /**
    * Set the DependencyFactory to be used for creating Dependency objects. Defaults
    * to DefaultDependencyFactory
    * @param dependencyFactory
    */
   public void setDependencyFactory( DependencyFactory dependencyFactory ) 
   {
      this.dependencyFactory = dependencyFactory;
   }

   /**
    * Convenience method for subclasses to call. Merely defers to the DependencyFactory 
    * that has been set to create a Dependency
    */
   protected final Dependency createDependency( Object obj, String name) 
   {
	return dependencyFactory.createDependency( obj, name );
   }
   
   /**
    * Make a note of the specified class in a thread local collection. If the class is already present then this
    * indicates that we were already in the process of resolving dependencies for an instance of that class, 
    * so there must somehow be a cycle in the dependencies. If this is the case, break out of the cycle by 
    * throwing an exception
    * @param clazz the class of an instance that is having dependencies injected 
    * @throws IllegalStateException if there appear to be cyclical dependencies.
    */
   private void register( Class clazz ) throws IllegalStateException
   {
	   if ( cachedDependencies.containsKey( clazz ) )
		   return;
	   Set<Class> inProgress = cycleDetect.get();
	   if ( inProgress == null )
	   {
		   inProgress = new HashSet<Class>();
		   cycleDetect.set( inProgress );
	   }
	   if ( inProgress.contains( clazz ) )
	   {
		   throw new IllegalStateException( "Cyclic dependency detected on attempt to inject an instance of " + clazz );
	   }
	   else
		   inProgress.add( clazz );
   }
   
   /**
    * Called on completion of processing an instance to indicate that we have finished injecting dependencies
    * for an instance of this class.
    * @param clazz the class of an instance that has had dependencies injected
    */
   private void unregister( Class clazz )
   {
	   if ( cachedDependencies.containsKey( clazz ) )
		   return;
	   Set<Class> inProgress = cycleDetect.get();
	   if ( inProgress != null )
	      inProgress.remove( clazz );
   }  
}
