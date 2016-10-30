package com.andframe.api.multistatus;

import android.view.View;
import android.view.ViewGroup;

/**
 * 可切换状态页面的布局
 * Created by SCWANG on 2016/10/20.
 */

public interface StatusLayouter {
    ViewGroup getLayout();
    void setContenView(View content);
    void setOnRefreshListener(OnRefreshListener listener);
    void setEmptyLayoutId(int layoutId);
    void setEmptyLayoutId(int layoutId, int msgId);
    void setErrorLayoutId(int layoutId, int msgId);
    void setEmptyLayoutId(int layoutId, int msgId, int btnId);
    void setErrorLayoutId(int layoutId, int msgId, int btnId);
    void setProgressLayoutId(int layoutId);
    void setProgressLayoutId(int layoutId, int msgId);
    void setInvalidnetLayoutId(int layoutId);
    void setInvalidnetLayoutId(int layoutId, int msgId);
    void setInvalidnetLayoutId(int layoutId, int msgId, int btnId);
    void autoCompletedLayout();
    void showEmpty();
    void showEmpty(String empty);
    void showProgress();
    void showProgress(String message);
    void showInvalidnet();
    void showInvalidnet(String message);
    void showError(String error);
    void showContent();

    boolean isProgress();
    boolean isContent();
}