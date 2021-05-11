package com.accion.photo.search.network;

import android.content.Context;
import android.util.Log;

import com.accion.photo.R;
import com.accion.photo.Utils.NetworkUtils;
import com.accion.photo.search.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * @author Vinod Singh
 * Class used to make the netwoek API calls
 */
public class NetworkRequestHelper {

    /**
     * Function used to make the Network API Call
     * @param context  -- context object from where the request made
     * @param call Retrofit call object
     * @param networkListener Listener Object to handle the success and failure responses
     */
    public static void makeNetworkRequest(final Context context,
                                          Call<JSONObject> call,
                                          NetworkListener networkListener){
        Call<JSONObject> callObject = call;

        Log.d(Constants.TAG,"NetworkRequestHelper makeNetworkRequest");
        if(!NetworkUtils.isNetworkConnected(context)){
            networkListener.onFailure(context.getString(R.string.internet_offline));
            return;
        }

       /* Callback<String> callback = new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.d(Constants.TAG,"NetworkRequestHelper OnResponse");
                if(response.isSuccessful()){
                    networkListener.onSuccess("");
                } else {
                    // If we are not receiving the Succesfull response,
                    // create JSOn Object for server error response
                        JSONObject jsonObject =
                                new JSONObject(response.errorBody().string());
                    networkListener.onSuccess("jsonObject");

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.e(Constants.TAG,"NetworkRequestHelper onFailure");
                // returns error code for the failure response
                String errorResponse = context.getString(R.string.error_bad_request);
                networkListener.onFailure(errorResponse);
                // ToDo It can be handled for various Server Error messages

            }
        };*/


        // Request Callback for receiving the response and failure
        Callback<JSONObject> callback = new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                Log.e(Constants.TAG,"NetworkRequestHelper onResponse " + response.toString());
                if(response.isSuccessful()){
                    networkListener.onSuccess(response.body());
                } else {
                    // If we are not receiving the Succesfull response,
                    // create JSON Object for server error response
                    try{
                        JSONObject jsonObject =
                                new JSONObject(response.errorBody().string());
                        networkListener.onSuccess(jsonObject);
                    } catch (IOException| JSONException e){
                        Log.e(Constants.TAG,"NetworkRequestHelper JSONException");
                        e.printStackTrace();
                        networkListener.onFailure(
                                response.toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {

                Log.e(Constants.TAG,"NetworkRequestHelper onFailure");
                // returns error code for the failure response
                String errorResponse = context.getString(R.string.error_bad_request);
                networkListener.onFailure(errorResponse);
                // ToDo It can be handled for various Server Error messages
            }
        };

        // make the network request
        callObject.enqueue(callback);
    }
}
