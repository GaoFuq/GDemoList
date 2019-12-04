package com.gfq.gdemolist.rv_itemdecoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * create by 高富强
 * on {2019/12/2} {11:54}
 * desctapion:
 */
public class RVItemDecoration_1 extends RecyclerView.ItemDecoration {
    private Paint paint;

    public RVItemDecoration_1(int color) {
        this.paint = new Paint();
        paint.setColor(color);
    }

    public void setPaintColor(int color) {
        this.paint .setColor(color);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            c.drawRect(0,child.getBottom(),child.getRight(),child.getBottom()+10,paint);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.set(0, 0, 0, 10);
    }
}
