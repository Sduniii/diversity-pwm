package de.diversity.androidpwm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;

import de.diversity.androidpwm.items.Item;
import de.diversity.androidpwm.models.Mode;
import de.diversity.androidpwm.tools.Core;

/**
 * Created by Lokales Profil on 22.12.2014.
 */
public class LoadingSreenActivity extends Activity {

    private ArrayList<String> arguments;
    private TextView txtV;
    private LinkedList<String> lst;

    //Introduce an delay
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);
        findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);

        txtV = (TextView) findViewById(R.id.lText);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getStringArrayList("arguments") != null) {
                arguments = extras.getStringArrayList("arguments");
            }
        }

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    Core core = new Core(arguments.get(0), arguments.get(1), Mode.LOAD, txtV);
                    core.doInBackground();
                    lst = core.getAllDecrypt();
                    int id = 0;
                    for(int i = 0; i < lst.size(); i++){

                        Item.addItem(new Item.DummyItem(id, lst.get(i), lst.get(++i), lst.get(++i)));
                        id++;
                    }
                    Intent intent = new Intent(LoadingSreenActivity.this, ItemListActivity.class);
                    startActivity(intent);

                } catch (Exception e) {

                } finally {
                    Intent i = new Intent(LoadingSreenActivity.this,
                            ItemListActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}