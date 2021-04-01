import java.util.Scanner;
import java.util.Random;
import java.lang.StringBuilder;
public class Game {

	/**
	 * Starts the game loop for yahtzee, looping for the totat number of categories
	 * for each player.
	 *
	 * @param scan the Scanner object
	 * @param players the players playing the game
	 */
	public void start(Scanner scan, Player[] players) {
		Random rand = new Random();
		final int ROUNDS = Player.Plays.values().length;
		// Game Loop
		Player current = null;
		for (int i = 0; i < (ROUNDS*players.length); i++) {
			current = players[i % players.length];
			System.out.println(printLeaderboard(players, current) + "\nIt is " + current.getName() + "'s turn\n" + current);
			playerRound(rand, scan, current);
		}
		// Display game end
		StringBuilder builder = new StringBuilder();
		int max = -1;
		int score = 0;
		Player[] winners = new Player[players.length];
		int wIndex = 0;
		for (int i = 0; i < players.length; i++) {
			current = players[i];
			score = current.score();
			builder.append(current.getName() + " scored: " + score + "\n");
			if (score > max) {
				max = score;
				for (int k = 0; k < winners.length; k++)
					winners[k] = null;
				wIndex = 0;
				winners[wIndex] = current;
				wIndex++;
			} else if (score == max) {
				winners[wIndex] = current;
				wIndex++;
			}
		}
		builder.append("\n");
		if (wIndex == 1) {
			builder.append("The winner is " + winners[0].getName() + "! Winning with a score of " + max + " points!\n");
		} else {
			builder.append("There was a " + wIndex + " way draw!");
			for (int i = 0; i < wIndex; i++) {
				builder.append(i < wIndex - 1 ? winners[i].getName() + ", " : "and " + winners[i].getName());
			}
			builder.append(" all tied with a score of " + max + " points!\n");
		}
		builder.append("Congratulations!");
		System.out.println("\n" + builder.toString());
	}

	/**
	 * Prints the leaderboard for the yahtzee game.
	 *
	 * @param players the players
	 * @param current the player who is playing
	 * @return A string of the leaderboard for the yahtzee game with players in
	 * descending order attached to their score. The current player is starred.
	 */
	private String printLeaderboard(Player[] players, Player current) {
		int pad = 0;
		for (int i = 0; i < players.length; i++) {
			int length = players[i].getName().length();
			if (length > pad)
				pad = length;
		}

		// Adjust for constant Strings
		pad += 5;
		System.out.println(pad);
		Player[] sorted = sort(players);
		StringBuilder builder = new StringBuilder();
		builder.append("\n" + "Leaderboard:" + "\n");
		for (int i = 0; i < sorted.length; i++) {
			builder.append(pad((i + 1) + ". " + sorted[i].getName() + ": ", pad) + sorted[i].score() + (sorted[i].equals(current) ? " *" : "") + "\n");
		}
		return builder.toString();
	}
	
	/**
	 * Pads the end of a string with spaces if the string is not long enough.
	 *
	 * @param str the string to pad
	 * @param pad the length of the final string
	 * @return the orginal string if the string's length is the same or greater than
	 * pad, otherwise returns a string with str + spaces of length pad.
	 */
	private String pad(String str, int pad) {
		int length = str.length();
		if (length >= pad)
			return str;
		StringBuilder builder = new StringBuilder();
		builder.append(str);
		for (int i = 0; i < pad - length; i++)
			builder.append(" ");
		return builder.toString();
	}

	/**
	 * Sorts a deep copy of an array in descending order. Useful for leaderboard.
	 *
	 * @param players The Player[] to sort
	 * @return a sorted array in order of decreasing score
	 */
	private Player[] sort(Player[] players) {
		int length = players.length;
		Player[] sorted = new Player[length];
		for (int i = 0; i < length; i++)
			sorted[i] = players[i];
		int c = 0;
		Player cp = null;
		int n = 0;
		Player np = null;
		for (int i = 0; i < length - 1; i++) {
			c = sorted[i].score();
			cp = sorted[i];
			n = sorted[i+1].score();
			np = sorted[i+1];
			if (c < n) {
				sorted[i+1] = cp;
				sorted[i] = np;
				int p = 0;
				Player pp = null;
				for (int k = i; i > 1; k--) {
					c = sorted[k].score();
					cp = sorted[k];
					p = sorted[k - 1].score();
					pp = sorted[k - 1];
					if (c > p) {
						sorted[k] = pp;
						sorted[k - 1] = cp;
					}
				}
			}
		}
		return sorted;
	}

	/**
	 * Prints the dice and associated roll.
	 *
	 * @param dice The int[] of dice to print
	 * @return the dice and dice rolls
	 */
	private String printDice(int[] dice) {
		StringBuilder builder = new StringBuilder();
		builder.append("Dice: Roll\n");
		for (int i = 0; i < dice.length; i++) {
			builder.append(" (" + (i + 1) + ")   " + dice[i] + "\n");
		}
		return builder.toString();
	}

	/**
	 * Plays a round of yahtzee.
	 *
	 * @param rand the Random object
	 * @param scan the Scanner object
	 * @param player the player who is playing
	 */
	private void playerRound(Random rand, Scanner scan, Player player) {
		StringBuilder builder = new StringBuilder();
		int[] dice = new int[5];
		boolean[] isRolling = new boolean[5];
		builder.append("Rolling dice...\n");
		roll(dice, rand);
		int rerolls = 2;
		boolean rolling = true;
		String input = "";
		char c = ' ';
		do {
			builder.append(printDice(dice) + "\n");
			builder.append("There are " + rerolls + " rerolls remaining\n");
			builder.append("Reroll (r), keep (k), select all (a), quit (xx), or toggle dice (1-5): ");
			System.out.print(builder.toString());
			builder.setLength(0);
			input = scan.nextLine();
			int length = input.length();
			for (int i = 0; i < length; i++) {
				c = input.charAt(i);
				switch (c) {
					case 'r':
						boolean isEmpty = true;
						for (int k = 0; k < isRolling.length; k++) {
							if (isRolling[k]) {
								isEmpty = false;
								break;
							}
						}
						if (isEmpty) {
							builder.append("No selected dice to roll\n");
							break;
						}
						if (rerolls > 0) {
							rerolls--;
							roll(dice, isRolling, rand);
						} else
							builder.append("Cannot reroll, out of rerolls.\n");
						break;
					case 'k':
						rolling = false;
						break;
					case 'a':
						for (int k = 0; k < isRolling.length; k++)
							isRolling[k] = true;
						break;
					case '1':
						isRolling[0] = true;
						break;
					case '2':
						isRolling[1] = true;
						break;
					case '3':
						isRolling[2] = true;
						break;
					case '4':
						isRolling[3] = true;
						break;
					case '5':
						isRolling[4] = true;
						break;
					case 'x':
						if (i + 1 < length) {
							if (input.charAt(i+1) == 'x')
								System.exit(0);
						} else
							builder.append("Exit canceled. Need \"xx\" to exit.\n");
						break;
					default:
						builder.append("input: \"" + c + "\" is not recognized.\n");
						break;
				}
			}
			// reset after each turn
			for (int k = 0; k < isRolling.length; k++)
				isRolling[k] = false;
		} while (rolling && rerolls > 0);

		// clear builder
		builder.setLength(0);
		boolean joker = false;
		// Check if a yahtzee has been made - bonus points if scored again
		if (!player.canPlay(Player.Plays.YAHTZEE) && player.hadYahtzee())
			if (isKind(dice, 5)) {
				player.bonusYahtzee();
				// index is 0-5, equal to side - 1
				int index = dice[0] - 1;

				joker = player.isScored(index);
				builder.append("Yahtzee Joker: " + joker + (joker ? " Can be used to score full points on a Full House or either Straight!" : "") + "\n");
				if (!joker) {
					builder.append("Automatically playing as Upper Play.\n");
					player.play(index, sumRoll(dice, dice[0]), true);
					System.out.println(builder.toString());
					return;
				}
			}

		// Choosing score to assign
		boolean choosing = true;
		builder.append(printChoices(player, dice));
		while (choosing) {
			System.out.println(builder.toString());
			input = scan.nextLine();
			choosing = !checkSelection(input.charAt(0), player, dice, scan, joker);
		}
	}

	/**
	 * Checks that the selction made by a player is valid, and if it is, determines the point value
	 *
	 * @param d decision character entered by the player
	 * @param player the player who is playing
	 * @param dice the dice rolled
	 * @param scan the Scanner object
	 * @param joker whether this selection follows joker rules
	 * @return true if the selection is valid and play can be made, false otherwise.
	 */
	private boolean checkSelection(char d, Player player, int[] dice, Scanner scan, boolean joker) {
		final int LSTRAIGHT = 30;
		final int HSTRAIGHT = 40;
		final int FHOUSE = 25;
		final int YAHTZEE = 50;
		final boolean LOWER = false;
		final boolean UPPER = true;
		
		int pts = 0;
		switch (d) {
			case '1':
				if (checkPlay(player, Player.Plays.ONE, dice, scan)) {
					pts = sumRoll(dice, 1);
					if ((pts == 0 && confirm(scan)) || pts > 0) {
						player.play(Player.Plays.ONE, pts, UPPER);
						return true;
					}
				}
					break;
			case '2':
				if (checkPlay(player, Player.Plays.TWO, dice, scan)) {
					pts = sumRoll(dice, 2);
					if ((pts == 0 && confirm(scan)) || pts > 0) {
						player.play(Player.Plays.TWO, pts, UPPER);
						return true;
					}
				}
				break;
			case '3':
				if (checkPlay(player, Player.Plays.THREE, dice, scan)) {
					pts = sumRoll(dice, 3);
					if ((pts == 0 && confirm(scan)) || pts > 0) {
						player.play(Player.Plays.THREE, pts, UPPER);
						return true;
					}
				}
				break;
			case '4':
				if (checkPlay(player, Player.Plays.FOUR, dice, scan)) {
					pts = sumRoll(dice, 4);
					if ((pts == 0 && confirm(scan)) || pts > 0) {
						player.play(Player.Plays.FOUR, pts, UPPER);
						return true;
					}
				}
				break;
			case '5':
				if (checkPlay(player, Player.Plays.FIVE, dice, scan)) {
					pts = sumRoll(dice, 5);
					if ((pts == 0 && confirm(scan)) || pts > 0) {
						player.play(Player.Plays.FIVE, pts, UPPER);
						return true;
					}
				}
				break;
			case '6':
				if (checkPlay(player, Player.Plays.SIX, dice, scan)) {
					pts = sumRoll(dice, 6);
					if ((pts == 0 && confirm(scan)) || pts > 0) {
						player.play(Player.Plays.SIX, pts, UPPER);
						return true;
					}
				}
				break;
			case 'k':
				if (checkPlay(player, Player.Plays.KIND3, dice, scan)) {
					pts = isKind(dice, 3) ? sum(dice) : 0;
					player.play(Player.Plays.KIND3, pts, LOWER);
					return true;
				}
				break;
			case 'K':
				if (checkPlay(player, Player.Plays.KIND4, dice, scan)) {
					pts = isKind(dice, 4) ? sum(dice) : 0;
					player.play(Player.Plays.KIND4, pts, LOWER);
					return true;
				}
				break;
			case 's':
				if (checkPlay(player, Player.Plays.LSTRAIGHT, dice, scan, joker)) {
					pts = isStraight(dice, 4) || joker ? LSTRAIGHT : 0;
					player.play(Player.Plays.LSTRAIGHT, pts, LOWER);
					return true;
				}
				break;
			case 'S':
				if (checkPlay(player, Player.Plays.HSTRAIGHT, dice, scan, joker)) {
					pts = isStraight(dice, 5) || joker ? HSTRAIGHT : 0;
					player.play(Player.Plays.HSTRAIGHT, pts, LOWER);
					return true;
				}
				break;
			case 'h':
				if (checkPlay(player, Player.Plays.FHOUSE, dice, scan, joker)) {
					pts = isHouse(dice) || joker ? FHOUSE : 0;
					player.play(Player.Plays.FHOUSE, pts, LOWER);
					return true;
				}
				break;
			case 'c':
				if (checkPlay(player, Player.Plays.CHANCE, dice, scan)) {
					pts = sum(dice);
					player.play(Player.Plays.CHANCE, pts, LOWER);
					return true;
				}
				break;
			case 'y':
				if (checkPlay(player, Player.Plays.YAHTZEE, dice, scan)) {
					pts = isKind(dice, 5) ? YAHTZEE : 0;
					player.play(Player.Plays.YAHTZEE, pts, LOWER);
					return true;
				}
				break;
			default:
				System.out.println("Unrecognized input");
				return false;
			}
		return false;
	}
	
	/**
	 * Checks player selection assuming joker is false.
	 *
	 * @param d decision character entered by the player
	 * @param player the player who is playing
	 * @param dice the dice rolled
	 * @param scan the Scanner object
	 * @return true if the selection is valid and play can be made, false otherwise.
	 */
	private boolean checkPlay(Player player, Player.Plays play, int[] dice, Scanner scan) {
		return checkPlay(player, play, dice, scan, false);
	}

	/**
	 * Part of the checkSelection process - checks the particular play matches the play criteria.
	 *
	 * @param player the player who is playing
	 * @param play the Player.Plays constant describing the category to fill
	 * @param dice the dice rolled
	 * @param scan the Scanner object
	 * @param joker if this play can be used as a joker
	 * @return true if the play has been made, false otherwise
	 */
	private boolean checkPlay(Player player, Player.Plays play, int[] dice, Scanner scan, boolean joker) {
		if (player.canPlay(play)) {
			switch (play) {
				case KIND3:
					if (isKind(dice, 3))
						return true;
					else
						return confirm(scan);
				case KIND4:
					if (isKind(dice, 4))
						return true;
					else
						return confirm(scan);
				case FHOUSE:
					if (isHouse(dice) || joker)
						return true;
					else
						return confirm(scan);
				case LSTRAIGHT:
					if (isStraight(dice, 4) || joker)
						return true;
					else
						return confirm(scan);
				case HSTRAIGHT:
					if (isStraight(dice, 5) || joker)
						return true;
					else
						return confirm(scan);
				case YAHTZEE:
					if (isKind(dice, 5))
						return true;
					else
						return confirm(scan);
				default:
					// Includes numbers and chance - already checked it was available
					return true;
			}
		}
		System.out.println("That score has already been used or the roll is not correct.");
		return false;
	}

	/**
	 * Prompts the user to confirm a zero addition.
	 *
	 * @param scan the Scanner object
	 * @param play the Player.Plays play constant (category to fill)
	 * @return true if the user inputs 'y' as the first letter, false otherwise.
	 */
	private boolean confirm(Scanner scan) {
		System.out.print("Are you sure you want to fill in a lowerScore with zero? (y/n): ");
		char ans = scan.nextLine().charAt(0);
		if (ans == 'y')
			return true;
		return false;
	}

	/**
	 * Prints the player's score sheet with the final roll.
	 *
	 * @param player the player who is playing
	 * @param dice the dice rolled
	 * @return a string containing the scoresheet, final rolls, and a prompt
	 */
	private String printChoices(Player player, int[] dice) {
		return (player + "\nFinal " + printDice(dice) + "\nAces (1), Twos (2), Threes (3), " +
				"Fours (4), Fives (5), Sixes (6),\nSmall Straight (s), Large Straight (S), " + 
				"Full House (h), Three of a Kind (k), or Four of a kind (K), Chance (c), or Yahtzee (y): ");
	}

	/**
	 * Rolls the dice selected to roll.
	 *
	 * @param dice the dice
	 * @param isRolling the dice to roll
	 * @param rand the Random object
	 */
	private void roll(int[] dice, boolean[] isRolling, Random rand) {
		for (int i = 0; i < dice.length; i++) {
			if (isRolling[i])
				dice[i] = rand.nextInt(6) + 1;
		}
	}

	/**
	 * Rolls all dice.
	 *
	 * @param dice the dice to roll
	 * @param rand the Random object
	 */
	private void roll(int[] dice, Random rand) {
		for (int i = 0; i < dice.length; i++)
			dice[i] = rand.nextInt(6) + 1;
	}

	/**
	 * Counts the total of dice that are a particular roll
	 * 
	 * @param dice The dice rolled
	 * @param roll The roll to count
	 * @return The sum of the dice of the specified roll
	 */
	private int sumRoll(int[] dice, int roll) {
		int total = 0; 
		for (int i = 0; i < dice.length; i++) {
			if (dice[i] == roll)
				total += roll;
		}
		return total;
	}

	/**
	 * Returns the total of the dice rolled.
	 * @param dice The dice rolled
	 * @return The sum of the dice
	 */
	private int sum(int[] dice) {
		int total = 0;
		for (int i = 0; i < dice.length; i++) {
			total += dice[i];
		}
		return total;
	}

	/**
	 * Returns if the rolls is a valid # of a kind. A yahtzee is a
	 * five of a kind.
	 * @param dice the dice rolled
	 * @param number the number of rolls needed
	 * @return true if the number of rolls is met, false otherwise
	 */
	private boolean isKind(int[] dice, int number) {
		int[] count = count(dice);
		for (int i = 0; i < count.length; i++) {
			if (count[i] >= number)
				return true;
		}
		return false;
	}

	/**
	 * Counts the number of appearances of each dice
	 * @param dice the dice to count
	 * @return counts of each side
	 */
	private int[] count(int[] dice) {
		int[] count = new int[6];
		for (int i = 0; i < dice.length; i++) {
			// dice side -1 = index
			count[dice[i] - 1]++; 
		}
		return count;
	}

	/**
	 * Returns if the rolls resulted in a full house
	 * @param dice The dice rolled
	 * @return true if it is a full house, false otherwise
	 */
	private boolean isHouse(int[] dice) {
		int[] count = count(dice);
		boolean two = false;
		boolean three = false;
		for (int i = 0; i < count.length; i++) {
			if (count[i] == 2)
				two = true;
			else if (count[i] == 3)
				three = true;
		}
		if (two && three)
			return true;
		return false;
	}

	/**
	 * Returns if the dice form a straight of a given length.
	 *
	 * @param dice the dice rolled
	 * @param length the length of the straight
	 * @return true if the rolls form a straight of given length or more, false otherwise
	 */
	private boolean isStraight(int[] dice, int length) {
		int[] count = count(dice);
		int maxRow = 0;
		int row = 0;
		for (int i = 0; i < count.length; i++) {
			if (count[i] >= 1)
				row++;
			if (row > maxRow)
				maxRow = row;
			if (count[i] < 1)
				row = 0;
		}
		if (maxRow >= length)
			return true;
		return false;
	}
}
