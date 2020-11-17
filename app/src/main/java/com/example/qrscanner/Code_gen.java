package com.example.qrscanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class Code_gen extends AppCompatActivity {

    private static String message = "";
    private String type = "";
    private Button button_generate;
    private TextInputLayout editText1;
    private Spinner type_spinner;
    private ImageView imageView;
    private int size = 660;
    private int size_width = 660;
    private int size_height = 264;
    private TextView success_text;
    private ImageView success_imageview;

    private String time;

    private Bitmap myBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_gen);

        button_generate = findViewById(R.id.generate_button);
        editText1 = findViewById(R.id.input_text);
        type_spinner = findViewById(R.id.type_spinner);
        imageView = findViewById(R.id.image_imageview);

        type_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        type = "QR Code";
                        break;
                    case 1:
                        type = "Barcode";
                        break;
                    case 2:
                        type = "Data Matrix";
                        break;
                    case 3:
                        type = "PDF 417";
                        break;
                    case 4:
                        type = "Barcode-39";
                        break;
                    case 5:
                        type = "Barcode-93";
                        break;
                    case 6:
                        type = "AZTEC";
                        break;
                    default:
                        type = "QR Code";
                        break;
                }
                Log.d("type", type);
            }

        });

        button_generate.setOnClickListener(v -> {

            message = editText1.getEditText().getText().toString();
            if (message.equals("") || type.equals("")) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Code_gen.this);
                dialog.setTitle("Error");
                dialog.setMessage("Invalid input!");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  do nothing
                    }
                });
                dialog.create();
                dialog.show();
            }else{
                Bitmap bitmap = null;
                try {
                    bitmap = CreateImage(message,type);
                    myBitmap = bitmap;
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (bitmap !=null){
                    imageView.setImageBitmap(myBitmap);
                }

            }
        });

    }

    private Bitmap CreateImage(String message, String type) {
        BitMatrix bitMatrix = null;
        // BitMatrix bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.QR_CODE, size, size);
        switch (type)
        {
            case "QR Code":
                try {
                    bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.QR_CODE, size, size);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
            case "Barcode":
                try {
                    bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.CODE_128, size_width, size_height);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
            case "Data Matrix":
                try {
                    bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.DATA_MATRIX, size, size);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
            case "PDF 417":
                try {
                    bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.PDF_417, size_width, size_height);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
            case "Barcode-39":
                try {
                    bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.CODE_39, size_width, size_height);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
            case "Barcode-93":
                try {
                    bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.CODE_93, size_width, size_height);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
            case "AZTEC":
                try {
                    bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.AZTEC, size, size);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
            default:
                try {
                    bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.QR_CODE, size, size);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
        }
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        int [] pixels = new int [width * height];
        for (int i = 0 ; i < height ; i++)
        {
            for (int j = 0 ; j < width ; j++)
            {
                if (bitMatrix.get(j, i))
                {
                    pixels[i * width + j] = 0xff000000;
                }
                else
                {
                    pixels[i * width + j] = 0xffffffff;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public void saveBitmap (Bitmap bitmap, String message, String bitName)
    {

        String[] PERMISSIONS = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE" };
        int permission = ContextCompat.checkSelfPermission(this,
                "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS,1);
        }

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millisecond = calendar.get(Calendar.MILLISECOND);

        String fileName = message + "_at_" + String.valueOf(year) + "_" + String.valueOf(month) + "_" + String.valueOf(day) + "_" + String.valueOf(hour) + "_" + String.valueOf(minute) + "_" + String.valueOf(second) + "_"  + String.valueOf(millisecond);
        time = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day) + " " + String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String.valueOf(second) + "." + String.valueOf(millisecond);
        File file;

        String fileLocation;

        String folderLocation;

        if(Build.BRAND.equals("Xiaomi") ){
            fileLocation = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/AndroidBarcodeGenerator/" + fileName + bitName ;
            folderLocation = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/AndroidBarcodeGenerator/";
        }else{
            fileLocation = Environment.getExternalStorageDirectory().getPath()+"/DCIM/AndroidBarcodeGenerator/" + fileName + bitName ;
            folderLocation = Environment.getExternalStorageDirectory().getPath()+"/DCIM/AndroidBarcodeGenerator/";
        }

        Log.d("file_location", fileLocation);

        file = new File(fileLocation);

        File folder = new File(folderLocation);
        if (!folder.exists())
        {
            folder.mkdirs();
        }

        if (file.exists())
        {
            file.delete();
        }


        FileOutputStream out;

        try
        {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out))
            {
                out.flush();
                out.close();
            }
        }
        catch (FileNotFoundException fnfe)
        {
            fnfe.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            // Save image
            saveBitmap(myBitmap, message, ".jpg");

            LayoutInflater layoutInflater = LayoutInflater.from(Code_gen.this);
            View view = layoutInflater.inflate(R.layout.success_dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(Code_gen.this);
            builder.setTitle("Summary");
            builder.setCancelable(false);
            builder.setView(view);
            success_text = (TextView) view.findViewById(R.id.success_text);
            success_text.setText(message + "\n\n" + time);
            success_imageview = (ImageView) view.findViewById(R.id.success_imageview);
            success_imageview.setImageBitmap(myBitmap);
            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            });
            builder.create();
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}


