package com.tatamotors.errorproofing.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tatamotors.errorproofing.response.ChildPartsAndFamily;
import com.tatamotors.errorproofing.response.PartResponse;
import com.tatamotors.errorproofing.response.SerialNovalidation;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class AppPreference {
    private static AppPreference theInstance = null;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String login = "login";
    public static final String username = "username";
    public static final String station_name = "station_name";
    public static final String module = "module";
    public static final String booking = "booking";
    public static final String SERIALNO = "SERIALNO-";
    public static final String part_serial = "part_serial";
    public static final String sequenceNumber = "sequenceNumber";
    public static final String SUCCESS = "SUCCESS";
    public static final String token = "token";
    public static final String booking_serial = "booking_serial";
    public static final String complete = "complete";
    public static final String booking_report = "booking_report";
    public static final String booking_complete = "booking_complete";
    public static final String booking_pending = "booking_pending";
    public static final String booking_pending_validation = "booking_pending_validation";
    public static final String skip = "skip_part";
    public static final String skip_booking = "skip_booking";
    public static final String report = "report";
    public static final String part_serial_no = "part_serial_no";
    public static final String sub_part_serial_no = "sub_part_serial_no";
    public static final String part_response = "part_response";
    public static final String child_part_family = "child_part_family";
    public static final int success_response =200;
    public static final String isPostion ="isPosition";
    public static final String station ="station";
    public static final String booked_serial ="booked_serial";

    public static AppPreference getInstance() {
        if (theInstance == null)
            theInstance = new AppPreference();

        return theInstance;
    }

    public void set(Context context, String Key, HashMap<String,List<PartResponse>> listHashMap) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(listHashMap);
            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Key, json);
            editor.apply();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setPostion(Context context, String Key, HashMap<Integer,Boolean> listHashMap) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(listHashMap);
            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Key, json);
            editor.apply();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void set(Context context, String Key, SerialNovalidation serialNovalidation) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(serialNovalidation);
            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Key, json);
            editor.apply();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void set(Context context, String Key, ChildPartsAndFamily childPartsAndFamily) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(childPartsAndFamily);
            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Key, json);
            editor.apply();
        } catch (Exception e){
            e.printStackTrace();
        }

    }


    public void set(Context context, String Key,String keyValue) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Key, keyValue);
            editor.apply();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void set(Context context, String Key,int keyValue) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(Key, keyValue);
            editor.apply();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public HashMap<String,List<PartResponse>> getPartList(Context context, String key) {

        HashMap<String,List<PartResponse>> listHashMap=new HashMap<>();


        String keyValue = "";
        try {
            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            if (prefs != null) {
                keyValue = (prefs.getString((key), ""));
                Gson gson = new Gson();
                Type type = new TypeToken<HashMap<String,List<PartResponse>>>() {}.getType();
                 listHashMap=gson.fromJson(keyValue, type);;
                //  keyValue = EncryptionUtility.decrypt(prefs.getString(EncryptionUtility.encrypt(key), ""));
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return listHashMap;
    }


    public SerialNovalidation getList(Context context, String key) {

        SerialNovalidation serialNovalidation=new SerialNovalidation();


        String keyValue = "";
        try {
            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            if (prefs != null) {
                keyValue = (prefs.getString((key), ""));
                Gson gson = new Gson();
                Type type = new TypeToken<SerialNovalidation>() {}.getType();
                serialNovalidation=gson.fromJson(keyValue, type);;
                //  keyValue = EncryptionUtility.decrypt(prefs.getString(EncryptionUtility.encrypt(key), ""));
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return serialNovalidation;
    }

    public ChildPartsAndFamily getChildFamilyList(Context context, String key) {

        ChildPartsAndFamily childPartsAndFamily=new ChildPartsAndFamily();
        String keyValue = "";
        try {
            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            if (prefs != null) {
                keyValue = (prefs.getString((key), ""));
                Gson gson = new Gson();
                Type type = new TypeToken<ChildPartsAndFamily>() {}.getType();
                childPartsAndFamily=gson.fromJson(keyValue, type);;
                //  keyValue = EncryptionUtility.decrypt(prefs.getString(EncryptionUtility.encrypt(key), ""));
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return childPartsAndFamily;
    }


    public String get(Context context, String key) {
        String keyValue = "";
        try {
            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            if (prefs != null) {
                keyValue = (prefs.getString((key), ""));
                //  keyValue = EncryptionUtility.decrypt(prefs.getString(EncryptionUtility.encrypt(key), ""));
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return keyValue;
    }

    public int getInt(Context context, String key) {
        int keyValue =0;
        try {
            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            if (prefs != null) {
                keyValue = (prefs.getInt((key), 0));
                //  keyValue = EncryptionUtility.decrypt(prefs.getString(EncryptionUtility.encrypt(key), ""));
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return keyValue;
    }

    public int getSubInt(Context context, String key) {
        int keyValue =0;
        try {
            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            if (prefs != null) {
                keyValue = (prefs.getInt((key), 0));
                //  keyValue = EncryptionUtility.decrypt(prefs.getString(EncryptionUtility.encrypt(key), ""));
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return keyValue;
    }

    public void clearPrefernce(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
        } catch (Exception e){

            e.printStackTrace();
        }

    }

}
