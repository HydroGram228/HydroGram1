package org.telegram.ui;

import android.content.Context;
import android.content.SharedPreferences;
import org.telegram.messenger.ApplicationLoader;
import org.json.JSONArray;
import java.util.ArrayList;

public class HydroQuickNotes {

    private static final String PREF_NAME = "hydro_quick_notes";
    private static final String KEY_NOTES = "saved_notes_list";
    private static SharedPreferences preferences;
    private static final ArrayList<String> notesList = new ArrayList<>();
    private static boolean loaded = false;

    private static void ensureLoaded() {
        if (loaded) return;
        if (ApplicationLoader.applicationContext == null) return;

        preferences = ApplicationLoader.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = preferences.getString(KEY_NOTES, null);

        notesList.clear();
        if (json != null) {
            try {
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    notesList.add(array.getString(i));
                }
            } catch (Exception ignored) {}
        } else {
            // Начальные стандартные примеры
            notesList.add("Привет! Я сейчас занят, отвечу чуть позже 👋");
            notesList.add("Отправил в личные сообщения ✅");
            notesList.add("Скинул реквизиты ниже 👇");
        }
        loaded = true;
    }

    public static ArrayList<String> getNotes() {
        ensureLoaded();
        return notesList;
    }

    public static void addNote(String note) {
        if (note == null || note.trim().isEmpty()) return;
        ensureLoaded();
        notesList.add(0, note.trim()); // Добавляем наверх списка
        saveToPrefs();
    }

    public static void removeNote(int index) {
        ensureLoaded();
        if (index >= 0 && index < notesList.size()) {
            notesList.remove(index);
            saveToPrefs();
        }
    }

    private static void saveToPrefs() {
        if (preferences == null) return;
        JSONArray array = new JSONArray();
        for (String s : notesList) {
            array.put(s);
        }
        preferences.edit().putString(KEY_NOTES, array.toString()).apply();
    }
}
