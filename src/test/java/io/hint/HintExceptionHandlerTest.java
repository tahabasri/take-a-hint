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

import io.hint.common.NoExitSecurityManager;
import io.hint.exception.HintRuntimeException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class HintExceptionHandlerTest extends HintTest {

    @Test
    void testDisableShowHints() {
        final String errMsg = "Oxygen leak !!!";
        class Spaceship {
            private void goToMars() {
                throw HintRuntimeException.of(new IllegalStateException(errMsg), "A hint message");
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht =
                new HintCommand(spaceShip).showHints(false);
        try {
            spaceShip.goToMars();
        } catch (HintRuntimeException ex) {
            String expectedMsg = "\n" + ht.getErrorPrefix()
                    + ht.getDefaultSeparator()
                    + ht.getDefaultExceptionMessage()
                    + ex.getMessage() + "\n";
            handleException(ht, ex);
            assertEquals(expectedMsg, errContent.toString());
        }
    }

    @Test
    void testShowStackTrace() {
        final String errMsg = "Oxygen leak !!!";
        class Spaceship {
            private void goToMars() {
                throw new IllegalStateException(errMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht =
                new HintCommand(spaceShip).showStackTrace(true);
        try {
            new Spaceship().goToMars();
        } catch (IllegalStateException ex) {
            String expectedMsg = "\n" + ht.getErrorPrefix()
                    + ht.getDefaultSeparator()
                    + ht.getDefaultExceptionMessage()
                    + ex.getMessage() + "\n\n"
                    + ht.getStackPrefix()
                    + ht.getDefaultSeparator()
                    + ex.toString();
            handleException(ht, ex);
            assertTrue(errContent.toString().startsWith(expectedMsg));
        }
    }

    @Test
    void testDefaultExceptionMessage() {
        final String errMsg = "Oxygen leak !!!";
        final String defaultMsg = "This is the default error : ";
        class Spaceship {
            private void goToMars() {
                throw new IllegalStateException(errMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht =
                new HintCommand(spaceShip)
                        .defaultExceptionMessage(defaultMsg);
        try {
            spaceShip.goToMars();
        } catch (IllegalStateException ex) {
            String expectedMsg = "\n" + ht.getErrorPrefix()
                    + ht.getDefaultSeparator()
                    + defaultMsg
                    + ex.getMessage() + "\n";
            handleException(ht, ex);
            assertEquals(expectedMsg, errContent.toString());
        }
    }

    @Test
    void testDefaultDocsMessage() {
        final String errMsg = "Oxygen leak !!!";
        final String docsUrl = "http://github.com";
        final String defaultMsg = "This is the default docs msg : ";
        class Spaceship {
            private void goToMars() {
                throw new IllegalStateException(errMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht =
                new HintCommand(spaceShip)
                        .docsUrl(docsUrl)
                        .defaultDocsMessage(defaultMsg);
        try {
            spaceShip.goToMars();
        } catch (IllegalStateException ex) {
            String expectedMsg = "\n" + ht.getErrorPrefix()
                    + ht.getDefaultSeparator() + ht.getDefaultExceptionMessage() + ex.getMessage()
                    + "\n" + ht.getDocsPrefix() + ht.getDefaultSeparator() + ht.getDefaultDocsSeparator()
                    + "\n" + ht.getDocsPrefix() + ht.getDefaultSeparator() + defaultMsg + docsUrl + "\n";
            handleException(ht, ex);
            assertEquals(expectedMsg, errContent.toString());
        }
    }

    @Test
    void testDefaultExitCode() {
        final String errMsg = "Oxygen leak !!!";
        int exitCode = 2000;
        class Spaceship {
            private void goToMars() {
                throw new IllegalStateException(errMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht =
                new HintCommand(spaceShip)
                        .defaultExitCode(exitCode);

        System.setSecurityManager(new NoExitSecurityManager());
        try {
            spaceShip.goToMars();
        } catch (IllegalStateException ex) {
            try {
                handleException(ht, ex);
            } catch (NoExitSecurityManager.ExitException e) {
                assertEquals(exitCode, e.status);
            }
        }
        System.setSecurityManager(null);
    }

    @Test
    void testHintPrefix() {
        final String errMsg = "Oxygen leak !!!";
        final String hintPrefix = "HPrefix";
        final String hintMsg = "Hint message";
        class Spaceship {
            private void goToMars() {
                throw HintRuntimeException.of(new IllegalStateException(errMsg), hintMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht =
                new HintCommand(spaceShip)
                        .hintPrefix(hintPrefix);
        try {
            spaceShip.goToMars();
        } catch (HintRuntimeException ex) {
            String expectedMsg =
                    "\n" + ht.getErrorPrefix()
                            + ht.getDefaultSeparator() + ht.getDefaultExceptionMessage() + ex.getMessage()
                            + "\n\n" + hintPrefix + ht.getDefaultSeparator() + hintMsg + "\n";
            handleException(ht, ex);
            assertEquals(expectedMsg, errContent.toString());
        }
    }

    @Test
    void testErrorPrefix() {
        final String errMsg = "Oxygen leak !!!";
        final String errorPrefix = "EPrefix";
        class Spaceship {
            private void goToMars() {
                throw new IllegalStateException(errMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht =
                new HintCommand(spaceShip)
                        .errorPrefix(errorPrefix);
        try {
            spaceShip.goToMars();
        } catch (IllegalStateException ex) {
            String expectedMsg =
                    "\n" + errorPrefix + ht.getDefaultSeparator()
                            + ht.getDefaultExceptionMessage() + ex.getMessage() + "\n";
            handleException(ht, ex);
            assertEquals(expectedMsg, errContent.toString());
        }
    }

    @Test
    void testStackPrefix() {
        final String errMsg = "Oxygen leak !!!";
        final String stackPrefix = "SPrefix";
        class Spaceship {
            private void goToMars() {
                throw new IllegalStateException(errMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht =
                new HintCommand(spaceShip)
                        .showStackTrace(true)
                        .stackPrefix(stackPrefix);
        try {
            spaceShip.goToMars();
        } catch (IllegalStateException ex) {
            String expectedMsg = "\n" + ht.getErrorPrefix()
                    + ht.getDefaultSeparator()
                    + ht.getDefaultExceptionMessage()
                    + ex.getMessage() + "\n\n"
                    + stackPrefix
                    + ht.getDefaultSeparator()
                    + ex.toString();
            handleException(ht, ex);
            assertTrue(errContent.toString().startsWith(expectedMsg));
        }
    }

    @Test
    void testDocsPrefix() {
        final String errMsg = "Oxygen leak !!!";
        final String docsPrefix = "DPrefix";
        final String docsUrl = "http://github.com";
        class Spaceship {
            private void goToMars() {
                throw new IllegalStateException(errMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht =
                new HintCommand(spaceShip)
                        .docsUrl(docsUrl)
                        .docsPrefix(docsPrefix);
        try {
            spaceShip.goToMars();
        } catch (IllegalStateException ex) {
            String expectedMsg = "\n" + ht.getErrorPrefix()
                    + ht.getDefaultSeparator() + ht.getDefaultExceptionMessage() + ex.getMessage()
                    + "\n" + ht.getDocsPrefix() + ht.getDefaultSeparator() + ht.getDefaultDocsSeparator()
                    + "\n" + ht.getDocsPrefix() + ht.getDefaultSeparator()
                    + ht.getDefaultDocsMessage() + docsUrl + "\n";
            handleException(ht, ex);
            assertEquals(expectedMsg, errContent.toString());
        }
    }

    @Test
    void testDefaultSeparator() {
        final String errMsg = "Oxygen leak !!!";
        final String defaultSep = "~~~";
        class Spaceship {
            private void goToMars() {
                throw new IllegalStateException(errMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht =
                new HintCommand(spaceShip)
                        .showStackTrace(true)
                        .defaultSeparator(defaultSep);
        try {
            spaceShip.goToMars();
        } catch (IllegalStateException ex) {
            String expectedMsg = "\n" + ht.getErrorPrefix()
                    + defaultSep
                    + ht.getDefaultExceptionMessage()
                    + ex.getMessage() + "\n\n"
                    + ht.getStackPrefix()
                    + defaultSep
                    + ex.toString();
            handleException(ht, ex);
            assertTrue(errContent.toString().startsWith(expectedMsg));
        }
    }

    @Test
    void testDefaultDocsSeparator() {
        final String errMsg = "Oxygen leak !!!";
        final String docsUrl = "http://github.com";
        final String defaultDocsSeparator = "~~~";

        class Spaceship {
            private void goToMars() {
                throw new IllegalStateException(errMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht =
                new HintCommand(spaceShip)
                        .docsUrl(docsUrl)
                        .defaultDocsSeparator(defaultDocsSeparator);
        try {
            spaceShip.goToMars();
        } catch (IllegalStateException ex) {
            String expectedMsg = "\n" + ht.getErrorPrefix()
                    + ht.getDefaultSeparator() + ht.getDefaultExceptionMessage() + ex.getMessage()
                    + "\n" + ht.getDocsPrefix() + ht.getDefaultSeparator() + defaultDocsSeparator
                    + "\n" + ht.getDocsPrefix() + ht.getDefaultSeparator()
                    + ht.getDefaultDocsMessage() + docsUrl + "\n";
            handleException(ht, ex);
            assertEquals(expectedMsg, errContent.toString());
        }
    }

    @Test
    void testDocsUrl() {
        final String errMsg = "Oxygen leak !!!";
        final String docsUrl = "http://github.com";

        class Spaceship {
            private void goToMars() {
                throw new IllegalStateException(errMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht =
                new HintCommand(spaceShip)
                        .docsUrl(docsUrl);
        try {
            spaceShip.goToMars();
        } catch (IllegalStateException ex) {
            String expectedMsg = "\n" + ht.getErrorPrefix()
                    + ht.getDefaultSeparator() + ht.getDefaultExceptionMessage() + ex.getMessage()
                    + "\n" + ht.getDocsPrefix() + ht.getDefaultSeparator() + ht.getDefaultDocsSeparator()
                    + "\n" + ht.getDocsPrefix() + ht.getDefaultSeparator()
                    + ht.getDefaultDocsMessage() + docsUrl + "\n";
            handleException(ht, ex);
            assertEquals(expectedMsg, errContent.toString());
        }
    }
}