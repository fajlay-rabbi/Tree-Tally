package com.example.treetally;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WishlistAdapter extends ArrayAdapter<WishlistModel> {
    private final Context context;
    private final ArrayList<WishlistModel> values;

    CartDB cartDB;


    public WishlistAdapter(@NonNull Context context, ArrayList<WishlistModel> values) {
        super(context, R.layout.wishlist_row, values);
        this.context = context;
        this.values = values;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.wishlist_row, parent, false);
        }

        WishlistModel wishlistValue = values.get(position);

        TextView name = convertView.findViewById(R.id.item_name);
        TextView price = convertView.findViewById(R.id.item_price);
        ImageView image = convertView.findViewById(R.id.item_image);
        TextView type = convertView.findViewById(R.id.item_type);

        TextView add_btn = convertView.findViewById(R.id.item_add);
        ImageButton remove_btn = convertView.findViewById(R.id.item_remove);


        name.setText(wishlistValue.name);
        price.setText(wishlistValue.price);
        type.setText(wishlistValue.type);

        byte[] imageByteArray = wishlistValue.image;

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        image.setImageBitmap(bitmap);


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                cartDB = new CartDB(context, "cartDB", null, 1);
                cartDB.onInsert(wishlistValue.id, wishlistValue.name, wishlistValue.price, wishlistValue.type, wishlistValue.image, "1", wishlistValue.price);

                sendWishlistDeleteRequest(wishlistValue.id);


                values.remove(position);
                notifyDataSetChanged();
            }
        });


        remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendWishlistDeleteRequest(wishlistValue.id);

                values.remove(position);
                notifyDataSetChanged();
            }
        });











        return convertView;

    }


    public void sendWishlistDeleteRequest(String wishlistID) {

//        String url = "http://192.168.137.146:3000/deleteWishlist";
        String url = "http://192.168.43.57:3000/deleteWishlist";
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("message");

                    if (status.equals("success")) {

                        Toast.makeText(context, "Item added to wishlist", Toast.LENGTH_SHORT).show();

                    } else if (status.equals("failed")){

                        Toast.makeText(context, "Please try again!", Toast.LENGTH_SHORT).show();

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

                params.put("id", wishlistID);

                return params;
            }

        };

        requestQueue.add(stringRequest);

    }












}
