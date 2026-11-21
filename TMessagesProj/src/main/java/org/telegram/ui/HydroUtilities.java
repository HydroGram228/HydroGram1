package org.telegram.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import java.util.regex.Pattern;

public class HydroUtilities {

    private static final Paint blurCensorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final RectF censorRect = new RectF();

    static {
        blurCensorPaint.setColor(Color.parseColor("#44808080")); // Полупрозрачный матовый блюр-фильтр
        blurCensorPaint.setStyle(Paint.Style.FILL);
    }

    // 🔗 1. Чистые ссылки: вырезает шпионские трекеры из TikTok, YouTube, Insta и др.
    public static String cleanUrl(String url) {
        if (!HydroConfig.cleanLinksEnabled || url == null || !url.contains("http")) {
            return url;
        }

        try {
            // Удаляем параметры: utm_*, si, igshid, fbclid, gclid, feature, ref_src и прочие
            String cleaned = url.replaceAll("([?&])(utm_[a-zA-Z0-9_]+|si|igshid|fbclid|gclid|feature|ref_src|is_from_webapp)=[^&#]*", "");
            // Подчищаем висящие знаки ? и &
            cleaned = cleaned.replaceAll("[?&]$", "");
            cleaned = cleaned.replaceAll("\\?&", "?");
            return cleaned;
        } catch (Exception e) {
            return url;
        }
    }

    // 🚇 2. Режим «Инкогнито в метро»: зарисовывает текст или аватарку матовой плашкой
    public static void drawMetroCensor(Canvas canvas, float left, float top, float right, float bottom) {
        if (!HydroConfig.incognitoMetroMode) {
            return;
        }
        censorRect.set(left, top, right, bottom);
        canvas.drawRoundRect(censorRect, AndroidUtilities.dp(6), AndroidUtilities.dp(6), blurCensorPaint);
    }

    // 🔍 3. Быстрый предпросмотр (Peek & Pop): масштабирует фото при удержании
    public static void setupPeekPreview(View mediaView) {
        if (!HydroConfig.peekPreviewEnabled || mediaView == null) {
            return;
        }

        mediaView.setOnLongClickListener(v -> {
            v.animate()
                    .scaleX(1.15f)
                    .scaleY(1.15f)
                    .setDuration(180)
                    .start();
            return true;
        });

        // Возврат в исходный размер при отпускании
        mediaView.setOnTouchListener((v, event) -> {
            if (event.getAction() == android.view.MotionEvent.ACTION_UP || 
                event.getAction() == android.view.MotionEvent.ACTION_CANCEL) {
                v.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(150)
                        .start();
            }
            return false;
        });
    }
}
