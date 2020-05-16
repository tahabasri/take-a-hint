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

import io.hint.exception.HintException;
import io.hint.exception.HintRuntimeException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HintExceptionsTest {

    @Test
    void testDefaultUncaughtExceptionHandler() {
        class Spaceship {}
        new HintCommand(new Spaceship()).init();
        Thread.UncaughtExceptionHandler exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        assertTrue(exceptionHandler instanceof HintExceptionHandler);
    }

    @Test
    void testHintExceptionFromThrowable() {
        try {
            throw HintException.of(new IllegalStateException());
        }catch (HintException e){
            assertTrue(e.getCause() instanceof IllegalStateException);
            assertTrue(e.getCustomErrorMsg().isEmpty());
            assertTrue(e.getHintsMsg().isEmpty());
        }
    }

    @Test
    void testHintExceptionFromThrowableAndHint() {
        final String hintMsg = "This is a hint";
        try {
            throw HintException.of(new IllegalStateException(), hintMsg);
        }catch (HintException e){
            assertTrue(e.getCause() instanceof IllegalStateException);
            assertTrue(e.getCustomErrorMsg().isEmpty());
            assertEquals(hintMsg, e.getHintsMsg());
        }
    }

    @Test
    void testHintExceptionFromThrowableAndHintAndError() {
        final String hintMsg = "This is a hint";
        final String errorMsg = "This is an error";
        try {
            throw HintException.of(new IllegalStateException(), hintMsg, errorMsg);
        }catch (HintException e){
            assertTrue(e.getCause() instanceof IllegalStateException);
            assertEquals(hintMsg, e.getHintsMsg());
            assertEquals(errorMsg, e.getCustomErrorMsg());
        }
    }

    @Test
    void testHintRuntimeExceptionFromThrowable() {
        try {
            throw HintRuntimeException.of(new IllegalStateException());
        }catch (HintRuntimeException e){
            assertTrue(e.getCause() instanceof IllegalStateException);
            assertTrue(e.getCustomErrorMsg().isEmpty());
            assertTrue(e.getHintsMsg().isEmpty());
        }
    }

    @Test
    void testHintRuntimeExceptionFromThrowableAndHint() {
        final String hintMsg = "This is a hint";
        try {
            throw HintRuntimeException.of(new IllegalStateException(), hintMsg);
        }catch (HintRuntimeException e){
            assertTrue(e.getCause() instanceof IllegalStateException);
            assertTrue(e.getCustomErrorMsg().isEmpty());
            assertEquals(hintMsg, e.getHintsMsg());
        }
    }

    @Test
    void testHintRuntimeExceptionFromThrowableAndHintAndError() {
        final String hintMsg = "This is a hint";
        final String errorMsg = "This is an error";
        try {
            throw HintRuntimeException.of(new IllegalStateException(), hintMsg, errorMsg);
        }catch (HintRuntimeException e){
            assertTrue(e.getCause() instanceof IllegalStateException);
            assertEquals(hintMsg, e.getHintsMsg());
            assertEquals(errorMsg, e.getCustomErrorMsg());
        }
    }
}