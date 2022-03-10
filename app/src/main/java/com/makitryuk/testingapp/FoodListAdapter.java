package com.makitryuk.testingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.makitryuk.testingapp.Models.Category;

import java.util.List;

public class FoodListAdapter extends ArrayAdapter<Category> { // мы все будем наследовать от класса эррей адаптер и в качестве типа данный указываем ту модель с которой будем взаимодействовать

    private LayoutInflater layoutInflater; // обьект для работы с xml файлом
    private List<Category> categories;
    private  int layoutListRow;
    private Context context;


    public FoodListAdapter(@NonNull Context context, int resource, @NonNull List<Category> objects) { // создаем конструктор принимающий три параметра
        super(context, resource, objects);
        /* теперь когда мы будем создавать обьект на основе этого класса, то у нас по умолчанию будет вызываться этот конструктор
         ( просто потому что других конструткоров нет XD ) и при создании обьекта мы будем принимать 3 параметра:
         контекст - это внутри какого классам мы создаем параметр в нашем случае это FoodPage.this,
         во второй параметр будем передавать id  того дизайна который будет использоваться для формирования списка для нас это food_item_in_list,
         а третий параметр это список всех тех обьектов за счет которых мы и сформируем наш лист вью  */

        categories = objects; // (1-й параметр конструктора) внутри этой переменной мы будем хранить все те обьекты что будут служить для наполнения списка
        layoutListRow = resource; // (2-й параметр конструктора) в этой переменной мы будем хранить id того дизайна который нам будет нужен для создания самого списка
        this.context = context; // внутри конструктора внутрь этого обьекта мы будем устанавливать тот параметр который получаем при создании обьекта на основе этого класса

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

        Category category = categories.get(position); /* берем обьект из нашего списка категорий и берем его по параметру позиция
         а с учетом что этот метод вызывается каждый раз при формировании нового ряда
         то наш позишен будет изначально с цифрой 0 и это мы сформируем первый ряд, затем 1,2,3 и тд.  */
        if (category != null) { // пропишем одну проверку сугубо для того чтобы обезопасить себя
            // если категори не является нул и обьект действительно был добавлен то тогда работаем с обьектом
            TextView foofName = convertView.findViewById(R.id.foodMainName); // создаем обьект иобращаемся к установленному дизайну и внутри этого дизайна ищем ообьект имя которого будет как фудМейнНейм
            ImageView photo = convertView.findViewById(R.id.mainFoto);

            if (foofName != null)
                foofName.setText(category.getName()); // Внутрь тектовой надписи мы подставим тот текст который у нас будет для конкретного обьекта

            if (photo != null) {
                //для этого мы создаем дополнительную переменную которая будет искать картинку по названию
                int id = getContext().getResources().getIdentifier("drawable/" + category.getImage(), null, getContext().getPackageName()); // заменяем атрибут src
                photo.setImageResource(id); //передаем полученный id

                photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FoodDetail.ID = position + 1; // обращаясь к индексу элемента на который нажали мы будем заносить позицию этого элемента в переменную ID (т.к. по индексу элементы считываются с нуля то к индексуц прибавляем единицу для корректности отображения
                        context.startActivity(new Intent(context, FoodDetail.class)); //по нажатию на любую категорию из списка мы будем переадресовывать пользователя на страничку с описанием выбранной категории
                        //Toast.makeText(getContext(), foofName.getText().toString(), Toast.LENGTH_LONG).show(); // получаем во всплывающем сообщении по нажатию на любую категорию название этой категории
                    }
                });
            }
        }

        return convertView;
    }
}
