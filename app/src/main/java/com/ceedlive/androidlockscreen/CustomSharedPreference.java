package com.ceedlive.androidlockscreen;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomSharedPreference {

    private static SharedPreferences sSharedPreferences;

    public static void init(Context context, String name) {
        if (sSharedPreferences == null) {
            if (name == null) {
                throw new NullPointerException("Prefs name may not be null");
            }
            sSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        }
    }

    public static SharedPreferences getPreferences() {
        if (sSharedPreferences != null) {
            return sSharedPreferences;
        }
        throw new RuntimeException("Prefs class not correctly instantiated please call Prefs.init(context, name) first");
    }

    public static Map<String, ?> getAll() {
        return getPreferences().getAll();
    }

    public static int getInt(final String key, final int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    public static boolean getBoolean(final String key, final boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    public static long getLong(final String key, final long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    public static double getDouble(final String key, final double defValue) {
        return Double.longBitsToDouble(getPreferences().getLong(key, Double.doubleToLongBits(defValue)));
    }

    public static float getFloat(final String key, final float defValue) {
        return getPreferences().getFloat(key, defValue);
    }

    public static String getString(final String key, final String defValue) {
        return getPreferences().getString(key, defValue);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Set<String> getStringSet(final String key, final Set<String> defValue) {
        final SharedPreferences sharedPreferences = getPreferences();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return sharedPreferences.getStringSet(key, defValue);
        } else {
            if (sharedPreferences.contains(key + "#LENGTH")) {
                final HashSet<String> set = new HashSet<String>();
                final int stringSetLength = sharedPreferences.getInt(key + "#LENGTH", -1);
                if (stringSetLength >= 0) {
                    for (int i=0; i<stringSetLength; i++) {
                        sharedPreferences.getString(key + "[" + i + "]", null);
                    }
                }
                return set;
            }
        }
        return defValue;
    }

    public static void putLong(final String key, final long value) {
        final SharedPreferences.Editor editor = getPreferences().edit();
        editor.putLong(key, value);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    public static void putInt(final String key, final int value) {
        final SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(key, value);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    public static void putDouble(final String key, final double value) {
        final SharedPreferences.Editor editor = getPreferences().edit();
        editor.putLong(key, Double.doubleToRawLongBits(value));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    public static void putFloat(final String key, final float value) {
        final SharedPreferences.Editor editor = getPreferences().edit();
        editor.putFloat(key, value);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    public static void putBoolean(final String key, final boolean value) {
        final SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    public static void putString(final String key, final String value) {
        final SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(key, value);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void putStringSet(final String key, final Set<String> value) {
        final SharedPreferences.Editor editor = getPreferences().edit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            editor.putStringSet(key, value);
        } else {
            int stringSetLength = 0;
            if (sSharedPreferences.contains(key + "#LENGTH")) {
                // First read what the value was
                stringSetLength = sSharedPreferences.getInt(key + "#LENGTH", -1);
            }
            editor.putInt(key + "#LENGTH", value.size());

            int i = 0;
            for (String aValue : value) {
                editor.putString(key + "[" + i + "]", aValue);
                i++;
            }
            for (; i<stringSetLength; i++) {
                editor.remove(key + "[" + i + "]");
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    public static void remove(final String key) {
        final SharedPreferences sharedPreferences = getPreferences();
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.contains(key + "#LENGTH")) {
            int stringSetLength = sharedPreferences.getInt(key + "#LENGTH", -1);
            if (stringSetLength >= 0) {
                editor.remove(key + "#LENGTH");
                for (int i=0; i<stringSetLength; i++) {
                    editor.remove(key + "[" + i + "]");
                }
            }
        }
        editor.remove(key);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    public static void removeAll() {
        clear().apply();
    }

    public static boolean contains(final String key) {
        return getPreferences().contains(key);
    }

    public static SharedPreferences.Editor clear() {
        return getPreferences().edit().clear();
    }
}
