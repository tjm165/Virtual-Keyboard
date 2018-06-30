package song;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import instrument.Note;

class BeatTest {

	@Test
	void constructor() {
		int bpm = 123;
		Beat beat = new Beat(bpm);
		assertEquals(beat.getBPM(), bpm);
	}

	@Test
	void setBPM() {
		int bpm = 123;
		Beat beat = new Beat(100);
		beat.setBPM(bpm);
		assertEquals(beat.getBPM(), bpm);
	}

	@Test
	void getBPM() {
		int bpm = 123;
		Beat beat = new Beat(bpm);
		assertEquals(beat.getBPM(), bpm);
	}

	@Test
	void add() {
		Beat beat = new Beat(123);
		Note n0 = new Note("note 0");
		Note n1 = new Note("note 1");
		Note n2 = new Note("note 2");
		LinkedList<Double> lengths = new LinkedList<Double>();

		beat.add(3, n0, n1);
		lengths.add(3.0);
		assertArrayEquals(beat.getNotes(), new Note[] { n0, n1 });
		assertEquals(beat.size(), 2);
		assertTrue(beat.getLengths().containsAll(lengths));

		beat.add(4.1, n2);
		lengths.add(4.1);
		assertArrayEquals(beat.getNotes(), new Note[] { n0, n1, n2 });
		assertEquals(beat.size(), 3);
		assertTrue(beat.getLengths().containsAll(lengths));

	}

	@Test
	void size() {
		Beat beat = new Beat(123);
		Note n0 = new Note("note 0");
		Note n1 = new Note("note 1");
		Note n2 = new Note("note 2");

		beat.add(3, n0, n1);
		assertEquals(beat.size(), 2);

		beat.add(3, n2);
		assertEquals(beat.size(), 3);
	}

	@Test
	void getNotes() {
		Beat beat = new Beat(123);
		Note n0 = new Note("note 0");
		Note n1 = new Note("note 1");
		Note n2 = new Note("note 2");
		LinkedList<Double> lengths = new LinkedList<Double>();

		beat.add(3, n0);
		beat.add(3, n1);
		lengths.add(3.0);
		assertTrue(beat.getLengths().containsAll(lengths));

		beat.add(4.1, n2);
		lengths.add(4.1);
		assertArrayEquals(beat.getNotes(), new Note[] { n0, n1, n2 });
		assertEquals(beat.size(), 3);
		assertTrue(beat.getLengths().containsAll(lengths));

	}

	@Test
	void equals() {
		Beat beat1 = new Beat(123);
		Beat beat2 = new Beat(1);
		
		Note n1 = new Note("1");
		Note n2 = new Note("2");
		Note n3 = new Note("3");

		// Compared to a different Object
		assertFalse(beat1.equals(""));

		//Different bpm
		assertFalse(beat1.equals(beat2));
		
		//Same bpm
		beat2.setBPM(123);
		assertTrue(beat1.equals(beat2));
		
		//Same bpm different sizes
		beat1 = new Beat(123);
		beat2 = new Beat(123);
		beat1.add(1, n1);
		assertFalse(beat1.equals(beat2));
		
		//Same bpm same Notes different Note lengths
		beat1 = new Beat(123);
		beat2 = new Beat(123);
		beat1.add(1, n1);
		beat2.add(2, n1);
		assertFalse(beat1.equals(beat2));
		
		//Same bpm different Notes same Lengths
		beat1 = new Beat(123);
		beat2 = new Beat(123);
		beat1.add(1, n1);
		beat2.add(1, n2);
		assertFalse(beat1.equals(beat2));
		
		//Same bpm same Notes same Lengths added in different order
		beat1 = new Beat(123);
		beat2 = new Beat(123);
		beat1.add(1, n1, n2);
		beat1.add(2, n3);
		beat2.add(1, n2, n1);
		beat2.add(2, n3);
		assertTrue(beat1.equals(beat2));
	}

}
