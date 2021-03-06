package com.nick.smarthome.ui.adapter;

import android.content.Context;
import android.view.View;

import com.nick.smarthome.R;
import com.nick.smarthome.bean.MyKeyListResult;
import com.nick.smarthome.ui.dialog.MyQrodeDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/15 23:34.
 * Description:
 */
public class MyKeyRecyclerAdapter extends AutoRVAdapter {


    private Context context;

    /**
     * @param context
     * @param list
     */
    public MyKeyRecyclerAdapter(Context context, List<MyKeyListResult.DataEntity.ListEntity> list) {
        super(context, list);
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_my_key_recyclerview;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        MyKeyListResult.DataEntity.ListEntity item = (MyKeyListResult.DataEntity.ListEntity) list.get(position);
//        String checkInDate = item.getTimeSegmentList().get(0).getCheckInDate();
//        String checkOutDate = item.getTimeSegmentList().get(0).getCheckOutDate();
        final String lockKey = item.getLockKeyContent();


        for (int i = 0; i < item.getTimeSegmentList().size(); i++) {
            String date = item.getTimeSegmentList().get(i).getCheckOutDate() + " " + item.getTimeSegmentList().get(i).getEndTime();
            int a = compare_date(date);
            if (a == 1) {
                String checkInDate = item.getTimeSegmentList().get(i).getCheckInDate();
                String checkOutDate = item.getTimeSegmentList().get(i).getCheckOutDate();
                holder.getTextView(R.id.tv_begin_time).setText(checkInDate.substring(5, checkInDate.length()) + " " + item.getTimeSegmentList().get(i).getStartTime());
                holder.getTextView(R.id.tv_end_time).setText(checkOutDate.substring(5, checkOutDate.length()) + " " + item.getTimeSegmentList().get(i).getEndTime());
                break;
            }
        }

        holder.getTextView(R.id.tv_house_name).setText(item.getHouseTitle());
        if ("ROOM".equals(item.getRoomType())) {
            holder.getTextView(R.id.tv_house_type).setText("房间");
        }else {
            holder.getTextView(R.id.tv_house_type).setText("客厅");
        }

        holder.getTextView(R.id.tv_house_no).setText(item.getRoomNo());
//        holder.getTextView(R.id.tv_begin_time).setText(checkInDate.substring(5, checkInDate.length()) + " " + item.getTimeSegmentList().get(0).getStartTime());
//        holder.getTextView(R.id.tv_end_time).setText(checkOutDate.substring(5, checkOutDate.length()) + " " + item.getTimeSegmentList().get(0).getEndTime());
        holder.getTextView(R.id.tv_order_code).setText(item.getOrderCode());

        holder.getImageView(R.id.qrImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyQrodeDialog dialog = new MyQrodeDialog(context, lockKey);
                dialog.show();
            }
        });

        holder.getConvertView().findViewById(R.id.item_key_list_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyQrodeDialog dialog = new MyQrodeDialog(context, lockKey);
                dialog.show();
            }
        });
    }

    public static int compare_date(String DATE1) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(df.format(new Date()));
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
}
