package com.makitryuk.testingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makitryuk.testingapp.Helpers.FoodListAdapter;
import com.makitryuk.testingapp.Models.Category;

import java.util.ArrayList;

public class FoodPage extends AppCompatActivity {

    private ListView listView; // создаем переменную которая будет ссылаться на список

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_page);

        listView = findViewById(R.id.list_of_food); // создаем ссылку на необходимый нам обьект

        FirebaseDatabase database = FirebaseDatabase.getInstance(); // метод возвращающий подключение к бд
        final DatabaseReference table = database.getReference("Category"); // а после подключения указываем с какой таблицей будем работать

        table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<Category> allFood = new ArrayList<>(); // создаем список внутрь которого будем помещать все наши категории
                for(DataSnapshot obj : snapshot.getChildren()) { // создаем цикл внутри которого создаем обьект на основе класса DataSnapshot и внутрь этого обьекта мы каждый раз будем подставлять новую запись полученную из таблички категории
                    Category category = obj.getValue(Category.class); // создаем обьект на основе класса категория и в него помещаем данные полученные из obj используя метод getValue
                    allFood.add(category); // помещаем внутрь нашего списка все те названия что есть в нашей табличке Category
                }
                /*
                Создаем обьект который позволит нам заполнить список указываем тип данных затем необходимо указать класс с которым мы работаем,
                затем указываем дизайн который мы будем использовать и далее указываем внутрь какой текстовой надписи мы будем помещать текст
                + данные записанные в обьекте allFood
                 */
                FoodListAdapter arrayAdapter = new FoodListAdapter(FoodPage.this, R.layout.food_item_in_list, allFood);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  // метод который срабатывает когда у нас произошла какая-либо ошибка
                Toast.makeText(FoodPage.this, "Нет интернет соединения", Toast.LENGTH_LONG).show();

            }
        });
    }
}
