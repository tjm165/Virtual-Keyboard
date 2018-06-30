package song;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import instrument.*;
import musictheory.Loadable;
import musictheory.MusicTheory;
import musictheory.Loadable.NotLoadedException;

/**
 * Holds a Collection of Notes that are each supposed to be played for a
 * specific length of time All units are in counts. To convert to milliseconds,
 * use MusicTheory methods
 * @author Tommy M
 *
 */
public class Beat implements Loadable {
	private int bpm;
	private int size;
	private Stack<Thread> threadsToStart;
	private TreeMap<Double, List<Note>> notesByLength;

	/**
	 * Create a new Beat
	 * @param bpm the beats per minute of this Beat
	 */
	public Beat(int bpm) {
		this.bpm = bpm;
		this.threadsToStart = new Stack<Thread>();
		this.notesByLength = new TreeMap<Double, List<Note>>();
	}

	/**
	 * Change the beats per minute of this Beat
	 * @param bpm the new beats per minute
	 */
	public void setBPM(int bpm) {
		this.bpm = bpm;
	}

	/**
	 * Get the beats per minute of this Beat
	 * @return the beats per minute of this Beat
	 */
	public int getBPM() {
		return bpm;
	}

	/**
	 * Add the given notes and plan for them to be played for the indicated amount
	 * of time
	 * @param length the length of time for the notes to be played for
	 * @param notes the notes to play
	 * @return true if successful
	 */
	public boolean add(double length, Note... notes) {
		List<Note> data;
		if (notesByLength.containsKey(length))
			data = this.notesByLength.get(length);
		else {
			data = new LinkedList<Note>();
			notesByLength.put(length, data);
		}

		for (Note note : notes)
			data.add(note);

		size += notes.length;
		return true;
	}

	/**
	 * The size of this Beat is dependant on how many Notes it holds
	 * @return the size of this Beat
	 */
	public int size() {
		return size;
	}

	/**
	 * Obtain all the notes this Beat holds
	 * @return the notes this Beat holds
	 */
	public Note[] getNotes() {
		Note[] notes = new Note[size()];

		int i = 0;
		for (List<Note> list : notesByLength.values())
			for (Note note : list) {
				notes[i] = note;
				i++;
			}

		return notes;
	}

	/**
	 * Obtain all the lengths this Beat plays notes for
	 * @return the lengths this Beat plays notes for
	 */
	public Set<Double> getLengths() {
		return notesByLength.keySet();
	}

	/**
	 * Determines if this Beat is equal to another Object. Two Beats are equal if
	 * they have the same bpm and Notes that play for the same length
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Beat))
			return false;

		Beat beat = (Beat) o;

		if (this.getBPM() != beat.getBPM())
			return false;

		if (this.size() != beat.size())
			return false;

		Iterator<List<Note>> notes0 = this.notesByLength.values().iterator();
		Iterator<Double> lengths0 = this.notesByLength.keySet().iterator();

		Iterator<List<Note>> notes1 = beat.notesByLength.values().iterator();
		Iterator<Double> lengths1 = beat.notesByLength.keySet().iterator();

		// check that notes and lengths are equal
		while (notes0.hasNext() && notes1.hasNext() && lengths0.hasNext() && lengths1.hasNext()) {
			if (!(lengths0.next().equals(lengths1.next())))
				return false;
			if (!(notes0.next().containsAll(notes1.next())))
				return false;
		}

		return true;
	}

	@Override
	public void load(int iterations) {
		Note[] notes = getNotes();
		Set<Double> lengths = getLengths();

		for (Note note : notes)
			note.load(iterations);

		for (int i = 0; i < iterations; i++) {
			threadsToStart.add(new Thread(() -> {

				for (Note note : notes) {
					note.start();
				}

				for (Double length : lengths) {
					try {
						Thread.sleep(MusicTheory.countsToMillis(getBPM(), length));
						for (Note note : notesByLength.get(length))
							note.stop();
					} catch (InterruptedException e) {
					}
				}

			}));
		}
	}

	@Override
	public void start() throws NotLoadedException {
		if (threadsToStart.isEmpty())
			throw new NotLoadedException();

		threadsToStart.pop().start();
	}
}
