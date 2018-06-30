package instrument.pane;

import java.util.Arrays;
import java.util.Hashtable;
import javafx.scene.input.KeyCode;

import instrument.Instrument;
import javafx.scene.input.KeyEvent;
import musictheory.MusicTheory;
import javafx.event.EventHandler;

/**
 * Ties a computer's keyboard to the Instrument class
 * @author Tommy M
 *
 */
class Keyboard implements Controller {
	private final Instrument instrument;
	private static final boolean MAJOR_SHIFT = false;

	// Keeps track of what type of chord is being played.
	private boolean[] fingersOnChordKey;

	// Represents the specific notes being played while a chord is being played
	private int[] fingersOnNote;
	Hashtable<KeyCode, int[]> chordByCode;

	Keyboard(Instrument instrument) {
		this.instrument = instrument;
		this.fingersOnNote = new int[instrument.size()];
		Arrays.fill(fingersOnNote, 0);

		this.fingersOnChordKey = new boolean[7];
		Arrays.fill(fingersOnChordKey, false);

		this.chordByCode = new Hashtable<KeyCode, int[]>();
	}

	// get the keyPressed EventHandler
	EventHandler<? super KeyEvent> keyPressed() {
		return ((event) -> {
			pushChord(event.getCode(), event.isShiftDown());
		});
	}

	// get the keyReleased EventHandler
	EventHandler<? super KeyEvent> keyReleased() {
		return ((event) -> {
			releaseChord(event.getCode());
		});
	}

	// what happens when a key is pressed
	private void pushChord(KeyCode code, boolean isShiftDown) {
		int key = chordKeyIndex(code);
		if ((key != -1) && (!fingersOnChordKey[key])) {
			fingersOnChordKey[key] = true;
			int[] chord = getChord(code, isShiftDown);
			chordByCode.put(code, chord);
			myStart(chord);
		}
	}

	// what happens when a key is released
	void releaseChord(KeyCode code) {
		int key = chordKeyIndex(code);
		if (key != -1) {
			fingersOnChordKey[key] = false;
			int[] chord = getChord(code);
			myStop(chord);
		}
	}

	// needs to call Controller.start so couldn't override
	private void myStart(int[] chord) {
		for (int note : chord)
			fingersOnNote[noteIndex(note)] += 1;

		for (int note : chord)
			if (fingersOnNote[noteIndex(note)] == 1)
				start(note);
	}

	// needs to call Controller.stop so couldn't override
	private void myStop(int[] chord) {
		for (int note : chord)
			fingersOnNote[noteIndex(note)] -= 1;

		for (int note : chord)
			if (fingersOnNote[noteIndex(note)] == 0)
				stop(note);
	}

	// Basically just hashing a note into fingersOnNote
	private int noteIndex(int note) {
		while (note >= getInstrument().size())
			note = note % getInstrument().size();

		return note;
	}

	// Basically just hashing a key into fingersOnChordKey
	int chordKeyIndex(KeyCode code) {
		switch (code) {
			case A:
				return 0;
			case B:
				return 1;
			case C:
				return 2;
			case D:
				return 3;
			case E:
				return 4;
			case F:
				return 5;
			case G:
				return 6;
			default:
				return -1;
		}
	}

	// Determines which chord should be played
	private int[] getChord(KeyCode code, boolean isShiftDown) {
		char keyChar = code.toString().charAt(0);
		int rootIndex = MusicTheory.getNoteIndex(keyChar);
		int[] chord;

		if (isShiftDown == MAJOR_SHIFT)
			chord = MusicTheory.getMajorChord(rootIndex);
		else
			chord = MusicTheory.getMinorChord(rootIndex);
		return chord;
	}

	// Determines which chord should be stopped
	private int[] getChord(KeyCode code) {
		return chordByCode.get(code);
	}

	@Override
	public Instrument getInstrument() {
		return this.instrument;
	}
}
