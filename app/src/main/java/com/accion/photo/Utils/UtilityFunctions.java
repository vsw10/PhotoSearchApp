package com.accion.photo.Utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.accion.photo.search.Constants;
import com.accion.photo.search.model.Pictures;

/**
 * Class used to create various utilities for the various functionalties
 */
public class UtilityFunctions {

    public static UtilityFunctions mUtilityFunctions;

    public static Context mContext;

    private UtilityFunctions(){

    }
    public static UtilityFunctions getInstance(Context context){
        if(mUtilityFunctions == null){
            mContext = context;
            mUtilityFunctions = new UtilityFunctions();
        }
        return mUtilityFunctions;
    }

    /**
     * Function to hide the Keyboard
     */
    public void hideKeyBoardInput(){
        InputMethodManager imm =
                (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = new View(mContext);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    /**
     * Function to get the URL
     * @param text
     * @return
     */
    public String getUrl(String text){
        return String.format(Constants.URL,text);
    }

    /**
     * Function to format the image URL
     * @param serverID
     * @param iD
     * @param secret
     * @param suffix
     * @return
     */
    public String getUrl(String serverID,String iD,String secret){
        return String.format(Constants.imageUrl,serverID,iD,secret);

    }
}
