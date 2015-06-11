package com.example.simpleui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MenuActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    public String getData() {
        LinearLayout root = (LinearLayout) findViewById(R.id.root);
        int count = root.getChildCount();

        String all = "";

        for (int i = 0 ; i < count - 1; i++) {
            LinearLayout child = (LinearLayout) root.getChildAt(i);
            TextView drinkNameTextView = (TextView) child.getChildAt(0);
            Button smallButton = (Button) child.getChildAt(1);
            Button mediumButton = (Button) child.getChildAt(2);
            Button largeButton = (Button) child.getChildAt(3);

            String drinkName = drinkNameTextView.getText().toString();
            int small = Integer.parseInt(smallButton.getText().toString());
            int medium = Integer.parseInt(mediumButton.getText().toString());
            int large = Integer.parseInt(largeButton.getText().toString());

            all += drinkName + "," + small + "," + medium + "," + large + "\n";
        }
        return all;
    }

    public void pick(View view) {
        Button button = (Button) view;
        String text = button.getText().toString();
        int count = Integer.parseInt(text) + 1;
        button.setText(String.valueOf(count));
    }

    public void done(View view) {
        Toast.makeText(this, getData(), Toast.LENGTH_LONG).show();
        finish();
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
}
