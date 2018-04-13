package com.example.venaj.slovnifotbal;


import java.util.ArrayList;
import java.util.List;

/**
 * reprezentuje uzel trie
 */
public class TrieNode {
    private TrieNode rodic; // odkaz na rodice
    private List<TrieNode> potomci; // seznam potomku

    private char value;
    private int listSize;

    private boolean word;
    //Alphabet alphabet;
    public static int count = 0;

    public TrieNode(char letter) {
        //alphabet = new Alphabet();
        potomci = new ArrayList<TrieNode>();
        listSize = Abeceda.abeceda.size();
        //System.out.println(listSize);
        fillList();
        //System.out.println("new node " + descendants.size());
        value = letter;
        word = false;
        count++;
        //System.out.println(count + " " + letter);
    }

    /**
     * prida do trie nove slovo
     * @param word
     */
    public void addNode(String word) {
        char letter = ' ';
        try {
            letter = word.charAt(0);
            //System.out.println(letter + " " + value);
            //int position = letter - 'a';

            int position = Abeceda.abeceda.indexOf(letter);
            //System.out.println("position " + position);
            //TrieNode child = new TrieNode(letter);
            //System.out.println(word + position + " "+ word.length());
            TrieNode child = potomci.get(position);
            //TrieNode child = descendants.get(position);
            //if (descendants.contains(child)) {
            //if (child!=null) {
            //child = descendants.get(position);
            //System.out.println("Jiz pridano");
            //} else {
            //child = new TrieNode(letter);
            if (child==null) {
                child = new TrieNode(letter);
                child.setParent(this);
                potomci.remove(position);
                potomci.add(position, child);
                //System.out.println("Pridan " + letter);
            }


            if (word.length()>1) {
                child.addNode(word.substring(1));
            } else {
                child.setIsWord(true);
                //System.out.println("True " + letter);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Vyjimka pri pridani " + letter);
            throw ex;
        }

    }

    /**
     * najde a vrati uzel podle pismena v parametru
     * @param letter
     * @return
     */
    public TrieNode findNode(char letter) {
        //int position = letter - 'a';
        try {
            int position = Abeceda.abeceda.indexOf(letter);


            //TrieNode child = new TrieNode(letter);
            //System.out.println(descendants.indexOf(child) + " " + position);
            //System.out.println(letter + position);
            if (position==-1) {
                return null;
            }
            TrieNode child = potomci.get(position);

            //if (descendants.contains(child)) {
            //if (child!=null)
            //child = descendants.get(position);
            //System.out.println("Nalezen " + child.value);
            //} else {
            //child = null;
            //System.out.println("Nenalezen "  + letter);
            //}
            return child;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Vyjimka pri hledani znaku " + letter);
        }
        return null;
    }

    public void setParent(TrieNode par) {
        rodic = par;
    }

    public void setIsWord(boolean w) {
        word = w;
    }

    public boolean getIsWord() {
        return word;
    }

    public void fillList() {
        for (int i = 0; i<listSize; i++) {
            potomci.add(i, null);
        }
    }

    public List<TrieNode> getPotomci() {
        return potomci;
    }

    public void setPotomci(List<TrieNode> potomci) {
        this.potomci = potomci;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public TrieNode getParent() {
        return this.rodic;
    }


    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        TrieNode object = (TrieNode) obj;
        if (object == null) {
            return false;
        }
        if (object.value==this.value) {
            return true;
        } else {
            return false;
        }
        //return super.equals(obj);
    }

}

