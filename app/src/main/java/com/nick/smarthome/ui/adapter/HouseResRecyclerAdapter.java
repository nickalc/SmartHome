package com.nick.smarthome.ui.adapter;

import android.content.Context;

import com.github.obsessive.library.utils.CommonUtils;
import com.nick.smarthome.R;
import com.nick.smarthome.bean.NearByHouseEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/14 23:34.
 * Description:
 */
public class HouseResRecyclerAdapter extends AutoRVAdapter{


    private Context context;

    /**
     * @param context
     * @param list
     */
    public HouseResRecyclerAdapter(Context context, List<NearByHouseEntity> list) {
        super(context, list);
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_house_res_list;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final NearByHouseEntity item=(NearByHouseEntity) list.get(position);
        holder.getTextView(R.id.list_item_house_name).setText(item.getHouseTitle());
        holder.getTextView(R.id.list_item_house_distance).setText(trans(item.getHouseDistance()));

        if (!CommonUtils.isEmpty(item.getPhotoUrl())) {
            ImageLoader.getInstance().displayImage(item.getPhotoUrl(), holder.getImageView(R.id.house_res_img));
           // FrecsoUtils.loadImage(item.getPhotoUrl(), holder.getSimpleDraweeView(R.id.house_res_img));
        }

    }

    public void removeItem(NearByHouseEntity entity) {
        int position = list.indexOf(entity);
        list.remove(position);
        notifyItemRemoved(position);
    }

    /**经纬度距离单位转换*/
    private String trans(double distance) {
        double dis = 0.0;
        if (distance >= 1000) {
            dis =  Math.round(distance/100d)/10d;
        }else {
            dis = Math.round(distance/100d)*100d;
            return String.valueOf((int)dis)+"m";
        }
        return Double.toString(dis)+"km";
    }

}
