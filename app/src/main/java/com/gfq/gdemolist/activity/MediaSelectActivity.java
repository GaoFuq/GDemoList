package com.gfq.gdemolist.activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gfq.gdemolist.R;
import com.gfq.gdemolist.base.BaseActivity;
import com.gfq.gdemolist.bean.MediaBean;
import com.gfq.gdemolist.databinding.ActivityVideoSelectBinding;
import com.gfq.gdemolist.rv_itemdecoration.GridSpacingItemDecoration;
import com.gfq.gdemolist.rvbinding.RVBindingAdapter;
import com.gfq.gdemolist.rvbinding.SuperBindingViewHolder;
import com.gfq.gdemolist.util.Permission;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * create by 高富强
 * on {2019/12/13} {10:17}
 * desctapion:  自定义选择图片 或 视频界面
 */
public class MediaSelectActivity extends BaseActivity<ActivityVideoSelectBinding> {
    private RVBindingAdapter<MediaBean> adapter;
    private String selectedPath;
    private boolean isBrowsePicture;
    private String mTempPhotoPath;//拍照结果的地址

    @Override
    protected int layout() {
        return R.layout.activity_video_select;
    }

    @Override
    protected void main() {
        isBrowsePicture = getIntent().getBooleanExtra("isPic", true);
        if (isBrowsePicture) {
            binding.takePictureOrVideo.setText("拍照");
            binding.tvTitle.setText("选择照片");
        } else {
            binding.takePictureOrVideo.setText("录像");
            binding.tvTitle.setText("选择视频");
        }
        binding.ivClose.setOnClickListener(v -> finish());
        binding.takePictureOrVideo.setOnClickListener(v -> takePictureOrVideo());
        initRV();
    }


    boolean hasPermissson = false;

    private void takePictureOrVideo() {
        String[] per = new String[]{Manifest.permission.CAMERA};
        Permission.requestPermissions(this, 4, per, new Permission.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                hasPermissson = true;
            }

            @Override
            public void onPermissionDenied() {
                hasPermissson = false;
            }
        });
        if (hasPermissson) {
            if (isBrowsePicture) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "demoList" + File.separator);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                File photoFile = new File(fileDir, System.currentTimeMillis() + ".jpeg");
                mTempPhotoPath = photoFile.getAbsolutePath();
                Uri imageUri = null;
                try {
                    imageUri = FileProvider.getUriForFile(this, "com.gfq.gdemolist.provider", photoFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 1);
            } else {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                Uri fileUri = null;
                try {
                    fileUri = FileProvider.getUriForFile(MediaSelectActivity.this, "com.gfq.gdemolist.provider", createMediaFile());//这是正确的写法
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    toast("" + uri.toString());
//                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
//                    adapter.setDataList(getLocalMedias());
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private File createMediaFile() {
        if ((Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))) {
            // 选择自己的文件夹
            String path = Environment.getExternalStorageDirectory().getPath() + "/siyu/";
            // Constants.video_url 是一个常量，代表存放视频的文件夹
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
            String suffix;
            if (isBrowsePicture) {
                suffix = ".jpg";
            } else {
                suffix = ".mp4";
            }
            File mediaFile = new File(mediaStorageDir + File.separator + imageFileName + suffix);
            return mediaFile;
        }
        return null;
    }

    private void initRV() {
        adapter = new RVBindingAdapter<MediaBean>(this, com.gfq.gdemolist.BR.data) {
            @Override
            public void setPresentor(SuperBindingViewHolder holder, int position) {
                MediaBean mediaBean = getDataList().get(position);
                ImageView img = holder.itemView.findViewById(R.id.image);
                Glide.with(MediaSelectActivity.this).load(mediaBean.getPath()).apply(RequestOptions.placeholderOf(R.drawable.image_placeholder)).into(img);
                holder.itemView.setOnClickListener(v -> {
                    selectedPath = mediaBean.getPath();
                    toast(selectedPath);
//                    setResult(RESULT_CHOOSE_VIDEO_CODE, new Intent().putExtra("path", selectedVideoPath));
//                    finish();
                });
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_rv_select_video;
            }
        };
        binding.rvVideo.setAdapter(adapter);
        binding.rvVideo.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvVideo.addItemDecoration(new GridSpacingItemDecoration(2, 10, true));
        adapter.setDataList(getLocalMedias());

    }

    private List<MediaBean> getLocalMedias() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        List<MediaBean> list = new ArrayList<>();
          /*  String[] projection = new String[]{  MediaStore.MediaColumns.DATA,
                    MediaStore.MediaColumns.MIME_TYPE,
                    MediaStore.MediaColumns.WIDTH,
                    MediaStore.MediaColumns.HEIGHT,
                    MediaStore.MediaColumns.DURATION,
                    MediaStore.MediaColumns.SIZE,
                    MediaStore.MediaColumns.BUCKET_DISPLAY_NAME};*/
        String[] projection = new String[]{MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.SIZE, MediaStore.MediaColumns.TITLE, MediaStore.MediaColumns.DATE_ADDED};
        Cursor cursor;
        if (isBrowsePicture) {
            cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.ImageColumns.IS_PRIVATE);
        } else {
            cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Video.VideoColumns.IS_PRIVATE);
        }
        if (cursor != null) {
            int count = cursor.getCount();
            if (count > 0) {
                cursor.moveToFirst();
                while (cursor.moveToNext()) {
                    MediaBean mediaBean = new MediaBean();
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED));
                    String size = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                    mediaBean.setPath(path);
                    mediaBean.setName(name);
                    double s = formatSize(Long.valueOf(size), SIZETYPE_MB);
                    mediaBean.setSize(s + "M");
                    String time = format.format(new Date(Long.valueOf(date)));
                    mediaBean.setTime(time);
                    list.add(mediaBean);
                }
                cursor.close();
            }
        }
        return list;
    }

    private static long getLocalDuration(String path) {
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(path);
            return Long.parseLong(mmr.extractMetadata
                    (MediaMetadataRetriever.METADATA_KEY_DURATION));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值

    private static double formatSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df
                        .format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }
}
