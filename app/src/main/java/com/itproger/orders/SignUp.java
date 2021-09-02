package com.itproger.orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class SignUp extends AppCompatActivity {

    private BlurImageView blurImageView;

    private Button btnSignUpUser;
    private EditText editTextPassword, editTextPhone, editTextName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   //уберем верхнюю панель на экране в приложении - заряд, wi-fi и т.п.

        blurImageView = findViewById(R.id.blurImageView);
        blurImageView.setBlur(20);

        btnSignUpUser = findViewById(R.id.btnSignUpUser);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextName = findViewById(R.id.editTextName);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table = database.getReference("User");


        btnSignUpUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(editTextPhone.getText().toString()).exists()) {   //ищем в БД номер телефона такой же, который указал пользователь в приложении при регистрации
                            Toast.makeText(SignUp.this, "Такой номер телефона уже зарегистрирован", Toast.LENGTH_LONG).show();
                        } else {
                            User user = new User(editTextName.getText().toString(), editTextPassword.getText().toString());
                            table.child(editTextPhone.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this, "Успешная регистрация", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {         //метод срабатывает когда произошла ошибка, отмена и т.п
                        Toast.makeText(SignUp.this, "Нет интернет соединения", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
}
