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

    public HintCommand(Object object) {
        Hint hint = object.getClass().getAnnotation(Hint.class);
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

    public void init() {
        Thread.setDefaultUncaughtExceptionHandler(new HintExceptionHandler(this));
    }

    // programmatic API

    private String getSafeValue(String o) {
        return o == null ? "" : o;
    }

    // flags
    public HintCommand showStackTrace(boolean showStackTrace) {
        this.showStackTrace = showStackTrace;
        return this;
    }

    public HintCommand showHints(boolean showHints) {
        this.showHints = showHints;
        return this;
    }

    // default messages

    public HintCommand defaultExceptionMessage(String defaultExceptionMessage) {
        this.defaultExceptionMessage = getSafeValue(defaultExceptionMessage);
        return this;
    }

    public HintCommand defaultDocsMessage(String defaultDocsMessage) {
        this.defaultDocsMessage = getSafeValue(defaultDocsMessage);
        return this;
    }

    public HintCommand defaultExitCode(int defaultExitCode) {
        this.defaultExitCode = defaultExitCode;
        return this;
    }

    // prefixes
    public HintCommand hintPrefix(String hintPrefix) {
        this.hintPrefix = getSafeValue(hintPrefix);
        return this;
    }

    public HintCommand errorPrefix(String errorPrefix) {
        this.errorPrefix = getSafeValue(errorPrefix);
        return this;
    }

    public HintCommand stackPrefix(String stackPrefix) {
        this.stackPrefix = getSafeValue(stackPrefix);
        return this;
    }

    public HintCommand docsPrefix(String docsPrefix) {
        this.docsPrefix = getSafeValue(docsPrefix);
        return this;
    }

    // separators
    public HintCommand defaultSeparator(String defaultSeparator) {
        this.defaultSeparator = getSafeValue(defaultSeparator);
        return this;
    }

    public HintCommand defaultDocsSeparator(String defaultDocsSeparator) {
        this.defaultDocsSeparator = getSafeValue(defaultDocsSeparator);
        return this;
    }

    // misc
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
