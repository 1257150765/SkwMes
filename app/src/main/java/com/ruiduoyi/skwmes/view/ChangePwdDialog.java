package com.ruiduoyi.skwmes.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ruiduoyi.skwmes.R;
import com.ruiduoyi.skwmes.util.PreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chen on 2018/4/24.
 */

public class ChangePwdDialog extends AlertDialog {
    @BindView(R.id.et_oldpwd_dialog_changepwd)
    EditText etOldpwd;
    @BindView(R.id.et_newpwd_dialog_changepwd)
    EditText etNewpwd;
    @BindView(R.id.tv_cancel_dialog_changepwd)
    TextView tvCancel;
    @BindView(R.id.tv_ok_dialog_changepwd)
    TextView tvOk;
    @BindView(R.id.et_newpwd_again_dialog_changepwd)
    EditText etNewpwdAgain;
    private View mRootView;
    private PreferencesUtil preferencesUtil;
    public ChangePwdDialog(@NonNull Context context) {
        this(context, 0);
    }

    protected ChangePwdDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(Context context) {
        preferencesUtil = new PreferencesUtil(context);
        mRootView = LayoutInflater.from(context).inflate(R.layout.dialog_changepwd, null, false);

        setView(mRootView);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ButterKnife.bind(this, mRootView);

    }
    //按键事件
    @OnClick({R.id.tv_cancel_dialog_changepwd, R.id.tv_ok_dialog_changepwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel_dialog_changepwd:
                dismiss();
                break;
            case R.id.tv_ok_dialog_changepwd:
                check(etOldpwd.getText().toString(),etNewpwd.getText().toString(),etNewpwdAgain.getText().toString());
                break;
        }
    }
    //检查并保存用户名
    private void check(String oldPwd, String newPwd, String newPwdAgain) {
        String pwd = "";

        if (preferencesUtil.getPwd().equals("")){
            pwd = "123456";
        }else {
            pwd = preferencesUtil.getPwd();
        }
        if (!pwd.equals(oldPwd)){
            showMsg("旧密码错误");
            return;
        }
        if ("".equals(newPwd)||newPwd.length() != 6){
            showMsg("新密码只能6位数字");
            return;
        }
        if(!newPwd.equals(newPwdAgain)){
            showMsg("两次输入的密码不一致");
            return;
        }
        //验证成功，保存密码
        preferencesUtil.savePwd(newPwd);
        dismiss();
    }
    //显示提示信息
    private void showMsg(String msg){
        Snackbar.make(mRootView,msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        //attributes.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 500,getContext().getResources().getDisplayMetrics());
        //attributes.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 350,getContext().getResources().getDisplayMetrics());
        attributes.width = 500;
        //attributes.height = 400;
        getWindow().setAttributes(attributes);
    }
}
