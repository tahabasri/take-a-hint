package io.hint.picocli;

import io.hint.HintCommand;
import io.hint.HintExceptionHandler;
import picocli.CommandLine;

public class PicocliExecutionExceptionHandler implements CommandLine.IExecutionExceptionHandler {
    private final HintCommand ht;

    public PicocliExecutionExceptionHandler(HintCommand ht) {
        this.ht = ht;
    }

    @Override
    public int handleExecutionException(Exception ex,
                                        CommandLine commandLine,
                                        CommandLine.ParseResult parseResult) {

        HintExceptionHandler hintExceptionHandler = new HintExceptionHandler(ht);
        hintExceptionHandler.setPrintWriter(commandLine.getErr());

        hintExceptionHandler.uncaughtException(Thread.currentThread(), ex);

        return commandLine.getExitCodeExceptionMapper() != null
                ? commandLine.getExitCodeExceptionMapper().getExitCode(ex)
                : commandLine.getCommandSpec().exitCodeOnExecutionException();
    }
}
