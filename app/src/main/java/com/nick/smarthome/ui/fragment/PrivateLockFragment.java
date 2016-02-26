package com.nick.smarthome.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nick.smarthome.R;
import com.nick.smarthome.utils.TLog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class PrivateLockFragment extends Fragment implements
        View.OnClickListener {

    private static final String ARG_POSITION = "position";

    @InjectView(R.id.price_settting)
    TextView priceSetting;

    TextView mPrice;
    private View rootView;//缓存Fragment view

    private int position;

    public String data = "1";


    public static PrivateLockFragment newInstance(int position) {
        PrivateLockFragment fragment = new PrivateLockFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        //
        TLog.log("", "call onCreate() method");
        position = getArguments().getInt(ARG_POSITION);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView==null){
            rootView = inflater.inflate(R.layout.fragment_private_lock, container, false);
            ButterKnife.inject(this, rootView);
            initView(rootView);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }


    public void initView(View view) {
            data = "获取fragement数据";

    }

    public String getData(){

      return data;

    }




    @Override
    @OnClick({R.id.price_settting})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.price_settting:
//                MyEvent event2 = new MyEvent("call main activity method");
//
//                EventBus.getDefault().post(event2);//发布消息

              //  EventBus.getDefault().post(new EventCenter(0));

                break;
            default:
                break;
        }
    }

}