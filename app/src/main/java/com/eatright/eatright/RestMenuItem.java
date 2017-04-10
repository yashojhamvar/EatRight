package com.eatright.eatright;

import java.util.ArrayList;

/**
 * Created by Yasho on 4/8/2017.
 */

public class RestMenuItem {

    private String dishName;        //name of dish
    private int dishType;        //type of dish- vegan - 0,vegetarian - 1 ,non vegetarian - 2
    private int lactose;         //Is the dish Lactose Intolerant 0/1
    private int[] calContent;       //array of calorie content divided over diff macros- carbs,protein,fats
    private int totCal;             //integer representing total calories
    private ArrayList<String> ingredients;   //array of all ingredients in the dish


    public RestMenuItem(String dishName, int dishType, int lactoseIndex, int[] calContent, int totCal, ArrayList<String> ingredients) {
        this.dishName = dishName;
        this.dishType = dishType;
        this.lactose = lactoseIndex;
        this.calContent = calContent;
        this.totCal = totCal;
        this.ingredients = ingredients;
    }

    public RestMenuItem(String dishName) {
        this.dishName = dishName;
    }

    public String getDishName() {
        return dishName;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public int[] getCalContent() {
        return calContent;
    }

    public int getTotCal() {
        return totCal;
    }

    public int getDishType() {
        return dishType;
    }

}
