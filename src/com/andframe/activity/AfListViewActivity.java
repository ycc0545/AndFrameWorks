package com.andframe.activity;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

import com.andframe.activity.framework.AfActivity;
import com.andframe.activity.framework.AfPageable;
import com.andframe.adapter.AfListAdapter;
import com.andframe.adapter.AfListAdapter.IListItem;
import com.andframe.annotation.mark.MarkCache;
import com.andframe.annotation.view.BindAfterViews;
import com.andframe.annotation.view.BindLayout;
import com.andframe.application.AfExceptionHandler;
import com.andframe.bean.Page;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.feature.AfIntent;
import com.andframe.helper.java.AfTimeSpan;
import com.andframe.layoutbind.AfFrameSelector;
import com.andframe.layoutbind.AfModuleNodata;
import com.andframe.layoutbind.AfModuleProgress;
import com.andframe.thread.AfListTask;
import com.andframe.thread.AfListViewTask;
import com.andframe.util.java.AfCollections;
import com.andframe.util.java.AfReflecter;
import com.andframe.view.AfGridView;
import com.andframe.view.AfListView;
import com.andframe.view.AfRefreshAbsListView;
import com.andframe.view.pulltorefresh.AfPullToRefreshBase.OnRefreshListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.andframe.util.java.AfReflecter.getAnnotation;

/**
 * 数据列表框架 Activity
 * 带有下拉刷新、数据分页加载、上啦更多、数据缓存
 *
 * @param <T> 列表数据实体类
 * @author 树朾
 */
public abstract class AfListViewActivity<T> extends AfActivity implements OnRefreshListener, OnItemClickListener, OnClickListener, AdapterView.OnItemLongClickListener {

    protected AfModuleNodata mNodata;
    protected AfModuleProgress mProgress;
    protected AfFrameSelector mSelector;

    protected AfListAdapter<T> mAdapter;
    protected AfRefreshAbsListView<? extends AbsListView> mListView;

    /**
     * 缓存使用的 class 对象（json要用到）
     * 设置 并且任务为 TASK_LOAD AfListTask 将自动使用缓存功能
     */
    public Class<T> mCacheClazz = null;
    /**
     * 缓存使用的 KEY_CACHELIST = this.getClass().getName()
     * KEY_CACHELIST 为缓存的标识
     */
    public String KEY_CACHETIME = "KEY_CACHETIME";
    public String KEY_CACHELIST = this.getClass().getName();

    protected AfTimeSpan mCacheSpan = AfListTask.CACHETIMEOUTSECOND;

    @SuppressWarnings("unchecked")
    public AfListViewActivity() {
        MarkCache mark = getAnnotation(this.getClass(), AfListViewActivity.class, MarkCache.class);
        if (mark != null) {
            if (mark.value().equals(MarkCache.class)) {
                mCacheClazz = AfReflecter.getActualTypeArgument(this, AfListViewActivity.class, 0);
            } else {
                mCacheClazz = (Class<T>) mark.value();
            }
            if (!"".equals(mark.key())) {
                KEY_CACHELIST = mark.key();
            }
        }
    }

    /**
     * 使用缓存必须调用这个构造函数
     *
     * @param clazz 缓存使用的 class 对象（json要用到）
     */
    public AfListViewActivity(Class<T> clazz) {
        this.mCacheClazz = clazz;
    }

    /**
     * 使用缓存必须调用这个构造函数
     * 可以自定义缓存标识
     *
     * @param clazz 缓存使用的 class 对象（json要用到）
     */
    public AfListViewActivity(Class<T> clazz, String KEY_CACHELIST) {
        this.mCacheClazz = clazz;
        this.KEY_CACHELIST = KEY_CACHELIST;
    }

    /**
     * 创建方法
     *
     * @param bundle 源Bundle
     * @param intent 框架AfIntent
     */
    @Override
    protected void onCreate(Bundle bundle, AfIntent intent) throws Exception {
        super.onCreate(bundle, intent);
        if (mRootView == null) {
            setContentView(getLayoutId());
        }
    }

    @BindAfterViews
    protected void onInitFrameWork() throws Exception {
        mNodata = newModuleNodata(this);
        mProgress = newModuleProgress(this);
        mSelector = newAfFrameSelector(this);

        mListView = newAfListView(this);
        mListView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);

        // 设置banner尺寸
        setLoading();
        onLoad();
    }

    /**
     * 创建指定命令的任务并执行
     *
     * @param task 任务标识
     */
    @SuppressWarnings("unchecked")
    protected AbListViewTask postTask(int task) {
        return postTask(new AbListViewTask(task));
    }

    /**
     * 创建新的AfListView
     *
     * @param pageable 页面对象
     * @return 可刷新的ListView
     */
    protected AfRefreshAbsListView<? extends AbsListView> newAfListView(AfPageable pageable) {
        AbsListView listView = findListView(pageable);
        if (listView instanceof ListView) {
            return new AfListView(((ListView) listView));
        } else if (listView instanceof GridView) {
            return new AfGridView(((GridView) listView));
        }
        return new AfListView(getContext());
    }

    /**
     * 获取setContentView的id
     *
     * @return id
     */
    protected int getLayoutId() {
        BindLayout layout = getAnnotation(this.getClass(), AfListViewActivity.class, BindLayout.class);
        if (layout != null) {
            return layout.value();
        }
        return 0;
    }

    /**
     *
     * 获取列表控件
     *
     * @param pageable 页面对象
     * @return pageable.findListViewById(id)
     */
    protected abstract AbsListView findListView(AfPageable pageable);

    /**
     * 新建页面选择器
     *
     * @param pageable 页面对象
     * @return 数据页面切换器
     */
    protected abstract AfFrameSelector newAfFrameSelector(AfPageable pageable);

    /**
     * 新建加载页面
     *
     * @param pageable 页面对象
     * @return 加载页面模块
     */
    protected abstract AfModuleProgress newModuleProgress(AfPageable pageable);

    /**
     * 新建空数据页面
     *
     * @param pageable 页面对象
     * @return 空数据页面模块
     */
    protected abstract AfModuleNodata newModuleNodata(AfPageable pageable);

    /**
     * 添加一条数据到显示列表
     *
     * @param value 添加的数据
     */
    @SuppressWarnings("unused")
    public void addData(T value) {
        if (mAdapter == null || mAdapter.getCount() == 0) {
            List<T> ltArray = new ArrayList<>();
            ltArray.add(value);
            setData(newAdapter(this, ltArray));
        } else {
            mAdapter.add(0, value);
        }
    }

    /**
     * 监听适配器改变，自动更新页面显示切换（子类要重写请重新赋值新对象）
     */
    protected DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            if(mAdapter == null || mAdapter.getCount() == 0){
                setNodata();
            } else{
                setData(mAdapter);
            }
        }
    };

    /**
     * 显示数据页面
     *
     * @param adapter 适配器数据
     */
    public void setData(AfListAdapter<T> adapter) {
        mAdapter = adapter;
        if (mListView.getRefreshableView().getAdapter() != adapter) {
            mListView.setAdapter(adapter);
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }
        mSelector.selectFrame(mListView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
    }

    /**
     * 正在加载数据提示
     */
    public void setLoading() {
        mProgress.setDescription("正在加载...");
        mSelector.selectFrame(mProgress);
    }

    /**
     * 空数据页面刷新监听器
     * 子类需要重写监听器的话可以对
     * mNodataRefreshListener 重新赋值
     */
    protected OnClickListener mNodataRefreshListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onRefresh();
            setLoading();
        }
    };

    /**
     * 处理空数据
     */
    public void setNodata() {
        mNodata.setDescription("抱歉，暂无数据");
        mSelector.selectFrame(mNodata);
        mNodata.setOnRefreshListener(mNodataRefreshListener);
    }

    /**
     * 错误信息处理
     *
     * @param ex 异常对象
     */
    public void setLoadError(Throwable ex) {
        mNodata.setDescription(AfExceptionHandler.tip(ex, "数据加载出现异常"));
        mNodata.setOnRefreshListener(mNodataRefreshListener);
        mSelector.selectFrame(mNodata);
    }

    /**
     * 加载数据（缓存优先）
     */
    protected void onLoad() {
        postTask(new AbListViewTask());
    }

    /**
     * 用户加载分页通知事件
     *
     * @return 是否处理（影响列表控件响应状态）
     */
    @Override
    public boolean onMore() {
        postTask(new AbListViewTask(mAdapter));
        return true;
    }

    /**
     * 用户刷新数据通知事件
     *
     * @return 是否处理（影响列表控件响应状态）
     */
    @Override
    public boolean onRefresh() {
        return postTask(new AbListViewTask(AfListTask.TASK_REFRESH)).setListener(mListView).prepare();
    }

    /**
     * 数据列表点击事件
     *
     * @param parent 列表控件
     * @param view   被点击的视图
     * @param index  被点击的index
     * @param id     被点击的视图ID
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
        index = ((AfRefreshAbsListView) mListView).getDataIndex(index);
        if (index >= 0) {
            T model = mAdapter.getItemAt(index);
            try {
                onItemClick(model, index);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG("onItemClick"));
            }
        }
    }

    /**
     * onItemClick 事件的 包装 一般情况下子类可以重写这个方法
     *
     * @param model 被点击的数据model
     * @param index 被点击的index
     */
    protected void onItemClick(T model, int index) {

    }

    /**
     * 数据列表点击事件
     *
     * @param parent 列表控件
     * @param view   被点击的视图
     * @param index  被点击的index
     * @param id     被点击的视图ID
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int index, long id) {
        index = mListView.getDataIndex(index);
        if (index >= 0) {
            T model = mAdapter.getItemAt(index);
            try {
                return onItemLongClick(model, index);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG("onItemLongClick"));
            }
        }
        return false;
    }

    /**
     * onItemLongClick 事件的 包装 一般情况下子类可以重写这个方法
     *
     * @param model 被点击的数据model
     * @param index 被点击的index
     */
    @SuppressWarnings("UnusedParameters")
    protected boolean onItemLongClick(T model, int index) {
        return false;
    }

    @Override
    public void onClick(View v) {
//		if (v != null && v.getId() == ModuleNodata.ID_BUTTON) {
//			onRefresh();
//			setLoading();
//		}
    }

    @SuppressWarnings("unused")
    protected void putCache() {
        if (mAdapter != null && AfCollections.isNotEmpty(mAdapter.getList())) {
            putCache(mAdapter.getList());
        }
    }

    protected void putCache(List<T> list) {
        onTaskPutCache(list);
    }

    /**
     * 获取缓存时间
     *
     * @return 如果没有缓存 返回 null
     */
    protected Date getCacheTime() {
        return AfPrivateCaches.getInstance(KEY_CACHELIST).getDate(KEY_CACHETIME, new Date(0));
    }

    /**
     * 数据加载内部任务类（数据加载事件已经转发，无实际处理代码）
     *
     * @author 树朾
     */
    protected class AbListViewTask extends AfListViewTask<T> {

        public AbListViewTask() {
            super(TASK_LOAD);
        }

        /**
         * 可以触发加载更多（分页）任务 （传空null可以触发刷新任务）
         *
         * @param adapter 适配器，用于统计当前条数计算分页（传空null可以触发刷新任务）
         */
        public AbListViewTask(AfListAdapter<T> adapter) {
            super(adapter);
        }

        /**
         * 自定义任务 触发 onWorking 和 onTaskWorking
         *
         * @param task 任务标识
         */
        public AbListViewTask(int task) {
            super(task);
        }

        @Override
        protected boolean onPrepare() {
            return AfListViewActivity.this.onTaskPrepare(mTask);
        }

        @Override
        protected List<T> onLoad(boolean isCheckExpired) {
            List<T> list = AfListViewActivity.this.onTaskLoad(isCheckExpired);
            if (!AfCollections.isEmpty(list)) {
                return list;
            }
            return super.onLoad(isCheckExpired);
        }

        @Override
        protected void onPutCache(List<T> list) {
            AfListViewActivity.this.onTaskPutCache(list);
        }

        @Override
        protected void onPushCache(List<T> list) {
            AfListViewActivity.this.onTaskPushCache(list);
        }

        //事件转发 参考 AfListViewFragment.onListByPage
        @Override
        protected List<T> onListByPage(Page page, int task) throws Exception {
            return AfListViewActivity.this.onTaskListByPage(page, task);
        }

        @Override
        protected boolean onWorking(int task) throws Exception {
            return AfListViewActivity.this.onTaskWorking(task);
        }

        //事件转发 参考 AfListViewFragment.onLoaded
        @Override
        protected boolean onLoaded(boolean isfinish, List<T> ltdata) {
            return AfListViewActivity.this.onLoaded(this, isfinish, ltdata, getCacheTime());
        }

        //事件转发 参考 AfListViewFragment.onRefreshed
        @Override
        protected boolean onRefreshed(boolean isfinish, List<T> ltdata) {
            return AfListViewActivity.this.onRefreshed(this, isfinish, ltdata);
        }

        //事件转发 参考 AfListViewFragment.onMored
        @Override
        protected boolean onMored(boolean isfinish, List<T> ltdata,
                                  boolean ended) {
            return AfListViewActivity.this.onMored(this, isfinish, ltdata);
        }

        @Override
        protected boolean onWorked(int task, boolean isfinish, List<T> ltdata) {
            return AfListViewActivity.this.onTaskWorked(this, isfinish, ltdata);
        }
    }

    /**
     * 任务准备开始 （在UI线程中）
     *
     * @return 返回true 表示准备完毕 否则 false 任务将被取消
     */
    protected boolean onTaskPrepare(int task) {
        return true;
    }

    /**
     * 缓存加载结束处理时间（框架默认调用onRefreshed事件处理）
     *
     * @param task      任务执行对象
     * @param isfinish  任务是否完成（未捕捉到异常）
     * @param ltdata    完成加载数据
     * @param cachetime 缓存时间
     * @return 返回true 已经做好错误页面显示 返回false 框架会做好默认错误反馈
     */
    protected boolean onLoaded(AbListViewTask task, boolean isfinish,
                               List<T> ltdata, Date cachetime) {
        boolean deal = onRefreshed(task, isfinish, ltdata);
        if (isfinish && !AfCollections.isEmpty(ltdata)) {
            //设置上次刷新缓存时间
            mListView.setLastUpdateTime(cachetime);
        }
        return deal;
    }

    /**
     * 任务刷新结束处理事件
     *
     * @param task     任务执行对象
     * @param isfinish 任务是否完成（未捕捉到异常）
     * @param ltdata   完成加载数据
     * @return 返回true 已经做好错误页面显示 返回false 框架会做好默认错误反馈
     */
    @SuppressWarnings("static-access")
    protected boolean onRefreshed(AbListViewTask task, boolean isfinish, List<T> ltdata) {
        if (isfinish) {
            //通知列表刷新完成
            mListView.finishRefresh();
            if (!AfCollections.isEmpty(ltdata)) {
                setData(mAdapter = newAdapter(getActivity(), ltdata));
                setMoreShow(task, ltdata);
//                if (ltdata.size() < task.mPageSize) {
//                    mAbsListView.removeMoreView();
//                } else if (mIsPaging) {
//                    mAbsListView.addMoreView();
//                }
            } else {
                if (mAdapter != null) {
                    mAdapter.set(new ArrayList<T>());
                }
                setNodata();
            }
        } else {
            //通知列表刷新失败
            mListView.finishRefreshFail();
            if (mAdapter != null && mAdapter.getCount() > 0) {
                setData(mAdapter);
                makeToastLong(task.makeErrorToast("刷新失败"));
            } else if (ltdata != null && ltdata.size() > 0) {
                setData(mAdapter = newAdapter(getActivity(), ltdata));
                makeToastLong(task.makeErrorToast("刷新失败"));
            } else {
                setLoadError(task.mException);
            }
        }
        return true;
    }

    /**
     * 任务加载更多结束处理事件
     *
     * @param task     任务执行对象
     * @param isfinish 任务是否完成（未捕捉到异常）
     * @param ltdata   完成加载数据
     * @return 返回true 已经做好错误页面显示 返回false 框架会做好默认错误反馈
     */
    @SuppressWarnings("static-access")
    protected boolean onMored(AbListViewTask task, boolean isfinish,
                              List<T> ltdata) {
        // 通知列表刷新完成
        mListView.finishLoadMore();
        if (isfinish) {
            if (!AfCollections.isEmpty(ltdata)) {
                final int count = mAdapter.getCount();
                // 更新列表
                mAdapter.addAll(ltdata);
                mListView.smoothScrollToPosition(count + 1);
            }
            if (!setMoreShow(task, ltdata)) {
                makeToastShort("数据全部加载完毕！");
            }
//            if (ltdata.size() < task.mPageSize) {
//                // 关闭更多选项
//                makeToastShort("数据全部加载完毕！");
//                mAbsListView.removeMoreView();
//            }
        } else {
            makeToastLong(task.makeErrorToast("获取更多失败！"));
        }
        return true;
    }

    /**
     * 设置是否现实加载更多功能
     * @param task     完成的任务
     * @param ltdata   任务加载的数据
     * @return true 显示更多功能 false 数据加载结束
     */
    protected boolean setMoreShow(AbListViewTask task, List<T> ltdata) {
        if (ltdata.size() < task.mPageSize) {
            mListView.removeMoreView();
            return false;
        } else {
            mListView.addMoreView();
            return true;
        }
    }

    /**
     * 获取列表项布局Item
     * 如果重写 newAdapter 之后，本方法将无效
     *
     * @param data 对应的数据
     * @return 实现 布局接口 IListItem 的Item兑现
     * new LayoutItem implements IListItem<T>(){}
     */
    protected abstract IListItem<T> getListItem(T data);

    /**
     * 加载缓存列表（不分页，在异步线程中执行，不可以更改页面操作）
     * @param isCheckExpired 是否检测缓存过期（刷新失败时候可以加载缓存）
     *
     * @return 返回 null 可以使用框架内置缓存
     */
    protected List<T> onTaskLoad(boolean isCheckExpired) {
        if (mCacheClazz != null) {
            AfPrivateCaches instance = AfPrivateCaches.getInstance(KEY_CACHELIST);
            Date date = instance.getDate(KEY_CACHETIME, new Date(0));
            if (!isCheckExpired || !AfTimeSpan.FromDate(date, new Date()).GreaterThan(mCacheSpan)) {
                return instance.getList(KEY_CACHELIST, mCacheClazz);
            }
        }
        return null;
    }

    protected void onTaskPushCache(List<T> list) {
        if (mCacheClazz != null) {
            AfPrivateCaches cache = AfPrivateCaches.getInstance(KEY_CACHELIST);
            cache.putList(KEY_CACHELIST, list);
        }
    }

    protected void onTaskPutCache(List<T> list) {
        if (mCacheClazz != null) {
            AfPrivateCaches cache = AfPrivateCaches.getInstance(KEY_CACHELIST);
            cache.putList(KEY_CACHELIST, list);
            cache.put(KEY_CACHETIME, new Date());
        }
    }


    /**
     * 数据分页加载（在异步线程中执行，不可以更改页面操作）
     *
     * @param page 分页对象
     * @param task 任务id
     * @return 加载到的数据列表
     * @throws Exception
     */
    protected abstract List<T> onTaskListByPage(Page page, int task) throws Exception;

    /**
     * 由postTask(int task)触发
     * 除了与刷新、翻页、加载缓存有关的其他任务工作（异步线程、留给子类任务扩展用）
     *
     * @param task 任务标识
     * @return 是否成功执行
     */
    protected boolean onTaskWorking(int task) throws Exception {
        return false;
    }

    /**
     * 与onTaskWorking相对应的结束（UI线程）
     *
     * @param abListViewTask 任务执行对象
     * @param isfinish       任务是否完成（未捕捉到异常）
     * @param ltdata         完成加载数据
     * @return 返回true 已经做好错误页面显示 返回false 框架会做好默认错误反馈
     */
    protected boolean onTaskWorked(AbListViewTask abListViewTask, boolean isfinish, List<T> ltdata) {
        return false;
    }

    /**
     * 根据数据ltdata新建一个 适配器 重写这个方法之后getListItem方法将失效
     *
     * @param context Context对象
     * @param ltdata  完成加载数据
     * @return 新的适配器
     */
    protected AfListAdapter<T> newAdapter(Context context, List<T> ltdata) {
        return new AbListViewAdapter(getContext(), ltdata);
    }

    /**
     * ListView数据适配器（事件已经转发getListItem，无实际处理代码）
     */
    protected class AbListViewAdapter extends AfListAdapter<T> {

        public AbListViewAdapter(Context context, List<T> ltdata) {
            super(context, ltdata);
        }

        /**
         * 转发事件到 AfListViewActivity.this.getListItem(data);
         */
        @Override
        protected IListItem<T> getListItem(T data) {
            return AfListViewActivity.this.getListItem(data);
        }
    }
}
