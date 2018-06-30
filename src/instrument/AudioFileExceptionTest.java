package instrument;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AudioFileExceptionTest {

	@Test
	void toStringTest() {
		String path = "path";
		AudioFileException e = new AudioFileException(path);
		
		assertEquals(e.toString(), "Problem with the Audio File found in " + path);
	}

}
