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

import io.hint.annotation.Hint;
import io.hint.annotation.HintMessage;
import io.hint.common.NoExitSecurityManager;
import io.hint.exception.HintRuntimeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HintExceptionHandlerWithAnnotationsTest extends HintTest {
    @Test
    void testDefaultBehaviorWithHint() {
        final String errMsg = "Oxygen leak !!!";
        @Hint
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
            String expectedMsg = "\n" + ht.getErrorPrefix()
                    + ht.getDefaultSeparator()
                    + ht.getDefaultExceptionMessage()
                    + ex.getMessage() + "\n";
            handleException(ht, ex);
            assertEquals(expectedMsg, errContent.toString());
        }
    }

    @Test
    void testDisableShowHints() {
        final String errMsg = "Oxygen leak !!!";

        @Hint(showHints = false)
        class Spaceship {
            private void goToMars() {
                throw HintRuntimeException.of(new IllegalStateException(errMsg), "A hint message");
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht = new HintCommand(spaceShip);
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

        @Hint(showStackTrace = true)
        class Spaceship {
            private void goToMars() {
                throw new IllegalStateException(errMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht = new HintCommand(spaceShip);
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

        @Hint(defaultExceptionMessage = defaultMsg)
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

        @Hint(docsUrl = docsUrl, defaultDocsMessage = defaultMsg)
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
        final int exitCode = 2000;

        @Hint(defaultExitCode = exitCode)
        class Spaceship {
            private void goToMars() {
                throw new IllegalStateException(errMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht = new HintCommand(spaceShip);

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

        @Hint(hintPrefix = hintPrefix)
        class Spaceship {
            private void goToMars() {
                throw HintRuntimeException.of(new IllegalStateException(errMsg), hintMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht = new HintCommand(spaceShip);
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

        @Hint(errorPrefix = errorPrefix)
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

        @Hint(showStackTrace = true, stackPrefix = stackPrefix)
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

        @Hint(docsUrl = docsUrl, docsPrefix = docsPrefix)
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

        @Hint(showStackTrace = true, defaultSeparator = defaultSep)
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

        @Hint(docsUrl = docsUrl, defaultDocsSeparator = defaultDocsSeparator)
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

        @Hint(docsUrl = docsUrl)
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
    void testDefaultHintMessageWithMethodAnnotation() {
        final String errMsg = "Oxygen leak !!!";
        final String hintMsg = "Default hint message";

        @Hint
        class Spaceship {
            @HintMessage(hintMsg)
            private void goToMars() {
                throw new IllegalStateException(errMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht = new HintCommand(spaceShip);
        try {
            spaceShip.goToMars();
        } catch (IllegalStateException ex) {
            String expectedMsg =
                    "\n" + ht.getErrorPrefix()
                            + ht.getDefaultSeparator() + ht.getDefaultExceptionMessage() + ex.getMessage()
                            + "\n\n" + ht.getHintPrefix() + ht.getDefaultSeparator() + hintMsg + "\n";
            handleException(ht, ex);
            assertEquals(expectedMsg, errContent.toString());
        }
    }

    @Test
    void testDefaultHintMessageWithTypeAnnotation() {
        final String errMsg = "Oxygen leak !!!";
        final String hintMsg = "Default hint message";

        @Hint
        @HintMessage(hintMsg)
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
            String expectedMsg =
                    "\n" + ht.getErrorPrefix()
                            + ht.getDefaultSeparator() + ht.getDefaultExceptionMessage() + ex.getMessage()
                            + "\n\n" + ht.getHintPrefix() + ht.getDefaultSeparator() + hintMsg + "\n";
            handleException(ht, ex);
            assertEquals(expectedMsg, errContent.toString());
        }
    }

    @Test
    void testDefaultHintMessageWithTypeAndMethodAnnotations() {
        final String errMsg = "Oxygen leak !!!";
        final String hintMsg = "Default hint message";

        @Hint
        @HintMessage("another Hint Message")
        class Spaceship {
            @HintMessage(hintMsg)
            private void goToMars() {
                throw new IllegalStateException(errMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht = new HintCommand(spaceShip);
        try {
            spaceShip.goToMars();
        } catch (IllegalStateException ex) {
            String expectedMsg =
                    "\n" + ht.getErrorPrefix()
                            + ht.getDefaultSeparator() + ht.getDefaultExceptionMessage() + ex.getMessage()
                            + "\n\n" + ht.getHintPrefix() + ht.getDefaultSeparator() + hintMsg + "\n";
            handleException(ht, ex);
            assertEquals(expectedMsg, errContent.toString());
        }
    }

    @Test
    void testDefaultOverrideAnnotationWithProgrammaticAPI() {
        final String errMsg = "Oxygen leak !!!";
        final String hintMsg = "Check your equipment !";

        final String defaultExceptionMessage = "Default Exception Message : ";
        final String defaultDocsMessage = "Default Docs Message : ";
        final int defaultExitCode = 9000;
        final String hintPrefix = "default hints:";
        final String errorPrefix = "default error:";
        final String stackPrefix = "default stack:";
        final String docsPrefix = "default usage:";
        final String defaultDocsSeparator = "~~~";
        final String defaultSeparator = "\t\t\t";
        final String docsUrl = "http://github.com";

        @Hint(
                showHints = false,
                showStackTrace = true,
                defaultExceptionMessage = "anotherDefaultExceptionMessage",
                defaultDocsMessage = "defaultDocsMessage",
                defaultExitCode = 5000,
                hintPrefix = "anotherHintPrefix",
                errorPrefix = "anotherErrorPrefix",
                stackPrefix = "anotherStackPrefix",
                docsPrefix = "anotherDocsPrefix",
                defaultDocsSeparator = "anotherDefaultDocsSeparator",
                defaultSeparator = "anotherDefaultSeparator",
                docsUrl = "anotherDocsUrl"
        )
        class Spaceship {
            private void goToMars() {
                throw HintRuntimeException.of(new IllegalStateException(errMsg), hintMsg);
            }
        }
        Spaceship spaceShip = new Spaceship();
        HintCommand ht =
                new HintCommand(spaceShip)
                        .showHints(true)
                        .showStackTrace(false)
                        .defaultExceptionMessage(defaultExceptionMessage)
                        .defaultDocsMessage(defaultDocsMessage)
                        .defaultExitCode(defaultExitCode)
                        .hintPrefix(hintPrefix)
                        .errorPrefix(errorPrefix)
                        .stackPrefix(stackPrefix)
                        .docsPrefix(docsPrefix)
                        .defaultDocsSeparator(defaultDocsSeparator)
                        .defaultSeparator(defaultSeparator)
                        .docsUrl(docsUrl);
        System.setSecurityManager(new NoExitSecurityManager());
        try {
            spaceShip.goToMars();
        } catch (HintRuntimeException ex) {
            try {
                handleException(ht, ex);
            } catch (NoExitSecurityManager.ExitException e) {
                assertEquals(defaultExitCode, e.status);

                String expectedMsg =
                        "\n" + errorPrefix + defaultSeparator + defaultExceptionMessage + ex.getMessage() + "\n\n"
                                + hintPrefix + defaultSeparator + hintMsg + "\n"
                                + docsPrefix + defaultSeparator + defaultDocsSeparator + "\n"
                                + docsPrefix + defaultSeparator + defaultDocsMessage + docsUrl + "\n";
                assertEquals(expectedMsg, errContent.toString());
            }
        }
        System.setSecurityManager(null);
    }
}