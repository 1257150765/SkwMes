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

import java.util.ArrayList;
import java.util.List;

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

    private Context context;
    private View mRootView;
    private String road1GzmsStr = "";
    private String road2GzmsStr = "";
    private String road1GzxxStr = "";
    private String road2GzxxStr = "";
    private RoadSettingListener roadSettingListener;


    public void setRoadSettingListener(RoadSettingListener roadSettingListener) {
        this.roadSettingListener = roadSettingListener;
    }

    public RoadSettingDialog(@NonNull Context context) {
        this(context, 0);
    }

    protected RoadSettingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;

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
        spRoad1Gzms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                road1GzmsStr = gzmsData.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        //工站信息
        final List<String> gzxxData = new ArrayList<>();
        gzxxData.add("OP1");
        gzxxData.add("OP2");
        gzxxData.add("OP3");
        gzxxData.add("OP4");
        ArrayAdapter gzxxAdapter = new ArrayAdapter(context, R.layout.item_select_dialog, gzxxData);

    }

    @OnClick({R.id.tv_exit_dialog_roadsetting, R.id.tv_ok_dialog_roadsetting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_exit_dialog_roadsetting:
                dismiss();
                break;
            case R.id.tv_ok_dialog_roadsetting:
                if (null != roadSettingListener){
                    roadSettingListener.onRoadSetting(road1GzmsStr,road2GzmsStr);
                }
                dismiss();
                break;
        }
    }

    public interface RoadSettingListener {
        void onRoadSetting(String road1GzmsStr, String road2GzmsStr);
    }
}
