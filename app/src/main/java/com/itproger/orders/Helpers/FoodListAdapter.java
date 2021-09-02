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

import com.itproger.orders.FoodDetail;
import com.itproger.orders.Models.Category;
import com.itproger.orders.R;

import java.util.List;

public class FoodListAdapter extends ArrayAdapter<Category> {

    private LayoutInflater layoutInflater;      //этот объект обеспечивает работу внутри .xml файла, у нас food_item_in_list.xml
    private List<Category> categories;          //внутри переменной будут храниться все объекты, для наполнения самого списка
    private int layoutListRow;                  //в этой переменной будет id дизайна для создания самого списка
    private Context context;

    public FoodListAdapter(@NonNull Context context, int resource, @NonNull List<Category> objects) {
        super(context, resource, objects);
        this.context = context;
        categories = objects;
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
        Category category = categories.get(position);
        if(category != null) {
            final TextView foodName = convertView.findViewById(R.id.foodMainName);
            ImageView photo = convertView.findViewById(R.id.mainPhoto);

            if(foodName != null)
                foodName.setText(category.getName());

            if(photo !=null) {
                int id = getContext().getResources().getIdentifier("drawable/" + category.getImage(), null, getContext().getPackageName());
                photo.setImageResource(id);

                photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FoodDetail.ID = position +1;
                        context.startActivity(new Intent(context, FoodDetail.class));
//                        Toast.makeText(getContext(), foodName.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
        return convertView;
    }
}
