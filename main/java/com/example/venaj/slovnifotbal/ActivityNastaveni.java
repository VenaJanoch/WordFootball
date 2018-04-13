package com.example.venaj.slovnifotbal;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * obsluhuje aktivitu s nastavenim parametru hry
 */
public class ActivityNastaveni extends AppCompatActivity {

    public static final int GAMER_COUNT = 4;
    EditText[] et_players; // editTexty pro zadani jmena hracu
    String dictionary;
    Spinner timeSpinner;
    String[] gamerNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        dictionary = getIntent().getStringExtra("DICTIONARY"); // jazyk slovniku
        gamerNames = new String[GAMER_COUNT];

        loadEditTextID();
        createTimeSpinner();
    }

    public void loadEditTextID(){
        et_players = new EditText[GAMER_COUNT];
        et_players[0] = (EditText)findViewById(R.id.et_gamer1);
        et_players[1] = (EditText)findViewById(R.id.et_gamer2);
        et_players[2] = (EditText)findViewById(R.id.et_gamer3);
        et_players[3] = (EditText)findViewById(R.id.et_gamer4);
    }


    public void createTimeSpinner(){
        timeSpinner = (Spinner) findViewById(R.id.sp_word_time);
        List<String> seznam = new ArrayList<>();
        seznam.add("10");
        seznam.add("20");
        seznam.add("25");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, seznam);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(dataAdapter);
    }

    /*
     * spusti herni aktivitu
     * @param view
     */
    public void showGameActivity(View view) {
       int players_count =  loadGamerName();

       if (players_count != 0 ){

           Intent i = new Intent(this, ActivityHra.class);
           i.putExtra("TIME", Integer.parseInt(timeSpinner.getSelectedItem().toString()));
           i.putExtra("GAMER_COUNT", players_count);
           i.putExtra("GAMERS_NAME", gamerNames);
           i.putExtra("DICTIONARY", dictionary);
           startActivity(i);
       }else{
           //TODO Alert window
       }


    }

    /**
     * zkontroluje, zda uzivatel zadal vsechna jmena hracu
     * @return
     */
    public int loadGamerName(){
        int playerCount = 0;


        for(int i = 0; i < GAMER_COUNT; i++){

            String jmeno = et_players[i].getText().toString();
            if(!TextUtils.isEmpty(jmeno)){
                System.out.println(jmeno);
                gamerNames[playerCount] = jmeno;
                playerCount++;
            }
        }

        return playerCount;
    }


}
