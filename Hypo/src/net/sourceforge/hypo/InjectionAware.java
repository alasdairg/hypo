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

package net.sourceforge.hypo;

import net.sourceforge.hypo.inject.dependency.Dependency;

/**
 * Callback interface that may optionally be implemented by an 'injectee'. Allows it to
 * be notified when it is just about to be injected as a dependency into some other object.
 * Details of the target object and the dependency that is being satisified are supplied
 */
public interface InjectionAware
{
	/**
	 * Called by the dependency injector just before an injectee is injected
	 * into an object to satisfy a dependency. This allows the injectee to perform
	 * context-specific configuration if required
	 * @param targetInstance the instance that this object is about to be injected into
	 * @param dep the field or method that the dependency is declared on
	 */
	void beforeInjection( Object targetInstance, Dependency dep  );
}
