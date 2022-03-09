package com.makitryuk.testingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makitryuk.testingapp.Models.User;

public class Signin extends AppCompatActivity {

    private Button btnSignIn; // переменная ссылающаяся на кнопку SignIn
    private EditText editPhone, editPassword; // переменные ссылающиеся на поле ввода номера телефона и пароля


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
//-------------------ссылки для переменных--------------------------
        btnSignIn = findViewById(R.id.btnSignIn);
        editPhone = findViewById(R.id.editTextPhone);
        editPassword= findViewById(R.id.editTextPassword);
//----------------------END-----------------------------------------
//------Создаем обьект через который мы будем работать с базой Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // метод возвращающий подключение к бд
        final DatabaseReference table = database.getReference("User"); // а после подключения указываем с какой таблицей будем работать

        btnSignIn.setOnClickListener(new View.OnClickListener() {  // вешаем на кнопку обработчик событий ( по нажатию на кнопку "войти в кабинет" произойдет подключение к верной таблице)
            @Override
            public void onClick(View view) {
                table.addValueEventListener(new ValueEventListener() { // обращаемся к таблице и через обработчик событий проверяем смогли ли мы подключиться к таблице или нет
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {  // метод который срабатывает если в таблице произошли какие-либо изменения
                        if (snapshot.child(editPhone.getText().toString()).exists()) { // при корректном подключении к  таблице User, ищем телефон такой-же, что и введен в поле "Номер телефона" через дочерний метод child.  используя ключ введенный пользователем, затем приводим это к строковому состоянию и затем через метод exist проверяем существует ли такая запись
                            User user = snapshot.child(editPhone.getText().toString()).getValue(User.class); // если мы нашли пользователя по номеру телефона, то сполучаем польностью весь обьект со всеми записями и преобразовываем к классу User
                            if (user.getPass().equals(editPassword.getText().toString())) { // через метод equals проверяем введенный пользователем пароль с паролем что лежит в базе
                                Toast.makeText(Signin.this, "Успешно авторизован", Toast.LENGTH_LONG).show(); // если проверка на пароль вернула true
                                Intent intent = new Intent(Signin.this, FoodPage.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Signin.this, "Не успешно авторизован", Toast.LENGTH_LONG).show(); // если проверка на пароль вернула false
                            }
                        } else {
                            Toast.makeText(Signin.this, "Такого пользователя нет", Toast.LENGTH_LONG).show(); // если метод exist вернет false то выведется надпись об отсутствии такого пользователя
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { // метод который срабатывает когда у нас произошла какая-либо ошибка
                        Toast.makeText(Signin.this, "Нет интернет соединения", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
