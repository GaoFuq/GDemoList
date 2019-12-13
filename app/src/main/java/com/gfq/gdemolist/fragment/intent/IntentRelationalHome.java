package com.gfq.gdemolist.fragment.intent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.recyclerview.widget.ItemTouchHelper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.gfq.gdemolist.rv_itemdecoration.RVItemDecoration_1;
import com.gfq.gdemolist.util.Permission;
import com.gfq.gdemolist.R;
import com.gfq.gdemolist.base.BaseFragment;
import com.gfq.gdemolist.databinding.FragmentIntentRelationalBinding;
import com.gfq.gdemolist.rvbinding.RVBindingAdapter;
import com.gfq.gdemolist.rvbinding.SuperBindingViewHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * create by 高富强
 * on {2019/12/2} {11:23}
 * desctapion: Intent相关  主Fragment
 */

/**
 *
 打开图库
 Intent intent = new Intent();
 intent.setAction(Intent.ACTION_PICK);
 intent.setType("image/jpeg");

 //9.打开摄像头拍照:
 // 打开拍照程序
 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
 startActivityForResult(intent, 0);
 // 取出照片数据
 Bundle extras = intent.getExtras();
 Bitmap bitmap = (Bitmap) extras.get("data");

 //另一种:
 //调用系统相机应用程序，并存储拍下来的照片
 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
 time = Calendar.getInstance().getTimeInMillis();
 intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment
 .getExternalStorageDirectory().getAbsolutePath()+"/tucue", time + ".jpg")));
 startActivityForResult(intent, ACTIVITY_GET_CAMERA_IMAGE);

 //===============================================================

 //10.获取并剪切图片
 // 获取并剪切图片
 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
 intent.setType("image/*");
 intent.putExtra("crop", "true"); // 开启剪切
 intent.putExtra("aspectX", 1); // 剪切的宽高比为1：2
 intent.putExtra("aspectY", 2);
 intent.putExtra("outputX", 20); // 保存图片的宽和高
 intent.putExtra("outputY", 40);
 intent.putExtra("output", Uri.fromFile(new File("/mnt/sdcard/temp"))); // 保存路径
 intent.putExtra("outputFormat", "JPEG");// 返回格式
 startActivityForResult(intent, 0);
 // 剪切特定图片
 Intent intent = new Intent("com.android.camera.action.CROP");
 intent.setClassName("com.android.camera", "com.android.camera.CropImage");
 intent.setData(Uri.fromFile(new File("/mnt/sdcard/temp")));
 intent.putExtra("outputX", 1); // 剪切的宽高比为1：2
 intent.putExtra("outputY", 2);
 intent.putExtra("aspectX", 20); // 保存图片的宽和高
 intent.putExtra("aspectY", 40);
 intent.putExtra("scale", true);
 intent.putExtra("noFaceDetection", true);
 intent.putExtra("output", Uri.parse("file:///mnt/sdcard/temp"));
 startActivityForResult(intent, 0);

 //===============================================================

 //11.打开Google Market
 // 打开Google Market直接进入该程序的详细页面
 Uri uri = Uri.parse("market://details?id=" + "com.demo.app");
 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
 startActivity(intent);

 //===============================================================

 //12.进入手机设置界面:
 // 进入无线网络设置界面（其它可以举一反三）
 Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
 startActivityForResult(intent, 0);

 //===============================================================

 //13.安装apk:
 Uri installUri = Uri.fromParts("package", "xxx", null);
 returnIt = new Intent(Intent.ACTION_PACKAGE_ADDED, installUri);

 //===============================================================

 //14.卸载apk:
 Uri uri = Uri.fromParts("package", strPackageName, null);
 Intent it = new Intent(Intent.ACTION_DELETE, uri);
 startActivity(it);

 //===============================================================

 //15.发送附件:
 Intent it = new Intent(Intent.ACTION_SEND);
 it.putExtra(Intent.EXTRA_SUBJECT, "The email subject text");
 it.putExtra(Intent.EXTRA_STREAM, "file:///sdcard/eoe.mp3");
 sendIntent.setType("audio/mp3");
 startActivity(Intent.createChooser(it, "Choose Email Client"));

 //===============================================================

 //16.进入联系人页面:
 Intent intent = new Intent();
 intent.setAction(Intent.ACTION_VIEW);
 intent.setData(People.CONTENT_URI);
 startActivity(intent);

 //===============================================================


 //17.查看指定联系人:
 Uri personUri = ContentUris.withAppendedId(People.CONTENT_URI, info.id);//info.id联系人ID
 Intent intent = new Intent();
 intent.setAction(Intent.ACTION_VIEW);
 intent.setData(personUri);
 startActivity(intent);

 //===============================================================

 //18.调用系统编辑添加联系人（高版本SDK有效）：
 Intent it = newIntent(Intent.ACTION_INSERT_OR_EDIT);
 it.setType("vnd.android.cursor.item/contact");
 //it.setType(Contacts.CONTENT_ITEM_TYPE);
 it.putExtra("name","myName");
 it.putExtra(android.provider.Contacts.Intents.Insert.COMPANY, "organization");
 it.putExtra(android.provider.Contacts.Intents.Insert.EMAIL,"email");
 it.putExtra(android.provider.Contacts.Intents.Insert.PHONE,"homePhone");
 it.putExtra(android.provider.Contacts.Intents.Insert.SECONDARY_PHONE,"mobilePhone");
 it.putExtra( android.provider.Contacts.Intents.Insert.TERTIARY_PHONE,"workPhone");
 it.putExtra(android.provider.Contacts.Intents.Insert.JOB_TITLE,"title");
 startActivity(it);

 //===============================================================

 //19.调用系统编辑添加联系人（全有效）：
 Intent intent = newIntent(Intent.ACTION_INSERT_OR_EDIT);
 intent.setType(People.CONTENT_ITEM_TYPE);
 intent.putExtra(Contacts.Intents.Insert.NAME, "My Name");
 intent.putExtra(Contacts.Intents.Insert.PHONE, "+1234567890");
 intent.putExtra(Contacts.Intents.Insert.PHONE_TYPE,Contacts.PhonesColumns.TYPE_MOBILE);
 intent.putExtra(Contacts.Intents.Insert.EMAIL, "com@com.com");
 intent.putExtra(Contacts.Intents.Insert.EMAIL_TYPE, Contacts.ContactMethodsColumns.TYPE_WORK);
 startActivity(intent);

 //===============================================================

 //20.打开另一程序
 Intent i = new Intent();
 ComponentName cn = new ComponentName("com.example.jay.test",
 "com.example.jay.test.MainActivity");
 i.setComponent(cn);
 i.setAction("android.intent.action.MAIN");
 startActivityForResult(i, RESULT_OK);

 //===============================================================

 //21.打开录音机
 Intent mi = new Intent(Media.RECORD_SOUND_ACTION);
 startActivity(mi);

 //===============================================================

 //22.从google搜索内容
 Intent intent = new Intent();
 intent.setAction(Intent.ACTION_WEB_SEARCH);
 intent.putExtra(SearchManager.QUERY,"searchString")
 startActivity(intent);


 */
public class IntentRelationalHome extends BaseFragment<FragmentIntentRelationalBinding> {
    private RVBindingAdapter<String> adapter;
    private List<String> titles;

    @Override
    protected int layout() {
        return R.layout.fragment_intent_relational;
    }

    @Override
    protected void main() {
        requestPermissions();
        initRV();
        addItems();
    }

    private void requestPermissions() {
        String[] permissions = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        Permission.requestPermissions(getActivity(), 101, permissions, new Permission.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied() {

            }
        });

    }

    private void addItems() {
        addItem("分享文本");
        addItem("分享图片");
        addItem("分享视频");
        addItem("点击Home键");
        addItem("打开百度首页");
        addItem("拨打电话");
        addItem("发送短信");
//        addItem("多媒体播放");
//        addItem("获取SD卡下所有音频文件,然后播放第一首");
        addItem("媒体查询");

    }

    private void onItemClick(int position) {
        switch (position) {
            case 0:
                shareText();
                break;
            case 1:
                sharePicture();
                break;
            case 2:
                shareVideo();
                break;
            case 3:
                asClickHome();
                break;
            case 4:
                openUrl();
                break;
            case 5:
                tel();
                break;
            case 6:
                sendSms();
                break;
            case 7:
                navigate(R.id.action_intentRelational_to_mediaQuery);
                break;
                default:break;
        }
    }

    private void mediaPlay() {
        //本地多媒体播放:
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        Uri uri = Uri.parse("file:///sdcard/foo.mp3");
//        intent.setDataAndType(uri, "audio/mp3");
//        startActivity(intent);


        //获取SD卡下所有音频文件,然后播放第一首=-=
        Uri uri = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "1");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(Intent.createChooser(intent, "分享视频连接测试"));

    }

    private void sendSms() {
        //发送短信
        // 给10086发送内容为“Hello”的短信
        Uri uri = Uri.parse("smsto:10086");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", "Hello");
        startActivity(intent);
    }

    private void tel() {
        Uri uri = Uri.parse("tel:10086");
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(intent);
    }

    private void openUrl() {
        Intent it = new Intent();
        it.setAction(Intent.ACTION_VIEW);
        it.setData(Uri.parse("http://www.baidu.com"));
        startActivity(it);
    }

    private void asClickHome() {
        Intent it = new Intent();
        it.setAction(Intent.ACTION_MAIN);
        it.addCategory(Intent.CATEGORY_HOME);
        startActivity(it);
    }

    @SuppressLint("CheckResult")
    private void shareVideo() {
        String url = "https://video.hibixin.com/video/standard/b71a0303-fc4e-41f3-8f30-a7f44c40a4d4.mp4";
        Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(ObservableEmitter<File> emitter) throws Exception {
                emitter.onNext(Glide.with(getActivity()).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        //获取到下载得到的图片，进行本地保存
                        File pictureFolder = Environment.getExternalStorageDirectory();
                        //第二个参数为你想要保存的目录名称
                        File appDir = new File(pictureFolder, "your_picture_save_path");
                        if (!appDir.exists()) {
                            appDir.mkdirs();
                        }
                        String fileName = System.currentTimeMillis() + ".mp4";
                        File destFile = new File(appDir, fileName);
                        //把gilde下载得到图片复制到定义好的目录中去
                        copy(file, destFile);
                        // 最后通知图库更新
                        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.fromFile(new File(destFile.getPath()))));
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_STREAM, destFile.getPath());
                        intent.setType("video/*");
                        startActivity(Intent.createChooser(intent, "分享视频连接测试"));
                    }
                });


    }

    /**
     * 必须是本地的图片才能分享，所以需要先下载
     */
    @SuppressLint("CheckResult")
    private void sharePicture() {
        String url = "http://e.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg";
        Observable.create(new ObservableOnSubscribe<File>() {//io.reactivex
            @Override
            public void subscribe(ObservableEmitter<File> e) throws Exception {
                //通过gilde下载得到file文件,这里需要注意android.permission.INTERNET权限
                e.onNext(Glide.with(getActivity())
                        .load(url)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        //获取到下载得到的图片，进行本地保存
                        File pictureFolder = Environment.getExternalStorageDirectory();
                        //第二个参数为你想要保存的目录名称
                        File appDir = new File(pictureFolder, "your_picture_save_path");
                        if (!appDir.exists()) {
                            appDir.mkdirs();
                        }
                        String fileName = System.currentTimeMillis() + ".jpg";
                        File destFile = new File(appDir, fileName);
                        //把gilde下载得到图片复制到定义好的目录中去
                        copy(file, destFile);

                        // 最后通知图库更新
                        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.fromFile(new File(destFile.getPath()))));

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_STREAM, destFile.getPath());
                        intent.setType("image/*");
                        startActivity(Intent.createChooser(intent, "分享图片连接测试"));
                    }

                });

    }

    public void copy(File source, File target) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(source);
            fileOutputStream = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void shareText() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "share test");
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "分享文本测试"));
    }

    private void initRV() {
        titles = new ArrayList<>();
        adapter = new RVBindingAdapter<String>(getActivity(), com.gfq.gdemolist.BR.data) {
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
        binding.recycleView.addItemDecoration(new RVItemDecoration_1(Color.GREEN));
    }


    private void addItem(String title) {
        titles.add(title);
        adapter.setDataList(titles);
    }

}
