/*
   Copyright 2020 Taha BASRI

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package io.hint.annotation;

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

    String defaultDocsMessage() default "See the docs for details : ";

    String docsUrl() default "";

    String hintPrefix() default "\u2705 hints:";

    String errorPrefix() default "\u274C error:";

    String stackPrefix() default "\u26D4 stack:";

    String docsPrefix() default "\u2754 usage:";

    String defaultSeparator() default "\t";

    String defaultDocsSeparator() default "---";

    int defaultExitCode() default 1;
}