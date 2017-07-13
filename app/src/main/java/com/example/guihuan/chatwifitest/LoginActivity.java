package com.example.guihuan.chatwifitest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private Context context;
    //public String serverSip = "sip:Server@10.128.253.106:6666";
    SipProfile sipProfile;
    private String friendList;
    private String onlineFriendList;
    boolean isSuccess = false;
    boolean isGetList = false;

    private Handler loginHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Var.UserHasLogined:
                    isSuccess = false;
                    isGetList = false;
                    editName.setError(String.valueOf(msg.obj));
                    break;
                case Var.PasswordIncorrect:
                    isSuccess = false;
                    isGetList = false;
                    editPassword.setError(String.valueOf(msg.obj));
                    break;
                case Var.UserNotExist:
                    isSuccess = false;
                    isGetList = false;
                    editName.setError(String.valueOf(msg.obj));
                case Var.LoginSuccess:
                    isSuccess = true;
                    isGetList = false;
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("user_name", name); //将用户名称传进去
                    bundle.putString("friend_list", friendList); //将好友列表传进去
                    bundle.putString("online_list",onlineFriendList);//将在线好友列表传进去
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                    break;
                case Var.FriendList:
                    if (isSuccess) {
                        friendList = String.valueOf(msg.obj);
                        isGetList = true;
                    }
                    break;
                case Var.OnlineFriendList:
                    if (isSuccess & isGetList){
                        onlineFriendList = String.valueOf(msg.obj);
                        isSuccess = false;
                        isGetList = false;

                        //传递用户名和好友列表、在线好友列表

                    }
                    break;
                default:
                    break;
            }
        }
    };

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
        DeviceImpl.getInstance().setChatHandler(loginHandler);

        context = getBaseContext();
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