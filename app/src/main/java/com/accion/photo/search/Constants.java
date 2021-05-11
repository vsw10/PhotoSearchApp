package com.accion.photo.search;

/**
 * Class to be used for defining all constants in PhotoSearchApp
 */
public class Constants {

    // TAG to be used in Logs, it can be your Application Name also
    public static final String TAG = "accion"; // PhotoSearch

    // API Key
    public static final String PHOTO_SEARCH_API_KEY = "3fda0aeb4894bbaa9fde8588e6dfa880";

    // Secret Key
    public static final String PHOTO_SEARCH_SECRET = "6bcf419b8365ac7b";

    // Url
   // public static final String URL = "https://www.flickr.com/search/?text=new";

    public static final String URL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&format=json&api_key=" + PHOTO_SEARCH_API_KEY +
            "&text=%s"  + "&per_page=1000";

    // Typical Usage
    //https://live.staticflickr.com/{server-id}/{id}_{secret}_{size-suffix}.jpg
    public static String imageUrl =
            "https://live.staticflickr.com/%s/%1s_%2s_w.jpg";
    /*public static final String URL =  "https://www.flickr.com/services/api/flickr.photos.search.html/&format=json&api_key=" + PHOTO_SEARCH_API_KEY
            + "&text=yes";*/

    //Safe Search
    public static final String SAFE_SEARCH = "1";
    // Content Type
    public static final String CONTENT_TYPE = "1";

    // Number of Columns
    public static final int SPAN_COUNT = 3;
}
