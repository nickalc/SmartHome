package com.nick.smarthome.ui.adapter;

import android.content.Context;
import android.view.View;

import com.nick.smarthome.R;
import com.nick.smarthome.bean.MyLockEntity;

import java.util.List;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/15 23:34.
 * Description:
 */
public class AddLockRecyclerAdapter extends AutoRVAdapter{


    private Context context;

    /**
     * @param context
     * @param list
     */
    public AddLockRecyclerAdapter(Context context, List<MyLockEntity> list) {
        super(context, list);
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_add_lock_recyclerview;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        MyLockEntity item=(MyLockEntity) list.get(position);
        holder.getTextView(R.id.tv_house_info).setText(item.getLockCode());
      //  holder.getTextView(R.id.room_type).setText(item.getLockType() == "ROOM"?"房间":"客厅");
        if ("ROOM".equals(item.getLockType())) {
            holder.getTextView(R.id.room_type).setText("房间");
        }else{
            holder.getTextView(R.id.room_type).setText("客厅");
        }
        holder.getTextView(R.id.tv_room_no).setText(item.getDoorNo());
        holder.getRadioButton(R.id.lock_radio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.getRadioButton(R.id.lock_radio).setChecked(true);
            }
        });

        holder.getConvertView().findViewById(R.id.item_lock_list_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, PayActivity.class);
//                context.startActivity(intent);
            }
        });
    }

    public Object getItems() {
        return list;
    }
}
