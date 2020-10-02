package com.example.retrofit.service;

import com.example.retrofit.model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface IUploadService {

    @Multipart
    @POST("api/file")
    Call<ResponseBody> Upload(
            @Part MultipartBody.Part photo,
            @Part("description") RequestBody description
            );


    @GET("api/file")
    Call<String> asd();

}
