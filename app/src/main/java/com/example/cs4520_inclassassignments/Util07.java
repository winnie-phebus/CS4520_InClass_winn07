package com.example.cs4520_inclassassignments;

import retrofit2.Call;
import retrofit2.Retrofit;

public class Util07 {
    public static final String BASE_URL = "http://dev.sakibnm.space:3000/api/auth/";

    // for convenience, as most objects in this class
    public static RetrofitAPI getAPI() {
        return RetrofitClient.getInstance(BASE_URL).create(RetrofitAPI.class);
    }

    // separated, prevents from wasteful object generation
    public static RetrofitAPI getInstanceApi(Retrofit retro){
        return retro.create(RetrofitAPI.class);
    }

/*    public static AccessToken postLogin(String email, String password){
        RetrofitAPI API = getAPI();
        Call<String> loginReq =
    }*/
}
