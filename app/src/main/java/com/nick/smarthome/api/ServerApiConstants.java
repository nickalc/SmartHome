package com.nick.smarthome.api;

import android.os.Environment;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/8 00:15.
 * Description:
 */
public class ServerApiConstants {

    /**
     *
     */
    public static final class Urls {

        public static final String URL_HOST = "http://139.196.12.187:8080";
        public static final String API_URI = "/HotelAppServer";

        public static final String URL_API_HOST = URL_HOST + API_URI;
        //登陆
        public static final String LOGIN_IN_URLS = URL_API_HOST + "/customerRegisterServer/customerLand";
        //获取验证码

        public static final String GET_VALID_CODE_URLS = URL_API_HOST + "/customerRegisterServer/getPhoneNumberValidationCode";
        //忘记密码获取验证码

        public static final String GET_FORGOT_VALID_CODE_URLS = URL_API_HOST + "/customerRegisterServer/getPhoneNumberValidationCodeNew";
        //验证手机验证码

        public static final String VALIDATION_PHONE_CODE_URLS = URL_API_HOST + "/customerRegisterServer/customerRegisterValidation";
        //注册

        public static final String SUBMIT_REGISTER_URLS = URL_API_HOST + "/customerRegisterServer/customerRegister";
        //重置/忘记密码

        public static final String SUBMIT_RESET_PSD_URLS = URL_API_HOST + "/customerRegisterServer/updateCustomerPassword";
        //客户图片(头像)上传

        public static final String SUBMIT_CUSTOMER_PIC_URLS = URL_API_HOST + "/customerInfoServer/submitCustomerPic";
        //个人信息查询

        public static final String GET_CUSTOMER_INFO_URLS = URL_API_HOST + "/customerInfoServer/getCustomerInfo";
        //个人信息添加

        public static final String ADD_CUSTOMER_INFO_URLS = URL_API_HOST + "/customerInfoServer/addCustomerInfo";
        //用户身份验证图片上传

        public static final String UPLOAD_CUSTOMER_CARD_PIC_URLS = URL_API_HOST + "/customerRegisterServer/uploadCustomerIdCardImg";
        //多图片上传

        public static final String UPLOAD_MULTI_PIC_URLS = URL_API_HOST + "/lockManagerServer/uploadCustomerLockImg";
        //我的锁列表

        public static final String GET_MY_LOCK_LIST_URLS = URL_API_HOST + "/lockManagerServer/queryForRnLockPageList";
        //添加锁

        public static final String ADD_LOCK_INFO_URLS = URL_API_HOST + "/lockManagerServer/addRnLockInfo";
        //修改锁的信息

        public static final String UPDATE_LOCK_INFO_URLS = URL_API_HOST + "/lockManagerServer/updateRnLockInfo";
        //提交房屋

        public static final String SUBMIT_HOUSE_INFO_URLS = URL_API_HOST + "/lockManagerServer/publishRnHouse";
        //查询我的房源列表

        public static final String GET_MY_HOUSE_LIST_URLS = URL_API_HOST + "/lockManagerServer/queryForRnHousePageList";
        //查询附近的房源列表

        public static final String GET_NEARBY_HOUSE_LIST_URLS = URL_API_HOST + "/houseResourceServer/qryHouseResourceList";
        //查询锁的详细信息

        public static final String GET_LOCK_DETAIL_INFO_URLS = URL_API_HOST + "/lockManagerServer/qryRnLockInfo";
        //查询房源详细信息

        public static final String GET_HOUSE_RESOURCE_DETAIL_INFO_URLS = URL_API_HOST + "/houseResourceServer/getHouseResoureDetail";
        //下单/**/

        public static final String ADD_CUST_ORDER_URLS = URL_API_HOST + "/houseResourceServer/addRnCustOrder";
        //支付信息列表查询

        public static final String GET_PAY_ORDER_INFO_LIST_URLS = URL_API_HOST + "/orderManagerServer/qryPayOrderInfoList";
        //支付宝回调url

        public static final String ALIPAT_NOTIFY_URLS = URL_API_HOST + "/orderManagerServer/aliPayNotifyUrl";
        //我的钥匙列表

        public static final String GET_MY_KEY_LIST_URLS = URL_API_HOST + "/orderManagerServer/queryForRnLockKeyPageList";
        //删除房源

        public static final String DELETE_HOUSE_RESOURCE_URLS = URL_API_HOST + "/lockManagerServer/deleteResourceRoom";
        //删除锁

        public static final String DELETE_LOCK_URLS = URL_API_HOST + "/lockManagerServer/deleteResourceLock";
        //查询我的房源信息

        public static final String GET_MY_HOUSE_INFO_URLS = URL_API_HOST + "/lockManagerServer/queryRnHouseInfo";
        //修改我的房源信息

        public static final String UPDATE_MY_HOUSE_INFO_URLS = URL_API_HOST + "/lockManagerServer/updateRnHouseInfo";
        //取消订单

        public static final String CANCLE_CUST_ORDER_URLS = URL_API_HOST + "/orderManagerServer/cancleCustOrder";
    }

    public static final class Paths {
        public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        public static final String IMAGE_LOADER_CACHE_PATH = "/SmartHome/Images/";
    }
}
