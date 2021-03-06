package com.examples.joshuayingwhat.latte.delegates.web.event;

import android.content.Context;
import android.webkit.WebView;

import com.examples.joshuayingwhat.latte.delegates.web.WebDelegate;

public abstract class Event implements IEvent {

    private Context mContext;
    private String mAction;
    private WebDelegate mDelegate;
    private String mUrl;
    private WebView mWebView;

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public String getAction() {
        return mAction;
    }

    public void setAction(String action) {
        mAction = action;
    }

    public WebDelegate getDelegate() {
        return mDelegate;
    }

    public void setDelegate(WebDelegate delegate) {
        mDelegate = delegate;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public WebView getWebView() {
        return mDelegate.getWebView();
    }
}
