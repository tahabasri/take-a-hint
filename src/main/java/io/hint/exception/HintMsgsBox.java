package io.hint.exception;

public class HintMsgsBox {
    private String hintsMsg;
    private String customErrorMsg;

    public HintMsgsBox(String hintsMsg, String customErrorMsg) {
        this.hintsMsg = hintsMsg;
        this.customErrorMsg = customErrorMsg;
    }

    public String getHintsMsg() {
        return hintsMsg;
    }

    public String getCustomErrorMsg() {
        return customErrorMsg;
    }
}
