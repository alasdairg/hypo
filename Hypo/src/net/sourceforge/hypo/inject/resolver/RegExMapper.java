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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExMapper
{
   private List<Mapping> mappings;
   
   public RegExMapper( List<String> patterns )
   {
      init( patterns );   
   }
   
   public String getMappedString( Class clazz )
   {
      for ( Mapping mapping: mappings )
      {
         String retval = mapping.getMappedString( clazz );
         if ( retval != null )
            return retval;
      }
      return null;
   }
   
   private void init( List<String> patList )
   {
      mappings = new ArrayList<Mapping>();
      for ( String str: patList )
      {
         mappings.add( new Mapping( str ) );  
      }
   }
   
   private static class Mapping
   {
      private Pattern pattern;
      private String template;
      
      public Mapping( String item )
      {                          
         String[] x = item.split( "=" );
         String patternStr = x[0].trim();
         pattern = Pattern.compile( patternStr );         
         template = x[1].trim();
      }      
      
      public String getMappedString( Class clazz )
      {
         String mapsTo = null;
         String toMatch = clazz.getName();         
         Matcher matcher = pattern.matcher( toMatch );         
         if ( matcher.matches() )
         {
            String[] groups = new String[ matcher.groupCount() ];
            for ( int i = 0; i < groups.length; i++ )
               groups[i] = matcher.group( i + 1 );
            mapsTo = MessageFormat.format( template, groups );            
         }
         return mapsTo;
      }            
   }  
}
