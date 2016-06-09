package com.android.pascual.androidchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogginActivity extends AppCompatActivity {
    @Bind(R.id.editTxtEmail)
    EditText inputEmail;
    @Bind(R.id.editTxtPassword)
    EditText inputPassword;
    @Bind(R.id.btnSignin)
    Button btnSignin;
    @Bind(R.id.btnSignup)
    Button btnSignup;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.layoutMainContainer)
    RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSignin)
    public void handleSignin() {
        //prueba
        Log.e("AndroidChat", inputEmail.getText().toString());
    }

    @OnClick(R.id.btnSignup)
    public void handleSignup() {
        //prueba
        Log.e("AndroidChat", inputPassword.getText().toString());
    }
}
