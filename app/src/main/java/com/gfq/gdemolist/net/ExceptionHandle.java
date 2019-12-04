package com.gfq.gdemolist.net;


import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

public class ExceptionHandle {

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    private static final int NO_CONTENT = 204;

    public static ResponeThrowable handleException(Throwable e) {
        ResponeThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponeThrowable(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                    //显示not found界面
                    ex.code = httpException.code();
                    ex.message = "not found";
                    break;
//                case REQUEST_TIMEOUT:
//                    //提示连接超时
//                    ex.code = httpException.code();
//                    ex.message = "连接超时";
//                    break;
                case GATEWAY_TIMEOUT:
                case NO_CONTENT:
                    ex.code = httpException.code();
                    ex.message = "";
                    break;
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                    ex.code = httpException.code();
                    ex.message = "未连接到服务器";
                    break;
                case SERVICE_UNAVAILABLE:
                    ex.code = httpException.code();
                    ex.message = "未连接到服务器";
                    break;
                default:
                    ex.code = httpException.code();
                    ex.message = "网络错误";
                    break;
            }
            return ex;
        } else if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            ex = new ResponeThrowable(resultException, resultException.code);
            ex.message = resultException.getMessage();
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException
        ) {
            ex = new ResponeThrowable(e, ERROR.PARSE_ERROR);
            ex.message = "解析错误";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponeThrowable(e, ERROR.NETWORD_ERROR);
            ex.message = "连接失败";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponeThrowable(e, ERROR.SSL_ERROR);
            ex.message = "证书验证失败";
            return ex;
        } else if (e instanceof UnknownHostException) {
            ex = new ResponeThrowable(e, ERROR.NETWORD_ERROR);
            ex.message = "网络连接不可用，请检查网络设置";
            return ex;
        } else {
            ex = new ResponeThrowable(e, ERROR.UNKNOWN);
            ex.message = "服务器开小差了";
//The mapper function returned a null value.
            if (e != null && e.getMessage() != null)
                if (e.getMessage().equals("The mapper function returned a null value.")) {
                    return null;
                }
            return ex;
            // return null;
        }
    }


    class ERROR {

        public static final int UNKNOWN = 1000;

        public static final int PARSE_ERROR = 1001;


        public static final int NETWORD_ERROR = 1002;

        public static final int HTTP_ERROR = 1003;

        public static final int SSL_ERROR = 1005;
    }

    public static class ResponeThrowable extends Exception {
        public int code;
        public String message;

        public ResponeThrowable(Throwable throwable, int code) {
            super(throwable);
            this.code = code;
        }
    }


    static class ServerException extends RuntimeException {
        int code;
        String message;

        public ServerException(int code, String message) {
            super(message);
            this.message = message;
            this.code = code;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }

}
