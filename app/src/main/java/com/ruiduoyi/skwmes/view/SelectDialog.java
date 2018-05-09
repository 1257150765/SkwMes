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
import com.ruiduoyi.skwmes.util.PreferencesUtil;

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
    @BindView(R.id.sp_gz_dialog_select)
    Spinner spGz;

    private View mRootView;
    private ArrayAdapter<String> xtAdapter;
    private ArrayAdapter<String> sybAdapter;
    private ArrayAdapter<String> gzAdapter;
    private List<String> xtData;
    private List<String> sybData;
    private List<String> gzData;
    private String gzStr = "";
    private String xtStr = "";
    private String sybStr = "";
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
                sybStr = sybData.get(position);
                if (null != selectListener ){
                    selectListener.onSelectSyb(sybStr);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spXt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                xtStr = xtData.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spGz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gzStr = gzData.get(position);
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
    public void setXtData(List<String> xtData) {
        this.xtData = xtData;
        xtAdapter = new ArrayAdapter<String>(context, R.layout.item_select_dialog, xtData);
        spXt.setAdapter(xtAdapter);
        spXt.setSelection(0);
        xtAdapter.notifyDataSetChanged();
    }

    /**
     * 设置工站
     *
     * @param gzData
     */
    public void setGzData(List<String> gzData) {
        this.gzData = gzData;
        gzAdapter = new ArrayAdapter<String>(context, R.layout.item_select_dialog, gzData);
        spGz.setAdapter(gzAdapter);
        spGz.setSelection(0);
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
        if ("".equals(sybStr) || sybStr.equals("点我选择事业部")) {
            Snackbar.make(mRootView, "请选择事业部", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(xtStr) || xtStr.equals("点我选择线体")) {
            Snackbar.make(mRootView, "请选择线体", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(gzStr) || gzStr.equals("点我选择工站")) {
            Snackbar.make(mRootView, "请选择工站", Snackbar.LENGTH_SHORT).show();
            return;
        }
        //数据验证正确，回调
        if (selectListener != null) {
            selectListener.onSelect(sybStr,xtStr, gzStr);
        }
        preferencesUtil.setSybXtGz(sybStr,xtStr,gzStr);
        dismiss();
    }

    public void setSyb(List<String> sybData) {
        this.sybData = sybData;
        sybAdapter = new ArrayAdapter<String>(context, R.layout.item_select_dialog, sybData);
        spSyb.setAdapter(sybAdapter);
        spSyb.setSelection(0);
        sybAdapter.notifyDataSetChanged();
    }

    public interface SelectListener {
        void onSelectSyb(String sybStr);
        void onSelect(String sybStr, String xtStr, String gzStr);
    }
}
