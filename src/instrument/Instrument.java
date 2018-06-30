package instrument;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import musictheory.Loadable;
import musictheory.MusicTheory;

/**
 * A Collection of Notes with some music specific methods.
 * @author Tommy M
 *
 */
public class Instrument extends ArrayList<Note> implements Loadable {
	private Note rest;
	private String name;
	static int NUMINSTANCES = 0;

	/**
	 * Constructs an Instrument containing the Notes of the specified Collection
	 * @param notes the Notes to add
	 */
	public Instrument(Collection<? extends Note> notes) {
		super(notes);
		this.name = "Instrument " + ++NUMINSTANCES;
		this.rest = new Note("REST");
	}

	/**
	 * Constructs an Instrument containing the Notes of the specified Collection
	 * @param notes the Notes to add
	 */
	public Instrument(Note... notes) {
		this(Arrays.asList(notes));
	}

	/**
	 * Constructs an Instrument containing the Notes of the specified File
	 * @param folder
	 * @throws AudioFileException
	 */
	public Instrument(File folder) throws AudioFileException {
		this(Instrument.loadFile(folder));
	}

	/**
	 * Obtain a Note by it's index. This circles through the Instrument to guarantee
	 * a Note is returned, even if it is in the incorrect octave An index less than
	 * 0 indicates a rest.
	 * @param note, the index of the indicated Note
	 * @return the indicated Note
	 */
	@Override
	public Note get(int note) {
		if (MusicTheory.isARest(note))
			return this.rest;

		while (note >= this.size())
			note = note % this.size();

		return super.get(note);
	}

	/**
	 * A shortcut that is equivalent to calling get(int) many times.
	 * @param notes the indexes of the specified Notes.
	 * @return the indicated Notes
	 */
	public Note[] get(int... notes) {
		Note[] all = new Note[notes.length];

		for (int i = 0; i < all.length; i++)
			all[i] = this.get(notes[i]);

		return all;
	}

	/**
	 * Set the name of this Instrument
	 * @param name the name of this Instrument
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Obtain the name of this Instrument
	 * @return the name of this Instrument
	 */
	public String getName() {
		return name;
	}

	/**
	 * Obtain the String representation of this Instrument
	 * @return the String representation of this Instrument
	 */
	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Determines if this Instrument is equal to another Object.
	 * The Note indexes must be the same.
	 * @param o the Object to compare to
	 * @return the corresponding boolean value
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Instrument))
			return false;

		boolean notesAreEqual = super.equals(o);

		return notesAreEqual;
	}

	private static Instrument loadFile(File folder) throws AudioFileException {
		String[] notePaths = folder.list();
		Note[] notes = new Note[notePaths.length];

		for (int i = 0; i < notes.length; i++) {
			notes[i] = new Note(notePaths[i], new File(folder, notePaths[i]));
		}

		return new Instrument(notes);
	}

	@Override
	public void load(int iterations) {
		for (Note note : this)
			note.load(iterations);
	}

	@Override
	public void start() throws NotLoadedException {
		throw new UnsupportedOperationException();
	}
}
