package com.nick.smarthome.ui.adapter;

import android.content.Context;
import android.view.View;

import com.nick.smarthome.R;

import java.util.List;
import java.util.Map;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/15 23:34.
 * Description:
 */
public class NoBusinessDateRecyclerAdapter extends AutoRVAdapter{


    private Context context;

    /**
     * @param context
     * @param list
     */
    public NoBusinessDateRecyclerAdapter(Context context, List<Map<String,Object>> list) {
        super(context, list);
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_no_business_date_recyclerview;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

       final Map<String,Object> item = (Map<String, Object>) list.get(position);
        holder.getTextView(R.id.no_business_date).setText(item.get("noBusinessDate").toString());
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

    public void removeItem(Map<String,Object> map) {
        int position = list.indexOf(map);
        list.remove(position);
        notifyItemRemoved(position);//Attention!
    }
}
