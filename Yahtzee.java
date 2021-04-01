import java.util.Scanner;
public class Yahtzee {

	/**
	 * Main method that starts the program based off cl arguments
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// TODO: Implement main method
		if (args.length == 0) {
			prompt();
		} else {
			switch (args[0]) {
				case "-i":
					// independent game
					game();
					break;
				case "-d":
					// dependent game
					break;
			}
		}
	}

	/**
	 * Prompts the user for how they would like to use the program
	 */
	private static void prompt() {
		// TODO
		System.out.println("Would you like to play as a scoresheet?");
	}

	/**
	 * Starts an interactive full yahtzee game
	 */
	private static void game() {
		Scanner scan = new Scanner(System.in);
		// Get number of players etc
		int numPlayers = ask("How many players are there?", scan, 1, 10);
		Player[] players = new Player[numPlayers];
		String name = "";
		for (int i = 0; i < numPlayers; i++) {
			System.out.print("What is player " + (i + 1) + "'s name? ");
			name = scan.nextLine();
			players[i] = new Player(name);
		}
		Game game = new Game();
		game.start(scan, players);
	}

	/**
	 * Asks the user for an integer value that is lower and upper bounded.
	 *
	 * @param msg the message to ask
	 * @param scan the Scanner object
	 * @param min the lower bound (input must be greater than or equal to min)
	 * @param max the upper bound (input must be less than or equal to max)
	 * @return an integer value that satisfies the bounds
	 */
	private static int ask(String msg, Scanner scan, int min, int max) {
		boolean invalid = true;
		int input = 0;
		do {
			System.out.print(msg + " (" + min + "-" + max + "): ");
			if (scan.hasNextLine()) {
				if (scan.hasNextInt()) {
					input = scan.nextInt();
					scan.nextLine();
					if (input >= min && input <= max)
						invalid = false;
				} else
					scan.nextLine();
			}

		} while (invalid);
		return input;
	}

}
