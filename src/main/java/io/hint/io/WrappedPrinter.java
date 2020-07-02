package io.hint.io;

/**
 * Wrapper of default PrintStream or PrintWriter, to be used for setting a prefix before each line in stacktrace
 */
public abstract class WrappedPrinter {
    protected final String stackPrefix;
    protected final String separator;

    public WrappedPrinter(String stackPrefix, String separator) {
        this.stackPrefix = stackPrefix;
        this.separator = separator;
    }

    public abstract void println();

    public abstract void println(Object o);

}
