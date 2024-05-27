package com.example.treetally;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WishlistFag extends Fragment {


    ListView wishListview;

    WishlistAdapter wishlistAdapter;
    WishlistModel wishlistModel;

    ArrayList<WishlistModel> wishArrayList = new ArrayList<>();

    TextView empty_wishlist;

    String email, JSON_web_token, user_id;

    Boolean isLogin = false;

    LinearLayout loginLayout;

    AppCompatButton loginBtn;

    ShimmerFrameLayout shimmerFrameLayout;

    public WishlistFag() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist_fag, container, false);



        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", getContext().MODE_PRIVATE);

        user_id = sharedPreferences.getString("id","");
        email = sharedPreferences.getString("email", "");
        JSON_web_token = sharedPreferences.getString("jwt", "");

        if (email.equals("") || JSON_web_token.equals("") || user_id.equals("")){
            isLogin = false;
        } else {
            isLogin = true;
        }



        wishListview = view.findViewById(R.id.wish_listview);
        loginLayout = view.findViewById(R.id.wish_login_layout);
        loginBtn = view.findViewById(R.id.wish_login);

        shimmerFrameLayout = view.findViewById(R.id.wish_shimmer_view_container);
        shimmerFrameLayout.startShimmer();

        if (isLogin){
            wishListview.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);
            wishlistAdapter = new WishlistAdapter(requireContext(), wishArrayList);
            wishListview.setAdapter(wishlistAdapter);


        }else {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            wishListview.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
        }




        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), Login_Activity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });




        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isLogin){

            fetchWishlist(user_id, JSON_web_token);

        } else {
            Toast.makeText(requireContext(), "You are not logged in!", Toast.LENGTH_SHORT).show();
        }



    }

    private void fetchWishlist(String userId, String jsonWebToken) {


//        String url = "http://192.168.137.146:3000/fetchWishlist";

        String url = "http://192.168.43.57:3000/fetchWishlist";



        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url , new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("message");

                    if (status.equals("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String Id = jsonObject1.getString("id");
                            String name = jsonObject1.getString("name");
                            String type = jsonObject1.getString("type");
                            String price = jsonObject1.getString("price");

                            JSONObject imageObject = jsonObject1.getJSONObject("image");
                            JSONArray dataArray = imageObject.getJSONArray("data");

                            byte[] imageByteArray = new byte[dataArray.length()];

                            for (int j = 0; j < dataArray.length(); j++) {
                                imageByteArray[j] = (byte) dataArray.getInt(j);
                            }

                            System.out.println("Wishlist: " + "Id: " + Id + " name: " + name + " type: " + type + " price: " + price + " image: " + imageByteArray);

                            wishArrayList.add(new WishlistModel(Id, name, price, type, imageByteArray));

                        }

                        wishlistAdapter.notifyDataSetChanged();

                    } else {
                        System.out.println("myMsg: "+ response);
                        Toast.makeText(requireContext(), "Error Fetching Data", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },
                new  Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);

                        Log.e("Error Fetching Data", "onErrorResponse: " + error.getMessage());

                    }
                }
        )

        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("id", userId);
                params.put("token", jsonWebToken);

                return params;
            }

        };

        requestQueue.add(stringRequest);

    }


//    private void fetchWishlist( String userID, String user_JSON_web_token) {
//
//
//        if (userID.equals("") || user_JSON_web_token.equals("")){
//            Toast.makeText(requireContext(), "You are not logged in!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//
//        JSONArray jsonArray = new JSONArray();
//
//        try {
//
//            JSONObject jsonItem = new JSONObject();
//            jsonItem.put("id", userID);
//            jsonItem.put("token", user_JSON_web_token);
//
//            jsonArray.put(jsonItem);
//
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //        String url = "http://192.168.0.107:3000/";   //mine
////        String url = "http://192.168.43.57:3000/";   //kirittka
//
//        String url = "http://192.168.137.146:3000/fetchWishlist";     //tamim
//
//
//
//        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                Request.Method.POST,
//                url,
//                jsonArray,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//
////                        shimmerFrameLayout.stopShimmer();
////                        shimmerFrameLayout.setVisibility(View.GONE);
////                        recyclerView.setVisibility(View.VISIBLE);
//
//
//
//                        try {
//                            for (int i = 0; i < response.length(); i++) {
//
//                                JSONObject jsonObject = response.getJSONObject(i);
//
//
//                                String Id = jsonObject.getString("id");
//                                String name = jsonObject.getString("name");
//                                String type = jsonObject.getString("type");
//                                String price = jsonObject.getString("price");
//
//
//                                JSONObject imageObject = jsonObject.getJSONObject("image");
//                                JSONArray dataArray = imageObject.getJSONArray("data");
//
//
//                                byte[] imageByteArray = new byte[dataArray.length()];
//
//
//                                for (int j = 0; j < dataArray.length(); j++) {
//                                    imageByteArray[j] = (byte) dataArray.getInt(j);
//                                }
//
//                                System.out.println("Wishlist: " + "Id: " + Id + " name: " + name + " type: " + type + " price: " + price);
//
////                                wishArrayList.add(new WishlistModel(Id, name, price, type, imageByteArray));
//
//                            }
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
////                        shimmerFrameLayout.stopShimmer();
////                        shimmerFrameLayout.setVisibility(View.GONE);
////                        recyclerView.setVisibility(View.VISIBLE);
//
//                        Log.e("Error Fetching Data", "onErrorResponse: " + error.getMessage());
//
//                    }
//                }
//        ) {
//
//            @Override
//            protected Map<String, String> getParams() {
//
//                Map<String, String> params = new HashMap<>();
//
//                params.put("id", userID);
//                params.put("token", user_JSON_web_token);
//
//                return params;
//            }
//
//        };
//
//        requestQueue.add(jsonArrayRequest);
//    }


}
