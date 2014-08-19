package io.dwak.holohackernews.app.manager;

/**
 * Created by vishnu on 8/18/14.
 */
public class HackerNewsException extends RuntimeException{
    public HackerNewsException() {
    }

    public HackerNewsException(String detailMessage) {
        super(detailMessage);
    }

    public HackerNewsException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public HackerNewsException(Throwable throwable) {
        super(throwable);
    }
}
