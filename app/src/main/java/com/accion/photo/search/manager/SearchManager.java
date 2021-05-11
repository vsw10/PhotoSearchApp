package com.accion.photo.search.manager;

import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.accion.photo.R;
import com.accion.photo.SearchActivity;
import com.accion.photo.Utils.UtilityFunctions;
import com.accion.photo.search.Constants;
import com.accion.photo.search.SearchApiInterface;
import com.accion.photo.search.model.Pictures;
import com.accion.photo.search.network.NetworkListener;
import com.accion.photo.search.network.NetworkListernerVolley;
import com.accion.photo.search.network.NetworkRequestHelper;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author Vinod Singh
 * This class manages the SearchActivity API Calls and
 * parse the respective result data
 */
public class SearchManager {

    private static SearchManager mSearchManager;
    private static SearchActivity mContext;

    public static SearchManager getInstance(){
        if(mSearchManager == null){
            mSearchManager = new SearchManager();
        }
        return mSearchManager;
    }

    /**
     * Function to get the Photos ArrayList
     * @param picturesList
     * @param mContext
     * @param text
     */
    public void getPhotosListUsingRetroFit(ArrayList<Pictures> picturesList,
                              SearchActivity mContext,
                              String text,
                              NetworkListener networkListener){
        Log.d(Constants.TAG,"SearchManager getPhotosList");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(customConverterFactory)
                .build();
        Call<JSONObject> call = retrofit.create(SearchApiInterface.class)
                .getPhotosList(Constants.PHOTO_SEARCH_API_KEY,
                        text);

        NetworkRequestHelper.makeNetworkRequest(mContext,
                call,
                new NetworkListener(){
                    @Override
                    public void onFailure(String error) {
                        Log.e(Constants.TAG,
                                "ONFailure");
                        networkListener.onFailure(error);
                    }

                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Log.d(Constants.TAG,
                                "ONSuccess");
                        networkListener.onSuccess(jsonObject);
                    }
                });

    }

    /**
     *
     * @param picturesList
     * @param mContext
     * @param text
     * @param networkListener
     */
    public void getPhotosListUsingVolley(List<Pictures> picturesList,
                                         SearchActivity mContext,
                                         String text,
                                         NetworkListernerVolley networkListener){

        String urlString = UtilityFunctions.getInstance(mContext)
                .getUrl(text);
        Log.d(Constants.TAG,"URL "+ urlString);
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Constants.TAG,
                                "Onresponse" + response);
                        networkListener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(Constants.TAG,
                        "OnErrorResponse " + error.getMessage());
                networkListener.onFailure(error.getMessage());
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

        /*JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(Constants.TAG,
                                "Onresponse" + response);
                        networkListener.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Constants.TAG,
                                "OnErrorResponse " + error.getMessage());
                        networkListener.onFailure(error.getMessage());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);*/
    }

    private Converter.Factory customConverterFactory = new Converter.Factory() {
        @Nullable
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            return new JSONConverter();
        }

        @Nullable
        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            return new BodyConverter();
        }
    };

    /**
     * Class used for JSONObject Converter for response
     */
    private static final class JSONConverter implements Converter<ResponseBody,JSONObject>{

        @Nullable
        @Override
        public JSONObject convert(ResponseBody value) throws IOException {
            try {
                return new JSONObject(value.string());
            } catch (JSONException e){
                throw new IOException("Failed to parse JSON",e);
            }
        }
    }

    /**
     * Class used for Body converter for request body
     */
    private static final class BodyConverter implements Converter<JSONObject,RequestBody> {

        @Nullable
        @Override
        public RequestBody convert(JSONObject value) throws IOException {
            return RequestBody.create(MediaType.parse("application/json"),value.toString());
        }
    }



}
