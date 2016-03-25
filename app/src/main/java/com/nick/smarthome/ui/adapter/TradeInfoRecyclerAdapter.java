package com.nick.smarthome.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nick.smarthome.R;
import com.nick.smarthome.bean.TradeListResult;
import com.nick.smarthome.ui.activity.PayActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/14 23:34.
 * Description:
 */
public class TradeInfoRecyclerAdapter extends AutoRVAdapter {

    private Context context;

    public TradeInfoRecyclerAdapter(Context context, List<TradeListResult.DataEntity.ListEntity> list) {
        super(context, list);
        this.context = context;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_order_recyclerview;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final TradeListResult.DataEntity.ListEntity item = (TradeListResult.DataEntity.ListEntity) list.get(position);
        String checkInDate = item.getTimeSegmentList().get(0).getCheckInDate();
        String checkOutDate = item.getTimeSegmentList().get(0).getCheckOutDate();
        String orderStatus = item.getOrderStatus();
        String roomType = item.getRoomType();

//        for (int i = 0; i < item.getTimeSegmentList().size(); i++) {
//            String date = item.getTimeSegmentList().get(i).getCheckOutDate() + item.getTimeSegmentList().get(i).getEndTime();
//            int a = compare_date(date);
//            if (a == 1) {
//                String checkInDate = item.getTimeSegmentList().get(i).getCheckInDate();
//                String checkOutDate = item.getTimeSegmentList().get(i).getCheckOutDate();
//                holder.getTextView(R.id.tv_begin_time).setText(checkInDate.substring(5, checkInDate.length()) + " " + item.getTimeSegmentList().get(i).getStartTime());
//                holder.getTextView(R.id.tv_end_time).setText(checkOutDate.substring(5, checkOutDate.length()) + " " + item.getTimeSegmentList().get(i).getEndTime());
//                break;
//            }
//        }


        holder.getTextView(R.id.tv_house_name).setText(item.getHouseTitle());
        holder.getTextView(R.id.tv_house_address).setText(item.getHouseAdress());
        holder.getTextView(R.id.tv_house_no).setText(item.getRoomNo());
        holder.getTextView(R.id.tv_begin_time).setText(checkInDate.substring(5, checkInDate.length())+" "+item.getTimeSegmentList().get(0).getStartTime());
        holder.getTextView(R.id.tv_end_time).setText(checkOutDate.substring(5, checkOutDate.length())+" "+item.getTimeSegmentList().get(0).getEndTime());
        holder.getTextView(R.id.tv_order_code).setText(item.getOrderCode());
        holder.getTextView(R.id.tv_order_price).setText(item.getTotalPrice() + "元");

        if ("ROOM".equals(roomType)) {
            holder.getTextView(R.id.house_no).setText("房间");
        } else {
            holder.getTextView(R.id.house_no).setText("客厅");
        }

        if ("1".equals(orderStatus)) {
            holder.getTextView(R.id.tv_order_status).setText("待支付");
        } else if ("2".equals(orderStatus)) {
            holder.getTextView(R.id.tv_order_status).setText("订单已完成");
        } else if ("3".equals(orderStatus)) {
            holder.getTextView(R.id.tv_order_status).setText("订单已取消");
        } else if ("4".equals(orderStatus)) {
            holder.getTextView(R.id.tv_order_status).setText("订单关闭");
        } else if ("5".equals(orderStatus)) {
            holder.getTextView(R.id.tv_order_status).setText("支付完成");
        }

        holder.getConvertView().findViewById(R.id.item_order_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("custOrderId", String.valueOf(item.getCustOrderId()));
                bundle.putString("title", "交易详情");
                Intent intent = new Intent(context, PayActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    public static int compare_date(String DATE1) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
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