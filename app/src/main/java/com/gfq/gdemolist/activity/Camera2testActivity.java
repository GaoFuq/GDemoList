package com.gfq.gdemolist.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import androidx.core.app.ActivityCompat;

import com.gfq.gdemolist.R;
import com.gfq.gdemolist.base.BaseActivity;
import com.gfq.gdemolist.databinding.ActivityCamera2Binding;
import com.gfq.gdemolist.util.Permission;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * create by 高富强
 * on {2019/12/6} {17:49}
 * desctapion:
 */
public class Camera2testActivity extends BaseActivity<ActivityCamera2Binding> {

    private String mCameraIdBack = "";
    private String mCameraIdFront = "";
    private Size mPreviewSize;
    private Size mCaptureSize;
    private HandlerThread mCameraThread;
    private Handler mCameraHandler;
    private CameraDevice mCameraDevice;
    private ImageReader mImageReader;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    private CaptureRequest mCaptureRequest;
    private CameraCaptureSession mCameraCaptureSession;

    private boolean isTakePicture = true;//false 录像
    private boolean isBackCameraId=true;//false 前置摄像头
    private Image image;

    @Override
    protected int layout() {
        return R.layout.activity_camera2;
    }

    @Override
    protected void main() {
        String[] permissions = {Manifest.permission.CAMERA};
        Permission.requestPermissions(this, 101, permissions, new Permission.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied() {

            }
        });

        binding.btnBack.setOnClickListener(v->{
//            restartPreview();
            startPreview();
            binding.rlView.setVisibility(View.GONE);

            if(image!=null){
                image.close();
            }
        });

        binding.btnSwitchCameraId.setOnClickListener(v->{
            if(isBackCameraId){
                openCamera(mCameraIdFront);
            }else {
                openCamera(mCameraIdBack);
            }
            isBackCameraId=!isBackCameraId;
        });

        binding.btnSwitch.setOnClickListener(v -> {
            if(isTakePicture){
                binding.btnPlay.setText("录像");
            }else {
                binding.btnPlay.setText("拍照");
            }
            isTakePicture=!isTakePicture;
        });

        binding.btnPlay.setOnClickListener(v -> {
            if (isTakePicture) {
                takePicture();
            } else {
                takeVideo();
            }
        });
    }

    private void takeVideo() {

    }

    @Override
    public void onResume() {
        super.onResume();
        startCameraThread();
        if (!binding.textureView.isAvailable()) {
            binding.textureView.setSurfaceTextureListener(mTextureListener);
        } else {
            if (mCameraDevice != null) {
                startPreview();
            } else {
                if (mImageReader == null) {
                    setupImageReader();
                }
                openCamera(mCameraIdBack);
            }
        }
    }


    private void restartPreview() {
        try {
            //执行setRepeatingRequest方法就行了，注意mCaptureRequest是之前开启预览设置的请求
            if (mCameraCaptureSession != null && mCaptureRequest != null && mCameraHandler != null)
                mCameraCaptureSession.setRepeatingRequest(mCaptureRequest, null, mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void startCameraThread() {
        mCameraThread = new HandlerThread("CameraThread");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());
    }

    private TextureView.SurfaceTextureListener mTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //当SurefaceTexture可用的时候，设置相机参数并打开相机
            setupCamera(width, height);
            openCamera(mCameraIdBack);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    private void setupCamera(int width, int height) {
        //获取摄像头的管理者CameraManager
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                if (!mCameraIdBack.equals("") && !mCameraIdFront.equals("")) {
                    logd("前后摄像头已匹配");
                    break;
                }
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null) {
                    if (facing == CameraCharacteristics.LENS_FACING_BACK) {
                        if (mCameraIdBack.equals("")) {
                            mCameraIdBack = cameraId;
                            logd("mCameraIdBack = " + cameraId);
                        }
                    } else if (facing == CameraCharacteristics.LENS_FACING_FRONT) {
                        if (mCameraIdFront.equals("")) {
                            mCameraIdFront = cameraId;
                            logd("mCameraIdFront = " + cameraId);
                        }
                    }
                }

                //获取StreamConfigurationMap，它是管理摄像头支持的所有输出格式和尺寸
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                assert map != null;
                //根据TextureView的尺寸设置预览尺寸
                mPreviewSize = getOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height);
                //获取相机支持的最大拍照尺寸
                mCaptureSize = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new Comparator<Size>() {
                    @Override
                    public int compare(Size lhs, Size rhs) {
                        return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getHeight() * rhs.getWidth());
                    }
                });
                //此ImageReader用于拍照所需
                setupImageReader();
            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    //选择sizeMap中大于并且最接近width和height的size
    private Size getOptimalSize(Size[] sizeMap, int width, int height) {
        List<Size> sizeList = new ArrayList<>();
        for (Size option : sizeMap) {
            if (width > height) {
                if (option.getWidth() > width && option.getHeight() > height) {
                    sizeList.add(option);
                }
            } else {
                if (option.getWidth() > height && option.getHeight() > width) {
                    sizeList.add(option);
                }
            }
        }
        if (sizeList.size() > 0) {
            return Collections.min(sizeList, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                }
            });
        }
        return sizeMap[0];
    }


    private void openCamera(String cameraId) {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (manager != null)
                manager.openCamera(cameraId, mStateCallback, mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            mCameraDevice = camera;
            startPreview();

        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            camera.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            camera.close();
            mCameraDevice = null;
        }


    };

    private void startPreview() {
        SurfaceTexture mSurfaceTexture = binding.textureView.getSurfaceTexture();
        //设置TextureView的缓冲区大小
        mSurfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        //获取Surface显示预览数据
        Surface previewSurface = new Surface(mSurfaceTexture);
        try {
            //创建CaptureRequestBuilder，TEMPLATE_PREVIEW表示预览请求
            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            //设置Surface作为预览数据的显示界面
            mCaptureRequestBuilder.addTarget(previewSurface);
            //创建相机捕获会话，第一个参数是捕获数据的输出Surface列表，第二个参数是CameraCaptureSession的状态回调接口，
            // 当它创建好后会回调onConfigured方法，第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行
            mCameraDevice.createCaptureSession(Arrays.asList(previewSurface, mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        //创建捕获请求
                        mCaptureRequest = mCaptureRequestBuilder.build();
                        mCameraCaptureSession = session;
                        //设置反复捕获数据的请求，这样预览界面就会一直有数据显示
                        mCameraCaptureSession.setRepeatingRequest(mCaptureRequest, null, mCameraHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {

                }
            }, mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拍照
     */
    public void takePicture() {
        try {
            mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
            mCameraCaptureSession.capture(mCaptureRequestBuilder.build(), mCaptureCallback, mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {
        }

        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            capture();
        }
    };

    private void capture() {
        try {
            //首先我们创建请求拍照的CaptureRequest
            final CaptureRequest.Builder mCaptureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            //获取屏幕方向
//            rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
            //设置CaptureRequest输出到mImageReader
            mCaptureBuilder.addTarget(mImageReader.getSurface());
            //设置拍照方向
            //     mCaptureBuilder.set(CaptureRequest.JPEG_ORIENTATION, rotation);
//            mCaptureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATION.get(rotation));
            //这个回调接口用于拍照结束时重启预览，因为拍照会导致预览停止
            CameraCaptureSession.CaptureCallback mImageSavedCallback = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
//                    restartPreview();
                }
            };
            //停止预览
            mCameraCaptureSession.stopRepeating();
            //开始拍照，然后回调上面的接口重启预览，因为mCaptureBuilder设置ImageReader作为target，所以会自动回调ImageReader的onImageAvailable()方法保存图片
            mCameraCaptureSession.capture(mCaptureBuilder.build(), mImageSavedCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setupImageReader() {
        //2代表ImageReader中最多可以获取两帧图像流
        logd("mCaptureSize w=" + mCaptureSize.getWidth() + "mCaptureSize h=" + mCaptureSize.getHeight());
        mImageReader = ImageReader.newInstance(mCaptureSize.getWidth(), mCaptureSize.getHeight(), ImageFormat.JPEG, 2);
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                image = reader.acquireNextImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] dat = new byte[buffer.remaining()];
                buffer.get(dat);

                Bitmap bitmap = bytes2Bimap(dat);
                runOnUiThread(() -> {
                    binding.img.setImageBitmap(bitmap);
                    binding.textureView.setVisibility(View.GONE);
                    binding.rlView.setVisibility(View.VISIBLE);
                });
            }
        }, mCameraHandler);
    }


    public Bitmap bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
}
