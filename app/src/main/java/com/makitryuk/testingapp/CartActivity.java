package com.makitryuk.testingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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
import com.makitryuk.testingapp.Helpers.CartItemsAdapter;
import com.makitryuk.testingapp.Helpers.JSONHelper;
import com.makitryuk.testingapp.Models.Cart;
import com.makitryuk.testingapp.Models.Order;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private ListView listView;
    private Button btnMakeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        btnMakeOrder = findViewById(R.id.btnMakeOrder);
        listView = findViewById(R.id.shopping_cart);
        List<Cart> cartList = JSONHelper.importFromJSON(this); // получаем все обьекты из нашего json файла

        if (cartList != null) {
            CartItemsAdapter arrayAdapter = new CartItemsAdapter(CartActivity.this, R.layout.cart_item, cartList);
            listView.setAdapter(arrayAdapter);

            Toast.makeText(this, "Данные восстановлены", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Не удалось подгрузить данные", Toast.LENGTH_LONG).show();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance(); // метод возвращающий подключение к бд
        final DatabaseReference table = database.getReference("Order"); // а после подключения указываем с какой таблицей будем работать

        btnMakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Cart> cartList = JSONHelper.importFromJSON(CartActivity.this);
                        if (cartList == null) {
                            Toast.makeText(CartActivity.this, "Корзина не сформирована", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String activeUser = Signin.getDefaults("phone", CartActivity.this); // получаем номер телефона
                        Order order = new Order(JSONHelper.createJSONString(cartList), activeUser); // в этой переменной будет храниться номер телефона

                        Long tsLong = System.currentTimeMillis() / 1000; //получили колличесво миллисекунд с определенной даты (1977 год вроде), поделили на тысячу чтоб получать в все в секундах

                        table.child(tsLong.toString()).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                List<Cart> cartList = new ArrayList<>();
                                JSONHelper.exportToJSON(CartActivity.this, cartList);

                                CartItemsAdapter arrayAdapter = new CartItemsAdapter(CartActivity.this, R.layout.cart_item, cartList);
                                listView.setAdapter(arrayAdapter);

                                Toast.makeText(CartActivity.this, "Заказ сформирован", Toast.LENGTH_LONG).show();

                            }
                        }); // получившееся время является ключем к нашей записи  и получаем значения из обьекта ордер

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}
