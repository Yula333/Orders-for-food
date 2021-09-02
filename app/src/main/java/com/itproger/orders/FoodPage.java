package com.itproger.orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itproger.orders.Helpers.FoodListAdapter;
import com.itproger.orders.Models.Category;

import java.util.ArrayList;

public class FoodPage extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_page);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   //уберем верхнюю панель на экране в приложении - заряд, wi-fi и т.п.

        listView = findViewById(R.id.list_of_food);

        FirebaseDatabase database = FirebaseDatabase.getInstance();     //подключаемся к БД к нашей таблице Category
        final DatabaseReference table = database.getReference("Category");

        table.addValueEventListener(new ValueEventListener() {      //обработчик события при успешном и не успешном соединении с таблицей
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<Category> allFood = new ArrayList<>();
                for(DataSnapshot obj : snapshot.getChildren()){     //перебираем все записи из таблизы и помещаем в obj
                    Category category = obj.getValue(Category.class);
                    allFood.add(category);
                }

                FoodListAdapter arrayAdapter = new FoodListAdapter(FoodPage.this, R.layout.food_item_in_list, allFood);
                //указали страницу активити, дизайн для каждого элемента списка, id поля куда будем размещать текст, какие конкретно записи размещаем
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FoodPage.this, "Нет интернет соединения", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
