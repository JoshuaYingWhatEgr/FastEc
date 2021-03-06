package com.examples.joshuayingwhat.latte.ui;

import android.content.Context;
import android.util.Log;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import java.util.WeakHashMap;

/**
 * 网络加载加载loading
 *
 * @author joshuayingwhat
 */
final class LoaderCreator {

    private static final WeakHashMap<String, Indicator> LOADING_MAP = new WeakHashMap<>();

    static AVLoadingIndicatorView createorLoading(String type, Context context) {

        final AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);

        if (LOADING_MAP.get(type) == null) {

            final Indicator indicator = getIndicator(type);
            LOADING_MAP.put(type, indicator);
        }
        avLoadingIndicatorView.setIndicator(LOADING_MAP.get(type));
        return avLoadingIndicatorView;
    }

    private static Indicator getIndicator(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        final StringBuilder drawableClassName = new StringBuilder();
        if (!name.contains(".")) {
            final String defaultPackageName = AVLoadingIndicatorView.class.getPackage().getName();
            drawableClassName.append(defaultPackageName).append(".indicators").append(".");
        }
        drawableClassName.append(name);
        Log.d("Tag", drawableClassName.toString());
        try {
            final Class<?> drawableClass = Class.forName(drawableClassName.toString());
            return (Indicator) drawableClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
