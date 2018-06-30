package song;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SongTest {

	@Test
	void add() {
		Song song = new Song();
		Beat b0 = new Beat(100);
		Beat b1 = new Beat(123);

		assertTrue(song.add(0, b0));
		assertEquals(song.size(), 1);
		assertTrue(song.contains(0));

		assertFalse(song.add(0, b1));
		assertEquals(song.size(), 1);
		assertTrue(song.contains(0));

		assertTrue(song.add(1, b1));
		assertEquals(song.size(), 2);
		assertTrue(song.contains(1));
	}

	@Test
	void contains() {
		Song song = new Song();
		Beat b0 = new Beat(123);

		assertFalse(song.contains(5));

		song.add(5, b0);
		assertTrue(song.contains(5));
	}

	@Test
	void size() {
		Song song = new Song();
		Beat b0 = new Beat(123);

		assertEquals(song.size(), 0);

		song.add(0, b0);
		assertEquals(song.size(), 1);

		song.add(1, b0);
		assertEquals(song.size(), 2);
	}

	@Test
	void isEmpty() {
		Song song = new Song();
		Beat b0 = new Beat(123);

		assertTrue(song.isEmpty());

		song.add(0, b0);
		assertFalse(song.isEmpty());
	}

	@Test
	void equals() {
		Song song1 = new Song();
		Song song2 = new Song();

		// Compared to a different Object
		assertFalse(song1.equals(""));

		// Different sizes
		song1 = new Song();
		song2 = new Song();
		song1.add(1, new Beat(123));
		assertFalse(song1.equals(song2));

		// Same Beats different Beat start counts
		song1 = new Song();
		song2 = new Song();
		song1.add(1, new Beat(123));
		song2.add(2, new Beat(123));
		assertFalse(song1.equals(song2));

		// Different Beats same Beat start counts
		song1 = new Song();
		song2 = new Song();
		song1.add(1, new Beat(123));
		song2.add(1, new Beat(13));
		assertFalse(song1.equals(song2));

		// Same Beats same Beat start counts added in different order
		song1 = new Song();
		song2 = new Song();
		song1.add(1, new Beat(123));
		song1.add(2, new Beat(13));
		song2.add(2, new Beat(13));
		song2.add(1, new Beat(123));
		assertTrue(song1.equals(song2));

	}

}
