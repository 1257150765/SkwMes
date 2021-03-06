package com.ruiduoyi.skwmes.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ruiduoyi.skwmes.R;
import com.ruiduoyi.skwmes.bean.GzBean;
import com.ruiduoyi.skwmes.bean.SystemBean;
import com.ruiduoyi.skwmes.bean.XbBean;
import com.ruiduoyi.skwmes.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Chen on 2018/4/27.
 */

public class SelectDialog extends AlertDialog {
    @BindView(R.id.tv_exit_dialog_select)
    TextView tvExit;
    @BindView(R.id.tv_ok_dialog_select)
    TextView tvOk;
    @BindView(R.id.sp_syb_dialog_select)
    Spinner spSyb;
    private Context context;
    @BindView(R.id.sp_xt_dialog_select)
    Spinner spXt;
    private View mRootView;
    private ArrayAdapter<String> xtAdapter;
    private ArrayAdapter<String> sybAdapter;
    private List<XbBean.UcDataBean> xtData;
    private List<SystemBean.UcDataBean> sybData;
    private XbBean.UcDataBean xtBean = null;
    private SystemBean.UcDataBean sybBean = null;
    private SelectListener selectListener;
    private PreferencesUtil preferencesUtil;
    Map<String, String> sybXtGz;
    public void setSelectListener(SelectListener selectListener) {
        this.selectListener = selectListener;
    }

    public SelectDialog(@NonNull Context context) {
        this(context, 0);
    }

    public SelectDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        preferencesUtil = new PreferencesUtil(context);
        sybXtGz = preferencesUtil.getSybXtGz();
        this.context = context;
        if (context instanceof Activity) {
            setOwnerActivity((Activity) context);
        }
        mRootView = LayoutInflater.from(context).inflate(R.layout.dialog_select, null, false);
        //super(mRootView,500,300);
        setCancelable(false);
        setView(mRootView);
        //getWindow().setDimAmount(0);
        ButterKnife.bind(this, mRootView);
        //如果系统没有保存有系统，线体等信息，说明是第一次进入，不设置只能退出系统
        if (preferencesUtil.getSybXtGz() == null){
            tvExit.setText("退出系统");
        }else {
            tvExit.setText("取消");
        }
        spSyb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                sybBean = sybData.get(position);
                if (null != selectListener ){
                    selectListener.onSelectSystem(sybBean);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spXt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                xtBean = xtData.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @OnClick({R.id.tv_exit_dialog_select, R.id.tv_ok_dialog_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_exit_dialog_select:
                dismiss();
                if ("退出系统".equals(tvExit.getText().toString())){
                    getOwnerActivity().finish();
                }
                break;
            case R.id.tv_ok_dialog_select:
                showTip();
                break;
        }
    }

    /**
     * 显示提示信息
     */
    private void showTip() {
        if (null == sybBean) {
            Snackbar.make(mRootView, "请选择系统", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (null == xtBean) {
            Snackbar.make(mRootView, "请选择线体", Snackbar.LENGTH_SHORT).show();
            return;
        }

        //数据验证正确，回调
        if (selectListener != null) {
            selectListener.onSelect(sybBean,xtBean);
        }
        //数据验证正确，保存系统名称，线体
        preferencesUtil.setSybXtGz(sybBean,xtBean,null,null);
        dismiss();
    }

    /**
     * 设置线体
     *
     * @param xtData
     */
    public void setXtData(List<XbBean.UcDataBean> xtData) {
        this.xtData = xtData;
        List<String> data = new ArrayList<>();
        int index = 0;
        int i=0;
        String xtCode = "";
        if (null != sybXtGz) {
            xtCode = sybXtGz.get(PreferencesUtil.XT_CODE);
        }
        for (XbBean.UcDataBean bean: xtData){
            data.add(bean.getV_xbdm()+" "+bean.getV_xbname());
            if (bean.getV_xbdm().equals(xtCode)){
                index = i;
            }
            i++;
        }
        xtAdapter = new ArrayAdapter<String>(context, R.layout.item_select_dialog, data);
        spXt.setAdapter(xtAdapter);
        spXt.setSelection(index,true);
        //xtAdapter.notifyDataSetChanged();
    }
    /**
     * 设置系统名称下拉框
     */
    public void setSyb(List<SystemBean.UcDataBean> sybData) {
        this.sybData = sybData;
        List<String> data = new ArrayList<>();
        int index = 0;
        int i=0;
        String systemName = "";
        if (null != sybXtGz) {
            systemName = sybXtGz.get(PreferencesUtil.SYB_NAME);
        }
        for (SystemBean.UcDataBean bean: sybData){
            data.add(bean.getPrj_name());
            if (bean.getPrj_name().equals(systemName)){
                index = i;
            }
            i++;
        }
        sybAdapter = new ArrayAdapter<String>(context, R.layout.item_select_dialog, data);
        spSyb.setAdapter(sybAdapter);
        //sybAdapter.notifyDataSetChanged();
        spSyb.setSelection(index,true);
    }

    public interface SelectListener {
        void onSelectSystem(SystemBean.UcDataBean sybStr);
        void onSelect(SystemBean.UcDataBean sybStr, XbBean.UcDataBean xtStr);
    }
}
