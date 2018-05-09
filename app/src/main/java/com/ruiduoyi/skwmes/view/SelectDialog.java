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
    @BindView(R.id.sp_road1_gz_dialog_select)
    Spinner spGz1;
    @BindView(R.id.sp_road2_gz_dialog_select)
    Spinner spGz2;

    private View mRootView;
    private ArrayAdapter<String> xtAdapter;
    private ArrayAdapter<String> sybAdapter;
    private ArrayAdapter<String> gzAdapter;
    private List<XbBean.UcDataBean> xtData;
    private List<SystemBean.UcDataBean> sybData;
    private List<GzBean.UcDataBean> gzData;
    private GzBean.UcDataBean gzBean1 = null;
    private GzBean.UcDataBean gzBean2 = null;
    private XbBean.UcDataBean xtBean = null;
    private SystemBean.UcDataBean sybBean = null;
    private SelectListener selectListener;
    private PreferencesUtil preferencesUtil;
    public void setSelectListener(SelectListener selectListener) {
        this.selectListener = selectListener;
    }

    public SelectDialog(@NonNull Context context) {
        this(context, 0);
    }

    public SelectDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        preferencesUtil = new PreferencesUtil(context);
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
                    selectListener.onSelectSyb(sybBean);
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


    /**
     * 设置线体
     *
     * @param xtData
     */
    public void setXtData(List<XbBean.UcDataBean> xtData) {
        this.xtData = xtData;
        List<String> data = new ArrayList<>();
        for (XbBean.UcDataBean bean: xtData){
            data.add(bean.getV_xbdm()+bean.getV_xbname());
        }
        xtAdapter = new ArrayAdapter<String>(context, R.layout.item_select_dialog, data);
        spXt.setAdapter(xtAdapter);
        spXt.setSelection(0);
        xtAdapter.notifyDataSetChanged();
    }

    /**
     * 设置工站
     *
     * @param gzData
     */
    public void setGzData(List<GzBean.UcDataBean> gzData) {
        //一轨工站
        this.gzData = gzData;
        List<String> data = new ArrayList<>();
        for (GzBean.UcDataBean bean: gzData){
            data.add(bean.getV_gzdm()+bean.getV_gzname());
        }
        gzAdapter = new ArrayAdapter<String>(context, R.layout.item_select_dialog, data);
        spGz1.setAdapter(gzAdapter);
        spGz1.setSelection(0);
        gzAdapter.notifyDataSetChanged();
        //二轨工站

        spGz2.setAdapter(gzAdapter);
        spGz2.setSelection(0);
        gzAdapter.notifyDataSetChanged();
    }

    public void show(View view) {
        //showAtLocation(view, Gravity.CENTER,0,0);
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
        if (null == gzBean1) {
            Snackbar.make(mRootView, "请选择一轨工站", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (null == gzBean2) {
            Snackbar.make(mRootView, "请选择二轨工站", Snackbar.LENGTH_SHORT).show();
            return;
        }
        //数据验证正确，回调
        if (selectListener != null) {
            selectListener.onSelect(sybBean,xtBean, gzBean1,gzBean2);
        }
        preferencesUtil.setSybXtGz(sybBean,xtBean,gzBean1,gzBean2);
        dismiss();
    }

    public void setSyb(List<SystemBean.UcDataBean> sybData) {
        this.sybData = sybData;
        List<String> data = new ArrayList<>();
        for (SystemBean.UcDataBean bean: sybData){
            data.add(bean.getPrj_name());
        }
        sybAdapter = new ArrayAdapter<String>(context, R.layout.item_select_dialog, data);
        spSyb.setAdapter(sybAdapter);
        spSyb.setSelection(0);
        sybAdapter.notifyDataSetChanged();
    }

    public interface SelectListener {
        void onSelectSyb(SystemBean.UcDataBean sybStr);
        void onSelect(SystemBean.UcDataBean sybStr, XbBean.UcDataBean xtStr, GzBean.UcDataBean gzStr, GzBean.UcDataBean gzBean2);
    }
}
