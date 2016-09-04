package org.sapia.tad.help;

import org.sapia.tad.util.Settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used on a method parameter to indicate to which settings
 * doc it points.
 * 
 * @author yduchesne
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER})
public @interface SettingsDoc {

  /**
   * @return the name of the static field corresponding to a settings
   * declaration.
   * 
   * @see Settings
   */
  String value();
}
