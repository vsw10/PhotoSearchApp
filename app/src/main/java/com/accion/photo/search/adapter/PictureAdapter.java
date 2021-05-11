package com.accion.photo.search.adapter;

import android.graphics.Bitmap;
import android.graphics.Picture;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accion.photo.GlideApp;
import com.accion.photo.R;
import com.accion.photo.SearchActivity;
import com.accion.photo.Utils.UtilityFunctions;
import com.accion.photo.search.Constants;
import com.accion.photo.search.model.Pictures;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to create adapter to disaplay Images
 */
public class PictureAdapter extends
        RecyclerView.Adapter<PictureAdapter.PictureViewHolder> {

    private final LayoutInflater inflater;
    private List<Pictures> picturesList = new ArrayList<>();
    SearchActivity mContext;

    public PictureAdapter(SearchActivity searchActivity,
                   List<Pictures> picturesList){
        mContext = searchActivity;
        this.picturesList = picturesList;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public PictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                int viewType) {

        Log.d(Constants.TAG,"onCreateViewHolder");
        View view =
                inflater.inflate(R.layout.layout_image,
                        parent,
                        false);
        ViewGroup.LayoutParams layoutParams =
                view.getLayoutParams();
        layoutParams.width = 300;
        layoutParams.height = 300;
        return new PictureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureViewHolder holder, int position) {
        Log.d(Constants.TAG,"onBindViewHolder");
        final Pictures pictures =
                picturesList.get(position);
        Log.d(Constants.TAG,"OnBindViewHolder URL "+
                pictures.getPartialUrl() + " Title " +
                pictures.getTitle());

        String serverID = pictures.getServer();
        String id =  pictures.getId();
        String secretdID = pictures.getSecret();
        String suffix = "w";
        String imageUrl = UtilityFunctions.getInstance(mContext)
                .getUrl(serverID,id,secretdID);
        Log.d(Constants.TAG,"Image URl "+ imageUrl);
        pictures.setPartialUrl(imageUrl);

       /* Glide.with(mContext)
                .load(pictures)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imageView);*/
        GlideApp.with(mContext)
                .load(pictures.getPartialUrl())
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return picturesList.size();
    }

    public static final class PictureViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        PictureViewHolder(View view){
            super(view);
            imageView = (ImageView) view;
        }
    }
}
