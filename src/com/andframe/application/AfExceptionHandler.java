package com.andframe.application;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;

import com.andframe.activity.framework.AfActivity;
import com.andframe.caches.AfDurableCache;
import com.andframe.exception.AfToastException;
import com.andframe.helper.android.AfDeviceInfo;
import com.andframe.model.Exceptional;
import com.andframe.thread.AfDispatcher;
import com.andframe.util.java.AfDateFormat;
import com.andframe.util.java.AfDateGuid;

import java.io.FileWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;
import java.util.HashMap;

public class AfExceptionHandler implements UncaughtExceptionHandler{

	protected static final String DURABLE_HANDLER = "63214261915190904102";
	protected static final String DURABLE_UNCAUGHT = "02589350915190904102";

	protected static boolean mIsShowDialog = false;
	protected static AfExceptionHandler INSTANCE = null;
	protected Thread.UncaughtExceptionHandler mDefaultHandler;

	public AfExceptionHandler() {
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	public static void register() {
		if(INSTANCE == null){
			INSTANCE = AfApplication.getApp().getExceptionHandler();
		}
	}

	public static AfExceptionHandler getInstance() {
		return INSTANCE;
	}

	public String onHandleTip(Throwable e, String tip) {
		int index = 0;
		Throwable ex = e;
		while (!(ex instanceof AfToastException) && ex.getCause() != null && ++index < 5) {
			ex = e.getCause();
		}
		if(ex instanceof AfToastException){
			return ex.getMessage();
		}
		String message = e.getMessage();
		boolean debug = AfApplication.getApp().isDebug();
		if (debug) {
			message = "内容:" + message;
			message = String.format("异常:%s\r\n%s",e.getClass().getName(),message);
			if(tip != null && !tip.equals("")){
				return String.format("消息:%s\r\n%s",tip,message);
			}
		}else {
			if(message == null || message.trim().equals("")){
				message = e.getClass().getName();
			}
			if(tip != null && !tip.equals("")){
				return tip;
//				return String.format("%s:%s",tip,message);
			}
		}
		return message;
	}

	@Override
	public void uncaughtException(final Thread thread, final Throwable ex) {
		try {
			ex.printStackTrace();
			final String msg = "日期:" +AfDateFormat.FULL.format(new Date()) +
					"\r\n\r\n备注:\r\n" + "程序崩溃" +
					"\r\n\r\n异常:\r\n" + getExceptionName(ex) +
					"\r\n\r\n信息:\r\n" + getExceptionMessage(ex) +
					"\r\n\r\n快捷:\r\n" + getPackageStackTraceInfo(ex) +
					"\r\n\r\n堆栈:\r\n" + getStackTraceInfo(ex);
//			AfDurableCache dc = AfDurableCache.getInstance("error");
//			dc.put(AfDateGuid.NewID(), msg);
			String path = AfDurableCache.getPath();
			FileWriter writer = new FileWriter(path+"/error-"+AfDateFormat.format("y-M-d$HH-mm-ss",new Date())+".txt");
			writer.write(msg);
			writer.close();

			final AfActivity activity = AfApplication.getApp().getCurActivity();
			if (activity != null && mIsShowDialog) {
				AfDispatcher.dispatch(new Runnable() {
					@Override
					public void run() {
						doShowDialog(AfApplication.getApp().getCurActivity(), "程序崩溃了", msg, new Handler.Callback() {
							@Override
							public boolean handleMessage(Message msg) {
								mDefaultHandler.uncaughtException(thread, ex);
								return false;
							}
						});
					}
				}, 1000);
			}else{
				mDefaultHandler.uncaughtException(thread, ex);
			}
			return;
		} catch (Throwable e) {
			e.printStackTrace();
		}

		if(mDefaultHandler != null){
			mDefaultHandler.uncaughtException(thread, ex);
		}
	}

	protected void onHandleAttachException(Throwable ex, String remark, final String handlerid) {
		try {
			final String msg = "时间:" + AfDateFormat.FULL.format(new Date()) +
					"\r\n\r\n备注:\r\n" + "Attach-"+remark +
					"\r\n\r\n异常:\r\n" + getExceptionName(ex) +
					"\r\n\r\n信息:\r\n" + getExceptionMessage(ex) +
					"\r\n\r\n快捷:\r\n" + getPackageStackTraceInfo(ex) +
					"\r\n\r\n堆栈:\r\n" + getStackTraceInfo(ex);
//			AfDurableCache dc = AfDurableCache.getInstance("attach");
//			dc.put(AfDateGuid.NewID(), msg);
			String path = AfDurableCache.getPath();
			FileWriter writer = new FileWriter(path+"/attach-"+AfDateFormat.format("y-M-d$HH-mm-ss",new Date())+".txt");
			writer.write(msg);
			writer.close();


			AfActivity activity = AfApplication.getApp().getCurActivity();
			if (activity != null && mIsShowDialog) {
				AfDispatcher.dispatch(new Runnable() {
					@Override
					public void run() {
						doShowDialog(AfApplication.getApp().getCurActivity(), "异常捕捉", msg, handlerid);
					}
				},1000);
			}

		} catch (Throwable ignored) {
		}
	}

	protected void onHandleException(Throwable ex, String remark, final String handlerid) {
		try {
			final String msg = "时间:" + AfDateFormat.FULL.format(new Date()) +
					"\r\n\r\n备注:\r\n" + remark +
					"\r\n\r\n异常:\r\n" + getExceptionName(ex) +
					"\r\n\r\n信息:\r\n" + getExceptionMessage(ex) +
					"\r\n\r\n快捷:\r\n" + getPackageStackTraceInfo(ex) +
					"\r\n\r\n堆栈:\r\n" + getStackTraceInfo(ex);
//			AfDurableCache dc = AfDurableCache.getInstance("handler");
//			dc.put(AfDateGuid.NewID(), msg);
			String path = AfDurableCache.getPath();
			FileWriter writer = new FileWriter(path+"/handler-"+AfDateFormat.format("y-M-d$HH-mm-ss",new Date())+".txt");
			writer.write(msg);
			writer.close();

			AfActivity activity = AfApplication.getApp().getCurActivity();
			if (activity != null && mIsShowDialog) {
				AfDispatcher.dispatch(new Runnable() {
					@Override
					public void run() {
						doShowDialog(AfApplication.getApp().getCurActivity(),"异常捕捉",msg,handlerid);
					}
				});
			}

		} catch (Throwable ignored) {
		}
	}

	public static Exceptional getHandler(Throwable ex,String remark) {
		return getHandler(Thread.currentThread(),ex,remark);
	}

	public static Exceptional getHandler(Thread thread,Throwable ex,String remark) {
		Exceptional ehandler = new Exceptional();
		ehandler.Thread = "异常线程：" + thread;

		ehandler.Remark = remark;
		ehandler.Name = getExceptionName(ex);
		ehandler.Message = getExceptionMessage(ex);
		ehandler.Stack = getAllStackTraceInfo(ex);
		ehandler.Version = AfApplication.getVersion();
		ehandler.Device = AfDeviceInfo.detDeviceMessage();

		return ehandler;
	}

	public static String tip(Throwable e, String tip) {
		if (INSTANCE != null) {
			return INSTANCE.onHandleTip(e, tip);
		}
		return tip;
	}

	public static void handleAttach(Throwable ex,String remark) {
		if(INSTANCE != null && !(ex instanceof AfToastException)){
			String handlerid;
			StackTraceElement[] stacks = ex.getStackTrace();
			if(stacks != null && stacks.length > 0){
				handlerid = stacks[0].toString();
			}else{
				handlerid = AfDateGuid.NewID();
			}
			INSTANCE.onHandleAttachException(ex, remark, handlerid);
		}
	}

	public static void handle(String message, String remark) {
		if(INSTANCE != null){
			String handlerid = ""+(message+remark).hashCode();
			Throwable ex = new Exception(message);
			ex.printStackTrace();
			INSTANCE.onHandleException(ex, remark, handlerid);
		}
	}

	public static void handle(Throwable ex, String remark) {
		if(INSTANCE != null && !(ex instanceof AfToastException)){
			ex.printStackTrace();
			String handlerid;
			StackTraceElement[] stacks = ex.getStackTrace();
			if(stacks != null && stacks.length > 0){
				handlerid = stacks[0].toString();
			}else{
				handlerid = AfDateGuid.NewID();
			}
			INSTANCE.onHandleException(ex, remark, handlerid);
		}
	}

	public static String getAllStackTraceInfo(Throwable ex) {
		StringBuilder sb = new StringBuilder();
		sb.append("本地推栈:\r\n");
		sb.append(getPackageStackTraceInfo(ex));
		sb.append("\r\n全部堆栈:\r\n");
		sb.append(getStackTraceInfo(ex));
		return sb.toString();
	}

	public static String getExceptionMessage(Throwable ex) {
		String message = ex.getMessage();
		ex = ex.getCause();
		if (ex != null) {
			String format = "%s\r\n-------------------------->\r\n%s";
			message = String.format(format, getExceptionMessage(ex),message);
		}
		return message;
	}

	public static String getExceptionName(Throwable ex) {
		String name = ex.getClass().toString();
		ex = ex.getCause();
		if (ex != null) {
			String format = "%s -> %s";
			name = String.format(format, getExceptionName(ex),name);
		}
		return name;
	}

	public static String getPackageStackTraceInfo(Throwable ex) {
		String TraceInfo = "";
		String Package = AfApplication.getApp().getPackageName() + ".";
		for (StackTraceElement stack : ex.getStackTrace()) {
			if (stack.getClassName().startsWith(Package)) {
				TraceInfo += stack.toString() + "\r\n";
			}
		}
		if (ex.getCause() != null) {
			String info = getPackageStackTraceInfo(ex.getCause());
			if (info != null && info.length() > 0) {
				String format = "%s\r\n-------------------------->\r\n%s";
				TraceInfo = String.format(format,info,TraceInfo);
			}
		}
		return TraceInfo;
	}

	public static String getStackTraceInfo(Throwable ex) {
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getClass().toString());
		builder.append("\r\n");
		for (StackTraceElement stack : ex.getStackTrace()) {
			builder.append(stack);
			builder.append("\r\n");
		}
		String TraceInfo = builder.toString();
		ex = ex.getCause();
		if (ex != null) {
			String format = "%s\r\n-------------------------->\r\n%s";
			TraceInfo = String.format(format,getStackTraceInfo(ex),TraceInfo);
		}
		return TraceInfo;
	}

	public static HashMap<String, String> mDialogMap = new HashMap<String, String>();

	public static synchronized void doShowDialog(Context activity, String title, String msg,Callback callback, Looper looper,String id) {
		if(mDialogMap.containsKey(id)){
			return;
		}
		final String tid = id;
		final String ttitle = title;
		final String tmsg = msg;
		final Callback tcallback = callback;
		final Context tactivity = activity;
		final Looper tLooper = looper;
		new Thread(){
			public void run() {
				try {
					Looper.prepare();
					mDialogMap.put(tid, ttitle);
					Builder dialog = new Builder(tactivity);
					dialog.setTitle(ttitle);
					dialog.setCancelable(false);
					dialog.setMessage(tmsg);
					dialog.setNeutralButton("我知道了",
							new OnClickListener() {
								@Override
								@SuppressLint("HandlerLeak")
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									mDialogMap.remove(tid);
									if(tLooper != null && tcallback != null){
										new Handler(tLooper, tcallback).sendMessage(Message.obtain());
									}else if(tcallback != null){
										tcallback.handleMessage(Message.obtain());
									}
									Handler handler = new Handler(){
										@Override
										public void handleMessage(Message msg) {
											Looper.myLooper().quit();
										}
									};
									handler.sendMessageDelayed(Message.obtain(), 300);
								}
							});
					dialog.show();
					Looper.loop();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	public static void doShowDialog(Context activity, String title, String msg,String id) {
		doShowDialog(activity, title, msg, null,null,id);
	}

	public static void doShowDialog(Context activity, String title, String msg,Callback callback) {
		doShowDialog(activity, title, msg, callback,null,AfDateGuid.NewID());
	}

	public static void doShowDialog(Context activity, String title, String msg,Callback callback,String id) {
		doShowDialog(activity, title, msg, callback,null,id);
	}

}
