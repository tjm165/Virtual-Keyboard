package demo;

import java.io.File;

import javafx.scene.control.ToolBar;

import instrument.AudioFileException;
import instrument.Instrument;
import instrument.pane.InstrumentPane;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import musictheory.Loadable.NotLoadedException;
import song.Interpreter;
import song.Interpreter.SongData;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;

public class GUIDemo extends Application {
	private GridPane gridpane;
	private ToolBar toolbar;
	private SongData songdata;
	private static final Color PLAYCOLOR = Color.AQUA;

	@Override
	public void init() throws Exception {
		super.init();
		this.gridpane = new GridPane();
		gridpane.setPrefSize(gridpane.getPrefWidth(), 150);
		setInstrument(new Instrument(new File(new File("Instruments"), "Piano")), 1);
		this.songdata = null;
	}

	private void setInstrument(Instrument instrument, int row) {
		InstrumentPane inspane = new InstrumentPane(instrument, randomColor(), randomColor(), PLAYCOLOR);
		gridpane.add(inspane, 0, row);
		gridpane.autosize();
	}

	private Color randomColor() {
		return new Color(Math.random(), Math.random(), Math.random(), 1);
	}

	@Override
	public void start(Stage stage) throws Exception {
		initToolPane(stage);

		gridpane.add(toolbar, 0, 0);

		Scene scene = new Scene(gridpane);
		stage.setScene(scene);
		stage.show();
	}

	private void initToolPane(Stage stage) {
		Button open = new Button("Open");
		open.setOnAction((event) -> {
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().addAll(new ExtensionFilter("csv files", "*.csv"));
			File f = fc.showOpenMultipleDialog(stage).get(0);
			try {
				songdata = Interpreter.interpretSong(f);

				int row = 1;
				for (Instrument ins : songdata.instruments)
					setInstrument(ins, row++);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		Button play = new Button("Play");
		play.setOnAction((event) -> {
			if (songdata != null) {
				try {
					songdata.song.load(1);
					songdata.song.start();

				} catch (NotLoadedException e) {
					e.printStackTrace();
				}
			}
		});

		toolbar = new ToolBar(open, play);
	}

	public static void main(String[] args) throws AudioFileException, InterruptedException {
		Application.launch();
	}
}
