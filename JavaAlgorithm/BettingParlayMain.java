import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class BettingParlayMain {
    public static final DecimalFormat money = new DecimalFormat("0.00");
    public static final DecimalFormat df = new DecimalFormat("0.000");

    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);

        String[] userInput = intro(console);
        String elimMethod = userInput[0];
        int numGames = Integer.parseInt(userInput[1]);
        int numPlayers = Integer.parseInt(userInput[2]);

        ArrayList[] allGames = new ArrayList[numGames];
        File[] inputFiles = {new File("input1.txt"), new File("input2.txt"),
                new File("input3.txt"), new File("input4.txt")};
        PrintStream output = new PrintStream("bettingOutput.txt");

        run(allGames, numPlayers, elimMethod, console, inputFiles);
        permutations(allGames, output);

        printJson(allGames);
    }

    public static String[] intro(Scanner console) {
        System.out.println("\nWelcome to the first basket permutations program. Follow the instructions below.");
        System.out.print("Elimination method (highest odds removed - H, lowest odds removed - L, manual - M): ");
        String elimMethod = console.next();
        System.out.print("Number of games (must be 2, 3, or 4): ");
        int numGames = console.nextInt();
        System.out.print("Number of players per game: ");
        int numPlayers = console.nextInt();
        String[] userInput = {elimMethod, Integer.toString(numGames), Integer.toString(numPlayers)};
        return userInput;
    }

    public static void run(ArrayList<Player>[] allGames, int numPlayers, String elimMethod, Scanner console, File[] inputFiles) throws FileNotFoundException {
        int numBets = numPlayers;
        boolean autoElim = false;
        if(elimMethod.equalsIgnoreCase("H") || elimMethod.equalsIgnoreCase("L")) {
            autoElim = true;
            System.out.print("Number of players willing to bet on per game (must be at most the number of players): ");
            numBets = console.nextInt();
        }
        System.out.println();

        //populates and prints array with full ArrayLists
        //determines players with the best odds for array
        for(int i = 0; i < allGames.length; i++) {
            populate(allGames, i, numPlayers, inputFiles);
            System.out.println("\nAll players for game " + (i + 1) + ":");
            printPlayers(allGames[i]);
            if(autoElim) {
                System.out.println("\nPlayers with selected odds for game " + (i + 1) + ":");
                if(elimMethod.equalsIgnoreCase("H")) {
                    allGames[i] = removeHighest(allGames[i], numBets);
                } else {
                    allGames[i] = removeLowest(allGames[i], numBets);
                }
                printPlayers(allGames[i]);
            }
            System.out.println();
        }
    }

    //populates each individual ArrayList within the array by scanning files
    public static void populate(ArrayList<Player>[] allGames, int numGame, int numPlayers, File[] inputFiles) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(inputFiles[numGame]);
        System.out.println("Game " + (numGame + 1) + ":");
        ArrayList<Player> currentGame = new ArrayList<>();
        for(int i = 0; i < numPlayers; i++) {
            System.out.print("Player " + (i + 1) + " name: ");
            String name = fileScanner.nextLine();
            System.out.println(name);
            System.out.print("Player " + (i + 1) + " odds: +");
            int odds = Integer.parseInt(fileScanner.nextLine().substring(1));
            System.out.println(odds);
            currentGame.add(new Player(name, odds));
        }
        allGames[numGame] = currentGame;
    }

    //prints each individual ArrayList within the array
    public static void printPlayers(ArrayList<Player> game) {
        for (Player playersV5 : game) {
            System.out.println(playersV5);
        }
    }

    //removes the players with the highest odds for each individual ArrayList within the array
    public static ArrayList<Player> removeHighest(ArrayList<Player> game, int numBets) {
        ArrayList<Player> oldPlayers = new ArrayList<>(game);
        ArrayList<Player> newPlayers = new ArrayList<>();
        while(newPlayers.size() < numBets) {
            Player compare = oldPlayers.get(0);
            for(int i = 1; i < oldPlayers.size(); i++) {
                if(compare.compareTo(oldPlayers.get(i)) < 0) {
                    compare = oldPlayers.get(i);
                }
            }
            newPlayers.add(compare);
            oldPlayers.remove(compare);
        }
        return newPlayers;
    }

    //removes the players with the lowest odds for each individual ArrayList within the array
    public static ArrayList<Player> removeLowest(ArrayList<Player> game, int numBets) {
        ArrayList<Player> oldPlayers = new ArrayList<>(game);
        ArrayList<Player> newPlayers = new ArrayList<>();
        while(newPlayers.size() < numBets) {
            Player compare = oldPlayers.get(0);
            for(int i = 1; i < oldPlayers.size(); i++) {
                if(compare.compareTo(oldPlayers.get(i)) > 0) {
                    compare = oldPlayers.get(i);
                }
            }
            newPlayers.add(compare);
            oldPlayers.remove(compare);
        }
        return newPlayers;
    }

    public static void permutations(ArrayList<Player>[] allGames, PrintStream output) {
        String s = "";
        double calculatedOdds = 0.0;
        double totalOdds = 0.0;
        if(allGames.length == 2) {
            int counter1 = 0;
            int counter2 = 0;
            while(counter1 < allGames[0].size()) {
                counter2 = 0;
                while(counter2 < allGames[1].size()) {
                    calculatedOdds = 100.0 * (100.0 / (((double) allGames[0].get(counter1).getOdds()) + 100.0)) *
                            (100.0 / (((double) allGames[1].get(counter2).getOdds()) + 100.0));
                    totalOdds += calculatedOdds;
                    s += allGames[0].get(counter1).getName() + ", " + allGames[1].get(counter2).getName() + " " + df.format(calculatedOdds) +"%\n";
                    counter2++;
                }
                counter1++;
            }
        }
        else if(allGames.length == 3) {
            int counter1 = 0;
            int counter2 = 0;
            int counter3 = 0;
            while(counter1 < allGames[0].size()) {
                counter2 = 0;
                while(counter2 < allGames[1].size()) {
                    counter3 = 0;
                    while(counter3 < allGames[2].size()) {
                        calculatedOdds = 100.0 * (100.0 / (((double) allGames[0].get(counter1).getOdds()) + 100.0)) *
                                (100.0 / (((double) allGames[1].get(counter2).getOdds()) + 100.0)) *
                                (100.0 / (((double) allGames[2].get(counter3).getOdds()) + 100.0));
                        totalOdds += calculatedOdds;
                        s += allGames[0].get(counter1).getName() + ", " + allGames[1].get(counter2).getName() + ", "
                                + allGames[2].get(counter3).getName() +  " " + df.format(calculatedOdds) +"%\n";
                        counter3++;
                    }
                    counter2++;
                }
                counter1++;
            }
        }
        else if(allGames.length == 4) {
            int counter1 = 0;
            int counter2 = 0;
            int counter3 = 0;
            int counter4 = 0;
            while(counter1 < allGames[0].size()) {
                counter2 = 0;
                while(counter2 < allGames[1].size()) {
                    counter3 = 0;
                    while(counter3 < allGames[2].size()) {
                        counter4 = 0;
                        while(counter4 < allGames[3].size()) {
                            calculatedOdds = 100.0 * (100.0 / (((double) allGames[0].get(counter1).getOdds()) + 100.0)) *
                                    (100.0 / (((double) allGames[1].get(counter2).getOdds()) + 100.0)) *
                                    (100.0 / (((double) allGames[2].get(counter3).getOdds()) + 100.0)) *
                                    (100.0 / (((double) allGames[3].get(counter4).getOdds()) + 100.0));
                            totalOdds += calculatedOdds;
                            s += allGames[0].get(counter1).getName() + ", " + allGames[1].get(counter2).getName() + ", "
                                    + allGames[2].get(counter3).getName() + ", " + allGames[3].get(counter4).getName() + " " + df.format(calculatedOdds) +"%\n";
                            counter4++;
                        }
                        counter3++;
                    }
                    counter2++;
                }
                counter1++;
            }
        }
        output.println(s);
        if(totalOdds > 100) {
            totalOdds = 100;
        }
        output.println("Implied probability of hitting at least one parlay: " + df.format(totalOdds) + "%");
        output.println("Implied probability of not hitting any parlays: " + df.format(100.0 - totalOdds) + "%");
    }

    public static void printJson(ArrayList<Player>[] allGames) throws FileNotFoundException {
        Scanner outputScanner = new Scanner(new File("bettingOutput.txt"));
        PrintStream jsonOutput = new PrintStream("jsonOutput.json");

        int numGames = allGames.length;
        int numBets = allGames[0].size();
        int numParlays = (int) Math.pow(numBets, numGames);

        int counter = 0;

        jsonOutput.println("{\n\t\"allGames\": [");
        while (outputScanner.hasNextLine()) {
            counter++;
            String dataline = outputScanner.nextLine();
            if (dataline.contains("0") && !dataline.contains("Implied")) {
                dataline = dataline.substring(0, dataline.indexOf(" 0"));
                String[] parlayNames = dataline.split(", ");
                jsonOutput.println("\t\t{");
                for (int j = 0; j < parlayNames.length; j++) {
                    if (j == parlayNames.length-1) {
                        jsonOutput.println("\t\t\t\"name" + (j+1) + "\": \"" + parlayNames[j] + "\"");
                    } else {
                        jsonOutput.println("\t\t\t\"name" + (j+1) + "\": \"" + parlayNames[j] + "\",");
                    }
                }
                if (counter < numParlays) {
                    jsonOutput.println("\t\t},");
                } else {
                    jsonOutput.println("\t\t}");
                }
            }
        }
        jsonOutput.println("\t]\n}");
    }
}