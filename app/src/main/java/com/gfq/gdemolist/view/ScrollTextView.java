package com.gfq.gdemolist.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.gfq.gdemolist.R;
import com.gfq.gdemolist.databinding.ScrollTextViewBinding;


/**
 * create by 高富强
 * on {2019/11/29} {16:40}
 * desctapion:
 */

//  binding.scotext.getTextView().setText("xxxxxXXXxxxxxx");
//          binding.scotext.getTextView().setTextColor(Color.WHITE);
//          binding.scotext.getTextView().setTextSize(30);
//          binding.scotext.post(new Runnable() {
//@Override
//public void run() {
//        width = binding.scotext.getTextView().getWidth();
//        binding.scotext.setRight2Left(5000,width);
//        }
//        });
public class ScrollTextView extends FrameLayout {
    private int screenW;
    private Context context;
    private ScrollTextViewBinding binding;

    private boolean isInit;
    private int textW;
    public ScrollTextView(Context context) {
        this(context, null);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public ScrollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initThis();
    }

    private void initThis() {
        View view = LayoutInflater.from(context).inflate(R.layout.scroll_text_view, null, false);
        binding = DataBindingUtil.bind(view);
        assert binding != null;
        addView(binding.getRoot());
        screenW = getResources().getDisplayMetrics().widthPixels;


    }

    public TextView getTextView() {
        return binding.textView;
    }

    public void setRight2Left(long time,int w) {
        Animation animation = new TranslateAnimation(screenW,-w,0,0);
        animation.setDuration(time);
        animation.setRepeatCount(-1);
        animation.setFillAfter(true);
        animation.setInterpolator(new LinearInterpolator());
        animation.start();
        binding.textView.startAnimation(animation);
    }


}
