package net.sourceforge.hypo.inject;

public class Utils
{
   public static String getName( Object obj )
   {
      return obj.getClass().getName() + '@' + Integer.toHexString( System.identityHashCode( obj ) );
   }
}
