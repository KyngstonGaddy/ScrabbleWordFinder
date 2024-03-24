import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Figures out all the playable scrabble words based on the given tile rack
 * @author Kyngston Gaddy
 * @version 03.24.2024
 * Extra: Handling blank tiles
 * Note: Andrew gave me tips on how to get my extra to work
 */
public class ScrabbleWordFinder {
    private ArrayList<String> dictionary;
    private String alpha = "EEEEEEEEEEEEAAAAAAAAAIIIIIIIIIOOOOOOOONNNNNNRRRRRRTTTTTTLLLLSSSSUUUUDDDDGGGBBCCMMPPFFHHVVWWYYKJXQZ  ";
    private ArrayList<String> tileRack;

    /** class constructor, initializes ArrayList<String> dictionary */
    public ScrabbleWordFinder() {
        dictionary = new ArrayList<>();
        tileRack = new ArrayList<>();
        buildDictionary();
        drawTiles();
    }

    private void buildDictionary() {
        try {
            Scanner in = new Scanner(new File("SCRABBLE_WORDS.txt"));
            while(in.hasNext()) {
                dictionary.add((in.nextLine().trim()));
            }
            in.close();
            Collections.sort(dictionary);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void drawTiles()    {
        ArrayList<String> tiles = new ArrayList<>();
        for(int i = 0; i < alpha.length(); i++) {
            tiles.add(alpha.substring(i, i+1));
        }
        for(int i = 0; i < 7; i++) {
            tileRack.add(tiles.remove((int)(Math.random()*tiles.size())));
        }
        Collections.shuffle(tileRack);
    }

    /** displays the contents of the player's tile rack */
    public void printRack() {
        System.out.println(tileRack);
    }

    /**
     * builds and returns an ArrayList of String objects that are values pulled from
     * the dictionary/database based on the available letters in the user's tile rack
     * @return wordPlays, all the playable words based on tile rack
     */
    public ArrayList<String> getPlaylist() {
        ArrayList<String> wordPlays = new ArrayList<>();
        for(String word : dictionary) {
            if(isPlayable(word)) {
                if(word.length() == 7){
                    String bingo = word + "*";
                    wordPlays.add(bingo);
                }
                else
                    wordPlays.add(word);
            }
        }
        return wordPlays;
    }

    private boolean isPlayable(String word) {
        ArrayList<String> rackCopy = new ArrayList<>(tileRack);
        int blankCount = 0; // my extra
        int skippedLetters = 0;
        for(int i = 0; i < 7; i++) { // counts the amount of blank spaces in the rack
            if(rackCopy.get(i).equals(" "))
                blankCount++;
        }
        for (int i = 0; i < word.length(); i++) { // counts how many letters are skipped
            if(!(rackCopy.remove(word.substring(i,i+1))))
                skippedLetters++;
        }
        if(blankCount>=skippedLetters) { //if the blank tiles are greater than or equal to the skipped letters, then the word will return true
            return true;
        }
        else { //if there are more skipped letters than blank tiles, then the word cannot be played
            for (int i = 0; i < word.length(); i++) {
                if (!(rackCopy.remove(word.substring(i, i + 1))))
                    return false;
            }
        }
        return true;
    }

    /** print all the playable words based on the letters in the tile rack*/
    public void printMatches() {
        ArrayList<String> matches = getPlaylist();
        System.out.println("You can play the following words from the letters in your tile rack: ");
        if(matches.size() > 0) { //if there are playable words from the given tiles, then print them
            int col = 0;
            for (String word : matches) {
                System.out.printf("%-10s", word);
                col++;
                if (col == 10) {
                    System.out.println();
                    col = 0;
                }
            }
            System.out.println("\n * denotes BINGO!");
        }
        else // otherwise, tell the user that no words can be played from tiles
            System.out.println("Sorry, NO words can be played from those tiles.");
    }

    /**
     * main method for the class; use only 3 command lines in main
     * @param args command line args, if needed
     */
    public static void main(String[] args){
        ScrabbleWordFinder app = new ScrabbleWordFinder();
        app.printRack();
        app.printMatches();
    }
}
