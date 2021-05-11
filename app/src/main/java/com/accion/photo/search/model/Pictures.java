package com.accion.photo.search.model;

import android.util.Log;

import com.accion.photo.search.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class used to represent Pictures Model
 */
public class Pictures {

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public String getServer() {
        return server;
    }

    public String getFarm() {
        return farm;
    }

    public String getSecret() {
        return secret;
    }

    public String getPartialUrl() {
        return partialUrl;
    }

    public void setPartialUrl(String partialUrl) {
        this.partialUrl = partialUrl;
    }

    private String id;
    private String owner;
    private String title;
    private String server;
    private String farm;
    private String secret;

    private String partialUrl = "null";
            //"https://live.staticflickr.com/65535/51161926928_0e1f7aa58c_w.jpg";

    public Pictures(JSONObject jsonObject){
        try {
            this.id = jsonObject.getString("id");
            this.owner = jsonObject.getString("owner");
            this.title = jsonObject.optString("title", "");
            this.server = jsonObject.getString("server");
            this.farm = jsonObject.getString("farm");
            this.secret = jsonObject.getString("secret");
        } catch (JSONException e){
            Log.e(Constants.TAG,"JSON Excepton ");
            e.printStackTrace();
        }
    }
}
