package com.accion.photo.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * @author Vinod Singh
 * Utilities of NetworkRequestHelperApi
 */
public class NetworkUtils {

    /**
     * Function to check whther device is connected to the internet or not
     * @param context Context from the calling API
     * @return true incase Internet is ON and false if Internet connection is Off
     */
    public static boolean isNetworkConnected(Context context){
        ConnectivityManager connectivityManager =
                (ConnectivityManager)context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        return (connectivityManager.getActiveNetworkInfo() != null) ?
                true : false;
    }
}
