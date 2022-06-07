package com.example.cs4520_inclassassignments;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitAPI {
    @FormUrlEncoded
    @POST("/login")
    Call<String> loginUser(
            @Field("email") String email,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("/login")
    Call<String> registerNewUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password);


}
