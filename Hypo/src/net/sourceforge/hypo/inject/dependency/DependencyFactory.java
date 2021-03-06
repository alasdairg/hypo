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

/**
 * Implementations of this interface know how to create Dependency objects
 * based on the passed-in object
 * @param obj An object representing an underlying dependency
 * @param name A name to be associated with the dependency (optional) 
 */
public interface DependencyFactory 
{
   Dependency createDependency(Object obj, String name );
}
