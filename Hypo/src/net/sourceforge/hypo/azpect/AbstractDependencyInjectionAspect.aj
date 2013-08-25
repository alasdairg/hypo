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

package net.sourceforge.hypo.azpect;

import org.apache.log4j.Logger;

import net.sourceforge.hypo.DI;
import net.sourceforge.hypo.DependencyInjector;
import net.sourceforge.hypo.inject.InjectionStrategy;
import net.sourceforge.hypo.inject.Utils;

/**
 * Intercepts the creation of new objects to allow injection to be automatically 
 * performed.
 */
public abstract aspect AbstractDependencyInjectionAspect implements DependencyInjector
{
   private Logger log = Logger.getLogger( AbstractDependencyInjectionAspect.class );
   
   private interface DeserializationSupport {};    
   
   declare parents: java.io.Serializable+ 
                            && !java..* 
                            && !org.springframework..* 
                            && !org.aspectj..*
                            && !net.sourceforge.hypo..*
                            && !java.lang.Enum+ 
                            && !java.lang.annotation.Annotation+ 
                            && !hasmethod( * *.readResolve() ) implements DeserializationSupport;

                            
   public Object DeserializationSupport.readResolve() throws java.io.ObjectStreamException
   {
      DI.getDependencyInjector().inject( this );
      return this;
   }   
   
   /** Initialization of obj itself, but not any of its superclasses/superinterfaces
     * Excludes classes within this framework */
   protected pointcut instantiatingCandidateObject( Object obj ):
      initialization( *.new(..) ) && target( obj )
      && if ( !thisJoinPointStaticPart.getSignature().getDeclaringType().isInterface() )
      && !within( net.sourceforge.hypo..* );
   
   /** 
    * Deserialization of obj - we intercept readResolve(), and use introduction to make
    * any class without a readResolve() method implement a trivial one  
    */   
   protected pointcut deserializingCandidateObject( Object obj ):
      ( execution( * *.readResolve() ) ) &&
      this( obj )
      && !within( net.sourceforge.hypo..* );
   
   /** Immediately before obj is initialised, but after initialization 
    * of its superclasses/superinterfaces */
   before( Object obj ): instantiatingCandidateObject( obj )
   { 
	   if ( log.isDebugEnabled() )
         log.debug( "Intercepted creation of candidate object [" + Utils.getName( obj ) + "]." );
      inject( obj, thisJoinPointStaticPart.getSignature().getDeclaringType() );
   }
   
   after( Object obj ): deserializingCandidateObject( obj )
   {
	   if ( log.isDebugEnabled() )
         log.debug( "Intercepted deserialization of candidate object [" + Utils.getName( obj.getClass() ) + "]." );
      inject( obj );
   }
   
   public void inject( Object obj, Class clazz )
   {
      if ( mInitialised )
      { 
         boolean processed = mInjectionStrategy.performInjection( obj, clazz );
         
         String name = Utils.getName( obj );
         if ( processed )
         {
        	if ( log.isDebugEnabled() )
               log.debug( "Successfully injected dependencies into object [" + name + "]." );
         }
         else
         {
        	 if ( log.isDebugEnabled() ) 
               log.debug( "No dependencies were injected into object [" + name + "]." );
         }
      }  
   }
   
   public void inject( Object obj )
   {
      if ( mInitialised )
      { 
         boolean processed = mInjectionStrategy.performInjection( obj );
         
         String name = Utils.getName( obj );
         if ( processed )
            log.debug( "Successfully injected dependencies into object [" + name + "]." );
         else
            log.debug( "No dependencies were injected into object [" + name + "]." );
      }  
   }      
   
   public void setInjectionStrategy( InjectionStrategy strat )
   {
      if ( !mInitialised )         
      {
    	  mInjectionStrategy = strat;
      }
      else
         throw new IllegalStateException( "DependencyInjector has already been initialised." );
   }
   
   public void ready()
   {
	   mInitialised = true;
   }
   
   private InjectionStrategy mInjectionStrategy;
   private boolean mInitialised;
}
