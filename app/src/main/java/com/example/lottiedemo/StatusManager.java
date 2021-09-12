package com.example.lottiedemo;

import android.util.Log;

///
///@author jacky.peng on 
///
public class StatusManager {
    public static final int STATUS_INIT = -1;
    private int mCurStatus = STATUS_INIT;
    private int mPreStatus = STATUS_INIT;
    public static final int STATUS_STARTED = 1;
    public static final int STATUS_DOWNLOADING = 2;
    public static final int STATUS_PAUSED = 3;
    public static final int STATUS_RESTART = 4;
    public static final int STATUS_FINISHED = 5;
    IStatusChangeListener listener;
    public static final String TAG = "StatusManager";

    public void setListener(IStatusChangeListener listener) {
        this.listener = listener;
    }

    public void updateStatus(int status) {
        updateStatus(status, 0);
    }

    public void updateStatus(int status, int progress) {
        if (status == STATUS_STARTED && mPreStatus == STATUS_INIT) {
            listener.init2Started();
        } else if (mPreStatus == STATUS_STARTED && status == STATUS_DOWNLOADING) {
            listener.started2Downloading();
        } else if (mPreStatus == STATUS_DOWNLOADING && status == STATUS_PAUSED) {
            listener.downloading2Paused();
        } else if (mPreStatus == STATUS_PAUSED && status == STATUS_DOWNLOADING) {
            listener.paused2Downloading();
        } else if (mPreStatus == STATUS_DOWNLOADING && status == STATUS_FINISHED) {
            listener.downloading2Finished();
        } else if (mPreStatus == STATUS_DOWNLOADING && status == STATUS_DOWNLOADING) {
            listener.downloading(progress);
        }
        Log.i(TAG, "updateStatus pre->" + mPreStatus + "  status:" + status);
        mPreStatus = status;
    }

    public int getStatus() {
        return mPreStatus;
    }

    public interface IStatusChangeListener {
        //
        void init2Started();

        void started2Downloading();

        void normal2FastDownloading();

        void fast2NormalDownloading();

        void downloading2Paused();

        void paused2Downloading();

        void downloading2Finished();

        void downloading(int progress);
    }
}
