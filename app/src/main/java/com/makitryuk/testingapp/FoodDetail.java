package com.makitryuk.testingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makitryuk.testingapp.Helpers.JSONHelper;
import com.makitryuk.testingapp.Models.Cart;
import com.makitryuk.testingapp.Models.Category;
import com.makitryuk.testingapp.Models.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodDetail extends AppCompatActivity {

    public  static int ID = 0; // это переменная в которую будет подставляться позиция элемента на который мы нажали
    private ImageView mainPhoto;
    private TextView foodMainName, price, foodFullName;
    private Button btnGoToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        mainPhoto = findViewById(R.id.mainFoto);
        foodMainName = findViewById(R.id.foodMainName);
        price = findViewById(R.id.price);
        foodFullName = findViewById(R.id.foodFullName);
        btnGoToCart = findViewById(R.id.btnGoToCart);

        btnGoToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FoodDetail.this, CartActivity.class));
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance(); // метод возвращающий подключение к бд
        final DatabaseReference table = database.getReference("Category"); // а после подключения указываем с какой таблицей будем работать

        table.child(String.valueOf(ID)).addValueEventListener(new ValueEventListener() { // мы находим дочерний обьект у которого ключ будет точно таким же как и значение которое помещено внутрь переменной ID
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { // метод который срабатывает если в таблице произошли какие-либо изменения
                Category category = snapshot.getValue(Category.class); // получаем нужный для нас обьект из таблички категории

                foodMainName.setText(category.getName()); // устанавливаем название категории в самое верхнее текстовое поле

                int id = getApplicationContext().getResources().getIdentifier("drawable/" + category.getImage(), null, getApplicationContext().getPackageName()); // устанавливаем картинку в описание выбранной категории
                mainPhoto.setImageResource(id); // указываем id той картинки которую мы хотим установить
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { // метод который срабатывает когда у нас произошла какая-либо ошибка
                Toast.makeText(FoodDetail.this, "Нет интернет соединения", Toast.LENGTH_LONG).show();
            }
        });

        final DatabaseReference table_food = database.getReference("Food"); // создаем новую переменную которая будет ссылаться на табличку еда
        table_food.child(String.valueOf(ID)).addValueEventListener(new ValueEventListener() { // мы находим дочерний обьект у которого ключ будет точно таким же как и значение которое помещено внутрь переменной ID
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { // метод который срабатывает если в таблице произошли какие-либо изменения
                Food foodItem = snapshot.getValue(Food.class);  // по пока неизвестной причине именно  в этой строку получаемое значение из бд ломает приложение

               price.setText(foodItem.getPrice() + "рублей"); // цена
                foodFullName.setText(foodItem.getFull_text()); // описание
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { // метод который срабатывает когда у нас произошла какая-либо ошибка
                Toast.makeText(FoodDetail.this, "Нет интернет соединения", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void btnAddToCart(View view) { // обработчик нажатия на кнопку "Добавить в корзину"
        List<Cart> cartList = JSONHelper.importFromJSON(this);
        if (cartList == null) { // если у нас список корзины пуст то
            cartList = new ArrayList<>(); //  мы выделяем память под пустой список
            cartList.add(new Cart(ID, 1)); // указываем сколько пользователь добавит товаров за одно нажатие
        } else { // перебираем список и если мы находимся на категории которая уже выбрана то к кол-во увеличится на 1
            boolean isFound = false;
            for (Cart el: cartList) {
                if (el.getCategoryID() == ID) {
                    el.setAmount(el.getAmount() + 1);
                    isFound = true;
                }
            }

            if (!isFound) // добавление другой категории в корзину
                cartList.add(new Cart(ID, 1));
        }

        boolean result = JSONHelper.exportToJSON(this, cartList);
        if (result) {
            Toast.makeText(this, "Добавлено", Toast.LENGTH_LONG).show();
            Button btnCart = (Button) view;// для изменения кнопки мы записываем "вью" в отдельный обьект и преобразовываем к классу "кнопка"
            btnCart.setText("Добавлено"); //при успешном добавлении товара текст на кнопке изменится на добавлено

        } else {
            Toast.makeText(this, "Не добавлено", Toast.LENGTH_LONG).show();
        }

    }
}
