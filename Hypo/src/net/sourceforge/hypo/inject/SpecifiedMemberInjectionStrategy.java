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

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.hypo.inject.dependency.Dependency;


/**
 * An InjectionStrategy that holds a list of fully qualified member names, and 
 * performs injection on any of those members which belong to the candidate instance's
 * class or superclasses
 */
public class SpecifiedMemberInjectionStrategy extends AbstractInjectionStrategy
{
   List<Dependency> specifiedMembers;   
   
   /**
    * @param obj the candidate Object
    * @return a List of Dependencies corresponding to all members of obj that
    * match the ones in the members list of this SpecifiedMemberInjectionStrategy.
    * NB this includes members which are declared anywhere in the inheritance hierarchy
    * of the candidate's class
    */
   public List<Dependency> selectDependencies( Class clazz )
   {
      List<Dependency> applicableMembers = Collections.EMPTY_LIST;
      for ( Dependency im: specifiedMembers )
      {
         Member member = im.getMember();
         if ( member != null && member.getDeclaringClass().isAssignableFrom( clazz ) )
         {
        	if ( applicableMembers.equals( Collections.EMPTY_LIST ) )
        		applicableMembers = new ArrayList<Dependency>();
            applicableMembers.add( im );
         }
      }
      
      return applicableMembers;
   }

   /**
    * Set the list of specified members that this SpecifiedMemberInjectionStrategy
    * should treat as dependencies. This is a list of Strings of the form
    * <fully-qualified-classname>.setFoo() (denoting a simple setter method for "foo") or
    * <fully-qualified-classname>.foo (denoting a field called "foo")
    * Note that the members specified may have any access modifier
    * @param list a List of Strings as described
    */
   public void setMembers( List<String> list )
   {
      specifiedMembers = new ArrayList<Dependency>();
      for ( String str: list )
      {
         Member member = null;
         int index = str.lastIndexOf( '.' );
         String className = str.substring( 0, index );
         String memberName = str.substring( index + 1 );
         Class clazz = createClass( className );
         if ( memberName.endsWith( "()" ) )
         {
            memberName = memberName.substring( 0, memberName.lastIndexOf( "()" )  );
            member = findSimpleSetterMethod( clazz, memberName );
         }
         else
         {
            member = findField( clazz, memberName );
         }
         specifiedMembers.add( createDependency( member, "" ) );
      }
   }
   
   private static Field findField( Class clazz, String name )
   {
      try
      {
         return clazz.getDeclaredField( name );
      }
      catch( Exception e )
      {
         throw new RuntimeException( "Could not find field " + name + " on class " + clazz );
      }
   }
   
   private static Method findSimpleSetterMethod( Class clazz, String name )
   {
      Method[] methods = clazz.getDeclaredMethods();
      for ( Method method: methods )
      {
         if ( name.startsWith( "set" ) &&
              method.getName().equals( name ) &&
              method.getParameterTypes().length == 1 )
         {
            return method;
         }
      }
      throw new RuntimeException( "Could not find simple setter method " + name + " on class " + clazz );
   }
   
   private static Class createClass( String name )
   {
      Class retval = null;
      try
      {
         retval = Class.forName( name );
      }
      catch( Exception e )
      {
         throw new RuntimeException( e );
      }   
      return retval;
   }
}
