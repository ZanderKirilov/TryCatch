package com.example.sethcohen.muchotrabajo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sethcohen.muchotrabajo.Model.DBManager;
import com.example.sethcohen.muchotrabajo.Model.User;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private TextView register;
    private EditText email, password;
    private static final int REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialization();
        clickListeners();
    }

    //======================= Variables initialisation ===========================

    private void initialization() {
        login = (Button) findViewById(R.id.login_btn_login);
        register = (TextView) findViewById(R.id.login_btn_register);
        email = (EditText) findViewById(R.id.login_edit_text_email);
        password = (EditText) findViewById(R.id.login_edit_text_password);

    }

//======================= Click listeners initialisation ===========================

    private void clickListeners(){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User u;

                if (DBManager.getInstance(LoginActivity.this).validLogin(email.getText().toString(), password.getText().toString())) {
                    u = DBManager.getInstance(LoginActivity.this).getUser(email.getText().toString(), password.getText().toString());

                    if (u == null) {
                        Toast.makeText(LoginActivity.this, R.string.fatal_error, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", u);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, getString(R.string.you_logged_as) + u.getName(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, R.string.wrong_email_pass, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String emailMessage = data.getStringExtra("EMAIL");
                    String passwordMessage = data.getStringExtra("PASSWORD");

                    email.setText(emailMessage);
                    password.setText(passwordMessage);
                }
            }
        }
    }
}
