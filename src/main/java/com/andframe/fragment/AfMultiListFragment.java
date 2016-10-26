package com.andframe.fragment;

import android.support.annotation.NonNull;
import android.widget.ListAdapter;

import com.andframe.api.page.ListPagerHelper;
import com.andframe.api.page.MultiListPager;
import com.andframe.api.page.MultiListHelper;
import com.andframe.api.view.ItemsRefreshableViewer;
import com.andframe.api.view.ItemsViewer;
import com.andframe.impl.helper.AfMultiListPagerHelper;
import com.andframe.model.Page;
import com.andframe.module.AfFrameSelector;
import com.andframe.module.AfModuleNodata;
import com.andframe.module.AfModuleProgress;
import com.andframe.task.AfListViewTask;

import java.util.Date;
import java.util.List;

/**
 * 多功能列表页
 * 1.多页面数据（数据页、空数据页、加载页）
 * 2.下拉刷新（下拉，上拉）
 * 3.分页加载数据 (TaskByPage)
 * 4.基本数据缓存（Cache）
 * Created by SCWANG on 2016/9/7.
 */
public abstract class AfMultiListFragment<T> extends AfListFragment<T> implements MultiListPager<T> {

    //<editor-fold desc="帮助类">
    protected MultiListHelper<T> mMultiHelper = newMultiHelper();

    @NonNull
    protected MultiListHelper<T> newMultiHelper() {
        if (mHelper instanceof MultiListHelper) {
            return ((MultiListHelper<T>) mHelper);
        }
        return new AfMultiListPagerHelper<>(this);
    }

    @NonNull
    @Override
    protected ListPagerHelper<T> newListHelper() {
        return mMultiHelper = newMultiHelper();
    }
    //</editor-fold>

    /**
     * 创建指定命令的任务并执行
     *
     * @param task 任务标识
     */
    protected AfListViewTask postTask(int task) {
        return mMultiHelper.postTask(task);
    }

    /**
     * 创建新的AfListView
     *
     * @param listView ListView对象
     * @return 可刷新的ListView
     */
    public ItemsRefreshableViewer<?> newAfListView(ItemsViewer listView) {
        return mMultiHelper.newAfListView(listView);
    }

    /**
     * 新建页面选择器
     *
     * @param pageable 页面对象
     * @return 数据页面切换器
     */
    public abstract AfFrameSelector newAfFrameSelector(MultiListPager<T> pageable);

    /**
     * 新建加载页面
     *
     * @param pageable 页面对象
     * @return 加载页面模块
     */
    public abstract AfModuleProgress newModuleProgress(MultiListPager<T> pageable);

    /**
     * 新建空数据页面
     *
     * @param pageable 页面对象
     * @return 空数据页面模块
     */
    public abstract AfModuleNodata newModuleNodata(MultiListPager<T> pageable);

    /**
     * 添加一条数据到显示列表
     * @param value 添加的数据
     */
    @SuppressWarnings("unused")
    public void addData(T value) {
        mMultiHelper.addData(value);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMultiHelper.onDestroy();
    }

    /**
     * 显示数据页面
     */
    public void showData() {
        mMultiHelper.showData();
    }

    /**
     * 正在加载数据提示
     */
    public void showLoading() {
        mMultiHelper.showLoading();
    }

    /**
     * 处理空数据
     */
    public void showNodata() {
        mMultiHelper.showNodata();
    }

    /**
     * 错误信息处理
     *
     * @param ex 异常对象
     */
    public void showLoadError(Throwable ex) {
        mMultiHelper.showLoadError(ex);
    }

    /**
     * 加载数据（缓存优先）
     */
    public void onLoad() {
        mMultiHelper.onLoad();
    }

    /**
     * 用户加载分页通知事件
     *
     * @return 是否处理（影响列表控件响应状态）
     */
    @Override
    public boolean onMore() {
        return mMultiHelper.onMore();
    }

    /**
     * 用户刷新数据通知事件
     *
     * @return 是否处理（影响列表控件响应状态）
     */
    @Override
    public boolean onRefresh() {
        return mMultiHelper.onRefresh();
    }

    /**
     * 更新缓存
     */
    public void putCache() {
        mMultiHelper.putCache();
    }

    /**
     * 清空缓存
     */
    public void clearCache() {
        mMultiHelper.clearCache();
    }

    /**
     * 更新缓存
     * @param list 要缓存的数据
     */
    public void putCache(List<T> list) {
        mMultiHelper.putCache(list);
    }

    /**
     * 获取缓存时间
     *
     * @return 如果没有缓存 返回 null
     */
    public Date getCacheTime() {
        return mMultiHelper.getCacheTime();
    }

    /**
     * 任务准备开始 （在UI线程中）
     *
     * @return 返回true 表示准备完毕 否则 false 任务将被取消
     */
    public boolean onTaskPrepare(int task) {
        return mMultiHelper.onTaskPrepare(task);
    }

    /**
     * 缓存加载结束处理时间（框架默认调用onRefreshed事件处理）
     *
     * @param task      任务执行对象
     * @param isfinish  任务是否完成（未捕捉到异常）
     * @param list    完成加载数据
     * @param cachetime 缓存时间
     * @return 返回true 已经做好错误页面显示 返回false 框架会做好默认错误反馈
     */
    public boolean onLoaded(AfListViewTask task, boolean isfinish, List<T> list, Date cachetime) {
        return mMultiHelper.onLoaded(task, isfinish, list, cachetime);
    }

    /**
     * 任务刷新结束处理事件
     *
     * @param task     任务执行对象
     * @param isfinish 任务是否完成（未捕捉到异常）
     * @param list   完成加载数据
     * @return 返回true 已经做好错误页面显示 返回false 框架会做好默认错误反馈
     */
    public boolean onRefreshed(AfListViewTask task, boolean isfinish, List<T> list) {
        return mMultiHelper.onRefreshed(task, isfinish, list);
    }

    /**
     * 任务加载更多结束处理事件
     *
     * @param task     任务执行对象
     * @param isfinish 任务是否完成（未捕捉到异常）
     * @param list   完成加载数据
     * @return 返回true 已经做好错误页面显示 返回false 框架会做好默认错误反馈
     */
    public boolean onMored(AfListViewTask task, boolean isfinish, List<T> list) {
        return mMultiHelper.onMored(task, isfinish, list);
    }

    /**
     * 设置是否现实加载更多功能
     * @param task     完成的任务
     * @param list   任务加载的数据
     * @return true 显示更多功能 false 数据加载结束
     */
    public boolean setMoreShow(AfListViewTask task, List<T> list) {
        return mMultiHelper.setMoreShow(task, list);
    }

    /**
     * 加载缓存列表（不分页，在异步线程中执行，不可以更改页面操作）
     * @param isCheckExpired 是否检测缓存过期（刷新失败时候可以加载缓存）
     *
     * @return 返回 null 可以使用框架内置缓存
     */
    public List<T> onTaskLoad(boolean isCheckExpired) {
        return mMultiHelper.onTaskLoad(isCheckExpired);
    }

    /**
     * 缓存追加
     */
    public void onTaskPushCache(List<T> list) {
        mMultiHelper.onTaskPushCache(list);
    }

    /**
     * 缓存覆盖
     */
    public void onTaskPutCache(List<T> list) {
        mMultiHelper.onTaskPutCache(list);
    }
    /**
     * 可刷新列表页有新的数据加载模式，需要 final 父类数据加载方法
     */
    @Override
    public final List<T> onTaskLoadList() throws Exception {
        return mMultiHelper.onTaskLoadList();
    }

    /**
     * 数据分页加载（在异步线程中执行，不可以更改页面操作）
     *
     * @param page 分页对象
     * @param task 任务id
     * @return 加载到的数据列表
     * @throws Exception
     */
    public abstract List<T> onTaskListByPage(Page page, int task) throws Exception;

    /**
     * 由postTask(int task)触发
     * 除了与刷新、翻页、加载缓存有关的其他任务工作（异步线程、留给子类任务扩展用）
     *
     * @param task 任务标识
     * @return 是否成功执行
     */
    public boolean onTaskWorking(int task) throws Exception {
        return mMultiHelper.onTaskWorking(task);
    }

    /**
     * 与onTaskWorking相对应的结束（UI线程）
     *
     * @param task 任务执行对象
     * @param isfinish       任务是否完成（未捕捉到异常）
     * @param list         完成加载数据
     * @return 返回true 已经做好错误页面显示 返回false 框架会做好默认错误反馈
     */
    public boolean onTaskWorked(AfListViewTask task, boolean isfinish, List<T> list) {
        return mMultiHelper.onTaskWorked(task, isfinish, list);
    }

    /**
     * 绑定适配器
     * @param listView 列表
     * @param adapter 适配器
     */
    @Override
    public void bindAdapter(ItemsViewer listView, ListAdapter adapter) {
        mMultiHelper.bindAdapter(listView, adapter);
    }


}