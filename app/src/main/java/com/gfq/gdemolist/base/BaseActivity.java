package com.gfq.gdemolist.base;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;


/**
 * create by 高富强
 * on {2019/10/16} {9:45}
 * desctapion:
 */
public abstract class BaseActivity<a extends ViewDataBinding> extends AppCompatActivity {
    protected a binding;

    protected abstract int layout();

    protected abstract void main();

    public void logd(String msg) {
        Log.d(getClass().getSimpleName(), msg);
    }

    public void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, layout());
        main();

    }


}
