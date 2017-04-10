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

    public UserPreferences(String username, String meal_type, String avoidVeg, String avoidMeat, String conditions) {
        //Get username from Yasho
        //this._username = <from firebase>
        this._username = username;
        this._mealtype = meal_type;
        this._avoidVeg = avoidVeg;
        this._avoidMeat = avoidMeat;
        this._conditions = conditions;
    }

    public String get_username() {
        return this._username;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public String get_meal_type() {
        return this._mealtype;
    }

    public void set_meal_type(String _meal_type) {
        this._mealtype = _meal_type;
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


}

