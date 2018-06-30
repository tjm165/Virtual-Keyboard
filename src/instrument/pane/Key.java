package instrument.pane;

import instrument.Instrument;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

class Key extends Button implements Controller{
	private int note;
	private Instrument instrument;

	public Key(Instrument instrument, int note) {
		super(instrument.get(note).toString());
		this.instrument = instrument;
		this.note = note;
		this.addEventFilter(MouseEvent.MOUSE_PRESSED, (e) -> {
			
			start(note);
		});

		this.addEventFilter(MouseEvent.MOUSE_RELEASED, (e) -> {
			stop(note);
		});
	}

	public void setColorScheme(Color resting, Color playing) {
		setBackground(new Background(new BackgroundFill(resting, null, null)));

		getInstrument().get(note).setDoOnStop(() -> {
			setBackground(new Background(new BackgroundFill(resting, null, null)));
		});

		getInstrument().get(note).setDoOnStart(() -> {
			setBackground(new Background(new BackgroundFill(playing, null, null)));
		});
	}

	@Override
	public Instrument getInstrument() {
		return this.instrument;
	}
}
