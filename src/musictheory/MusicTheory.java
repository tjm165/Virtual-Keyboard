package musictheory;

/**
 * The concepts behind music. Basically this is the static methods this program
 * is dealing with
 * @author Tommy Moawad
 *
 */
public class MusicTheory {
	public static final int CHORD_MAJOR = 'M';
	public static final int CHORD_MINOR = 'm';

	public static final int NOTE_REST = -1;

	public static final char TONE_SHARP = '#';
	public static final char TONE_FLAT = 'b';
	public static final char TONE_NATURAL = 'n';

	/**
	 * Converts from the count unit of measurement to the milliseconds unit of measurement
	 * @param bpm
	 * @param counts
	 * @return
	 */
	public static long countsToMillis(int bpm, double counts) {
		if ((bpm <= 0) || (counts <= 0))
			return 0;

		double countToMin = counts / bpm;

		return (long) (60000 * countToMin);
	}

	/**
	 * Determines if the note index represents a rest. 
	 * @param note the index of the note being represented
	 * @return true if the note index is less than 0
	 */
	public static boolean isARest(int note) {
		return note < 0;
	}

	/**
	 * Determines if the note index represents a natural note
	 * @param note the index of the note being represented
	 * @return the corresponding boolean
	 */
	public static boolean isNatural(int note) {
		if (isARest(note))
			return false;

		while (!((0 <= note) && (note <= 12)))
			note -= 12;

		switch (note) {
			case 1:
			case 4:
			case 6:
			case 9:
			case 11:
				return false;
			default:
				return true;
		}
	}

	/**
	 * Obtain the major chord starting with the indicated root Note
	 * @param rootIndex the root of the chord
	 * @return the major chord's indexes
	 */
	public static int[] getMajorChord(int rootIndex) {
		int[] indexes;

		if (isARest(rootIndex)) {
			indexes = new int[]{rootIndex};
		} else {
			indexes = new int[3];
			indexes[0] = rootIndex;
			indexes[1] = rootIndex + 4;
			indexes[2] = rootIndex + 7;
		}

		return indexes;
	}

	/**
	 * Obtain the minor chord starting with the indicated root Note
	 * @param rootNote the root of the chord
	 * @return the minor chord's indexes
	 */
	public static int[] getMinorChord(int rootIndex) {
		int[] indexes = new int[3];

		if (isARest(rootIndex)) {
			return getMajorChord(rootIndex);
		} else {
			indexes[0] = rootIndex;
			indexes[1] = rootIndex + 3;
			indexes[2] = rootIndex + 7;
		}

		return indexes;
	}

	/**
	 * Determines the index that represents the described note
	 * @param c the character value 'A' through 'G' of the note
	 * @param tone the tone of the note
	 * @param octave the octave '0' through '9' of the note
	 * @return the index that represents the described note
	 */
	public static int getNoteIndex(char c, char tone, char octave) {
		int oct = octave - '0';
		if (oct < 0)
			throw new RuntimeException("Octave " + octave + " cannot be less than 0");

		return getNoteIndex(c, tone) + 12 * oct;
	}

	private static int getNoteIndex(char c, char tone) {
		int toneInt = 0;
		
		if (tone == TONE_FLAT)
			toneInt = -1;
		if (tone == TONE_SHARP)
			toneInt = 1;
		
		int value = getNoteIndex(c) + toneInt;
		if (isARest(value))
			throw new RuntimeException("Note " + c + " and tone " + tone + " resulted to a index less than 0");

		else
			return value;
	}

	/**
	 * Determines the index that represents the described note
	 * @param c the character value 'A' through 'G' of the note
	 * @return the index that represents the described note
	 */
	public static int getNoteIndex(char c) {
		switch (c) {
			case 'a':
			case 'A':
				return 0;
			case 'b':
			case 'B':
				return 2;
			case 'c':
			case 'C':
				return 3;
			case 'd':
			case 'D':
				return 5;
			case 'e':
			case 'E':
				return 7;
			case 'f':
			case 'F':
				return 8;
			case 'g':
			case 'G':
				return 10;
			default:
				return NOTE_REST;
		}
	}
}
