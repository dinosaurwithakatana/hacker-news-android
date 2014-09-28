package io.dwak.holohackernews.app.manager;

/**
 * Created by vishnu on 8/18/14.
 */
public class Exception extends RuntimeException{
    public Exception() {
    }

    public Exception(String detailMessage) {
        super(detailMessage);
    }

    public Exception(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public Exception(Throwable throwable) {
        super(throwable);
    }
}
