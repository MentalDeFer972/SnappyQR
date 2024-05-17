package com.mentaldefer.snappyqr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRScanActivity extends AppCompatActivity {
    public QRScanActivity() {
        super();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() != null){
                String qrContent = result.getContents();
                sendResultToMainActivity(qrContent);
            }
            else{
                Toast.makeText(this,"Aucun code QR détectée",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    private void sendResultToMainActivity(String qrContent) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("qrContent",qrContent);
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false)
                        .setPrompt("Scanner un code QR contenant la date au format jj-mm-aaaa")
                            .setBeepEnabled(true)
                                .initiateScan();
    }

}
