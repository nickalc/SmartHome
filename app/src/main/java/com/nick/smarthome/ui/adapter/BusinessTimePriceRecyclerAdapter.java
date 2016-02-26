package com.nick.smarthome.ui.adapter;

import android.content.Context;
import android.view.View;

import com.github.obsessive.library.utils.CommonUtils;
import com.nick.smarthome.R;

import java.util.List;
import java.util.Map;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/15 23:34.
 * Description:
 */
public class BusinessTimePriceRecyclerAdapter extends AutoRVAdapter{


    private Context context;

    /**
     * @param context
     * @param list
     */
    public BusinessTimePriceRecyclerAdapter(Context context, List<Map<String, Object>> list) {
        super(context, list);
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_business_time_price_recyclerview;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Map<String,Object> item = (Map<String, Object>) list.get(position);
        holder.getTextView(R.id.no_business_date).setText(item.get("date").toString());
        if (position == 6) {
            holder.getImageView(R.id.sort).setImageDrawable(context.getResources().getDrawable(R.drawable.icon_moon));
        }else {
            holder.getImageView(R.id.sort).setImageDrawable(context.getResources().getDrawable(R.drawable.icon_sun));
        }

        if (!CommonUtils.isEmpty(item.get("salePrice").toString())) {
            holder.getEditText(R.id.tv_time_price).setText(item.get("salePrice").toString());
        }

//        holder.getEditText(R.id.tv_time_price).addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                item.put("salePrice",s.toString());
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        holder.getConvertView().findViewById(R.id.item_no_business_list_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, PayActivity.class);
//                context.startActivity(intent);
            }
        });
    }
}
