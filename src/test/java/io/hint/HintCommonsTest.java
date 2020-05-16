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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HintCommonsTest extends HintTest {

    @Test
    void testDefaultBehaviorWithoutHint() {
        final String errMsg = "Oxygen leak !!!";
        class SpaceShip {
            private void goToMars() {
                throw new IllegalStateException(errMsg);
            }
        }
        try {
            new SpaceShip().goToMars();
        } catch (IllegalStateException ex) {
            assertEquals(errMsg, ex.getMessage());
        }
    }

    @Test
    void testDefaultBehaviorWithHint() {
        final String errMsg = "Oxygen leak !!!";
        class SpaceShip {
            private void goToMars() {
                throw new IllegalStateException(errMsg);
            }
        }
        SpaceShip spaceShip = new SpaceShip();
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
        class SpaceShip {
            private void goToMars() {
                throw HintRuntimeException.of(new IllegalStateException("Oxygen leak !!!"), "", errorMsg);
            }
        }
        SpaceShip spaceShip = new SpaceShip();
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
    void isBlank() {
        assertTrue(HintExceptionHandler.isBlank(null));
        assertTrue(HintExceptionHandler.isBlank(""));
        assertTrue(HintExceptionHandler.isBlank(" "));
        assertFalse(HintExceptionHandler.isBlank("HINT"));
    }
}