package com.gfq.gdemolist.fragment.custom_view;

import com.gfq.gdemolist.R;
import com.gfq.gdemolist.base.BaseFragment;
import com.gfq.gdemolist.databinding.FragmentCustomViewHomeBinding;

/**
 * create by 高富强
 * on {2019/12/5} {14:49}
 * desctapion:
 */
public class CustomViewHome extends BaseFragment<FragmentCustomViewHomeBinding> {
    @Override
    protected int layout() {
        return R.layout.fragment_custom_view_home;
    }

    @Override
    protected void main() {
        binding.btnChoice.setOnClickListener(v->navigate(R.id.action_customViewHome_to_choiceListView));
        binding.btnScrollTextView.setOnClickListener(v->navigate(R.id.action_customViewHome_to_scrollText));
        binding.btnXml.setOnClickListener(v->navigate(R.id.action_customViewHome_to_xmlSetDrawable));
        binding.btnAnim.setOnClickListener(v->navigate(R.id.action_customViewHome_to_anim));
    }
}
