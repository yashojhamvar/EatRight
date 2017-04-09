package com.eatright.eatright;

/**
 * Created by Yasho on 4/8/2017.
 */

public class RestMenuItem {

    private String dishName;        //name of dish
    private int dishType;        //type of dish- vegan - 0,vegetarian - 1 ,non vegetarian - 2
    private int lactose;         //Is the dish Lactose Intolerant 0/1
    private int[] calContent;       //array of calorie content divided over diff macros- carbs,protein,fats
    private int totCal;             //integer representing total calories
    private String[] ingredients;   //array of all ingredients in the dish


    public RestMenuItem(String dishName, int dishType, int lactoseIndex, int[] calContent, int totCal, String[] ingredients) {
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

    public String name() {
        return dishName;
    }        //name of dish

    public String[] ingredients() {
        return ingredients;
    }   //array of all ingredients in the dish

    public int[] calContent() {
        return calContent;
    }      //array of calorie content divided over diff macros- carbs,protein,fats

    public int totCal() {
        return totCal;
    }             //integer representing total calories

    public int dishType() {
        return dishType;
    }        //type of dish- vegan,vegetarian,non vegetarian

}
