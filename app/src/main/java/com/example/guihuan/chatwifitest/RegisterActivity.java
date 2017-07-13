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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guihuan.chatwifitest.jsip_ua.SipProfile;
import com.example.guihuan.chatwifitest.jsip_ua.impl.DeviceImpl;

import java.util.HashMap;

public class RegisterActivity extends Activity {
    private EditText editName;
    private EditText editPassword;
    private EditText editRePassword;
    private Button btnRegister;
    private String name;
    private String password;
    private String rePassword;
    private ImageButton backToLogin;
    private TextView txtBack;
    private Context context;
    public String serverSip="sip:Server@10.128.253.106:6666";
    SipProfile sipProfile;

    private Handler registerHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Var.UserhasExisted:
                    editName.setError(String.valueOf(msg.obj));
                    break;
                case Var.ServerError:
                    Toast.makeText(context,String.valueOf(msg.obj),Toast.LENGTH_SHORT);
                    break;
                case Var.RegisterSuccess:
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    /*
                     Intent intent = new Intent();
                     Bundle bundle = new Bundle();
                    //int imageId = contact.getImageId();
                    bundle.putString("user_name", ); //将朋友名称传进去
                    bundle.putInt("chatting_friend_head", imageId); //将朋友头像传进去
                    intent.setClass(context, ChatActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    */
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); // 注意顺序
        setContentView(R.layout.activity_register);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.register_title_bar);
        sipProfile = new SipProfile();
        HashMap<String, String> customHeaders = new HashMap<>();
        customHeaders.put("customHeader1","customValue1");
        customHeaders.put("customHeader2","customValue2");

        DeviceImpl.getInstance().Initialize(getApplicationContext(), sipProfile,customHeaders);
        DeviceImpl.getInstance().setChatHandler(registerHandler);
        context = getBaseContext();
        editName = (EditText) findViewById(R.id.edit_name);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editRePassword = (EditText) findViewById(R.id.edit_repassword);
        btnRegister = (Button) findViewById(R.id.btn_sign_up);
        backToLogin = (ImageButton) findViewById(R.id.registerBackToLogin);
        txtBack = (TextView) findViewById(R.id.txt_back);
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
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (checkPassword()) {
                        editRePassword.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });
        editRePassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                attemptRegister();
            }
        });
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
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

    private boolean checkRePassword() {
        editRePassword.setError(null);
        rePassword = editRePassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editRePassword.setError(getString(R.string.error_repassword_required));
            editRePassword.requestFocus();
            return false;
        }
        if (!password.equals(rePassword)) {
            editRePassword.setError(getString(R.string.error_password_inconstancy));
            editRePassword.requestFocus();
            return false;
        }
        return !(password == null || password.isEmpty() || !password.equals(rePassword));
    }


    private void attemptRegister() {
        if (!checkName())
            return;
        if (!checkPassword())
            return;
        if (!checkRePassword())
            return;

        DeviceImpl.getInstance().SendMessage(Var.serverSip,name+"&"+ password,"REGISTER");
    }
}
