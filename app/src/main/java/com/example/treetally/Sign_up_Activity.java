package com.example.treetally;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Sign_up_Activity extends AppCompatActivity {

    LinearLayout uploadImg;
    private byte[] photo;

    TextView login;
    String base64Image;

    AppCompatButton btnRegister;

    private RequestQueue requestQueue;

    TextInputLayout lyemail;
    TextInputEditText etEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //to hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sign_up);


        btnRegister = findViewById(R.id.btnRegister);
        login = findViewById(R.id.tvLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_up_Activity.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });


        TextInputLayout lyname = findViewById(R.id.etName);
        lyemail = findViewById(R.id.etEmail);
        TextInputLayout lyphone = findViewById(R.id.etPhone);
        TextInputLayout lypassword = findViewById(R.id.etPassword);
        TextInputLayout lyaddress = findViewById(R.id.etAddress);


        TextInputEditText etName = lyname.findViewById(R.id.inName);
        etEmail = lyemail.findViewById(R.id.inEmail);
        TextInputEditText etPhone = lyphone.findViewById(R.id.inPhone);
        TextInputEditText etPassword = lypassword.findViewById(R.id.inPassword);
        TextInputEditText etAddress = lyaddress.findViewById(R.id.inAddress);


        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lyname.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lyname.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                lyname.setError(null);
            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lyemail.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lyemail.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                lyemail.setError(null);
            }
        });

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lyphone.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lyphone.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                lyphone.setError(null);
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lypassword.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lypassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                lypassword.setError(null);
            }
        });

        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lyaddress.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lyaddress.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                lyaddress.setError(null);
            }
        });









        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();
                String password = etPassword.getText().toString();
                String address = etAddress.getText().toString();


                if (name.isEmpty()) {
                    lyname.setError("Name is required!");
                    etName.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    lyemail.setError("Email is required!");
                    etEmail.requestFocus();
                    return;
                }

                if (phone.isEmpty()) {
                    lyphone.setError("Phone is required!");
                    etPhone.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    lypassword.setError("Password is required!");
                    etPassword.requestFocus();
                    return;
                }

                if (address.isEmpty()) {
                    lyaddress.setError("Address is required!");
                    etAddress.requestFocus();
                    return;
                }

                if (photo == null) {
                    Toast.makeText(Sign_up_Activity.this, "Please select an image!", Toast.LENGTH_SHORT).show();
                    return;
                }


                sendSignUpRequest(name, email, phone, password, address, photo);


//                testReq();







            }
        });













        uploadImg = findViewById(R.id.uploadBtn);
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        //onCreate ends here

    }

    private void sendSignUpRequest(String name, String email, String phone, String password, String address, byte[] photo) {


        //     String url = "http://192.168.0.107:3000/";   //mine
        String url = "http://192.168.43.57:3000/Signup";   //kirittka

//        String url = "http://192.168.137.146:3000/Signup";     //tamim


        RequestQueue requestQueue = Volley.newRequestQueue(Sign_up_Activity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("message");

                            if (status.equals("success")) {

                                showErrDialog("Account Created Successfully", "You are redirecting to login page.");

                            } else if (status.equals("failed")){

                                Toast.makeText(Sign_up_Activity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                            } else if(status.equals("exists")){
                                lyemail.setError("Email already exists!");
                                etEmail.requestFocus();
                                Toast.makeText(Sign_up_Activity.this, "Email already exists!", Toast.LENGTH_SHORT).show();
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

                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("phone", phone);
                params.put("address", address);
                params.put("photo", base64Image);


                return params;
            }

        };

        requestQueue.add(stringRequest);

    }








    private void openImagePicker() {
        Intent openGallery = new Intent(Intent.ACTION_PICK);
        openGallery.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGallery, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((resultCode == RESULT_OK) && (requestCode == 1000)) {
            Uri imageUri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                byteArrayOutputStream.close();

                photo = byteArrayOutputStream.toByteArray();

                base64Image = Base64.encodeToString(photo, Base64.DEFAULT);

            } catch (Exception e) {
                Toast.makeText(this, "Selecting Image failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void showErrDialog(String title, String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(false);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    Intent intent = new Intent(Sign_up_Activity.this, Login_Activity.class);
                    startActivity(intent);
                    finish();

                }
            });


        AlertDialog ad = builder.create();
        ad.show();
    }







//everything ends here
}