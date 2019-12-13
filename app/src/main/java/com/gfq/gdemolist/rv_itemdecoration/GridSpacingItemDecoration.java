package com.gfq.gdemolist.rv_itemdecoration;


import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * create by 高富强
 * on {2019/11/1} {16:22}
 * desctapion: 设置RecyclerView GridLayoutManager or StaggeredGridLayoutManager spacing
 *
 * 只能竖直方向，水平方向有问题。
 * 使用：
 * GridLayoutManager layoutManager = new GridLayoutManager(this, COLUMN, GridLayoutManager.VERTICAL, false);
 * mGridRv.setLayoutManager(layoutManager);
 * mGridRv.addItemDecoration(new GridSpacingItemDecoration(COLUMN, getResources().getDimensionPixelSize(R.dimen.padding_middle), true));
 * mGridRv.setHasFixedSize(true);
 * mRvGridAdapter = new RvGridAdapter(this);
 * mGridRv.setAdapter(mRvGridAdapter);
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;//列数
    private int spacing;//间距
    private boolean includeEdge;//边缘是否有间距

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }

}
