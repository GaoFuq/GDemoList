package com.gfq.gdemolist;


import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.gfq.gdemolist.activity.BiXinVideoActivity;
import com.gfq.gdemolist.activity.Camera2testActivity;
import com.gfq.gdemolist.activity.CustomViewActivity;
import com.gfq.gdemolist.activity.IntentActivity;
import com.gfq.gdemolist.base.BaseActivity;
import com.gfq.gdemolist.databinding.ActivityMainBinding;
import com.gfq.gdemolist.rv_itemdecoration.RVItemDecoration_1;
import com.gfq.gdemolist.rvbinding.RVBindingAdapter;
import com.gfq.gdemolist.rvbinding.SuperBindingViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private RVBindingAdapter<String> adapter;
    private List<String> titles;
    private int randomColor;
    private RVItemDecoration_1 rvItemDecoration_1;

    @Override
    protected int layout() {
        return R.layout.activity_main;
    }

    @Override
    protected void main() {
        initRV();
        addItems();
    }

    private void addItems() {
        addItem("Intent相关");
        addItem("View相关");
        addItem("改变 RecycleView ItemDecoration 装饰");
        addItem("爬取比心视频播放");
        addItem("Camera2");
    }

    private void onItemClick(int position) {
        switch (position){
            case 0:
                startActivity(new Intent(this, IntentActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, CustomViewActivity.class));
                break;
            case 2:
                randomColor =   getRandomColor();
                rvItemDecoration_1.setPaintColor(randomColor);
                adapter.notifyDataSetChanged();
                break;
            case 3:
                startActivity(new Intent(this, BiXinVideoActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, Camera2testActivity.class));
                break;
        }

    }

    private int getRandomColor(){
        return Color.rgb(new Random().nextInt(255),new Random().nextInt(255),new Random().nextInt(255));
    }
    private void initRV() {
        titles = new ArrayList<>();
        randomColor = getRandomColor();
        rvItemDecoration_1 = new RVItemDecoration_1(randomColor);
        adapter = new RVBindingAdapter<String>(this, BR.data) {
            @Override
            public void setPresentor(SuperBindingViewHolder holder, int position) {
                holder.itemView.setOnClickListener(v -> onItemClick(position));
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_rv_main;
            }
        };
        binding.recycleView.setAdapter(adapter);
        binding.recycleView.addItemDecoration(rvItemDecoration_1);
//        binding.recycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }



    private void addItem(String title) {
        titles.add(title);
        adapter.setDataList(titles);
    }
}
