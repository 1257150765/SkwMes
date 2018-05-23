package com.ruiduoyi.skwmes;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruiduoyi.skwmes.bean.GzBean;
import com.ruiduoyi.skwmes.bean.InfoBean;
import com.ruiduoyi.skwmes.bean.SystemBean;
import com.ruiduoyi.skwmes.bean.XbBean;
import com.ruiduoyi.skwmes.contact.MainActivityContact;
import com.ruiduoyi.skwmes.presenter.MainActivityPresenter;
import com.ruiduoyi.skwmes.util.LogWraper;
import com.ruiduoyi.skwmes.util.OnDoubleClickListener;
import com.ruiduoyi.skwmes.util.PreferencesUtil;
import com.ruiduoyi.skwmes.view.ChangeGpioActivity;
import com.ruiduoyi.skwmes.view.ChangePwdDialog;
import com.ruiduoyi.skwmes.view.RoadSettingDialog;
import com.ruiduoyi.skwmes.view.SelectDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ruiduoyi.skwmes.presenter.MainActivityPresenter.GPIO_INDEX_3;
import static com.ruiduoyi.skwmes.presenter.MainActivityPresenter.GPIO_INDEX_4;
import static com.ruiduoyi.skwmes.presenter.MainActivityPresenter.REQUEST_CODE_CHANGSYBXTGZ;

public class MainActivity extends AppCompatActivity implements MainActivityContact.View, DialogInterface.OnDismissListener, SelectDialog.SelectListener, RoadSettingDialog.RoadSettingListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_ROAD_SETTING = 1003;
    private boolean isAutoUpdate = true;
    @BindView(R.id.tv_title_mainavtivity)
    TextView tvTitle;
    @BindView(R.id.tv_date_mainavtivity)
    TextView tvDate;
    @BindView(R.id.tv_errorinfo1_mainactivity)
    TextView tvErrorinfo1;
    @BindView(R.id.tv_errorinfo2_mainactivity)
    TextView tvErrorinfo2;
    @BindView(R.id.btn_changepwd_mainactivity)
    Button btnChangepwd;

    @BindView(R.id.iv_netstatu_mainactivity)
    ImageView ivNetstatu;
    @BindView(R.id.iv_deng1_guidao1_mainactivity)
    ImageView ivDeng1Guidao1;
    @BindView(R.id.iv_deng2_guidao1_mainactivity)
    ImageView ivDeng2Guidao1;
    @BindView(R.id.iv_deng3_guidao1_mainactivity)
    ImageView ivDeng3Guidao1;
    @BindView(R.id.iv_deng4_guidao1_mainactivity)
    ImageView ivDeng4Guidao1;
    @BindView(R.id.iv_deng1_guidao2_mainactivity)
    ImageView ivDeng1Guidao2;
    @BindView(R.id.iv_deng2_guidao2_mainactivity)
    ImageView ivDeng2Guidao2;
    @BindView(R.id.iv_deng3_guidao2_mainactivity)
    ImageView ivDeng3Guidao2;
    @BindView(R.id.iv_deng4_guidao2_mainactivity)
    ImageView ivDeng4Guidao2;
    @BindView(R.id.tv_syb_mainactivity)
    TextView tvSyb;
    @BindView(R.id.tv_xt_mainactivity)
    TextView tvXt;
    @BindView(R.id.tv_road1_setting_mainactivity)
    TextView tvRoad1Setting;
    @BindView(R.id.tv_road1_gz_mainactivity)
    TextView tvRoad1Gz;
    @BindView(R.id.tv_road2_setting_mainactivity)
    TextView tvRoad2Setting;
    @BindView(R.id.tv_road2_gz_mainactivity)
    TextView tvRoad2Gz;

    private AlertDialog dialog;
    private PreferencesUtil preferencesUtil;
    private MainActivityContact.Presenter presenter;
    private boolean isConnect = false;
    private AlertDialog downloadDialog;
    private ProgressDialog downloadProgressDialog;
    private SelectDialog selectDialog;
    private RoadSettingDialog roadSettingDialog;
    private String gzStr1;
    private String gzStr2;
    //private AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        preferencesUtil = new PreferencesUtil(this);
        init();
        presenter = new MainActivityPresenter(this, this);
    }

    /**
     * 初始化
     */
    private void init() {
        initDialog();
        tvTitle.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
                presenter.checkUpdate();
                LogWraper.d(TAG, "onDoubleClick");
                isAutoUpdate = false;
            }
        }));

    }


    /**
     * 初始化提示对话框
     */
    private void initDialog() {
        dialog = new AlertDialog.Builder(this).setTitle("提示").setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(true)
                .create();
        dialog.setOnDismissListener(this);
        downloadProgressDialog = new ProgressDialog(this);
        downloadProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        downloadProgressDialog.setCancelable(false);
        downloadProgressDialog.setCanceledOnTouchOutside(false);
        downloadProgressDialog.setTitle("下载中");
        downloadProgressDialog.setMax(100);
        downloadProgressDialog.setOnDismissListener(this);
        /*View v = LayoutInflater.from(this).inflate(R.layout.dialog_loading,null,false);
        loadingDialog = new AlertDialog.Builder(this)
                .setView(v)
                .create();
        */
    }

    /**
     * 成功加载系统名称
     *
     * @param systmData
     */
    @Override
    public void onLoadSystemSecceed(SystemBean systmData) {
        selectDialog = new SelectDialog(MainActivity.this);
        selectDialog.setOnDismissListener(this);
        selectDialog.setSelectListener(this);
        selectDialog.setSyb(systmData.getUcData());
        selectDialog.show();
    }


    @Override
    public void onShowMsgDialog(String msg) {
        if (!MainActivity.this.isDestroyed()) {
            dialog.setMessage(msg);
            if (!dialog.isShowing()){
                dialog.show();
            }

        }
    }


    /**
     * 系统更新中
     *
     * @param progress
     */
    @Override
    public void onUpdate(int progress) {
        if (progress >= 0 && progress < 100) {
            downloadProgressDialog.setProgress(progress);
            downloadProgressDialog.show();
        }
    }

    /**
     * 成功检查更新
     *
     * @param hasUpdate
     * @param url
     */
    @Override
    public void onCheckUpdateSucceed(boolean hasUpdate, final String url) {

        if (hasUpdate) {
            downloadDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("提示")
                    .setMessage("发现新的版本，点击确定开始更新")
                    .setCancelable(false)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.update(url);
                        }
                    }).create();
        } else {
            //如果是自动检查更新，没有新版本就不提示更新
            if (isAutoUpdate){
                return;
            }
            //重置状态
            isAutoUpdate = true;
            downloadDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("提示")
                    .setMessage("是否更新")
                    .setCancelable(true)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.update(url);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
        }
        if (!MainActivity.this.isDestroyed()) {
            downloadDialog.setOnDismissListener(this);
            downloadDialog.show();
        }
    }

    //网络状态变化
    @Override
    public void onNetInfoChange(String netInfo) {
        if (Config.IS_STOP_0.equals(netInfo)) {
            isConnect = false;
            tvErrorinfo1.setTextColor(Color.RED);
            tvErrorinfo1.setText("网络异常，请检查网络！");
            tvErrorinfo2.setTextColor(Color.RED);
            tvErrorinfo2.setText("网络异常，请检查网络！");
            ivNetstatu.setImageResource(R.mipmap.netstatu_disconnect);
        } else {
            isConnect = true;
            ivNetstatu.setImageResource(R.mipmap.netstatu_connect);
        }
    }

    @Override
    public void onLoad(boolean isLoad) {
       /* if (isLoad){
            loadingDialog.show();
        }else {
            loadingDialog.dismiss();
        }*/
    }

    @Override
    public void onStartSend(String gpioIndex) {
        if (GPIO_INDEX_3.equals(gpioIndex)) {
            ivDeng3Guidao1.setImageResource(R.mipmap.deng_open3);
        } else if (GPIO_INDEX_4.equals(gpioIndex)) {
            ivDeng4Guidao2.setImageResource(R.mipmap.deng_open3);
        }
    }

    @Override
    public void onStopSend(String gpioIndex) {
        if (GPIO_INDEX_3.equals(gpioIndex)) {
            ivDeng3Guidao1.setImageResource(R.mipmap.deng_close);
        } else if (GPIO_INDEX_4.equals(gpioIndex)) {
            ivDeng4Guidao2.setImageResource(R.mipmap.deng_close);
        }
    }


    @OnClick({R.id.btn_changepwd_mainactivity, R.id.btn_roadsetting_mainactivity, R.id.btn_xtqh_mainactivity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_roadsetting_mainactivity:
                //checkPwd2ChangeGpioStatu(GPIO_INDEX_3);
                if (null == preferencesUtil.getSybXtGz()){
                    onShowMsgDialog("请先选择系统");
                }
                checkPwd2RoadSetting();
                break;
            case R.id.btn_changepwd_mainactivity:
                showChangePwdDialog();
                break;
            case R.id.btn_xtqh_mainactivity:
                checkPwd2ChangeSystem();
                break;
        }
    }

    private void checkPwd2RoadSetting() {
        //检查权限
        Intent intent = new Intent(MainActivity.this, ChangeGpioActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ROAD_SETTING);
    }

    /**
     * 显示修改密码对话框
     */
    private void showChangePwdDialog() {
        ChangePwdDialog changePwdDialog = new ChangePwdDialog(MainActivity.this);
        changePwdDialog.setOnDismissListener(this);
        changePwdDialog.show();
    }

    /**
     * 检查密码，然后改变系统名称，线体，工站
     */
    public void checkPwd2ChangeSystem() {
        if (!isConnect) {
            onShowMsgDialog("与服务器已断开连接");
            return;
        }
        Intent intent = new Intent(MainActivity.this, ChangeGpioActivity.class);
        //检查权限
        startActivityForResult(intent, REQUEST_CODE_CHANGSYBXTGZ);
    }

    /**
     * 验证是否有权限启动或暂停,或者修改系统状态
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hideBottomUIMenu();
        if (requestCode == REQUEST_CODE_ROAD_SETTING) {
            if (resultCode == RESULT_OK) {
                roadSettingDialog = new RoadSettingDialog(MainActivity.this);
                presenter.loadGz();
                roadSettingDialog.setRoadSettingListener(this);
                roadSettingDialog.show();
            }
        }
        if (requestCode == REQUEST_CODE_CHANGSYBXTGZ) {
            if (resultCode == RESULT_OK) {
                presenter.loadSystem();
            }
        }
    }
    //显示工作模式
    @Override
    public void onGpioGzmsChange(String gzms1, String gzms2) {
        tvRoad1Setting.setText("工作模式:" + gzms1);
        tvRoad2Setting.setText("工作模式:" + gzms2);

    }
    //选择系统
    @Override
    public void onSelectSystem(SystemBean.UcDataBean sybBean) {
        //线体和工站不在同一个地方选取，但是为了方便才一起加载
        presenter.loadXt(sybBean);
    }

    //选择所有信息回调
    @Override
    public void onSelect(SystemBean.UcDataBean sybBean, XbBean.UcDataBean xtBean) {
        setSybXt(sybBean.getPrj_name(), xtBean.getV_xbdm()+" "+xtBean.getV_xbname());
        //presenter.changeGpioStatu(GPIO_INDEX_3);
        //presenter.changeGpioStatu(GPIO_INDEX_4);
    }

    //显示系统名称和线体名称
    @Override
    public void setSybXt(String sybStr, String xtStr) {
        tvSyb.setText("系统名称:" + sybStr);
        tvXt.setText("生产线体:" + xtStr);
    }

    //显示工站
    @Override
    public void setGz(String gzStr1, String gzStr2) {
        if ("".equals(gzStr1)||"".equals(gzStr2)){
            return;
        }
        this.gzStr1 = gzStr1;
        this.gzStr2 = gzStr2;
        tvRoad1Gz.setTextColor(Color.BLACK);
        tvRoad1Gz.setText(gzStr1);
        tvRoad2Gz.setTextColor(Color.BLACK);
        tvRoad2Gz.setText(gzStr2);
    }
    //成功加载线体
    @Override
    public void onLoadXtSucceed(List<XbBean.UcDataBean> ucData) {
        selectDialog.setXtData(ucData);
    }
    //成功加载工站
    @Override
    public void onLoadGzSucceed(List<GzBean.UcDataBean> gzData) {
        LogWraper.d(TAG,""+gzData);
        roadSettingDialog.setGzData(gzData);
    }
    //成功更新时间
    @Override
    public void onDateUpdate(String date) {
        tvDate.setText(date);
    }

    @Override
    public void onLoadInfoSucceed(InfoBean.UcDataBean info3, InfoBean.UcDataBean info4) {
        if (null == info3){
            tvErrorinfo1.setTextColor(Color.RED);
            tvErrorinfo1.setText("加载服务器指令出错");
        }else {
            int color = getBackColor(info3.getErl_color());
            tvErrorinfo1.setTextColor(color);
            tvErrorinfo1.setText(info3.getErl_allycms());
        }

        if (null == info4){
            tvErrorinfo2.setTextColor(Color.RED);
            tvErrorinfo2.setText("加载服务器指令出错");
        }else {
            int color = getBackColor(info4.getErl_color());
            tvErrorinfo2.setTextColor(color);
            tvErrorinfo2.setText(info4.getErl_allycms());
        }
    }

    @Override
    public void onUpdateSucceed() {
        if (downloadProgressDialog != null) {
            if (downloadProgressDialog.isShowing()) {
                downloadProgressDialog.dismiss();
            }
        }
    }
    private int getBackColor(String erl_color) {
        int color = 0;
        switch (erl_color){
            case "R":
                color = Color.RED;
                break;
            /*    break;
            case "G":
                color = Color.GREEN;
                break;
            case "B":
                color = Color.BLUE;
                break;
            case "Y":
                color = Color.YELLOW;
                break;*/
            default:
                color = Color.BLACK;
                break;
        }
        return color;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //隐藏虚拟按键，并且全屏
    protected void hideBottomUIMenu() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    //当dialog消失的时候，吧底部导航栏隐藏掉
    @Override
    public void onDismiss(DialogInterface dialog) {
        hideBottomUIMenu();
    }

    @Override
    public void onRoadSetting(String road1GzmsStr, String road2GzmsStr, GzBean.UcDataBean road1Bean, GzBean.UcDataBean road2Bean) {
        presenter.changeGzms(road1GzmsStr, road2GzmsStr);
        if (road1Bean == null || road2Bean == null){

        }else {
            presenter.changeGzxx(road1Bean.getV_gzdm(), road2Bean.getV_gzdm());
            setGz(road1Bean.getV_gzname(),road2Bean.getV_gzname());
        }
    }
}
