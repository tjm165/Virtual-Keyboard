package demo;

import instrument.AudioFileException;
import song.Interpreter;
import song.Interpreter.SongData;
import song.Song;

public class InterpreterDemo {

	public static void main(String[] args) throws Exception {
		string().start();
	}

	public static Song string() throws AudioFileException {
		String[] line0 = new String[] { "100", "0", "", "1", "", "4", "" };
		String[] line1 = new String[] { "Synth", "A#M", "1", "R", "3", "A", "1" };

		String[][] source = new String[][] { line0, line1 };

		SongData sd = Interpreter.interpretSong(source);
		Song song = sd.song;

		song.load(1);
		System.out.println(song);
		return song;
	}
}
