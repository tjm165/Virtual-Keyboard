package instrument;

import java.io.File;

/**
 * An AudioFileException indicates that there is a problem with a File that is expected to contain audio.
 * @author Tommy M
 *
 */
public class AudioFileException extends RuntimeException {
	private String path;
	
	public AudioFileException(String path) {
		this.path = path;
	}
	
	public AudioFileException(File file) {
		this(file.getPath());
	}

	@Override
	public String toString() {
		return "Problem with the Audio File found in " + path;
	}
}
