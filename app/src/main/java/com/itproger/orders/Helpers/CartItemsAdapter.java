package com.itproger.orders.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itproger.orders.FoodDetail;
import com.itproger.orders.Models.Cart;
import com.itproger.orders.Models.Category;
import com.itproger.orders.R;

import java.util.List;

public class CartItemsAdapter extends ArrayAdapter<Cart> {

    private LayoutInflater layoutInflater;      //этот объект обеспечивает работу внутри .xml файла
    private List<Cart> cartList;          //внутри переменной будут храниться все объекты, для наполнения самого списка
    private int layoutListRow;

    public CartItemsAdapter(@NonNull Context context, int resource, @NonNull List<Cart> objects) {
        super(context, resource, objects);

        cartList = objects;
        layoutListRow = resource;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //перепишем метод getView. Он показывает каким образом адаптер будет устанавливать значения внутри списка
    //Сам метод getView вызывается каждый раз для формирования каждого элемента внутри  списка

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = layoutInflater.inflate(layoutListRow, null);    //переменная содержит в себе значение того дизайна, который будет использован для каждого элемента внутри списка
        //в переменную position каждый раз будет подставляться новое значение (от 0 и далее) при формировании нового ряда
        Cart cart = cartList.get(position);
        if(cart != null) {
            final TextView productName = convertView.findViewById(R.id.productName);
            TextView amount = convertView.findViewById(R.id.amount);

            ImageView photo = convertView.findViewById(R.id.mainPhoto);

            if(amount != null)
                amount.setText(String.valueOf(cart.getAmount()));

            if(productName !=null) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference table = database.getReference("Category");

                table.child(String.valueOf(cart.getCategoryID())).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Category category = snapshot.getValue(Category.class);
                        productName.setText(category.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
        return convertView;
    }
}
