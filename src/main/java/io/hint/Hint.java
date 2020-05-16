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

import io.hint.annotation.HintHandler;

import java.io.PrintStream;

@HintHandler
public class Hint {
    private String defaultExceptionMessage;
    private String defaultDocsMessage;
    private String docsUrl;
    private String hintPrefix;
    private String errorPrefix;
    private String stackPrefix;
    private String docsPrefix;
    private String defaultSeparator;
    private String defaultDocsSeparator;
    private boolean showStackTrace;
    private boolean showHints;
    private int defaultExitCode;

    public Hint(Object object) {
        HintHandler hintHandler = object.getClass().getAnnotation(HintHandler.class);
        // get default annotation values from this class (dummy annotation)
        if (hintHandler == null) {
            hintHandler = this.getClass().getAnnotation(HintHandler.class);
        }
        // init properties
        defaultExceptionMessage = hintHandler.defaultExceptionMessage();
        defaultDocsMessage = hintHandler.defaultDocsMessage();
        showStackTrace = hintHandler.showStackTrace();
        showHints = hintHandler.showHints();
        docsUrl = hintHandler.docsUrl();
        hintPrefix = hintHandler.hintPrefix();
        errorPrefix = hintHandler.errorPrefix();
        stackPrefix = hintHandler.stackPrefix();
        docsPrefix = hintHandler.docsPrefix();
        defaultSeparator = hintHandler.defaultSeparator();
        defaultDocsSeparator = hintHandler.defaultDocsSeparator();
        defaultExitCode = hintHandler.defaultExitCode();
    }

    public void init() {
        Thread.setDefaultUncaughtExceptionHandler(new HintExceptionHandler(this));
    }

    // programmatic API

    public Hint showStackTrace() {
        this.showStackTrace = true;
        return this;
    }

    public Hint showHints() {
        this.showHints = true;
        return this;
    }

    public Hint docsUrl(String docsUrl) {
        this.docsUrl = docsUrl;
        return this;
    }

    public Hint defaultExceptionMessage(String defaultExceptionMessage) {
        this.defaultExceptionMessage = defaultExceptionMessage;
        return this;
    }

    public Hint defaultSeparator(String defaultSeparator) {
        this.defaultSeparator = defaultSeparator;
        return this;
    }

    public Hint defaultDocsMessage(String defaultDocsMessage) {
        this.defaultDocsMessage = defaultDocsMessage;
        return this;
    }

    public Hint defaultDocsSeparator(String defaultDocsSeparator) {
        this.defaultDocsSeparator = defaultDocsSeparator;
        return this;
    }

    //TODO add programmatic methods for all properties

    boolean canShowStackTrace() {
        return showStackTrace;
    }

    boolean canShowHints() {
        return showHints;
    }

    String getDocsUrl() {
        return docsUrl;
    }

    String getDefaultExceptionMessage() {
        return defaultExceptionMessage;
    }

    String getDefaultDocsMessage() {
        return defaultDocsMessage;
    }

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

    String getDefaultSeparator() {
        return defaultSeparator;
    }

    String getDefaultDocsSeparator() {
        return defaultDocsSeparator;
    }

    int getDefaultExitCode() {
        return defaultExitCode;
    }

}
