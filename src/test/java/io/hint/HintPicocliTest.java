package io.hint;

import io.hint.annotation.Hint;
import io.hint.exception.HintRuntimeException;
import io.hint.picocli.PicocliExecutionExceptionHandler;
import io.hint.picocli.PicocliParameterExceptionHandler;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HintPicocliTest extends HintTest {

    @Test
    void testParameterExceptionHandler() {
        @CommandLine.Command
        class Spaceship implements Runnable {
            @Override
            public void run() {

            }
        }
        Spaceship spaceShip = new Spaceship();
        CommandLine cmd = new CommandLine(spaceShip);
        HintCommand ht = new HintCommand(spaceShip);
        cmd.setParameterExceptionHandler(new PicocliParameterExceptionHandler(ht));
        cmd.execute("unwantedParam");

        String expectedMsg = "\n" + ht.getErrorPrefix()
                + ht.getDefaultSeparator() + ht.getDefaultExceptionMessage() + "Unmatched argument at index";
        assertTrue(errContent.toString().startsWith(expectedMsg));
    }

    @Test
    void testExceptionHandler() {
        final String errMsg = "Oxygen leak !!!";
        @CommandLine.Command
        class Spaceship implements Runnable {
            @Override
            public void run() {
                throw new IllegalStateException(errMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        CommandLine cmd = new CommandLine(spaceShip);
        HintCommand ht = new HintCommand(spaceShip);
        cmd.setExecutionExceptionHandler(new PicocliExecutionExceptionHandler(ht));
        cmd.execute();

        System.out.println(errContent.toString());
        String expectedMsg = "\n" + ht.getErrorPrefix()
                + ht.getDefaultSeparator() + ht.getDefaultExceptionMessage() + errMsg;
        assertTrue(errContent.toString().startsWith(expectedMsg));
    }

    @Test
    void testHintExceptionWithPicocli() {
        final String errMsg = "Oxygen leak !!!";
        final String hintMsg = "Check your ship if it's not too late.";
        @CommandLine.Command
        class Spaceship implements Runnable {
            @CommandLine.Parameters
            int grade;

            @Override
            public void run() {
                if (grade > 0) {
                    throw HintRuntimeException.of(new Exception(errMsg), hintMsg);
                }
            }
        }
        Spaceship spaceShip = new Spaceship();
        CommandLine cmd = new CommandLine(spaceShip);
        HintCommand ht = new HintCommand(spaceShip);
        cmd.setExecutionExceptionHandler(new PicocliExecutionExceptionHandler(ht));
        cmd.execute("1");

        String expectedMsg = "\n" + ht.getErrorPrefix()
                + ht.getDefaultSeparator() + ht.getDefaultExceptionMessage()
                + Exception.class.getName() + ": " + errMsg + "\n\n"
                + ht.getHintPrefix() + ht.getDefaultSeparator() + hintMsg;

        assertTrue(errContent.toString().startsWith(expectedMsg));
    }


    @Test
    void testHintExceptionWithPicocliAndAnnotation() {
        final String errMsg = "Oxygen leak !!!";
        final String hintMsg = "Check your ship if it's not too late.";
        @CommandLine.Command
        @Hint(showHints = false)
        class Spaceship implements Runnable {
            @CommandLine.Parameters
            int grade;

            @Override
            public void run() {
                if (grade > 0) {
                    throw HintRuntimeException.of(new Exception(errMsg), hintMsg);
                }
            }
        }
        Spaceship spaceShip = new Spaceship();
        CommandLine cmd = new CommandLine(spaceShip);
        HintCommand ht = new HintCommand(spaceShip);
        cmd.setExecutionExceptionHandler(new PicocliExecutionExceptionHandler(ht));
        cmd.execute("1");

        String expectedMsg = "\n" + ht.getErrorPrefix()
                + ht.getDefaultSeparator() + ht.getDefaultExceptionMessage()
                + Exception.class.getName() + ": " + errMsg + "\n";

        assertTrue(errContent.toString().startsWith(expectedMsg));
    }

}
