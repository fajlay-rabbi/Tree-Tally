package com.example.treetally;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class CartFag extends Fragment {

    ListView cartListView;
    CartDB cartDB;
    CartAdapter cartAdapter;
    ArrayList<CartModel> cartArrayList = new ArrayList<>();
    TextView cartEmpty, cartTotal;
    Button orderBtn;

    public CartFag() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart_fag, container, false);

        cartListView = view.findViewById(R.id.cart_listview);
        cartEmpty = view.findViewById(R.id.cart_empty);
        orderBtn = view.findViewById(R.id.cart_checkout_btn);
        cartTotal = view.findViewById(R.id.total_price);



//        byte[] imgar = {20, 25, 25};
//        cartArrayList.add(new CartModel("1", "Rabbi", "200", "indoor", "5", imgar));





        cartAdapter = new CartAdapter(requireContext(), cartArrayList, cartTotal, orderBtn);
        cartListView.setAdapter(cartAdapter);




//        orderBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(requireContext(), Order_Activity.class);
//                startActivity(intent);
//
//
//            }
//        });








        return view;

    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchCartData();

        if (cartArrayList.size() == 0){
            cartListView.setVisibility(View.GONE);
            cartEmpty.setVisibility(View.VISIBLE);
            orderBtn.setEnabled(false);
            orderBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.disable_green)));
        }else{
            orderBtn.setEnabled(true);
            orderBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.green)));
            cartEmpty.setVisibility(View.GONE);
            cartListView.setVisibility(View.VISIBLE);
        }

    }



    private void fetchCartData() {

        cartArrayList.clear();

        cartDB = new CartDB(requireContext(),"CartDB.db", null, 1);

        Cursor rows = cartDB.onFetch();

        if (rows.getCount() > 0) {
            while (rows.moveToNext()) {


                int id = rows.getInt(0);
                String name = rows.getString(1);
                String price = rows.getString(2);
                String type = rows.getString(3);
                String quantity = rows.getString(4);
                byte[] image = rows.getBlob(5);
                String total_price = rows.getString(6);


                CartModel cartModel = new CartModel(String.valueOf(id), name, price, type, quantity, image, total_price);
                cartArrayList.add(cartModel);
            }
        }

        rows.close();
        cartDB.close();
    }
}