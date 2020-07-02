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
package io.hint;

import io.hint.annotation.Hint;

/**
 * <p>Initialize this class to use Hint custom exceptionHandler {@link HintExceptionHandler}.</p>
 *
 * <p>Example of simple initialization :</p>
 * <pre>
 *     new HintCommand(new Main()).init();
 * </pre>
 *
 * <p>You can configure your instance using annotation {@link Hint} or programmatic API (static methods)</p>
 * <p><b>Note : </b>Programmatic API overrides annotation configuration</p>
 *
 * <p>If no explicit configuration is provided,
 * the new instance uses the default configuration specified with {@link Hint} properties</p>.
 */
@Hint
public class HintCommand {
    // flags
    private boolean showStackTrace;
    private boolean showHints;
    // default messages
    private String defaultExceptionMessage;
    private String defaultDocsMessage;
    private int defaultExitCode;
    // prefixes
    private String hintPrefix;
    private String errorPrefix;
    private String stackPrefix;
    private String docsPrefix;
    // separators
    private String defaultSeparator;
    private String defaultDocsSeparator;
    // misc
    private String docsUrl;

    /**
     * <p>Constructs an object of HintCommand based on default settings</p>
     *
     * @see #HintCommand(Object)
     */
    public HintCommand() {
        this(null);
    }

    /**
     * <p>Constructs an object of HintCommand based on class annotated with HintCommand</p>
     *
     * <p><b>Note: </b>If the passed object is {@code null} or not annotated with {@code Hint} annotation,
     * default settings will be used.</p>
     *
     * @param object class annotated with {@code Hint} annotation
     */
    public HintCommand(Object object) {
        // checks if there is an annotation on passed object
        Hint hint = null;
        if (object != null) {
            hint = object.getClass().getAnnotation(Hint.class);
        }
        // get default annotation values from this class (dummy annotation)
        if (hint == null) {
            hint = this.getClass().getAnnotation(Hint.class);
        }
        // init properties
        defaultExceptionMessage = hint.defaultExceptionMessage();
        defaultDocsMessage = hint.defaultDocsMessage();
        showStackTrace = hint.showStackTrace();
        showHints = hint.showHints();
        docsUrl = hint.docsUrl();
        hintPrefix = hint.hintPrefix();
        errorPrefix = hint.errorPrefix();
        stackPrefix = hint.stackPrefix();
        docsPrefix = hint.docsPrefix();
        defaultSeparator = hint.defaultSeparator();
        defaultDocsSeparator = hint.defaultDocsSeparator();
        defaultExitCode = hint.defaultExitCode();
    }

    /**
     * <p>Initialize exception handling by setting a custom {@code uncaughtExceptionHandler} to the main thread.</p>
     * <p>
     * This custom exception handler takes care of showing final output for uncaught exceptions using Hint configuration.
     *
     * @throws SecurityException if a security manager is present and it
     *                           denies <tt>{@link RuntimePermission}
     *                           (&quot;setDefaultUncaughtExceptionHandler&quot;)</tt>
     */
    public void init() {
        Thread.setDefaultUncaughtExceptionHandler(new HintExceptionHandler(this));
    }

    // programmatic API

    private String getSafeValue(String o) {
        return o == null ? "" : o;
    }

    // flags

    /**
     * Shows or hides stacktrace in final output
     *
     * @param showStackTrace {@code true} if stacktrace should be shown,
     *                       {@code false} otherwise
     * @return this HintCommand instance, to allow configuration chaining.
     */
    public HintCommand showStackTrace(boolean showStackTrace) {
        this.showStackTrace = showStackTrace;
        return this;
    }

    /**
     * Shows or hides hints messages in final output
     *
     * @param showHints {@code true} if hints should be shown,
     *                  {@code false} otherwise
     * @return this HintCommand instance, to allow configuration chaining.
     */
    public HintCommand showHints(boolean showHints) {
        this.showHints = showHints;
        return this;
    }

    // default messages

    /**
     * Sets default message for exceptions without custom error message
     *
     * @param defaultExceptionMessage default message for exception message
     * @return this HintCommand instance, to allow configuration chaining.
     */
    public HintCommand defaultExceptionMessage(String defaultExceptionMessage) {
        this.defaultExceptionMessage = getSafeValue(defaultExceptionMessage);
        return this;
    }

    /**
     * Sets default message for notes about documentations
     *
     * @param defaultDocsMessage default message for docs
     * @return this HintCommand instance, to allow configuration chaining.
     */
    public HintCommand defaultDocsMessage(String defaultDocsMessage) {
        this.defaultDocsMessage = getSafeValue(defaultDocsMessage);
        return this;
    }

    /**
     * sets default exit code to be used by your program when an uncaught exception gets thrown
     *
     * @param defaultExitCode exit code
     * @return this HintCommand instance, to allow configuration chaining.
     */
    public HintCommand defaultExitCode(int defaultExitCode) {
        this.defaultExitCode = defaultExitCode;
        return this;
    }

    // prefixes

    /**
     * Sets default prefix to be used for each line in hints messages
     *
     * @param hintPrefix hints messages prefix
     * @return this HintCommand instance, to allow configuration chaining.
     */
    public HintCommand hintPrefix(String hintPrefix) {
        this.hintPrefix = getSafeValue(hintPrefix);
        return this;
    }

    /**
     * Sets default prefix to be used for each line in error messages
     *
     * @param errorPrefix error messages prefix
     * @return this HintCommand instance, to allow configuration chaining.
     */
    public HintCommand errorPrefix(String errorPrefix) {
        this.errorPrefix = getSafeValue(errorPrefix);
        return this;
    }

    /**
     * Sets default prefix to be used for each line in stacktrace
     *
     * @param stackPrefix stacktrace prefix
     * @return this HintCommand instance, to allow configuration chaining.
     */
    public HintCommand stackPrefix(String stackPrefix) {
        this.stackPrefix = getSafeValue(stackPrefix);
        return this;
    }

    /**
     * Sets default prefix to be used for each line in usage messages (docs)
     *
     * @param docsPrefix docs prefix
     * @return this HintCommand instance, to allow configuration chaining.
     */
    public HintCommand docsPrefix(String docsPrefix) {
        this.docsPrefix = getSafeValue(docsPrefix);
        return this;
    }

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
     * @param defaultDocsSeparator default separator
     * @return this HintCommand instance, to allow configuration chaining.
     */
    public HintCommand defaultDocsSeparator(String defaultDocsSeparator) {
        this.defaultDocsSeparator = getSafeValue(defaultDocsSeparator);
        return this;
    }

    /**
     * Sets default separator to be used between each token in final output
     *
     * @param defaultSeparator default messages separator
     * @return this HintCommand instance, to allow configuration chaining.
     */
    public HintCommand defaultSeparator(String defaultSeparator) {
        this.defaultSeparator = getSafeValue(defaultSeparator);
        return this;
    }

    // misc

    /**
     * Sets your global documentation url, if unset, documentation help message won't show up on your final output
     *
     * @param docsUrl documentation URL
     * @return this HintCommand instance, to allow configuration chaining.
     */
    public HintCommand docsUrl(String docsUrl) {
        this.docsUrl = getSafeValue(docsUrl);
        return this;
    }

    // package-visible getters

    // flags

    boolean canShowStackTrace() {
        return showStackTrace;
    }

    boolean canShowHints() {
        return showHints;
    }

    // default messages

    String getDefaultExceptionMessage() {
        return defaultExceptionMessage;
    }

    String getDefaultDocsMessage() {
        return defaultDocsMessage;
    }

    int getDefaultExitCode() {
        return defaultExitCode;
    }

    // prefixes

    String getHintPrefix() {
        return hintPrefix;
    }

    String getErrorPrefix() {
        return errorPrefix;
    }

    String getStackPrefix() {
        return stackPrefix;
    }

    String getDocsPrefix() {
        return docsPrefix;
    }

    // separators

    String getDefaultSeparator() {
        return defaultSeparator;
    }

    String getDefaultDocsSeparator() {
        return defaultDocsSeparator;
    }

    // misc

    String getDocsUrl() {
        return docsUrl;
    }
}