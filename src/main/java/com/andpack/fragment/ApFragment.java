package com.andpack.fragment;

import com.andframe.annotation.view.BindViewModule;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfView;
import com.andframe.fragment.AfTabFragment;
import com.andframe.module.AfModuleTitlebar;
import com.andpack.api.ApPager;
import com.andpack.impl.ApActivityHelper;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public class ApFragment extends AfTabFragment implements ApPager {

    @BindViewModule
    protected AfModuleTitlebar mTitlebar;

    protected ApActivityHelper mHelper = new ApActivityHelper(this);

    @Override
    protected void onCreated(AfBundle bundle, AfView view) throws Exception {
        mHelper.onCreate(bundle.getBundle(), null);
        super.onCreated(bundle, view);
    }

    //<editor-fold desc="下拉刷新">
    @Override
    public boolean onMore() {
        return false;
    }

    @Override
    public boolean onRefresh() {
        return false;
    }
    //</editor-fold>
}