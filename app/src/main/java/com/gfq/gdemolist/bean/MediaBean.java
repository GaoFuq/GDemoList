package com.gfq.gdemolist.bean;

import android.graphics.Bitmap;

/**
 * create by 高富强
 * on {2019/11/27} {15:31}
 * desctapion:
 */
public class MediaBean {
    private String path;
//    private long duration;
    private String size;
    private String name;
    private String time;
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

//    public long getDuration() {
//        return duration;
//    }
//
//    public void setDuration(long duration) {
//        this.duration = duration;
//    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
