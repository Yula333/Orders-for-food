package com.itproger.orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itproger.orders.Models.User;
import com.jgabrielfreitas.core.BlurImageView;

public class SignIn extends AppCompatActivity {

    private BlurImageView blurImageView;
    private Button btnSignInUser;
    private EditText editTextPassword;
    private EditText editTextPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   //уберем верхнюю панель на экране в приложении - заряд, wi-fi и т.п.

        blurImageView = findViewById(R.id.blurImageView);
        blurImageView.setBlur(20);

        btnSignInUser = findViewById(R.id.btnSignInUser);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPhone = findViewById(R.id.editTextPhone);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table = database.getReference("User");

        btnSignInUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table.addValueEventListener(new ValueEventListener() {      //проверяем смогли ли мы подключиться к таблице или нет
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {      //метод срабатывает когда произошли иззменения внутри таблицы из таблички ищем дочерний объект
                        if (snapshot.child(editTextPhone.getText().toString()).exists()) {      //у которого ключ будет таким же, который введет пользователь в поле номер телефона /editTextPhone/ в приложении
                            //если мы нашли пользователя в БД по введеному такому же номеру телефона, то оздаем новый объект
                            //на основе класса User и помещаем всю найденную запись из БД
                            User user = snapshot.child(editTextPhone.getText().toString()).getValue(User.class);
                                if (user.getPass().equals(editTextPassword.getText().toString())){
                                    setDefaults("phone", editTextPhone.getText().toString(), SignIn.this);  //в момент успешной авторизации в память телефона устанавливаем
                                    setDefaults("name", user.getName(), SignIn.this);  // новый ключ, сохраняем по ключу некоторые данные

                                    Toast.makeText(SignIn.this, "Успешная авторизация", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(SignIn.this, FoodPage.class);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(SignIn.this, "Авторизация не успешна", Toast.LENGTH_LONG).show();
                                }
                        } else {
                            Toast.makeText(SignIn.this, "Пользователь не найден", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {         //метод срабатывает когда произошла ошибка, отмена и т.п
                        Toast.makeText(SignIn.this, "Нет интернет соединения", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    public static void setDefaults(String key, String value, Context context){          //для сохранения авторизации
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getDefaults(String key, Context context){                      //для сохранения авторизации
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, null);
    }

}


