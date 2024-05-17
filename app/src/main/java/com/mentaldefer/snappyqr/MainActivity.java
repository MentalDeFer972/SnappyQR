package com.mentaldefer.snappyqr;

import static android.util.Log.INFO;
import static android.util.Log.VERBOSE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button button;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int QR_SCAN_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.title_app);
        button = findViewById(R.id.validez_button);
        button.setOnClickListener(view -> checkCameraPermission());
    }

    protected void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            startQRScanActivity();
        }
    }

    private void startQRScanActivity() {
        Intent intent = new Intent(MainActivity.this, QRScanActivity.class);
        startActivityForResult(intent, QR_SCAN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_SCAN_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String qrContent = data.getStringExtra("qrContent");
                addToCalendar(qrContent);
            }
        }
    }

    private void addToCalendar(String qrContent) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try{
                Date date = dateFormat.parse(qrContent);
                Calendar calendar = Calendar.getInstance();

                if (date != null){
                    calendar.setTime(date);
                    Intent intent = new Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.Events.TITLE,"Evénement QR Code")
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.getTimeInMillis())
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendar.getTimeInMillis() + 60 * 60 * 1000)
                            .putExtra(CalendarContract.Events.ALL_DAY,true)
                            .putExtra(CalendarContract.Events.EVENT_TIMEZONE,Calendar.getInstance().getTimeZone().getID());
                    startActivity(intent);
                }


            }catch (ParseException e) {
                Toast.makeText(this,"Format de date incorrect dans le code QR",Toast.LENGTH_SHORT).show();
        }
    }
}