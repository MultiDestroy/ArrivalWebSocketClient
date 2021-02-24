package com.github.multidestroy.api;

public interface APIAdapter {

    void onRequest();

    void onRespond(Object object);

    void fail();

}
