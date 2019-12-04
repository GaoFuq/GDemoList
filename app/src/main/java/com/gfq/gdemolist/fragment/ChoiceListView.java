package com.gfq.gdemolist.fragment;

import android.util.SparseBooleanArray;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;

import com.gfq.gdemolist.R;
import com.gfq.gdemolist.base.BaseFragment;
import com.gfq.gdemolist.databinding.FragmentChoiceListviewBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * create by 高富强
 * on {2019/12/4} {18:18}
 * desctapion:
 */
public class ChoiceListView extends BaseFragment<FragmentChoiceListviewBinding> {
    private List<String> mData;
    @Override
    protected int layout() {
        return R.layout.fragment_choice_listview;
    }

    @Override
    protected void main() {
        mData = new ArrayList<>();
        for (int i = 0; i <6 ; i++) {
            mData.add("数据"+i);
        }

        binding.btnSingleChoice.setOnClickListener(v->{
            if (getActivity() != null) {
                binding.listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_single_choice);
                adapter.addAll(mData);
                binding.listView.setAdapter(adapter);
            }
        });

        binding.btnMultiChoice.setOnClickListener(v->{
            if (getActivity() != null) {
                binding.listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice);
                adapter.addAll(mData);
                binding.listView.setAdapter(adapter);
            }
        });

        binding.btnConfirm.setOnClickListener(v->{
            SparseBooleanArray checkedItemPositions = binding.listView.getCheckedItemPositions();
            StringBuilder stringBuilder= new StringBuilder();
            for (int i = 0; i < mData.size(); i++) {
                if(checkedItemPositions.get(i)){
                    stringBuilder.append(mData.get(i)).append("-");
                }
            }
            toast(stringBuilder.toString());
        });
    }
}
