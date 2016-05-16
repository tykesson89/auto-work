package com.lhadalo.oladahl.autowork;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;

/**
 * Created by oladahl on 16-05-15.
 */
public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration{
    private final int mVerticalHeight;

    public VerticalSpaceItemDecoration(int mVerticalHeight){
        this.mVerticalHeight = mVerticalHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        super.getItemOffsets(outRect, itemPosition, parent);

        outRect.bottom = mVerticalHeight;
    }
}
