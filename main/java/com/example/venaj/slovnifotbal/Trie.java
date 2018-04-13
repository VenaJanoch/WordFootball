package com.example.venaj.slovnifotbal;



import java.util.ArrayList;
import java.util.Random;

/**
 * reprezentuje strukturu trie
 */
public class Trie {

    private TrieNode koren;

    //private Alphabet alphabet;

    public Trie() {
        Abeceda.naplnAbecedu();
        //alphabet = new Alphabet();
        koren = new TrieNode(' ');
    }

    /**
     * zavola metodu na pridani uzlu z tridy TrieNode z korene.
     * @param word
     * @throws Exception
     */
    public void addWord(String word) throws Exception {
        koren.addNode(word);
        //System.out.println("Pridano " + word);
    }

    /**
     * vraci true/false podle toho, zda se slovo v parametru nachazi ve slovniku nebo ne
     * @param word
     * @return
     * @throws Exception
     */
    public boolean findWord(String word) throws Exception {
        TrieNode node = koren;

        for (int i =0; i<word.length(); i++) {
            char letter = word.charAt(i);
            TrieNode child = node.findNode(letter);
            //System.out.println(letter);
            if (child==null) {
                //System.out.println("nenalezeno " + word);
                return false;
            }
            node = child;
        }

        if (node.getIsWord()) {
            //System.out.println("nalezeno " + word);
            return true;
        } else {
            return false;
        }

    }

    /**
     * vrati nahodne slovo zacinajici na pismeno z parametru
     * @param prvniPismeno
     * @return
     */
    public String vratRandomSlovo(char prvniPismeno){
        TrieNode node = koren;
        node = node.getPotomci().get(Abeceda.najdiPoziciPismena(prvniPismeno));
        String slovo = "" + node.getValue();
        ArrayList<Integer> indexyNenulovych = new ArrayList<>();
        indexyNenulovych.add(5);
        Random r = new Random();
        while(indexyNenulovych.size() != 0){
            indexyNenulovych.clear();
            for(int i = 0; i < node.getPotomci().size(); i++){
                if(node.getPotomci().get(i) != null){
                    indexyNenulovych.add(i);
                }
            }
            if(indexyNenulovych.size() == 0){
                break;
            }
            int random = r.nextInt(indexyNenulovych.size());
            node = node.getPotomci().get(indexyNenulovych.get(random));
            slovo += node.getValue();
        }
        System.out.println(slovo);
        return slovo;
    }
}
