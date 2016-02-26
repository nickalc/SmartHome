package com.nick.smarthome.ui.adapter;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.view.View;

import com.android.okhttp.OkHttpUtils;
import com.google.gson.Gson;
import com.nick.smarthome.R;
import com.nick.smarthome.api.ServerApiConstants;
import com.nick.smarthome.bean.CommResult;
import com.nick.smarthome.bean.MyLockEntity;
import com.nick.smarthome.callback.CommonCallback;
import com.nick.smarthome.common.Constants;
import com.nick.smarthome.ui.activity.LockInfoActivity;
import com.nick.smarthome.utils.DialogHelp;
import com.nick.smarthome.utils.UIHelper;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/15 23:34.
 * Description:
 */
public class MyLockRecyclerAdapter extends AutoRVAdapter {


    private Context context;
    Vibrator vibrator;

    /**
     * @param context
     * @param list
     */
    public MyLockRecyclerAdapter(Context context, List<MyLockEntity> list) {
        super(context, list);
        this.context = context;
        vibrator = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_my_lock_recyclerview;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        MyLockEntity item = (MyLockEntity) list.get(position);
        holder.getTextView(R.id.tv_house_info).setText(item.getLockCode());
        if ("ROOM".equals(item.getLockType())) {
            holder.getTextView(R.id.room_type).setText("房间");
        } else {
            holder.getTextView(R.id.room_type).setText("客厅");
        }

        holder.getTextView(R.id.tv_room_no).setText(item.getDoorNo());
        final String lockCode = item.getLockCode();
        final String lockId = String.valueOf(item.getRnLockId());
        final String isBound = String.valueOf(item.getIsBound());

        holder.getImageView(R.id.iv_setting_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LockInfoActivity.class);
                intent.putExtra("lockCode", lockCode);
                intent.putExtra("lockId", lockId);
                intent.putExtra("isBound",isBound);
                context.startActivity(intent);
            }
        });

        holder.getConvertView().findViewById(R.id.item_lock_list_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, PayActivity.class);
//                context.startActivity(intent);
            }
        });

        if ("0".equals(isBound)) {
            holder.getConvertView().findViewById(R.id.item_lock_list_layout).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    vibrator.vibrate(new long[] {30,100},-1);

                    DialogHelp.getConfirmDialog(context, "确定删除锁吗?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteLockInfo(lockId);
                        }
                    }).show();

                    return false;
                }
            });
        }

    }

    private void deleteLockInfo(String lockId) {

        List rnLockIdList = new ArrayList();
        rnLockIdList.add(lockId);

        Gson gson = new Gson();
        String rnLockIdListStr = gson.toJson(rnLockIdList);

        String url = ServerApiConstants.Urls.DELETE_LOCK_URLS;
        Map<String, String> params = new HashMap<String, String>();
        params.put("rnLockIdList", rnLockIdListStr);

        OkHttpUtils.
                post()
                .url(url)
                .params(params)
                .build()
                .execute(new CommonCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        UIHelper.showToast(context, "接口出错");
                    }

                    @Override
                    public void onResponse(CommResult response) {
                        if (response.statuscode.equals("1")) {
                            UIHelper.showToast(context, response.message);
                            context.sendBroadcast(new Intent(Constants.INTENT_ACTION_LOCK_BOUGHT));
                        } else {
                            UIHelper.showToast(context, response.message);
                        }
                    }
                });
    }
}
