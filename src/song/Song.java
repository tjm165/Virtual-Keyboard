package song;

import musictheory.Loadable;
import musictheory.MusicTheory;

import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * A Song can start Beats at a specified Count
 * @author Tommy M
 *
 */// NOTE: It would be nice to get this working more like a hashtable
public class Song implements Loadable {
	private Hashtable<Double, Beat> beatsByCount;
	private Stack<Thread> threadsToStart;

	/**
	 * Create a new Song
	 */
	public Song() {
		this.threadsToStart = new Stack<Thread>();
		this.beatsByCount = new Hashtable<Double, Beat>();
	}

	/**
	 * Add a Beat. Cannot add two Beats with the same count
	 * @param count the count to start the Beat at
	 * @param beat the Beat to add
	 * @return true if this was successful
	 */
	public boolean add(double count, Beat beat) {

		if (this.contains(count)) // NOTE: should support this
			return false;

		beatsByCount.put(count, beat);

		return true;
	}

	/**
	 * Indicates if there is a Beat at the given count
	 * @param count the count to check for a Beat at
	 * @return the corresponding boolean
	 */
	public boolean contains(double count) {
		return beatsByCount.containsKey(count);
	}

	/**
	 * The size of this Song. Determined by how many Beats there are
	 * @return
	 */
	public int size() {
		return beatsByCount.size();
	}

	/**
	 * A Song is considered empty if its size is equal to 0
	 * @return the corresponding boolean
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Compare this Song to another Object. Two Songs are considered equal if they
	 * contain the same Beats to start at the same counts
	 * @return the corresponding boolean
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Song))
			return false;

		Song song = (Song) o;

		if (this.size() != song.size())
			return false;

		if (!(this.beatsByCount.equals(song.beatsByCount)))
			return false;

		return true;
	}

	@Override
	public void load(int iterations) {
		for (Beat beat : beatsByCount.values())
			beat.load(iterations);

		PriorityQueue<Double> countQueue = new PriorityQueue<Double>(beatsByCount.keySet());

		for (int i = 0; i < iterations; i++) {
			threadsToStart.add(new Thread(() -> {

				double nextCount;
				double lastCount = 0;
				Beat lastBeat;

				for (int j = 0; j < size(); j++) {
					nextCount = countQueue.poll();
					lastBeat = beatsByCount.get(lastCount);

					try {
						// NOTE: null pointer exception when first count of source isn't 0
						Thread.sleep(MusicTheory.countsToMillis(lastBeat.getBPM(), nextCount - lastCount));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					try {
						beatsByCount.get(nextCount).start();
					} catch (NotLoadedException e) {
						e.printStackTrace();
					}
					lastCount = nextCount;
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
