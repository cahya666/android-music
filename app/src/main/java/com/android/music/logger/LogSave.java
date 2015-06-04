package com.android.music.logger;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
        File file = new File(myDir,fileName+".json");
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file,false);
            //System.out.println(file.toString());
            fout.write(text.getBytes());
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void jSonSave(String user,String application,String activity,String view,String event) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = format.format(new Date());

        String jsonText = jsonText(application);
        try {

            JSONObject jsonObject;
            JSONArray array;
            if (jsonText.isEmpty()) {
                jsonObject = new JSONObject();
                array = new JSONArray();
            } else {
                jsonObject = new JSONObject(jsonText);
                array = jsonObject.getJSONArray(application);
            }

            JSONObject data = new JSONObject();
            data.put("user", user);
            //data.put("application", application);
            data.put("activity", activity);
            data.put("view", view);
            data.put("event", event);
            data.put("timestamp", timestamp);

            array.put(data);

            jsonObject.put(application, array);

            fileSave(jsonObject.toString(), application);

            //Log.i("json", jsonObject.toString());

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

    }

    static String jsonText(String filename){
        StringBuilder text = new StringBuilder();
        String result="";

        try {
            File root = Environment.getExternalStorageDirectory();
            File myDir = new File(root + "/Tesis");
            myDir.mkdirs();
            File file = new File(myDir,filename + ".json");

            if (file.exists()){
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();
                result = text.toString();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    static void printLog(String application){
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/Tesis");
            myDir.mkdirs();
            File logfile = new File(myDir,application+".log");

            if (logfile.exists()){
                logfile.delete();
            }

            logfile.createNewFile();
            String cmd = "logcat -d -f "+logfile.getAbsolutePath();
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void printLog(String application){
//        String command = "logcat -d *:V";
//        try{
//            Process process = Runtime.getRuntime().exec(command);
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line = null;
//            try{
//                String root = Environment.getExternalStorageDirectory().toString();
//                File myDir = new File(root + "/Tesis");
//                myDir.mkdirs();
//                File file = new File(myDir,application+".log");
//
//                FileOutputStream fout = new FileOutputStream(file,true);
//                while((line = in.readLine()) != null){
//                    line += "\n";
//                    fout.write(line.getBytes());
//                }
//                fout.close();
//            }
//            catch(IOException e){
//                e.printStackTrace();
//            }
//        }
//        catch(IOException e){
//            e.printStackTrace();
//        }
//    }

}
