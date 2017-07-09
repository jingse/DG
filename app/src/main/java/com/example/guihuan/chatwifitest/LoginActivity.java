package com.example.guihuan.chatwifitest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends Activity {
    private EditText editName;
    private EditText editPassword;
    private Button btnLogin;
    private TextView btnNewUser;
    private String name;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        editName = (EditText) findViewById(R.id.edit_name);
        editPassword = (EditText) findViewById(R.id.edit_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnNewUser = (TextView)findViewById(R.id.btn_sign_up);
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
                //startActivity(new Intent(getApplication(), RegisterActivity.class));
            }
        });
    }

    private boolean checkName() {
        editName.setError(null);
        name = editName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            editName.setError(getString(R.string.error_name_required));
            editPassword.requestFocus();
            return false;
        }
        return !(name == null || name.isEmpty());
    }

    private boolean checkPassword() {
        editPassword.setError(null);
        password = editPassword.getText().toString().trim();
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
        /*
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage(getString(R.string.action_logining));
        pd.show();
        */
        //todo:login,getfriendlist,user作为全局变量??
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}