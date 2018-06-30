package instrument;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

class InstrumentTest {

	@Test
	void constructor() {
		Note n0 = new Note("note 0");
		Note n1 = new Note("note 1");

		Collection<Note> notes = new LinkedList<Note>();
		notes.add(n0);
		notes.add(n1);

		Instrument ins = new Instrument(notes);
		assertEquals(ins.get(0), n0);
		assertEquals(ins.get(1), n1);

		ins = new Instrument(n0, n1);
		assertEquals(ins.get(0), n0);
		assertEquals(ins.get(1), n1);
	}

	@Test
	void get() {
		Note rest = new Note("REST");
		Note n0 = new Note("note 1");
		Note n1 = new Note("note 2");
		Note n2 = new Note("note 3");
		Instrument ins = new Instrument(n0, n1, n2);

		// less than 0 should return rest
		assertEquals(ins.get(-1), rest);

		// 0 should return n0
		assertEquals(ins.get(0), n0);

		// 1 should return n1
		assertEquals(ins.get(1), n1);

		// 2 should return n2
		assertEquals(ins.get(2), n2);

		// 3 should return n0
		assertEquals(ins.get(3), n0);

		// 4 should return n1
		assertEquals(ins.get(4), n1);

		// 5 should return n2
		assertEquals(ins.get(5), n2);

		// Test get(int...)
		assertArrayEquals(ins.get(-1, 2, 1), new Note[] { ins.get(-1), ins.get(2), ins.get(1) });
	}
	
	@Test
	void setName() {
		Instrument ins = new Instrument();
		String name = "Instrument name";
		ins.setName(name);
		assertEquals(name, ins.getName());
	}
	
	@Test
	void getName() {
		Instrument ins1 = new Instrument();
		Instrument ins2 = new Instrument();
		
		assertEquals(ins1.getName(), "Instrument " + (Instrument.NUMINSTANCES - 1));
		assertEquals(ins2.getName(), "Instrument " + Instrument.NUMINSTANCES);
	}
	
	@Test
	void toStringTest() {
		Instrument ins0 = new Instrument();
		assertEquals(ins0.toString(), ins0.getName());
	}
	
	@Test
	void equals() {
		Instrument ins1 = new Instrument(new Note("n1"), new Note("n2"));
		Instrument ins2 = new Instrument(new Note("n1"), new Note("n2"));
		Instrument ins3 = new Instrument(new Note("n2"), new Note("n1"));

		//Test when Note order is the same
		assertTrue(ins1.equals(ins2));
		
		//Test when Note order is different
		assertFalse(ins1.equals(ins3));
		
		//Test when a different Object
		assertFalse(ins1.equals(""));
	}

}
