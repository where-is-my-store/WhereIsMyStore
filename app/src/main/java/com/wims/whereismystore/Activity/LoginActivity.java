package com.wims.whereismystore.Activity;

import android.content.Intent; // 인텐트 활용

import android.os.Bundle; // 액티비티 생성 번들
import android.view.View; // 뷰계열 최상위 클래스
import android.widget.Button; // 버튺 뷰 사용

import androidx.appcompat.app.AppCompatActivity;

import com.example.Final_Project.R;

import static com.example.Final_Project.R.id.login_button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button button1 = (Button)findViewById(R.id.join_button);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });


        Button button2 = (Button)findViewById(login_button);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SaleActivity.class);
                startActivity(intent);
            }
        });

    }
}