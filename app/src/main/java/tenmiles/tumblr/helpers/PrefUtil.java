package tenmiles.tumblr.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Venkatesh S on 13-Nov-16.
 * venkatselva8@gmail.com
 */

public class PrefUtil {
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    public PrefUtil(Context context) {
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        editor = settings.edit();
    }

    public void setStringPref(String key, String value) {
        editor.putString(key, value);
    }

    public void setIntPref(String key, int value) {
        editor.putInt(key, value);
    }

    public void setBooleanPref(String key, boolean value) {
        editor.putBoolean(key, value);
    }

    public void setLongPref(String key, Long value) {
        editor.putLong(key, value);
    }


    public String getStringPref(String key, String defValue) {
        return settings.getString(key, defValue);
    }

    public int getIntPref(String key, int defValue) {
        return settings.getInt(key, defValue);
    }

    public boolean getBooleanPref(String key, boolean defValue) {
        return settings.getBoolean(key, defValue);
    }

    public long getLongPref(String key, Long defValue) {
        return settings.getLong(key, defValue);
    }

    public void removePref(String Key) {
        editor.remove(Key);
    }

    public void clearAllSharedPreferences() {
        if (settings != null)
            settings.edit().clear().commit();
    }

    public void commit() {
        editor.commit();
    }

}
