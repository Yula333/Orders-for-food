package com.itproger.orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itproger.orders.Helpers.JSONHelper;
import com.itproger.orders.Models.Cart;
import com.itproger.orders.Models.Category;
import com.itproger.orders.Models.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodDetail extends AppCompatActivity {

    public static int ID = 0;

    private ImageView mainPhoto;
    private TextView foodMainName, price, foodFullName;

    private Button btnGoToCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   //уберем верхнюю панель на экране в приложении - заряд, wi-fi и т.п.

        mainPhoto = findViewById(R.id.mainPhoto);
        foodMainName = findViewById(R.id.foodMainName);
        price = findViewById(R.id.price);
        foodFullName = findViewById(R.id.foodFullName);
        btnGoToCart = findViewById(R.id.btnGoToCart);

        btnGoToCart.setOnClickListener(new View.OnClickListener() {             //при нажатии на кнопку Перейти в корзину
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FoodDetail.this, CartActivity.class));     //осуществляется переход на новую активити CartActivity (корзина)
            }
        });

        FirebaseDatabase  database = FirebaseDatabase.getInstance();
        final DatabaseReference table = database.getReference("Category");

        table.child(String.valueOf(ID)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Category category = snapshot.getValue(Category.class);      //получаем нужный объект из БД из таблицы Category
                foodMainName.setText(category.getName());

                int id = getApplicationContext().getResources().getIdentifier("drawable/" + category.getImage(), null, getApplicationContext().getPackageName());
                mainPhoto.setImageResource(id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FoodDetail.this, "Нет интернет соединения", Toast.LENGTH_SHORT).show();
            }
        });

        final DatabaseReference table_food = database.getReference("Food");
        table_food.child(String.valueOf(ID)).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Food foodItem = snapshot.getValue(Food.class);
                price.setText(foodItem.getPrice() + " рублей");
                foodFullName.setText(foodItem.getFull_text());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FoodDetail.this, "Нет интернет соединения", Toast.LENGTH_SHORT).show();
            }
        });
    }
        public void btnAddToCart(View view){
            List<Cart> cartList = JSONHelper.importFromJSON(this);
            if(cartList == null){                   //если пользователь нажал на кнопку добавить в корзину впервые
                cartList = new ArrayList<>();
                cartList.add(new Cart(ID, 1));      //добавляем в список первый объект /первый товар, ту категорию на странице которой находится пользователь
            }else{                                  //если повторно нажимает на кнопку добавить, не зависимо на какой странице пользователь находится
                boolean isFound = false;
                for(Cart el: cartList){                 //перебираем корзину по добавленным позициям
                    if(el.getCategoryID() == ID) {      //если находимся на той странице, объект которой уже занесен в корзину
                        el.setAmount(el.getAmount() + 1);       // то увеличиваем его кол-во
                        isFound = true;
                    }
                }
                if(!isFound)                        //если в корзине (объекте .json) нет еще такого объекта
                    cartList.add(new Cart(ID, 1));      //то добавляем в наш список этот объект как новый
            }


            boolean result = JSONHelper.exportToJSON(this, cartList);          //список переводим в .json объект и устанавливаем значения внутрь файла
            if(result = true){
                Toast.makeText(this, "Добавлено", Toast.LENGTH_SHORT).show();
                Button btnCart = (Button) view;
                btnCart.setText("Добавлено");
            }else {
                Toast.makeText(this, "Не добавлено", Toast.LENGTH_SHORT).show();
            }
    }

}
