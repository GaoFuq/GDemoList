package com.gfq.gdemolist.rvbinding;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ViewHolder基类
 */
public class SuperBindingViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    public  T getBinding() {
        return binding;
    }

    private final T binding;

    public SuperBindingViewHolder(View itemView) {
        //构造方法，传入item布局文件生成的view
        super(itemView);
        //通过DataBindingUtil.bind()方法，使DataBinding绑定布局，并且返回ViewDataBinding的子类对象
        binding = DataBindingUtil.bind(itemView);
    }

}