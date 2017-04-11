package com.eatright.eatright;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.eatright.eatright.VolleySingleton;

import java.util.ArrayList;

public class ResultDisplayActivity extends AppCompatActivity {

    final Context self = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_display);

        ImageLoader imgLoad = VolleySingleton.getInstance(self).getImageLoader();
        com.android.volley.toolbox.NetworkImageView image1;

        String desc = getIntent().getExtras().getString("dishDesc");
        String title = getIntent().getExtras().getString("dishName");
        ArrayList<String> ingred = getIntent().getExtras().getStringArrayList("ingredients");
        int[] cal = getIntent().getExtras().getIntArray("calContent");
        ArrayList<String> reason = getIntent().getExtras().getStringArrayList("reasons");
        int lactose = getIntent().getExtras().getInt("lactose");
        int gluten = getIntent().getExtras().getInt("gluten");
        int type = getIntent().getExtras().getInt("dishType");
        int totCal = getIntent().getExtras().getInt("totCal");
        int recommended = getIntent().getExtras().getInt("recommended");
        String image = getIntent().getExtras().getString("imageURL");

        TextView ingredView = (TextView) findViewById(R.id.Ingredients);
        TextView titleView = (TextView) findViewById(R.id.Title);
        TextView descView = (TextView) findViewById(R.id.Description);
        TextView calView = (TextView) findViewById(R.id.Calories);
        TextView reasonView = (TextView) findViewById(R.id.Reason);
        TextView typeView = (TextView) findViewById(R.id.Type);

        image1 = (com.android.volley.toolbox.NetworkImageView) findViewById(R.id.image1);
        image1.setImageUrl(image, imgLoad);

        //dishname and description
        titleView.setText(title);
        descView.setText(desc);

        StringBuilder sb = new StringBuilder();
        //ingredients
        for (int i = 0; i < ingred.size(); i++) {
            sb.append(ingred.get(i));
            if (i < ingred.size() - 1)
                sb.append(", ");
        }
        ingredView.setText(sb.toString());


        //Cal Content
        sb = new StringBuilder();
        sb.append("Calorie Content\n");
        sb.append("Carbohydrates: ");
        sb.append(cal[0]);
        sb.append("\n");

        sb.append("Fat: ");
        sb.append(cal[1]);
        sb.append("\n");

        sb.append("Protein: ");
        sb.append(cal[2]);
        sb.append("\n");

        sb.append("Sugar: ");
        sb.append(cal[3]);
        sb.append("\n");

        sb.append("Total Calories: ");
        sb.append(totCal);
        sb.append("\n");

        calView.setText(sb.toString());

        //Type
        sb = new StringBuilder();
        sb.append("Dish Type : ");
        if (type == 0)
            sb.append("Vegan");
        else if (type == 1)
            sb.append("Vegetarian");
        else
            sb.append("Non-Vegetarian");

        if (lactose == 0)
            sb.append("\nLactose Free");
        else
            sb.append("\nContains Lactose");

        if (gluten == 0)
            sb.append("\nGluten Free");
        else
            sb.append("\nContains Gluten");

        typeView.setText(sb.toString());

        //Reason
        if (recommended == 0) {    //if dish is not recommended due to various reasons
            sb = new StringBuilder();
            sb.append("Reasons to avoid this dish:\n");
            for (int i = 0; i < reason.size(); i++) {
                sb.append(reason.get(i));
                sb.append("\n");
            }
            reasonView.setText(sb.toString());
        } else {
            sb = new StringBuilder("This dish is recommended for you" + "\n");
            reasonView.setText(sb.toString());
        }


        Button backButton = (Button) this.findViewById(R.id.button2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
