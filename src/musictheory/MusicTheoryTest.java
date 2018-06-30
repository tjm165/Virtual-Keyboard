package musictheory;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MusicTheoryTest {

	@Test
	void countsToMillis() {
		assertEquals(MusicTheory.countsToMillis(100, -1), 0);
		assertEquals(MusicTheory.countsToMillis(-1, 100), 0);
		assertEquals(MusicTheory.countsToMillis(100, 0), 0);
		assertEquals(MusicTheory.countsToMillis(0, 100), 0);

		assertEquals(MusicTheory.countsToMillis(100, 2), 1200);
	}

	@Test
	void isARest() {
		assertTrue(MusicTheory.isARest(-2));
		assertTrue(MusicTheory.isARest(-1));
		assertFalse(MusicTheory.isARest(0));
	}

	@Test
	void isNatural() {
		// is a rest
		assertFalse(MusicTheory.isNatural(-1));

		// circles down to natural
		assertTrue(MusicTheory.isNatural(12));

		// circles down to accidental
		assertFalse(MusicTheory.isNatural(13));

		//Test corner cases
		assertTrue(MusicTheory.isNatural(0));
		assertTrue(MusicTheory.isNatural(2));
		assertTrue(MusicTheory.isNatural(3));
		assertTrue(MusicTheory.isNatural(5));
		assertTrue(MusicTheory.isNatural(7));
		assertTrue(MusicTheory.isNatural(8));
		assertTrue(MusicTheory.isNatural(10));
		
		assertFalse(MusicTheory.isNatural(1));
		assertFalse(MusicTheory.isNatural(4));
		assertFalse(MusicTheory.isNatural(6));
		assertFalse(MusicTheory.isNatural(9));
		assertFalse(MusicTheory.isNatural(11));
	}
	
	@Test
	void getMajorChord() {
		assertArrayEquals(MusicTheory.getMajorChord(-1), new int[]{-1});
		assertArrayEquals(MusicTheory.getMajorChord(0), new int[]{0, 4, 7});
	}
	
	@Test
	void getMinorChord() {
		assertArrayEquals(MusicTheory.getMinorChord(-1), new int[]{-1});
		assertArrayEquals(MusicTheory.getMinorChord(0), new int[]{0, 3, 7});
	}
	
	@Test
	void getNoteIndex_AllParams() {
		assertEquals(MusicTheory.getNoteIndex('B', MusicTheory.TONE_NATURAL, '0'), 2);
		assertEquals(MusicTheory.getNoteIndex('B', MusicTheory.TONE_SHARP, '0'), 3);
		assertEquals(MusicTheory.getNoteIndex('B', MusicTheory.TONE_FLAT, '0'), 1);
		assertEquals(MusicTheory.getNoteIndex('B', MusicTheory.TONE_NATURAL, '1'), 2 + 12);
	
		boolean caught = false;
		try {
			MusicTheory.getNoteIndex('A', MusicTheory.TONE_FLAT, '0');
		}catch(RuntimeException e) {
			caught = true;
		}
		assertTrue(caught);
		
		
		caught = false;
		try {
			MusicTheory.getNoteIndex('A', MusicTheory.TONE_NATURAL, (char) ('0' - 1));
		}catch(RuntimeException e) {
			caught = true;
		}
		assertTrue(caught);
		
	}
	
	@Test
	void getNoteIndex_CharParam() {
		
		//Test corner cases
		assertEquals(MusicTheory.getNoteIndex('A'), 0);
		assertEquals(MusicTheory.getNoteIndex('a'), 0);
		assertEquals(MusicTheory.getNoteIndex('B'), 2);
		assertEquals(MusicTheory.getNoteIndex('b'), 2);
		assertEquals(MusicTheory.getNoteIndex('C'), 3);
		assertEquals(MusicTheory.getNoteIndex('c'), 3);
		assertEquals(MusicTheory.getNoteIndex('D'), 5);
		assertEquals(MusicTheory.getNoteIndex('d'), 5);
		assertEquals(MusicTheory.getNoteIndex('E'), 7);
		assertEquals(MusicTheory.getNoteIndex('e'), 7);
		assertEquals(MusicTheory.getNoteIndex('F'), 8);
		assertEquals(MusicTheory.getNoteIndex('f'), 8);
		assertEquals(MusicTheory.getNoteIndex('G'), 10);
		assertEquals(MusicTheory.getNoteIndex('g'), 10);
		assertEquals(MusicTheory.getNoteIndex('!'), -1);
	}
	

}
