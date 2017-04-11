package com.eatright.eatright;

import java.util.ArrayList;

/**
 * Created by Yasho on 4/8/2017.
 */

public class RestMenuItem {

    private String dishName;        //name of dish
    private String dishDesc;
    private String imageURL;
    private int dishType;        //type of dish- vegan - 0,vegetarian - 1 ,non vegetarian - 2
    private int lactose;         //Is the dish Lactose Intolerant 0/1
    private int gluten;
    private int[] calContent;       //array of calorie content divided over diff macros- carbs,protein,fats
    private int totCal;             //integer representing total calories
    private ArrayList<String> ingredients;   //array of all ingredients in the dish
    private int recommended;
    private ArrayList<String> reasons;

    public RestMenuItem(String dishName, String dishdesc, String imageUrl, int dishType, int lactoseIndex, int glutenIndex, int[] calContent, int totCal, ArrayList<String> ingredients, int reco, ArrayList<String> reason) {
        this.dishName = dishName;
        this.dishDesc = dishdesc;
        this.imageURL = imageUrl;
        this.dishType = dishType;
        this.lactose = lactoseIndex;
        this.gluten = glutenIndex;
        this.calContent = calContent;
        this.totCal = totCal;
        this.ingredients = ingredients;
        this.recommended = reco;
        this.reasons = reason;
    }

    public RestMenuItem(String dishName) {
        this.dishName = dishName;
    }

    public String getDishName() {
        return dishName;
    }

    public String getDishDesc() {
        return dishDesc;
    }

    public String getImageUrl() {
        return imageURL;
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

    public int getLactose() {
        return lactose;
    }

    public int getGluten() {
        return gluten;
    }

    public int getRecommended() {
        return recommended;
    }

    public ArrayList<String> getReasons() {
        return reasons;
    }

}
