package com.andframe.layoutbind.framework;

import android.view.View;

import com.andframe.activity.framework.AfViewable;
import com.andframe.activity.framework.IViewQuery;
import com.andframe.annotation.interpreter.Injecter;
import com.andframe.annotation.interpreter.LayoutBinder;
import com.andframe.annotation.interpreter.ViewBinder;
import com.andframe.application.AfApp;
import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;
import com.google.gson.internal.UnsafeAllocator;

public class AfViewModule extends AfViewWrapper implements AfViewable, IViewModule{

	public static <T extends AfViewModule> T init(Class<T> clazz, AfViewable viewable, int viewId) {
		try {
			T module = UnsafeAllocator.create().newInstance(clazz);
//            Constructor<?>[] constructors = clazz.getConstructors();
//            for (int i = 0; i < constructors.length && module == null; i++) {
//                Class<?>[] parameterTypes = constructors[i].getParameterTypes();
//                if (parameterTypes.length == 0) {
//                    module = clazz.newInstance();
//                } else if (parameterTypes.length == 1 && AfViewable.class.isAssignableFrom(parameterTypes[0])) {
//                    module = (T) constructors[i].newInstance(viewable);
//                }
//            }
			if (module != null && !module.isValid()) {
				AfViewModule viewModule = module;
				viewModule.setTarget(viewable, viewable.findViewByID(viewId));
			}
			return module;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static <T extends AfViewModule> T init(Class<T> clazz, AfViewable viewable) {
		int layoutId = LayoutBinder.getBindLayoutId(clazz, viewable.getContext(), AfViewModule.class);
		if (layoutId <= 0) {
			return null;
		}
		return init(clazz, viewable, layoutId);
	}

	protected AfViewModule(){
		super(new View(AfApplication.getApp()));
	}

	public AfViewModule(View view) {
		super(view);
	}

	protected AfViewModule(AfViewable view) {
		super(new View(view.getContext()));
		int layoutId = LayoutBinder.getBindLayoutId(this, view.getContext(), AfViewModule.class);
		if (layoutId > 0) {
			wrapped = view.findViewById(layoutId);
		} else {
			wrapped = null;
		}
	}

	protected AfViewModule(AfViewable view, int id) {
		super(new View(view.getContext()));
		wrapped = view.findViewById(id);
	}

	/**
	 * 如果不想要采用 注入的形式
	 * 子类构造函数中必须调用这个函数
	 */
	protected void initializeComponent(AfViewable viewable){
		if (wrapped == null ) {
			int layoutId = LayoutBinder.getBindLayoutId(this, viewable.getContext(), AfViewModule.class);
			if (layoutId > 0) {
				wrapped = viewable.findViewById(layoutId);
			}
		}
		setTarget(viewable, wrapped);
	}

	private void setTarget(final AfViewable viewable, final View target) {
		if (target != null) {
			this.wrapped = target;
			this.onCreated(viewable, target);
		}
	}

	protected void onCreated(AfViewable viewable, View view) {
		this.doInject();
	}

	protected void doInject(){
		if(isValid()){
			Injecter.doInject(this, getContext());
			ViewBinder.doBind(this, wrapped);
		}
	}

	@Override
	public void hide() {
		if(isValid()){
			setVisibility(View.GONE);
		}
	}

	@Override
	public void show() {
		if(isValid()){
			setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean isValid() {
		return wrapped != null;
	}

	@Override
	public boolean isVisibility() {
		if(isValid()){
			return getVisibility() == View.VISIBLE;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends View> T findViewByID(int id) {
		try {
			return (T) wrapped.findViewById(id);
		} catch (Throwable e) {
			AfExceptionHandler.handle(e, "AfViewModule.findViewByID");
		}
		return null;
	}

	@Override
	public <T extends View> T findViewById(int id, Class<T> clazz) {
		View view = wrapped.findViewById(id);
		if (clazz.isInstance(view)) {
			return clazz.cast(view);
		}
		return null;
	}


	/**
	 * 开始 IViewQuery 查询
	 * @param id 控件Id
	 */
	protected IViewQuery $(int... id) {
		IViewQuery query = AfApp.get().getViewQuery(wrapped);
		if (id == null || id.length == 0) {
			return query;
		}
		return query.$(id[0]);
	}

	@SuppressWarnings("unused")
	protected IViewQuery $(View view) {
		return AfApp.get().getViewQuery(view);
	}

}
