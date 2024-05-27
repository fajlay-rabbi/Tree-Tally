package com.example.treetally;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;




import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFrag extends Fragment {
    private AppCompatButton all_filter, indoor_filter, outdoor_filter, garden_filter;
    String filter = "all";
    private AppCompatEditText search_bar;

    RecylerPlantAdapter recylerPlantAdapter;
    RecyclerView recyclerView;
    LinearLayout wel_sec;

    ShimmerFrameLayout shimmerFrameLayout;


    private ArrayList<PlantModel> plantArrayList = new ArrayList<>();

    public HomeFrag() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        all_filter = view.findViewById(R.id.all_filter);
        indoor_filter = view.findViewById(R.id.indoor_filter);
        outdoor_filter = view.findViewById(R.id.outdoor_filter);
        garden_filter = view.findViewById(R.id.garden_filter);

        search_bar = view.findViewById(R.id.search_box);
        wel_sec = view.findViewById(R.id.welcome_section);


        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmer();




        recyclerView = view.findViewById(R.id.plant_recycler);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


//        plantArrayList.add(new PlantModel(R.drawable.plant, "Money Plant", "₹ 200", "indoor"));
//        plantArrayList.add(new PlantModel(R.drawable.plant, "Free Plant", "₹ 200", "indoor"));
//        plantArrayList.add(new PlantModel(R.drawable.plant, "Rabbi Plant", "₹ 200", "indoor"));
//        plantArrayList.add(new PlantModel(R.drawable.plant, "Krittika Plant", "₹ 200", "indoor"));
//        plantArrayList.add(new PlantModel(R.drawable.plant, "Money Plant", "₹ 200", "indoor"));
//        plantArrayList.add(new PlantModel(R.drawable.plant, "Free Plant", "₹ 200", "garden"));
//        plantArrayList.add(new PlantModel(R.drawable.plant, "Rabbi Plant", "₹ 200", "indoor"));
//        plantArrayList.add(new PlantModel(R.drawable.plant, "Krittika Plant", "₹ 200", "outdoor"));
//        plantArrayList.add(new PlantModel(R.drawable.plant, "Money Plant", "₹ 200", "indoor"));
//        plantArrayList.add(new PlantModel(R.drawable.plant, "Free Plant", "₹ 200", "indoor"));
//        plantArrayList.add(new PlantModel(R.drawable.plant, "Rabbi Plant", "₹ 200", "outdoor"));
//        plantArrayList.add(new PlantModel(R.drawable.plant, "Krittika Plant", "₹ 200", "garden"));







        all_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String  filter = "all";




                recylerPlantAdapter = new RecylerPlantAdapter(requireContext(), plantArrayList, filter);
                recyclerView.setAdapter(recylerPlantAdapter);



                all_filter.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)));
                all_filter.setTextColor(Color.parseColor("#FFFFFFFF"));

                indoor_filter.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.semiwhite)));
                indoor_filter.setTextColor(Color.parseColor("#FF000000"));

                outdoor_filter.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.semiwhite)));
                outdoor_filter.setTextColor(Color.parseColor("#FF000000"));

                garden_filter.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.semiwhite)));
                garden_filter.setTextColor(Color.parseColor("#FF000000"));

            }
        });

        indoor_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String  filter = "indoor";

//                recylerPlantAdapter.notifyDataSetChanged();
//                recyclerView.invalidate();

                recylerPlantAdapter = new RecylerPlantAdapter(requireContext(), plantArrayList, filter);
                recyclerView.setAdapter(recylerPlantAdapter);


                indoor_filter.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)));
                indoor_filter.setTextColor(Color.parseColor("#FFFFFFFF"));

                all_filter.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.semiwhite)));
                all_filter.setTextColor(Color.parseColor("#FF000000"));

                outdoor_filter.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.semiwhite)));
                outdoor_filter.setTextColor(Color.parseColor("#FF000000"));

                garden_filter.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.semiwhite)));
                garden_filter.setTextColor(Color.parseColor("#FF000000"));



            }
        });

        outdoor_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String filter = "outdoor";
//              recylerPlantAdapter.notifyDataSetChanged();
//              recyclerView.invalidate();


                recylerPlantAdapter = new RecylerPlantAdapter(requireContext(), plantArrayList, filter);
                recyclerView.setAdapter(recylerPlantAdapter);



                outdoor_filter.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)));
                outdoor_filter.setTextColor(Color.parseColor("#FFFFFFFF"));

                all_filter.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.semiwhite)));
                all_filter.setTextColor(Color.parseColor("#FF000000"));

                indoor_filter.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.semiwhite)));
                indoor_filter.setTextColor(Color.parseColor("#FF000000"));

                garden_filter.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.semiwhite)));
                garden_filter.setTextColor(Color.parseColor("#FF000000"));



            }
        });

        garden_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            String  filter = "garden";

//              recylerPlantAdapter.notifyDataSetChanged();
//              recyclerView.invalidate();
//
              recylerPlantAdapter = new RecylerPlantAdapter(requireContext(), plantArrayList, filter);
              recyclerView.setAdapter(recylerPlantAdapter);




                garden_filter.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)));
                garden_filter.setTextColor(Color.parseColor("#FFFFFFFF"));

                all_filter.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.semiwhite)));
                all_filter.setTextColor(Color.parseColor("#FF000000"));

                indoor_filter.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.semiwhite)));
                indoor_filter.setTextColor(Color.parseColor("#FF000000"));

                outdoor_filter.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.semiwhite)));
                outdoor_filter.setTextColor(Color.parseColor("#FF000000"));



            }
        });



        recylerPlantAdapter = new RecylerPlantAdapter(requireContext(), plantArrayList, filter);
        recyclerView.setAdapter(recylerPlantAdapter);





        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recylerPlantAdapter.setSearchText(s.toString());
                if (s.toString().isEmpty()) {
                    wel_sec.setVisibility(View.VISIBLE);
                } else {
                    wel_sec.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        return view;

    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchPlantData();


    }






    private void fetchPlantData() {

//        String url = "http://192.168.0.107:3000/";   //mine
//        String url = "http://192.168.43.57:3000/";   //kirittka

//        String url = "http://192.168.137.58:3000/";     //tamim

//        String url = "http://192.168.137.146:3000/";     //tamim

        String url = "http://192.168.43.57:3000/";

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        try {
                            for (int i = 0; i < response.length(); i++) {


                                JSONObject jsonObject = response.getJSONObject(i);

                                String Id = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                String type = jsonObject.getString("type");
                                String price = jsonObject.getString("price");


                                JSONObject imageObject = jsonObject.getJSONObject("image");
                                JSONArray dataArray = imageObject.getJSONArray("data");

                                byte[] imageByteArray = new byte[dataArray.length()];

                                for (int j = 0; j < dataArray.length(); j++) {
                                    imageByteArray[j] = (byte) dataArray.getInt(j);
                                }

//                                System.out.println("Id: " + Id + " Name: " + name + " Type: " + type + " Price: " + price + " Image: " + imageByteArray.toString());

                                plantArrayList.add(new PlantModel(Id, imageByteArray, name, price, type));




                            }


                            recylerPlantAdapter = new RecylerPlantAdapter(requireContext(), plantArrayList, filter);
                            recyclerView.setAdapter(recylerPlantAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        Log.e("Error Fetching Data", "onErrorResponse: " + error.getMessage());

                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }





}