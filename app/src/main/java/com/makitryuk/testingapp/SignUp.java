package com.makitryuk.testingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class SignUp extends AppCompatActivity {

    private Button btnSignUp; // переменная ссылающаяся на кнопку SignIn
    private EditText editPhone, editName, editPassword; // переменные ссылающиеся на поле ввода номера телефона и пароля

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //-------------------ссылки для переменных--------------------------
        btnSignUp = findViewById(R.id.btnSignUp);
        editPhone = findViewById(R.id.editTextPhone);
        editName = findViewById(R.id.editTextName);
        editPassword= findViewById(R.id.editTextPassword);
//----------------------END-----------------------------------------
//------Создаем обьект через который мы будем работать с базой Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // метод возвращающий подключение к бд
        final DatabaseReference table = database.getReference("User"); // а после подключения указываем с какой таблицей будем работать

        btnSignUp.setOnClickListener(new View.OnClickListener() {  // вешаем на кнопку обработчик событий ( по нажатию на кнопку "войти в кабинет" произойдет подключение к верной таблице)
            @Override
            public void onClick(View view) {
                table.addValueEventListener(new ValueEventListener() { // обращаемся к таблице и через обработчик событий проверяем смогли ли мы подключиться к таблице или нет
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {  // метод который срабатывает если в таблице произошли какие-либо изменения
                        if (snapshot.child(editPhone.getText().toString()).exists()) { // проверка по номеру
                            Toast.makeText(SignUp.this, "Такой пользователь уже есть", Toast.LENGTH_LONG).show();
                        } else {
                            User user = new User(editName.getText().toString(), editPassword.getText().toString()); //создаем обьект содержащий имя и пароль
                            table.child(editPhone.getText().toString()).setValue(user); // создаем новый дочерний обьект по номеру полученному из текстового поля (указывая ключ телефон) и указываем значения которые мы хотим поместить внутрь этого обьекта (name и password полученные из user)
                            Toast.makeText(SignUp.this, "Успешная регистрация", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { // метод который срабатывает когда у нас произошла какая-либо ошибка
                        Toast.makeText(SignUp.this, "Нет интернет соединения", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
