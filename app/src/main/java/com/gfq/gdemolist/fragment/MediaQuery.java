package com.gfq.gdemolist.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.MediaController;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.gfq.gdemolist.R;
import com.gfq.gdemolist.base.BaseFragment;
import com.gfq.gdemolist.databinding.FragmentMediaQueryBinding;

import java.io.File;
import java.util.Calendar;

/**
 * create by 高富强
 * on {2019/12/4} {18:47}
 * desctapion:
 */
public class MediaQuery extends BaseFragment<FragmentMediaQueryBinding> {
    private static final int OPEN_SYS_ALBUM_PICTURE = 0;//打开系统相册（图片）
    private static final int OPEN_SYS_ALBUM_VIDEO = 2;//打开系统相册（视频）
    private static final int OPEN_SYS_CAMERA_TAKE_VIDEO = 1;//打开系统相机(拍视频)
    private static final int OPEN_SYS_CAMERA_TAKE_PICTURE = 3;//打开系统相机(拍照片)
    private String mTempPhotoPath;

    @Override
    protected int layout() {
        return R.layout.fragment_media_query;
    }

    @Override
    protected void main() {
        binding.video.setMediaController(new MediaController(getActivity()));

        binding.openSysAlbumPicture.setOnClickListener(v -> openSysAlbumPicture());
        binding.openSysAlbumVideo.setOnClickListener(v -> openSysAlbumVideo());
        binding.openCustomPicture.setOnClickListener(v -> openCustomPicture());
        binding.openCustomVideo.setOnClickListener(v -> openCustomVideo());
        binding.openSysCameraTakePicture.setOnClickListener(v -> openSysCameraTakePicture());
        binding.openSysCameraTakeVideo.setOnClickListener(v -> openSysCameraTakeVideo());

    }

    private void openSysCameraTakeVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri fileUri = null;
        try {
            fileUri = FileProvider.getUriForFile(getActivity(), "com.gfq.gdemolist.provider", createMediaFile());//这是正确的写法
        } catch (Exception e) {
            e.printStackTrace();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, OPEN_SYS_CAMERA_TAKE_VIDEO);
    }

    private void openSysCameraTakePicture() {
        //方式一
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, OPEN_SYS_CAMERA_TAKE_PICTURE);

        //方式二

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "demoList" + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File photoFile = new File(fileDir, System.currentTimeMillis()+".jpeg");
        mTempPhotoPath = photoFile.getAbsolutePath();
        Uri imageUri=null;
        try{
            imageUri  =FileProvider.getUriForFile(getActivity(),"com.gfq.gdemolist.provider", photoFile);
        }catch (Exception e){
            e.printStackTrace();
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, OPEN_SYS_CAMERA_TAKE_PICTURE);
    }

    private void openCustomVideo() {

    }

    private void openCustomPicture() {

    }

    private void openSysAlbumVideo() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, OPEN_SYS_ALBUM_VIDEO);
    }

    private void openSysAlbumPicture() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_SYS_ALBUM_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        binding.showInfo.setText(mTempPhotoPath);
        Glide.with(getActivity()).load(mTempPhotoPath).into(binding.img);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == OPEN_SYS_ALBUM_PICTURE) {//打开系统相册（图片）
                Uri uri = data.getData();
                if (uri != null) {
                    binding.showInfo.setText(uri.toString());
                    Glide.with(getActivity()).load(uri).into(binding.img);
                }
            } else if (requestCode == OPEN_SYS_ALBUM_VIDEO) {//打开系统相册（视频）
                Uri uri = data.getData();
                if (uri != null) {
                    binding.showInfo.setText(uri.toString());
                    Glide.with(getActivity()).load(uri).into(binding.img);
                    binding.video.setVideoURI(uri);
                    binding.video.start();
                }
            } else if (requestCode == OPEN_SYS_CAMERA_TAKE_PICTURE) {//打开系统相机(拍照片)
                //方式一取数据，感觉有点模糊
//                Bundle extras = data.getExtras();
//                if (extras != null) {
//                    Bitmap bitmap = (Bitmap) extras.get("data");
//                    Glide.with(getActivity()).load(bitmap).into(binding.img);
//                }
                Uri uri = data.getData();
                if (uri != null) {
                    binding.showInfo.setText(uri.toString());
                    Glide.with(getActivity()).load(uri).into(binding.img);
                }

            } else if (requestCode == OPEN_SYS_CAMERA_TAKE_VIDEO) {//打开系统相机(拍视频)
                Uri uri = data.getData();
                if (uri != null) {
                    binding.showInfo.setText(uri.toString());
                    Glide.with(getActivity()).load(uri).into(binding.img);
                    binding.video.setVideoURI(uri);
                    binding.video.start();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.video.stopPlayback();
    }


    private File createMediaFile() {
        if ((Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))) {
            // 选择自己的文件夹
            String path = Environment.getExternalStorageDirectory().getPath() + "/demoList/";
            File mediaStorageDir = new File(path);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.e("TAG", "文件夹创建失败");
                    return null;
                }
            }
            // 文件根据当前的毫秒数给自己命名
            String timeStamp = String.valueOf(System.currentTimeMillis());
            timeStamp = timeStamp.substring(7);
            String imageFileName = "V" + timeStamp;
            String suffix = ".mp4";
            return new File(mediaStorageDir + File.separator + imageFileName + suffix);
        }
        return null;
    }
}
