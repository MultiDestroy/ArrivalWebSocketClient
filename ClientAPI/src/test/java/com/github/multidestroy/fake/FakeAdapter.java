package com.github.multidestroy.fake;

import com.github.multidestroy.api.APIAdapter;

public class FakeAdapter implements APIAdapter {

    private boolean calledRequestFunc = false;
    private boolean calledRespondFunc = false;
    private boolean calledFailFunc = false;

    @Override
    public void onRequest() {
        calledRequestFunc = true;
    }

    @Override
    public void onRespond(Object object) {
        calledRespondFunc = true;
    }

    @Override
    public void fail() {
        calledFailFunc = true;
    }

    public boolean wasRequestFuncExecuted() {
        return calledRequestFunc;
    }

    public boolean wasRespondFuncExecuted() {
        return calledRespondFunc;
    }

    public boolean wasFailFuncExecuted() {
        return calledFailFunc;
    }

}
