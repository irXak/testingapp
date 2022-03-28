package com.makitryuk.testingapp.Helpers;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makitryuk.testingapp.FoodDetail;
import com.makitryuk.testingapp.Models.Cart;
import com.makitryuk.testingapp.Models.Category;
import com.makitryuk.testingapp.R;

import java.util.List;

public class CartItemsAdapter extends ArrayAdapter<Cart> {

    private LayoutInflater layoutInflater; // обьект для работы с xml файлом
    private List<Cart> cartList;
    private  int layoutListRow;

    public CartItemsAdapter(@NonNull Context context, int resource, @NonNull List<Cart> objects) {
        super(context, resource, objects);

        cartList = objects; // (1-й параметр конструктора) внутри этой переменной мы будем хранить все те обьекты что будут служить для наполнения списка
        layoutListRow = resource; // (2-й параметр конструктора) в этой переменной мы будем хранить id того дизайна который нам будет нужен для создания самого списка


        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); /*  мы указываем переданный контекст и затем указываем режим
         в котором инфлейтер будет работать а дальше указываем сам сервис работы...  */
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // нам необходимо переписывать значение для конверт вью ведь это переменная содержит значения того дизайна который будет использован для каждого элемента внутри списка
        convertView = layoutInflater.inflate(layoutListRow, null); /* обращаемся к методу инфлейтер и указываем id  с которым мы хотим работать (а он будет записан в ЛейаутЛистРоу)
         а второй параметр нам не нужен поэтому передваем нул*/

        Cart cart = cartList.get(position); /* берем обьект из нашего списка и берем его по параметру позиция
         а с учетом что этот метод вызывается каждый раз при формировании нового ряда
         то наш позишен будет изначально с цифрой 0 и это мы сформируем первый ряд, затем 1,2,3 и тд.  */
        if (cart != null) { // пропишем одну проверку сугубо для того чтобы обезопасить себя
            // если категори не является нул и обьект действительно был добавлен то тогда работаем с обьектом
            final TextView productName = convertView.findViewById(R.id.productName); //
            TextView amount = convertView.findViewById(R.id.amount); //
            ImageView photo = convertView.findViewById(R.id.mainFoto);

            if (amount != null)
                amount.setText(String.valueOf(cart.getAmount())); // Внутрь тектовой надписи мы подставим тот текст который у нас будет для конкретного обьекта

            if (productName != null) {
                FirebaseDatabase database = FirebaseDatabase.getInstance(); // метод возвращающий подключение к бд
                final DatabaseReference table = database.getReference("Category"); // а после подключения указываем с какой таблицей будем работать

                table.child(String.valueOf(cart.getCategoryID())).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Category category = snapshot.getValue(Category.class);
                        productName.setText(category.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }

        return convertView;
    }

}
