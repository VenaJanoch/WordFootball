package com.example.venaj.slovnifotbal;


import java.util.ArrayList;
import java.util.List;


public class Abeceda {


    private static String letters = "a, á, b, c, č, d, ď, e, é, ě, f, g, h, i, í, j, k, l, m, n, ň, o, ó, p, q, r, ř, s, š, t, ť, "
            + "u, ú, ů, v, w, x, y, ý, z, ž, ö, ü, -,  , '";

    public static List<Character> abeceda;


    public static void naplnAbecedu() {
        abeceda = new ArrayList<Character>();
        //String[] splitList = letters.split(", ");
        int index = 0;
        for (int i = 0; i < letters.length(); i+=3) {
            //System.out.println(letters.charAt(i));
            abeceda.add(index, letters.charAt(i));
            index++;
        }
    }

    public static int najdiPoziciPismena(char pismeno){
        for(int i = 0; i < abeceda.size(); i++){
            if(abeceda.get(i) == pismeno){
                return i;
            }
        }
        return 0;
    }

}
