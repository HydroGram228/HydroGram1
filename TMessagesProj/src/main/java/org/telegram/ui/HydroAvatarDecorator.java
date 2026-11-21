package org.telegram.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import org.telegram.messenger.AndroidUtilities;

public class HydroAvatarDecorator {

    private static final Paint ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final Paint accentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static boolean initialized = false;

    private static void initPaints() {
        if (initialized) return;
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeCap(Paint.Cap.ROUND);
        accentPaint.setStyle(Paint.Style.FILL);
        initialized = true;
    }

    public static void draw(Canvas canvas, float cx, float cy, float radius) {
        if (!HydroConfig.enableAvatarFrames || HydroConfig.currentSeason == HydroConfig.THEME_NONE) {
            return;
        }

        initPaints();
        float strokeWidth = AndroidUtilities.dp(2.5f);
        ringPaint.setStrokeWidth(strokeWidth);
        float ringRadius = radius + strokeWidth / 2f + AndroidUtilities.dp(1f);

        switch (HydroConfig.currentSeason) {
            case HydroConfig.THEME_OCEAN:
                drawOceanFrame(canvas, cx, cy, ringRadius);
                break;
            case HydroConfig.THEME_NEW_YEAR:
                drawNewYearFrame(canvas, cx, cy, ringRadius);
                break;
            case HydroConfig.THEME_AUTUMN:
                drawAutumnFrame(canvas, cx, cy, ringRadius);
                break;
            case HydroConfig.THEME_SPRING:
                drawSpringFrame(canvas, cx, cy, ringRadius);
                break;
        }
    }

    // 🌊 Океан: Неоновый аквамарин + пузырьки
    private static void drawOceanFrame(Canvas canvas, float cx, float cy, float r) {
        ringPaint.setColor(Color.parseColor("#00E5FF")); // Неоновый океанский циан
        canvas.drawCircle(cx, cy, r, ringPaint);

        accentPaint.setColor(Color.parseColor("#E0F7FA")); // Светлые пузырьки
        for (int i = 0; i < 4; i++) {
            double angle = Math.toRadians(i * 90 + 25);
            float bx = (float) (cx + r * Math.cos(angle));
            float by = (float) (cy + r * Math.sin(angle));
            canvas.drawCircle(bx, by, AndroidUtilities.dp(1.8f), accentPaint);
        }
    }

    // ❄️ Новый Год: Лед + разноцветная гирлянда
    private static void drawNewYearFrame(Canvas canvas, float cx, float cy, float r) {
        ringPaint.setColor(Color.parseColor("#80D8FF")); // Ледяной цвет
        canvas.drawCircle(cx, cy, r, ringPaint);

        // Огоньки гирлянды: красный, зеленый, желтый, синий
        int[] lights = {
            Color.parseColor("#FF5252"),
            Color.parseColor("#69F0AE"),
            Color.parseColor("#FFD740"),
            Color.parseColor("#40C4FF")
        };

        for (int i = 0; i < 8; i++) {
            accentPaint.setColor(lights[i % lights.length]);
            double angle = Math.toRadians(i * 45);
            float lx = (float) (cx + r * Math.cos(angle));
            float ly = (float) (cy + r * Math.sin(angle));
            canvas.drawCircle(lx, ly, AndroidUtilities.dp(2f), accentPaint);
        }
    }

    // 🍂 Осень: Золотисто-янтарный венок
    private static void drawAutumnFrame(Canvas canvas, float cx, float cy, float r) {
        ringPaint.setColor(Color.parseColor("#FF9800")); // Янтарный оранжевый
        canvas.drawCircle(cx, cy, r, ringPaint);

        int[] autumnColors = {
            Color.parseColor("#D84315"), // Багряный
            Color.parseColor("#FFB300"), // Золотой
            Color.parseColor("#8D6E63")  // Теплый древесный
        };

        for (int i = 0; i < 6; i++) {
            accentPaint.setColor(autumnColors[i % autumnColors.length]);
            double angle = Math.toRadians(i * 60 + 15);
            float lx = (float) (cx + r * Math.cos(angle));
            float ly = (float) (cy + r * Math.sin(angle));
            canvas.drawCircle(lx, ly, AndroidUtilities.dp(2.2f), accentPaint);
        }
    }

    // 🌸 Весна: Нежная сакура и весенние бутоны
    private static void drawSpringFrame(Canvas canvas, float cx, float cy, float r) {
        ringPaint.setColor(Color.parseColor("#FF80AB")); // Розовая сакура
        canvas.drawCircle(cx, cy, r, ringPaint);

        int[] springColors = {
            Color.parseColor("#FFFFFF"), // Белый лепесток
            Color.parseColor("#F48FB1"), // Нежно-розовый
            Color.parseColor("#B9F6CA")  // Свежий росток
        };

        for (int i = 0; i < 6; i++) {
            accentPaint.setColor(springColors[i % springColors.length]);
            double angle = Math.toRadians(i * 60);
            float sx = (float) (cx + r * Math.cos(angle));
            float sy = (float) (cy + r * Math.sin(angle));
            canvas.drawCircle(sx, sy, AndroidUtilities.dp(2f), accentPaint);
        }
    }
}
