package com.itproger.orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itproger.orders.Helpers.CartItemsAdapter;
import com.itproger.orders.Helpers.JSONHelper;
import com.itproger.orders.Models.Cart;
import com.itproger.orders.Models.Order;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private ListView listView;
    private Button btnMakeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   //уберем верхнюю панель на экране в приложении - заряд, wi-fi и т.п.

        btnMakeOrder = findViewById(R.id.btnMakeOrder);
        listView = findViewById(R.id.shopping_cart);
        List<Cart> cartList = JSONHelper.importFromJSON(this);      //получаем все объекты из /json файла

        if(cartList != null){
            CartItemsAdapter arrayAdapter = new CartItemsAdapter(CartActivity.this, R.layout.cart_item, cartList);
            listView.setAdapter(arrayAdapter);
        } else {
            Toast.makeText(this, "Не удалось подгрузить данные в корзину", Toast.LENGTH_SHORT).show();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table = database.getReference("Order");

        btnMakeOrder.setOnClickListener(new View.OnClickListener() {    //при нажатии на кнопку мы должны добавлять заказ внутрь таблицы Order
            @Override
            public void onClick(View v) {
                table.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //чтобы добавить объект в таблицу, мы должны его создать
                        //для начала возьмем номер телефона, через прописанный ранее метод getDefaults ( он позоляет получить определюзначение
                        // через SharedPreferences), номер телефона нужен чтобы добавить его в конструктор нового объекта

                        List<Cart> cartList = JSONHelper.importFromJSON(CartActivity.this);
                            if(cartList == null){
                                Toast.makeText(CartActivity.this, "Добавьте заказы в корзину", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        String activeUser = SignIn.getDefaults("phone", CartActivity.this);
                        Order order = new Order(JSONHelper.createJSONString(cartList), activeUser);

                        //для установки ключа нужно получить время
                        Long tsLong = System.currentTimeMillis()/1000;
                        table.child(tsLong.toString()).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {         //если успешно добавили объект

                                //создаем пустой список и забрасываем его в .json файл, чтобы файл был пустым
                                List<Cart> cartList = new ArrayList<>();
                                JSONHelper.exportToJSON(CartActivity.this, cartList);

                                //очистим listView, в качестве объектов указываем пустой список
                                CartItemsAdapter arrayAdapter = new CartItemsAdapter(CartActivity.this, R.layout.cart_item, cartList);
                                listView.setAdapter(arrayAdapter);

                                Toast.makeText(CartActivity.this, "Заказ сформирован!", Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}
