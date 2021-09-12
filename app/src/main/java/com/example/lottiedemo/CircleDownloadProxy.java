package com.example.lottiedemo;

import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayDeque;
import java.util.Queue;

///
///@author jacky.peng on 
///
public class CircleDownloadProxy {
    StatusManager statusManager;
    CircleDownloadView circleDownloadView;
    Queue<LottieAnimationView> pendingAnimationView = new ArrayDeque<>();

    public CircleDownloadProxy(final StatusManager statusManager, CircleDownloadView view) {
        this.circleDownloadView = view;
        this.statusManager = statusManager;
        circleDownloadView.setListener(new CircleDownloadView.IClickListener() {
            @Override
            public void onClicked() {
                if (itemData.isDownloading) {
                    itemData.isCancel = true;
                } else {
                    startDownload();
                }
            }
        });
        circleDownloadView.setLottieAnimationListener(new CircleDownloadView.IAnimationListener() {
            @Override
            public void onDownloadIconAppeared() {
                if (statusManager.getStatus() == StatusManager.STATUS_DOWNLOADING) {
                    circleDownloadView.fireNormalDownloadHighLightAnimation();
                }
            }
        });
        statusManager.setListener(new StatusManager.IStatusChangeListener() {
            @Override
            public void init2Started() {
                circleDownloadView.post(new Runnable() {
                    @Override
                    public void run() {
                        circleDownloadView.fireInit2StartAnimation();
                    }
                });
            }

            @Override
            public void started2Downloading() {
                circleDownloadView.post(new Runnable() {
                    @Override
                    public void run() {
                        circleDownloadView.fireDownloadingStartAnimation();
                    }
                });
            }

            @Override
            public void normal2FastDownloading() {

            }

            @Override
            public void fast2NormalDownloading() {

            }

            @Override
            public void downloading2Paused() {
                circleDownloadView.post(new Runnable() {
                    @Override
                    public void run() {
                        circleDownloadView.fireDownloading2PausedAnimation();
                    }
                });
            }

            @Override
            public void paused2Downloading() {
                circleDownloadView.post(new Runnable() {
                    @Override
                    public void run() {
                        circleDownloadView.firePaused2NormalDownloadingAnimation();
                    }
                });
            }

            @Override
            public void downloading2Finished() {
                circleDownloadView.post(new Runnable() {
                    @Override
                    public void run() {
                        circleDownloadView.fireDownloading2FinishAnimation();
                    }
                });
            }

            @Override
            public void downloading(final int progress) {
                circleDownloadView.post(new Runnable() {
                    @Override
                    public void run() {
                        circleDownloadView.updateProgress(progress);
                    }
                });
            }
        });
    }


    public void startDownload() {
        if (!itemData.isDownloading) {
            itemData.isDownloading = true;
            itemData.isCancel = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (itemData.progress == 0) {
                            statusManager.updateStatus(StatusManager.STATUS_STARTED);
                        } else {
//                            statusManager.updateStatus(StatusManager.STATUS_RESTART);
                        }
                        Thread.sleep(500);
                        while (!itemData.isCancel) {
                            if (itemData.progress >= itemData.max) {
                                statusManager.updateStatus(StatusManager.STATUS_FINISHED);
                                break;
                            }
                            Thread.sleep(500);
                            itemData.progress += 2;
                            statusManager.updateStatus(StatusManager.STATUS_DOWNLOADING, itemData.progress);
                        }
                        if (itemData.progress < itemData.max) {
                            statusManager.updateStatus(StatusManager.STATUS_PAUSED);
                        } else {
                            statusManager.updateStatus(StatusManager.STATUS_FINISHED);
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        itemData.isDownloading = false;
                    }
                }
            }).start();
        }
    }

    ItemData itemData;

    /**
     * 此时所有的view的动画都要暂停
     *
     * @param data
     */
    public void bindData(ItemData data) {
        this.itemData = data;
        circleDownloadView.cancelAnimations();
        statusManager.updateStatus(itemData.status);
        showView(itemData.status, itemData.progress);
    }

    private void showView(int status, int progress) {
        if (status == StatusManager.STATUS_INIT) {
            circleDownloadView.showContent();
            circleDownloadView.setContent("点击下载");
        } else if (status == StatusManager.STATUS_FINISHED) {
            circleDownloadView.showContent();
            circleDownloadView.setContent("安装");
        } else if (status == StatusManager.STATUS_PAUSED) {
            circleDownloadView.hideContent();
            circleDownloadView.bindStatusPause();
            circleDownloadView.updateProgress(progress);
        } else {
            circleDownloadView.hideContent();
        }
    }
}
