package org.telegram.ui;

import android.content.Context;
import android.content.SharedPreferences;
import org.telegram.messenger.ApplicationLoader;

public class HydroConfig {
    public static final int THEME_NONE = 0;
    public static final int THEME_OCEAN = 1;
    public static final int THEME_NEW_YEAR = 2;
    public static final int THEME_AUTUMN = 3;
    public static final int THEME_SPRING = 4;

    private static final String PREFS_NAME = "hydro_config";
    private static SharedPreferences preferences;

    public static int currentSeason = THEME_OCEAN;
    public static boolean enableParticles = true;
    public static boolean enableAvatarFrames = true;
    public static boolean enableEasterEggs = true;

    // 🕵️ Новые функции удобства
    public static boolean incognitoMetroMode = false; // Режим Инкогнито (блюр в метро)
    public static boolean cleanLinksEnabled = true;   // Очистка ссылок от мусора
    public static boolean peekPreviewEnabled = true;  // Быстрый предпросмотр фото по удержанию

    public static void loadConfig() {
        if (ApplicationLoader.applicationContext == null) return;
        preferences = ApplicationLoader.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        currentSeason = preferences.getInt("current_season", THEME_OCEAN);
        enableParticles = preferences.getBoolean("particles", true);
        enableAvatarFrames = preferences.getBoolean("avatar_frames", true);
        enableEasterEggs = preferences.getBoolean("easter_eggs", true);
        incognitoMetroMode = preferences.getBoolean("incognito_metro", false);
        cleanLinksEnabled = preferences.getBoolean("clean_links", true);
        peekPreviewEnabled = preferences.getBoolean("peek_preview", true);
    }

    public static void toggleIncognitoMetro() {
        incognitoMetroMode = !incognitoMetroMode;
        if (preferences != null) {
            preferences.edit().putBoolean("incognito_metro", incognitoMetroMode).apply();
        }
    }

    public static void setSeason(int season) {
        currentSeason = season;
        if (preferences != null) {
            preferences.edit().putInt("current_season", season).apply();
        }
    }

    public static void toggleParticles(boolean enabled) {
        enableParticles = enabled;
        if (preferences != null) {
            preferences.edit().putBoolean("particles", enabled).apply();
        }
    }

    public static void toggleAvatarFrames(boolean enabled) {
        enableAvatarFrames = enabled;
        if (preferences != null) {
            preferences.edit().putBoolean("avatar_frames", enabled).apply();
        }
    }
}
