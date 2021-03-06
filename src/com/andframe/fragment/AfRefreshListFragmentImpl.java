package com.andframe.fragment;

import android.widget.ListView;

import com.andframe.R;
import com.andframe.activity.framework.AfPageable;
import com.andframe.layoutbind.AfFrameSelector;
import com.andframe.layoutbind.AfModuleNodata;
import com.andframe.layoutbind.AfModuleNodataImpl;
import com.andframe.layoutbind.AfModuleProgress;
import com.andframe.layoutbind.AfModuleProgressImpl;
import com.andframe.layoutbind.AfModuleTitlebar;
import com.andframe.layoutbind.AfModuleTitlebarImpl;

/**
 * 数据列表框架 Activity
 * 带有下拉刷新、数据分页加载、上啦更多、数据缓存
 *
 * @param <T> 列表数据实体类
 * @author 树朾
 */
public abstract class AfRefreshListFragmentImpl<T> extends AfRefreshListFragment<T> {

    protected AfModuleTitlebar mTitlebar;

    @SuppressWarnings("unused")
    public AfRefreshListFragmentImpl() {
    }

    /**
     * 使用缓存必须调用这个构造函数
     * @param clazz 缓存使用的 class 对象（json要用到）
     */
    @SuppressWarnings("unused")
    public AfRefreshListFragmentImpl(Class<T> clazz) {
        super(clazz);
    }

    /**
     * 使用缓存必须调用这个构造函数
     * 	可以自定义缓存标识
     * @param clazz 缓存使用的 class 对象（json要用到）
     */
    @SuppressWarnings("unused")
    public AfRefreshListFragmentImpl(Class<T> clazz, String KEY_CACHELIST) {
        super(clazz,KEY_CACHELIST);
    }

    @Override
    protected void onInitFrameWork() throws Exception {
        super.onInitFrameWork();
        mTitlebar = newModuleTitlebar(this);
    }

    protected AfModuleTitlebar newModuleTitlebar(AfPageable pageable) {
        return new AfModuleTitlebarImpl(pageable);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.af_activity_listview;
    }

    @Override
    protected ListView findListView(AfPageable pageable) {
        return pageable.findViewByID(R.id.listcontent_list);
    }

    @Override
    protected AfFrameSelector newAfFrameSelector(AfPageable pageable) {
        return new AfFrameSelector(this, R.id.listcontent_contentframe);
    }

    @Override
    protected AfModuleProgress newModuleProgress(AfPageable pageable) {
        return new AfModuleProgressImpl(pageable);
    }

    @Override
    protected AfModuleNodata newModuleNodata(AfPageable pageable) {
        return new AfModuleNodataImpl(pageable);
    }

//    @Override
//    protected IListItem<T> getListItem(T data) {
//        return null;
//    }
//
//    @Override
//	protected List<T> onTaskListByPage(Page page, int task) throws Exception {
//		return null;
//	}
}
