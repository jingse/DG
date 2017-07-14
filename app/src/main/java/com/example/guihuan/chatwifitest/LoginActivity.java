package com.example.guihuan.chatwifitest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.guihuan.chatwifitest.jsip_ua.SipProfile;
import com.example.guihuan.chatwifitest.jsip_ua.impl.DeviceImpl;

import java.util.HashMap;


public class LoginActivity extends Activity {
    private EditText editName;
    private EditText editPassword;
    private Button btnLogin;
    private TextView btnNewUser;
    private String name;
    private String password;
    public String serverSip = "sip:Server@10.128.253.106:6666";
    SipProfile sipProfile;
    private MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        sipProfile = new SipProfile();
        HashMap<String, String> customHeaders = new HashMap<>();
        customHeaders.put("customHeader1", "customValue1");
        customHeaders.put("customHeader2", "customValue2");

        DeviceImpl.getInstance().Initialize(getApplicationContext(), sipProfile, customHeaders);
        myHandler = new MyHandler();
        DeviceImpl.getInstance().setChatHandler(myHandler);

        editName = (EditText) findViewById(R.id.edit_name);
        editPassword = (EditText) findViewById(R.id.edit_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnNewUser = (TextView) findViewById(R.id.btn_sign_up);
        editName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (checkName()) {
                        editPassword.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });
        editPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                attemptLogin();
            }
        });
        btnNewUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), RegisterActivity.class));
            }
        });


    }
    class MyHandler extends Handler {
        public MyHandler() {
        }

        public MyHandler(Looper L) {
            super(L);
        }

        // 子类必须重写此方法，接受数据
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Var.FriendList:
                    Var.friendList = String.valueOf(msg.obj);
                    System.out.println("好友列表："+ Var.friendList);
                    break;
                case Var.OnlineFriendList:
                    Var.onlineList = String.valueOf(msg.obj);
                    System.out.println("在线好友列表："+ Var.onlineList);
                    break;
                case Var.UserHasLogined:
                    editName.setError(getString(R.string.user_has_logined));
                    break;
                case Var.PasswordIncorrect:
                    editPassword.setError(getString(R.string.password_incorrect));
                    break;
                case Var.UserNotExist:
                    editName.setError(getString(R.string.user_not_exist));
                    break;
                case Var.LoginSuccess:
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("user_name", name); //将用户名称传进去
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        myHandler = new MyHandler();
        DeviceImpl.getInstance().setChatHandler(myHandler);

    }
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
    }

    private boolean checkName() {
        editName.setError(null);
        name = editName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            editName.setError(getString(R.string.error_name_required));
            editName.requestFocus();
            return false;
        }
        return !(name == null || name.isEmpty());
    }

    private boolean checkPassword() {
        editPassword.setError(null);
        password = editPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editPassword.setError(getString(R.string.error_password_required));
            editPassword.requestFocus();
            return false;
        }
        return !(password == null || password.isEmpty());
    }


    private void attemptLogin() {
        if (!checkName())
            return;

        if (!checkPassword())
            return;
        DeviceImpl.getInstance().SendMessage(Var.serverSip, name + "&" + password, "INVITE");

    }

}