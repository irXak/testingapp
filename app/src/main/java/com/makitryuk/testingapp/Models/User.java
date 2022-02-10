package com.makitryuk.testingapp.Models;

public class User { // класс созданный для работы с таблицей User

    private String name, pass; // обращаемся к полям в нашей таблице (!!! важно чтобы регистр совпадал !!!)

    public User() { // пустой конструктор
    }

    public User(String name, String pass) { // конструктор через который мы будем устанавливать переменные
        this.name = name;
        this.pass = pass;
    }

    public String getName() { // метод с помощью которого мы получаем значение из переменной name
        return name;
    }

    public void setName(String name) { // метод с помощью которого мы устанавливаем значение переменной name
        this.name = name;
    }

    public String getPass() { // метод с помощью которого мы получаем значение из переменной pass
        return pass;
    }

    public void setPass(String pass) { // метод с помощью которого мы устанавливаем значение  переменной pass
        this.pass = pass;
    }
}
