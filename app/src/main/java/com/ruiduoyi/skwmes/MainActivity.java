package com.ruiduoyi.skwmes;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruiduoyi.skwmes.contact.MainActivityContact;
import com.ruiduoyi.skwmes.presenter.MainActivityPresenter;
import com.ruiduoyi.skwmes.util.LogWraper;
import com.ruiduoyi.skwmes.util.OnDoubleClickListener;
import com.ruiduoyi.skwmes.util.PreferencesUtil;
import com.ruiduoyi.skwmes.view.ChangePwdDialog;
import com.ruiduoyi.skwmes.view.SelectDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ruiduoyi.skwmes.presenter.MainActivityPresenter.GPIO_INDEX;
import static com.ruiduoyi.skwmes.presenter.MainActivityPresenter.GPIO_INDEX_3;
import static com.ruiduoyi.skwmes.presenter.MainActivityPresenter.GPIO_INDEX_4;
import static com.ruiduoyi.skwmes.presenter.MainActivityPresenter.REQUEST_CODE_CHANGEGPIO;
import static com.ruiduoyi.skwmes.presenter.MainActivityPresenter.REQUEST_CODE_CHANGSYBXTGZ;

public class MainActivity extends AppCompatActivity implements MainActivityContact.View, DialogInterface.OnDismissListener, SelectDialog.SelectListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.tv_title_mainavtivity)
    TextView tvTitle;
    @BindView(R.id.tv_date_mainavtivity)
    TextView tvDate;
    @BindView(R.id.tv_errorinfo1_mainactivity)
    TextView tvErrorinfo1;
    @BindView(R.id.tv_errorinfo2_mainactivity)
    TextView tvErrorinfo2;
    @BindView(R.id.btn_road1_mainactivity)
    Button btnRoad1;
    @BindView(R.id.btn_changepwd_mainactivity)
    Button btnChangepwd;
    @BindView(R.id.btn_road2_mainactivity)
    Button btnRoad2;
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
    @BindView(R.id.tv_gz_mainactivity)
    TextView tvGz;

    private AlertDialog dialog;
    private PreferencesUtil preferencesUtil;
    private MainActivityContact.Presenter presenter;
    private boolean isConnect = false;
    private AlertDialog downloadDialog;
    private ProgressDialog downloadProgressDialog;
    private SelectDialog selectDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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

        /*if (selectDialog == null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            },2000L);

        }*/
    }

    /**
     * 成功加载事业部
     * @param sybData
     */
    @Override
    public void onLoadSybSecceed(List<String> sybData) {
        selectDialog = new SelectDialog(MainActivity.this);
        selectDialog.setOnDismissListener(this);
        selectDialog.setSelectListener(this);
        selectDialog.setSyb(sybData);
        selectDialog.show();
    }

    /**
     * 成功加载线体和工站
     * @param xtData
     * @param gzData
     */
    @Override
    public void onLoadXtAndGzSucceed(List<String> xtData, List<String> gzData) {
        selectDialog.setXtData(xtData);
        selectDialog.setGzData(gzData);
        selectDialog.show();
    }

    @Override
    public void onShowMsgDialog(String msg) {
        if (!MainActivity.this.isDestroyed()) {
            dialog.setMessage(msg);
            dialog.show();
        }
    }
    //某一个引脚发生状态变化（暂时不用）
    @Override
    public void onGpioStatuChange(String gpioIndex, String statu) {

    }

    /**
     * 系统更新中
     * @param progress
     */
    @Override
    public void onUpdate(int progress) {
        if (progress >= 0 && progress < 100) {
            downloadProgressDialog.setProgress(progress);
            downloadProgressDialog.show();
        } else if (progress >= 100) {
            downloadProgressDialog.dismiss();
        }
    }

    /**
     * 成功检查更新
     * @param hasUpdate
     * @param url
     */
    @Override
    public void onCheckUpdateSucceed(boolean hasUpdate, final String url) {
        if (hasUpdate) {
            downloadDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("提示")
                    .setMessage("发现新的版本，是否现在下载更新")
                    .setCancelable(false)
                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.update(url);
                        }
                    }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();


        } else {
            downloadDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("提示")
                    .setMessage("没有新版本")
                    .setCancelable(false)
                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
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
            ivNetstatu.setImageResource(R.mipmap.netstatu_disconnect);
        } else {
            isConnect = true;
            ivNetstatu.setImageResource(R.mipmap.netstatu_connect);
        }
    }

    @Override
    public void onLoad(boolean isLoad) {

    }

    @Override
    public void onStartSend(String gpioIndex) {
        if (GPIO_INDEX_3.equals(gpioIndex)) {
            ivDeng3Guidao1.setImageResource(R.mipmap.deng_open3);
            btnRoad1.setText("暂停一轨");
        } else if (GPIO_INDEX_4.equals(gpioIndex)) {
            ivDeng4Guidao2.setImageResource(R.mipmap.deng_open3);
            btnRoad2.setText("暂停二轨");
        }
    }

    @Override
    public void onStopSend(String gpioIndex) {
        if (GPIO_INDEX_3.equals(gpioIndex)) {
            ivDeng3Guidao1.setImageResource(R.mipmap.deng_close);
            btnRoad1.setText("启动一轨");
        } else if (GPIO_INDEX_4.equals(gpioIndex)) {
            ivDeng4Guidao2.setImageResource(R.mipmap.deng_close);
            btnRoad2.setText("启动二轨");
        }
    }


    @OnClick({R.id.btn_road1_mainactivity, R.id.btn_changepwd_mainactivity, R.id.btn_road2_mainactivity, R.id.btn_xtqh_mainactivity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_road1_mainactivity:
                checkPwd2ChangeGpioStatu(GPIO_INDEX_3);
                break;
            case R.id.btn_changepwd_mainactivity:
                showChangePwdDialog();
                break;
            case R.id.btn_road2_mainactivity:
                checkPwd2ChangeGpioStatu(GPIO_INDEX_4);
                break;
            case R.id.btn_xtqh_mainactivity:
                checkPwd2ChangeSystem();
                break;
        }
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
     * 检查密码，然后改变gpio运行状态
     * @param gpioIndex 引脚
     */
    public void checkPwd2ChangeGpioStatu(String gpioIndex) {

        if (!isConnect) {
            onShowMsgDialog("与服务器已断开连接");
            return;
        }
        Intent intent = new Intent(MainActivity.this, ChangeGpioActivity.class);
        intent.putExtra(GPIO_INDEX, gpioIndex);
        //检查权限
        startActivityForResult(intent, REQUEST_CODE_CHANGEGPIO);
    }
    /**
     * 检查密码，然后改变事业部，线体，工站
     *
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
        if (requestCode == REQUEST_CODE_CHANGEGPIO) {
            if (resultCode == RESULT_OK) {
                presenter.changeGpioStatu(data.getStringExtra(GPIO_INDEX));
            }
        }
        if (requestCode == REQUEST_CODE_CHANGSYBXTGZ) {
            if (resultCode == RESULT_OK) {
                presenter.loadSyb();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detroy();
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

    //选择事业部
    @Override
    public void onSelectSyb(String sybStr) {
        if (sybStr.equals("请选择事业部")) {
            return;
        }
        presenter.loadXtAndGz(sybStr);
    }
    //选择所有信息
    @Override
    public void onSelect(String sybStr, String xtStr, String gzStr) {
        setSybXtGz(sybStr, xtStr, gzStr);
        //presenter.changeGpioStatu(GPIO_INDEX_3);
        //presenter.changeGpioStatu(GPIO_INDEX_4);
    }
    //设置系统状态
    @Override
    public void setSybXtGz(String sybStr, String xtStr, String gzStr) {
        tvSyb.setText("系统名称:"+sybStr);
        tvXt.setText("生产线体:"+xtStr);
        tvGz.setText("采集工站:"+gzStr);
    }
}
