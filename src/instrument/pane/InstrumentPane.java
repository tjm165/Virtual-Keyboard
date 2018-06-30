package instrument.pane;

import instrument.Instrument;
import javafx.scene.layout.GridPane;
import musictheory.MusicTheory;

import javafx.scene.paint.Color;

public class InstrumentPane extends GridPane {
	private Color natural, accidental, playing;
	private final Instrument instrument;
	private Key[] buttons;

	public InstrumentPane(Instrument instrument, Color natural, Color accidental, Color playing) {
		this.instrument = instrument;
		this.natural = natural;
		this.accidental = accidental;
		this.playing = playing;
		this.buttons = new Key[instrument.size()];

		this.initKeyboard();
		this.initButtons();
	}

	public InstrumentPane(Instrument instrument) {
		this(instrument, Color.ANTIQUEWHITE, Color.BLACK, Color.WHEAT);
	}

	private void initKeyboard() {
		Keyboard keyboard = new Keyboard(instrument);

		this.setOnKeyPressed(keyboard.keyPressed());
		this.setOnKeyReleased(keyboard.keyReleased());
	}

	private void initButtons() {
		int row;
		for (int i = 0; i < instrument.size(); i++) {
			Key button = new Key(instrument, i);

			if (MusicTheory.isNatural(i)) {
				button.setColorScheme(natural, playing);
				row = 1;
			} else {
				button.setColorScheme(accidental, playing);
				row = 0;
			}

			this.add(button, i, row);
		}
		instrument.load(20);
	}

	public void setColorScheme(Color natural, Color accidental, Color playing) {
		this.natural = natural;
		this.accidental = accidental;
		this.playing = playing;

		for (int i = 0; i < buttons.length; i++)
			if (MusicTheory.isNatural(i))
				buttons[i].setColorScheme(natural, playing);
			else
				buttons[i].setColorScheme(accidental, playing);
	}
}