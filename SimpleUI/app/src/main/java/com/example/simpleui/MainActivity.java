package com.example.simpleui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    private EditText inputEditText;
    private Button sendButton;
    private CheckBox hideCheckBox;
    private ListView historyListView;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    private void setHistoryData() {
        String raw = Utils.readFile(this, "history.txt");
        String[] data = raw.split("\n");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        historyListView.setAdapter(adapter);
    }

    private void send() {

        String text = inputEditText.getText().toString();
        if (hideCheckBox.isChecked()) {
            text = "*************";
        }

        Utils.writeFile(this, "history.txt", text + "\n");
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        inputEditText.setText("");

        setHistoryData();
    }

    public void goToMenu(View view) {

        Intent intent = new Intent();
        intent.setClass(this, MenuActivity.class);
        startActivity(intent);

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
