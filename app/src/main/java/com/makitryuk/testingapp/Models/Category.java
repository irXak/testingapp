package com.makitryuk.testingapp.Models;

public class Category { // класс созданный для работы с таблицей Category

    private String image, name; // обращаемся к полям в нашей таблице (!!! важно чтобы регистр совпадал !!!)

    public Category() { // пустой конструктор
    }

    public Category(String image, String name) { // конструктор через который мы будем устанавливать переменные
        this.image = image;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
