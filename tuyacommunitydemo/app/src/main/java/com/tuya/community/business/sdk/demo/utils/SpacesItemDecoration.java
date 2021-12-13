package com.tuya.community.business.sdk.demo.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int horizontalSpace;
    private int verticalSpace;
    private boolean isOnlyTop;

    public SpacesItemDecoration(int verticalSpace, int horizontalSpace, boolean isOnlyTop) {

        this.verticalSpace = verticalSpace;
        this.horizontalSpace = horizontalSpace;
        this.isOnlyTop = isOnlyTop;

    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        if (isOnlyTop) {
            outRect.top = verticalSpace;
        } else {
            outRect.left = horizontalSpace;
            outRect.right = horizontalSpace;
            outRect.bottom = verticalSpace;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildPosition(view) == 0)
                outRect.top = verticalSpace;
        }
    }
}
