package com.example.treetally;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;

public class Home_Activity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    Boolean isLogin = false;
    String name,image, email;

    userImgDB imgDB;

    AppCompatButton logoutBtn;
    de.hdodenhof.circleimageview.CircleImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        try {
            SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

            name = sharedPreferences.getString("name", "");
            email = sharedPreferences.getString("email", "");

            if (name.equals("") || email.equals("")){
                isLogin = false;
            }else {
                isLogin = true;
            }

        }catch (Exception e) {
            e.printStackTrace();
        }




        bottomNavigationView = findViewById(R.id.bottom_navigation);
        toolbar = findViewById(R.id.toolbar);
        logoutBtn = findViewById(R.id.login_button);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.home_item){
                    loadFag(new HomeFrag());
                } else if (id == R.id.wishlist_item) {
                    loadFag(new WishlistFag());
                } else if (id == R.id.card_item) {
                    loadFag(new CartFag());
                } else {
                    loadFag(new TrackFag());
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.home_item);



        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Activity.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        profileImage = findViewById(R.id.profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        if (isLogin){

            logoutBtn.setVisibility(View.GONE);

            try {

//                byte[] imageBytes = Base64.decode(image, Base64.DEFAULT);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//                profileImage.setImageBitmap(bitmap);

                imgDB = new userImgDB(Home_Activity.this);
                Cursor cursor = imgDB.onFetch(email);

                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        byte[] imageBytes = cursor.getBlob(2);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        profileImage.setImageBitmap(bitmap);
                    }
                }

                cursor.close();
                imgDB.close();




            }catch (Exception e){

                e.printStackTrace();
            }


            System.out.println("image: "+ image);

            profileImage.setVisibility(View.VISIBLE);

        }else {
            profileImage.setVisibility(View.GONE);
            logoutBtn.setVisibility(View.VISIBLE);
        }


    }

    public void loadFag(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.commit();
    }


    public void showPopupMenu(View view) {

        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.popup_menu);

        MenuItem nameItem = popupMenu.getMenu().findItem(R.id.userName);
        nameItem.setTitle(name);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_logout){


                    SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    imgDB = new userImgDB(Home_Activity.this);

                    imgDB.onDeleteAll();

                    imgDB.close();



                    Intent intent = new Intent(Home_Activity.this, Home_Activity.class);
                    startActivity(intent);
                    finish();





                    return true;
                } else {

                    return true;
                }
            }
        });
        popupMenu.show();
    }





}