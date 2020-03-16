package com.beytech.flashlight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.Manifest;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.view.View;
import android.widget.Toast;

import com.beytech.flashlight.R;

public class MainActivity extends AppCompatActivity {

        private ImageView imageFlashlight;
        private static final int CAMERA_REQUEST_CODE = 50;
        boolean hasCameraFlashFeature = false;
        private boolean isFlashOn=false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            imageFlashlight = (ImageView) findViewById(R.id.imageFlashlight);

            hasCameraFlashFeature = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

            imageFlashlight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    askPermission(Manifest.permission.CAMERA,CAMERA_REQUEST_CODE);

                }
            });
        }

        private void flashLight() {
            if (hasCameraFlashFeature) {
                if (isFlashOn) {
                    imageFlashlight.setImageResource(R.drawable.torchoff);
                    flashLightOff();
                    isFlashOn=false;
                } else {
                    imageFlashlight.setImageResource(R.drawable.torch);
                    flashLightOn();
                    isFlashOn=true;
                }
            } else {
                Toast.makeText(MainActivity.this, "No flash available on your device",
                        Toast.LENGTH_SHORT).show();
            }
        }

        private void flashLightOn() {
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

            try {
                String cameraId = cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(cameraId, true);
            } catch (CameraAccessException e) {
            }
        }

        private void flashLightOff() {
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
                String cameraId = cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(cameraId, false);
            } catch (CameraAccessException e) {
            }
        }

        private void askPermission(String permission,int requestCode) {
            if (ContextCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{permission},requestCode);

            }else {
                flashLight();
            }

        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        hasCameraFlashFeature = getPackageManager().
                                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                        Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_LONG).show();
                        flashLight();

                    } else {
                        Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }

