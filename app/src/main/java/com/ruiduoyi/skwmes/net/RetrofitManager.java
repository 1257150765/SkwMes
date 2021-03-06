package com.ruiduoyi.skwmes.net;

import com.ruiduoyi.skwmes.bean.DateBean;
import com.ruiduoyi.skwmes.bean.GzBean;
import com.ruiduoyi.skwmes.bean.InfoBean;
import com.ruiduoyi.skwmes.bean.SystemBean;
import com.ruiduoyi.skwmes.bean.UpdateBean;
import com.ruiduoyi.skwmes.bean.XbBean;
import com.ruiduoyi.skwmes.util.LogWraper;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

        Interceptor logInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                //在这里获取到request后就可以做任何事情了
                LogWraper.d("Net",request.toString());
                Response response = chain.proceed(request);
                return response;
            }
        };
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(MyGsonConverterFactory.create())//自定义数据解析
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
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
    public static Observable<DateBean> getDate(){
        return retrofit.create(Api.class).getDate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public static Observable<InfoBean> getInfoBean(String fcServer, String fcDataBase,String fcUid,String fcPwd,String fcXbdm,String fcGzdm){
        return retrofit.create(Api.class).getInfoBean(fcServer,fcDataBase,fcUid,fcPwd,fcXbdm,fcGzdm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    interface Api{
        @GET("GetSrvVersion")
        Observable<UpdateBean> getUpdateInfo();
        @GET("GetPrjList")
        Observable<SystemBean> getSystemName();
        @GET("GetXbmList")
        Observable<XbBean> getXbBean(@Query("fcServer") String fcServer,@Query("fcDataBase") String fcDataBase,@Query("fcUid") String fcUid,@Query("fcPwd") String fcPwd);
        @GET("GetOprList")
        Observable<GzBean> getGzBean(@Query("fcServer") String fcServer, @Query("fcDataBase") String fcDataBase, @Query("fcUid") String fcUid, @Query("fcPwd") String fcPwd);
        @GET("GetErlList")
        Observable<InfoBean> getInfoBean(@Query("fcServer") String fcServer, @Query("fcDataBase") String fcDataBase, @Query("fcUid") String fcUid, @Query("fcPwd") String fcPwd, @Query("fcXbdm") String fcXbdm, @Query("fcGzdm") String fcGzdm);
        @GET("GetSrvDateTime")
        Observable<DateBean> getDate();
    }
}
