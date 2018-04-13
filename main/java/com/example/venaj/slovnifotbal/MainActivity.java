package com.example.venaj.slovnifotbal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


/**
 * Obsluhuje uvodvni obrazovku aplikace
 */
public class MainActivity extends AppCompatActivity {

    public static String language; // jazyk slovniku

    public static final String CZE = "CZE";
    public static final String ENG = "ENg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    /**
     * spusti nasledujici aktivitu s nastavenim hry
     * @param view
     */
    public void showSettingActivity(View view) {
        Intent i = new Intent(this,
                ActivityNastaveni.class);

        i.putExtra("DICTIONARY", language);
        startActivity(i);
    }

    /**
     * spusti aktvitu na prohlidnuti slovnku
     * @param view
     */
    public void showDictionary(View view) {
        Intent i = new Intent(this,
                ActivitySlovnik.class);
        i.putExtra("slovnik", language);
        startActivity(i);
    }

    public void languageSelect(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        RadioButton engRB = findViewById(R.id.rb_eng);
        RadioButton czeRB = findViewById(R.id.rb_cze);


        switch(view.getId()) {
            case R.id.rb_cze:
                if (checked){
                    language = CZE;
                    engRB.setChecked(false);
                }

                    break;
            case R.id.rb_eng:
                if (checked){
                    language = ENG;
                    czeRB.setChecked(false);
                }
                    break;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
