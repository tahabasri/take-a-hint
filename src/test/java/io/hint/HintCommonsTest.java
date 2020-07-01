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

import io.hint.exception.HintRuntimeException;
import io.hint.io.WrappedPrintWriter;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

class HintCommonsTest extends HintTest {

    @Test
    void testDefaultBehaviorWithoutHint() {
        final String errMsg = "Oxygen leak !!!";
        class Spaceship {
            private void goToMars() {
                throw new IllegalStateException(errMsg);
            }
        }
        try {
            new Spaceship().goToMars();
        } catch (IllegalStateException ex) {
            assertEquals(errMsg, ex.getMessage());
        }
    }

    @Test
    void testWrappedPrintWriter() {
        WrappedPrintWriter outPrinter = new WrappedPrintWriter("", "",
                new PrintWriter(System.err, true));
        String msg = "here";
        outPrinter.println();
        outPrinter.println(msg);
        Assumptions.assumeTrue(outPrinter.getWrappingPrintWriter()!=null);
        assertEquals("\n" + msg + "\n", errContent.toString());
    }

    @Test
    void testDefaultBehaviorWithHintDefaultConstructor() {
        final String errMsg = "Oxygen leak !!!";
        class Spaceship {
            private void goToMars() {
                throw new IllegalStateException(errMsg);
            }
        }
        HintCommand ht = new HintCommand();
        try {
            new Spaceship().goToMars();
        } catch (IllegalStateException ex) {
            final String expectedMsg = "\n" + ht.getErrorPrefix()
                    + ht.getDefaultSeparator()
                    + ht.getDefaultExceptionMessage()
                    + ex.getMessage() + "\n";
            handleException(ht, ex);
            assertEquals(expectedMsg, errContent.toString());
        }
    }

    @Test
    void testDefaultBehaviorWithHint() {
        final String errMsg = "Oxygen leak !!!";
        class Spaceship {
            private void goToMars() {
                throw new IllegalStateException(errMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht = new HintCommand(spaceShip);
        try {
            spaceShip.goToMars();
        } catch (IllegalStateException ex) {
            final String expectedMsg = "\n" + ht.getErrorPrefix()
                    + ht.getDefaultSeparator()
                    + ht.getDefaultExceptionMessage()
                    + ex.getMessage() + "\n";
            handleException(ht, ex);
            assertEquals(expectedMsg, errContent.toString());
        }
    }

    @Test
    void testCustomErrorPrefix() {
        final String errorMsg = "Error message";
        class Spaceship {
            private void goToMars() {
                throw HintRuntimeException.of(new IllegalStateException("Oxygen leak !!!"), "", errorMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht =
                new HintCommand(spaceShip)
                        .showHints(false);
        try {
            spaceShip.goToMars();
        } catch (HintRuntimeException ex) {
            String expectedMsg =
                    "\n" + ht.getErrorPrefix()
                            + ht.getDefaultSeparator() + errorMsg + "\n";
            handleException(ht, ex);
            assertEquals(expectedMsg, errContent.toString());
        }
    }

    @Test
    void getSafeValue() {
        class Spaceship {
        }
        HintCommand ht = new HintCommand(new Spaceship()).defaultSeparator(null);
        assertNotNull(ht.getDefaultSeparator());
    }

    @Test
    void isBlank() {
        assertTrue(HintExceptionHandler.isBlank(null));
        assertTrue(HintExceptionHandler.isBlank(""));
        assertTrue(HintExceptionHandler.isBlank(" "));
        assertFalse(HintExceptionHandler.isBlank("HINT"));
    }
}