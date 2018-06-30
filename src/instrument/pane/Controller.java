package instrument.pane;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import instrument.Instrument;
import instrument.Note;

public interface Controller {
	static final long RING_TIME = 100;
	static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
	
	Instrument getInstrument();

	default void start(int... notes) {
		if (notes != null)
			for (int note : notes) {
				System.out.println("get " + note + " means " + getInstrument().get(note));
				getInstrument().get(note).start();
			}
	}

	default void stop(int... notes) {
		if (notes != null)

			new Thread(() -> {
				try {
					new CountDownLatch(1).await(RING_TIME, TIME_UNIT);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				for (int note : notes)
					getInstrument().get(note).stop();
			}).start();
	}
}
