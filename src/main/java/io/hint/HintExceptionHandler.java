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

import io.hint.annotation.HintMessage;
import io.hint.exception.HintThrowable;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * Custom {@code UncaughtExceptionHandler} to be used by Hint in order to reformat exceptions messages
 * depending on specified configuration.
 */
class HintExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final HintCommand hintCommandProperties;

    HintExceptionHandler(HintCommand hintCommandProperties) {
        this.hintCommandProperties = hintCommandProperties;
    }

    /**
     * Instead of showing plain stacktrace as default behavior,
     * we use this handler to parse configuration and behave depending on the given properties:
     * <ul>
     *     <li>If elements are annotated with {@code HintMessage}, extract values from them and use them as hints</li>
     *     <li>If thrown exception is a supported type of {@code HintThrowable}, use original cause and extract
     *     metadata (hints message and custom error message)</li>
     *     <li>Show final output depending on the values in configuration for :
     *      <ul>
     *          <li>show/hide stacktrace</li>
     *          <li>show/hide hints</li>
     *          <li>sets default message for exception with no custom error message</li>
     *          <li>set default separator to be used between messages</li>
     *          <li>if enabled, show hint messages using a pre-defined prefix and custom hint message</li>
     *          <li>show error messages using a pre-defined prefix</li>
     *          <li>if enabled, show stacktrace using a pre-defined prefix</li>
     *          <li>if a docs URL is specified, show documentation help message using pre-defined separator and prefix</li>
     *          <li>if specified, exit application with custom exit code</li>
     *      </ul>
     *     </li>
     * </ul>
     *
     * @param thread thread concerned by the custom exception handler
     * @param e      throwable to be caught during exception handling
     */
    public void uncaughtException(Thread thread, Throwable e) {
        if (e == null) {
            return;
        }

        // populate from method or from class
        String defaultHintMessage = null;

        // experiment with HintMessage annotation
        try {
            if (e.getStackTrace().length != 0) {
                // get origin class from stacktrace
                StackTraceElement originalCause = e.getStackTrace()[0];
                Class<?> clsType = Class.forName(originalCause.getClassName());

                // get origin method from stacktrace
                Method method = Stream.of(clsType.getDeclaredMethods())
                        .filter(m ->
                                originalCause.getMethodName().equals(m.getName())
                                        && m.isAnnotationPresent(HintMessage.class))
                        .findAny().orElse(null);

                // if method is retrieved, retrieve info from annotation (if annotation is present)
                if (method != null) {
                    HintMessage hintMessage = method.getAnnotation(HintMessage.class);
                    if (hintMessage != null) {
                        defaultHintMessage = hintMessage.value();
                    }
                }

                // if no message was retrieved from method, search for global annotation in class
                if (isBlank(defaultHintMessage)) {
                    HintMessage hintMessage = clsType.getAnnotation(HintMessage.class);
                    if (hintMessage != null) {
                        defaultHintMessage = hintMessage.value();
                    }
                }
            }
        } catch (ClassNotFoundException ignored) {
        }

        // get error message from thrown exception
        String errorMsg;
        // get hints message from thrown exception
        String hintsMsg;
        // use this object to refer to original cause (unwrap if thrown exception is instance of HintThrowable)
        Throwable t;

        if (e instanceof HintThrowable) {
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
            // get metadata from parent throwable (wrapper of type HintThrowable)
            errorMsg = ((HintThrowable) e).getCustomErrorMsg();
            hintsMsg = ((HintThrowable) e).getHintsMsg();
        } else {
            // thrown exception is not wrapped inside HintThrowable -> use it directly
            t = e;
            // no custom error messages nor hints are available for Exceptions non wrapped by HintThrowable
            errorMsg = "";
            hintsMsg = "";
        }

        // if no custom error message was retrieved from exception
        // opt for default exception message, if this latter is also non valid,
        // use default global error message
        if (isBlank(errorMsg)) {
            // get value for default message from properties
            final String prefixThrowableMsg = hintCommandProperties.getDefaultExceptionMessage();
            // use either original exception class name or default exception detailMessage as final error message
            errorMsg = String.format(prefixThrowableMsg + "%s",
                    isBlank(e.getMessage()) ? t.getClass().getName() : e.getMessage());
        }

        // use err output as main stream
        PrintStream sysErr = System.err;

        // retrieve prefix properties (prefix value for [error|hints|docs] + default separator)
        final String errorPrefix = hintCommandProperties.getErrorPrefix() + hintCommandProperties.getDefaultSeparator();
        final String hintsPrefix = hintCommandProperties.getHintPrefix() + hintCommandProperties.getDefaultSeparator();
        final String docsPrefix = hintCommandProperties.getDocsPrefix() + hintCommandProperties.getDefaultSeparator();

        // print error message to stream
        sysErr.println(resolveMsg(errorPrefix, errorMsg));

        // show hints on-demand
        if (hintCommandProperties.canShowHints()) {
            // if no hints message was explicitly set (e.g given a non custom exception),
            // opt for default hint message (retrieved using annotations in original method throwing handled exception)
            if (isBlank(hintsMsg)) {
                hintsMsg = defaultHintMessage;
            }
            // show hints if there is a valid value:
            // an explicit message or a default message retrieved from annotations
            if (!isBlank(hintsMsg)) {
                sysErr.println(resolveMsg(hintsPrefix, hintsMsg));
            }
        }

        // if there is a URL for docs, append it as hints message
        if (!isBlank(hintCommandProperties.getDocsUrl())) {
            final String docsMsg =
                    (isBlank(hintCommandProperties.getDefaultDocsSeparator())
                            ? ""
                            : hintCommandProperties.getDefaultDocsSeparator().concat("\n"))
                            .concat(hintCommandProperties.getDefaultDocsMessage().concat(hintCommandProperties.getDocsUrl()));

            // show docs message
            sysErr.println(resolveMsg(docsPrefix, docsMsg, false));
        }

        // show stacktrace on-demand
        if (hintCommandProperties.canShowStackTrace()) {
            sysErr.println();
            // use custom PrintStream to add custom prefix + separator
            t.printStackTrace(
                    new WrappedPrintStream(sysErr,
                            hintCommandProperties.getStackPrefix(),
                            hintCommandProperties.getDefaultSeparator()));
        }

        // change default exit code on-demand
        if (hintCommandProperties.getDefaultExitCode() != 1) {
            System.exit(hintCommandProperties.getDefaultExitCode());
        }
    }

    static boolean isBlank(String value) {
        return value == null || value.length() == 0 || value.trim().length() == 0;
    }

    private String resolveMsg(String prefix, String msg) {
        return resolveMsg(prefix, msg, true);
    }

    private String resolveMsg(String prefix, String msg, boolean startWithLineBreak) {
        final String lineSeparator = "\n";
        return (startWithLineBreak ? lineSeparator : "")
                + prefix + msg.replace(lineSeparator, lineSeparator + prefix);
    }

    /**
     * Wrapper of default PrintStream, to be used for setting a prefix before each line in stacktrace
     */
    static class WrappedPrintStream extends PrintStream {
        private final String stackPrefix;
        private final String separator;

        WrappedPrintStream(PrintStream s, String stackPrefix, String separator) {
            super(s);
            this.stackPrefix = stackPrefix;
            this.separator = separator;
        }

        @Override
        public void println(Object o) {
            super.println(stackPrefix + separator + o);
        }
    }
}