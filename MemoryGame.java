/* Name: David Lam
 * Date: 3/1/2020
 * Description: Creating a children's memory matching game
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class MemoryGame {
    public static void main (String[] args) {
        final int rows = 4, cols = 4;
        String[][] strArray = new String[rows][cols];
        boolean[][] boolArray = new boolean[rows][cols];
        while (true) {
            readFile(getFileChoice(), strArray);
            int matchCounter = 0; // S8
            // once there are 16 cards, there are 8 matches
            int turnShuffle = 1; // On and off switch for shuffle method ( 1 being on, 0 is off)

            while (matchCounter != 8) { // While loop only initializes if there is less than 8 matches
                if (turnShuffle == 1) {
                    shuffle(strArray); // shuffles deck
                }
                displayBoard(strArray, boolArray);
                int card1 = getChoice();

                while (checkFlipped(card1, boolArray)) { // loop initializes only when card1 is already flipped
                    // Prompts user to pick another card
                    System.out.println("Card is already flipped up. Pick another card.");
                    System.out.println("");
                    card1 = getChoice();
                }

                flipChoice(card1, boolArray);
                displayBoard(strArray, boolArray);
                int card2 = getChoice();

                while (checkFlipped(card2, boolArray)) { // loop initializes only when card2 is already flipped
                    // Prompts user to pick another card
                    System.out.println("Card is already flipped up. Pick another card.");
                    System.out.println("");
                    card2 = getChoice();
                }

                flipChoice(card2, boolArray);
                displayBoard(strArray, boolArray);
                // If the chosen two cards are not a match, turn off shuffle and flip down cards.
                // Continues and runs code starting from second while loop
                if (!isMatch(card1, card2, strArray)) {
                    turnShuffle = 0;
                    flipChoice(card1,boolArray);
                    flipChoice(card2,boolArray);
                }
                else {
                    // If the cards match, add a counter and turn off shuffle.
                    // Continues and runs code starting from second while loop
                    matchCounter += 1;
                    turnShuffle = 0;
                }
            }
            // Prompts user if they want to play again
            System.out.println("You found all 8 matches you win!");
            System.out.println("Do you want to play again? Y/N?: ");
            boolean userInput = CheckInput.getYesNo();
            if (!userInput) {
                // If user inputs "no" or "n" breaks out and ends program
                System.out.println("Goodbye!");
                break;
            }
            else {
                // If user wants to play again, iterate through boolean array and set all values to false
                // so the the cards display facedown when program runs again
                for (int i = 0; i < boolArray.length; i++) {
                    for (int j = 0; j < boolArray.length;j++) {
                        boolArray[i][j] = false;
                    }
                }
            }

        }
    }
    /**
     * getFileChoice prompts a menu of files
     * @return the choice of file they chose
     */
    public static int getFileChoice() {
        System.out.println("Memory Game\n" +
                            "1. Letters\n" +
                            "2. Numbers\n" +
                            "3. Animals\n" +
                            "4. Objects\n");
        System.out.print("Enter Choice: ");
        return CheckInput.getIntRange(1,4);
    }

    /**
     * readFile reads in the file and populates the string array
     * @param fileChoice the choice user picks from getFileChoice()
     * @param array passes in string array
     */
    public static void readFile(int fileChoice, String[][] array) {
        switch (fileChoice) {
            case 1:
                populateArray(new File("letters.txt"),array);
                break;

            case 2:
                populateArray(new File("numbers.txt"),array);
                break;

            case 3:
                populateArray(new File("animals.txt"),array);
                break;

            case 4:
                populateArray(new File("objects.txt"),array);
                break;
        }
    }

    /**
     * populateArray iterates through empty array and fills the array with the contents of the file
     * @param file The file user wants to play with
     * @param array String array
     */
    private static void populateArray(File file, String[][] array) {
        try {
            Scanner read = new Scanner(new File(String.valueOf(file)));
            for (int i = 0; i < array.length && read.hasNext(); i++) {
                for (int j = 0; j < array.length; j++) {
                    String value = read.nextLine();
                    array[i][j] = value;
                    j++;
                    array[i][j] = value;
                }
            }
            read.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Invalid File");
        }
    }

    /**
     * shuffle takes two random locations and swaps them. This process repeats for 100 times
     * @param array String array
     */
    public static void shuffle(String[][] array) {
        Random rand = new Random();
        int max = 4;
        for (int i = 0; i <= 100; i++) {

            // First Location
            int randRow = rand.nextInt(max);
            int randCol = rand.nextInt(max);
            String value1 = array[randRow][randCol]; // Saves the value at that location

            // Second Location
            int randRow2 = rand.nextInt(max);
            int randCol2 = rand.nextInt(max);
            String value2 = array[randRow2][randCol2];

            // Swaps the values at those locations
            array[randRow][randCol] = value2;
            array[randRow2][randCol2] = value1;
        }
    }

    /**
     * getChoice asks for the card number
     * @return the card number (1-16)
     */
    public static int getChoice() {
        System.out.println("Enter the card number would you like to pick: ");
        int input = CheckInput.getIntRange(1,16);
        return input;
    }

    /**
     * flipChoice flips cards faceup or facedown depending on value in boolean array (True or False)
     * @param choice  card value from getChoice
     * @param boolArray the boolean array
     */
    public static void flipChoice(int choice, boolean[][] boolArray) {
        int row = 0;
        int col = 0;
        if (choice <= 4) {
             row = 0;
             col = (choice - 1);
        }
        else if (choice <= 8) {
            row = 1;
            col = (choice - 4) - 1;
        }
        else if (choice <= 12) {
            row = 2;
            col = (choice - 8) - 1;
        }
        else if (choice <= 16) {
            row = 3;
            col = (choice - 12) - 1;
        }
        boolArray[row][col] = !boolArray[row][col]; // Condition to either flip up or down the card

    }

    /**
     * displayBoard displays the card with either the number if face down, or value if face up
     * @param array String Array
     * @param boolArray Boolean Array
     */
    public static void displayBoard(String[][] array, boolean[][] boolArray) {
        int num = 1;
        for(int i = 0; i < 4; i++){
            System.out.println("+- - - -+" + " " + "+- - - -+" + " " + "+- - - -+" + " " + "+- - - -+");
            System.out.println("|       |" + " " + "|       |" + " " + "|       |" + " " + "|       |");
            for(int j = 0; j < 4; j++){
                String vars = Integer.toString(num);
                if(boolArray[i][j]){
                    System.out.print("| " +array[i][j]+ "  |" + " ");
                    num++;
                }

                else if(vars.length() >=2){
                    System.out.print("|   " +(num++)+ "  |" + " ");
                }
                else{
                    System.out.print("|   " +(num++)+ "   |" + " ");
                }
            }
            System.out.println("");
            System.out.println("|       |" + " " + "|       |" + " " + "|       |" + " " + "|       |");
            System.out.println("|       |" + " " + "|       |" + " " + "|       |" + " " + "|       |");
            System.out.println("+- - - -+" + " " + "+- - - -+" + " " + "+- - - -+" + " " + "+- - - -+");

        }
        System.out.println("");
    }

    /**
     * isMatch checks to see if the string values are the 2 cards the user choose are the same
     * @param choice1 User's first card choice
     * @param choice2 User's second card choice
     * @param strArray String Array
     * @return true if they match, false if they don't match
     */
    public static boolean isMatch(int choice1, int choice2,String[][] strArray) {
        String card1 = null,card2 = null;
        if (choice1 <= 4) {
            card1 = strArray[0][choice1 - 1]; // Storing the card value into a variable
        }
        else if (choice1 <= 8) {
            card1 = strArray[1][(choice1 - 4) - 1];
        }
        else if (choice1 <= 12) {
            card1 = strArray[2][(choice1 - 8) - 1];
        }
        else if (choice1 <= 16) {
            card1 = strArray[3][(choice1 - 12) - 1];
        }
        // Card 2
        if (choice2 <= 4) {
            card2 = strArray[0][choice2 - 1] ;
        }
        else if (choice2 <= 8) {
            card2 = strArray[1][(choice2 - 4) - 1];
        }
        else if (choice2 <= 12) {
            card2 = strArray[2][(choice2 - 8) - 1];
        }
        else if (choice2 <= 16) {
            card2 = strArray[3][(choice2 - 12) - 1];
        }
        if (card1.equals(card2) && choice1 != choice2) {
            System.out.println("Matched!");
            return true;
        }
        else{
            System.out.println("Not a Match!");
            return false;
        }
    }

    /**
     * checkFlipped checks to see if the card is already face up
     * @param choice user's card choice
     * @param boolArray Boolean Array
     * @return true or false depending on whether it's flipped up or down
     */
    public static boolean checkFlipped(int choice,boolean[][] boolArray) {
        int row = 0;
        int col = 0;
        if (choice <= 4) {
            row = 0;
            col = (choice - 1);
        }
        else if (choice <= 8) {
            row = 1;
            col = (choice - 4) - 1;
        }
        else if (choice <= 12) {
            row = 2;
            col = (choice - 8) - 1;
        }
        else if (choice <= 16) {
            row = 3;
            col = (choice - 12) - 1;
        }
        return boolArray[row][col]; //returns the value of the card (True or False)
    }
}
