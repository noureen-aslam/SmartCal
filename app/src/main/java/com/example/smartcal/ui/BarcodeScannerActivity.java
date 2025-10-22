package com.example.smartcal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeScannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // start scanner
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan food product barcode");
        integrator.initiateScan();
    }

    // onActivityResult works with IntentIntegrator
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String barcode = result.getContents();
            if (barcode != null) {
                // Immediately return barcode to caller
                Intent resultIntent = new Intent();
                resultIntent.putExtra("barcode_value", barcode);
                setResult(RESULT_OK, resultIntent);
            } else {
                // user cancelled or empty
                setResult(RESULT_CANCELED);
            }
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        // Do not call finish() here, IntentIntegrator handles it.
    }
}
