package com.nick.smarthome.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nick.smarthome.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by nick on 15/12/31.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private ArrayList<String> photoPaths = new ArrayList<String>();
    private LayoutInflater inflater;

    private Context mContext;


    public PhotoAdapter(Context mContext, ArrayList<String> photoPaths) {
        this.photoPaths = photoPaths;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);

    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {

        if (photoPaths.get(position).contains("http")) {
            Picasso.with(mContext)
                    .load(photoPaths.get(position))
                    .placeholder(me.nereo.multi_image_selector.R.drawable.default_error)
                    .into(holder.ivPhoto);
        } else {
            Uri uri = Uri.fromFile(new File(photoPaths.get(position)));
            Picasso.with(mContext)
                    .load(uri)
                    .placeholder(me.nereo.multi_image_selector.R.drawable.default_error)
                    .into(holder.ivPhoto);
        }


        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//        Intent intent = new Intent(mContext, PhotoPagerActivity.class);
//        intent.putExtra(PhotoPagerActivity.EXTRA_CURRENT_ITEM, position);
//        intent.putExtra(PhotoPagerActivity.EXTRA_PHOTOS, photoPaths);
//        intent.putExtra(PhotoPagerActivity.EXTRA_SHOW_DELETE, true);
//        if (mContext instanceof MainActivity) {
//          ((MainActivity) mContext).previewPhoto(intent);
//        }
            }
        });

    }


    @Override
    public int getItemCount() {
        return photoPaths.size();
    }


    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private View vSelected;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            vSelected = itemView.findViewById(R.id.v_selected);
            vSelected.setVisibility(View.GONE);
        }
    }

}
