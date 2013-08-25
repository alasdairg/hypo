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

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.hypo.inject.dependency.Dependency;


/**
 * An InjectionStrategy which considers an instance eligible for injection if any of its
 * members (or those of its superclasses) are annotated with an instance of a specified 
 * Annotation class. The name of the annotation class is specified by calling setMemberAnnotationClassName 
 * at config time. In addition, if this annotation supplies
 * a String value(), this value will be bound to the Dependency, and can be used during
 * the injection process (given a DependencyResolver implementation that can deal with names).  
 */
public class AnnotationInjectionStrategy extends AbstractInjectionStrategy
{
   private Class<? extends Annotation> memberAnnotationClass = net.sourceforge.hypo.annotation.Dependency.class;
	
   /**
	* Return a list of Dependencies representing fields and simple setter methods for 
	* this instance which are annotated with an instance of memberAnnotationClass 
	* (see setMemberAnnotationClassName())
	* @param obj An instance to consider for dependency injection
	* @return a list of Dependencies that must be satisfied for this instance
	*/
	public List<Dependency> selectDependencies( Class clazz )
	{
	   List<Dependency> list = Collections.EMPTY_LIST;

	   Method[] methods = clazz.getDeclaredMethods();
	   for ( Method method: methods )
	   {  
	      if ( isMethodEligible( method ) )
	      {
	    	 if ( list.equals( Collections.EMPTY_LIST ) )
	    		 list = new ArrayList<Dependency>();
	         list.add( createDependency( method, getNameForAnnotatedMember( method ) ) );
	      }
	   }
	   Field[] fields = clazz.getDeclaredFields();  
	   for ( Field field: fields )
	   {
	      if ( isFieldEligible( field ) )
	      {
	         if ( list.equals( Collections.EMPTY_LIST ) )
		        list = new ArrayList<Dependency>();
	         list.add( createDependency( field, getNameForAnnotatedMember( field ) ) );
	      }
	   }
	   return list;
	}
	    
   private boolean isMethodEligible( Method method )
   {
      return ( method.getName().startsWith( "set" ) &&
               method.getParameterTypes().length == 1 &&
               method.getAnnotation( memberAnnotationClass ) != null );
   }
   
   private boolean isFieldEligible( Field field )
   {
      return field.getAnnotation( memberAnnotationClass ) != null;
   }
   
   /**
    * Set the name of the Annotation class which is used to mark individual
    * members of a class as dependencies. If not specified, the
    * memberAnnotationClass defaults to be net.sourceforge.hypo.annotation.Dependency
    * @param annClass the full class name of the Annotation
    */
   public void setMemberAnnotationClassName( String annClass )
   {
      memberAnnotationClass = createClass( annClass );
   }
   
   /**
    * Helper method that constructs a Class object from the given fully-qualified name, 
    * @param name the Class name
    * @return A Class object for the given name
    * @throws RuntimeException if the Class object could not be created
    */
   private static Class<? extends Annotation> createClass( String name )
   {
      Class<? extends Annotation> retval = null;
      try
      {
         retval = (Class<? extends Annotation>) Class.forName( name );
      }
      catch( Exception e )
      {
         throw new RuntimeException( e );
      }   
      return retval;
   }
   
   /**
    * @param member a class member that is annotated by an instance of 
    * the memberAnnotationClass
    * @return the default value() for the annotation if there is one. Return the
    * empty String otherwise
    */
   private String getNameForAnnotatedMember( AccessibleObject member )
   {
	  Annotation ann = member.getAnnotation( memberAnnotationClass ); 
      String retval = "";
      if ( ann != null )
      {
         Class clazz = ann.getClass();
         try
         {        	 
            Method meth = clazz.getDeclaredMethod( "value" );
            retval = (String) meth.invoke( ann );
         }
         catch( Exception e )
         {            
         }
      }
      
      return retval;
   }
}
