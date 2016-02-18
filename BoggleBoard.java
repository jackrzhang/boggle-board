/* 
 * BoggleBoard.java
 *
 * @author jackrzhang
 * @version February 5, 2015
 * 
 * Emulates a Boggle Game Board and determines if the board contains input words
 * NOTE: This program does NOT handle reading in a dictionary file, so the program
 * only handles the finding of Strings within the boggle board, not necessarily
 * real words.
*/
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class BoggleBoard 
{
    private final static int SIZE = 6; 
    // SIZE is two units longer for borders in the array, thus actual board size is 4
    private static String[][] board = new String[SIZE][SIZE];
    
    private static String[] cubes = { "COTAIA", "OSEWDN", "LERASC", "VGTZEI", "EYUGKL",
            "DUNOKT", "HYFEIE", "DVNZAE", "OQAMJB", "LEPUTS", "NEPIHS", 
            "LUWIGR", "XIBOFR", "ALIBTY", "OMARSH", "DCEAPM" };
    
    private static String inputWord; // Original user input word to be searched
    private static String nextPiece;
    private static String stored;
    
    public static void main(String[] args)
    {
        generateBoard();
        
        int numWordsFound = 0; // counter for number of words found
        ArrayList<String> wordsFound = new ArrayList<String>();
        boolean found;

        boolean terminate = false; // Player can find words indefinitely
        while ( terminate == false ) 
        {
            System.out.println("BOGGLE - J. Zhang");
            printBoard();
            System.out.println("\nWords found: " + numWordsFound);
            
            Scanner sc = new Scanner(System.in); // User inputs word to be found/not found
            System.out.print("Find a word: ");
            inputWord = sc.nextLine();
            inputWord = inputWord.toUpperCase(); // standardizes letters to uppercase
        
            //Checks to see if the inputWord has already been found
            found = false;
            for ( int i = 0; i < wordsFound.size(); i++ ) {
                if ( inputWord.equals(wordsFound.get(i)) )
                    found = true;
            }
            
            if ( found == true ) {
                System.out.println("Word already found.");
            }
            else if ( inputWord.length() < 4 ) {
                System.out.println("Word must be longer than four characters.");
            }
            else if ( wordPresent() ) {
                System.out.println("Yes - " + inputWord + " is on the board.");
                wordsFound.add(inputWord);
                numWordsFound++;
            }               
            else
                System.out.println("No - " + inputWord + " is not on the board.");
            
            try {
                Thread.sleep(1000); // delay for 1000 milliseconds
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 50; ++i) System.out.println(); // Clear Console
        }
        
    }
    
    /**
     * By starting at each board position, determines if the board contains the inputWord
     * @return true if the inputWord is found within the board
     * @return false if the inputWord is not found within the board
     */
    private static boolean wordPresent()
    {
        for ( int y = 1; y < SIZE-1; y++ )
        {
            for ( int x = 1; x < SIZE-1; x++ )
            {
                stored = "";
                nextPiece = "";
                if (findWord("", y, x).equals(inputWord)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Recursive helper method for the wordPresent method
     * @param builder - the current portion of the inputWord that has been found
     * @param row - row at which the search starts
     * @param column - column at which the seart starts
     * @return the String builder(equal to inputWord) if the inputWord is found
     * @return "*NOPE*" is the inputWord is not found
     */
    private static String findWord( String builder, int row, int column )
    {
        // base case(s), where all letters have been found OR
        if ( inputWord.equals("") ) {
            inputWord = stored; // Resets the found inputWord
            for ( int y = 1; y < SIZE-1; y++ ) {
                for ( int x = 1; x < SIZE-1; x++ ) {
                    board[y][x] = board[y][x].toUpperCase(); // Resets the board
                }
            }
            return builder;
        }
        else
        {
            // Store the next letter from inputWord, then remove that same letter from
            // inputWord itself
            nextPiece = Character.toString(inputWord.charAt(0)); 
            inputWord = inputWord.substring(1, inputWord.length());
            stored = stored.concat(nextPiece); // Storage for all found pieces
            
            // Changes the current piece so it cannot be added twice to the same word
            board[row][column] = board[row][column].toLowerCase(); 
            
            if ( board[row-1][column-1].equals(nextPiece)) {
                builder = builder.concat(nextPiece);
                return findWord(builder, row-1, column-1);
            }
            else if ( board[row-1][column].equals(nextPiece)) {
                builder = builder.concat(nextPiece);
                return findWord(builder, row-1, column);
            }
            else if ( board[row-1][column+1].equals(nextPiece)) {
                builder = builder.concat(nextPiece);
                return findWord(builder, row-1, column+1);
            }
            else if ( board[row][column-1].equals(nextPiece)) {
                builder = builder.concat(nextPiece);
                return findWord(builder, row, column-1);
            }
            else if ( board[row][column+1].equals(nextPiece)) {
                builder = builder.concat(nextPiece);
                return findWord(builder, row, column+1);
            }
            else if ( board[row+1][column-1].equals(nextPiece)) {
                builder = builder.concat(nextPiece);
                return findWord(builder, row+1, column-1);
            }
            else if ( board[row+1][column].equals(nextPiece)) {
                builder = builder.concat(nextPiece);
                return findWord(builder, row+1, column);
            }
            else if ( board[row+1][column+1].equals(stored)) {
                builder = builder.concat(nextPiece);
                return findWord(builder, row+1, column+1);
            }
            else { // if no surrounding letters match the next letter of the inputWord
                inputWord = stored.concat(inputWord); // undos adjustments              
                for ( int y = 1; y < SIZE-1; y++ ) {
                    for ( int x = 1; x < SIZE-1; x++ ) {
                        board[y][x] = board[y][x].toUpperCase(); // resets the board
                    }
                }
 
                return "*NOPE*"; // other base case, where the word is not found
            }
        }
    }
    
    /**
     * Randomly generate the boggle board using the cubes
     */
    private static void generateBoard()
    {
        Random rand = new Random();
        int r;
        
        // Randomly determines the positions of the cubes on the board
        int[] cubePositions = new int[cubes.length];
        for ( int i = cubes.length-1; i > 0; i-- )
        {
            r = rand.nextInt(i+1);
            cubePositions[i] = r;
        }
        
        // Places a random character from each cube into each location on the board
        int cubePositionsIndex = 0;
        for(int y = 0; y < SIZE; y++)
        {
            for(int x = 0; x < SIZE; x++)
            {
                if ( x == 0 || y == 0 || x == SIZE-1 || y == SIZE-1 ) {
                    board[y][x] = ".";
                }
                
                else {
                    r = rand.nextInt(6); // 6 characters/sides on each cube
                    board[y][x] = cubes[cubePositions[cubePositionsIndex]].substring(r, r+1);
                    
                    // Searches for the QU exception (Only Q is present in cubes[8])
                    if ( cubePositions[cubePositionsIndex] == 8 && r == 1 ) 
                        board[y][x] = board[y][x].concat("");
                        
                    cubePositionsIndex++; // moves to cube for the next board position
                }
                
            }
        }
    }
    
    /**
     * Print the board (instantiated as a 2D String array) to console
     */
    private static void printBoard()
    {
        for(int y = 0; y < SIZE; y++)
        {
            for(int x = 0; x < SIZE; x++)
            {
                if ( board[y][x].equals("QU") ) // Spacing for QU
                    System.out.print(" " + board[y][x]);
                else
                    System.out.print("  " + board[y][x]);
            }
            System.out.println();
        }
    }
    
}
