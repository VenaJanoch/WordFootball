package com.example.venaj.slovnifotbal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;


/**
 * Obsluhuje uvodvni obrazovku aplikace
 */
public class MainActivity extends AppCompatActivity {

    public static String language; // jazyk slovniku
    AlertDialog alert;

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
                ActivitySetting.class);

        if(language != null){
            i.putExtra("DICTIONARY", language);
            language = null;
            startActivity(i);
        }else{
            showNoLanguageDialog();
        }

    }

    /**
     * spusti aktvitu na prohlidnuti slovnku
     * @param view
     */
    public void showDictionary(View view) {
        Intent i = new Intent(this,
                ActivityDictionary.class);
        if(language != null){
            i.putExtra("DICTIONARY", language);
            language = null;
            startActivity(i);
        }else{
        showNoLanguageDialog();
        }

    }

    public void showNoLanguageDialog(){
        alert = new AlertDialog.Builder(this).create();
        alert.setTitle(getString(R.string.ww_ad_no_language));
        alert.setMessage(getString(R.string.ww_ad_no_language_message));
        alert.setButton(RESULT_OK, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.cancel();
            }
        });
        alert.show();
    }

    public void languageSelect(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        RadioButton engRB = findViewById(R.id.rb_eng);
        RadioButton czeRB = findViewById(R.id.rb_cze);


        switch(view.getId()) {
            case R.id.rb_cze:
                if (checked){
                    language = getString(R.string.cze_language);
                    engRB.setChecked(false);
                }

                    break;
            case R.id.rb_eng:
                if (checked){
                    language = getString(R.string.eng_language);
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
