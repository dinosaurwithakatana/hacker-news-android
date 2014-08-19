package io.dwak.holohackernews.app.manager;

/**
 * Created by vishnu on 8/18/14.
 */
public interface HackerNewsCallback<T> {
    void onResponse(T response, HackerNewsException exception);
}
