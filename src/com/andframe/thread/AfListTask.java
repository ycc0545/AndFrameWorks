package com.andframe.thread;

import com.andframe.helper.java.AfTimeSpan;

import java.util.ArrayList;
import java.util.List;

public abstract class AfListTask<T> extends AfHandlerTask {
	// 加载单页条数
	public static int PAGE_SIZE = 15;
	//缓存有效时间（5分钟）
	public static AfTimeSpan CACHETIMEOUTSECOND = AfTimeSpan.FromMinutes(5);
	// 枚举任务类型
	public static final int TASK_REFRESH = 100; // 下拉刷新
	public static final int TASK_MORE = 101; // 点击更多
	public static final int TASK_SORT = 102; // 排序
	public static final int TASK_CACHES = 103; // 本地数据库缓存
	public static final int TASK_CACHESADD = 104; // 本地数据库缓存追加

	public int mFirstResult = 0;
	public int mPageSize = AfListTask.PAGE_SIZE;
	public List<T> mltData = new ArrayList<T>();

	/**
	 * 缓存使用的 class 对象（json要用到）
	 * 设置 并且任务为 TASK_LOAD AfListTask 将自动使用缓存功能
	 */
//	public Class<T> mCacheClazz = null;
	/**
	 *  缓存使用的 KEY_CACHELIST = this.getClass().getName()
	 * 		KEY_CACHELIST 为缓存的标识
	 */
//	public String KEY_CACHELIST = this.getClass().getName();
//	public String KEY_CACHETIME = "KEY_CACHETIME";
	//缓存有效时间
//	public AfTimeSpan mCacheSpan = AfListTask.CACHETIMEOUTSECOND;
	/**
	 * 调用这个构造函数可以触发 TASK_LOAD 任务
	 * 加载上次缓存数据，如果过期将触发 TASK_REFRESH 加载新数据
	 * 缓存使用的 KEY_CACHELIST = this.getClass().getName()
	 * 		KEY_CACHELIST 为缓存的标识
	 * 缓存的过期 间隔为 AfListTask.CACHETIMEOUTSECOND
	 * 		这个时间间隔可以在App初始化时候更改
	 * @param clazz 数据Model的类对象 （json要用到）
	 */
//	public AfListTask(Class<T> clazz){
//		super(TASK_LOAD);
//		this.mCacheClazz = clazz;
//	}

//	/**
//	 * 调用这个构造函数可以触发 TASK_LOAD 任务
//	 * 加载上次缓存数据，如果过期将触发 TASK_REFRESH 加载新数据
//	 * 缓存使用的 KEY_CACHELIST 可以自定义
//	 * 缓存的过期 间隔为 AfListTask.CACHETIMEOUTSECOND
//	 * 		这个时间间隔可以在App初始化时候更改
//	 * @param clazz 数据Model的类对象 （json要用到）
//	 * @param KEY_CACHELIST 缓存的KEY标识
//	 */
//	public AfListTask(Class<T> clazz,String KEY_CACHELIST){
//		super(TASK_LOAD);
//		this.mCacheClazz = clazz;
//		this.KEY_CACHELIST = KEY_CACHELIST;
//	}
//
//	public AfListTask(int task) {
//		super(task);
//	}
//
//	public AfListTask(Handler handle, int task) {
//		super(handle, task);
//	}
//
//	public AfListTask(int task,int first) {
//		super(task);
//		mFirstResult = first;
//	}
//
//	public AfListTask(List<T> list) {
//		super(list!=null?TASK_MORE:TASK_LOAD);
//		if (list!= null && list.size() > 0) {
//			mFirstResult = list.size();
//		}
//	}
//	//加载更多专用
//	public AfListTask(AfListAdapter<T> adapter) {
//		super(adapter!=null?TASK_MORE:TASK_LOAD);
//		if (adapter!= null && adapter.getCount() > 0) {
//			mFirstResult = adapter.getCount();
//		}
//	}
//
//	public void setCacheClazz(Class<T> clazz) {
//		this.mCacheClazz = clazz;
//	}
//
//	public void setKEY_CACHELIST(String KEY_CACHELIST) {
//		this.KEY_CACHELIST = KEY_CACHELIST;
//	}

//	/**
//	 * 获取缓存时间
//	 * @return 如果没有缓存 返回 null
//	 */
//	public Date getCacheTime(){
//		return AfPrivateCaches.getInstance(KEY_CACHELIST).getDate(KEY_CACHETIME, new Date(0));
//	}
//
//	/**
//	 * 获取缓存是否过期
//	 * mCacheTimeOutSecond 决定缓存有效时间
//	 * @return 是否过期
//	 */
//	protected boolean isCacheTimeout() {
//		Date date = AfPrivateCaches.getInstance(KEY_CACHELIST).getDate(KEY_CACHETIME, new Date(0));
//		return AfTimeSpan.FromDate(date, new Date()).GreaterThan(mCacheSpan);
//	}
//
//	/**
//	 * 加载缓存
//	 * 是否检测缓存过期（刷新失败时候可以加载缓存）
//	 * @return 缓存数据 或 null
//	 */
//	protected List<T> onLoad(){
////		if (mCacheClazz != null) {
////			return AfPrivateCaches.getInstance(KEY_CACHELIST).getList(KEY_CACHELIST, mCacheClazz);
////		}
//		return mltData;
//	}
//
//	protected abstract List<T> onRefresh(Page page) throws Exception;
//
//	protected abstract List<T> onMore(Page page) throws Exception;
//
//	protected boolean onWorking(int task) throws Exception {
//		return false;
//	}
//
//	@Override
//	protected final void onWorking(Message tMessage) throws Exception {
////		AfPrivateCaches cache = AfPrivateCaches.getInstance(KEY_CACHELIST);
//		switch (mTask) {
//		case AfListTask.TASK_LOAD:
//			/**
//			 * 为了安全考虑，系统框架规定 onLoad 不能抛出异常
//			 * 	onLoad 主要用于 本地数据库加载缓存，就算本地没有数据
//			 * 	也可返回空List
//			 * 		以防万一使用try catch 阻止异常
//			 */
//			if (/*!isCacheTimeout() && */(mltData = onLoad()) != null && mltData.size() > 0) {
//				break;
//			}
//			mTask = TASK_REFRESH;
////			if (isCacheTimeout() || (mltData = onLoad()) == null || mltData.size() == 0) {
////				try {
////					mTask = TASK_REFRESH;
////					mltData = onRefresh(new Page(mPageSize,0));
////					if (mCacheClazz != null) {
////						cache.putList(KEY_CACHELIST, mltData, mCacheClazz);
////						cache.put(KEY_CACHETIME, new Date());
////					}
////				} catch (Throwable e) {
////					mTask = TASK_LOAD;
////					e.printStackTrace();
////					mltData = onLoad();
////					//如果调试阶段 抛出异常
////					if (AfApplication.getApp().isDebug()) {
////						if (mltData==null||mltData.size()==0) {
////							mTask = TASK_REFRESH;
////							throw e;
////						}
////					}
////				}
////			}
////			break;
//		case AfListTask.TASK_REFRESH:
//			mltData = onRefresh(new Page(mPageSize,0));
//			this.onPutCache(mltData);
////			if (mCacheClazz != null) {
////				cache.putList(KEY_CACHELIST, mltData);
////				cache.put(KEY_CACHETIME, new Date());
////			}
//			break;
//		case AfListTask.TASK_MORE:
//			mltData = onMore(new Page(mPageSize,mFirstResult));
//			this.onPushCache(mltData);
////			if (mCacheClazz != null) {
////				cache.pushList(KEY_CACHELIST, mltData);
////			}
//			break;
//		default :
//			this.onWorking(mTask);
//			break;
//		}
//	}
//
//	protected void onPushCache(List<T> list) {
//
//	}
//
//	protected void onPutCache(List<T> list) {
//
//	}
//
//	@Override
//	protected boolean onHandle(/*Message msg*/) {
//		return false;
//	}
//
//	@Override
//	protected void onException(Throwable e) {
//		if (mTask == TASK_REFRESH) {
//			mltData = onLoad();
//		}
//		if (AfApplication.getNetworkStatus() == AfNetworkEnum.TYPE_NONE
//				&& mTask != AfTask.TASK_LOAD) {
//			mErrors = "当前网络不可用！";
//			mException = new AfToastException(mErrors);
//		}
//	}
//
//	@SuppressWarnings("rawtypes")
//	public static AfListTask getTask(Message msg) {
//		if (msg.obj instanceof AfListTask) {
//			return (AfListTask) msg.obj;
//		}
//		return null;
//	}
}
