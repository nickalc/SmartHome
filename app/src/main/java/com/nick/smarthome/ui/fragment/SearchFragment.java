package com.nick.smarthome.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.github.obsessive.library.utils.CommonUtils;
import com.nick.smarthome.R;
import com.nick.smarthome.ui.activity.NearByHouseActivity;
import com.nick.smarthome.utils.TLog;
import com.nick.smarthome.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/9 14:13.
 * Description: 搜索
 */
public class SearchFragment extends Fragment implements
        AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, TextWatcher,
        PoiSearch.OnPoiSearchListener, View.OnClickListener {

    private static final String TAG = SearchFragment.class.getSimpleName();
    @InjectView(R.id.rootview)
    LinearLayout mRootview;

    @InjectView(R.id.keyWord)
    AutoCompleteTextView searchText;

    @InjectView(R.id.city)
    EditText editCity;

    @InjectView(R.id.search_btn)
    ImageView searchBtn;


    private AMap aMap;
    private String keyWord = "";// 要输入的poi搜索关键字
    private ProgressDialog progDialog = null;// 搜索时进度条
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private View rootView;//缓存Fragment view
    private double latitude,longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //
        TLog.log("", "call onCreate() method");
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_search, container, false);
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

    protected void initView(View view) {

        searchText.setFocusable(true);
        searchText.setFocusableInTouchMode(true);
        searchText.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) searchText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(searchText, 0);
            }

        }, 500);

        if (aMap == null) {
            aMap = ((SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map)).getMap();

//            SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
//            aMap = mMapFragment.getMap();
//            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//            transaction.add(R.id.map, mMapFragment).commit();

            setUpMap();
        }

    }

    /**
     * 设置页面监听
     */
    private void setUpMap() {
        searchBtn.setOnClickListener(this);
        searchText.addTextChangedListener(this);// 添加文本输入框监听事件
        aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
        aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        Inputtips inputTips = new Inputtips(getActivity(),
                new Inputtips.InputtipsListener() {

                    @Override
                    public void onGetInputtips(List<Tip> tipList, int rCode) {
                        if (rCode == 0) {// 正确返回
                            List<String> listString = new ArrayList<String>();
                            for (int i = 0; i < tipList.size(); i++) {
                                listString.add(tipList.get(i).getName());
                            }
                            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                                    getActivity().getApplicationContext(),
                                    R.layout.route_inputs, listString);
                            searchText.setAdapter(aAdapter);
                            aAdapter.notifyDataSetChanged();
                        }
                    }
                });
        try {
            inputTips.requestInputtips(newText, editCity.getText().toString());// 第一个参数表示提示关键字，第二个参数默认代表全国，也可以为城市区号

        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public View getInfoWindow(final Marker marker) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.poikeywordsearch_uri, null);
        RelativeLayout rlPoiSearch = (RelativeLayout) view.findViewById(R.id.rl_poi_search);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(marker.getTitle());

        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        // ImageButton button = (ImageButton) view.findViewById(R.id.start_amap_app);
        // 调起高德地图app
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // startAMapNavi(marker);
//            }
//        });
        rlPoiSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putDouble("latitude",latitude);
                bundle.putDouble("longitude",longitude);
                Intent intent = new Intent(getActivity(), NearByHouseActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;
        marker.showInfoWindow();
        return false;
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {

        dissmissProgressDialog();// 隐藏对话框
        if (rCode == 0) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        showToast(getString(R.string.no_result));
                    }
                }
            } else {
                showToast(getString(R.string.no_result));
            }
        } else if (rCode == 27) {
            showToast(getString(R.string.error_network));
        } else if (rCode == 32) {
            showToast(getString(R.string.error_key));
        } else {
            showToast(getString(R.string.error_other) + rCode);
        }
    }


    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        UIHelper.showToast(getActivity(),infomation);

    }

    /**
     * 判断高德地图app是否已经安装
     */
    public boolean getAppIn() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getActivity().getPackageManager().getPackageInfo(
                    "com.autonavi.minimap", 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        // 本手机没有安装高德地图app
        if (packageInfo != null) {
            return true;
        }
        // 本手机成功安装有高德地图app
        else {
            return false;
        }
    }


    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(getActivity());
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + keyWord);
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        showProgressDialog();// 显示进度框
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", editCity.getText().toString());// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(getActivity(), query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    /**
     * 点击搜索按钮
     */
    public void searchButton() {
        keyWord = checkEditText(searchText);
        if ("".equals(keyWord)) {
            showToast("请输入搜索关键字");
            return;
        } else {
            hideIM(searchText);
            doSearchQuery();
        }
    }

    /**
     * 判断edittext是否null
     */
    public static String checkEditText(EditText editText) {
        if (editText != null && editText.getText() != null
                && !(editText.getText().toString().trim().equals(""))) {
            return editText.getText().toString().trim();
        } else {
            return "";
        }
    }

    /**
     * 点击下一页按钮
     */
    public void nextButton() {
        if (query != null && poiSearch != null && poiResult != null) {
            if (poiResult.getPageCount() - 1 > currentPage) {
                currentPage++;
                query.setPageNum(currentPage);// 设置查后一页
                poiSearch.searchPOIAsyn();
            } else {
                showToast(getString(R.string.no_result));
            }
        }
    }

    /**
     * show toast
     *
     * @param msg
     */
    protected void showToast(String msg) {
        if (null != msg && !CommonUtils.isEmpty(msg)) {
            Snackbar.make(getActivity().getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Button点击事件回调方法
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 点击搜索按钮
             */
            case R.id.search_btn:
                searchButton();
                break;

            default:
                break;
        }
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidd) {
        if (hidd) {//隐藏
            searchText.clearFocus();
            Log.i(TAG, "onHiddenChanged"+hidd);
        } else {//显示
            searchText.setFocusable(true);
            searchText.setFocusableInTouchMode(true);
            searchText.requestFocus();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                public void run() {
                    InputMethodManager inputManager = (InputMethodManager) searchText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(searchText, 0);
                }

            }, 500);
            Log.i(TAG, "onHiddenChanged"+hidd);
        }
    }

    /**
     * 隐藏弹出输入键盘
     *
     * @param edt
     */
    public void hideIM(View edt) {
        try {
            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            IBinder windowToken = edt.getWindowToken();
            if (windowToken != null) {
                im.hideSoftInputFromWindow(windowToken, 0);
            }
        } catch (Exception e) {
            Log.e("HideInputMethod", "failed:" + e.getMessage());
        }
    }
}
