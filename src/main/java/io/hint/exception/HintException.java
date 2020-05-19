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
 * <p>Checked flavor of Hint throwable, may be used by final program for custom logic.</p>
 *
 * <p>Holds references for hint and custom error messages.</p>
 */
public class HintException extends Exception implements HintThrowable {
    private final HintMsgsBox msgsBox;

    /**
     * Creates {@code HintException} object from a throwable
     * @param cause Throwable object
     * @return instance of HintException with empty hint message and custom error message
     */
    public static HintException of(Throwable cause){
        return new HintException(cause, "", "");
    }

    /**
     * Creates {@code HintException} object from a throwable and a hint message
     * @param cause Throwable object
     * @param hintsMsg hints message string
     * @return instance of HintException with empty custom error message
     */
    public static HintException of(Throwable cause, String hintsMsg){
        return new HintException(cause, hintsMsg, "");
    }

    /**
     * Creates {@code HintException} object from a throwable, hint message and a custom error message
     * @param cause Throwable object
     * @param hintsMsg hints message string
     * @param customErrorMsg custom error message string
     * @return instance of HintException
     */
    public static HintException of(Throwable cause, String hintsMsg, String customErrorMsg){
        return new HintException(cause, hintsMsg, customErrorMsg);
    }

    private HintException(Throwable cause, String hintsMsg, String customErrorMsg) {
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