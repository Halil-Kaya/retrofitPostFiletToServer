package com.example.retrofittekrartest2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.retrofittekrartest2.model.Test;
import com.example.retrofittekrartest2.service.ApiClient;
import com.example.retrofittekrartest2.service.IUploadService;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_IMAGE_PICKER = 100;
    private final static int REQUEST_EXTERNAL_STORAGE = 1;
    private final static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    ImageView image_view;
    EditText etVideoName;
    Button btnUpload;
    String imagePath;

    Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifiyStoragePermissions(MainActivity.this);

        image_view = findViewById(R.id.image_view);
        etVideoName = findViewById(R.id.etVideoName);
        btnUpload = findViewById(R.id.btnUpload);




        image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openImageChooser();

            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadToServer();

            }
        });


    }


    public void uploadToServer(){




        IUploadService uploadService = ApiClient.getClient().create(IUploadService.class);
        File file = new File(imagePath);
        Toast.makeText(this,"total space: "+file,Toast.LENGTH_SHORT).show();
        System.out.println(file.getTotalSpace());
        RequestBody filePart =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        file
                );
        RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM,etVideoName.getText().toString());
        MultipartBody.Part files = MultipartBody.Part.createFormData("photo",file.getName(),filePart);

        Call<ResponseBody> call = uploadService.Upload(files,descriptionPart);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(MainActivity.this,"işlem başarılı",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {



                Toast.makeText(MainActivity.this,"hata oldu sebebi: " + t.getMessage(),Toast.LENGTH_SHORT).show();
                System.out.println(t.getLocalizedMessage());
                t.printStackTrace();

            }
        });



    }




    public void openImageChooser(){
        Intent intent = new Intent();
        //video
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        Intent result = Intent.createChooser(intent,"asdasdasd");
        startActivityForResult(result,REQUEST_CODE_IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_IMAGE_PICKER){
            if(resultCode == RESULT_OK){
                Uri uri = data.getData();
                fileUri = data.getData();
                imagePath = getRealPathFromURI(uri);
                image_view.setImageURI(uri);

            }
        }
    }


    public String getRealPathFromURI(Uri contentUri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(),contentUri,proj,null,null,null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public static void verifiyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}