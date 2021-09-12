package com.example.lottiedemo;

///
///@author jacky.peng on 
///
public class ItemData {
    int status;
    int progress;
    int max = 100;
    boolean isDownloading = false;
    boolean isCancel = false;

    public ItemData(int status, int progress) {
        this.status = status;
        this.progress = progress;
    }

    public ItemData(int status) {
        this.status = status;
    }
}
