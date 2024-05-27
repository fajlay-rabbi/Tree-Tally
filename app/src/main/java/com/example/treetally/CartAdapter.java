package com.example.treetally;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CartAdapter extends ArrayAdapter<CartModel> {

    private final Context context;
    private final ArrayList<CartModel> values;

    TextView total_price;
    private CartDB cartDB;

    int total;

    Button orderBtn;

    public CartAdapter(@NonNull Context context, ArrayList<CartModel> values, TextView total_price, Button orderBtn) {
        super(context, R.layout.cart_row, values);
        this.context = context;
        this.values = values;
        this.total_price = total_price;
        this.total = 0;
        this.orderBtn = orderBtn;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        View rowView = inflater.inflate(R.layout.cart_row, parent, false);
//        TextView name = rowView.findViewById(R.id.cart_name);
//        TextView price = rowView.findViewById(R.id.cart_price);
//        TextView quantity = rowView.findViewById(R.id.cart_quan);
//        ImageView image = rowView.findViewById(R.id.cart_image);


        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_row, parent, false);
        }

        CartModel cartValue = values.get(position);

        TextView name = convertView.findViewById(R.id.cart_name);
        TextView price = convertView.findViewById(R.id.cart_price);
        TextView quantity = convertView.findViewById(R.id.cart_quan);
        ImageView image = convertView.findViewById(R.id.cart_image);



        ImageButton cart_add = convertView.findViewById(R.id.cart_add);
        ImageButton cart_remove = convertView.findViewById(R.id.cart_remove);

        String id = cartValue.id;
        cart_add.setTag(id);
        String nameText = cartValue.name;
        String priceText = "৳ " + cartValue.price;
        String typeText = cartValue.type;
        String quantityText = cartValue.quantity;
        byte[] imageByteArray = cartValue.image;
        total += Integer.parseInt(cartValue.total_price);




        name.setText(nameText);
        price.setText(priceText);
        quantity.setText(quantityText);
        total_price.setText("Total: ৳ "+ total);

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        image.setImageBitmap(bitmap);


        //quantity ar total price increase kore dibo
        //shared preference use kore total price er value ta store kore rakhte hobe


        cart_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = ((ImageButton)v).getTag().toString();
                try {

                    int quan = Integer.parseInt(quantityText);
                    quan++;

                    cartValue.quantity = String.valueOf(quan);

                    notifyDataSetChanged();

//                    quantity.setText(cartValue.quantity);

                    int itemTotal = (Integer.parseInt(cartValue.price) * quan);

                    total -= Integer.parseInt(cartValue.price);

                    total += itemTotal;


                    total_price.setText("Total: ৳ "+ String.valueOf(total));


                    cartDB = new CartDB(context, null, null, 1);

                    cartDB.onUpdate(String.valueOf(quan), String.valueOf(itemTotal), id);


                    cartDB.close();

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        cart_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    int quan = Integer.parseInt(quantityText);
                    quan--;
                    cartValue.quantity = String.valueOf(quan);

                    total -= Integer.parseInt(cartValue.price);



                    notifyDataSetChanged();

                    if (quan == 0){
                        cartDB = new CartDB(context, null, null, 1);
                        cartDB.onDelete(id);
                        cartDB.close();
                        values.remove(position);
                        notifyDataSetChanged();

                    }else{
                        int itemTotal = (Integer.parseInt(cartValue.price) * quan);
                        total += itemTotal;
                        total_price.setText("Total: ৳ "+ String.valueOf(total));

                        cartDB = new CartDB(context, null, null, 1);
                        cartDB.onUpdate(String.valueOf(quan), String.valueOf(itemTotal), id);
                        cartDB.close();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //order activity te jabe
                //cartDB teke sob data delete hobe
                //cartArrayList teke sob data delete hobe
                //cartListView teke sob data delete hobe
                //cartEmpty teke sob data delete hobe
                //orderBtn disable hobe
                //total price 0 hobe
                //cartListView invisible hobe
                //cartEmpty visible hobe

                Intent intent = new Intent(context, Order_Activity.class);

                intent.putExtra("total", String.valueOf(total));

                context.startActivity(intent);
            }
        });






        return convertView;
    }







}
