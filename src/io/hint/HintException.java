package io.hint;

public class HintException extends Exception {
    private String customErrorMsg;
    private String hintsMsg;

    public HintException(Throwable cause, String hintsMsg) {
        this(cause, hintsMsg, "");
    }

    public HintException(Throwable cause, String hintsMsg, String customErrorMsg) {
        super(cause);
        this.hintsMsg = hintsMsg;
        this.customErrorMsg = customErrorMsg;
    }

    public String getCustomErrorMsg() {
        return customErrorMsg;
    }

    public String getHintsMsg() {
        return hintsMsg;
    }

}