package com.example.venaj.slovnifotbal;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * obsluhuje herni cyklus aplikace
 */

public class ActivityHra extends AppCompatActivity {

    private Trie trie;
    ProgressBar mProgressBar;
    CountDownTimer stopwatch;
    TextView tvStopwatch, tvWord, tvGamer;
    EditText etWord;
    Hrac[] gamers;
    int time, gamerCount, playingGamer, playingGamerCount;
    NacteniSlovniku slovnik;
    int cislo = 100;
    String[] gamerName;
    
    ArrayList<String> wordList;
    Animation animation;
    Button newWord, comuterTurn; // napovedy
    String dictionaryType;

    ProgressDialog progress, prepnuti;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent i = getIntent();
        gamerCount = i.getIntExtra("GAMER_COUNT", 1);
        dictionaryType = i.getStringExtra("DICTIONARY");
        playingGamerCount = gamerCount;
        gamerName = i.getStringArrayExtra("GAMERS_NAME");
        time = i.getIntExtra("TIME", 10);
        tvWord = (TextView) findViewById(R.id.textView);
        tvStopwatch = (TextView) findViewById(R.id.textView2);
        tvGamer = (TextView) findViewById(R.id.textViewAktualniHrac);
        wordList = new ArrayList<>();

        inicializujHrace();
        inicializujButton();
        inicializujEditText();

        tvGamer.setText(gamerName[0]);
        tvWord.setText("První slovo bude náhodně vybráno po načtení slovníku");
        tvStopwatch.setText("" + time);

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Načítání slovníku ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        new InicializaceSlovniku().execute(""); // spusti nacitani slovniku
        mProgressBar=(ProgressBar)findViewById(R.id.progressBar5);
    }

    private void inicializujHrace(){
        gamers = new Hrac[gamerCount];
        for(int i = 0; i < gamerCount; i++){
            gamers[i] = new Hrac(gamerName[i]);
        }
    }

    /**
     * animuje nekonecne reverzne skakajici mic
     */
//    public void spustAnimaci(){
//        mic = (ImageView) findViewById(R.id.imageView);
//        mic.setImageResource(R.drawable.ic_kopacak);
//        animation = new TranslateAnimation(0, 0, 0, this.getResources().getDisplayMetrics().heightPixels/2);
////        animation.setStartOffset(10000);
//        animation.setDuration(5000);
//        animation.setRepeatMode(Animation.REVERSE);
//        animation.setRepeatCount(Animation.INFINITE);
//        animation.setInterpolator(new BounceInterpolator());
//
//        mic.startAnimation(animation);
//    }

    private void inicializujEditText(){

        etWord = (EditText) findViewById(R.id.editText);

        etWord.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)){
                    try {
                        potvrdSlovo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void inicializujButton(){
        newWord = (Button) findViewById(R.id.jineSlovo);
        comuterTurn = (Button) findViewById(R.id.tahPocitace);
        newWord.setEnabled(true);
        comuterTurn.setEnabled(true);
        newWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aktualniSlovo = tvWord.getText().toString();
                tvWord.setText(trie.vratRandomSlovo(aktualniSlovo.charAt(0)));
                gamers[playingGamer].setJineSlovo(false);
                newWord.setEnabled(false);
            }
        });

        comuterTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aktualniSlovo = tvWord.getText().toString();
                etWord.setText(trie.vratRandomSlovo(aktualniSlovo.charAt(aktualniSlovo.length()-1)));
                gamers[playingGamer].setPocitacuvTah(false);
                comuterTurn.setEnabled(false);
            }
        });
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    potvrdSlovo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void zavibruj(){
        Vibrator vibrator = (Vibrator)
                getSystemService(Context.VIBRATOR_SERVICE);
        long [] vzor = {1000, 2000, 1000, 2000, 1000 };
        vibrator.vibrate(vzor, 0);
        vibrator.vibrate(1000);
    }

    /**
     * kontroluje, zda slovo zacina poslednim pismenem predchoziho tahu,
     * zda se slovo nachazi ve slovniku a zda jeste nebylo zadano.
     * Kdyz vsechno projde, prepne na dalsiho hrace, nebo vygeneruje tah pocitace.
     * @throws Exception
     */
    public void potvrdSlovo() throws Exception {
        String slovoPredchozi = tvWord.getText().toString();
        String slovoZadane = etWord.getText().toString();
        slovoZadane.toLowerCase();
        slovoZadane = slovoZadane.trim();
        if(TextUtils.isEmpty(slovoZadane) == true){
            etWord.setError("Nezadal jsi žádné slovo!");
            zavibruj();
        }else{
            int delka = slovoPredchozi.length() - 1;
            if(slovoZadane.charAt(0) != slovoPredchozi.charAt(delka)){
                etWord.setError("Zadané slovo nezačíná písmenem, kterým předchozí slovo končí!");
                zavibruj();
            }else if(trie.findWord(slovoZadane) == false){
                etWord.setError("Zadané slovo není české podstatné jméno!");
                zavibruj();
            }else if(uzByloZadane(slovoZadane) == true){
                etWord.setError("Zadané slovo již bylo použito!");
                zavibruj();
            }else{
                stopwatch.cancel();
            //    animation.cancel();
                int delkaZadaneho = slovoZadane.length() - 1;
                wordList.add(slovoZadane);
                if(gamerCount == 1){
                    String tah = trie.vratRandomSlovo(slovoZadane.charAt(delkaZadaneho));
                    while(uzByloZadane(tah) == true){
                        tah = trie.vratRandomSlovo(slovoZadane.charAt(delkaZadaneho));
                    }
                    wordList.add(tah);
                    pocitacZahral(tah);
                    tvWord.setText(tah);
                }else{
                    urciDalsihoHrace(slovoZadane);
                    prepniHrace();
                }
            }
        }
    }

    /**
     * Zobrazi dialog s tahem pocitace, aby mel hrac trosku vice casu na nej zareagovat
     * @param tah
     */
    private void pocitacZahral(String tah){
        prepnuti = new ProgressDialog(this);
        prepnuti.setCancelable(false);
        prepnuti.setProgressNumberFormat(null);
        prepnuti.setProgressPercentFormat(null);
        prepnuti.setTitle("Na tahu je počítač!");
        prepnuti.setMessage("Počítač zahrál slovo " + tah);
        prepnuti.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        prepnuti.show();
        cislo = 100;
//        new DalsiPlejer().execute("");
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                cislo = cislo - (100/5);
                prepnuti.setProgress(cislo);
            }

            public void onFinish() {
                prepnuti.dismiss();
                etWord.setText("");
                cislo = 100;
                spustOdpocet();
                //mic.startAnimation(animation);
            }
        }.start();
    }

    /**
     * zobrazi dialog pri prepinani hracu, aby mohlo dojit k vymene hracu
     */
    private void prepniHrace(){
        prepnuti = new ProgressDialog(this);
        prepnuti.setCancelable(false);
        prepnuti.setProgressNumberFormat(null);
        prepnuti.setProgressPercentFormat(null);
        prepnuti.setTitle("Přepnutí na dalšího hráče!");
        prepnuti.setMessage("Další na řadě je hráč " + gamers[playingGamer].getJmeno());
        prepnuti.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        prepnuti.show();
        cislo = 100;
//        new DalsiPlejer().execute("");
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                cislo = cislo - (100/5);
                prepnuti.setProgress(cislo);
            }

            public void onFinish() {
                prepnuti.dismiss();
                etWord.setText("");
                cislo = 100;
                spustOdpocet();
            //    mic.startAnimation(animation);
            }
        }.start();

    }

    private int najdiViteze(){
        int vitez = 0;
        for(int i = 0; i < gamerCount; i++){
            if(gamers[i].isHraje() == true){
                vitez = i;
            }
        }
        return vitez;
    }

    /**
     * zjistuje, ktery z uzivatelu je jeste ve hre a je dalsi na rade.
     * @param tah
     */
    private void urciDalsihoHrace(String tah){
        if(playingGamerCount == 1){
            int vitez = najdiViteze();
            konecHryMulti(vitez);
        }
        playingGamer++;
        if(playingGamer == gamerCount){
            playingGamer = 0;
        }
        String jmeno = gamers[playingGamer].getJmeno();
        int pom = 0;
        while(gamers[playingGamer].isHraje() == false && pom < gamerCount){
            playingGamer++;
            if(playingGamer == gamerCount){
                playingGamer = 0;
            }
            jmeno = gamers[playingGamer].getJmeno();
        }
        if(gamers[playingGamer].isHraje() == false){
            konecHry();
        }
        tvGamer.setText(jmeno);
        tvWord.setText(tah);
        if(gamers[playingGamer].isJineSlovo() == false){
            newWord.setEnabled(false);
        }else{
            newWord.setEnabled(true);
        }
        if(gamers[playingGamer].isPocitacuvTah() == false){
            comuterTurn.setEnabled(false);
        }else{
            comuterTurn.setEnabled(true);
        }
    }

    private boolean uzByloZadane(String slovo){
        for (String word:
                wordList) {
            if(slovo.equals(word) == true){
                return true;
            }
        }
        return false;
    }

    /**
     * inicializuje odpocet doby na zadani tahu
     */
    private void spustOdpocet(){

        stopwatch = new CountDownTimer(time*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvStopwatch.setText("" + millisUntilFinished / 1000);
                cislo = cislo - (100/time);
                mProgressBar.setProgress(cislo);
            }

            public void onFinish() {
                tvStopwatch.setText("Čas vypršel!");
//                animation.cancel();
                mProgressBar.setProgress(0);
                if(gamerCount == 1){
                    konecHry();
                }else{
                    ukonciHrace(playingGamer);
                    if(playingGamerCount == 1){
                        int vitez = najdiViteze();
                        konecHryMulti(vitez);
                    }else{
                        urciDalsihoHrace(tvWord.getText().toString());
                        prepniHrace();
                    }
                }
            }
        };

        stopwatch.start();
    }

    private void ukonciHrace(int cisloHrace){
        gamers[cisloHrace].setHraje(false);
        playingGamerCount--;
    }

    private void zrusHru(){
        Intent i = new Intent(this, ActivityNastaveni.class);
        startActivity(i);
    }

    /**
     * konec hry v singleplayeru
     */
    private void konecHry(){
        alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Hra končí!");
        alert.setMessage("Nestihl jsi najít odpověď na počítačův tah " + tvWord.getText().toString() + " hra končí!");
        alert.setButton(RESULT_OK, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.cancel();
                zrusHru();
            }
        });
        alert.show();
    }

    /**
     * konec hry v multiplayeru
     * @param vitez
     */
    private void konecHryMulti(int vitez){
        alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Hra končí!");
        alert.setMessage("Hra končí, vítězí hráč " + gamers[vitez].getJmeno() + ", který zahrál tah " + tvWord.getText().toString() + ", na který nenašel žádný hráč odpověď");
        alert.setButton(RESULT_OK, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.cancel();
                zrusHru();
            }
        });
        alert.show();
    }

    /**
     * zachycuje tlacitko zpet v panelu aplikace
     */
    @Override
    public void onBackPressed(){
        stopwatch.cancel();
        animation.cancel();
        zrusHru();

    }

    /**
     * zachycuje zpatecni tlacitko na panelu telefonu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    /**
     * asynchronni pozadavek na nacteni slovniku
     */
    private class InicializaceSlovniku extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            inicializujSlovnik();
            trie = slovnik.getTrie();
            return slovnik.getNahodne();
        }

        /**
         * slovnik je nacten, spusti se hra
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            progress.dismiss();
            tvWord.setText(result);
            wordList.add(result);
            playingGamer = 0;
            spustOdpocet();
         //   spustAnimaci();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class DalsiPlejer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            new CountDownTimer(5000, 1000) {

                public void onTick(long millisUntilFinished) {
                    cislo = cislo - (100/5);
                    prepnuti.setProgress(cislo);
                }

                public void onFinish() {
                }
            }.start();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            prepnuti.dismiss();
            etWord.setText("");
            cislo = 100;
            spustOdpocet();
          //  mic.startAnimation(animation);
        }
    }

    private void inicializujSlovnik(){
        InputStream ins;
        if(MainActivity.language.equals("CZE")){
            ins = getResources().openRawResource(R.raw.cze);
        }else{
            ins = getResources().openRawResource(R.raw.eng);
        }
        try {
            this.slovnik = new NacteniSlovniku(ins);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

