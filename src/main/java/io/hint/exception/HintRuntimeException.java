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

public class HintRuntimeException extends RuntimeException implements HintThrowable {
    private final HintMsgsBox msgsBox;

    public static HintRuntimeException of(Throwable cause){
        return new HintRuntimeException(cause, "", "");
    }

    public static HintRuntimeException of(Throwable cause, String hintsMsg){
        return new HintRuntimeException(cause, hintsMsg, "");
    }

    public static HintRuntimeException of(Throwable cause, String hintsMsg, String customErrorMsg){
        return new HintRuntimeException(cause, hintsMsg, customErrorMsg);
    }

    private HintRuntimeException(Throwable cause, String hintsMsg, String customErrorMsg) {
        super(cause);
        msgsBox = new HintMsgsBox(hintsMsg, customErrorMsg);
    }

    @Override
    public String getHintsMsg() {
        return msgsBox.getHintsMsg();
    }

    @Override
    public String getCustomErrorMsg() {
        return msgsBox.getCustomErrorMsg();
    }
}