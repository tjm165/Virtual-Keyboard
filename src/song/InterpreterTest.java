package song;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import musictheory.MusicTheory;

class InterpreterTest {

	@Test
	void getBPM() {

		String[] meta = new String[] { "100", "1", "", "2", "" };
		assertEquals(Interpreter.getBPM(meta), 100);
	}

	@Test
	void getNumBeats() {
		String[] meta = new String[] { "100", "2", "", "3", "" };
		assertEquals(Interpreter.getNumBeats(meta), 2);

		meta = new String[] { "100", "1", "", "2", "", "3", "" };
		assertEquals(Interpreter.getNumBeats(meta), 3);

	}

	@Test
	void getCounts() {
		String[] meta;
		double[] counts;

		// Test 0
		meta = new String[] { "100" };
		counts = new double[] {};
		assertArrayEquals(counts, Interpreter.getCounts(0, meta));

		// Test 1
		meta = new String[] { "100", "2", "" };
		counts = new double[] { 2 };
		assertArrayEquals(counts, Interpreter.getCounts(1, meta));

		// Test many
		meta = new String[] { "100", "2", "", "20", "" };
		counts = new double[] { 2, 20 };
		assertArrayEquals(counts, Interpreter.getCounts(2, meta));

	}

	@Test
	void toNoteID() {
		//Test 1
		assertArrayEquals(Interpreter.toNoteID("B"), new char[] { 'B', '0', '0', '0' });
		
		//Test 2
		assertArrayEquals(Interpreter.toNoteID("B#"), new char[] { 'B', '#', '0', '0' });
		assertArrayEquals(Interpreter.toNoteID("Bb"), new char[] { 'B', 'b', '0', '0' });
		
		assertArrayEquals(Interpreter.toNoteID("B8"), new char[] { 'B', '0', '8', '0' });
		
		assertArrayEquals(Interpreter.toNoteID("BM"), new char[] { 'B', '0', '0', Interpreter.CHORD_MAJOR});
		assertArrayEquals(Interpreter.toNoteID("Bm"), new char[] { 'B', '0', '0', Interpreter.CHORD_MINOR });
		
		//Test 3
		assertArrayEquals(Interpreter.toNoteID("B#8"), new char[] { 'B', '#', '8', '0' });
		
		//Test 4
		assertArrayEquals(Interpreter.toNoteID("B#8M"), new char[] { 'B', '#', '8', Interpreter.CHORD_MAJOR });

	}

}
