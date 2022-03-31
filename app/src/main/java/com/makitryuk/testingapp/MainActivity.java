package com.makitryuk.testingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.makitryuk.testingapp.Models.MyThread;

public class MainActivity extends AppCompatActivity {

    //private static final String TAG = "MainActivity";    //рекламный тэг

    private Button btnSignIn, btnSignUp;
    private static final String TAG = "shaman";
    Button startButton, stopButton;
    MyThread myThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startThreadRoutine();

        String activeUser = Signin.getDefaults("phone", MainActivity.this); // проверяем авторизован ли пользователь и если он авторизован то показывать страничку фуд_пейдж

        if (activeUser != null && activeUser.equals("")) {  // создадим условие: если эктив юзер не будет равен пустой строке, то перебрасываем пользователя на другую страницу
            Intent intent = new Intent(MainActivity.this, FoodPage.class);
            startActivity(intent);
        }
//--------------------вызов рекламного блока----------------------

//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);  //Пока гугл в блоке не активно
//---------------------END----------------------------------------------------

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Signin.class); // прописываем переход между активити из main  в signin
                startActivity(intent); // при нажатии на кнопку авторизация мы будем переходить в активити с вводом номера телефона и пароля
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUp.class); // прописываем переход между активити из main  в signin
                startActivity(intent); // при нажатии на кнопку авторизация мы будем переходить в активити с вводом номера телефона и пароля
            }
        });
    }

    public void startThreadRoutine(){
        Log.d(TAG, ">>>> startThreadRoutine called...");
        this.myThread = new MyThread(this);
        this.myThread.start();
    }

    public void onDestroy() {
        super.onDestroy();
        this.myThread.stop();
    }
}

