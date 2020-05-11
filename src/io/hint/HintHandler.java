package io.hint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HintHandler {
    boolean showHints() default true;
    boolean showStackTrace() default false;
    String defaultExceptionMessage() default "Application failed with exception : ";
    String docsUrl() default "";
}