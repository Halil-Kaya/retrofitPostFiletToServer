package com.example.retrofittekrartest2.service;

import com.example.retrofittekrartest2.model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IUploadService {
    @Multipart
    @POST("api/file")
    Call<ResponseBody> Upload(
            @Part MultipartBody.Part photo,
            @Part("description") RequestBody description
    );


    @Multipart
    @POST("api/test")
    Call<User> UploadUser(
            @Part("id") int id,
            @Part("name") String name
    );


    @GET("api/file")
    Call<String> asd();
}
