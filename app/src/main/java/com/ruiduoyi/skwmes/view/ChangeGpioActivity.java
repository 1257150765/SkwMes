package com.ruiduoyi.skwmes.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ruiduoyi.skwmes.Config;
import com.ruiduoyi.skwmes.R;
import com.ruiduoyi.skwmes.presenter.MainActivityPresenter;
import com.ruiduoyi.skwmes.util.PreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*
 * Created by 少雄 on 2018-04-12.
*/


public class ChangeGpioActivity extends AppCompatActivity {
    @BindView(R.id.et_pwd_changegpioactivity)
    EditText etPwd;
    @BindView(R.id.tv_tip_changegpioactivity)
    TextView tvTip;
    @BindView(R.id.btn_ok_changegpioactivity)
    Button btnOk;
    @BindView(R.id.btn_cancle_changegpioactivity)
    Button btnCancle;
    private PreferencesUtil preferencesUtil;
    private String gpioIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        hideBottomUIMenu();
        setContentView(R.layout.dialog_changegpio);
        preferencesUtil = new PreferencesUtil(ChangeGpioActivity.this);
        //如果密码为空则，设置起始密码
        if (preferencesUtil.getPwd().equals("")){
            preferencesUtil.savePwd(Config.PWD);
        }
        ButterKnife.bind(this);
        gpioIndex = getIntent().getStringExtra(MainActivityPresenter.GPIO_INDEX);
        etPwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                return false;
            }
        });
    }


    @OnClick({R.id.btn_ok_changegpioactivity, R.id.btn_cancle_changegpioactivity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_ok_changegpioactivity:
                if (preferencesUtil.getPwd().equals(etPwd.getText().toString())){
                    Intent intent = new Intent();
                    intent.putExtra(MainActivityPresenter.GPIO_INDEX,gpioIndex);
                    setResult(RESULT_OK,intent);
                    finish();
                }else {
                    tvTip.setText("密码错误");
                }
                break;
            case R.id.btn_cancle_changegpioactivity:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }
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
}
