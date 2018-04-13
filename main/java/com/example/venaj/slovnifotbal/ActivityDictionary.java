package com.example.venaj.slovnifotbal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * obsluhuje aktivitu na zobrazeni slovniku
 */
public class ActivityDictionary extends AppCompatActivity {
    BufferedReader br;
    String line;
    StringBuilder text;
    ArrayList<String> words;
    ArrayAdapter<String> mHistory;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slovnik);

        String slovnik = getIntent().getStringExtra("DICTIONARY"); // jazyk slovniku

        lv = (ListView) findViewById(R.id.lv);
        lv.setFastScrollEnabled(true); // umozni rychle scrollovani
        lv.setClickable(false);

        words = new ArrayList<>();
        mHistory = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, words);

        InputStream ins;
        if(slovnik.equals(getString(R.string.cze_language))) { // podle jazyku inicializuje inputstream ze souboru ulozenych v raw
            ins = getResources().openRawResource(R.raw.cze);
        }else{
            ins = getResources().openRawResource(R.raw.eng);
        }
        br = new BufferedReader(new InputStreamReader(ins));
        text = new StringBuilder();
        loadDictionary();

    }

    private void loadDictionary(){
        try {
            while ((line = br.readLine()) != null) {
                words.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        lv.setAdapter(mHistory);
    }
}
