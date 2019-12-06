package com.gfq.gdemolist.activity;

import android.Manifest;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.gfq.gdemolist.R;
import com.gfq.gdemolist.base.BaseActivity;
import com.gfq.gdemolist.bean.BixinBean;
import com.gfq.gdemolist.databinding.ActivityBixinBinding;
import com.gfq.gdemolist.net.APIService;
import com.gfq.gdemolist.net.OnCallBack;
import com.gfq.gdemolist.util.Permission;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * create by 高富强
 * on {2019/12/2} {15:49}
 * desctapion:
 */
public class BiXinVideoActivity extends BaseActivity<ActivityBixinBinding> {
    String name;
    private int pageNum = 1;
    List<String> listName;
    List<String> listPath;
    private int count = 0;

    @Override
    protected int layout() {
        return R.layout.activity_bixin;
    }

    @Override
    protected void main() {
        requestPermissions();
        listName = new ArrayList<>();
        listPath = new ArrayList<>();
        binding.video.setMediaController(new MediaController(this));
        request();


        binding.btnRefresh.setOnClickListener(v -> {
            count = 0;
            listName.clear();
            listPath.clear();
            request();
        });


        binding.btnPlayNext.setOnClickListener(v -> {
          count++;
          if(count>listName.size()-1){
              count=0;
          }
            playVideo();
        });

        binding.btnPlayPre.setOnClickListener(v->{
            count--;
            if (count < 0) {
                count = listName.size() - 1;
            }
            playVideo();
        });
        binding.btnPut.setOnClickListener(v -> {
            binding.btnPutInfo.setText("正在下载...");
            download(listName.get(count), listPath.get(count));
        });
    }

    private void playVideo(){
        binding.video.setVideoURI(Uri.parse(listPath.get(count)));
        binding.video.start();
        binding.btnShow.setText("共" +  listName.size() + "条数据,正在播放第" + (count + 1) + "个视频");
    }
    private void request() {
        Map<String, Object> map = new HashMap<>();
        map.put("cityName", "");
        map.put("pageNo", pageNum);
        map.put("pageSize", 20);
        map.put("isStart", false);
        map.put("fromToken", "5dc705761235cb78e6a12de787359bff");
        APIService.call(APIService.api().bixin(map), new OnCallBack<BixinBean>() {
            @Override
            public void onSuccess(BixinBean bixinBean) {
                if(bixinBean==null||bixinBean.getResult()==null){
                    binding.btnShow.setText(bixinBean.getMsg());
                    return;
                }
                if (bixinBean.getResult().getTimeLineList() != null && bixinBean.getResult().getTimeLineList().size() > 0) {
                    for (int i = 0; i < bixinBean.getResult().getTimeLineList().size(); i++) {
                        if (bixinBean.getResult().getTimeLineList().get(i) != null) {
                            if (bixinBean.getResult().getTimeLineList().get(i).getVideoUrls() != null && bixinBean.getResult().getTimeLineList().get(i).getVideoUrls().size() > 0) {
                                String videoPath = bixinBean.getResult().getTimeLineList().get(i).getVideoUrls().get(0);
                                if (!TextUtils.isEmpty(videoPath)) {
                                    String[] split = videoPath.split("/");
                                    name = split[split.length - 1];
                                    listName.add(name);
                                    listPath.add(videoPath);
                                }
                            }
                        }
                    }
                    binding.btnShow.setText("共" + listName.size() + "条数据,正在播放第1个视频");
                    playVideo();
                }
            }
        });
    }

    private void download(String b, String url) {
        Request request = new Request.Builder().url(url).get().build();
        OkHttpClient okHttpClient = new OkHttpClient();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.btnPutInfo.setText("下载失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (response.body() != null) {
//                    byte[] bytes = response.body().bytes();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.btnPutInfo.setText("下载完成，正在上传...");
                        }
                    });
                    InputStream inputStream = response.body().byteStream();
                    send(b, inputStream);
//                    File dir = new File(Environment.getExternalStorageDirectory().getPath()+"/bixin");
//                    if(!dir.exists()){
//                        dir.mkdirs();
//                    }
//                    File file = new File(dir.getAbsolutePath()+System.currentTimeMillis()+".mp4");
//                    FileOutputStream out = new FileOutputStream(file);
//                    byte[] len = new byte[2048];
//                    out.write(bytes,0,len.length);


                }
            }
        });
    }


    private void send(String name, InputStream inputStream) {
        try {
            // Create a minioClient with the MinIO Server name, Port, Access key and Secret key.
            MinioClient minioClient = new MinioClient("http://oss.demo.cq1080.com/", "minio", "minio123");
            minioClient.putObject("demo", "video/bixin/" + name, inputStream, "video/mp4");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.btnPutInfo.setText("上传完成");
                }
            });
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException | XmlPullParserException e) {
            System.out.println("Error occurred: " + e);
        }
    }


    private void requestPermissions() {
        String[] permissions = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        Permission.requestPermissions(this, 101, permissions, new Permission.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied() {

            }
        });

    }
}
