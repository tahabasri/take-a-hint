package io.hint;

import java.io.PrintStream;

public class HintExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Hint hintProperties;

    public HintExceptionHandler(Hint hintProperties) {
        this.hintProperties = hintProperties;
    }

    public void uncaughtException(Thread thread, Throwable e) {
        if (e == null) {
            return;
        }

        String errorMsg;
        String hintsMsg;

        Throwable t;
        if (e instanceof HintException) {
            Throwable cause = e.getCause();
            // 'cause = null' means that the original throwable was thrown by hand,
            // not wrapped inside HintThrowable -> throw it directly
            if (cause == null) {
                t = e;
            } else {
                // throwable was wrapped inside HintThrowable
                // -> replace main throwable cause by the one wrapped
                t = cause;
            }
            // get metadata
            errorMsg = ((HintException) e).getCustomErrorMsg();
            hintsMsg = ((HintException) e).getHintsMsg();
        } else {
            t = e;
            errorMsg = "";
            hintsMsg = "";
        }

        if(errorMsg == null || errorMsg.length() == 0){
            final String prefixThrowableMsg = hintProperties.getDefaultExceptionMessage();
            final boolean validErrMsg = e.getMessage() != null && e.getMessage().length() != 0;
            final String prettyClassType = "[" + e.getClass().getName() + "]";

            errorMsg = String.format(prefixThrowableMsg + "%s",
                    validErrMsg ? (e.getMessage() + " " + prettyClassType) : prettyClassType);
        }

        PrintStream sysErr = System.err;
        final String errorPrefix = "error:\t";
        final String hintsPrefix = "hints:\t";

        sysErr.println("\n" + errorPrefix + errorMsg.replace("\n", "\n" + errorPrefix));

        if (hintProperties.canShowHints() && hintsMsg != null && hintsMsg.length() != 0) {
            sysErr.println("\n" + hintsPrefix + hintsMsg.replace("\n", "\n" + hintsPrefix));
        }

        if (hintProperties.getDocsUrl() != null && hintProperties.getDocsUrl().length() != 0) {
            sysErr.println(hintsPrefix + "---");
            sysErr.println(hintsPrefix + "See the docs for details : " + hintProperties.getDocsUrl());
        }

        if(hintProperties.canShowStackTrace()){
            sysErr.println();
            t.printStackTrace(new Hint.WrappedPrintStream(sysErr));
        }
    }
}