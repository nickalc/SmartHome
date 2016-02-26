package com.nick.smarthome.ui.adapter;

import android.content.Context;
import android.view.View;

import com.github.obsessive.library.utils.CommonUtils;
import com.nick.smarthome.R;
import com.nick.smarthome.bean.MyHouseListEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/14 23:34.
 * Description:
 */
public class MyHouseRecyclerAdapter extends AutoRVAdapter{


    private Context context;

    /**
     * @param context
     * @param list
     */
    public MyHouseRecyclerAdapter(Context context, List<MyHouseListEntity> list) {
        super(context, list);
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_my_house_recyclerview;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final MyHouseListEntity item=(MyHouseListEntity) list.get(position);
        holder.getTextView(R.id.tv_house_name).setText(item.getHouseTitle());
        holder.getTextView(R.id.tv_house_addr).setText(item.getHouseAdress());
        holder.getTextView(R.id.tv_house_price).setText(item.getSalePrice() + "元");

        if (!CommonUtils.isEmpty(item.getPhotoUrl())) {
            ImageLoader.getInstance().displayImage(item.getPhotoUrl(), holder.getImageView(R.id.houseImageView));
        }

        if (item.isSelected()) {
            holder.getCheckBox(R.id.ckeckbox_house).setChecked(true);
        }else {
            holder.getCheckBox(R.id.ckeckbox_house).setChecked(false);
        }

        holder.getCheckBox(R.id.ckeckbox_house).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isSelected()) {
                    holder.getCheckBox(R.id.ckeckbox_house).setChecked(false);
                    item.setIsSelected(false);
                }else{
                    holder.getCheckBox(R.id.ckeckbox_house).setChecked(true);
                    item.setIsSelected(true);
                }
            }
        });

//        holder.getConvertView().findViewById(R.id.item_house_list_layout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(context, PayActivity.class);
////                context.startActivity(intent);
//            }
//        });
    }

    public void removeItem(MyHouseListEntity entity) {
        int position = list.indexOf(entity);
        list.remove(position);
        notifyItemRemoved(position);
    }

}
