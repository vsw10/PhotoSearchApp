package com.accion.photo.search;

import android.util.Log;

import com.accion.photo.search.model.Pictures;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class is used to perform the JSONObject Parsing of responses obtained from the API Calls
 */
public class SearchJsonParser {

    /**
     * Function to parse the String response
     * @param response
     * @return
     */
    public List<Pictures> parseStringResponse(String response) {
        List<Pictures> picturesList;
        try {
            Log.d(Constants.TAG,"parseStringResponse String Response Length"+ response.length());
            String newString = response.substring(14,response.length()-1);
            Log.d(Constants.TAG,"parseStringResponse SubString \n"+ newString);
            JSONObject jsonObject =
                    new JSONObject(newString);
            JSONArray jsonArray = jsonObject.getJSONObject("photos").getJSONArray("photo");
            Log.d(Constants.TAG,"parseStringResponse jsonArray LENGTH = "+ jsonArray.length());
            picturesList = new ArrayList<>(jsonArray.length());
            Log.d(Constants.TAG,"parseStringResponse Size of Pictures List"+ picturesList.size());
            for (int i = 0; i < jsonArray.length(); i++) {
                Log.d(Constants.TAG,"parseStringResponse  Adding Picture Value"+ i);
                ((ArrayList) picturesList).add(new Pictures(jsonArray.getJSONObject(i)));
            }
            return  picturesList;
        } catch (JSONException e){
            Log.d(Constants.TAG,"JSON Exception ");
            e.printStackTrace();
            picturesList = new ArrayList<>();
            return picturesList;
        }
    }

}
