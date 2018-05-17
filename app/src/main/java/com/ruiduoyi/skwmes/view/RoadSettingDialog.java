package com.ruiduoyi.skwmes.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ruiduoyi.skwmes.Config;
import com.ruiduoyi.skwmes.R;
import com.ruiduoyi.skwmes.bean.GzBean;
import com.ruiduoyi.skwmes.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chen on 2018/5/9.
 */

public class RoadSettingDialog extends AlertDialog {

    @BindView(R.id.sp_road1_gzms_dialog_roadsetting)
    Spinner spRoad1Gzms;
    @BindView(R.id.sp_road2_gzms_dialog_roadsetting)
    Spinner spRoad2Gzms;
    @BindView(R.id.tv_exit_dialog_roadsetting)
    TextView tvExit;
    @BindView(R.id.tv_ok_dialog_roadsetting)
    TextView tvOk;
    @BindView(R.id.sp_road1_gz_dialog_roadsetting)
    Spinner spGz1;
    @BindView(R.id.sp_road2_gz_dialog_roadsetting)
    Spinner spGz2;
    private Context context;
    private View mRootView;
    private String road1GzmsStr = "";
    private String road2GzmsStr = "";
    private String road1GzxxStr = "";
    private String road2GzxxStr = "";
    private RoadSettingListener roadSettingListener;
    private ArrayAdapter<String> gzAdapter;
    private List<GzBean.UcDataBean> gzData;
    private GzBean.UcDataBean gzBean1 = null;
    private GzBean.UcDataBean gzBean2 = null;
    private PreferencesUtil preferencesUtil;

    public void setRoadSettingListener(RoadSettingListener roadSettingListener) {
        this.roadSettingListener = roadSettingListener;
    }

    public RoadSettingDialog(@NonNull Context context) {
        this(context, 0);
    }

    protected RoadSettingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        preferencesUtil = new PreferencesUtil(context);
        if (context instanceof Activity) {
            setOwnerActivity((Activity) context);
        }
        mRootView = LayoutInflater.from(context).inflate(R.layout.dialog_roadsetting, null, false);
        setCancelable(false);
        setView(mRootView);
        ButterKnife.bind(this, mRootView);
        //工作模式
        final List<String> gzmsData = new ArrayList<>();
        gzmsData.add(Config.GZMS_SYSTEM_CONTROL);
        gzmsData.add(Config.GZMS_SGFX);
        gzmsData.add(Config.GZMS_ZTYX);
        ArrayAdapter adapter = new ArrayAdapter(context, R.layout.item_select_dialog, gzmsData);
        spRoad1Gzms.setAdapter(adapter);
        int index3 = 0;
        int index4 = 0;
        String gzms3 = preferencesUtil.getGZMS3();
        String gzms4 = preferencesUtil.getGZMS4();
        for (int i=0; i<gzmsData.size(); i++){
            if (gzms3.equals(gzmsData.get(i))){
                index3 = i;
            }
            if (gzms4.equals(gzmsData.get(i))){
                index4 = i;
            }
        }
        spRoad1Gzms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                road1GzmsStr = gzmsData.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spRoad1Gzms.setSelection(index3,true);
        spRoad2Gzms.setAdapter(adapter);
        spRoad2Gzms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                road2GzmsStr = gzmsData.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spRoad2Gzms.setSelection(index4,true);
        //工站信息
        spGz1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gzBean1 = gzData.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spGz2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gzBean2 = gzData.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick({R.id.tv_exit_dialog_roadsetting, R.id.tv_ok_dialog_roadsetting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_exit_dialog_roadsetting:
                dismiss();
                break;
            case R.id.tv_ok_dialog_roadsetting:
                if (null != roadSettingListener){
                    //当连接不到网络的时候，不保存工站信息
                    preferencesUtil.setSybXtGz(null,null,gzBean1,gzBean2);
                    roadSettingListener.onRoadSetting(road1GzmsStr,road2GzmsStr,gzBean1,gzBean2);
                }
                dismiss();
                break;
        }
    }

    /**
     * 设置工站
     *
     * @param gzData
     */
    public void setGzData(List<GzBean.UcDataBean> gzData) {
        //一轨工站
        this.gzData = gzData;
        Map<String, String> sybXtGz = preferencesUtil.getSybXtGz();
        String gzCode3 = "";
        String gzCode4 = "";
        if (sybXtGz != null) {
            gzCode3 = sybXtGz.get(PreferencesUtil.GZ_Code_3);
            gzCode4 = sybXtGz.get(PreferencesUtil.GZ_Code_4);
        }
        List<String> data = new ArrayList<>();
        sybXtGz.get(PreferencesUtil.GZ_Code_3);
        int index3 = 0;
        int index4 = 0;
        int i = 0;
        for (GzBean.UcDataBean bean: gzData){
            data.add(bean.getV_gzdm()+" "+bean.getV_gzname());
            if (gzCode3.equals(bean.getV_gzdm())){
                index3 = i;
            }
            if (gzCode4.equals(bean.getV_gzdm())){
                index4 = i;
            }
            i++;
        }
        gzAdapter = new ArrayAdapter<String>(context, R.layout.item_select_dialog, data);
        spGz1.setAdapter(gzAdapter);
        spGz1.setSelection(index3,true);
        //gzAdapter.notifyDataSetChanged();
        //二轨工站

        spGz2.setAdapter(gzAdapter);
        spGz2.setSelection(index4,true);
        //gzAdapter.notifyDataSetChanged();
    }

    public interface RoadSettingListener {
        void onRoadSetting(String road1GzmsStr, String road2GzmsStr, GzBean.UcDataBean road1GzxxStr, GzBean.UcDataBean road2GzxxStr);
    }
}
