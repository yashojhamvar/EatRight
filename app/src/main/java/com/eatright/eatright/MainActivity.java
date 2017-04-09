package com.eatright.eatright;

import android.content.Context;
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
import java.util.Arrays;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mrecView;
    private RecyclerView.Adapter madapter;
    private RecyclerView.LayoutManager mlayout;
    private RecyclerView mrecView2;
    private RecyclerView.Adapter madapter2;
    private RecyclerView.LayoutManager mlayout2;

    public static String USERNAME = "";
    private final String RESTAURANT_URL = "http://192.168.0.6/dishes/Subway";
    private final String CONDITION_URL = "http://192.168.0.6/promotions";
    public String totalResult;
    private RequestQueue requestQ;

    private static final int RC_SIGN_IN = 1;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


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

        mrecView2 = (RecyclerView) findViewById(R.id.recycler_view2);
        mlayout2 = new LinearLayoutManager(this);
        madapter2 = new MenuItemAdapter(this);
        mrecView2.setHasFixedSize(true);
        mrecView2.setLayoutManager(mlayout2);
        mrecView2.setAdapter(madapter2);

        requestQ = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Yasho - for Auth with FireBase
        mAuth = FirebaseAuth.getInstance();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // Toast.makeText(MainActivity.this, "You are In!", Toast.LENGTH_SHORT).show();
                    onSignedIn(user.getDisplayName(), user.getPhotoUrl(), user.getEmail());
                } else {
                    onSignedOut();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);

                }
            }
        };

        final Button restaurantButton = (Button) findViewById(R.id.buttonRestaurant);
        restaurantButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MenuItemAdapter) madapter).clear();
                getRestaurantData();
            }
        });

        final Button conditionButton = (Button) findViewById(R.id.buttonCondition);
        conditionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getConditionData();
            }
        });
    }

    private void getRestaurantData() {
        final Context self = this;
        // Toast.makeText(this, "In Get Restaurant", Toast.LENGTH_SHORT).show();
        final StringRequest res = new StringRequest(Request.Method.GET, RESTAURANT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String dishName = null;
                    int dishVeg=1;
                    int lactose=1;
                    int[] calories = new int[4];
                    int totalCal;
                    String[] ingredients = new String[20];

                    totalResult = response;
                    JSONArray resultJsonArray = (new JSONObject(response)).getJSONArray("menu_items");
                    for (int i = 0; i < resultJsonArray.length(); ++i) {
                        dishName = resultJsonArray.getJSONObject(i).getString("item_name");
                        dishVeg = resultJsonArray.getJSONObject(i).getInt("vegetarian_index");
                        lactose = resultJsonArray.getJSONObject(i).getInt("lactose_content");

                        //Take calorie content in an array
                        JSONArray calorieArray = resultJsonArray.getJSONObject(i).getJSONArray("Cal_content");
                        calories[0] = calorieArray.getJSONObject(0).getInt("Carbohydrates");
                        calories[1] = calorieArray.getJSONObject(1).getInt("Fat");
                        calories[2] = calorieArray.getJSONObject(2).getInt("Protein");
                        totalCal = calorieArray.getJSONObject(3).getInt("Calories");

                        //Take ingredients in an array
                        JSONArray ingredientArray = resultJsonArray.getJSONObject(i).getJSONArray("Ingredients");
                        for (int j = 0; j < ingredientArray.length(); ++j) {
                            ingredients[j] = ingredientArray.getJSONObject(j).getString("ingredient_name");
                        }

                        //Constructor to create an object of RestMenuItem
                        RestMenuItem datum = new RestMenuItem(dishName, dishVeg, lactose, calories, totalCal, ingredients);
                        //Add to Recycler View
                        ((MenuItemAdapter) madapter).addItem(datum, madapter.getItemCount(), Color.GREEN);
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

    private void getConditionData() {// Make a request
        //final EditText keywordEditText = (EditText) findViewById(R.id.keyWordEditText);
        final Context self = this;
        final StringRequest res = new StringRequest(Request.Method.GET, CONDITION_URL, new Response.Listener<String>() {
            private ImageLoader imgLoad = VolleySingleton.getInstance(self).getImageLoader();

            // On response
            @Override
            public void onResponse(String response) {
                try {
                    totalResult = response;
                    // Get the response and convert it to JSON
                    Toast.makeText(self, totalResult, Toast.LENGTH_LONG).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_preferences) {
            Toast.makeText(this, "Preferences?", Toast.LENGTH_SHORT).show();
           /* Intent i = new Intent(this, FoodPreferences.class);
            i.putExtra("username", USERNAME);
            startActivity(i);*/
        } else if (id == R.id.nav_sign_out) {
            //Toast.makeText(this,"Sign Out?",Toast.LENGTH_SHORT).show();
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
}