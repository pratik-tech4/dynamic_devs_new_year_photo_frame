package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common;

import android.content.Context;
import android.content.SharedPreferences;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.R;

public class SharedPreferenceManager {

    public static String PREF_KEY_IS_FIRST_TIME_OPEN = "isAppOpenFirstTime";
    public static final String PRE_KEY_APP_OPEN_COUNT = "KEY_APP_OPEN_COUNT";


    public static void saveAppPreferences(Context context, String key, Object value) {
        SharedPreferences sharedPreferencesTheme = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesTheme.edit();
        if (value instanceof String) {
            editor.putString(key, ((String) value));
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, ((boolean) value));
        } else if (value instanceof Integer) {
            editor.putInt(key, ((int) value));
        }
        editor.apply();
    }

    public static String getPreferenceStringValue(Context context, String key) {
        SharedPreferences sharedPreferencesTheme = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedPreferencesTheme.getString(key, "");
    }

    public static boolean getPreferenceBooleanValue(Context context, String key) {
        SharedPreferences sharedPreferencesTheme = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedPreferencesTheme.getBoolean(key, false);
    }

    public static int getPreferenceIntegerValue(Context context, String key) {
        SharedPreferences sharedPreferencesTheme = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedPreferencesTheme.getInt(key, 0);
    }

    public static void setKeyboard(Context context, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(context.getString(R.string.app_name), 0).edit();
        edit.putInt("height_of_keyboard", i);
        edit.apply();
    }

    public static int getKeyboard(Context context) {
        return context.getSharedPreferences(context.getString(R.string.app_name), 0).getInt("height_of_keyboard", -1);
    }

    public static void setHeightOfNotch(Context context, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(context.getString(R.string.app_name), 0).edit();
        edit.putInt("height_of_notch", i);
        edit.apply();
    }

    public static int getHeightOfNotch(Context context) {
        return context.getSharedPreferences(context.getString(R.string.app_name), 0).getInt("height_of_notch", -1);
    }

}
