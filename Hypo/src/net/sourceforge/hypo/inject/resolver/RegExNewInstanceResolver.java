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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.hypo.inject.dependency.Dependency;


/**
 * A Resolver that matches the type name of the dependency to be injected 
 * against a regular expression, constructs a fully qualified class name 
 * from a template expression (which may itself refer back to groups captured
 * by the RE) and injects a new instance of this class into the Dependency.
 * The named class must have an accessible no-args constructor. The template
 * expression may be prefixed with a "!" to denote that this new instance
 * should be a singleton - i.e. after initial creation, the same instance will
 * be used whenever a dependency of that type is matched by the same mapping 
 */
public class RegExNewInstanceResolver extends AbstractDependencyResolver
{
   
   private RegExMapper mapper;
   private Map<Class<?>,Object> singletons;
   
   public RegExNewInstanceResolver()
   {
      singletons = new HashMap<Class<?>,Object>();
   }
   
   public RegExNewInstanceResolver( String pattern )
   {
      this();
      List<String> list = new ArrayList<String>();
      list.add( pattern );
      setPatternMappings( list );
   }
   
   public RegExNewInstanceResolver( String[] patternMappings )
   {
      this();
      List<String> list = new ArrayList<String>();
      for ( String str: patternMappings )
      {
         list.add( str );
      }
      setPatternMappings( list );
   }
   
   public RegExNewInstanceResolver( List<String> patternMappings )
   {
      this();
      setPatternMappings( patternMappings );
   }
   
   public ResolutionResult doResolve( Dependency dep, Object target )
   {
      boolean singleton = false;;
      String mappedClassName = mapper.getMappedString( dep.getType() );                  
      if ( mappedClassName != null )
      {
         if ( mappedClassName.startsWith( "!" ) )
         {
            mappedClassName = mappedClassName.substring( 1 );
            singleton = true;
         }
      
         Class<?> mappedClass = createClass( mappedClassName );
         Object obj = getInstance( mappedClass, singleton );
         return ResolutionResult.resolved(obj);
      }
      else
         return ResolutionResult.couldNotResolve();
   }

   private Class<?> createClass( String className )
   {
      try
      {
         return Class.forName( className );
      }
      catch( Exception e )
      {
         throw new RuntimeException( e );
      }     
   }
   
   private Object getInstance( Class<?> clazz, boolean singleton )
   {
      Object retval = null;
      if ( singleton )
      {
         retval = singletons.get( clazz );
         if ( retval == null )
         {
            retval = createNewInstance( clazz );
            singletons.put( clazz, retval );            
         }              
      }
      else
         retval = createNewInstance( clazz );
      
      return retval;
   }

   private Object createNewInstance( Class<?> clazz )
   {
      try
      {
         return clazz.newInstance();
      }
      catch( Exception e )
      {
         throw new RuntimeException( e );
      }      
   }
   
   public void setPatternMappings( List<String> mappings )
   {
      mapper = new RegExMapper( mappings );
   }
}
