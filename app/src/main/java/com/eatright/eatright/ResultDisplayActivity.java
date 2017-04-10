/*
package com.eatright.eatright;

import android.content.Context;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.toolbox.ImageLoader;

        import java.io.BufferedReader;
        import java.io.BufferedWriter;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.OutputStreamWriter;
        import java.util.ArrayList;

public class ResultDisplayActivity extends AppCompatActivity {

    TextView ingredView;
    TextView titleView;
    TextView descView, calView, reasonView;
    public String title, desc;
    public ArrayList<String> ingred, cal, reason;


    com.android.volley.toolbox.NetworkImageView image1;
    final Context self = this;
    private ImageLoader imgLoad = VolleySingleton.getInstance(self).getImageLoader();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);


        desc = getIntent().getExtras().getString("desc");
        title = getIntent().getExtras().getString("title");
        ingred = getIntent().getExtras().getStringArrayList("ingredients");
        cal = getIntent().getExtras().getStringArrayList("calories");
        reason = getIntent().getExtras().getStringArrayList("reason");
        setTitle("BOOK:" + title);


        //  String msg = "index is " + resid;


        //    private ImageLoader imgLoad = VolleySingleton.getInstance(self).getImageLoader();

        ingredView = (TextView) findViewById(R.id.Ingredients);
        titleView = (TextView) findViewById(R.id.Title);
        descView = (TextView) findViewById(R.id.Description);
        calView = (TextView) findViewById(R.id.Calories);
        reasonView = (TextView) findViewById(R.id.Reason);

        image1 = (com.android.volley.toolbox.NetworkImageView) findViewById(R.id.image1);

        Button backButton = (Button) this.findViewById(R.id.button2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        text.setText(resid);
        Date.setText(date);
        title1.setText(title);


        // File file = new File(path);
        //file.mkdir();


        ImageButton b = (ImageButton) this.findViewById(R.id.StarButton);
        b.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //String message = title1.getText().toString();
                ArrayList<String> s = new ArrayList<String>();

                try {
                    //first check if name already present in file
                    InputStream inputStream = self.openFileInput("config.csv");
                    if (inputStream != null) {
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String receiveString = "";


                        while ((receiveString = bufferedReader.readLine()) != null) {

                            String str[] = receiveString.split(" ");

                            s.add(str[0]);

                            receiveString = bufferedReader.readLine();

                        }

                        inputStream.close();
                    }
                } catch (FileNotFoundException e) {
                    Log.e("login activity", "File not found: " + e.toString());
                } catch (IOException e) {
                    Log.e("login activity", "Can not read file: " + e.toString());
                }

                try {
                    if (s.isEmpty() || (!s.isEmpty() && !s.contains(Integer.toString(id)))) {

                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(self.openFileOutput("config.csv", Context.MODE_APPEND));
                        BufferedWriter bwriter = new BufferedWriter(outputStreamWriter);
                        outputStreamWriter.append(id + " " + title + "\r\n");


                        outputStreamWriter.close();
                    }
                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
                Toast toast = Toast.makeText(self, "Added to Favorites", Toast.LENGTH_SHORT);
                toast.show();

            }
        });

        image1.setImageUrl(im, imgLoad);


        text.setText(resid);
        Date.setText(date);
        title1.setText(title);


    }


}
*/
