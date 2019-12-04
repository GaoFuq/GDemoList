package com.gfq.gdemolist.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class APIService {
//    public static final String BASE_URL = "http://139.224.119.93:7007";
    public static final String BASE_URL = "https://api.hibixin.com";
    private static APIInterface apiInterface;
    // 缓存文件最大限制大小50M
    private static final long cacheSize = 1024 * 1024 * 50;
    // 设置缓存文件路径
    private static String cacheDirectory = Environment.getExternalStorageDirectory() + "/omi_caches";
    private static Cache cache = new Cache(new File(cacheDirectory), cacheSize);
    private static OkHttpClient mClient;
    private static String token = "";


    static {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // 设置连接超时时间
                .writeTimeout(30, TimeUnit.SECONDS)// 设置写入超时时间
                .readTimeout(30, TimeUnit.SECONDS)// 设置读取数据超时时间
                .retryOnConnectionFailure(true)// 设置进行连接失败重试
                .addInterceptor(loggingInterceptor)
               /* .addInterceptor(chain -> {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("token", token)
                            .build();
                    return chain.proceed(request);
                })*/
                .addInterceptor(chain -> {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("X-Udid", "201910311149536c7d730b52e6e611e8c5b9d513a6ccaa01bb4e9bf994f56a")
                            .addHeader("X-Client-Time", "1574941955909")
                            .addHeader("X-Sign", "46486986d111e862a7089542a530ba9d")
                            .addHeader("X-AccessToken", "oxkvnQlZcTUpe76DADMl8i-Q5vGIfXGzZByxMmTRNrwenX6l0i655EIny-mUOQ0G9AIq7kxbLpqNrbmtEnsMoOaVoIErHwZp-lw_hbTEe3-pituIDb7nf04A-GCAOaEg9zRaVXaJUSdGqPLS_ppRgALWYFAjI-7dWOJmHp2FDVQ")
                            .addHeader("X-NetWork", "4G")
                            .addHeader("X-User-Agent", "mapi/1.0 (Android 28;com.yitantech.gaigai 4.4.1;Xiaomi MI+CC+9;bx-yyb)")
                            .addHeader("X-Authentication", "90245a583a158fad14367e46ba09183f")
                            .addHeader("Cache-Control", "no-store")
                            .addHeader("Content-Type", "application/json; charset=utf-8")
                            .addHeader("Content-Length", "103")
                            .addHeader("Host", "api.hibixin.com")
                            .addHeader("Connection", "Keep-Alive")
                            .addHeader("Accept-Encoding", "gzip")
                            .build();
                    return chain.proceed(request);
                })
                .cache(cache)// 设置缓存
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .client(mClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        apiInterface = retrofit.create(APIInterface.class);
    }


    public static void setToken(String tt) {
        token = tt;
    }

    public static APIInterface api() {
        return apiInterface;
    }

    private static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = null;
            if (mConnectivityManager != null) {
                mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            }
            //获取连接对象
            if (mNetworkInfo != null) {
                //判断是TYPE_MOBILE网络
                if (ConnectivityManager.TYPE_MOBILE == mNetworkInfo.getType()) {
//                    LogManager.i("AppNetworkMgr", "网络连接类型为：TYPE_MOBILE");
                    //判断移动网络连接状态
                    NetworkInfo.State STATE_MOBILE = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
                    if (STATE_MOBILE == NetworkInfo.State.CONNECTED) {
//                        LogManager.i("AppNetworkMgrd", "网络连接类型为：TYPE_MOBILE, 网络连接状态CONNECTED成功！");
                        return mNetworkInfo.isAvailable();
                    }
                }
                //判断是TYPE_WIFI网络
                if (ConnectivityManager.TYPE_WIFI == mNetworkInfo.getType()) {
//                    LogManager.i("AppNetworkMgr", "网络连接类型为：TYPE_WIFI");
                    //判断WIFI网络状态
                    NetworkInfo.State STATE_WIFI = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
                    if (STATE_WIFI == NetworkInfo.State.CONNECTED) {
//                        LogManager.i("AppNetworkMgr", "网络连接类型为：TYPE_WIFI, 网络连接状态CONNECTED成功！");
                        return mNetworkInfo.isAvailable();
                    }
                }
            }
        }
        return false;
    }
/*
    public static <T extends BaseBean> void call(Observable<T> apiObservable, Context context, OnCallBack<T> onCallBack) {

        if (!isNetworkConnected(context.getApplicationContext())) {
            Toast.makeText(context, "网络已断开", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("APIService token = ", token);
        apiObservable.compose(upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(data -> {
                    if (data.getProDataStatus() == 200) {
                        return data;
                    } else {
                        // throw new ExceptionHandle.ServerException(data.getProDataStatus(), data.getProDescrib());
                        throw new RuntimeException();
                    }

                }))
                .subscribe(new DisposableObserver<T>() {
                    @Override
                    public void onNext(T t) {
                        onCallBack.onSuccess(t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("onError", "onError: " + e.getMessage());
//                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                        ExceptionHandle.ResponeThrowable error = ExceptionHandle.handleException(e);
                        if (error == null) {
                            onCallBack.onSuccess(null);
                            return;
                        }
                        Toast.makeText(context, error.message, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }*/








    public static <T> void call(Observable<T> observable, final OnCallBack<T> onCallBack) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<T>() {
                    @Override
                    public void onNext(T o) {
                        // onCallBack.onNext(o);
                        onCallBack.onSuccess(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //onCallBack.onError(e);
                      e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        // onCallBack.onComplete();
                    }
                });
    }


    public static <T> void call(Observable<T> apiObservable, Context context, OnCallBack<T> onCallBack) {

        if (!isNetworkConnected(context.getApplicationContext())) {
            Toast.makeText(context, "网络已断开", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("APIService token = ", token);
        apiObservable.compose(upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(data -> data))
                .subscribe(new DisposableObserver<T>() {
                    @Override
                    public void onNext(T t) {
                        onCallBack.onSuccess(t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("onError", "onError: " + e.getMessage());
//                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                        ExceptionHandle.ResponeThrowable error = ExceptionHandle.handleException(e);
                        if (error == null) {
                            onCallBack.onSuccess(null);
                            return;
                        }
                        Toast.makeText(context, error.message, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }
}
