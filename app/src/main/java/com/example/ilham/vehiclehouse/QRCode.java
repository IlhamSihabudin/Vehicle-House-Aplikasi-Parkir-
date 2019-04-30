package com.example.ilham.vehiclehouse;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRCode extends AppCompatActivity {
    ImageView imgQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        imgQrCode = findViewById(R.id.qr_code);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        String id_user = db.select();

        //Convert To QR Code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            Intent intent = getIntent();
            BitMatrix bitMatrix = multiFormatWriter.encode(id_user, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imgQrCode.setImageBitmap(bitmap);
        }catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
