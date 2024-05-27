package com.example.treetally;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login_Activity extends AppCompatActivity {

    TextView signUp;
    Button loginBtn;

    TextInputLayout lyEmail,lyPass;

    TextInputEditText  etEmail,etPassword;

    userImgDB imgDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        signUp = findViewById(R.id.tvSignUp);
        loginBtn = findViewById(R.id.btnLogin);



        signUp.setOnClickListener(v -> {
             Intent intent = new Intent(Login_Activity.this, Sign_up_Activity.class);
             startActivity(intent);
             finish();
        });



        lyEmail = findViewById(R.id.etEmail);
        etEmail = lyEmail.findViewById(R.id.inEmail);

        lyPass = findViewById(R.id.etPassword);
        etPassword = lyPass.findViewById(R.id.inPassword);






        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lyEmail.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lyEmail.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                lyEmail.setError(null);
            }
        });


        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lyPass.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lyPass.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                lyPass.setError(null);
            }
        });






        loginBtn.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();


            if (email.isEmpty()) {
                etEmail.setError("Please enter your email");
                etEmail.requestFocus();

            } else if (password.isEmpty()) {

                etPassword.setError("Please enter your password");
                etPassword.requestFocus();

            } else {



                sendLoginRequest(email, password);



            }

        });

    }


    private void sendLoginRequest(String email, String password) {


        //     String url = "http://192.168.0.107:3000/";   //mine
//        String url = "http://192.168.43.57:3000/";   //kirittka

//        String url = "http://192.168.137.146:3000/login";     //tamim
        String url = "http://192.168.43.57:3000/login";

        RequestQueue requestQueue = Volley.newRequestQueue(Login_Activity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("message");





                    if (status.equals("success")) {

                        String id = jsonObject.getString("id");
                        String jwt = jsonObject.getString("token");
                        String name = jsonObject.getString("name");
                        JSONObject imageDataObject = jsonObject.getJSONObject("image");


                        if (jwt != null && name != null){

                            SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();


                            JSONArray imageDataArray = imageDataObject.getJSONArray("data");

                            System.out.println("Login: " + "image: " + imageDataArray);



                            byte[] imageBytes = new byte[imageDataArray.length()];


                            for (int i = 0; i < imageDataArray.length(); i++) {
                                imageBytes[i] = (byte) imageDataArray.getInt(i);
                            }

                            imgDB = new userImgDB(Login_Activity.this);

                            imgDB.onInsert(email, imageBytes);

                            imgDB.close();

                            editor.putString("email", email);
                            editor.putString("jwt", jwt);
                            editor.putString("name", name);
                            editor.putString("id", id);

                            editor.apply();

                            Toast.makeText(Login_Activity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Login_Activity.this, Home_Activity.class);
                            startActivity(intent);
                            finish();

                        }else {
                            Toast.makeText(Login_Activity.this, "Login Failed! Try again", Toast.LENGTH_SHORT).show();
                        }

                    } else if (status.equals("failed")){
                        Toast.makeText(Login_Activity.this, "Login Failed! Try again", Toast.LENGTH_SHORT).show();
                    } else if(status.equals("notExists")){

                        lyEmail.setError("Email doesn't exist!");
                        etEmail.requestFocus();

                        Toast.makeText(Login_Activity.this, "Email already exists!", Toast.LENGTH_SHORT).show();
                    } else if (status.equals("wrongPass")) {
                        lyPass.setError("Wrong Password!");
                        etPassword.requestFocus();
                        Toast.makeText(Login_Activity.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        },
                new  Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("Error Fetching Data", "onErrorResponse: " + error.getMessage());

                    }
                }
        )

        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        requestQueue.add(stringRequest);

    }










}