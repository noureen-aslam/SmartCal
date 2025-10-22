package com.example.smartcal.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Custom view for a donut-style pie chart. Uses PieChartData (simple POJO: label, value, color).
 * Ensure Paint.Align is used (not Paint.ALIGN) and Locale import is present.
 */
public class PieChartView extends View {

    private List<PieChartData> data = Collections.emptyList();
    private final Paint paint;
    private final Paint textPaint;
    private final RectF oval;
    private float totalValue = 0;

    // Constants for drawing
    private static final int STROKE_WIDTH = 60; // Thickness of the chart ring
    private static final int TEXT_SIZE = 36;
    private static final int CENTER_TEXT_SIZE = 72;
    private static final int TEXT_OFFSET = 12;

    public PieChartView(Context context) {
        this(context, null);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        // Paint for arcs
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setStrokeCap(Paint.Cap.BUTT);

        // Paint for text
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.LEFT);

        oval = new RectF();
    }

    public void setData(List<PieChartData> data) {
        this.data = data == null ? Collections.emptyList() : data;
        this.totalValue = 0;
        for (PieChartData item : this.data) {
            this.totalValue += item.value;
        }
        requestLayout();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int size = Math.min(w, h);
        int padding = STROKE_WIDTH / 2 + TEXT_SIZE * 2;
        oval.set(padding, padding, size - padding, size - padding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data.isEmpty() || totalValue == 0) {
            drawEmptyChart(canvas);
            return;
        }

        float currentStartAngle = -90f;
        float center_x = getWidth() / 2f;
        float center_y = getHeight() / 2f;
        float radius = oval.width() / 2f;

        for (PieChartData item : data) {
            float sweepAngle = (item.value / totalValue) * 360f;
            paint.setColor(item.color);
            canvas.drawArc(oval, currentStartAngle, sweepAngle, false, paint);

            float midAngle = currentStartAngle + sweepAngle / 2f;
            float midAngleRad = (float) Math.toRadians(midAngle);

            float textRadius = radius + STROKE_WIDTH;
            float text_x = center_x + textRadius * (float) Math.cos(midAngleRad);
            float text_y = center_y + textRadius * (float) Math.sin(midAngleRad);

            if (midAngle > 90 && midAngle < 270) {
                textPaint.setTextAlign(Paint.Align.RIGHT);
                text_x -= TEXT_OFFSET;
            } else {
                textPaint.setTextAlign(Paint.Align.LEFT);
                text_x += TEXT_OFFSET;
            }

            text_y += TEXT_SIZE / 3f;
            String labelText = String.format(Locale.getDefault(), "%s %.0f", item.label, item.value);
            canvas.drawText(labelText, text_x, text_y, textPaint);

            currentStartAngle += sweepAngle;
        }

        drawCenterText(canvas);
    }

    private void drawCenterText(Canvas canvas) {
        textPaint.setTextSize(CENTER_TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.DKGRAY);

        String text = String.format(Locale.getDefault(), "%.0f", totalValue);
        String label = "Total (kcal)";

        float x = oval.centerX();
        float y = oval.centerY();

        canvas.drawText(text, x, y, textPaint);

        textPaint.setTextSize(TEXT_SIZE);
        canvas.drawText(label, x, y + CENTER_TEXT_SIZE, textPaint);

        textPaint.setTextSize(TEXT_SIZE);
    }

    private void drawEmptyChart(Canvas canvas) {
        paint.setColor(Color.LTGRAY);
        canvas.drawArc(oval, 0, 360, false, paint);

        textPaint.setTextSize(CENTER_TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.DKGRAY);

        String text = "No Data";
        float x = oval.centerX();
        float y = oval.centerY() - ((textPaint.descent() + textPaint.ascent()) / 2);

        canvas.drawText(text, x, y, textPaint);

        textPaint.setTextSize(TEXT_SIZE);
    }
}
