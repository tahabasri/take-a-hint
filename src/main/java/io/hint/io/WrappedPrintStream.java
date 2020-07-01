package io.hint.io;

import java.io.PrintStream;

public class WrappedPrintStream extends WrappedPrinter {
    private final PrintStream printStream;
    private final WrappingPrintStream wrappingPrintStream;

    public WrappedPrintStream(String stackPrefix, String separator, PrintStream printStream) {
        super(stackPrefix, separator);
        this.printStream = printStream;
        wrappingPrintStream = new WrappingPrintStream(printStream, stackPrefix, separator);
    }

    @Override
    public void println() {
        printStream.println();
    }

    @Override
    public void println(Object o) {
        printStream.println(o);
    }

    public WrappingPrintStream getWrappingPrintStream() {
        return wrappingPrintStream;
    }

    /**
     * Wrapper of default PrintStream, to be used for setting a prefix before each line in stacktrace
     */
    public static class WrappingPrintStream extends PrintStream {
        private final String stackPrefix;
        private final String separator;

        WrappingPrintStream(PrintStream s, String stackPrefix, String separator) {
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
