package com.android.music.logger;

import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by cahya on 25/05/2015.
 */
public class BadSymptoms {
    private GestureDetector gestureDetector;
    private String application,activityName;
    private static final String TAG ="BadSymptoms";
    private Activity activity;
    private GestureListener gestureListener;

    private ArrayList<View> allView;

    public BadSymptoms(Context context){
        activity = (Activity) context;
        application = getApplicationName(context);
        activityName = this.activity.getClass().getSimpleName();

        initializeLogging();

        gestureListener = new GestureListener(application,activityName);

        gestureDetector = new GestureDetector(context, gestureListener);

        allView = LogSave.getAllChildren(activity.getWindow().getDecorView().getRootView());
        logView();
    }

    private void logView() {
        for (View child : allView) {
            if (child instanceof View) {
//                Log.i("allview", viewName(child));
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

    public void initializeLogging() {

        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);

        Log.i(TAG, application);

        LogSave.jSonSave("user", application, activityName, "", "login");
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
        LogSave.jSonSave("user", application, activityName, jenis+" : "+menu, "Touch");
    }


}
