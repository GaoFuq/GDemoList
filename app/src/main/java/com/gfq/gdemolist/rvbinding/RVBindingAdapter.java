package com.gfq.gdemolist.rvbinding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 作者 : 高富强
 * 2019/8/13  19:23
 * 作用 :
 */
public abstract class RVBindingAdapter<T> extends RecyclerView.Adapter<SuperBindingViewHolder> {


    protected Context mContext;
    private LayoutInflater mInflater;
    protected List<T> mDataList = new ArrayList<>();//数据集合
    private int BR_id;//数据的实体类在BR文件中的id。在绑定到xml中后，在**！项目编译后 ！**会自动生成这个BR文件。

    public RVBindingAdapter(Context context, int br_id) {//构造方法传入上下文 和 BR_id
        mContext = context;
        BR_id = br_id;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public SuperBindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(getLayoutId(), parent, false);
        return new SuperBindingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SuperBindingViewHolder holder, final int position) {
        //把数据实体类的信息传递给xml文件，同时把item在recycleView中对应位置的数据传过去
        holder.getBinding().setVariable(BR_id, mDataList.get(position));
        //立即执行绑定
        holder.getBinding().executePendingBindings();
        setPresentor(holder, position);
    }

    //设置item里面的view的事件
    public abstract void setPresentor(SuperBindingViewHolder holder, int position);

    //设置item布局文件id
    public abstract int getLayoutId();

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public void setDataList(Collection<T> list) {
        this.mDataList.clear();
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(Collection<T> list) {
        int lastIndex = this.mDataList.size();
        if (this.mDataList.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    public void remove(int position) {
        this.mDataList.remove(position);
        notifyItemRemoved(position);

        if (position != (getDataList().size())) { // 如果移除的是最后一个，忽略
            notifyItemRangeChanged(position, this.mDataList.size() - position);
        }
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }
}