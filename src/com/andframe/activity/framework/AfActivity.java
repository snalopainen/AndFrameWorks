package com.andframe.activity.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andframe.annotation.inject.interpreter.Injecter;
import com.andframe.application.AfApplication;
import com.andframe.application.AfDaemonThread;
import com.andframe.application.AfExceptionHandler;
import com.andframe.exception.AfException;
import com.andframe.exception.AfToastException;
import com.andframe.feature.AfDailog;
import com.andframe.feature.AfIntent;
import com.andframe.feature.AfSoftInputer;
import com.andframe.feature.AfViewBinder;
import com.andframe.fragment.AfFragment;
import com.andframe.thread.AfTask;
import com.andframe.thread.AfThreadWorker;
import com.andframe.util.java.AfStackTrace;
/**
 * ��� Activity
 * @author SCWANG
 *
 *	������ Activity �������ṩ�� ���ܷ���
 *
	protected void buildThreadWorker()
	 * Ϊ��ҳ�濪��һ��������̨�߳� �� postTask �� ����(AfTask)���� ע�⣺�����߳�֮�� postTask
	 * �κ����񶼻��ڸ��߳������С� ��� postTask ǰһ������δ��ɣ���һ�����񽫵ȴ�
	 * 
	protected AfTask postTask(AfTask task)
	 * ��������Workerִ��

	AfPageable �ӿ��еķ���
	public Activity getActivity();
	public void makeToastLong(String tip);
	public void makeToastShort(String tip);
	public void makeToastLong(int resid);
	public void makeToastShort(int resid);
	public boolean getSoftInputStatus();
	public boolean getSoftInputStatus(View view);
	public void setSoftInputEnable(EditText editview, boolean enable);
	public void showProgressDialog(String message);
	public void showProgressDialog(String message, boolean cancel);
	public void showProgressDialog(String message, boolean cancel,int textsize);
	public void showProgressDialog(String message, listener);
	public void showProgressDialog(String message, listener, int textsize);
	public void hideProgressDialog();
	public void startActivity(Class<? extends AfActivity> tclass);
	public void startActivityForResult(Class<AfActivity> tclass,int request);
	
	public void doShowDialog(String title, String message);
	public void doShowDialog(String title, String message,OnClickListener);
	public void doShowDialog(String title, String message,String ,OnClickListener);
	public void doShowDialog(String, String,String,OnClickListener,String,OnClickListener);
	public void doShowDialog(String,String,String,Listener,String,Listener,String,Listener);
	public void doShowDialog(int,String,String,String,OnClickListener,String,OnClickListener);
	public void doShowDialog(int,String,String,String,Listener,String,Listener,String,Listener);
	
	public void doShowViewDialog(title, View view,String positive, OnClickListener );
	public void doShowViewDialog(title, View view,String positive, OnClickListener , String negative,OnClickListener );
	public void doShowViewDialog(title,view,String,Listener,String,Listener,String,Listener);
	public void doShowViewDialog(int iconres, title,  view,String, OnClickListener,String,OnClickListener );
	public void doShowViewDialog(int iconres,title,view,String,Listener,String,Listener,String,Listener);
	
	public void doSelectItem(String title,String[] items,OnClickListener);
	public void doSelectItem(String title,String[] items,OnClickListener,cancel);
	public void doSelectItem(String title,String[] items,OnClickListener,oncancel);
	
	public void doInputText(String title,InputTextListener listener);
	public void doInputText(String title,int type,InputTextListener listener);
	public void doInputText(String title,String defaul,int type,InputTextListener listener);
	
	AfPageListener �ӿ��еķ���
	public void onSoftInputShown();
	public void onSoftInputHiden();
	public void onQueryChanged();
}
 */
public abstract class AfActivity extends FragmentActivity implements AfPageable,OnItemClickListener {

	public static final String EXTRA_DATA = "EXTRA_DATA";
	public static final String EXTRA_INDEX = "EXTRA_INDEX";
	public static final String EXTRA_RESULT = "EXTRA_RESULT";

	public static final int LP_MP = LayoutParams.MATCH_PARENT;
	public static final int LP_WC = LayoutParams.WRAP_CONTENT;

	protected View mRoot = null;;
	protected ProgressDialog mProgress;
	protected AfThreadWorker mWorker = null;

	protected boolean mIsRecycled = false;

	/**
	 * @Description: ��ȡLOG��־ TAG �� AfActivity �ķ���
	 * �û�Ҳ������д�Զ���TAG,���ֵAfActivity����־��¼ʱ���ʹ��
	 * ����ʵ��Ҳ����ʹ��
	 * @Author: scwang
	 * @Version: V1.0, 2015-2-14 ����10:58:00
	 * @Modified: ���δ���TAG����
	 * @return
	 */
	protected String TAG() {
		// TODO Auto-generated method stub
		return "AfActivity("+getClass().getName()+")";
	}
	
	protected String TAG(String tag) {
		// TODO Auto-generated method stub
		return "AfActivity("+getClass().getName()+")."+tag;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
			super.onRestoreInstanceState(savedInstanceState);
		} catch (Throwable e) {
			// TODO: handle exception
			if (AfApplication.getApp().isDebug()) {
				AfExceptionHandler.handler(e, "AfActivity.onRestoreInstanceState");
			}
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		try {
			AfApplication.getApp().onSaveInstanceState();
			super.onSaveInstanceState(outState);
		} catch (Exception e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, "AfActivity.onSaveInstanceState");
		}
	}
	/**
	 * ��ȡ Application �� AfApplicationʵ��
	 * @return ��� Application ���� AfApplication ���� null 
	 */
	public AfApplication getAfApplication(){
		Application app = getApplication();
		if (app instanceof AfApplication) {
			return AfApplication.class.cast(app);
		}
		return null;
	}
	
	/**
	 * �ж��Ƿ񱻻���
	 * @return true �Ѿ�������
	 */
	@Override
	public boolean isRecycled() {
		// TODO Auto-generated method stub
		return mIsRecycled;
	}

	/**
	 * Ϊ��ʵ�ֶ����������뷨��ʾ������ �ļ�����д�� setContentView
	 * 	�����ڶ� setContentView ��д��ʱ������� 
	 * 		super.setContentView(res);
	 * 	�����ܶ������̽��м���
	 */
	@Override
	public void setContentView(int res) {
		// TODO Auto-generated method stub
		setContentView(LayoutInflater.from(this).inflate(res, null));
	}

	/**
	 * Ϊ��ʵ�ֶ����������뷨��ʾ������ �ļ�����д�� setContentView
	 * 	�����ڶ� setContentView ��д��ʱ������� 
	 * 		super.setContentView(view);
	 * 	�����ܶ������̽��м���
	 */
	@Override
	public void setContentView(View view) {
		// TODO Auto-generated method stub
		setContentView(view, new LayoutParams(LP_MP, LP_MP));
	}

	/**
	 * Ϊ��ʵ�ֶ����������뷨��ʾ������ �ļ�����д�� setContentView
	 * 	�����ڶ� setContentView ��д��ʱ������� 
	 * 		super.setContentView(view,params);
	 * 	�����ܶ������̽��м���
	 */
	@Override
	public void setContentView(View view, LayoutParams params) {
		// TODO Auto-generated method stub
		super.setContentView(view, params);
		mRoot = view;
		AfViewBinder binder;
		binder = new AfViewBinder(this);
		binder.doBind(view);
		AfSoftInputer inputer = new AfSoftInputer(this);
		inputer.setBindListener(view,this);
	}

	/**
	 * onGlobalLayout �������̵Ľ�ͧ���
	 * ����������ʾ
	 */
	public void onSoftInputShown() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * onGlobalLayout �������̵Ľ�ͧ���
	 * ��������������
	 */
	public void onSoftInputHiden() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AfApplication.getApp().setCurActivity(this, this);
		this.onQueryChanged();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mIsRecycled = true;
		if (mWorker != null) {
			mWorker.quit();
		}
	}

	/**
	 * ��ѯϵͳ���ݱ䶯
	 */
	public void onQueryChanged() {
		// TODO Auto-generated method stub
	}

	/**
	 * Ϊ��ҳ�濪��һ��������̨�߳� �� postTask �� ����(AfTask)���� ע�⣺�����߳�֮�� postTask
	 * �κ����񶼻��ڸ��߳������С� ��� postTask ǰһ������δ��ɣ���һ�����񽫵ȴ�
	 */
	protected void buildThreadWorker() {
		// TODO Auto-generated method stub
		if (mWorker == null) {
			mWorker = new AfThreadWorker(this.getClass().getSimpleName());
		}
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public Activity getActivity() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void startActivity(Class<? extends AfActivity> tclass) {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, tclass));
	}

	@Override
	public void startActivityForResult(Class<? extends AfActivity> tclass,
			int request) {
		// TODO Auto-generated method stub
		startActivityForResult(new Intent(this, tclass), request);
	}
	
	@Override
	public boolean getSoftInputStatus() {
		// TODO Auto-generated method stub
		return new AfSoftInputer(this).getSoftInputStatus();
	}
	
	@Override
	public boolean getSoftInputStatus(View view) {
		// TODO Auto-generated method stub
		return new AfSoftInputer(this).getSoftInputStatus(view);
	}

	@Override
	public void setSoftInputEnable(EditText editview, boolean enable) {
		// TODO Auto-generated method stub
		new AfSoftInputer(this).setSoftInputEnable(editview, enable);
	}

	@Override
	public void makeToastLong(int resid) {
		// TODO Auto-generated method stub
		Toast.makeText(this, resid, Toast.LENGTH_LONG).show();
	}

	@Override
	public void makeToastShort(int resid) {
		// TODO Auto-generated method stub
		Toast.makeText(this, resid, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void makeToastLong(String tip) {
		// TODO Auto-generated method stub
		Toast.makeText(this, tip, Toast.LENGTH_LONG).show();
	}

	@Override
	public void makeToastShort(String tip) {
		// TODO Auto-generated method stub
		Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void makeToastLong(String tip,Throwable e) {
		// TODO Auto-generated method stub
		tip = AfException.handle(e, tip);
		Toast.makeText(this, tip, Toast.LENGTH_LONG).show();
	}

	@Override
	public <T extends View> T findViewById(int id, Class<T> clazz) {
		// TODO Auto-generated method stub
		View view = findViewById(id);
		if (clazz.isInstance(view)) {
			return clazz.cast(view);
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends View> T findViewByID(int id) {
		// TODO Auto-generated method stub
		try {
			return (T)findViewById(id);
		} catch (Exception e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, TAG("findViewByID"));
		}
		return null;
	}
	/**
	 * ��������Workerִ��
	 * @param task
	 */
	public AfTask postTask(AfTask task) {
		// TODO Auto-generated method stub
		if (mWorker != null) {
			return mWorker.postTask(task);
		}
		return AfDaemonThread.postTask(task);
	}

	/**
	 * ��ʾ ���ȶԻ���
	 * @param message ��Ϣ
	 */
	public void showProgressDialog(String message) {
		// TODO Auto-generated method stub
		showProgressDialog(message, false, 25);
	}

	/**
	 * ��ʾ ���ȶԻ���
	 * @param message ��Ϣ
	 * @param cancel �Ƿ��ȡ��
	 */
	public void showProgressDialog(String message, boolean cancel) {
		// TODO Auto-generated method stub
		showProgressDialog(message, cancel, 25);
	}

	/**
	 * ��ʾ ���ȶԻ���
	 * @param message ��Ϣ
	 * @param cancel �Ƿ��ȡ��
	 * @param textsize �����С
	 */
	public void showProgressDialog(String message, boolean cancel,
			int textsize) {
		// TODO Auto-generated method stub
		try {
			mProgress = new ProgressDialog(this);
			mProgress.setMessage(message);
			mProgress.setCancelable(cancel);
			mProgress.setOnCancelListener(null);
			mProgress.show();

			setDialogFontSize(mProgress, textsize);
		} catch (Exception e) {
			// TODO: handle exception
			//������־��֤������쳣�ᷢ�ͣ����Ǹ��ʷǳ�С��ע�͵��쳣֪ͨ
//			AfExceptionHandler.handler(e, "AfActivity.showProgressDialog");
		}
	}

	/**
	 * ��ʾ ���ȶԻ���
	 * @param message ��Ϣ
	 * @param cancel �Ƿ��ȡ��
	 * @param textsize �����С
	 */
	public void showProgressDialog(String message,
			OnCancelListener listener) {
		// TODO Auto-generated method stub
		try {
			mProgress = new ProgressDialog(this);
			mProgress.setMessage(message);
			mProgress.setCancelable(true);
			mProgress.setOnCancelListener(listener);
			mProgress.show();
			
			setDialogFontSize(mProgress, 25);
		} catch (Exception e) {
			// TODO: handle exception
			//������־��֤������쳣�ᷢ�ͣ����Ǹ��ʷǳ�С��ע�͵��쳣֪ͨ
//			AfExceptionHandler.handler(e, "AfActivity.showProgressDialog");
		}
	}

	/**
	 * ��ʾ ���ȶԻ���
	 * @param message ��Ϣ
	 * @param cancel �Ƿ��ȡ��
	 * @param textsize �����С
	 */
	public void showProgressDialog(String message,
			OnCancelListener listener, int textsize) {
		// TODO Auto-generated method stub
		try {
			mProgress = new ProgressDialog(this);
			mProgress.setMessage(message);
			mProgress.setCancelable(true);
			mProgress.setOnCancelListener(listener);
			mProgress.show();
			
			setDialogFontSize(mProgress, textsize);
		} catch (Exception e) {
			// TODO: handle exception
			//������־��֤������쳣�ᷢ�ͣ����Ǹ��ʷǳ�С��ע�͵��쳣֪ͨ
//			AfExceptionHandler.handler(e, "AfActivity.showProgressDialog");
		}
	}

	/**
	 * ���� ���ȶԻ���
	 */
	public void hideProgressDialog() {
		// TODO Auto-generated method stub
		try {
			if (mProgress != null && !isRecycled()) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, "AfActivity.hideProgressDialog");
		}
	}

	/**
	 * ��ʾ�Ի��� ������Ĭ�ϰ�ť "��֪����"
	 * @param title ��ʾ����
	 * @param message ��ʾ����
	 */
	public void doShowDialog(String title, String message) {
		doShowDialog(0,title,message,"��֪����", null, "", null);
	}
	/**
	 * ��ʾ�Ի��� ������Ĭ�ϰ�ť "��֪����"
	 * @param title ��ʾ����
	 * @param message ��ʾ����
	 * @param lpositive ���  "��֪����" ��Ӧ�¼�
	 */
	public void doShowDialog(String title, String message,OnClickListener lpositive) {
		doShowDialog(0,title,message,"��֪����", lpositive, "", null);
	}
	/**
	 * ��ʾ�Ի��� 
	 * @param title ��ʾ����
	 * @param message ��ʾ����
	 * @param positive ȷ�� ��ť��ʾ��Ϣ
	 * @param lpositive ���  ȷ�� ��ť ��Ӧ�¼�
	 */
	public void doShowDialog(String title, String message,String positive,OnClickListener lpositive) {
		doShowDialog(0,title,message,positive, lpositive, "", null);
	}
	/**
	 * ��ʾ�Ի��� 
	 * @param title ��ʾ����
	 * @param message ��ʾ����
	 * @param positive ȷ�� ��ť��ʾ��Ϣ
	 * @param lpositive ���  ȷ�� ��ť ��Ӧ�¼�
	 * @param negative ��ť��ʾ��Ϣ
	 * @param lnegative ���  �ܾ� ��ť ��Ӧ�¼�
	 */
	public void doShowDialog(String title, String message,
			String positive, OnClickListener lpositive, String negative,
			OnClickListener lnegative) {
		doShowDialog(0,title,message,positive, lpositive,negative,lnegative);
	}
	/**
	 * ��ʾ�Ի��� 
	 * @param title ��ʾ����
	 * @param message ��ʾ����
	 * @param positive ȷ�� ��ť��ʾ��Ϣ
	 * @param lpositive ���  ȷ�� ��ť ��Ӧ�¼�
	 * @param neutral ��ϸ ��ť��ʾ��Ϣ
	 * @param lneutral ���  ��ϸ ��ť ��Ӧ�¼�
	 * @param negative ��ť��ʾ��Ϣ
	 * @param lnegative ���  �ܾ� ��ť ��Ӧ�¼�
	 */
	@Override
	public void doShowDialog(String title, String message,
			String positive, OnClickListener lpositive, 
			String neutral, OnClickListener lneutral, 
			String negative,OnClickListener lnegative){
		doShowDialog(0, title, message,positive, lpositive, neutral, lneutral, negative,lnegative);
	}
	
	/**
	 * ��ʾ�Ի��� 
	 * @param iconres �Ի���ͼ��
	 * @param title ��ʾ����
	 * @param message ��ʾ����
	 * @param positive ȷ�� ��ť��ʾ��Ϣ
	 * @param lpositive ���  ȷ�� ��ť ��Ӧ�¼�
	 * @param negative ��ť��ʾ��Ϣ
	 * @param lnegative ���  �ܾ� ��ť ��Ӧ�¼�
	 */
	public void doShowDialog(int iconres, String title, String message,
			String positive, OnClickListener lpositive, String negative,
			OnClickListener lnegative) {
		doShowDialog(iconres, title, message,positive, lpositive, "", null, negative,lnegative);
	}

	/**
	 * ��ʾ�Ի��� 
	 * @param iconres �Ի���ͼ��
	 * @param title ��ʾ����
	 * @param message ��ʾ����
	 * @param positive ȷ�� ��ť��ʾ��Ϣ
	 * @param lpositive ���  ȷ�� ��ť ��Ӧ�¼�
	 * @param neutral ��ϸ ��ť��ʾ��Ϣ
	 * @param lneutral ���  ��ϸ ��ť ��Ӧ�¼�
	 * @param negative ��ť��ʾ��Ϣ
	 * @param lnegative ���  �ܾ� ��ť ��Ӧ�¼�
	 */
	public void doShowDialog(int iconres, String title, String message,
			String positive, OnClickListener lpositive, 
			String neutral, OnClickListener lneutral, 
			String negative,OnClickListener lnegative) {
		// TODO Auto-generated method stub
		doShowDialog(-1, iconres, title, message, positive, lpositive, neutral, lneutral, negative, lnegative);
	}

	/**
	 * ��ʾ��ͼ�Ի��� 
	 * @param theme ����
	 * @param iconres �Ի���ͼ��
	 * @param title ��ʾ����
	 * @param message ��ʾ����
	 * @param positive ȷ�� ��ť��ʾ��Ϣ
	 * @param lpositive ���  ȷ�� ��ť ��Ӧ�¼�
	 * @param neutral ��ϸ ��ť��ʾ��Ϣ
	 * @param lneutral ���  ��ϸ ��ť ��Ӧ�¼�
	 * @param negative ��ť��ʾ��Ϣ
	 * @param lnegative ���  �ܾ� ��ť ��Ӧ�¼�
	 */
	@Override
	@SuppressLint("NewApi")
	public void doShowDialog(int theme, int iconres, 
			String title,String message, 
			String positive, OnClickListener lpositive,
			String neutral, OnClickListener lneutral, 
			String negative,OnClickListener lnegative) {
		// TODO Auto-generated method stub
		new AfDailog(this).doShowDialog(theme, iconres, title, message, positive, lpositive, neutral, lneutral, negative, lnegative);
	}
	/**
	 * ��ʾ��ͼ�Ի��� 
	 * @param title ��ʾ����
	 * @param view ��ʾ����
	 * @param positive ȷ�� ��ť��ʾ��Ϣ
	 * @param lpositive ���  ȷ�� ��ť ��Ӧ�¼�
	 */
	@Override
	public void doShowViewDialog(String title, View view, String positive,
			OnClickListener lpositive) {
		// TODO Auto-generated method stub
		doShowViewDialog(title, view, positive, lpositive,"",null);
	}

	/**
	 * ��ʾ��ͼ�Ի��� 
	 * @param title ��ʾ����
	 * @param view ��ʾ����
	 * @param positive ȷ�� ��ť��ʾ��Ϣ
	 * @param lpositive ���  ȷ�� ��ť ��Ӧ�¼�
	 * @param negative ��ť��ʾ��Ϣ
	 * @param lnegative ���  �ܾ� ��ť ��Ӧ�¼�
	 */
	@Override
	public void doShowViewDialog(String title, View view, String positive,
			OnClickListener lpositive, String negative,
			OnClickListener lnegative) {
		// TODO Auto-generated method stub
		doShowViewDialog(0,title,view,positive, lpositive,negative,lnegative);
	}
	/**
	 * ��ʾ��ͼ�Ի��� 
	 * @param title ��ʾ����
	 * @param view ��ʾ����
	 * @param positive ȷ�� ��ť��ʾ��Ϣ
	 * @param lpositive ���  ȷ�� ��ť ��Ӧ�¼�
	 * @param neutral ��ϸ ��ť��ʾ��Ϣ
	 * @param lneutral ���  ��ϸ ��ť ��Ӧ�¼�
	 * @param negative ��ť��ʾ��Ϣ
	 * @param lnegative ���  �ܾ� ��ť ��Ӧ�¼�
	 */
	@Override
	public void doShowViewDialog(String title, View view,
			String positive, OnClickListener lpositive, 
			String neutral, OnClickListener lneutral, 
			String negative,OnClickListener lnegative) {
		doShowViewDialog(0,title,view,positive, lpositive,neutral,lneutral,negative,lnegative);
	}
	/**
	 * ��ʾ��ͼ�Ի��� 
	 * @param iconres �Ի���ͼ��
	 * @param title ��ʾ����
	 * @param view ��ʾ����
	 * @param positive ȷ�� ��ť��ʾ��Ϣ
	 * @param lpositive ���  ȷ�� ��ť ��Ӧ�¼�
	 * @param negative ��ť��ʾ��Ϣ
	 * @param lnegative ���  �ܾ� ��ť ��Ӧ�¼�
	 */
	@Override
	public void doShowViewDialog(int iconres, String title, View view,
			String positive, OnClickListener lpositive, 
			String negative,OnClickListener lnegative) {
		doShowViewDialog(0,title,view,positive, lpositive,"",null,negative,lnegative);
	}
	/**
	 * ��ʾ��ͼ�Ի��� 
	 * @param iconres �Ի���ͼ��
	 * @param title ��ʾ����
	 * @param view ��ʾ����
	 * @param positive ȷ�� ��ť��ʾ��Ϣ
	 * @param lpositive ���  ȷ�� ��ť ��Ӧ�¼�
	 * @param neutral ��ϸ ��ť��ʾ��Ϣ
	 * @param lneutral ���  ��ϸ ��ť ��Ӧ�¼�
	 * @param negative ��ť��ʾ��Ϣ
	 * @param lnegative ���  �ܾ� ��ť ��Ӧ�¼�
	 */
	@Override
	public void doShowViewDialog(int iconres, String title, View view,
			String positive, OnClickListener lpositive, 
			String neutral, OnClickListener lneutral, 
			String negative,OnClickListener lnegative) {
		// TODO Auto-generated method stub
		doShowViewDialog(-1, iconres, title, view, positive, lpositive, neutral, lneutral, negative, lnegative);
	}

	/**
	 * ��ʾ��ͼ�Ի��� 
	 * @param theme ����
	 * @param iconres �Ի���ͼ��
	 * @param title ��ʾ����
	 * @param view ��ʾ����
	 * @param positive ȷ�� ��ť��ʾ��Ϣ
	 * @param lpositive ���  ȷ�� ��ť ��Ӧ�¼�
	 * @param neutral ��ϸ ��ť��ʾ��Ϣ
	 * @param lneutral ���  ��ϸ ��ť ��Ӧ�¼�
	 * @param negative ��ť��ʾ��Ϣ
	 * @param lnegative ���  �ܾ� ��ť ��Ӧ�¼�
	 */
	@SuppressLint("NewApi")
	@Override
	public void doShowViewDialog(int theme, 
			int iconres, String title,View view, 
			String positive, OnClickListener lpositive,
			String neutral, OnClickListener lneutral, 
			String negative,OnClickListener lnegative) {
		// TODO Auto-generated method stub
		new AfDailog(this).doShowViewDialog(theme, iconres, title, view, positive, lpositive, neutral, lneutral, negative, lnegative);
	}
	/**
	 * ��ʾһ����ѡ�Ի��� �����ÿ�ȡ����
	 * @param title �Ի������
	 * @param items ѡ��˵���
	 * @param listener ѡ�������
	 * @param cancel ȡ��ѡ�������
	 */
	public void doSelectItem(String title,String[] items,OnClickListener listener,
			boolean cancel){
		new AfDailog(this).doSelectItem(title, items, listener, cancel);
	}

	/**
	 * ��ʾһ����ѡ�Ի��� 
	 * @param title �Ի������
	 * @param items ѡ��˵���
	 * @param listener ѡ�������
	 * @param oncancel ȡ��ѡ�������
	 */
	public void doSelectItem(String title,String[] items,OnClickListener listener,
			final OnClickListener oncancel) {
		// TODO Auto-generated method stub
		new AfDailog(this).doSelectItem(title, items, listener, oncancel);
	}

	/**
	 * ��ʾһ����ѡ�Ի��� ��Ĭ�Ͽ�ȡ����
	 * @param title �Ի������
	 * @param items ѡ��˵���
	 * @param listener ѡ�������
	 */
	public void doSelectItem(String title,String[] items,OnClickListener listener) {
		// TODO Auto-generated method stub
		doSelectItem(title, items, listener, null);
	}

	/**
	 * ����һ���ı������
	 * @param title ����
	 * @param listener ������
	 */
	public void doInputText(String title,InputTextListener listener) {
		doInputText(title, "", InputType.TYPE_CLASS_TEXT, listener);
	}

	/**
	 * ����һ���ı������
	 * @param title ����
	 * @param type android.text.InputType
	 * @param listener ������
	 */
	public void doInputText(String title,int type,InputTextListener listener) {
		doInputText(title, "", type, listener);
	}

	/**
	 * ����һ���ı������
	 * @param title ����
	 * @param defaul Ĭ��ֵ
	 * @param type android.text.InputType
	 * @param listener ������
	 */
	public void doInputText(String title,String defaul,int type,InputTextListener listener) {
		new AfDailog(this).doInputText(title, defaul, type, listener);
	}
	
	protected void setProgressDialogText(ProgressDialog dialog, String text) {
		Window window = dialog.getWindow();
		View view = window.getDecorView();
		setViewFontText(view, text);
	}

	private void setViewFontText(View view, String text) {
		// TODO Auto-generated method stub
		if (view instanceof ViewGroup) {
			ViewGroup parent = (ViewGroup) view;
			int count = parent.getChildCount();
			for (int i = 0; i < count; i++) {
				setViewFontText(parent.getChildAt(i), text);
			}
		} else if (view instanceof TextView) {
			TextView textview = (TextView) view;
			textview.setText(text);
		}
	}

	protected void setDialogFontSize(Dialog dialog, int size) {
		Window window = dialog.getWindow();
		View view = window.getDecorView();
		setViewFontSize(view, size);
	}

	protected void setViewFontSize(View view, int size) {
		if (view instanceof ViewGroup) {
			ViewGroup parent = (ViewGroup) view;
			int count = parent.getChildCount();
			for (int i = 0; i < count; i++) {
				setViewFontSize(parent.getChildAt(i), size);
			}
		} else if (view instanceof TextView) {
			TextView textview = (TextView) view;
			textview.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
		}
	}

	/**
	 * ת�� onKeyLongPress �¼��� AfFragment
	 */
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		boolean isHandled = false;
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		fragments = fragments == null ? new ArrayList<Fragment>() : fragments;
		for (Fragment fragment : fragments) {
			if(fragment.getUserVisibleHint() && fragment instanceof AfFragment){
				AfFragment afment = (AfFragment)fragment;
				isHandled = afment.onKeyLongPress(keyCode,event) || isHandled;
			}
		}
		if(isHandled){
			return true;
		}
		return super.onKeyLongPress(keyCode, event);
	}

	/**
	 * ת�� onKeyShortcut �¼��� AfFragment
	 */
	@Override
	@SuppressLint("NewApi")
	public boolean onKeyShortcut(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		boolean isHandled = false;
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		fragments = fragments == null ? new ArrayList<Fragment>() : fragments;
		for (Fragment fragment : fragments) {
			if(fragment.getUserVisibleHint() && fragment instanceof AfFragment){
				AfFragment afment = (AfFragment)fragment;
				isHandled = afment.onKeyShortcut(keyCode,event) || isHandled;
			}
		}
		if(isHandled){
			return true;
		}
		return super.onKeyShortcut(keyCode, event);
	}
	/**
	 * ת�� onKeyMultiple �¼��� AfFragment
	 */
	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		// TODO Auto-generated method stub
		boolean isHandled = false;
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		fragments = fragments == null ? new ArrayList<Fragment>() : fragments;
		for (Fragment fragment : fragments) {
			if(fragment.getUserVisibleHint() && fragment instanceof AfFragment){
				AfFragment afment = (AfFragment)fragment;
				isHandled = afment.onKeyMultiple(keyCode,repeatCount,event) || isHandled;
			}
		}
		if(isHandled){
			return true;
		}
		return super.onKeyMultiple(keyCode, repeatCount, event);
	}

	/**
	 * ת�� onKeyUp �¼��� AfFragment
	 */
	@Override
	@SuppressLint("NewApi")
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		boolean isHandled = false;
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		fragments = fragments == null ? new ArrayList<Fragment>() : fragments;
		for (Fragment fragment : fragments) {
			if(fragment.getUserVisibleHint() && fragment instanceof AfFragment){
				AfFragment afment = (AfFragment)fragment;
				isHandled = afment.onKeyUp(keyCode,event) || isHandled;
			}
		}
		if(isHandled){
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * ת�� onKeyDown �¼��� AfFragment
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		boolean isHandled = false;
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		fragments = fragments == null ? new ArrayList<Fragment>() : fragments;
		for (Fragment fragment : fragments) {
			if(fragment.getUserVisibleHint() && fragment instanceof AfFragment){
				AfFragment afment = (AfFragment)fragment;
				isHandled = afment.onKeyDown(keyCode,event) || isHandled;
			}
		}
		if(isHandled){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * final ԭʼ onCreate(Bundle bundle)
	 * ����ֻ����д onCreate(Bundle bundle,AfIntent intent)
	 */
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		try {
			if (AfStackTrace.isLoopCall()) {
				//System.out.println("�ݹ���");
				super.onCreate(bundle);
				return;
			}
			Injecter injecter = new Injecter(this);
			injecter.doInject(this);
			this.onCreate(bundle, new AfIntent(getIntent()));
		} catch (final Throwable e) {
			// TODO: handle exception
			//handler ���ܻ���� Activity ������ʾ������Ϣ
			//��ǰ Activity �����رգ���ʾ����Ҳ��ر�
			//�ö�ʱ�� �ȵ�ԭʼ Activity ����ʾ����
			if (!(e instanceof AfToastException)) {
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						AfExceptionHandler.handler(e, TAG()+".onCreate");
					}
				},500);
			}
			makeToastLong("ҳ������ʧ��",e);
			this.finish();
		}
	}

	/**
	 * �µ� onCreate ʵ��
	 * @param bundle
	 * @param intent 
	 * @throws Exception ��ȫ�쳣
	 * 	��д�� ʱ�� һ��������� ����
	 * 		super.onCreate(bundle,intent);
	 */
	protected void onCreate(Bundle bundle,AfIntent intent) throws Exception{
		super.onCreate(bundle);
		if(bundle != null){
			AfApplication.getApp().onRestoreInstanceState();
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 * final ��д onActivityResult ʹ�� try-catch ���� 
	 * 		onActivityResult(AfIntent intent, int questcode,int resultcode)
	 * @see AfActivity#onActivityResult(AfIntent intent, int questcode,int resultcode)
	 * {@link AfActivity#onActivityResult(AfIntent intent, int questcode,int resultcode)}
	 */
	@Override
	protected void onActivityResult(int questcode, int resultcode, Intent data) {
		// TODO Auto-generated method stub
		try {
			if (AfStackTrace.isLoopCall()) {
				//System.out.println("�ݹ���");
				return;
			}
			onActivityResult(new AfIntent(data), questcode, resultcode);
		} catch (Throwable e) {
			// TODO: handle exception
			if (!(e instanceof AfToastException)) {
				AfExceptionHandler.handler(e, TAG()+".onActivityResult");
			}
			makeToastLong("������Ϣ��ȡ����",e);
		}
	}
	
	/**
	 * @Description: final ��װ onItemClick �¼����� ��ֹ�׳��쳣����
	 * @Author: scwang
	 * @Version: V1.0, 2015-2-14 ����10:34:56
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		try {
			if (AfStackTrace.isLoopCall()) {
				//System.out.println("�ݹ���");
				return;
			}
			this.onItemClick(parent,view,id,position);
		} catch (Exception e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, TAG()+".onItemClick");
		}
	}

	/**
	 * @Description: 
	 * ��ȫonItemClick��ܻᲶ׽�쳣��ֹ����
	 * @Author: scwang
	 * @Version: V1.0, 2015-2-14 ����10:38:56
	 * @Modified: ���δ���onItemClick����
	 * @param parent
	 * @param item
	 * @param id
	 * @param index
	 */
	protected void onItemClick(AdapterView<?> parent, View item, long id,int index) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * ��ȫ onActivityResult(AfIntent intent, int questcode,int resultcode) 
	 * ��onActivityResult(int questCode, int resultCode, Intent data) �е���
	 * ��ʹ�� try-catch ��߰�ȫ�ԣ���������д������� 
	 * @see AfActivity#onActivityResult(int, int, android.content.Intent)
	 * {@link AfActivity#onActivityResult(int, int, android.content.Intent)}
	 * @param intent
	 * @param questcode
	 * @param resultcode
	 */
	protected void onActivityResult(AfIntent intent, int questcode,int resultcode) {
		// TODO Auto-generated method stub
		super.onActivityResult(questcode, resultcode, intent);
	}

	/**
	 * ת�� onBackPressed �¼��� AfFragment
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (AfStackTrace.isLoopCall()) {
			super.onBackPressed();
			return;
		}
		
		if(!this.onBackKeyPressed()){
			super.onBackPressed();
		}
	}

	/**
	 * ת�� onBackPressed �¼��� AfFragment
	 */
	protected boolean onBackKeyPressed() {
		// TODO Auto-generated method stub
		boolean isHandled = false;
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		fragments = fragments == null ? new ArrayList<Fragment>() : fragments;
		for (Fragment fragment : fragments) {
			if(fragment.getUserVisibleHint() && fragment instanceof AfFragment){
				AfFragment afment = (AfFragment)fragment;
				isHandled = afment.onBackPressed() || isHandled;
			}
		}
		return isHandled;
	}
}