package com.gfq.gdemolist.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.gfq.gdemolist.R;

/**
 * create by 高富强
 * on {2019/9/18} {16:29}
 * desctapion:
 */
public abstract class BaseFragment<a extends ViewDataBinding> extends Fragment {
    protected a binding;

    protected abstract int layout();

    protected abstract void main();

    protected NavController controller;
    protected TextView title;
    private TextView titleRight;
    protected ImageView ivBack;
    protected ImageView ivTitleImg;
    private ImageView ivTitleRight;
    private RelativeLayout rlTitleBar;


    protected void getArgs() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        FrameLayout content = view.findViewById(R.id.container);
        binding = DataBindingUtil.inflate(inflater, layout(), content, false);
        content.addView(binding.getRoot());
        if (getArguments() != null) {
            getArgs();
        }
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);
        controller = NavHostFragment.findNavController(this);
        main();
    }


    public void navigate(int id) {
        if (controller != null)
            controller.navigate(id);
    }


    protected void toast(String msg) {
        if (getActivity() != null)
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void logd(String msg) {
        Log.d(getClass().getSimpleName(), msg);
    }


    /**
     * D/Login: onAttach
     * D/Login: onCreate
     * D/Login: onCreateView
     * D/Login: onViewCreated
     * D/Login: onActivityCreated
     * D/Login: onResume
     *
     *
     * D/Login: onPause
     * D/Login: onDestroyView
     *
     *
     * D/Login: onCreateView
     * D/Login: onViewCreated
     * D/Login: onActivityCreated
     * D/Login: onResume
     */
}
