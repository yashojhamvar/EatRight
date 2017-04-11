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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class FoodPreferences extends AppCompatActivity {
    ArrayList<String> mealType_list = new ArrayList<String>();
    ArrayList<String> avoidVeg_list = new ArrayList<String>();
    ArrayList<String> avoidMeat_list = new ArrayList<String>();
    ArrayList<String> conditions_list = new ArrayList<String>();
    ArrayList<String> diets_list = new ArrayList<String>();
    ArrayList<String> calorie_list = new ArrayList<String>();

    MyDBHandler dbHandler;
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

       /*  *******************************************************************
        *                       Query and Show records on page load
        * ******************************************************************* */
        dbHandler = new MyDBHandler(this, null, null, 1);
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
        CheckBox mealType_nonveg = (CheckBox) findViewById(R.id.mealType_nonveg);
        CheckBox mealType_veg = (CheckBox) findViewById(R.id.mealType_veg);
        CheckBox mealType_vegan = (CheckBox) findViewById(R.id.mealType_vegan);
        CheckBox avoidMeat_chicken = (CheckBox) findViewById(R.id.avoidMeat_chicken);
        CheckBox avoidMeat_pork = (CheckBox) findViewById(R.id.avoidMeat_pork);
        CheckBox avoidMeat_fish = (CheckBox) findViewById(R.id.avoidMeat_fish);
        CheckBox avoidMeat_beef = (CheckBox) findViewById(R.id.avoidMeat_beef);
        CheckBox avoidVeg_onions = (CheckBox) findViewById(R.id.avoidVeg_onions);
        CheckBox avoidVeg_corn = (CheckBox) findViewById(R.id.avoidVeg_corn);
        CheckBox avoidVeg_lettuce = (CheckBox) findViewById(R.id.avoidVeg_lettuce);
        CheckBox avoidVeg_tomato = (CheckBox) findViewById(R.id.avoidVeg_tomato);
        CheckBox avoidVeg_diary = (CheckBox) findViewById(R.id.avoidVeg_diary);
        CheckBox avoidVeg_wheat = (CheckBox) findViewById(R.id.avoidVeg_wheat);
        CheckBox userConditions_diabetes = (CheckBox) findViewById(R.id.userConditions_diabetes);
        CheckBox userConditions_lactoseIntolerance = (CheckBox) findViewById(R.id.userConditions_lactoseIntolerance);
        CheckBox userConditions_glutenIntolerance = (CheckBox) findViewById(R.id.userConditions_glutenIntolerance);
        CheckBox userConditions_nutAllergy = (CheckBox) findViewById(R.id.userConditions_nutAllergy);
        CheckBox userConditions_highBloodPressure = (CheckBox) findViewById(R.id.userConditions_highBloodPressure);
        CheckBox diet_keto = (CheckBox) findViewById(R.id.diet_keto);
        CheckBox diet_helal = (CheckBox) findViewById(R.id.diet_helal);


        ArrayList<String> fetchedRecords = dbRecords;
        for (
                String record : fetchedRecords)

        {
            String[] separated = record.split(",");
            for (String item : separated) {
                if (item.equalsIgnoreCase("Non-Vegetarian Meals")) {
                    mealType_nonveg.setEnabled(true);
                }
                if (item.equalsIgnoreCase("Vegetarian Meals")) {
                    mealType_veg.setEnabled(true);
                }
                if (item.equalsIgnoreCase("Vegan Meals")) {
                    mealType_vegan.setEnabled(true);
                }
                if (item.equalsIgnoreCase("Chicken")) {
                    avoidMeat_chicken.setEnabled(true);
                }
                if (item.equalsIgnoreCase("Pork")) {
                    avoidMeat_pork.setEnabled(true);
                }
                if (item.equalsIgnoreCase("Fish")) {
                    avoidMeat_fish.setEnabled(true);
                }
                if (item.equalsIgnoreCase("Beef")) {
                    avoidMeat_beef.setEnabled(true);
                }
                if (item.equalsIgnoreCase("Onions")) {
                    avoidVeg_onions.setEnabled(true);
                }
                if (item.equalsIgnoreCase("Corn")) {
                    avoidVeg_corn.setEnabled(true);
                }
                if (item.equalsIgnoreCase("Tomatoes")) {
                    avoidVeg_tomato.setEnabled(true);
                }
                if (item.equalsIgnoreCase("Dairy")) {
                    avoidVeg_diary.setEnabled(true);
                }
                if (item.equalsIgnoreCase("Wheat")) {
                    avoidVeg_wheat.setEnabled(true);
                }
                if (item.equalsIgnoreCase("Diabetes")) {
                    userConditions_diabetes.setEnabled(true);
                }
                if (item.equalsIgnoreCase("Lactose Intolerance")) {
                    userConditions_lactoseIntolerance.setEnabled(true);
                }
                if (item.equalsIgnoreCase("Gluten Intolerance")) {
                    userConditions_glutenIntolerance.setEnabled(true);
                }
                if (item.equalsIgnoreCase("Nut Allergy")) {
                    userConditions_nutAllergy.setEnabled(true);
                }
                if (item.equalsIgnoreCase("High Blood Pressure")) {
                    userConditions_highBloodPressure.setEnabled(true);
                }
                if (item.equalsIgnoreCase("Ketogenic Diet")) {
                    diet_keto.setEnabled(true);
                }
                if (item.equalsIgnoreCase("Dr. Sumi Helal Diet")) {
                    diet_helal.setEnabled(true);
                }
            }
        }
    }

    /*  *******************************************************************
     * Parameters: view state
     * Return: none
     * Description: Populates lists with selected Checkboxes
     * Author: Srishti Hunjan
     * Date: 4/9/2017
     * ********************************************************************/
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
                    avoidVeg_list.add("Tomatoes");
                else
                    avoidVeg_list.remove("Tomatoes");
                break;
            case R.id.avoidVeg_diary:
                if (checked)
                    avoidVeg_list.add("Dairy");
                else
                    avoidVeg_list.remove("Dairy");
                break;
            case R.id.avoidVeg_wheat:
                if (checked)
                    avoidVeg_list.add("Wheat");
                else
                    avoidVeg_list.remove("Wheat");
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
            case R.id.avoidMeat_beef:
                if (checked)
                    avoidMeat_list.add("Beef");
                else
                    avoidMeat_list.remove("Beef");
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
            case R.id.userConditions_nutAllergy:
                if (checked)
                    conditions_list.add("Nut Allergy");
                else
                    conditions_list.remove("Nut Allergy");
                break;
            case R.id.userConditions_highBloodPressure:
                if (checked)
                    conditions_list.add("High Blood Pressure");
                else
                    conditions_list.remove("High Blood Pressure");
                break;
        }
    }

    public void savePreferences(View view) {
        EditText constraint_Protein = (EditText) findViewById(R.id.constraint_Protein);
        final EditText constraint_carbs = (EditText) findViewById(R.id.constraint_carbs);
        final EditText constraint_sugar = (EditText) findViewById(R.id.constraint_sugar);
        final EditText constraint_calories = (EditText) findViewById(R.id.constraint_calories);
        final EditText constraint_fat = (EditText) findViewById(R.id.constraint_fat);

        //(String username, String meal_type, String avoidVeg, String avoidMeat, String conditions, String diets)
        calorie_list.add(constraint_Protein.toString());
        calorie_list.add(constraint_carbs.toString());
        calorie_list.add(constraint_fat.toString());
        calorie_list.add(constraint_calories.toString());
        calorie_list.add(constraint_sugar.toString());
        UserPreferences userPreferences = new UserPreferences(USERNAME, mealType_list.toString(), avoidVeg_list.toString(), avoidMeat_list.toString(), conditions_list.toString(), diets_list.toString(), calorie_list.toString());
        int recordCount = dbHandler.addPreferences(userPreferences);
        String testStr = "Preferences Saved!! : " + String.valueOf(recordCount);
        Toast.makeText(this, testStr, Toast.LENGTH_LONG).show();
        Intent mainPage = new Intent(this, MainActivity.class);
        startActivity(mainPage);
    }
}
