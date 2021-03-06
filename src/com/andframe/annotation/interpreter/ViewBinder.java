package com.andframe.annotation.interpreter;

import android.app.Activity;
import android.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.andframe.activity.framework.AfView;
import com.andframe.activity.framework.AfViewable;
import com.andframe.annotation.view.BindAfterViews;
import com.andframe.annotation.view.BindCheckedChange;
import com.andframe.annotation.view.BindClick;
import com.andframe.annotation.view.BindItemClick;
import com.andframe.annotation.view.BindItemLongClick;
import com.andframe.annotation.view.BindLayout;
import com.andframe.annotation.view.BindLongClick;
import com.andframe.annotation.view.BindTouch;
import com.andframe.annotation.view.BindView;
import com.andframe.annotation.view.BindViewModule;
import com.andframe.application.AfExceptionHandler;
import com.andframe.layoutbind.AfFrameSelector;
import com.andframe.layoutbind.AfModuleNodata;
import com.andframe.layoutbind.AfModuleNodataImpl;
import com.andframe.layoutbind.AfModuleProgress;
import com.andframe.layoutbind.AfModuleProgressImpl;
import com.andframe.layoutbind.AfModuleTitlebar;
import com.andframe.layoutbind.AfModuleTitlebarImpl;
import com.andframe.layoutbind.AfSelectorBottombar;
import com.andframe.layoutbind.AfSelectorBottombarImpl;
import com.andframe.layoutbind.AfSelectorTitlebar;
import com.andframe.layoutbind.AfSelectorTitlebarImpl;
import com.andframe.layoutbind.framework.AfViewWrapper;
import com.andframe.layoutbind.framework.AfViewModule;
import com.andframe.util.java.AfReflecter;
import com.andframe.view.AfContactsRefreshView;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * 控件绑定器
 *
 * @author 树朾
 */
public class ViewBinder {

    protected static String TAG(Object obj, String tag) {
        if (obj == null) {
            return "ViewBinder." + tag;
        }
        return "LayoutBinder(" + obj.getClass().getName() + ")." + tag;
    }

    public static void doBind(AfViewable root) {
        doBind(root, root);
    }

    public static void doBind(Object handler, View root) {
        doBind(handler, new AfView(root));
    }

    public static void doBind(Object handler, AfViewable root) {
        bindClick(handler, root);
        bindTouch(handler, root);
        bindLongClick(handler, root);
        bindItemClick(handler, root);
        bindItemLongClick(handler, root);
        bindCheckedChange(handler, root);
        bindView(handler, root);
        bindViewModule(handler, root);
        bindAfterView(handler, root);
    }

    private static Class<?> getStopType(Object handler) {
        if (handler instanceof AfViewWrapper) {
            return AfViewWrapper.class;
        }
        if (handler instanceof Activity) {
            return Activity.class;
        }
        if (handler instanceof Fragment) {
            return Fragment.class;
        }
        return Object.class;
    }

    private static void bindTouch(Object handler, AfViewable root) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), getStopType(handler), BindTouch.class)) {
            try {
                BindTouch bind = method.getAnnotation(BindTouch.class);
                for (int id : bind.value()) {
                    View view = root.findViewById(id);
                    view.setOnTouchListener(new EventListener(handler).touch(method));
                }
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "doBindClick.") + method.getName());
            }
        }
    }

    private static void bindClick(Object handler, AfViewable root) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), getStopType(handler), BindClick.class)) {
            try {
                BindClick bind = method.getAnnotation(BindClick.class);
                for (int id : bind.value()) {
                    View view = root.findViewById(id);
                    view.setOnClickListener(new EventListener(handler).click(method));
                }
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "doBindClick.") + method.getName());
            }
        }
    }

    private static void bindLongClick(Object handler, AfViewable root) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), getStopType(handler), BindLongClick.class)) {
            try {
                BindLongClick bind = method.getAnnotation(BindLongClick.class);
                for (int id : bind.value()) {
                    View view = root.findViewById(id);
                    view.setOnLongClickListener(new EventListener(handler).longClick(method));
                }
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "doBindLongClick.") + method.getName());
            }
        }
    }

    private static void bindItemClick(Object handler, AfViewable root) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), getStopType(handler), BindItemClick.class)) {
            try {
                BindItemClick bind = method.getAnnotation(BindItemClick.class);
                for (int id : bind.value()) {
                    AdapterView<?> view = root.findViewByID(id);
                    if (view != null) {
                        view.setOnItemClickListener(new EventListener(handler).itemClick(method));
                    }
                }
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "doBindLongClick.") + method.getName());
            }
        }
    }

    private static void bindItemLongClick(Object handler, AfViewable root) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), getStopType(handler), BindItemLongClick.class)) {
            try {
                BindItemLongClick bind = method.getAnnotation(BindItemLongClick.class);
                for (int id : bind.value()) {
                    AdapterView<?> view = root.findViewByID(id);
                    if (view != null) {
                        view.setOnItemLongClickListener(new EventListener(handler).itemLongClick(method));
                    }
                }
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "doBindLongClick.") + method.getName());
            }
        }
    }

    private static void bindCheckedChange(Object handler, AfViewable root) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), getStopType(handler), BindCheckedChange.class)) {
            try {
                BindCheckedChange bind = method.getAnnotation(BindCheckedChange.class);
                for (int id : bind.value()) {
                    CompoundButton view = root.findViewByID(id);
                    if (view != null) {
                        view.setOnCheckedChangeListener(new EventListener(handler).checkedChange(method));
                    }
                }
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "doBindLongClick.") + method.getName());
            }
        }
    }

    private static void bindView(Object handler, AfViewable root) {
        for (Field field : AfReflecter.getFieldAnnotation(handler.getClass(), getStopType(handler), BindView.class)) {
            try {
                BindView bind = field.getAnnotation(BindView.class);
                List<View> list = new ArrayList<>();
                for (int id : bind.value()) {
                    View view = null;
                    if (id > 0) {
                        view = root.findViewById(id);
                    } else {
                        Queue<View> views = new LinkedBlockingQueue<>();
                        views.add(root.getView());
                        do {
                            View cview = views.poll();
                            if (cview != null && field.getType().isAssignableFrom(cview.getClass())) {
                                view = cview;
                            } else {
                                if (cview instanceof ViewGroup) {
                                    ViewGroup group = (ViewGroup) cview;
                                    for (int i = 0; i < group.getChildCount(); i++) {
                                        views.add(group.getChildAt(i));
                                    }
                                }
                            }
                        } while (view == null && !views.isEmpty());
                    }
                    if (view != null) {
                        if (bind.click() && handler instanceof OnClickListener) {
                            view.setOnClickListener((OnClickListener) handler);
                        }
                        list.add(view);
                    }
                }
                if (list.size() > 0) {
                    field.setAccessible(true);
                    if (field.getType().isArray()) {
                        Class<?> componentType = field.getType().getComponentType();
                        Object[] array = list.toArray((Object[]) Array.newInstance(componentType, list.size()));
                        field.set(handler, array);
                    } else if (List.class.equals(field.getType())) {
                        field.set(handler, list);
                    } else if(list.get(0) != null){
                        field.set(handler, list.get(0));
                    }
                }
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "doBindView.") + field.getName());
            }
        }
    }

    private static void bindViewModule(Object handler, AfViewable root) {
        for (Field field : AfReflecter.getFieldAnnotation(handler.getClass(), getStopType(handler), BindViewModule.class)) {
            try {
                Class<?> clazz = field.getType();
                BindViewModule bind = field.getAnnotation(BindViewModule.class);
                List<Object> list = new ArrayList<>();
                for (int id : bind.value()) {
                    Object value = null;
                    if (clazz.equals(AfModuleTitlebar.class) && root != null) {
                        value = new AfModuleTitlebarImpl(root);
                    } else if (clazz.equals(AfSelectorTitlebar.class) && root != null) {
                        value = new AfSelectorTitlebarImpl(root);
                    } else if (clazz.equals(AfSelectorBottombar.class) && root != null) {
                        value = new AfSelectorBottombarImpl(root);
                    } else if (clazz.equals(AfFrameSelector.class) && root != null) {
                        value = new AfFrameSelector(root, id);
                    } else if (clazz.equals(AfModuleNodata.class) && root != null) {
                        value = new AfModuleNodataImpl(root);
                    } else if (clazz.equals(AfModuleProgress.class) && root != null) {
                        value = new AfModuleProgressImpl(root);
                    } else if (clazz.equals(AfContactsRefreshView.class) && root != null) {
                        value = new AfContactsRefreshView(root, bind.value()[0]);
                    } else if (root != null
                            && (field.getType().isAnnotationPresent(BindLayout.class) || id > 0)
                            /*&& AfViewModule.class.isAssignableFrom(field.getType())*/) {
                        if (id <= 0) {
                            id = field.getType().getAnnotation(BindLayout.class).value();
                        }
                        //Class<? extends AfViewModule> type = (Class<? extends AfViewModule>) field.getType();
                        if (field.getType().isArray()) {
                            Class<?> type = field.getType().getComponentType();
                            value = AfViewModule.init((Class<? extends AfViewModule>) type, root, id);
                        } else if (List.class.isAssignableFrom(field.getType())) {
                            Type generic = field.getGenericType();
                            ParameterizedType parameterized = (ParameterizedType) generic;
                            Class<?> type = (Class<?>) parameterized.getActualTypeArguments()[0];
                            value = AfViewModule.init((Class<? extends AfViewModule>) type, root, id);
                        } else {
                            value = AfViewModule.init((Class<? extends AfViewModule>) field.getType(), root, id);
                        }
                    }
                    if (value != null) {
                        list.add(value);
                    }
                }

                if (list.size() > 0) {
                    field.setAccessible(true);
                    if (field.getType().isArray()) {
                        Class<?> componentType = field.getType().getComponentType();
                        Object[] array = list.toArray((Object[]) Array.newInstance(componentType, list.size()));
                        field.set(handler, array);
                    } else if (List.class.isAssignableFrom(field.getType())) {
                        field.set(handler, list);
                    } else if (list.get(0) != null) {
                        field.set(handler, list.get(0));
                    }
                }
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "doBindViewModule.") + field.getName());
            }
        }
    }

    public static void bindAfterView(Object handler, AfViewable root) {
        List<SimpleEntry> methods = new ArrayList<>();
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), getStopType(handler), BindAfterViews.class)) {
            BindAfterViews annotation = method.getAnnotation(BindAfterViews.class);
            methods.add(new SimpleEntry(method, annotation));
        }
        Collections.sort(methods, new Comparator<SimpleEntry>() {
            @Override
            public int compare(SimpleEntry lhs, SimpleEntry rhs) {
                return lhs.getValue().value() - rhs.getValue().value();
            }
        });
        for (SimpleEntry entry : methods) {
            try {
                invokeMethod(handler, entry.getKey());
            } catch (Throwable e) {
                e.printStackTrace();
                if (!entry.getValue().exception()) {
                    throw new RuntimeException("调用视图初始化失败", e);
                }
                AfExceptionHandler.handle(e, TAG(handler, "doBindView.") + entry.getKey().getName());
            }
        }
    }

    private static Object invokeMethod(Object handler, Method method, Object... params) throws Exception {
        if (handler != null && method != null) {
            method.setAccessible(true);
            return method.invoke(handler, params);
        }
        return null;
    }

    public static class SimpleEntry {

        private final Method key;
        private BindAfterViews value;

        public SimpleEntry(Method theKey, BindAfterViews theValue) {
            key = theKey;
            value = theValue;
        }

        public Method getKey() {
            return key;
        }

        public BindAfterViews getValue() {
            return value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }


    public static class EventListener implements OnClickListener,
            View.OnLongClickListener,
            AdapterView.OnItemClickListener,
            AdapterView.OnItemLongClickListener,
            CompoundButton.OnCheckedChangeListener, View.OnTouchListener {

        private Object handler;

        private Method clickMethod;
        private Method touchMethod;
        private Method longClickMethod;
        private Method itemClickMethod;
        private Method itemLongClickMehtod;
        private Method checkedChangedMehtod;

        public EventListener(Object handler) {
            this.handler = handler;
        }

        public OnClickListener click(Method method) {
            clickMethod = method;
            return this;
        }

        public View.OnTouchListener touch(Method method) {
            touchMethod = method;
            return this;
        }

        public View.OnLongClickListener longClick(Method method) {
            this.longClickMethod = method;
            return this;
        }

        public AdapterView.OnItemClickListener itemClick(Method method) {
            this.itemClickMethod = method;
            return this;
        }

        public AdapterView.OnItemLongClickListener itemLongClick(Method method) {
            this.itemLongClickMehtod = method;
            return this;
        }

        public CompoundButton.OnCheckedChangeListener checkedChange(Method method) {
            this.checkedChangedMehtod = method;
            return this;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return Boolean.valueOf(true).equals(invokeMethod(handler, touchMethod, v, event));
        }

        public void onClick(View v) {
            invokeMethod(handler, clickMethod, v);
        }

        public boolean onLongClick(View v) {
            return Boolean.valueOf(true).equals(invokeMethod(handler, longClickMethod, v));
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            invokeMethod(handler, itemClickMethod, parent, view, position, id);
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            return Boolean.valueOf(true).equals(invokeMethod(handler, itemLongClickMehtod, parent, view, position, id));
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            invokeMethod(handler, checkedChangedMehtod, buttonView, isChecked);
        }

        private Object invokeMethod(Object handler, Method method, Object... params) {
            if (handler != null && method != null) {
                try {
                    method.setAccessible(true);
                    return method.invoke(handler, paramAllot(method, params));
                } catch (Throwable e) {
                    e.printStackTrace();
                    AfExceptionHandler.handle(e, "EventListener.invokeMethod");
                }
            }
            return null;
        }

        /**
         * 智能参数分配
         */
        private Object[] paramAllot(Method method, Object... params) {
            Set<Integer> set = new HashSet<>();
            List<Object> list = new ArrayList<>();
            Class<?>[] types = method.getParameterTypes();
            if (types.length > 0) {
                for (int i = 0; i < types.length; i++) {
                    Object obj = null;
                    if (params.length > i && params[i] != null && isInstance(types[i], params[i])) {
                        set.add(i);
                        obj = params[i];
                    } else {
                        for (int j = 0; j < params.length; j++) {
                            if (params[j] != null && !set.contains(j) && isInstance(types[i], params[j])) {
                                set.add(j);
                                obj = params[j];
                            }
                        }
                    }
                    list.add(obj);
                }
            }
            return paramAllot(method,list.toArray(new Object[list.size()]),params);
        }

        /**
         * 特定参数分配
         */
        private Object[] paramAllot(Method method, Object[] args, Object... params) {
            if (params.length == 0) {
                return args;
            }
            Class<?>[] types = null;
            //View 智能获取tag中的值
            if (params[0] instanceof View) {
                Object tag = null;
                for (int i = 0; i < args.length && i < params.length; i++) {
                    if (args[i] == null) {
                        if (tag == null) {
                            tag = ((View) params[0]).getTag();
                            if (tag == null) {
                                break;
                            }
                            types = method.getParameterTypes();
                        }
                        if (types[i].isAssignableFrom(tag.getClass())) {
                            args[i] = tag;
                        }
                    }
                }
            }
            //ListView 智能获取列表 中的元素
            if (params[0] instanceof AdapterView && params[2] instanceof Integer) {
                Adapter adapter = ((AdapterView) params[0]).getAdapter();
                if (adapter != null && adapter.getCount() > 0) {
                    int index = ((Integer) params[2]);
                    if (params[0] instanceof ListView) {
                        int count = ((ListView) params[0]).getHeaderViewsCount();
                        index = index >= count ? (index - count) : index;
                    }
                    Object value = null;
                    for (int i = 0; i < args.length && i < params.length; i++) {
                        if (args[i] == null) {
                            if (value == null) {
                                value = adapter.getItem(index);
                                if (value == null) {
                                    break;
                                }
                                types = method.getParameterTypes();
                            }
                            if (types[i].isAssignableFrom(value.getClass())) {
                                args[i] = value;
                            }
                        }
                    }
                }
            }
            return args;
        }

        private boolean isInstance(Class<?> t1, Object object) {
            if (t1.isPrimitive()) {
                if (t1.equals(int.class)) {
                    t1 = Integer.class;
                } else if (t1.equals(short.class)) {
                    t1 = Short.class;
                } else if (t1.equals(long.class)) {
                    t1 = Long.class;
                } else if (t1.equals(float.class)) {
                    t1 = Float.class;
                } else if (t1.equals(double.class)) {
                    t1 = Double.class;
                } else if (t1.equals(char.class)) {
                    t1 = Character.class;
                } else if (t1.equals(byte.class)) {
                    t1 = Byte.class;
                } else if (t1.equals(boolean.class)) {
                    t1 = Boolean.class;
                }
            }
            return t1.isInstance(object);
        }
    }


}
