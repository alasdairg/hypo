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

public class Utils
{
   /**
    * Get a unique name for an arbitrary Object without causing any constructors to get
    * called ....
    * @param obj an Object
    * @return a unique name for display purposes
    */
   public static String getName( Object obj )
   {
      return obj.getClass().getName() + '@' + Integer.toHexString( System.identityHashCode( obj ) );
   }
}
