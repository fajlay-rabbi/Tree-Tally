package com.example.treetally;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.treetally.PlantModel;
import com.example.treetally.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecylerPlantAdapter extends RecyclerView.Adapter<RecylerPlantAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PlantModel> plantArrayList;
    private String type;
    private String searchText;
    private ArrayList<PlantModel> filteredList;

    private WishlistDB wishlistDB;

    private CartDB cartDB;

    private int total_price;

    String sId, sEmail, sJSON_web_token;


    RecylerPlantAdapter(Context context, ArrayList<PlantModel> plantArrayList, String type) {
        this.context = context;
        this.plantArrayList = plantArrayList;
        this.type = type;
        this.searchText = "";
        this.filteredList = new ArrayList<>();
        this.total_price = 0;
        filterList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.plant_card, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



        PlantModel plant = filteredList.get(position);

        byte[] imageByteArray = plant.image;

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);

//        holder.plant_image.setImageResource(plant.image);

        holder.plant_image.setImageBitmap(bitmap);


        holder.plant_name.setText(plant.name);
        holder.plant_price.setText(plant.price);

        if (plant.type.equals("indoor")){
            holder.plant_type.setText("Indoor");
        } else if (plant.type.equals("outdoor")) {
            holder.plant_type.setText("Outdoor");
        } else if (plant.type.equals("garden")) {
            holder.plant_type.setText("Garden");
        }


//        holder.plant_type.setText(plant.type);



        String plantID = plant.id;
//        byte[] img = plant.image;
//        String name = plant.name;
//        String price = plant.price;
//        String type = plant.type;



        holder.add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cartDB = new CartDB(context, "CartDB.db", null, 1);

//
//                String id = plant.id;
//                byte[] img = plant.image;
//                String name = plant.name;
//                String price = plant.price;
//                String type = plant.type;



//                Cursor cursor = cartDB.onFindOne(id);
//
//                if (cursor != null && cursor.getCount() > 0) {
//
//                    cursor.moveToFirst();
//
//                    String strQuan = cursor.getString(4);
//                    String strTot = cursor.getString(6);
//
//                    int currQuan = Integer.parseInt(strQuan);
//                    int currTotalPrice = Integer.parseInt(strTot);
//
//                    int newQuantity = currQuan + 1;
////                    int newTotalPrice = currTotalPrice + Integer.parseInt(price);
//
//                    total_price = currTotalPrice + Integer.parseInt(price);
//
//
//
////                    System.out.println("cart: " +  "total_price: " + newTotalPrice + "newQuan: " + newQuantity + " name: " + name );
//
//                    cartDB.onUpdate(String.valueOf(newQuantity), String.valueOf(total_price), id);
//
//
//                } else {
//                    total_price += Integer.parseInt(price);
//                    cartDB.onInsert(id, name, price, type, img, "1", String.valueOf(total_price));
//                }
//
//
//                cursor.close();
//                cartDB.close();
                String id = plant.id;
                byte[] img = plant.image;
                String name = plant.name;
                String price = plant.price;
                String type = plant.type;

//                total_price += Integer.parseInt(price);

                cartDB = new CartDB(context, "CartDB.db", null, 1);

                Cursor cursor = cartDB.onFindOne(id);


                if (cursor.getCount() == 0){
//                    total_price += Integer.parseInt(price);
                    cartDB.onInsert(id, name, price, type, img, "1", price);

                }

//                cartDB.onInsert(id, name, price, type, img, "1", price);

                cartDB.close();
            }
        });



        holder.add_to_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                sEmail = sharedPreferences.getString("email", "");
                sJSON_web_token = sharedPreferences.getString("jwt", "");
                sId = sharedPreferences.getString("id", "");

                if (sEmail.equals("") || sJSON_web_token.equals("") || sId.equals("")){

                    showErrDialog();

                } else {

                    sendWishlistRequest(plantID);

                }

            }
        });


        holder.itemView.setAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in));
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    // Method to filter the list based on search text
    public void filterList() {
        filteredList.clear();
        if (TextUtils.isEmpty(searchText)) {
            if (type.equals("all")) {
                filteredList.addAll(plantArrayList);
            } else {
                for (PlantModel plant : plantArrayList) {
                    if (type.equals(plant.type)) {
                        filteredList.add(plant);
                    }
                }
            }
        } else {
            for (PlantModel plant : plantArrayList) {
                String name = plant.name.toLowerCase();
                String searchStr = searchText.toLowerCase();
                if (name.startsWith(searchStr) || name.contains(" "+searchStr)){
                    if (type.equals("all") || type.equals(plant.type)) {
                        filteredList.add(plant);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    // Update the search text and filter the list
    public void setSearchText(String searchText) {
        this.searchText = searchText;
        filterList();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView plant_name, plant_price, plant_type;
        ImageView plant_image;

        Button add_to_cart;
        ImageButton add_to_wishlist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plant_name = itemView.findViewById(R.id.plant_name);
            plant_price = itemView.findViewById(R.id.plant_price);
            plant_image = itemView.findViewById(R.id.plant_image);
            plant_type = itemView.findViewById(R.id.plant_type);
            add_to_cart = itemView.findViewById(R.id.AddToCart);
            add_to_wishlist = itemView.findViewById(R.id.addToWishlist);
        }
    }


    private void showErrDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("You are not logged in");
        builder.setMessage("Are you want to login? now");
        builder.setCancelable(true);


        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Intent intent = new Intent(context, Login_Activity.class);
                context.startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });


        AlertDialog ad = builder.create();
        ad.show();
    }

    public void sendWishlistRequest(String plantID) {


//        String url = "http://192.168.137.146:3000/wishlist";

        String url = "http://192.168.43.57:3000/wishlist";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("message");

                    if (status.equals("success")) {

                       Toast.makeText(context, "Added to wishlist", Toast.LENGTH_SHORT).show();

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

                params.put("id", sId);
                params.put("JSON_web_token", sJSON_web_token);
                params.put("plant_id", plantID);


                return params;
            }

        };

        requestQueue.add(stringRequest);



}
}
