package ${package}.web.pasm;

import java.lang.annotation.*;

/**
 * Created by chj
 */
@Inherited
@Documented
@Target({ElementType.METHOD,ElementType.TYPE,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UnPageAuthorize {
}
