package com.android.music.logger;

import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cahya on 26/05/2015.
 */
public class LogSave {

    static void fileSave(String text, String fileName){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Tesis");
        myDir.mkdirs();
        File file = new File(myDir,fileName+".txt");
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file,true);
            //System.out.println(file.toString());
            fout.write(text.getBytes());
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void jSonSave(String user,String application,String activity,String view,String event) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String timestamp = format.format(new Date());
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user", user);
            jsonObject.put("application", application);
            jsonObject.put("activity", activity);
            jsonObject.put("view", view);
            jsonObject.put("event", event);
            jsonObject.put("timestamp", timestamp);

            fileSave(jsonObject.toString() + ",", application);

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

    }

    static ArrayList<View> getAllChildren(View v) {
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
