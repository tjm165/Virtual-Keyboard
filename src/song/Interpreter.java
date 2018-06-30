package song;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;

import instrument.AudioFileException;
import instrument.Instrument;
import musictheory.MusicTheory;
import parser.CSVParser;

public class Interpreter {
	public static final char CHORD_MAJOR = 'M';
	public static final char CHORD_MINOR = 'm';
	public static final int CHORD_NOT = '0';

	public static class SongData {
		public final Song song;
		public final Instrument[] instruments;

		private SongData(Song song, Instrument[] instruments) {
			this.song = song;
			this.instruments = instruments;
		}
	}

	public static SongData interpretSong(File file) throws IOException, AudioFileException {
		String[][] source = CSVParser.parse(file);
		return interpretSong(source);
	}

	public static SongData interpretSong(String[][] source) throws AudioFileException {
		String[] meta = source[0];
		int numBeats = getNumBeats(meta);
		int bpm = getBPM(meta);
		Beat[] beats = initBeats(numBeats, bpm);
		double[] counts = getCounts(numBeats, meta);
		Instrument[] instrumentsUsed = new Instrument[source.length - 1];// make a numInstruments method

		Instrument instrument;
		for (int i = 1; i < source.length; i++) {
			System.out.println("Interpreting " + source[i][0]);
			File file = new File(new File("Instruments"), source[i][0]);
			instrument = new Instrument(file);
			instrumentsUsed[i - 1] = instrument;

			try {
				interpretLine(source[i], instrument, beats);
			} catch (Exception e) {
				System.out.println("Trouble at line " + i);
				e.printStackTrace();
			}
		}

		Song song = new Song();
		for (int i = 0; i < beats.length; i++)
			song.add(counts[i], beats[i]);

		SongData sd = new SongData(song, instrumentsUsed);
		return sd;
	}

	//determine the bpm
	static int getBPM(String[] metadata) {
		Double asDouble = Double.parseDouble(metadata[0]);

		return asDouble.intValue();
	}

	//determine the number of Beats
	static int getNumBeats(String[] metadata) {
		return (metadata.length - 1) / 2;
	}

	//initiate the Beats
	private static Beat[] initBeats(int numBeats, int bpm) {

		Beat[] beats = new Beat[numBeats];
		for (int i = 0; i < beats.length; i++)
			beats[i] = new Beat(bpm);

		return beats;
	}

	//determine which counts are contained in the Song
	static double[] getCounts(int numBeats, String[] metadata) {
		double[] counts = new double[numBeats];

		for (int i = 1, c = 0; i < metadata.length && c < numBeats; i += 2, c++)
			counts[c] = Double.parseDouble(metadata[i]);
		// NOTE: number format exception with blank string
		// it should throw an error but it should make more sense

		return counts;
	}

	//interpret a line and using the following instrument and store it in the following Beats
	private static void interpretLine(String[] line, Instrument ins, Beat[] beats) throws AudioFileException {

		int[] notesToAdd;
		double lengthToAdd;

		for (int beat = 0, note = 1, length = 2; beat < beats.length; beat++, length += 2, note += 2) {
			try {
				notesToAdd = getNotes(line[note]);
				lengthToAdd = Double.parseDouble(line[length]);
			} catch (Exception e) {
				notesToAdd = new int[] { -1 };
				lengthToAdd = 0;
				System.out.println("Trouble interpreting note at Beat " + beat + ". Will make it a rest for 0 counts");
			}

			beats[beat].add(lengthToAdd, ins.get(notesToAdd));
		}
	}

	// get the indexes of the notes from its string id like A#8M etc..
	// NOTE: It would be nice to have a void character.
	static int[] getNotes(String note) {
		char[] id = toNoteID(note);

		int rootIndex;
		try {
			rootIndex = MusicTheory.getNoteIndex(id[0], id[1], id[2]);
		} catch (RuntimeException e) {
			if ((id[0] == 'r') || (id[0] == 'R'))// NOTE: Clean up
				rootIndex = -1;
			else
				throw e;
		}

		if (id[3] == MusicTheory.CHORD_MAJOR)
			return MusicTheory.getMajorChord(rootIndex);
		if (id[3] == MusicTheory.CHORD_MINOR)
			return MusicTheory.getMinorChord(rootIndex);

		return new int[] { rootIndex };
	}

	// gets an set of chars that can be used in MusicTheory
	static char[] toNoteID(String note) {

		// note, tone, octave, chord
		char[] stack = new char[4];
		stack[0] = 'r';
		stack[1] = '0';
		stack[2] = '0';
		stack[3] = '0';

		int s = 0;
		int i = 0;

		while ((i < note.length()) && (s < stack.length)) {
			if ((s == 0) && (isNote(note.charAt(i)))) {
				stack[s] = note.charAt(i);
				i++;
			} else if ((s == 1) && (isTone(note.charAt(i)))) {
				stack[s] = note.charAt(i);
				i++;
			} else if ((s == 2) && (isOctave(note.charAt(i)))) {
				stack[s] = note.charAt(i);
				i++;
			} else if ((s == 3) && (isChord(note.charAt(i)))) {
				stack[s] = note.charAt(i);
				i++;
			}
			s++;
		}

		return stack;

	}

	private static boolean isNote(char c) {
		switch (c) {
			case 'A':
			case 'a':
			case 'B':
			case 'b':
			case 'C':
			case 'c':
			case 'D':
			case 'd':
			case 'E':
			case 'e':
			case 'F':
			case 'f':
			case 'G':
			case 'g':
				return true;
			default:
				return false;
		}
	}

	private static boolean isChord(char c) {
		return (c == 'm' || c == 'M');
	}

	private static boolean isOctave(char c) {
		int zero = '0';
		int nine = '9';

		if ((zero <= c) && (c <= nine))
			return true;
		return false;
	}

	private static boolean isTone(char c) {
		return (c == MusicTheory.TONE_FLAT || c == MusicTheory.TONE_SHARP);
	}

}
