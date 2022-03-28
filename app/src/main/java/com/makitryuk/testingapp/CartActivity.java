package com.makitryuk.testingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.makitryuk.testingapp.Helpers.JSONHelper;
import com.makitryuk.testingapp.Models.Cart;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        listView = findViewById(R.id.shopping_cart);
        List<Cart> cartList = JSONHelper.importFromJSON(this); // получаем все обьекты из нашего json файла

        if (cartList != null) {
            ArrayAdapter<Cart> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cartList); // используем встроенное оформление андроида
            listView.setAdapter(arrayAdapter);

            Toast.makeText(this, "Данные восстановлены", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Не удалось подгрузить данные", Toast.LENGTH_LONG).show();
        }
    }
}
