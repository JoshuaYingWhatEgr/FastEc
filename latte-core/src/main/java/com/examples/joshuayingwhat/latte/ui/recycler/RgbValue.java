package com.examples.joshuayingwhat.latte.ui.recycler;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RgbValue {

    public abstract int red();

    public abstract int green();

    public abstract int blue();

    public static RgbValue creator(int red, int green, int blue) {
        return new AutoValue_RgbValue(red, green, blue);
    }

}
