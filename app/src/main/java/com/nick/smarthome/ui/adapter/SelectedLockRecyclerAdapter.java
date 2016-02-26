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
public class SelectedLockRecyclerAdapter extends AutoRVAdapter{


    private Context context;

    /**
     * @param context
     * @param list
     */
    public SelectedLockRecyclerAdapter(Context context, List<MyLockEntity> list) {
        super(context, list);
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_selected_lock_recyclerview;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final MyLockEntity item = (MyLockEntity) list.get(position);
        holder.getTextView(R.id.lock_code).setText(item.getLockCode());
        holder.getTextView(R.id.sort).setText(String.valueOf(position+1));

        holder.getImageView(R.id.iv_delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeItem(item);
            }
        });

        holder.getConvertView().findViewById(R.id.item_no_business_list_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, PayActivity.class);
//                context.startActivity(intent);
            }
        });
    }

    public void removeItem(MyLockEntity myLockEntity) {
        int position = list.indexOf(myLockEntity);
        list.remove(position);
        notifyItemRemoved(position);//Attention!
    }
}
