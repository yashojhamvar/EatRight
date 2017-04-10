package com.eatright.eatright;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class FoodPreferences extends AppCompatActivity {
    ArrayList<String> mealType_list = new ArrayList<String>();
    ArrayList<String> avoidVeg_list = new ArrayList<String>();
    ArrayList<String> avoidMeat_list = new ArrayList<String>();
    ArrayList<String> conditions_list = new ArrayList<String>();

    //private SQLiteDatabase sqLiteDatabase;
    MyDBHandler dbHandler;
    //private Cursor cursor;
    public static String USERNAME;
    public ArrayList<String> dbRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        USERNAME = getIntent().getExtras().getString("username");
        Toast.makeText(this, "UserName is : " + USERNAME, Toast.LENGTH_SHORT).show();

        /* *******************************************************************
        *                       Query and Show records on page load
        * ******************************************************************* */
        dbHandler = new MyDBHandler(this, null, null, 1);
        /*SQLiteDatabase sqLiteDatabase = dbHandler.getReadableDatabase();
        sqLiteDatabase = openOrCreateDatabase("eatRightDB", Context.MODE_PRIVATE, null);
        int dbVersion = sqLiteDatabase.getVersion();*/
        Toast.makeText(this, "before retrieveData", Toast.LENGTH_LONG).show();
        retrieveData();
        setCheckboxState();


        //(Context, String Name,CursorFactory factory,int version)
        dbHandler.close();
        dbHandler = new MyDBHandler(this, null, null, 1);
        final Button BTN_SAVE = (Button) findViewById(R.id.btn_save);
        BTN_SAVE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                savePreferences(view);
            }
        });
    }

    /* *******************************************************************
    * Parameters: none
    * Return: none
    * Description: Queries all existing records in DB
    * Author: Srishti Hunjan
    * Date: 4/8/2017
    * ******************************************************************* */
    public void retrieveData() {
        dbRecords = dbHandler.retrievePreferences();
        if (dbRecords != null) {
            Toast.makeText(this, "Retrieved data= " + dbRecords, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Retrieved data = NULL", Toast.LENGTH_LONG).show();
        }

    }

    /* *******************************************************************
    * Parameters: none
    * Return: none
    * Description: Loads preexisting user preferences
    * Author: Srishti Hunjan
    * Date: 4/9/2017
    * ******************************************************************* */
    public void setCheckboxState() {
        ArrayList<String> fetchedRecords = dbRecords;
        for (String record : fetchedRecords) {
            String[] separated = record.split(",");
            for (String item : separated) {
                if (item.equalsIgnoreCase("Vegetarian")) {

                }
            }
            //separated[0];
            //separated[1];
        }
    }

    /* *******************************************************************
    * Parameters: view state
    * Return: none
    * Description: Populates lists with selected Checkboxes
    * Author: Srishti Hunjan
    * Date: 4/9/2017
    * ******************************************************************* */
    public void selectItem(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            // Meal Type Preference
            case R.id.mealType_nonveg:
                if (checked)
                    mealType_list.add("Non-Vegetarian Meals");
                else
                    mealType_list.remove("Non-Vegetarian Meals");
                break;
            case R.id.mealType_veg:
                if (checked)
                    mealType_list.add("Vegetarian Meals");
                else
                    mealType_list.remove("Vegetarian Meals");
                break;
            case R.id.mealType_vegan:
                if (checked)
                    mealType_list.add("Vegan Meals");
                else
                    mealType_list.remove("Vegan Meals");
                break;

            // Avoid which Veggies

            case R.id.avoidVeg_corn:
                if (checked)
                    avoidVeg_list.add("Corn");
                else
                    avoidVeg_list.remove("Corn");
                break;
            case R.id.avoidVeg_onions:
                if (checked)
                    avoidVeg_list.add("Onion");
                else
                    avoidVeg_list.remove("Onion");
                break;
            case R.id.avoidVeg_lettuce:
                if (checked)
                    avoidVeg_list.add("Lettuce");
                else
                    avoidVeg_list.remove("Lettuce");
                break;
            case R.id.avoidVeg_tomato:
                if (checked)
                    avoidVeg_list.add("Tomato");
                else
                    avoidVeg_list.remove("Tomato");
                break;

            // Avoid which Meats

            case R.id.avoidMeat_chicken:
                if (checked)
                    avoidMeat_list.add("Chicken");
                else
                    avoidMeat_list.remove("Chicken");
                break;
            case R.id.avoidMeat_fish:
                if (checked)
                    avoidMeat_list.add("Fish");
                else
                    avoidMeat_list.remove("Fish");
                break;
            case R.id.avoidMeat_pork:
                if (checked)
                    avoidMeat_list.add("Pork");
                else
                    avoidMeat_list.remove("Pork");
                break;

            // User Conditions to consider

            case R.id.userConditions_diabetes:
                if (checked)
                    conditions_list.add("Diabetes");
                else
                    conditions_list.remove("Diabetes");
                break;
            case R.id.userConditions_glutenIntolerance:
                if (checked)
                    conditions_list.add("Gluten Intolerance");
                else
                    conditions_list.remove("Gluten Intolerance");
                break;
            case R.id.userConditions_lactoseIntolerance:
                if (checked)
                    conditions_list.add("Lactose Intolerance");
                else
                    conditions_list.remove("Lactose Intolerance");
                break;
        }
    }

    public void savePreferences(View view) {
        //(String username, String meal_type, String avoidVeg, String avoidMeat, String conditions)
        UserPreferences userPreferences = new UserPreferences(USERNAME, mealType_list.toString(), avoidVeg_list.toString(), avoidMeat_list.toString(), conditions_list.toString());
        int recordCount = dbHandler.addPreferences(userPreferences);
        String testStr = "Preferences Saved!! : " + String.valueOf(recordCount);
        Toast.makeText(this, testStr, Toast.LENGTH_LONG).show();
        Intent mainPage = new Intent(this, MainActivity.class);
        startActivity(mainPage);
    }
}

