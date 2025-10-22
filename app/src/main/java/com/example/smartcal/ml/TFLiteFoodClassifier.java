package com.example.smartcal.ml;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.Interpreter;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.*;

public class TFLiteFoodClassifier {

    private static final String MODEL = "model_food.tflite";
    private static final String LABELS = "labelmap.txt";
    private Interpreter interpreter;
    private List<String> labels;
    private Context ctx;

    public TFLiteFoodClassifier(Context ctx) {
        this.ctx = ctx;
        try {
            MappedByteBuffer model = FileUtil.loadMappedFile(ctx, MODEL);
            interpreter = new Interpreter(model);
            labels = FileUtil.loadLabels(ctx, LABELS);
        } catch (IOException e) {
            Log.w("TFLiteFoodClassifier", "Model or labels not found in assets. Place model_food.tflite and labelmap.txt in assets.");
            interpreter = null;
            labels = new ArrayList<>();
        }
    }

    public static class Prediction {
        public String label;
        public float confidence;
    }

    public List<Prediction> classify(Bitmap bitmap, int topK) {
        if (interpreter == null) return Collections.emptyList();
        int inputSize = 224;
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true);

        TensorImage tImage = TensorImage.fromBitmap(resized);
        float[][] output = new float[1][labels.size()];
        interpreter.run(tImage.getBuffer(), output);
        float[] preds = output[0];
        PriorityQueue<Prediction> pq = new PriorityQueue<>(Comparator.comparingDouble(p -> p.confidence));
        for (int i = 0; i < preds.length; i++) {
            Prediction p = new Prediction();
            p.label = i < labels.size() ? labels.get(i) : "label_" + i;
            p.confidence = preds[i];
            pq.offer(p);
            if (pq.size() > topK) pq.poll();
        }
        List<Prediction> results = new ArrayList<>();
        while (!pq.isEmpty()) results.add(0, pq.poll());
        return results;
    }
}
