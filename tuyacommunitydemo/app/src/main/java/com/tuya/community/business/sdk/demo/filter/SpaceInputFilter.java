package com.tuya.community.business.sdk.demo.filter;

import android.text.InputFilter;
import android.text.Spanned;

public class SpaceInputFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (source.equals(" ")) return "";
        else return null;
    }
}
