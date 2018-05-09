package com.ruiduoyi.skwmes.net;

import com.ruiduoyi.skwmes.bean.GzBean;
import com.ruiduoyi.skwmes.bean.StopOrder;
import com.ruiduoyi.skwmes.bean.SystemBean;
import com.ruiduoyi.skwmes.bean.UpdateBean;
import com.ruiduoyi.skwmes.bean.XbBean;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.ruiduoyi.skwmes.Config.BASE_URL;


/**
 * Created by Chen on 2018/4/24.
 */

public class RetrofitManager {
    static {
        init();
    }
    static Retrofit retrofit;
    public static void init(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(MyGsonConverterFactory.create())//自定义数据解析
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//新的配置
                .baseUrl(BASE_URL)
                .build();
    }

    /**
     *获取指令状态(当网络发生错误时，这个函数不会返回任何东西)
     * @return StopOrder
     *
     */
    public static Observable<StopOrder> getStopOrder(){
         return retrofit.create(Api.class).getStopOrder()
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread());
    }
    public static Observable<UpdateBean> getUpdateInfo(){
        return retrofit.create(Api.class).getUpdateInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public static Observable<SystemBean> getSystemName(){
        return retrofit.create(Api.class).getSystemName()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public static Observable<XbBean> getXbBean(String fcServer, String fcDataBase,String fcUid,String fcPwd){
        return retrofit.create(Api.class).getXbBean(fcServer,fcDataBase,fcUid,fcPwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public static Observable<GzBean> getGzBean(String fcServer, String fcDataBase,String fcUid,String fcPwd){
        return retrofit.create(Api.class).getGzBean(fcServer,fcDataBase,fcUid,fcPwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    interface Api{
        @GET("GetStopOrder")
        Observable<StopOrder> getStopOrder();
        @GET("GetUpdateInfo")
        Observable<UpdateBean> getUpdateInfo();
        @GET("GetPrjList")
        Observable<SystemBean> getSystemName();
        @GET("GetXbmList")
        Observable<XbBean> getXbBean(@Query("fcServer") String fcServer,@Query("fcDataBase") String fcDataBase,@Query("fcUid") String fcUid,@Query("fcPwd") String fcPwd);
        @GET("GetOprList")
        Observable<GzBean> getGzBean(@Query("fcServer") String fcServer, @Query("fcDataBase") String fcDataBase, @Query("fcUid") String fcUid, @Query("fcPwd") String fcPwd);
    }
}
