package com.example.sethcohen.muchotrabajo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sethcohen.muchotrabajo.Model.DBManager;
import com.example.sethcohen.muchotrabajo.Model.User;
import com.example.sethcohen.muchotrabajo.Util.Validator;


public class RegisterActivity extends AppCompatActivity {

    private EditText email, name, password, password2;
    private View registerLayout;
    private Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialization();
        clickListeners();
    }

//======================= Variables initialisation ===========================

    private void initialization() {
        register = (Button) findViewById(R.id.register_btn_register);

        email = (EditText) findViewById(R.id.register_edit_text_email);
        name = (EditText) findViewById(R.id.register_edit_text_name);
        password = (EditText) findViewById(R.id.register_edit_text_password);
        password2 = (EditText) findViewById(R.id.register_edit_text_confirm_password);
        registerLayout = findViewById(R.id.register_layout);
    }

    //======================= Click listeners initialisation ===========================

    private void clickListeners() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userDataIsValid()) {
                    String nameVal = name.getText().toString();
                    String passwordVal = password.getText().toString();
                    String emailVal = email.getText().toString();

                    User u = new User(nameVal, emailVal, passwordVal);
                    DBManager.getInstance(RegisterActivity.this).addUser(u);
                    Toast.makeText(RegisterActivity.this, R.string.registration_success, Toast.LENGTH_SHORT).show();
                    setProgressDialog();
                    Intent intent = new Intent();
                    intent.putExtra("EMAIL", emailVal);
                    intent.putExtra("PASSWORD", passwordVal);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, R.string.invalid_fields, Toast.LENGTH_SHORT).show();
                }


            }
        });

}


    @Override
    protected void onResume() {
        super.onResume();
    }

//======================= User validation ===========================

    private boolean userDataIsValid() {

        boolean isValid = true;

        if (!password.getText().toString().matches(password2.getText().toString())) {
            password2.setError(getString(R.string.pass_dont_match));
            isValid = false;
            password2.requestFocus();
        }

        if (!Validator.validatePassword(password.getText().toString())) {
            password.setError(getString(R.string.pass_has_to_be));
            isValid = false;
            password.requestFocus();
        }

        if (!Validator.validateName(name.getText().toString())) {
            name.setError(getString(R.string.enter_name));
            isValid = false;
            name.requestFocus();
        }

        if (!Validator.validateEmail(email.getText().toString())) {
            email.setError(getString(R.string.invalid_email));
            isValid = false;
            email.requestFocus();
        }

        if (DBManager.getInstance(RegisterActivity.this).userExists(email.getText().toString())) {
            isValid = false;
            email.setError(getString(R.string.email_registered));
            email.requestFocus();
        }

        return isValid;
    }

    //======================= Progress dialog ===========================

    public void setProgressDialog(){
        AsyncTask<Void, Void, Void> progressBar = new AsyncTask<Void, Void, Void>() {

            ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);

            @Override
            protected void onPreExecute() {
                dialog.setMessage(getString(R.string.please_wait));
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                dialog.dismiss();
            }
        };

        progressBar.execute();

    }
}
