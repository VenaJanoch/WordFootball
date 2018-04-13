package com.example.venaj.slovnifotbal;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class NacteniSlovniku {


    Trie trie;

    private String nahodne;


    private InputStreamReader stream;
    /**
     * Input stream.
     */
    private InputStream fileInput;

    /**
     * inicializuje vstupni streamy a nacte slovnik do globalni promenne trie
     * @param fileInput
     * @throws Exception
     */
    public NacteniSlovniku(InputStream fileInput) throws Exception{
        this.fileInput = fileInput;
        vytvorStream();
        nactiSlovnik();
    }

    public void vytvorStream() throws Exception {
        try {;
            stream = new InputStreamReader(fileInput);
            trie = new Trie();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }


    public void nactiSlovnik() throws Exception {
        Random rand = new Random();
        int i = rand.nextInt(4000);
        int k = 0;
        try {
//			trie = new Trie();
//			InputStream localInputStream = NacteniSlovniku.class.getResourceAsStream(fileName);
//			BufferedReader in = new BufferedReader(new InputStreamReader(localInputStream, "utf-8"));
            BufferedReader in = new BufferedReader(stream);
            String line;
            while ((line = in.readLine()) != null) {
                if(k == i){
                    nahodne = line.toLowerCase();
                }
//				System.out.println(line);
                line = line.toLowerCase();
                trie.addWord(line);
                k++;
            }
//			localInputStream.close();
            stream.close();
            fileInput.close();
            System.out.println("Slovnik nacten");
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

    public String getNahodne() {
        return nahodne;
    }

    public void setNahodne(String nahodne) {
        this.nahodne = nahodne;
    }


}
