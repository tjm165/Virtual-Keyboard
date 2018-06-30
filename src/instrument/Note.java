package instrument;

import java.io.File;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import musictheory.Loadable;

//NOTE: clean up constructors in both data types
public class Note implements Loadable {
	private String name;
	private final Sound sound;
	private Stack<Thread> threadsOnStart;
	private Stack<Thread> threadsOnStop;
	private Runnable doOnStart;
	private Runnable doOnStop;
	static int NUMINSTANCES = 0;

	/**
	 * Create a Note with the default constructor
	 */
	public Note() {
		this("Note " + ++NUMINSTANCES);
	}

	/**
	 * Create a Note with a name
	 * @param name the name of this Note
	 */
	public Note(String name) {
		this(name, null);
	}

	/**
	 * Create a Note with a name and Sound
	 * @param name the name of this Note
	 * @param soundPath the File that contains the Sound
	 * @throws AudioFileException if soundPath is an issue
	 */
	public Note(String name, File soundPath) throws AudioFileException {
		this.name = name;
		
		if (soundPath == null)
			this.sound = null;
		else
			this.sound = new Sound(soundPath);
		
		this.threadsOnStart = new Stack<Thread>();
		this.threadsOnStop = new Stack<Thread>();
	}
	
	/**
	 * Indicates if this Note has a Sound
	 * @return the corresponding boolean
	 */
	public boolean hasSound() {
		return this.sound != null;
	}

	/**
	 * Set the name of this Note
	 * @param name the name of this Note
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Obtain the name of this Note
	 * @return the name of this Note
	 */
	public String getName() {
		return name;
	}

	/**
	 * Obtain the String representation of this Note
	 * @return the String representation of this Note
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Obtain the Runnable that will run when this Note starts playing
	 * @return the Runnable that will run when this Note starts playing
	 */
	public Runnable getDoOnStart() {
		return doOnStart;
	}

	/**
	 * Set the Runnable that will run when this Note starts playing
	 * @param doOnStart the Runnable that will run when this Note starts playing
	 */
	public void setDoOnStart(Runnable doOnStart) {
		this.doOnStart = doOnStart;
	}

	/**
	 * Obtain the Runnable that will run when this Note stops playing
	 * @return the Runnable that will run when this Note stops playing
	 */
	public Runnable getDoOnStop() {
		return doOnStop;
	}

	/**
	 * Set the Runnable that will run when this Note stops playing
	 * @param doOnStart the Runnable that will run when this Note stops playing
	 */
	public void setDoOnStop(Runnable doOnStop) {
		this.doOnStop = doOnStop;
	}

	/**
	 * Start this Note
	 */
	@Override
	public void start() {
		if (hasSound())
			sound.start();
		
		threadsOnStart.pop().start();
	}

	@Override
	public void load(int iterations) {
		for (int i = 0; i < iterations; i++) {
			try {
				threadsOnStart.add(new Thread(doOnStart));
				threadsOnStop.add(new Thread(doOnStop));
				sound.load(1);
			} catch (Exception e) {// NOTE: null pointer with rests..
				;
			}
		}
	}

	/**
	 * Stop this Note
	 */
	public void stop() {
		if (hasSound())
			sound.stop();

		threadsOnStop.pop().start();
	}

	/**
	 * Obtain millisecond length of this Note
	 * @return the millisecond length of this Note
	 */
	public long getMillisecondLength() {
		return getMicrosecondLength() / 1000;
	}

	/**
	 * Obtain microsecond length of this Note
	 * @return the millisecond length of this Note
	 */
	public long getMicrosecondLength() {
		if (!(hasSound()))
			return 0;

		return sound.microsecondLength;
	}

	private String equalsHelper() {
		String important = this.getName();

		if (hasSound())
			important = sound.getPath();

		return important + " " + doOnStart + " " + doOnStop;
	}

	/**
	 * Determines if this Note is equal to another Object
	 * @param o the Object to compare to
	 * @return the corresponding boolean value
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Note))
			return false;

		Note note = (Note) o;

		return this.equalsHelper().equals(note.equalsHelper());
	}

	// The sound that the Note has
	private static class Sound extends File implements Loadable {
		// null once started
		private Stack<Clip> toStart;
		private Queue<Clip> toStop;
		private final long microsecondLength;

		public Sound(File dir) throws AudioFileException {
			super(dir.getAbsolutePath());
			toStart = new Stack<Clip>();
			toStop = new LinkedBlockingQueue<Clip>();
			load(1);
			microsecondLength = toStart.peek().getMicrosecondLength();
		}

		private Clip clip() throws AudioFileException {
			try {
				Clip clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(this));
				return clip;
			} catch (Exception e) {
				throw new AudioFileException(this.getAbsolutePath());
			}
		}

		@Override
		public void load(int iterations) {
			for (int i = 0; i < iterations; i++)
				try {
					toStart.add(clip());
				} catch (AudioFileException e) {
					e.printStackTrace();
				}
		}

		@Override
		public void start() {
			if (toStart.isEmpty())
				throw new RuntimeException("Out of clips to start");

			Clip clip = toStart.pop();
			toStop.add(clip);
			clip.start();
		}

		public void stop() {
			if (!toStop.isEmpty())
				toStop.poll().stop();
		}
	}
}
