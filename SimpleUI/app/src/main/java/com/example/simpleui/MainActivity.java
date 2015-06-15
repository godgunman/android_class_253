package com.example.simpleui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

    private static final int REQUEST_CODE_MENU_ACTIVITY = 1;
    private EditText inputEditText;
    private Button sendButton;
    private CheckBox hideCheckBox;
    private ListView historyListView;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private JSONObject menuInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "PihJMpOOpNYxpXN8wYcd3Jvn6R1x6IHOl6TA5gKc",
                "mnPmwNUDinSNH3b4RRiScFdkNRgLFxK61DVIpXYI");


        // /data/data/com.example.simple/shared_prefs/settings.xml
        sp = getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = sp.edit();

        inputEditText = (EditText) findViewById(R.id.editText);
        inputEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                String text = inputEditText.getText().toString();
                editor.putString("input", text);
                editor.commit();

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        send();
                    }
                    return true;
                }
                return false;
            }
        });

        sendButton = (Button) findViewById(R.id.button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        historyListView = (ListView) findViewById(R.id.listView);

        hideCheckBox = (CheckBox) findViewById(R.id.checkBox);
        hideCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("hide", isChecked);
                editor.commit();
            }
        });

        inputEditText.setText(sp.getString("input", ""));
        hideCheckBox.setChecked(sp.getBoolean("hide", false));

        setHistoryData();
    }

    private String getDrinkCategory() {
        return "black tea, milk tea";
    }

    private int getDrinkSum(JSONArray menu) {

        try {
            int sum = 0;
            for (int j = 0; j < menu.length(); j++) {
                JSONObject menuObj = menu.getJSONObject(j);
                String drinkName = menuObj.getString("drinkName");
                int s = menuObj.getInt("s");
                int m = menuObj.getInt("m");
                int l = menuObj.getInt("l");
                sum += s + m + l;
            }
            return sum;
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void setHistoryData() {
        String raw = Utils.readFile(this, "history.txt");

        String[] data = raw.split("\n");
        List<Map<String, String>> mapData = new ArrayList<>();


        for(int i = 0 ; i < data.length; i++) {
            try {
                Map<String, String> item = new HashMap<>();

                JSONObject order = new JSONObject(data[i]);
                String note = order.getString("note");
                JSONArray menuInfo = order.getJSONArray("menu");

                item.put("note", note);
                item.put("drink_category", getDrinkCategory());
                item.put("drink_sum", ""+getDrinkSum(menuInfo));

                mapData.add(item);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String[] from = {"note", "drink_category", "drink_sum"};
        int[] to = {R.id.note, R.id.drink_category, R.id.drink_sum};
        SimpleAdapter adapter = new SimpleAdapter(this, mapData, R.layout.listview_item, from, to);

        historyListView.setAdapter(adapter);
    }

    private void send() {

        JSONObject order = new JSONObject();

        String text = inputEditText.getText().toString();
        if (hideCheckBox.isChecked()) {
            text = "*************";
        }

        try {
            order.put("note", text);
            if (menuInfo != null) {
                order.put("menu", menuInfo.getJSONArray("result"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Utils.writeFile(this, "history.txt", order.toString() + "\n");
        Toast.makeText(this, order.toString(), Toast.LENGTH_SHORT).show();
        inputEditText.setText("");

        setHistoryData();
    }
    /*
        {
            "note": "hello world",
            "menu": [
                {"drinkName": "black tea", "s": 0, "m": 1, "l":1},
                {"drinkName": "milk tea", "s": 0, "m": 1, "l":1},
                {"drinkName": "green tea", "s": 0, "m": 1, "l":1}
            ]
        }
    */


    public void goToMenu(View view) {

        Intent intent = new Intent();
        intent.setClass(this, MenuActivity.class);
        startActivityForResult(intent, REQUEST_CODE_MENU_ACTIVITY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_MENU_ACTIVITY && resultCode == RESULT_OK) {
            try {
                menuInfo = new JSONObject(data.getStringExtra("data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void send2(View view) {
        send();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
