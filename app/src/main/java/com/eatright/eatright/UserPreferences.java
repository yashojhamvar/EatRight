package com.eatright.eatright;

/**
 * Created by srish on 4/7/2017.
 */

public class UserPreferences {
    //private int _id;
    private String _username;
    private String _mealtype;
    private String _avoidVeg;
    private String _avoidMeat;
    private String _conditions;
    private String _diets;
    private String _calories;

    public UserPreferences(String username, String meal_type, String avoidVeg, String avoidMeat, String conditions, String diets, String calories) {
        this._username = username;
        this._mealtype = meal_type;
        this._avoidVeg = avoidVeg;
        this._avoidMeat = avoidMeat;
        this._conditions = conditions;
        this._diets = diets;
        this._calories = calories;
    }

    public String get_username() {
        return this._username;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public String get_avoidVeg() {
        return this._avoidVeg;
    }

    public void set_avoidVeg(String _avoidVeg) {
        this._avoidVeg = _avoidVeg;
    }

    public String get_avoidMeat() {
        return this._avoidMeat;
    }

    public void set_avoidMeat(String _avoidMeat) {
        this._avoidMeat = _avoidMeat;
    }

    public String get_conditions() {
        return this._conditions;
    }

    public void set_conditions(String _conditions) {
        this._conditions = _conditions;
    }

    public String get_mealtype() {
        return this._mealtype;
    }

    public void set_mealtype(String _mealtype) {
        this._mealtype = _mealtype;
    }

    public String get_diets() {
        return this._diets;
    }

    public void set_diets(String _diets) {
        this._diets = _diets;
    }

    public String get_calories() {
        return this._calories;
    }

    public void set_calories(String _calories) {
        this._calories = _calories;
    }

}

