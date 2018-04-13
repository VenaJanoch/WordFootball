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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * obsluhuje herni cyklus aplikace
 */

public class ActivityGame extends AppCompatActivity {

    private Trie trie;
    ProgressBar mProgressBar;
    CountDownTimer stopwatch;
    TextView tvStopwatch, tvWord, tvGamer;
    EditText etWord;
    Gamer[] gamers;
    int time, gamerCount, playingGamer, playingGamerCount;
    Dictionary dictionary;
    int number = 100;
    String[] gamerName;
    
    ArrayList<String> wordList;
    Animation animation;
    Button newWord, comuterTurn; // napovedy
    String dictionaryType;

    ProgressDialog progress, pdSwitchPlayer;
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
        tvWord = (TextView) findViewById(R.id.tv_word);
        tvStopwatch = (TextView) findViewById(R.id.tv_time);
        tvGamer = (TextView) findViewById(R.id.tv_actual_gamer);
        wordList = new ArrayList<>();

        initETGamer();
        initButton();
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

    private void initETGamer(){
        gamers = new Gamer[gamerCount];
        for(int i = 0; i < gamerCount; i++){
            gamers[i] = new Gamer(gamerName[i]);
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

        etWord = (EditText) findViewById(R.id.et_input_text);

        etWord.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)){
                    try {
                        confirmWord();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void initButton(){
        newWord = (Button) findViewById(R.id.anotherWord);
        comuterTurn = (Button) findViewById(R.id.tahPocitace);
        newWord.setEnabled(true);
        comuterTurn.setEnabled(true);
        newWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aktualniSlovo = tvWord.getText().toString();
                tvWord.setText(trie.returnRandomWord(aktualniSlovo.charAt(0)));
                gamers[playingGamer].setAnotherWord(false);
                newWord.setEnabled(false);
            }
        });

        comuterTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aktualniSlovo = tvWord.getText().toString();
                etWord.setText(trie.returnRandomWord(aktualniSlovo.charAt(aktualniSlovo.length()-1)));
                gamers[playingGamer].setComputerTurn(false);
                comuterTurn.setEnabled(false);
            }
        });
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    confirmWord();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void makeVib(){
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
    public void confirmWord() throws Exception {
        String previousWord = tvWord.getText().toString();
        String enteredWord = etWord.getText().toString();
        enteredWord.toLowerCase();
        enteredWord = enteredWord.trim();
        if(TextUtils.isEmpty(enteredWord) == true){
            etWord.setError(getString(R.string.gw_err_no_word));
            makeVib();
        }else{
            int lenght = previousWord.length() - 1;
            if(enteredWord.charAt(0) != previousWord.charAt(lenght)){
                etWord.setError(getString(R.string.gw_err_bad_letterr));
                makeVib();
            }else if(trie.findWord(enteredWord) == false){
                etWord.setError(getString(R.string.gw_err_bad_noun));
                makeVib();
            }else if(checkEnteredWord(enteredWord) == true){
                etWord.setError(getString(R.string.gw_err_bad_word));
                makeVib();
            }else{
                stopwatch.cancel();
                int enteredLenght = enteredWord.length() - 1;
                wordList.add(enteredWord);
                if(gamerCount == 1){
                    String turn = trie.returnRandomWord(enteredWord.charAt(enteredLenght));
                    while(checkEnteredWord(turn) == true){
                        turn = trie.returnRandomWord(enteredWord.charAt(enteredLenght));
                    }
                    wordList.add(turn);
                    makeComputerTurn(turn);
                    tvWord.setText(turn);
                }else{
                    chooseNextPlayer(enteredWord);
                    switchPlayer();
                }
            }
        }
    }

    /**
     * Zobrazi dialog s tahem pocitace, aby mel hrac trosku vice casu na nej zareagovat
     * @param turn
     */
    private void makeComputerTurn(String turn){
        pdSwitchPlayer = new ProgressDialog(this);
        pdSwitchPlayer.setCancelable(false);
        pdSwitchPlayer.setProgressNumberFormat(null);
        pdSwitchPlayer.setProgressPercentFormat(null);
        pdSwitchPlayer.setTitle(getString(R.string.gw_pd_computer_turn));
        pdSwitchPlayer.setMessage(getString(R.string.gw_pd_computer_word) + turn);
        pdSwitchPlayer.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pdSwitchPlayer.show();
        number = 100;

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                number = number - (100/5);
                pdSwitchPlayer.setProgress(number);
            }

            public void onFinish() {
                pdSwitchPlayer.dismiss();
                etWord.setText("");
                number = 100;
                startStopwatch();

            }
        }.start();
    }

    /**
     * zobrazi dialog pri prepinani hracu, aby mohlo dojit k vymene hracu
     */
    private void switchPlayer(){
        pdSwitchPlayer = new ProgressDialog(this);
        pdSwitchPlayer.setCancelable(false);
        pdSwitchPlayer.setProgressNumberFormat(null);
        pdSwitchPlayer.setProgressPercentFormat(null);
        pdSwitchPlayer.setTitle(getString(R.string.gw_pd_switch_player));
        pdSwitchPlayer.setMessage(getString(R.string.gw_pd_next_player)+ gamers[playingGamer].getName());
        pdSwitchPlayer.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pdSwitchPlayer.show();
        number = 100;

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                number = number - (100/5);
                pdSwitchPlayer.setProgress(number);
            }

            public void onFinish() {
                pdSwitchPlayer.dismiss();
                etWord.setText("");
                number = 100;
                startStopwatch();
            //    mic.startAnimation(animation);
            }
        }.start();

    }

    private int findWinner(){
        int vitez = 0;
        for(int i = 0; i < gamerCount; i++){
            if(gamers[i].isInGame() == true){
                vitez = i;
            }
        }
        return vitez;
    }

    /**
     * zjistuje, ktery z uzivatelu je jeste ve hre a je dalsi na rade.
     * @param turn
     */
    private void chooseNextPlayer(String turn){
        if(playingGamerCount == 1){
            int winner = findWinner();
            endGameMulti(winner);
        }
        playingGamer++;
        if(playingGamer == gamerCount){
            playingGamer = 0;
        }
        String name = gamers[playingGamer].getName();
        int pom = 0;
        while(gamers[playingGamer].isInGame() == false && pom < gamerCount){
            playingGamer++;
            if(playingGamer == gamerCount){
                playingGamer = 0;
            }
            name = gamers[playingGamer].getName();
        }
        if(gamers[playingGamer].isInGame() == false){
            endGame();
        }
        tvGamer.setText(name);
        tvWord.setText(turn);
        if(gamers[playingGamer].isAnotherWord() == false){
            newWord.setEnabled(false);
        }else{
            newWord.setEnabled(true);
        }
        if(gamers[playingGamer].isComputerTurn() == false){
            comuterTurn.setEnabled(false);
        }else{
            comuterTurn.setEnabled(true);
        }
    }

    private boolean checkEnteredWord(String enteredWord){
        for (String word: wordList) {
            if(enteredWord.equals(word) == true){
                return true;
            }
        }
        return false;
    }

    /**
     * inicializuje odpocet doby na zadani tahu
     */
    private void startStopwatch(){

        stopwatch = new CountDownTimer(time*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvStopwatch.setText("" + millisUntilFinished / 1000);
                number = number - (100/time);
                mProgressBar.setProgress(number);
            }

            public void onFinish() {
                tvStopwatch.setText(getString(R.string.gw_tv_time_out));

                mProgressBar.setProgress(0);
                if(gamerCount == 1){
                    endGame();
                }else{
                    deletePlayer(playingGamer);
                    if(playingGamerCount == 1){
                        int winner = findWinner();
                        endGameMulti(winner);
                    }else{
                        chooseNextPlayer(tvWord.getText().toString());
                        switchPlayer();
                    }
                }
            }
        };

        stopwatch.start();
    }

    private void deletePlayer(int playerID){
        gamers[playerID].setInGame(false);
        playingGamerCount--;
    }

    private void cancelGame(){
        Intent i = new Intent(this, ActivitySetting.class);
        startActivity(i);
    }

    /**
     * konec hry v singleplayeru
     */
    private void endGame(){
        alert = new AlertDialog.Builder(this).create();
        alert.setTitle(getString(R.string.gw_ad_end_game));
        alert.setMessage(getString(R.string.gw_ad_computer_turn) + tvWord.getText().toString() + getString(R.string.gw_ad_end_game));
        alert.setButton(RESULT_OK, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.cancel();
                cancelGame();
            }
        });
        alert.show();
    }

    /**
     * konec hry v multiplayeru
     * @param vitez
     */
    private void endGameMulti(int vitez){
        alert = new AlertDialog.Builder(this).create();
        alert.setTitle(getString(R.string.gw_ad_end_game));
        alert.setMessage(getString(R.string.gw_ad_multiple_turn, gamers[vitez].getName(), tvWord.getText().toString()));
        alert.setButton(RESULT_OK, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.cancel();
                cancelGame();
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
        cancelGame();

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
            initDictionary();
            trie = dictionary.getTrie();
            return dictionary.getRandomWord();
        }

        /**
         * dictionary je nacten, spusti se hra
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            progress.dismiss();
            tvWord.setText(result);
            wordList.add(result);
            playingGamer = 0;
            startStopwatch();
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
                    number = number - (100/5);
                    pdSwitchPlayer.setProgress(number);
                }

                public void onFinish() {
                }
            }.start();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdSwitchPlayer.dismiss();
            etWord.setText("");
            number = 100;
            startStopwatch();
        }
    }

    private void initDictionary(){
        InputStream ins;
        if(ActivitySetting.dictionary .equals(getString(R.string.cze_language))){
            ins = getResources().openRawResource(R.raw.cze);
        }else{
            ins = getResources().openRawResource(R.raw.eng);
        }
        try {
            this.dictionary = new Dictionary(ins);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

