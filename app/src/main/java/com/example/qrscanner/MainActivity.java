package com.example.qrscanner;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends AppCompatActivity{


    private Button button,gen_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.scan_btn);
        gen_btn = findViewById(R.id.gen_btn);
        button.setOnClickListener(v -> {
            scanCode();
        });

        gen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Code_gen.class));
            }
        });

    }

    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result !=null){
            if (result.getContents() !=null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                 builder.setTitle("Scan Result");
                 builder.setMessage(result.getContents());
                 builder.setPositiveButton("Scan Again", (dialog, which) -> scanCode());
                 builder.setNegativeButton("Finish", (dialog, which) -> finish());
                 AlertDialog dialog = builder.create();
                 dialog.show();


            }
            else {
                Toast.makeText(this,"Scanning Result Not Found !!",Toast.LENGTH_SHORT).show();

            }
        }
        else {
            super.onActivityResult(requestCode,resultCode,data);
        }

    }
}