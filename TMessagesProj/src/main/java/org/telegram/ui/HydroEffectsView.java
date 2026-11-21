package org.telegram.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import java.util.Random;

public class HydroEffectsView extends View {

    private static class Particle {
        float x, y;
        float vx, vy;
        float size;
        int color;
        float angle;
        float vAngle;
    }

    private static final int MAX_PARTICLES = 35;
    private final Particle[] particles = new Particle[MAX_PARTICLES];
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint garlandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random random = new Random();

    // Пасхалка (Дед Мороз / Кит)
    private float easterX = -500f;
    private float easterY = 150f;
    private float easterSpeed = 3f;
    private long lastEasterTime = 0;

    public HydroEffectsView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(AndroidUtilities.dp(32));

        for (int i = 0; i < MAX_PARTICLES; i++) {
            particles[i] = new Particle();
            resetParticle(particles[i], true);
        }
    }

    private void resetParticle(Particle p, boolean randomY) {
        int w = getWidth() > 0 ? getWidth() : AndroidUtilities.displaySize.x;
        int h = getHeight() > 0 ? getHeight() : AndroidUtilities.displaySize.y;

        p.x = random.nextFloat() * w;
        p.size = AndroidUtilities.dp(3 + random.nextInt(6));
        p.angle = random.nextFloat() * 360f;
        p.vAngle = (random.nextFloat() - 0.5f) * 2f;

        switch (HydroConfig.currentSeason) {
            case HydroConfig.THEME_OCEAN: // Пузыри снизу вверх
                p.y = randomY ? random.nextFloat() * h : h + p.size;
                p.vy = -(1.5f + random.nextFloat() * 2f);
                p.vx = (random.nextFloat() - 0.5f) * 0.8f;
                p.color = Color.argb(120 + random.nextInt(60), 0, 229, 255);
                break;

            case HydroConfig.THEME_NEW_YEAR: // Снежинки сверху вниз
                p.y = randomY ? random.nextFloat() * h : -p.size;
                p.vy = 1.2f + random.nextFloat() * 2f;
                p.vx = (random.nextFloat() - 0.5f) * 1.2f;
                p.color = Color.argb(160 + random.nextInt(70), 220, 245, 255);
                break;

            case HydroConfig.THEME_AUTUMN: // Листья кружатся и падают
                p.y = randomY ? random.nextFloat() * h : -p.size;
                p.vy = 1.0f + random.nextFloat() * 1.8f;
                p.vx = (random.nextFloat() - 0.5f) * 2.0f;
                int[] autumnColors = {0xFFD84315, 0xFFFF9800, 0xFFFFB300, 0xFF8D6E63};
                p.color = autumnColors[random.nextInt(autumnColors.length)];
                break;

            case HydroConfig.THEME_SPRING: // Лепестки сакуры
                p.y = randomY ? random.nextFloat() * h : -p.size;
                p.vy = 0.9f + random.nextFloat() * 1.5f;
                p.vx = 0.5f + random.nextFloat() * 1.5f;
                int[] springColors = {0xFFFF80AB, 0xFFF48FB1, 0xFFFFFFFF};
                p.color = springColors[random.nextInt(springColors.length)];
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!HydroConfig.enableParticles || HydroConfig.currentSeason == HydroConfig.THEME_NONE) {
            return;
        }

        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) return;

        // 🎄 Гирлянда для Нового года сверху экрана
        if (HydroConfig.currentSeason == HydroConfig.THEME_NEW_YEAR) {
            drawGarland(canvas, w);
        }

        // 🌟 Отрисовка частиц (снежинки, пузыри, листья)
        for (Particle p : particles) {
            p.x += p.vx;
            p.y += p.vy;
            p.angle += p.vAngle;

            // Если частица улетела за экран — пересоздаем
            if (p.y < -50 || p.y > h + 50 || p.x < -50 || p.x > w + 50) {
                resetParticle(p, false);
            }

            paint.setColor(p.color);
            canvas.drawCircle(p.x, p.y, p.size, paint);
        }

        // 🎅 Пасхалки (Дед Мороз / Кит)
        if (HydroConfig.enableEasterEggs) {
            drawEasterEgg(canvas, w, h);
        }

        invalidate(); // Запуск следующего кадра анимации
    }

    private void drawGarland(Canvas canvas, int width) {
        int lightsCount = 12;
        float step = (float) width / lightsCount;
        int[] lightColors = {0xFFFF5252, 0xFF69F0AE, 0xFFFFD740, 0xFF40C4FF};

        for (int i = 0; i <= lightsCount; i++) {
            garlandPaint.setColor(lightColors[i % lightColors.length]);
            float cx = i * step;
            float cy = AndroidUtilities.dp(8);
            canvas.drawCircle(cx, cy, AndroidUtilities.dp(4), garlandPaint);
        }
    }

    private void drawEasterEgg(Canvas canvas, int width, int height) {
        long now = System.currentTimeMillis();

        // Запуск раз в 35 секунд
        if (easterX > width + 200 || easterX < -400) {
            if (now - lastEasterTime > 35000) {
                easterX = -100;
                easterY = AndroidUtilities.dp(80 + random.nextInt(200));
                lastEasterTime = now;
            } else {
                return;
            }
        }

        easterX += easterSpeed;

        if (HydroConfig.currentSeason == HydroConfig.THEME_NEW_YEAR) {
            canvas.drawText("🎅🛷🦌", easterX, easterY, textPaint);
        } else if (HydroConfig.currentSeason == HydroConfig.THEME_OCEAN) {
            canvas.drawText("🐋", easterX, easterY, textPaint);
        } else if (HydroConfig.currentSeason == HydroConfig.THEME_SPRING) {
            canvas.drawText("✨🦋", easterX, easterY, textPaint);
        }
    }
}
