package com.example.sethcohen.muchotrabajo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sethcohen.muchotrabajo.Adapter.MyRecyclerViewAdapter;
import com.example.sethcohen.muchotrabajo.Model.DBManager;
import com.example.sethcohen.muchotrabajo.Model.Item;
import com.example.sethcohen.muchotrabajo.Model.User;
import com.example.sethcohen.muchotrabajo.Util.Validator;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int CHANGE_PROFILE = 50;
    private static final int ITEM_ACTIVITY = 6;

    private TextView welcome, userName, userEmail;
    private User user;
    private View top;
    private RecyclerView recyclerView;
    private ImageView userImage;
    private ArrayList<Item> items;

    private byte[] bytes;
    private boolean imageChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        welcome = (TextView) findViewById(R.id.user_text_view_welcome);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        user = (User) bundle.getSerializable("user");

        welcome.setText(getString(R.string.welcome_message) + " " + user.getName());

        NavigationView userHeader = (NavigationView) findViewById(R.id.nav_view);
        View v = userHeader.getHeaderView(0);
        userName = (TextView) v.findViewById(R.id.nav_header_text_view_user_name);
        userEmail = (TextView) v.findViewById(R.id.nav_header_text_view_user_email);
        userImage = (ImageView) v.findViewById(R.id.imageView);

        if (user.getUserImageBytes() != null) {

            bytes = user.getUserImageBytes();

            if (bytes == null) {
                userImage.setImageResource(R.mipmap.emptyprofile);
            } else {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                RoundedBitmapDrawable round = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                round.setCircular(true);
                userImage.setImageDrawable(round);
                userImage.setImageBitmap(bitmap);
            }
        }

        userName.setText(user.getName());
        userEmail.setText(user.getEmail());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getAllItems();

        recyclerView = (RecyclerView) findViewById(R.id.content_user_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void onResume() {
        super.onResume();
        getAllItems();
        userName.setText(user.getName());
    }

//========== OnBackPressed AlertDialog, logout ==============

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            final AlertDialog.Builder exitAlert = new AlertDialog.Builder(UserActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth);
            exitAlert.setTitle(R.string.alertExitQuest);
            exitAlert.setMessage(R.string.alertExit);
            exitAlert.setPositiveButton(R.string.exitYes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            exitAlert.setNegativeButton(R.string.exitNo, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            final AlertDialog alert = exitAlert.create();
            alert.show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.user, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_items_for_sell) {

            Intent intent = new Intent(UserActivity.this, UploadItemActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user);
            intent.putExtras(bundle);
            startActivityForResult(intent, ITEM_ACTIVITY);
        }

        if (id == R.id.action_search) {

        }

        if (id == R.id.action_profile) {

            Intent intent = new Intent(UserActivity.this, ProfileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user);
            intent.putExtras(bundle);
            startActivityForResult(intent, CHANGE_PROFILE);
        }

        if (id == R.id.action_logout) {

            final AlertDialog.Builder exitAlert = new AlertDialog.Builder(UserActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth);
            exitAlert.setTitle(R.string.alertLogoutQuest);
            exitAlert.setMessage(R.string.alertLogout);
            exitAlert.setPositiveButton(R.string.logoutYes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                    finish();
                }
            });

            exitAlert.setNegativeButton(R.string.logoutNo, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            final AlertDialog alert = exitAlert.create();
            alert.show();

        }

        return super.onOptionsItemSelected(item);
    }

//========== Get profile image ==============

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHANGE_PROFILE && resultCode == RESULT_OK) {

            user = (User) data.getSerializableExtra("user");
            userName = (TextView) findViewById(R.id.nav_header_text_view_user_name);
            userName.setText(user.getName());
            welcome.setText(user.getName());

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
                        RoundedBitmapDrawable round = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                        round.setCircular(true);
                        userImage.setImageDrawable(round);
                        imageChanged = true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        imageChanged = false;
                        Toast.makeText(getApplicationContext(), R.string.enable_to_open_image, Toast.LENGTH_LONG).show();
                    }

                    byte[] bytes = user.getUserImageBytes();
                    if (bytes == null) {
                        userImage.setImageResource(R.mipmap.emptyprofile);
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        RoundedBitmapDrawable round = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                        round.setCircular(true);

                        userImage.setImageDrawable(round);
                    }
                }
            }
        }

        if (requestCode == ITEM_ACTIVITY && resultCode == RESULT_OK) {

            user = (User) data.getSerializableExtra("user");
            welcome.setText(user.getName());
        }
    }

//========== Load all items from DB ==============

    private void getAllItems() {

        items = DBManager.getInstance(this).getAllItems(user.getId());

        if (items.size() == 0 || items.isEmpty()) {

        } else {

            recyclerView = (RecyclerView) findViewById(R.id.content_user_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            setRecyclerViewVisible();
        }
    }

//========== Set recycleView ==============

    private void setRecyclerViewVisible() {

        recyclerView.setVisibility(View.VISIBLE);
        welcome.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyRecyclerViewAdapter(this, items));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        Menu m = nv.getMenu();
        int id = item.getItemId();
        if (id == R.id.category_home) {
            getAllItems();
            setRecyclerViewVisible();
        } else if (id == R.id.category_advert) {
            getItemByCategory("Advertising");
        } else if (id == R.id.category_archit) {
            getItemByCategory("Architecture");
        } else if (id == R.id.category_art) {
            getItemByCategory("Art");
        } else if (id == R.id.category_aviation) {
            getItemByCategory("Aviation");
        } else if (id == R.id.category_bank) {
            getItemByCategory("Banking");
        } else if (id == R.id.category_design) {
            getItemByCategory("Design");
        } else if (id == R.id.category_hosp) {
            getItemByCategory("Hospitality");
        } else if (id == R.id.category_hr) {
            getItemByCategory("Human resources");
        } else if (id == R.id.category_it) {
            getItemByCategory("IT");
        } else if (id == R.id.category_mark) {
            getItemByCategory("Marketing");
        } else if (id == R.id.category_pr) {
            getItemByCategory("PR");
        } else if (id == R.id.category_security) {
            getItemByCategory("Security");
        } else if (id == R.id.category_sport) {
            getItemByCategory("Sport");
        } else if (id == R.id.category_trans_logist) {
            getItemByCategory("Transport and Logistic");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//========== Get items by their category from DB ==============

    private void getItemByCategory(String category) {

        items = DBManager.getInstance(this).getItemByCategory(category);

        if (items.size() == 0 || items.isEmpty()) {
            welcome.setText(getString(R.string.no_items) + " " + category);
            recyclerView.setVisibility(View.GONE);
        } else {
            welcome.setText(category);
            setRecyclerViewVisible();
        }
    }
}
