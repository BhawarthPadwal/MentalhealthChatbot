package com.bae.dialogflowbot.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bae.dialogflowbot.MainActivity;
import com.bae.dialogflowbot.R;

public class UserLoginPage extends AppCompatActivity {
    private Button button;
    private TextView forgot_tv, return_reg_pg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_page);

        call_all_ids();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserLoginPage.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        forgot_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserLoginPage.this, ForgotPassword.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        return_reg_pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserLoginPage.this, UserRegisterPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }
    private void call_all_ids() {

        button = findViewById(R.id.login_btn_login);
        forgot_tv = findViewById(R.id.forgot_pass_txt_login);
        return_reg_pg = findViewById(R.id.return_reg_pg);
    }
}