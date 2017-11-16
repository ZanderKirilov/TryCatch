package com.example.sethcohen.muchotrabajo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sethcohen.muchotrabajo.Model.DBManager;
import com.example.sethcohen.muchotrabajo.Model.User;
import com.example.sethcohen.muchotrabajo.Util.Validator;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;
    private static final int GALLERY_REQUEST = 30;
    private ImageView imageView;
    private EditText name, password, confirm;

    private Button btnUpdate, btnUploadImage, btnAddCV;

    private User user;
    private boolean imageChanged = false;
    private byte[] bytes;
    private View topView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = (User) getIntent().getSerializableExtra("user");

        initialization();

        name.setText(user.getName());
        password.setText(user.getPassword());
        confirm.setText(user.getPassword());

        byte[] bytes = user.getUserImageBytes();

        if (bytes == null) {

            imageView.setImageResource(R.mipmap.emptyprofile);
        } else {

            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            RoundedBitmapDrawable round = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            round.setCircular(true);
            imageView.setImageDrawable(round);
        }
        topView.requestFocus();

        clickListeners();

    }

//======================= Variables initialisation ===========================

    private void initialization() {
        topView = findViewById(R.id.profile_top_view2);

        name = (EditText) findViewById(R.id.et_nameChange_profile);
        password = (EditText) findViewById(R.id.et_passwordChange_profile);
        confirm = (EditText) findViewById(R.id.et_password2Change_profile);

        imageView = (ImageView) findViewById(R.id.iv_imageChange_profile);

        btnAddCV = (Button)findViewById(R.id.btn_addCV_profile);
        btnUpdate = (Button)findViewById(R.id.btn_update_profile);
        btnUploadImage = (Button)findViewById(R.id.btn_avatarChange_profile);

    }

    //======================= Click listeners start ===========================

    private void clickListeners(){
        addCVListener();
        updateInfoListener();
        uploadImageListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();

                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(resultUri);

                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                    bytes = Validator.getBytes(image);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    RoundedBitmapDrawable round = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
                    round.setCircular(true);

                    imageView.setImageDrawable(round);

                    imageChanged = true;
                    Toast.makeText(getApplicationContext(), R.string.image_changed, Toast.LENGTH_SHORT).show();

                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                    imageChanged = false;
                    Toast.makeText(getApplicationContext(), R.string.unable_to_open, Toast.LENGTH_LONG).show();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getApplicationContext(), R.string.wrong_cropping, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImageListener() {
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_REQUEST);
            }
        });
    }

    private void updateInfoListener() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isUserDataValid() || imageChanged){

                    user.setName(name.getText().toString());
                    user.setPassword(password.getText().toString());
                    DBManager.getInstance(ProfileActivity.this).updateUser(user);

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", user);
                    intent.putExtras(bundle);

                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void addCVListener() {
        btnAddCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/*");
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });
    }

//======================= User validation ===========================

    private boolean isUserDataValid() {

        boolean isValid = true;

        if (!password.getText().toString().matches(confirm.getText().toString())) {
            confirm.setError(getString(R.string.pass_dont_match));
            isValid = false;
            confirm.requestFocus();
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

        return isValid;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
