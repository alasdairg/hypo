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
import java.lang.reflect.Method;

/**
 * Basic implementation of DependencyFactory that knows how to create
 * Dependency objects for Fields and Simple Setter Methods only.
 */
public class DefaultDependencyFactory implements DependencyFactory
{
   public Dependency createDependency( Object obj, String name )
   {
      if ( obj instanceof Method )
      {
         Method m = (Method) obj;
         return new SimpleSetterDependency( m, name );
      }
      else if ( obj instanceof Field )
      {
         Field f = (Field) obj;
         return new FieldDependency( f, name );
      }
      else
         throw new IllegalArgumentException( "This implementation only supports fields and simple setter methods" );
   }

}
