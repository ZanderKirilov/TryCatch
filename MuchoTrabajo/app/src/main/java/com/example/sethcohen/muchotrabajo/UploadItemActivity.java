package com.example.sethcohen.muchotrabajo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sethcohen.muchotrabajo.Model.DBManager;
import com.example.sethcohen.muchotrabajo.Model.Item;
import com.example.sethcohen.muchotrabajo.Model.User;
import com.example.sethcohen.muchotrabajo.Util.Util;

import java.util.ArrayList;


public class UploadItemActivity extends AppCompatActivity {

    private Button btnUpload;

    private EditText txtTitle, txtDesc, txtLocation, txtSalary, txtCompanyName;

    private Spinner spCategory;
    public static User user;
    public static Item item;
    private View topView;

    private boolean isTitleTrue, isDescTrue, isLocationTrue, isSalaryTrue, isCompanyNameTrue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        user = (User) bundle.getSerializable("user");
        initialization();
        checks();
        clickListeners();

        final ArrayList<String> types = DBManager.getInstance(this).getCategories();
        ArrayAdapter<CharSequence> arrTypesAdapter = ArrayAdapter.createFromResource(this, R.array.itemTypes, android.R.layout.simple_spinner_item);
        spCategory.setAdapter(arrTypesAdapter);
        arrTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

//======================= Variables initialisation ===========================

    private void initialization(){

        topView = findViewById(R.id.uploadItem_top_view);

        txtTitle = (EditText) findViewById(R.id.txtTitlePreview);
        txtDesc = (EditText) findViewById(R.id.txtDescPreview);
        txtLocation = (EditText) findViewById(R.id.tv_location_ads);
        txtSalary = (EditText) findViewById(R.id.tv_salary_upload) ;
        txtCompanyName = (EditText) findViewById(R.id.tv_companyname_ads);

        spCategory = (Spinner) findViewById(R.id.sp_category_ads);

        btnUpload = (Button) findViewById(R.id.btnSaveUpload);
    }

//======================= Fields check start ===========================

    private void checks(){
        txtTitleCheck();
        txtDescCheck();
        txtLocationCheck();
        txtSalaryCheck();
        txtCompanyCheck();
    }

    private void clickListeners(){
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = ((TextView) view).getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadItem();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

//======================= Upload ad ===========================

    private void uploadItem() {

        if (isCompanyNameTrue && isTitleTrue && isDescTrue && isLocationTrue && isSalaryTrue) {

            Item newItem = new Item(
                    txtTitle.getText().toString(),
                    txtDesc.getText().toString(),
                    txtCompanyName.getText().toString(),
                    Util.twoDecimalPlaces(Double.parseDouble(txtSalary.getText().toString())),
                    txtLocation.getText().toString(),
                    getCategoryID());

            user.addItem(newItem);
            DBManager.getInstance(this).addNewItem(newItem);

                Toast.makeText(this, R.string.ad_uploaded, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                finish();
        }
    }

    private void txtTitleCheck() {
        txtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 40) {
                    isTitleTrue = true;
                } else {
                    txtTitle.setError(getString(R.string.min_length_title));
                    isTitleTrue = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void txtDescCheck() {
        txtDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 800) {
                    isDescTrue = true;
                } else {
                    txtDesc.setError(getString(R.string.max_length));
                    isDescTrue = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void txtLocationCheck() {
        txtLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 20) {
                    isLocationTrue = true;
                } else {
                    txtLocation.setError(getString(R.string.min_length_location));
                    isLocationTrue = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void txtCompanyCheck() {
        txtDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 20) {
                    isCompanyNameTrue = true;
                } else {
                    txtCompanyName.setError(getString(R.string.min_length_company));
                    isCompanyNameTrue = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void txtSalaryCheck() {
        txtSalary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches("[0-9.]+") && s.length() <= 9) {
                    isSalaryTrue = true;
                    Double salary = Double.parseDouble(txtSalary.getText().toString());
                } else {
                    txtSalary.setError(getString(R.string.digits_only));
                    isSalaryTrue = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private int getCategoryID() {

        String category = spCategory.getSelectedItem().toString();

        return DBManager.getInstance(UploadItemActivity.this).getCategoryID(category);
    }
}
