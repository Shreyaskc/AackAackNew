package com.aackaacknew.pojo;

import com.aackaacknew.activities.R;

public enum ModelObject {

    RED(1, R.layout.screen1_layout),
    BLUE(2, R.layout.screen2_layout),
    GREEN(3, R.layout.screen3_layout);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}