package com.android.music.logger;

import android.app.Activity;
import android.app.Application;
import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.music.logger.activitylifecyclecallbackscompat.ActivityLifecycleCallbacksCompat;
import com.android.music.logger.activitylifecyclecallbackscompat.ApplicationHelper;

import java.util.ArrayList;

public class BadSymptoms {
    private boolean LOGOUT = false;
    private GestureDetector gestureDetector;
    private String application,activityName;
    private Activity activity;
    private GestureListener gestureListener;
    private Application app;
    private ArrayList<View> allView;


    public BadSymptoms(Context context){
        activity = (Activity) context;
        application = getApplicationName(context);
        activityName = this.activity.getClass().getSimpleName();

        gestureListener = new GestureListener(application,activityName);
        gestureDetector = new GestureDetector(context, gestureListener);

        allView = getAllChildren(activity.getWindow().getDecorView().getRootView());
        logView();

        if (activity instanceof ListActivity) {
            ListActivity listActivity = (ListActivity) activity;
            ListView lv = listActivity.getListView();
            onTouch(lv);
        }

        if (activity instanceof ExpandableListActivity) {
            ExpandableListActivity expandableListActivity = (ExpandableListActivity) activity;
            ListView lv = expandableListActivity.getExpandableListView();
            onTouch(lv);
        }
        app = activity.getApplication();
        ApplicationHelper.registerActivityLifecycleCallbacks(app, new ActivityLifecycleCallbacksCompat() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (LOGOUT)
                Log.i("callBack", "onActivityCreated " + activity.getClass());
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (LOGOUT)
                Log.i("callBack", "onActivityStarted " + activity.getClass());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (LOGOUT)
                Log.i("callBack", "onActivityResumed " + activity.getClass());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                LogSave.printLog(application);
                if (LOGOUT)
                Log.i("callBack", "onActivityPaused " + activity.getClass());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (LOGOUT)
                Log.i("callBack", "onActivityStopped " + activity.getClass());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                if (LOGOUT)
                Log.i("callBack", "onActivitySaveInstanceState " + activity.getClass());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (LOGOUT)
                Log.i("callBack", "onActivityDestroyed " + activity.getClass());
            }
        });
    }

    private void logView() {
        for (View child : allView) {
            if (child instanceof View) {
                onTouch(child);
            }
        }
    }

    private static String getApplicationName(Context context) {
        int stringId = context.getApplicationInfo().labelRes;
        return context.getString(stringId);
    }
    public GestureDetector getGestureDetector(){
        return this.gestureDetector;
    }

    public void setGestureDetector(GestureDetector gestureDetector){
        this.gestureDetector = gestureDetector;
    }

    public void resumeActivity() {
        //jSonSave("user", application, activityName, "", "login");
    }

     public void onTouch(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureListener.setView(viewName(view));
                gestureDetector.onTouchEvent(motionEvent);

                return false;
            }
        });
    }

    private String viewName(View view) {
        String result;

        try {
            result = view.getResources().getResourceEntryName(view.getId());
        } catch (Exception e) {
            result = String.valueOf(view.getId());
        }

        return result;
    }

    public void saveMenu(String jenis,String menu){
        LogSave.jSonSave("user", application, activityName, jenis + " : " + menu, "Touch");
        //LogSave.printLog(application);
    }

    public ArrayList<View> getAllChildren(View v) {
        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();

            if (!(v.getId() < 0 )) {
                viewArrayList.add(v);
            }
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();

            if (!(v.getId() < 0 )) {
                viewArrayList.add(v);
            }

            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }
}
