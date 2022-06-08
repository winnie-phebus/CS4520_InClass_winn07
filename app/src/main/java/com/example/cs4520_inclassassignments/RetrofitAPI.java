package com.example.cs4520_inclassassignments;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * @author Winnie Phebus
 * Assignment 07
 */
public interface RetrofitAPI {
    @FormUrlEncoded
    @POST("auth/login")
    Call<AccessToken> loginUser(
            @Field("email") String email,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/register")
    Call<AccessToken> registerNewUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password);

    @GET("auth/me")
    Call<IC07Profile> getProfile(
            @Header("x-access-token") String tokenValue);

    @GET("auth/logout")
    Call<AccessToken> logoutUser();

    @GET("note/getall")
    Call<IC07Notes> getAllNotes(
            @Header("x-access-token") String tokenValue);

    @FormUrlEncoded
    @POST("note/post")
    Call<IC07Note> postNewNote(
            @Header("x-access-token") String tokenValue,
            @Field("text") String text);

    @FormUrlEncoded
    @POST("note/delete")
    Call<IC07Note> deleteNote(
            @Header("x-access-token") String tokenValue,
            @Field("id") String noteId);
}
