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

/**
 * <p>
 * Use the annotation {@code @Hint} to configure your application to use {@code HintExceptionHandler}
 * when throwing Runtime exceptions.
 * </p>
 *
 * <p>Use options specified by this annotation to configure your final output.</p>
 *
 * <p><b>Note: </b>configuration via annotation can be overridden by programmatic API ({@code Hint} static methods)</p>
 *
 * @author tahabasri
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Hint {
    // flags

    /**
     * Shows or hides stacktrace in final output
     *
     * @return show stacktrace
     */
    boolean showStackTrace() default false;

    /**
     * Shows or hides hints messages in final output
     *
     * @return hints
     */
    boolean showHints() default true;

    // default messages

    /**
     * Sets default message for exceptions without custom error message
     *
     * @return default exception message
     */
    String defaultExceptionMessage() default "Application failed with exception : ";

    /**
     * Sets default message for notes about documentations
     *
     * @return default docs message
     */
    String defaultDocsMessage() default "See the docs for details : ";

    /**
     * Sets default exit code to be used by your program when an uncaught exception gets thrown
     *
     * @return default exit code
     */
    int defaultExitCode() default 1;

    // prefixes

    /**
     * Sets default prefix to be used for each line in hints messages
     *
     * @return hint prefix
     */
    String hintPrefix() default "\u2705 hints:";

    /**
     * Sets default prefix to be used for each line in error messages
     *
     * @return error prefix
     */
    String errorPrefix() default "\u274C error:";

    /**
     * Sets default prefix to be used for each line in stacktrace
     *
     * @return stack prefix
     */
    String stackPrefix() default "\u26D4 stack:";

    /**
     * Sets default prefix to be used for each line in usage messages (docs)
     *
     * @return docs prefix
     */
    String docsPrefix() default "\u2754 usage:";

    // separators

    /**
     * <p>Sets default separator to be used before showing documentation message.</p>
     *
     * <p>By default, documentation is shown as follows:</p>
     * <pre>
     * ❔ usage: ---
     * ❔ usage: See the docs for details : URL
     * </pre>
     *
     * @return default docs separator
     */
    String defaultDocsSeparator() default "---";

    /**
     * Sets default separator to be used between each token in final output
     *
     * @return default separator
     */
    String defaultSeparator() default "\t";

    /**
     * Sets your global documentation url, if unset, documentation help message won't show up on your final output
     *
     * @return docs URL
     */
    // misc
    String docsUrl() default "";
}