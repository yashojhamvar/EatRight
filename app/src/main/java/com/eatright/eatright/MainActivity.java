package com.eatright.eatright;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.estimote.sdk.SystemRequirementsChecker;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mrecView;
    private RecyclerView.Adapter madapter;
    private RecyclerView.LayoutManager mlayout;

    public static String USERNAME;
    private final String RESTAURANT_URL = "http://192.168.0.24/dishes/";
    private final String CONDITION_URL = "http://192.168.0.24/promotions";
    public String totalResult;
    private RequestQueue requestQ;

    private static final int RC_SIGN_IN = 1;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    final Context self = this;
    String restaurantNameRecd;

    MyDBHandler dbHandler;
    public ArrayList<String> dbRecords;
    HashSet<String> hset = new HashSet<String>();
    public int ALLOWED_SUGAR = 999;
    public int ALLOWED_CARBS = 999;
    public int ALLOWED_FATS = 999;
    public int ALLOWED_PROTEINS = 999;
    public int ALLOWED_CAL = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mrecView = (RecyclerView) findViewById(R.id.recycler_view);
        mlayout = new LinearLayoutManager(this);
        madapter = new MenuItemAdapter(this);

        mrecView.setHasFixedSize(true);
        mrecView.setLayoutManager(mlayout);
        mrecView.setAdapter(madapter);
        requestQ = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView restView = (TextView) findViewById(R.id.textViewRestName);
        //Yasho - for Auth with FireBase
        mAuth = FirebaseAuth.getInstance();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        retrieveDataFromDB();
        retrieveConditions();

        ((MenuItemAdapter) madapter).clear();
        restaurantNameRecd = EatRight.RESTAURANTNAME;

        if (restaurantNameRecd == null || restaurantNameRecd.length() == 0) {
            restView.setText("You are not at any Restaurant");
        } else {
            ((MenuItemAdapter) madapter).clear();
            restView.setText("You are at " + restaurantNameRecd);
            getRestaurantData();
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSignedIn(user.getDisplayName(), user.getPhotoUrl(), user.getEmail());
                } else {
                    onSignedOut();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);

                }
            }
        };


        ((MenuItemAdapter) madapter).setOnItemClickListener(new MenuItemAdapter.RestMenuItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                String dishName = ((MenuItemAdapter) madapter).getItem(position).getDishName();
                String dishDesc = ((MenuItemAdapter) madapter).getItem(position).getDishDesc();
                String imageURL = ((MenuItemAdapter) madapter).getItem(position).getImageUrl();
                int dishType = ((MenuItemAdapter) madapter).getItem(position).getDishType();
                int lactose = ((MenuItemAdapter) madapter).getItem(position).getLactose();
                int gluten = ((MenuItemAdapter) madapter).getItem(position).getGluten();
                int[] calContent = ((MenuItemAdapter) madapter).getItem(position).getCalContent();
                int totCal = ((MenuItemAdapter) madapter).getItem(position).getTotCal();
                ArrayList<String> ingredients = ((MenuItemAdapter) madapter).getItem(position).getIngredients();
                int recommended = ((MenuItemAdapter) madapter).getItem(position).getRecommended();
                ArrayList<String> reasons = ((MenuItemAdapter) madapter).getItem(position).getReasons();

                Intent in = new Intent(MainActivity.this, ResultDisplayActivity.class);
                in.putExtra("dishName", dishName);
                in.putExtra("dishDesc", dishDesc);
                in.putExtra("imageURL", imageURL);
                in.putExtra("dishType", dishType);
                in.putExtra("lactose", lactose);
                in.putExtra("gluten", gluten);
                in.putExtra("calContent", calContent);
                in.putExtra("totCal", totCal);
                in.putExtra("ingredients", ingredients);
                in.putExtra("recommended", recommended);
                in.putExtra("reasons", reasons);
                startActivity(in);
            }
        });

    }

    private void getRestaurantData() {
        final Context self = this;
        final StringRequest res = new StringRequest(Request.Method.GET, RESTAURANT_URL + restaurantNameRecd, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String dishName = null;
                    String dishDesc;
                    String imageUrl;
                    int dishVeg = 1;
                    int lactose = 1;
                    int gluten = 1;
                    int reco = 1;
                    int totalCal;
                    ArrayList<RestMenuItem> approved = new ArrayList<RestMenuItem>();
                    ArrayList<RestMenuItem> disapproved = new ArrayList<RestMenuItem>();

                    totalResult = response;
                    JSONArray resultJsonArray = (new JSONObject(response)).getJSONArray("menu_items");
                    for (int i = 0; i < resultJsonArray.length(); ++i) {
                        reco = 1;
                        ArrayList<String> reason = new ArrayList<String>();

                        dishName = resultJsonArray.getJSONObject(i).getString("item_name");
                        dishDesc = resultJsonArray.getJSONObject(i).getString("item_description");
                        imageUrl = resultJsonArray.getJSONObject(i).getString("item_url");

                        dishVeg = resultJsonArray.getJSONObject(i).getInt("vegetarian_index");
                        if (dishVeg == 2 && (hset.contains("Vegetarian Meals") || hset.contains("Vegan Meals"))) {
                            reco = 0;
                            reason.add("It is Non-Vegetarian");
                        } else if ((dishVeg == 1) && hset.contains("Vegan Meals")) {
                            reco = 0;
                            reason.add("It Contains Dairy Products");
                        }

                        lactose = resultJsonArray.getJSONObject(i).getInt("lactose_content");
                        if (lactose == 1 && hset.contains("Lactose Intolerance")) {
                            reco = 0;
                            reason.add("It has Lactose Contents");
                        }

                        gluten = resultJsonArray.getJSONObject(i).getInt("gluten_index");
                        if (gluten == 1 && hset.contains("Gluten Intolerance")) {
                            reco = 0;
                            reason.add("It has Gluten Contents");
                        }

                        //Take calorie content in an array
                        JSONArray calorieArray = resultJsonArray.getJSONObject(i).getJSONArray("cal_content");
                        int[] calories = new int[4];
                        calories[0] = calorieArray.getJSONObject(0).getInt("Carbohydrates");
                        if (calories[0] > ALLOWED_CARBS) {
                            reco = 0;
                            reason.add("It has more Carbs than permitted for you");
                        }
                        calories[1] = calorieArray.getJSONObject(0).getInt("Fat");
                        if (calories[1] > ALLOWED_FATS) {
                            reco = 0;
                            reason.add("It has more Fats than permitted for you");
                        }
                        calories[2] = calorieArray.getJSONObject(0).getInt("Protein");
                        if (calories[2] > ALLOWED_PROTEINS) {
                            reco = 0;
                            reason.add("It has more Proteins than permitted for you");
                        }
                        calories[3] = calorieArray.getJSONObject(0).getInt("Sugar");
                        if (calories[3] > ALLOWED_SUGAR) {
                            reco = 0;
                            reason.add("It has more Sugar than permitted for you");
                        }
                        totalCal = calorieArray.getJSONObject(0).getInt("Calories");
                        if (totalCal > ALLOWED_CAL) {
                            reco = 0;
                            reason.add("It has more total Calories than permitted for you");
                        }

                        //Take ingredients in an array
                        JSONArray ingredientArray = resultJsonArray.getJSONObject(i).getJSONArray("Ingredients");
                        ArrayList<String> ingredients = new ArrayList<String>();
                        for (int j = 0; j < ingredientArray.length(); ++j) {
                            String temp = ingredientArray.getJSONObject(j).getString("ingredient_name");
                            ingredients.add(temp);
                            if (hset.contains(temp)) {
                                reco = 0;
                                reason.add("It contains " + temp);
                            }
                        }

                        //Constructor to create an object of RestMenuItem
                        RestMenuItem datum = new RestMenuItem(dishName, dishDesc, imageUrl, dishVeg, lactose, gluten, calories, totalCal, ingredients, reco, reason);

                        if (reco == 1)
                            approved.add(datum);
                        else
                            disapproved.add(datum);

                    }
                    //add to madapter
                    Iterator<RestMenuItem> it = approved.iterator();
                    while (it.hasNext()) {
                        ((MenuItemAdapter) madapter).addItem(it.next(), madapter.getItemCount());
                    }
                    Iterator<RestMenuItem> it2 = disapproved.iterator();
                    while (it2.hasNext()) {
                        ((MenuItemAdapter) madapter).addItem(it2.next(), madapter.getItemCount());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError err) {
                err.printStackTrace();
            }
        }
        );
        requestQ.add(res);
    }


    private void checkNeededPermissions() {

        ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
        PermissionListener snackbarPermissionListener =
                SnackbarOnDeniedPermissionListener.Builder
                        .with(rootView, "Location is needed to detect BLE devices near you")
                        .withOpenSettingsButton("Settings")
                        .withCallback(new Snackbar.Callback() {
                            @Override
                            public void onShown(Snackbar snackbar) {
                                // Event handler for when the given Snackbar is visible
                            }

                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                // Event handler for when the given Snackbar has been dismissed
                            }
                        }).build();

        Dexter.withActivity(this)
                .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)

                .withListener(snackbarPermissionListener)
                .check();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_preferences) {
            Intent prefPage = new Intent(this, FoodPreferences.class);
            prefPage.putExtra("username", USERNAME);
            startActivity(prefPage);
            finish();
        } else if (id == R.id.nav_sign_out) {
            AuthUI.getInstance().signOut(this);
            finish();
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onSignedIn(String username, Uri photo, String email) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView textName = (TextView) header.findViewById(R.id.UserName);
        textName.setText(username);
        TextView textEmail = (TextView) header.findViewById(R.id.UserEmail);
        textEmail.setText(email);

        ImageLoader imgLoad = VolleySingleton.getInstance(this).getImageLoader();
        com.android.volley.toolbox.NetworkImageView image1;
        image1 = (com.android.volley.toolbox.NetworkImageView) header.findViewById(R.id.UserImage);
        image1.setImageUrl(photo.toString(), imgLoad);
        USERNAME = username;

    }

    private void onSignedOut() {

    }

    protected void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(mAuthListener);
        //mMessageAdapter.clear();
        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    public void retrieveDataFromDB() {
        dbHandler = new MyDBHandler(this, null, null, 2);
        dbRecords = dbHandler.retrievePreferences();

        if (dbRecords != null && !dbRecords.isEmpty()) {
            makeHashSet();
        } else {
        }
        dbHandler.close();

    }

    public void makeHashSet() {
        for (int i = 1; i < 5; i++) {
            String[] separated = dbRecords.get(i).split(",");
            for (String ingr : separated)
                hset.add(ingr);
        }
        String[] calContent = dbRecords.get(6).split(",");

        if (dbRecords.get(5).contains("Dr. Sumi Helal Diet")) {
            ALLOWED_SUGAR = 0;
        } else
            ALLOWED_SUGAR = Integer.parseInt(calContent[2]);
        if (dbRecords.get(5).contains("Ketogenic Diet")) {
            ALLOWED_CARBS = 10;
            ALLOWED_FATS = 300;
            ALLOWED_PROTEINS = 50;
        } else {
            ALLOWED_CARBS = Integer.parseInt(calContent[1]);
            ALLOWED_FATS = Integer.parseInt(calContent[4]);
            ALLOWED_PROTEINS = Integer.parseInt(calContent[0]);
        }
        ALLOWED_CAL = Integer.parseInt(calContent[3]);
    }

    private void retrieveConditions() {
        dbHandler = new MyDBHandler(this, null, null, 1);
        dbRecords = dbHandler.retrievePreferences();
        final String[] conditions;
        if (dbRecords != null && !dbRecords.isEmpty())
            conditions = dbRecords.get(4).split(",");
        else
            conditions = null;
        final Context self = this;
        final StringRequest res = new StringRequest(Request.Method.GET, CONDITION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    totalResult = response;
                    JSONArray resultJsonArray = (new JSONObject(response)).getJSONArray("conditions");
                    for (int i = 0; i < conditions.length; i++) {
                        for (int j = 0; j < resultJsonArray.length(); j++) {
                            if (resultJsonArray.getJSONObject(j).getString("condition_name").equalsIgnoreCase(conditions[i])) {
                                JSONArray ingredientsToAvoid = resultJsonArray.getJSONObject(j).getJSONArray("ingredients_avoid");
                                for (int k = 0; k < ingredientsToAvoid.length(); k++) {
                                    hset.add(ingredientsToAvoid.getJSONObject(k).getString("ingredient_name"));
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    // Print to console on error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            // Print to console on Error
            @Override
            public void onErrorResponse(VolleyError err) {
                err.printStackTrace();
            }
        }
        );
// Start the request
        requestQ.add(res);
    }

}