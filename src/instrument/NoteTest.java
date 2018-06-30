package instrument;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NoteTest {

	@Test
	void constructor() {
		Note note = new Note();
		Note note1 = new Note("Test");
	}
	
	@Test
	void hasSound() {
		Note note = new Note();
		assertFalse(note.hasSound());
	}
	
	@Test
	void setName() {
		Note note = new Note();
		String name = "note 1";
		note.setName(name);
		
		assertEquals(note.getName(), name);
	}
	
	@Test
	void getName() {
		Note note1 = new Note();
		Note note2 = new Note();
		
		assertEquals(note1.getName(), "Note " + (Note.NUMINSTANCES - 1));
		assertEquals(note2.getName(), "Note " + Note.NUMINSTANCES);
	}
	
	@Test
	void toStringTest() {
		Note note1 = new Note();
		
		assertEquals(note1.toString(), note1.getName());
	}
	
	@Test
	void getDoOnStart() {
		Note note = new Note();
		assertEquals(note.getDoOnStart(), null);
	}
	
	@Test
	void setDoOnStart() {
		Note note = new Note();
		Runnable run = () -> {};
		note.setDoOnStart(run);
		
		assertEquals(note.getDoOnStart(), run);
	}
	
	@Test
	void getDoOnStop() {
		Note note = new Note();
		assertEquals(note.getDoOnStop(), null);
	}
	
	@Test
	void setDoOnStop() {
		Note note = new Note();
		Runnable run = () -> {};
		note.setDoOnStop(run);
		
		assertEquals(note.getDoOnStop(), run);
	}
	
	@Test
	void getMillisecondLength() {
		Note note = new Note();
		assertEquals(note.getMillisecondLength(), 0);
	}
	
	@Test
	void getMicrosecondLength() {
		Note note = new Note();
		assertEquals(note.getMicrosecondLength(), 0);
	}
	
	@Test
	void equals() {
		Note note1 = new Note("note");
		Note note2 = new Note("note2");
		
		Runnable start1 = () -> {int i = 1;};
		Runnable start2 = () -> {int i = 2;};
		Runnable stop1 = () -> {int i = 3;};
		Runnable stop2 = () -> {int i = 4;};
		
		//Not a Note
		assertFalse(note1.equals(""));
		
		//Different name
		assertFalse(note1.equals(note2));
		
		//Same name, Different doOnStart
		note1.setName("name");
		note2.setName("name");
		note1.setDoOnStart(start1);
		note2.setDoOnStart(start2);
		note1.setDoOnStop(stop1);
		note2.setDoOnStop(stop1);
		
		assertFalse(note1.equals(note2));
		
		//Same name, Different doOnStop
		note1.setName("name");
		note2.setName("name");
		note1.setDoOnStart(start1);
		note2.setDoOnStart(start1);
		note1.setDoOnStop(stop1);
		note2.setDoOnStop(stop2);
		
		assertFalse(note1.equals(note2));
	
		//Same name, doOnStart and doOnStop
		note1.setName("name");
		note2.setName("name");
		note1.setDoOnStart(start1);
		note2.setDoOnStart(start1);
		note1.setDoOnStop(stop1);
		note2.setDoOnStop(stop1);
		
		assertTrue(note1.equals(note2));
	}

}
