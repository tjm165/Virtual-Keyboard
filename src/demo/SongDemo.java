package demo;

import java.io.File;

import instrument.*;
import musictheory.Loadable.NotLoadedException;
import musictheory.MusicTheory;
import song.*;

public class SongDemo {

	public static void main(String[] args) throws Exception {
		song();
	}

	static Instrument getInstrument() throws AudioFileException {
		File dir = new File(new File("Instruments"), "Piano");
		return new Instrument(dir);
	}

	public static void note(Note note) {
		try {
			note.start();
		} catch (Exception e) {
		}
	}

	public static void note() {

		try {
			Instrument ins = getInstrument();
			Note note = ins.get(0);

			note.start();
		} catch (Exception e) {
		}
	}

	public static void beat() throws AudioFileException, InterruptedException, NotLoadedException {
		Instrument ins = getInstrument();

		Beat[] beats = new Beat[2];

		beats[0] = new Beat(100);
		beats[0].add(2, ins.get(1));
		beats[0].add(3, ins.get(4));
		beats[0].load(1);

		beats[0].start();
		Thread.sleep(4000);
	}
	
	public static void song() throws NotLoadedException, AudioFileException {
		Instrument ins = getInstrument();

		Beat[] beats = new Beat[2];

		beats[0] = new Beat(200);
		beats[0].add(2, ins.get(1));
		beats[0].add(4, ins.get(4));
		
		beats[1] = new Beat(200);
		beats[1].add(4, ins.get(0));
		
		Song song = new Song();
		
		song.add(0, beats[0]);
		song.add(5, beats[1]);
		song.load(1);
		
		song.start();	
	}
}
