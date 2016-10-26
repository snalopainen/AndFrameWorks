package com.andframe.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.andframe.adapter.recycler.ViewHolderItem;
import com.andframe.api.ListItem;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 可直接更新状态(不刷新列表)的适配器
 * Created by SCWANG on 2016/8/5.
 */
@SuppressWarnings("unused")
public class AfUpdateAdapter<T> extends AfListAdapterWrapper<T> {

    protected Map<View, SimpleEntry<Integer, ListItem<T>>> itemTMap = new HashMap<>();

    public AfUpdateAdapter(AfListAdapter<T> wrapped) {
        super(wrapped);
    }

    //<editor-fold desc="逻辑连接">
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolderItem<T> holder = null;
        if (view != null) {
            //noinspection unchecked
            holder = (ViewHolderItem<T>) view.getTag(KEY_VIEW_TAG);
        }
        if (holder == null) {
            holder = createViewHolder(viewGroup, getItemViewType(i));
            holder.itemView.setTag(KEY_VIEW_TAG, holder);
        }
        bindViewHolder(holder, i);
        return holder.itemView;
    }
    @Override
    public void onBindViewHolder(ViewHolderItem<T> holder, int position, List<Object> payloads) {
        onBindViewHolder(holder, position);
    }
    @Override
    public void onBindViewHolder(ViewHolderItem<T> holder, int position) {
        bindingItem(holder.itemView, holder.getItem(), position);
    }
    @Override
    protected void bindingItem(View view, ListItem<T> item, int index) {
        itemTMap.put(view, new SimpleEntry<>(index, item));
        super.bindingItem(view, item, index);
    }
    //</editor-fold>

    //<editor-fold desc="功能方法">
    public void update() {
        update(null);
    }

    public void update(int index) {
        update(get(index));
    }

    public void update(T model) {
        for (Map.Entry<View, SimpleEntry<Integer, ListItem<T>>> entry : itemTMap.entrySet()) {
            int index = entry.getValue().getKey();
            if (index > -1 && index < getCount()) {
                T itemModel = get(index);
                if (model == null || itemModel == model) {
                    onUpdate(entry.getKey(), entry.getValue().getValue(), index, itemModel);
                }
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="子类重写">
    protected void onUpdate(View view, ListItem<T> item, int index, T model) {
        item.onBinding(view, model, index);
    }
    //</editor-fold>

}