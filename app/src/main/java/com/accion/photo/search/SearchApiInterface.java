package com.accion.photo.search;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface SearchApiInterface {

    @GET("/api/flickr/photos/search")
    Call<JSONObject> getPhotosList(
            @Header("api_key") String apiKey,
            @Header("text") String text);
}
