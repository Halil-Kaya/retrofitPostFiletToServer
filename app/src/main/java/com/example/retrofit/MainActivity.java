package com.example.retrofit;

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
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.retrofit.model.User;
import com.example.retrofit.service.ApiClient;
import com.example.retrofit.service.IUploadService;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

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
        Toast.makeText(this,"uploat etmeye basliyor",Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"image Path: " + imagePath,Toast.LENGTH_SHORT).show();

        IUploadService uploadService = ApiClient.getClient().create(IUploadService.class);

        File file = new File(imagePath);


        RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM,etVideoName.getText().toString());


        RequestBody filePart = RequestBody.create(MediaType.parse("multipart/form-data"),file);

        MultipartBody.Part files = MultipartBody.Part.createFormData("photo",file.getName(),filePart);
        Toast.makeText(this,"paketleri olusturdu " + imagePath,Toast.LENGTH_SHORT).show();

        Call<ResponseBody> call = uploadService.Upload(files,descriptionPart);
        Toast.makeText(this,"gondermeye hazirlaniyor" + imagePath,Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(MainActivity.this,"gonderdi" + imagePath,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this,"hata sebebi: " + t.getMessage() + imagePath,Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,"hata sebebi: " + t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });


    /*
            File file = new File(imagePath);

            RequestBody photoContent = RequestBody.create(MediaType.parse("multipart/form-data"),file);

            MultipartBody.Part photo = MultipartBody.Part.createFormData("photo",file.getName(),photoContent);

            RequestBody description = RequestBody.create(MediaType.parse("text/plain"),etVideoName.getText().toString());

            IUploadService uploadService = ApiClient.getClient().create(IUploadService.class);



            uploadService.Upload(photo,description).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"işlem başarılı",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.println("Warning");
                }
            });

*/

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
        System.out.println("result: " + result);
        return result;
    }

    public static void verifiyStoragePermissions(Activity activity){
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

    }


}