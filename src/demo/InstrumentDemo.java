package demo;
import java.io.File;

import instrument.*;
import song.*;

public class InstrumentDemo {
	
	public static void main(String[] args) throws AudioFileException, InterruptedException {
		File dir = new File(new File("Instruments"), "Piano");
		Instrument ins = new Instrument(dir);
		
		Note n = ins.get(0);
		Note n2 = new Note();
		
		n.start();
		n2.start();
	
		Thread.sleep(2000);	
	}
	

}
