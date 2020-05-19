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
package io.hint.exception;

/**
 * <p>Unchecked flavor of Hint throwable, gets handled by {@code HintExceptionHandler} if thrown.</p>
 *
 * <p>Holds references for hint and custom error messages.</p>
 */
public class HintRuntimeException extends RuntimeException implements HintThrowable {
    private final HintMsgsBox msgsBox;

    /**
     * Creates {@code HintRuntimeException} object from a throwable
     * @param cause Throwable object
     * @return instance of HintRuntimeException with empty hint message and custom error message
     */
    public static HintRuntimeException of(Throwable cause){
        return new HintRuntimeException(cause, "", "");
    }

    /**
     * Creates {@code HintRuntimeException} object from a throwable and a hint message
     * @param cause Throwable object
     * @param hintsMsg hints message string
     * @return instance of HintRuntimeException with empty custom error message
     */
    public static HintRuntimeException of(Throwable cause, String hintsMsg){
        return new HintRuntimeException(cause, hintsMsg, "");
    }

    /**
     * Creates {@code HintRuntimeException} object from a throwable, hint message and a custom error message
     * @param cause Throwable object
     * @param hintsMsg hints message string
     * @param customErrorMsg custom error message string
     * @return instance of HintRuntimeException
     */
    public static HintRuntimeException of(Throwable cause, String hintsMsg, String customErrorMsg){
        return new HintRuntimeException(cause, hintsMsg, customErrorMsg);
    }

    private HintRuntimeException(Throwable cause, String hintsMsg, String customErrorMsg) {
        super(cause);
        msgsBox = new HintMsgsBox(hintsMsg, customErrorMsg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHintsMsg() {
        return msgsBox.getHintsMsg();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCustomErrorMsg() {
        return msgsBox.getCustomErrorMsg();
    }
}