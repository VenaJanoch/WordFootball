package com.example.venaj.slovnifotbal;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class Dictionary {


    Trie trie;

    private String randomWord;


    private InputStreamReader stream;
    /**
     * Input stream.
     */
    private InputStream fileInput;

    /**
     * inicializuje vstupni streamy a nacte dictionary do globalni promenne trie
     * @param fileInput
     * @throws Exception
     */
    public Dictionary(InputStream fileInput) throws Exception{
        this.fileInput = fileInput;
        loadDictionary();
    }




    public void loadDictionary() throws Exception {


        Random rand = new Random();

        try {;
            stream = new InputStreamReader(fileInput);
            trie = new Trie();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
        int i = rand.nextInt(4000);
        int k = 0;
        try {
            BufferedReader in = new BufferedReader(stream);
            String line;
            while ((line = in.readLine()) != null) {
                if(k == i){
                    randomWord = line.toLowerCase();
                }
                line = line.toLowerCase();
                trie.addWord(line);
                k++;
            }
            stream.close();
            fileInput.close();
            System.out.println("Dictionary load");
            System.out.println("--------------");
        } catch (Exception ex) {
            throw ex;
        }

    }

    public Trie getTrie() {
        return trie;
    }

    public void setTrie(Trie trie) {
        this.trie = trie;
    }

    public String getRandomWord() {
        return randomWord;
    }

    public void setRandomWord(String randomWord) {
        this.randomWord = randomWord;
    }


}
