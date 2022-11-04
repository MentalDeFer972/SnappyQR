package com.mentaldefer.snappyqr;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.qrcode.encoder.QRCode;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.title_app);
        button = findViewById(R.id.validez_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanOptions options = new ScanOptions();
                options.setPrompt("Veuillez scanner le QR Code");
                options.setBeepEnabled(true);
                options.setOrientationLocked(true);
                options.setCaptureActivity(QRCodeCapAct.class);
            }
        });
    }

    ActivityResultLauncher<ScanOptions> scanOptionsActivityResultLauncher =
            registerForActivityResult(new ScanContract(), result ->
            {
                if (result.getContents() != null){
                    Intent intent = new Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.Events.TITLE,"SnappyQR Event")
                            .putExtra(CalendarContract.EventDays.STARTDAY, result.getContents().toString())
                            .putExtra(CalendarContract.EventDays.ENDDAY, result.getContents().toString());

                            if (intent.resolveActivity(getPackageManager()) != null){
                                startActivity(intent);
                            }
                }
            }
            );



}