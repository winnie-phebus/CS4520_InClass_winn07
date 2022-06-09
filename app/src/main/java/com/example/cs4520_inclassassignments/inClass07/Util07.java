package com.example.cs4520_inclassassignments.inClass07;

/**
 * @author Winnie Phebus
 * Assignment 07
 */
public class Util07 {
    public static final String BASE_URL = "http://dev.sakibnm.space:3000/api/";

    // for convenience, as most objects in this class
    public static RetrofitAPI getAPI() {
        return RetrofitClient.getInstance(BASE_URL).create(RetrofitAPI.class);
    }

/*    // separated, prevents from wasteful object generation
    public static RetrofitAPI getInstanceApi(Retrofit retro) {
        return retro.create(RetrofitAPI.class);
    }*/

    public static IC07Note defaultNote() {
        return new IC07Note("",
                "No Notes have been found in the Database at this time. \nAdd a note to begin.",
                "default",
                "");
    }
}
