package ${package}.web.annotation;

import java.lang.annotation.*;

/**
 * description 前端权限验证
 *
 * @author Andy
 * @version 1.0
 * @date 05/29/2019 15:12
 */
@Inherited
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebAuthorize {
	String value() default "";
}
