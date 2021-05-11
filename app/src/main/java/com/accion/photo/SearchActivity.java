package com.accion.photo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.accion.photo.Utils.UtilityFunctions;
import com.accion.photo.databinding.ActivityMainBinding;
import com.accion.photo.search.Constants;
import com.accion.photo.search.SearchJsonParser;
import com.accion.photo.search.SearchViewModel;
import com.accion.photo.search.adapter.PictureAdapter;
import com.accion.photo.search.manager.SearchManager;
import com.accion.photo.search.model.Pictures;
import com.accion.photo.search.network.NetworkListener;
import com.accion.photo.search.network.NetworkListernerVolley;
import com.bumptech.glide.Glide;

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
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author Vinod Singh
 * Class used to search using the flicke.photos.search API calls
 */
public class SearchActivity extends AppCompatActivity {

    SearchViewModel mSearchViewModel;
    SearchActivity mContext;

    EditText searchText;
    TextView textView;
    GridLayoutManager mGridLayoutManager;

    ProgressBar mProgressBar;
    RecyclerView mRecyclerView;

    PictureAdapter pictureAdapter;
    List<Pictures> list;

    private int photoSize = 10;
    int heightCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.TAG," SearchActivity OnCreate ");

        // DataBindings
        setUpBindings();

        // initializes UI
        initUI();
        // Adding Listeners
        setOnClickListener();
    }

    // Function to set view model data bindings
    private void setUpBindings(){
        ActivityMainBinding activityMainBinding =
                DataBindingUtil.setContentView(this,
                        R.layout.activity_main);
        activityMainBinding.setLifecycleOwner(this);
        mSearchViewModel =
                ViewModelProviders.of(this).get(SearchViewModel.class);
        activityMainBinding.setSearchViewModel(mSearchViewModel);

        mSearchViewModel.getSearchValue().observe(this,
                value -> {
                    searchText.setSelection(value.length());
                    Log.d(Constants.TAG," Value is "+ value);
                });

    }
    /**
     * Initialize the UI elements
     */
    public void initUI(){
        mContext = this;
        searchText = findViewById(R.id.editText);
        mProgressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);
        mRecyclerView = findViewById(R.id.recyclerView);
        textView.setText(R.string.text_view_search);
        textView.setVisibility(View.VISIBLE);
        list = new ArrayList<>();

        mGridLayoutManager = new GridLayoutManager(mContext,
                Constants.SPAN_COUNT);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        heightCount = getResources().getDisplayMetrics().heightPixels / photoSize;
        // Recycled View Pool
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0,
                Constants.SPAN_COUNT * heightCount * 2);
        mRecyclerView.setItemViewCacheSize(0);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                //super.getItemOffsets(outRect, view, parent, state);
                outRect.set(1,1,1,1);
            }
        });

       /* mRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
                PictureAdapter.PictureViewHolder pictureViewHolder =
                        (PictureAdapter.PictureViewHolder) holder;

            }
        });*/
    }

    /**
     * Function to set Click Listener
     */
    public void setOnClickListener(){
        Log.d(Constants.TAG,"setOnClickListener");
        searchText.setOnEditorActionListener((v,actionId, event) ->{

            Log.d(Constants.TAG,"ACTION ID" + actionId);
            // Press Enter/Search from Keyboard's Input
            String searchTextString = searchText.getText().toString();
            if(!(searchTextString.isEmpty()) && (actionId == EditorInfo.IME_ACTION_SEARCH ||
            actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED)){
                hideKeyBoard();
                performSearchAction();
                return true;
            }
            if(searchTextString.isEmpty()){
                Toast.makeText(mContext,"Enter Text to Search",Toast.LENGTH_LONG).show();
            }

            return false;
        });
    }

    /**
     * Function to Perform the Search Action
     */
    private void performSearchAction(){
        Log.d(Constants.TAG,"performSearchAction");
        String searchValue = searchText.getText().toString();
        Log.d(Constants.TAG,"EDT Enter Text is "+ searchValue);
        mSearchViewModel.getSearchValue().setValue(searchValue);
        String enteredValue = mSearchViewModel.getSearchValue().getValue();
        Log.d(Constants.TAG,"MLD Enter Text is "+ searchValue);

        searchText.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);

        String text = searchText.getText().toString();
        Log.d(Constants.TAG,"Text is "+ text);
        //handlePictures(arrayList,text);
        handlePicturesusingVolley(list,text);
    }

    /**
     * Funtion used to hide the Soft Input Keyboard
     */
    private void hideKeyBoard(){
        InputMethodManager imm =
                (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if(view == null){
            view = new View(mContext);
        }
        imm.hideSoftInputFromWindow(((View) view).getWindowToken(),0);
    }

    /**
     * Function to handle the list of pictures
     * @param arrayList
     * @param text
     */
    private void handlePictures(ArrayList<Pictures> arrayList, String text){

        SearchManager.getInstance().getPhotosListUsingRetroFit(arrayList,
                mContext, text, new NetworkListener() {
                    @Override
                    public void onFailure(String error) {
                        Log.e(Constants.TAG,"SearchActivity onFailure");
                        onFailureOperations(error);
                    }

                    @Override
                    public void onSuccess(JSONObject string) {
                        Log.d(Constants.TAG,"SearchActivity onSuccess");
                        onSuccessOperations();
                    }
                });

    }

    /**
     * Functions to perform UI related operation when OnFailure happens
     */
    public void onSuccessOperations(){
        mProgressBar.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        searchText.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        hideKeyBoard();
        Toast.makeText(mContext,"Retrieve the Pictures SuccesFully",Toast.LENGTH_LONG).show();
    }

    /**
     * Functions to perform UI related operation when OnSuccess happens
     */
    public void onFailureOperations(String error) {
        mProgressBar.setVisibility(View.GONE);
        searchText.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        textView.setText(error);
        hideKeyBoard();
        Toast.makeText(mContext,"Unable to Get the Pictures",Toast.LENGTH_LONG).show();
    }

    private void handlePicturesusingVolley(List<Pictures> arrayList,
                                           String text){
        final List<Pictures> arrayListNew =  arrayList;
        SearchManager.getInstance().getPhotosListUsingVolley(arrayListNew,
                mContext,
                text,
                new NetworkListernerVolley() {

                    @Override
                    public void onSuccess(String response) {
                        Log.d(Constants.TAG,"SearchActivity onSuccess");
                        onSuccessOperations();

                        list = new SearchJsonParser().parseStringResponse(response);
                        pictureAdapter = new PictureAdapter(mContext,list);
                        mRecyclerView.setAdapter(pictureAdapter);

                    }

                    @Override
                    public void onFailure(String error) {
                        Log.e(Constants.TAG,"SearchActivity onFailure");
                        onFailureOperations(error);
                    }

                    @Override
                    public void onSuccess(JSONObject string) {

                    }
                });
    }
}