import java.lang.StringBuilder;
public class Player {
	private String name;

	private int upperTotal;
	private int lowerTotal;
	private int yahtzees;

	private boolean[] plays;
	private int[] playsScores;

	// Constants depicting the different yahtzee categories to score
	public enum Plays {
		ONE(0), TWO(1), THREE(2), FOUR(3), FIVE(4), SIX(5), KIND3(6), KIND4(7), FHOUSE(8), LSTRAIGHT(9), HSTRAIGHT(10), CHANCE(11), YAHTZEE(12);

		private final int INDEX;
		private Plays(int index) {
			INDEX = index;
		}

		public int getIndex() {
			return INDEX;
		}

       	} 

	/**
	 * Creates a player with a given name and a blank score sheet
	 *
	 * @param name the player's name
	 */
	public Player(String name) {
		this.name = name;
		final int LENGTH = Plays.values().length;
		plays = new boolean[LENGTH];
		playsScores = new int[LENGTH];
		upperTotal = 0;
		lowerTotal = 0;
		yahtzees = 0;
	}

	/**
	 * Returns if the player has filled in the
	 * particular play of the scoresheet.
	 *
	 * @param play the Player.Plays constant play
	 * @return true if the spot is unfilled, false otherwise.
	 */
	public boolean canPlay(Plays play) {
		return !plays[play.getIndex()];
	}

	/**
	 * Fills a scoresheet play with a certain number of points
	 * and adds it to a particular total.
	 *
	 * @param play the Player.Plays constant play
	 * @param points the number of points gained
	 * @param isUpper if the points should be added to the upper or lower section
	 */
	public void play(Plays play, int points, boolean isUpper) {
		int index = play.getIndex();
		play(index, points, isUpper);
	}

	/**
	 * Fills a scoresheet play with a certain number of points
	 * and adds it to a particular total.
	 *
	 * @param index the index associated with the play
	 * @param points the number of points gained
	 * @param isUpper if the points should be added to the upper or lower section
	 */
	public void play(int index, int points, boolean isUpper) {
		plays[index] = true;
		playsScores[index] = points;
		if (isUpper)
			upperTotal += points;
		else
			lowerTotal += points;
	}

	/**
	 * Adds a bonus yahtzee score
	 */
	public void bonusYahtzee() {
		yahtzees++;
	}

	/**
	 * Returns if the player has already scored a valid yahtzee, which is
	 * considered valid if and only if the yahtzee category is scored and not
	 * zero
	 *
	 * @return true if the yahtzee category is non zero, false otherwise
	 */
	public boolean hadYahtzee() {
		return playsScores[Plays.YAHTZEE.getIndex()] != 0;
	}

	/**
	 * Returns if a category is scored
	 *
	 * @param play the Player.Plays constant play
	 * @return true if the play is scored, false otherwise
	 */
	public boolean isScored(Plays play) {
		return plays[play.getIndex()];
	}

	/**
	 * Returns if a category is scored
	 *
	 * @param index the index to check
	 * @return true if the play is scored, false otherwise.
	 */
	public boolean isScored(int index) {
		return plays[index];
	}

	/**
	 * Returns the player's name
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the player's total score
	 *
	 * @return the total score for the player
	 */
	public int score() {
		return (upperTotal >= 63 ? upperTotal + 35 : upperTotal) + lowerTotal + (100 * yahtzees);
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

	@Override
	/**
	 * Prints out the player's score sheet
	 *
	 * @return a string containing this player's score sheet.
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Name: " + name + "\n");
		builder.append("Upper Score: " + upperTotal + "\n");
		builder.append("Upper Bonus: " + (upperTotal >= 63 ? "35" : "0") + "\n");
		builder.append("Lower Score: " + lowerTotal + "\n");
		builder.append("Bns Yahtzee: " + yahtzees + " (" + (100 * yahtzees) + ")\n");
		builder.append("Total Score: " + score() + "\n\n");
		Plays[] values = Plays.values();
		final int PAD = 11;
		for (int i = 0; i < values.length; i++) {
			builder.append(pad(values[i] + ": ", PAD));
			builder.append(canPlay(values[i]) ? "Open" : playsScores[i]);
			builder.append("\n");
		}
		return builder.toString();
	}

}
