package com.nick.smarthome.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.okhttp.OkHttpUtils;
import com.github.obsessive.library.eventbus.EventCenter;
import com.github.obsessive.library.netstatus.NetUtils;
import com.nick.smarthome.R;
import com.nick.smarthome.api.ServerApiConstants;
import com.nick.smarthome.bean.CommResult;
import com.nick.smarthome.bean.TradeListResult;
import com.nick.smarthome.callback.CommonCallback;
import com.nick.smarthome.callback.OrderInfoListCallback;
import com.nick.smarthome.ui.adapter.AutoRVAdapter;
import com.nick.smarthome.ui.adapter.ViewHolder;
import com.nick.smarthome.ui.base.BaseSwipeBackActivity;
import com.nick.smarthome.utils.DialogHelp;
import com.nick.smarthome.utils.TLog;
import com.nick.smarthome.utils.UIHelper;
import com.nick.smarthome.utils.alipay.PayResult;
import com.nick.smarthome.utils.alipay.SignUtils;
import com.squareup.okhttp.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/01/04 20:40.
 * Description: 支付页面
 */
public class PayActivity extends BaseSwipeBackActivity implements View.OnClickListener {

    @InjectView(R.id.btn_do_pay)
    TextView payBtn;

    @InjectView(R.id.btn_cancle)
    TextView cancleBtn;

    @InjectView(R.id.tv_house_name)
    TextView tvHouseName;

    @InjectView(R.id.tv_room_type)
    TextView tvRoomType;

    @InjectView(R.id.tv_room_no)
    TextView tvRoomNo;

    @InjectView(R.id.tv_house_address)
    TextView tvHouseAddr;

    @InjectView(R.id.tv_order_code)
    TextView tvOrderCode;

    @InjectView(R.id.order_price)
    TextView orderPrice;

    @InjectView(R.id.tv_order_price)
    TextView tvOrderPrice;

    @InjectView(R.id.ll_pay_btn)
    LinearLayout llPayBtn;

    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    // 商户PID
    public static final String PARTNER = "2088711916150681";
    // 商户收款账号
    public static final String SELLER = "sunkevinzhang@gmail.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALPQbMTHECOP1ktWkxR3YOEcBJLAnZ9VCoUniDclVCKRZlfwiq2R/f/owIDGF1CtpgM07iU/ouWHbxJFrLMJeuifA4aSKeH9J+1ycWyuC5HPGrxZL8OWQxsG1oL83aRk+uVlWSlVRX284iNrM/CisCJcJ8c1A2tmDBI5LcJozZk5AgMBAAECgYA2gw02apK99TEmDczw+2/nTrMkOjiOwR07WMy2yx5CjInWRxoDnKrAFjOl1mfjEYGunzFDIZOkufY2N+EvNcSIlruKossAb591FCgz0KriOB2PxjnLr0gOx4n3bgXY07ii3lD6dBSZSsIOG7M6/idEYUHQGKk9oAOKvEye+SsJ8QJBAOOjUQCJjLUNko4EvjKJjW8y77FBRWBWahi+5ap9yoAXLLvmcjOyVIaY1/+kvbpCZHrUZixZs7J7piaR0in/26MCQQDKN7uLD0HrlvhaOpW7YgZlyKusStQQC75IveV6Z5jUiE6oQ8CZ1Iw6n8SRhvgiYdrWyIMsT8AjScbWQ45zWUVzAkEAxutvHE4WhBxRJgNlBRqIWlSjejy0WEQLQIaIgVyc+SPHNodg4mYCUP/9OGsIVFwyG9fBVR4p1l0QywGAKq1niwJANHHx5OXnHdfAv330OW355Mn/pCagCy1Qg4MkkfNuUqirZb50pDDY0ZeAHpIXpqskW5ITWxmv5jcLJ6TLvCTJ+wJBAI4lwSlcwbqvZVEeHf2NoFltH03hsvGFXnzJ85qFkWsZqD92DtlUaWLtQ4i7J3ChRNR4JZ3gc9+9YRYUGgGp5Og=";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpLg5AIztFirOBayxhMBMkCiCTvTU1xXOnN6eV 4FctKU0qShjKLWQU/crb9vMNQmjJwjLHMF1996etS1fG+wAjJberUvY+OOFgCLCsSGrE5wi5ZRq3 xuW2L3uyhb2EBuUYTU7ws1GydPCtAV7bVkYFHUxaSQMCgfwHwLaAb4ufqwIDAQAB";
    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private String custOrderId, mCustomerId, title;

    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<TradeListResult.DataEntity.ListEntity.TimeSegmentListEntity> timeSegmentList = new ArrayList<TradeListResult.DataEntity.ListEntity.TimeSegmentListEntity>();

    private TimeRecyclerAdapter timeRecyclerAdapter;

    ProgressDialog waitDialog;

    private String houseTitle, houseAddr, housePrice, orderCode, roomNo, roomType;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String memo = payResult.getMemo();
                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("houseTitle", houseTitle);
                        bundle.putString("houseAddr", houseAddr);
                        bundle.putString("housePrice", housePrice);
                        bundle.putString("orderCode", orderCode);
                        bundle.putString("roomNo", roomNo);
                        bundle.putString("roomType", roomType);
                        readyGo(PayFinishActivity.class, bundle);
                        finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.equals(resultStatus, "6001")) {//用户主动取消支付
                            Toast.makeText(PayActivity.this, memo, Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败,或者系统返回的错误
                            Toast.makeText(PayActivity.this, memo, Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(PayActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


//    protected int getActionBarTitle() {
//        return R.string.pay;
//    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

        custOrderId = extras.getString("custOrderId");
        title = extras.getString("title");


    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_pay;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        getSupportActionBar().setTitle(title);

        waitDialog = DialogHelp.getWaitDialog(this, "正在加载数据...");

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        timeRecyclerAdapter = new TimeRecyclerAdapter(this, timeSegmentList);

        mRecyclerView.setAdapter(timeRecyclerAdapter);

        initData();
    }

    private void initData() {

        waitDialog.show();

        SharedPreferences settings = getSharedPreferences("secrecy", Activity.MODE_PRIVATE);
        mCustomerId = settings.getString("customerId", null);

        String url = ServerApiConstants.Urls.GET_PAY_ORDER_INFO_LIST_URLS;

        Map<String, String> params = new HashMap<String, String>();
        params.put("customerId", mCustomerId);
        params.put("pageNum", "1");
        params.put("custOrderId", custOrderId);
        params.put("pageSize", "10");

        if (NetUtils.isNetworkConnected(mContext)) {
            OkHttpUtils
                    .get()
                    .url(url)
                    .params(params)
                    .build()
                    .execute(new OrderInfoListCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            TLog.error(e.toString());
                            UIHelper.showToast(mContext, "接口异常...");
                            waitDialog.hide();
                        }

                        @Override
                        public void onResponse(TradeListResult response) {

                            if (response.getStatuscode().equals("1")) {
                                int totalCnt = response.getData().getTotalCnt();
                                if (totalCnt > 0) {
                                    List<TradeListResult.DataEntity.ListEntity> orderListItems = response.getData().getList();
                                    TradeListResult.DataEntity.ListEntity orderInfo = orderListItems.get(0);

                                    houseTitle = orderInfo.getHouseTitle();
                                    houseAddr = orderInfo.getHouseAdress();
                                    housePrice = String.valueOf(orderInfo.getTotalPrice());
                                    orderCode = orderInfo.getOrderCode();
                                    roomNo = orderInfo.getRoomNo();
                                    roomType = orderInfo.getRoomType();

                                    tvHouseName.setText(houseTitle);
                                    if ("ROOM".equals(roomType)) {
                                        tvRoomType.setText("房间");
                                    } else {
                                        tvRoomType.setText("客厅");
                                    }
                                    tvRoomNo.setText(roomNo);
                                    tvHouseAddr.setText(houseAddr);
                                    tvOrderCode.setText(orderCode);
                                    tvOrderPrice.setText(housePrice + "元");

                                    if (orderInfo.isPayStatus()) {
                                        orderPrice.setText("已支付:");
                                        // llPayBtn.setVisibility(View.GONE);
                                    } else {
                                        orderPrice.setText("需支付:");
                                        //llPayBtn.setVisibility(View.VISIBLE);
                                    }

                                    if ("1".equals(orderInfo.getOrderStatus())) {
                                        llPayBtn.setVisibility(View.VISIBLE);
                                    } else {
                                        llPayBtn.setVisibility(View.GONE);
                                    }

                                    timeSegmentList = (ArrayList<TradeListResult.DataEntity.ListEntity.TimeSegmentListEntity>) orderInfo.getTimeSegmentList();

                                    timeRecyclerAdapter = new TimeRecyclerAdapter(mContext, timeSegmentList);
                                    mRecyclerView.setAdapter(timeRecyclerAdapter);
                                    timeRecyclerAdapter.notifyDataSetChanged();

                                } else {
                                    UIHelper.showToast(mContext, "查询无此订单数据");
                                }
                            } else {
                                UIHelper.showToast(mContext, response.getMessage());
                            }
                            waitDialog.hide();
                        }
                    });
        } else {
            waitDialog.hide();
            UIHelper.showToast(mContext, "网络连接失败,请检查");
        }
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay() {

        // 订单
        String orderInfo = getOrderInfo(houseTitle, houseAddr, housePrice);

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderCode + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + ServerApiConstants.Urls.ALIPAT_NOTIFY_URLS
                + "\"";//"http://notify.msp.hk/notify.htm"

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }


    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }

    /**
     * 取消订单
     */
    private void cancleOrder(){

        waitDialog.setMessage("正在取消订单...");
        waitDialog.show();

        String url = ServerApiConstants.Urls.CANCLE_CUST_ORDER_URLS;

        OkHttpUtils.
                post()
                .url(url)
                .addParams("custOrderId", custOrderId)
                .build()
                .execute(new CommonCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        UIHelper.showToast(mContext, "提交信息出错");
                        waitDialog.hide();
                    }

                    @Override
                    public void onResponse(CommResult response) {
                        waitDialog.hide();
                        if (response.statuscode.equals("1")) {
                            UIHelper.showToast(mContext, response.message);
                            setResult(Activity.RESULT_OK);
                            finish();
                        } else {
                            UIHelper.showToast(mContext, response.message);
                        }
                    }
                });
    }

    @Override
    @OnClick({R.id.btn_cancle, R.id.btn_do_pay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_do_pay:
                pay();
                break;
            case R.id.btn_cancle:
                DialogHelp.getConfirmDialog(this, "是否确定取消订单?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cancleOrder();
                    }
                }).show();
                break;
            default:
                break;
        }
    }


    public class TimeRecyclerAdapter extends AutoRVAdapter {

        private Context context;

        /**
         * @param context
         * @param list
         */
        public TimeRecyclerAdapter(Context context, List<TradeListResult.DataEntity.ListEntity.TimeSegmentListEntity> list) {
            super(context, list);
            this.context = context;
            this.list = list;
        }

        @Override
        public int onCreateViewLayoutID(int viewType) {
            return R.layout.item_order_info_time_recyclerview;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final TradeListResult.DataEntity.ListEntity.TimeSegmentListEntity item = (TradeListResult.DataEntity.ListEntity.TimeSegmentListEntity) list.get(position);
            holder.getTextView(R.id.tv_begin_time).setText(item.getCheckInDate() + " " + item.getStartTime());
            holder.getTextView(R.id.tv_end_time).setText(item.getCheckOutDate() + " " + item.getEndTime());
        }

        public Object getItems() {
            return list;
        }
    }

}
