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
import java.util.Iterator;

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

       /*  *******************************************************************
        *                       Query and Show records on page load
        * ******************************************************************* */
        //(Context, String Name,CursorFactory factory,int version)
        dbHandler = new MyDBHandler(this, null, null, 2);
        retrieveData();

        EditText constraint_Protein = (EditText) findViewById(R.id.constraint_Protein);
        EditText constraint_carbs = (EditText) findViewById(R.id.constraint_carbs);
        EditText constraint_sugar = (EditText) findViewById(R.id.constraint_sugar);
        EditText constraint_fat = (EditText) findViewById(R.id.constraint_fat);
        EditText constraint_calories = (EditText) findViewById(R.id.constraint_calories);

        if ((!dbRecords.isEmpty()) && (dbRecords != null) && dbRecords.size() > 6) {
            String calories = dbRecords.get(6);
            String[] str_list = calories.split(",");
            if (str_list[0] != null || !str_list[0].isEmpty()) {
                constraint_Protein.setText(str_list[0], TextView.BufferType.EDITABLE);
                if (str_list[0].equalsIgnoreCase("999"))
                    constraint_Protein.setText("", TextView.BufferType.EDITABLE);
            }
            if (str_list[1] != null || !str_list[1].isEmpty()) {
                constraint_carbs.setText(str_list[1], TextView.BufferType.EDITABLE);
                if (str_list[1].equalsIgnoreCase("999"))
                    constraint_carbs.setText("", TextView.BufferType.EDITABLE);
            }
            if (str_list[2] != null || !str_list[2].isEmpty()) {
                constraint_sugar.setText(str_list[2], TextView.BufferType.EDITABLE);
                if (str_list[2].equalsIgnoreCase("999"))
                    constraint_sugar.setText("", TextView.BufferType.EDITABLE);
            }
            if (str_list[3] != null || !str_list[3].isEmpty()) {
                constraint_calories.setText(str_list[3], TextView.BufferType.EDITABLE);
                if (str_list[3].equalsIgnoreCase("999"))
                    constraint_calories.setText("", TextView.BufferType.EDITABLE);
            }
            if (str_list[4] != null || !str_list[4].isEmpty()) {
                constraint_fat.setText(str_list[4], TextView.BufferType.EDITABLE);
                if (str_list[4].equalsIgnoreCase("999"))
                    constraint_fat.setText("", TextView.BufferType.EDITABLE);
            }
        }


        dbHandler.close();
        dbHandler = new MyDBHandler(this, null, null, 2);
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
        if (dbRecords != null && (!dbRecords.isEmpty())) {
            Toast.makeText(this, "Retrieved data= " + dbRecords, Toast.LENGTH_SHORT).show();
            setCheckboxState();
        } else if (dbRecords == null) {
            Toast.makeText(this, "Retrieved data = NULL" + dbRecords, Toast.LENGTH_SHORT).show();
        } else if (dbRecords.isEmpty()) {
            Toast.makeText(this, "Retrieved data = EMPTY", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Retrieved data = NEITHER", Toast.LENGTH_SHORT).show();
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
        if (!dbRecords.isEmpty() && dbRecords != null) {
            for (String record : fetchedRecords) {
                String[] separated = record.split(",");
                for (String item : separated) {
                    if (item.equalsIgnoreCase("Non-Vegetarian Meals")) {
                        mealType_nonveg.setChecked(true);
                    }
                    if (item.equalsIgnoreCase("Vegetarian Meals")) {
                        mealType_veg.setChecked(true);
                    }
                    if (item.equalsIgnoreCase("Vegan Meals")) {
                        mealType_vegan.setChecked(true);
                    }
                    if (item.equalsIgnoreCase("Chicken")) {
                        avoidMeat_chicken.setChecked(true);
                    }
                    if (item.equalsIgnoreCase("Pork")) {
                        avoidMeat_pork.setChecked(true);
                    }
                    if (item.equalsIgnoreCase("Fish")) {
                        avoidMeat_fish.setChecked(true);
                    }
                    if (item.equalsIgnoreCase("Beef")) {
                        avoidMeat_beef.setChecked(true);
                    }
                    if (item.equalsIgnoreCase("Onion")) {
                        avoidVeg_onions.setChecked(true);
                    }
                    if (item.equalsIgnoreCase("Lettuce")) {
                        avoidVeg_lettuce.setChecked(true);
                    }
                    if (item.equalsIgnoreCase("Corn")) {
                        avoidVeg_corn.setChecked(true);
                    }
                    if (item.equalsIgnoreCase("Tomatoes")) {
                        avoidVeg_tomato.setChecked(true);
                    }
                    if (item.equalsIgnoreCase("Dairy")) {
                        avoidVeg_diary.setChecked(true);
                    }
                    if (item.equalsIgnoreCase("Wheat")) {
                        avoidVeg_wheat.setChecked(true);
                    }
                    if (item.equalsIgnoreCase("Diabetes")) {
                        userConditions_diabetes.setChecked(true);
                    }
                    if (item.equalsIgnoreCase("Lactose Intolerance")) {
                        userConditions_lactoseIntolerance.setChecked(true);
                    }
                    if (item.equalsIgnoreCase("Gluten Intolerance")) {
                        userConditions_glutenIntolerance.setChecked(true);
                    }
                    if (item.equalsIgnoreCase("Nut Allergy")) {
                        userConditions_nutAllergy.setChecked(true);
                    }
                    if (item.equalsIgnoreCase("High Blood Pressure")) {
                        userConditions_highBloodPressure.setChecked(true);
                    }
                    if (item.equalsIgnoreCase("Ketogenic Diet")) {
                        diet_keto.setChecked(true);
                    }
                    if (item.equalsIgnoreCase("Dr. Sumi Helal Diet")) {
                        diet_helal.setChecked(true);
                    }
                }
            }
        }
    }

    /*  *******************************************************************
     * Parameters: view state
     * Return: none
     * Description: Inserts preferences into database
     * Author: Srishti Hunjan
     * Date: 4/7/2017
     * ********************************************************************/
    public void savePreferences(View view) {

        getCheckboxValues();

        EditText constraint_Protein = (EditText) findViewById(R.id.constraint_Protein);
        EditText constraint_carbs = (EditText) findViewById(R.id.constraint_carbs);
        EditText constraint_sugar = (EditText) findViewById(R.id.constraint_sugar);
        EditText constraint_calories = (EditText) findViewById(R.id.constraint_calories);
        EditText constraint_fat = (EditText) findViewById(R.id.constraint_fat);

        if ((constraint_Protein.getText().toString() == null) || (constraint_Protein.getText().toString().isEmpty()))
            calorie_list.add("999");
        else
            calorie_list.add(constraint_Protein.getText().toString());

        if ((constraint_carbs.getText().toString() == null) || (constraint_carbs.getText().toString().isEmpty()))
            calorie_list.add("999");
        else
            calorie_list.add(constraint_carbs.getText().toString());

        if ((constraint_sugar.getText().toString() == null) || (constraint_sugar.getText().toString().isEmpty()))
            calorie_list.add("999");
        else
            calorie_list.add(constraint_sugar.getText().toString());

        if ((constraint_calories.getText().toString() == null) || (constraint_calories.getText().toString().isEmpty()))
            calorie_list.add("999");
        else
            calorie_list.add(constraint_calories.getText().toString());

        if ((constraint_fat.getText().toString() == null) || (constraint_fat.getText().toString().isEmpty()))
            calorie_list.add("999");
        else
            calorie_list.add(constraint_fat.getText().toString());

        //(String username, String meal_type, String avoidVeg, String avoidMeat, String conditions, String diets)
        //UserPreferences userPreferences = new UserPreferences(USERNAME, mealType_list.toString(), avoidVeg_list.toString(), avoidMeat_list.toString(), conditions_list.toString(), diets_list.toString(), calorie_list.toString());
        UserPreferences userPreferences = new UserPreferences(USERNAME, convertToString(mealType_list), convertToString(avoidVeg_list), convertToString(avoidMeat_list), convertToString(conditions_list), convertToString(diets_list), convertToString(calorie_list));
        int recordCount = dbHandler.addPreferences(userPreferences);
        Toast.makeText(this, "Preferences Saved!! : " + recordCount, Toast.LENGTH_LONG).show();
        Intent mainPage = new Intent(this, MainActivity.class);
        startActivity(mainPage);
        finish();
    }

    /*  *******************************************************************
     * Parameters: null
     * Return: null
     * Description: Checks if checkbox is checked or not
     * Author: Srishti Hunjan
     * Date: 4/10/2017
     * ********************************************************************/
    public void getCheckboxValues() {
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

        if (mealType_nonveg.isChecked()) {
            mealType_list.add("Non-Vegetarian Meals");
        }
        if (mealType_veg.isChecked()) {
            mealType_list.add("Vegetarian Meals");
        }
        if (mealType_vegan.isChecked()) {
            mealType_list.add("Vegan Meals");
        }
        if (avoidVeg_corn.isChecked()) {
            avoidVeg_list.add("Corn");
        }
        if (avoidVeg_onions.isChecked()) {
            avoidVeg_list.add("Onion");
        }
        if (avoidVeg_lettuce.isChecked()) {
            avoidVeg_list.add("Lettuce");
        }
        if (avoidVeg_tomato.isChecked()) {
            avoidVeg_list.add("Tomatoes");
        }
        if (avoidVeg_diary.isChecked()) {
            avoidVeg_list.add("Dairy");
        }
        if (avoidVeg_wheat.isChecked()) {
            avoidVeg_list.add("Wheat");
        }
        if (avoidMeat_chicken.isChecked()) {
            avoidMeat_list.add("Chicken");
        }
        if (avoidMeat_fish.isChecked()) {
            avoidMeat_list.add("Fish");
        }
        if (avoidMeat_pork.isChecked()) {
            avoidMeat_list.add("Pork");
        }
        if (avoidMeat_beef.isChecked()) {
            avoidMeat_list.add("Beef");
        }
        if (userConditions_diabetes.isChecked()) {
            conditions_list.add("Diabetes");
        }
        if (userConditions_glutenIntolerance.isChecked()) {
            conditions_list.add("Gluten Intolerance");
        }
        if (userConditions_lactoseIntolerance.isChecked()) {
            conditions_list.add("Lactose Intolerance");
        }
        if (userConditions_nutAllergy.isChecked()) {
            conditions_list.add("Nut Allergy");
        }
        if (userConditions_highBloodPressure.isChecked()) {
            conditions_list.add("High Blood Pressure");
        }
        if (diet_keto.isChecked()) {
            diets_list.add("Ketogenic Diet");
        }
        if (diet_helal.isChecked()) {
            diets_list.add("Dr. Sumi Helal Diet");
        }
    }

    /*  *******************************************************************
     * Parameters: view state
     * Return: String of arrayList
     * Description: Converts ArrayList to String
     * Author: Srishti Hunjan
     * Date: 4/10/2017
     * ********************************************************************/
    public String convertToString(ArrayList<String> string_list) {
        StringBuilder sb = new StringBuilder();

        Iterator it = string_list.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            sb.append(",");
        }
        return sb.toString();
    }
}

