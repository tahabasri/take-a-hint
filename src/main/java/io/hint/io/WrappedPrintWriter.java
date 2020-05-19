package io.hint.io;

import java.io.PrintStream;
import java.io.PrintWriter;

public class WrappedPrintWriter extends WrappedPrinter {
    private final PrintWriter printWriter;
    private final WrappingPrintWriter wrappingPrintWriter;

    public WrappedPrintWriter(String stackPrefix, String separator, PrintWriter printWriter) {
        super(stackPrefix, separator);
        this.printWriter = printWriter;
        wrappingPrintWriter = new WrappingPrintWriter(printWriter, stackPrefix, separator);
    }

    @Override
    public void println() {
        printWriter.println();
    }

    @Override
    public void println(Object o) {
        printWriter.println(o);
    }

    public WrappingPrintWriter getWrappingPrintWriter() {
        return wrappingPrintWriter;
    }

    /**
     * Wrapper of default PrintWriter, to be used for setting a prefix before each line in stacktrace
     */
    public static class WrappingPrintWriter extends PrintWriter {
        private final String stackPrefix;
        private final String separator;

        WrappingPrintWriter(PrintWriter s, String stackPrefix, String separator) {
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
