package com.example.venaj.slovnifotbal;


import java.util.ArrayList;
import java.util.List;

/**
 * reprezentuje uzel trie
 */
public class TrieNode {
    private TrieNode parent; // odkaz na rodice
    private List<TrieNode> children; // seznam potomku

    private char value;
    private int listSize;

    private boolean word;

    public static int count = 0;

    public TrieNode(char letter) {

        children = new ArrayList<>();
        listSize = Alphabet.abeceda.size();

        fillList();
        value = letter;
        word = false;
        count++;
    }

    /**
     * prida do trie nove slovo
     * @param word
     */
    public void addNode(String word) {
        char letter = ' ';
        try {
            letter = word.charAt(0);

            int position = Alphabet.abeceda.indexOf(letter);
            TrieNode child = children.get(position);
            if (child==null) {
                child = new TrieNode(letter);
                child.setParent(this);
                children.remove(position);
                children.add(position, child);
            }


            if (word.length()>1) {
                child.addNode(word.substring(1));
            } else {
                child.setIsWord(true);
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
        try {
            int position = Alphabet.abeceda.indexOf(letter);

    if (position==-1) {
                return null;
            }
            TrieNode child = children.get(position);

            return child;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Vyjimka pri hledani znaku " + letter);
        }
        return null;
    }

    public void setParent(TrieNode par) {
        parent = par;
    }

    public void setIsWord(boolean w) {
        word = w;
    }

    public boolean getIsWord() {
        return word;
    }

    public void fillList() {
        for (int i = 0; i<listSize; i++) {
            children.add(i, null);
        }
    }

    public List<TrieNode> getChildren() {
        return children;
    }

    public void setChildren(List<TrieNode> children) {
        this.children = children;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public TrieNode getParent() {
        return this.parent;
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
    }

}

