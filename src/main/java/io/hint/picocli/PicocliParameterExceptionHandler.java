package io.hint.picocli;

import io.hint.HintCommand;
import io.hint.HintExceptionHandler;
import picocli.CommandLine;
import java.io.PrintWriter;

public class PicocliParameterExceptionHandler implements CommandLine.IParameterExceptionHandler {
    private HintCommand ht;

    public PicocliParameterExceptionHandler(HintCommand ht) {
        this.ht = ht;
    }

    @Override
    public int handleParseException(CommandLine.ParameterException ex, String[] args) {
        CommandLine cmd = ex.getCommandLine();
        PrintWriter writer = cmd.getErr();

        HintExceptionHandler hintExceptionHandler = new HintExceptionHandler(ht);
        hintExceptionHandler.setPrintWriter(writer);
        hintExceptionHandler.uncaughtException(Thread.currentThread(), ex);

        CommandLine.UnmatchedArgumentException.printSuggestions(ex, writer);

        CommandLine.Model.CommandSpec spec = cmd.getCommandSpec();

        return cmd.getExitCodeExceptionMapper() != null
                ? cmd.getExitCodeExceptionMapper().getExitCode(ex)
                : spec.exitCodeOnInvalidInput();
    }
}
