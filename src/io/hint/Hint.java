package io.hint;

import java.io.PrintStream;

public class Hint {
    private String docsUrl;
    private String defaultExceptionMessage;
    private boolean showStackTrace;
    private boolean showHints;

    public Hint(Object object) {
        initViaAnnotation(object);
    }

    public void init() {
        HintExceptionHandler hintExceptionHandler = new HintExceptionHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(hintExceptionHandler);
    }

    private void initViaAnnotation(Object object) {
        HintHandler hintHandler = object.getClass().getAnnotation(HintHandler.class);
        // init properties
        defaultExceptionMessage = hintHandler.defaultExceptionMessage();
        showStackTrace = hintHandler.showStackTrace();
        showHints = hintHandler.showHints();
        docsUrl = hintHandler.docsUrl();
    }

    // programmatic API
    public Hint setDocsUrl(String docsUrl) {
        this.docsUrl = docsUrl;
        return this;
    }

    public Hint setDefaultExceptionMessage(String defaultExceptionMessage) {
        this.defaultExceptionMessage = defaultExceptionMessage;
        return this;
    }

    public Hint setShowStackTrace(boolean showStackTrace) {
        this.showStackTrace = showStackTrace;
        return this;
    }

    public Hint setShowHints(boolean showHints) {
        this.showHints = showHints;
        return this;
    }

    public String getDocsUrl() {
        return docsUrl;
    }

    public String getDefaultExceptionMessage() {
        return defaultExceptionMessage;
    }

    public boolean canShowStackTrace() {
        return showStackTrace;
    }

    public boolean canShowHints() {
        return showHints;
    }

    // common objects

    static class WrappedPrintStream extends PrintStream {

        WrappedPrintStream(PrintStream s) {
            super(s);
        }

        @Override
        public void println(Object o) {
            super.println("stack:\t" + o);
        }
    }
}
