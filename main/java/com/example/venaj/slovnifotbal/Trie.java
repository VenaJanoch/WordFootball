package com.example.venaj.slovnifotbal;



import java.util.ArrayList;
import java.util.Random;

/**
 * reprezentuje strukturu trie
 */
public class Trie {

    private TrieNode root;

    //private Alphabet alphabet;

    public Trie() {
        Alphabet.fillAlphabet();
        root = new TrieNode(' ');
    }

    /**
     * zavola metodu na pridani uzlu z tridy TrieNode z korene.
     * @param word
     * @throws Exception
     */
    public void addWord(String word) throws Exception {
        root.addNode(word);

    }

    /**
     * vraci true/false podle toho, zda se slovo v parametru nachazi ve slovniku nebo ne
     * @param word
     * @return
     * @throws Exception
     */
    public boolean findWord(String word) throws Exception {
        TrieNode node = root;

        for (int i =0; i<word.length(); i++) {
            char letter = word.charAt(i);
            TrieNode child = node.findNode(letter);

            if (child==null) {

                return false;
            }
            node = child;
        }

        if (node.getIsWord()) {

            return true;
        } else {
            return false;
        }

    }

    /**
     * vrati nahodne slovo zacinajici na pismeno z parametru
     * @param firstChar
     * @return
     */
    public String returnRandomWord(char firstChar){
        TrieNode node = root;
        node = node.getChildren().get(Alphabet.findLetterPosition(firstChar));
        String word = "" + node.getValue();
        ArrayList<Integer> fullIndexs = new ArrayList<>();
        fullIndexs.add(5);
        Random r = new Random();
        while(fullIndexs.size() != 0){
            fullIndexs.clear();
            for(int i = 0; i < node.getChildren().size(); i++){
                if(node.getChildren().get(i) != null){
                    fullIndexs.add(i);
                }
            }
            if(fullIndexs.size() == 0){
                break;
            }
            int random = r.nextInt(fullIndexs.size());
            node = node.getChildren().get(fullIndexs.get(random));
            word += node.getValue();
        }

        return word;
    }
}
