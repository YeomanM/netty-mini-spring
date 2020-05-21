package com.yeoman.minispring.annitation;

import java.lang.annotation.*;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/5/19
 * @desc
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
    String value() default "";
}
