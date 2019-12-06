package com.gfq.gdemolist.fragment.custom_view;

import android.graphics.Color;
import android.widget.SeekBar;

import com.gfq.gdemolist.R;
import com.gfq.gdemolist.base.BaseFragment;
import com.gfq.gdemolist.databinding.FragmentScrollTextBinding;
import com.gfq.gdemolist.view.ScrollTextView;

import java.util.Random;

/**
 * create by 高富强
 * on {2019/12/5} {14:53}
 * desctapion:
 */
public class ScrollText extends BaseFragment<FragmentScrollTextBinding> {
    private int index = -1;
    @Override
    protected int layout() {
        return R.layout.fragment_scroll_text;
    }

    @Override
    protected void main() {
        binding.text1.getTextView().setText("大大大大大大大大大大大大大大大大大大大");
        binding.text1.setBackgroundColor(Color.BLACK);
        binding.text1.getTextView().setTextColor(Color.WHITE);
        binding.text1.post(() -> {
            int w =  binding.text1.getTextView().getWidth();
            binding.text1.setRight2Left(1000, w);
        });


        binding.text2.getTextView().setText("大大大大大大大大大大大大大大大大大大大");
        binding.text2.setBackgroundColor(Color.BLACK);
        binding.text2.getTextView().setTextColor(Color.YELLOW);
        binding.text2.post(() -> {
            int w =  binding.text2.getTextView().getWidth();
            binding.text2.setRight2Left(2000, w);
        });


        binding.text3.getTextView().setText("大大大大大大大大大大大大大大大大大大大");
        binding.text3.setBackgroundColor(Color.BLACK);
        binding.text3.getTextView().setTextColor(Color.YELLOW);
        binding.text3.post(() -> {
            int w =  binding.text3.getTextView().getWidth();
            binding.text3.setRight2Left(3000, w);
        });


        binding.text4.getTextView().setText("大大大大大大大大大大大大大大大大大大大");
        binding.text4.setBackgroundColor(Color.BLACK);
        binding.text4.getTextView().setTextColor(Color.YELLOW);
        binding.text4.setBackgroundResource(R.drawable.ic_launcher_background);
        binding.text4.post(() -> {
            int w =  binding.text4.getTextView().getWidth();
            binding.text4.setRight2Left(4000, w);
        });


        binding.text5.getTextView().setText("大大大大大大大大大大大大大大大大大大大");
        binding.text5.setBackgroundColor(Color.BLACK);
        binding.text5.getTextView().setTextColor(Color.YELLOW);
        binding.text5.post(() -> {
            int w =  binding.text5.getTextView().getWidth();
            binding.text5.setRight2Left(5000, w);
        });

        binding.changeBGcolor.setOnClickListener(v->{
            binding.text1.setBackgroundColor(getRandomColor());
            index=1;
        });


        binding.changeTEXTcolor.setOnClickListener(v->{
            binding.text2.getTextView().setTextColor(getRandomColor());
            index=2;
        });

        binding.changeBGTEXTcolor.setOnClickListener(v->{
            binding.text3.getTextView().setBackgroundColor(getRandomColor());
            index=3;
        });

        binding.changeScrollSpeed.setOnClickListener(v->{
            binding.text5.post(() -> {
                int w =  binding.text5.getTextView().getWidth();
                binding.text5.setRight2Left(getRandomSpeed(), w);
                index=4;
            });
        });

        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                switch (index){
                    case 1:
                        binding.text1.setBackgroundColor(Color.rgb(progress,progress,progress));
                        break;
                    case 2:
                        binding.text2.getTextView().setTextColor(Color.rgb(progress,progress,progress));
                        break;
                    case 3:
                        binding.text3.getTextView().setBackgroundColor(Color.rgb(progress,progress,progress));
                        break;
                    case 4:
                        binding.text5.post(() -> {
                            int w =  binding.text5.getTextView().getWidth();
                            binding.text5.setRight2Left(1000+progress*5, w);
                        });
                        break;
                        default:
                            break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private int getRandomSpeed(){
        return new Random().nextInt(100000)+1000;
    }

    private int getRandomColor(){
        return Color.rgb(new Random().nextInt(255),new Random().nextInt(255),new Random().nextInt(255));
    }
}
