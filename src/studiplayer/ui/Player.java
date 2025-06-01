package studiplayer.ui;

import studiplayer.audio.*;

import java.io.File;
import java.net.URL;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;

public class Player extends Application{
	// Attributes
	private String pathname;
	
	// CONSTANTS
	private static final String PLAYLIST_DIRECTORY = "playlists/";
	public static final String DEFAULT_PLAYLIST = PLAYLIST_DIRECTORY + "DefaultPlayList.m3u";
	private static final String INITIAL_PLAY_TIME_LABEL = "00:00";
	private static final String NO_CURRENT_SONG = "-";
	
	// used for front end
	private Button playButton, pauseButton, stopButton, nextButton, filterButton; 
	private Label playListLabel, playTimeLabel, currentSongLabel;
	private ChoiceBox<SortCriterion> sortChoiceBox = new ChoiceBox<SortCriterion>();
	private TextField searchTextField = new TextField();
	
	// Other
	private PlayList playList;
	private boolean useCertPlayList = false;
	PlayerThread playerThread;
	TimerThread timerThread;
	boolean stopped = false, isPaused = false;
	
	@Override
	public void start(Stage stage) throws Exception {
		
		
		stage.setTitle("APA Player");
		BorderPane mainPane = new BorderPane();
		
		if (!useCertPlayList) {
			// choose m3u to load
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			File selectedFile = fileChooser.showOpenDialog(stage);
			if (selectedFile != null) {
				pathname = selectedFile.getAbsolutePath();
			}
			else {
				pathname = DEFAULT_PLAYLIST;
			}
			
		} else {
			pathname = DEFAULT_PLAYLIST;
		}
		
		loadPlayList(pathname);
		// create buttons
		HBox controlBox = new HBox();
		controlBox.setAlignment(Pos.CENTER);
        playButton = createButton("play.jpg");
        pauseButton = createButton("pause.jpg");
        stopButton = createButton("stop.jpg");
        nextButton = createButton("next.jpg");

		playButton.setDisable(false);
		pauseButton.setDisable(true);
		stopButton.setDisable(true);
		nextButton.setDisable(false);

        controlBox.getChildren().addAll(playButton, pauseButton, stopButton, nextButton);
		
		// Event handling using Lambda Expressions
		playButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				playerThread = new PlayerThread();
				timerThread = new TimerThread();

				playerThread.start();
				timerThread.start();

				playButton.setDisable(true);
				pauseButton.setDisable(false);
				stopButton.setDisable(false);
				nextButton.setDisable(false);
			}
		});
		
		pauseButton.setOnAction(e -> {
			playList.currentAudioFile().togglePause();
			if (isPaused) {
				timerThread = new TimerThread();
				timerThread.start();
				isPaused = false;
			}
			else {
				timerThread.terminate();
				isPaused = true;
			}
			playButton.setDisable(true);
			pauseButton.setDisable(false);
			stopButton.setDisable(false);
			nextButton.setDisable(false);
		});
		
		stopButton.setOnAction(e -> {
			playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);
			playerThread.terminate();
			timerThread.terminate();
			playButton.setDisable(false);
			pauseButton.setDisable(true);
			stopButton.setDisable(true);
			nextButton.setDisable(false);
		});
		
		nextButton.setOnAction(e -> {
			stopButton.fire();
			playList.nextSong();
			playButton.fire();
		});
		
		
		// 
		// Top
		HBox filterBox = new HBox();
        searchTextField = new TextField();
        filterButton = new Button("Filter");
        
        sortChoiceBox = new ChoiceBox<>();
        sortChoiceBox.getItems().addAll(SortCriterion.ALBUM, SortCriterion.AUTHOR, SortCriterion.DEFAULT, SortCriterion.DURATION, SortCriterion.TITLE);

		// Event Handling of Search and Sort
		sortChoiceBox.setOnAction(e -> {
			
		});
		
		filterButton.setOnAction(e ->{
			playList.setSearch(searchTextField.getText());
			playList.setSortCriterion(sortChoiceBox.getValue());
			SongTable songTable = new SongTable(playList);
	        mainPane.setCenter(songTable);
		});
     		
        // Formatting for Top
        VBox labelsBox = new VBox();
        labelsBox.getChildren().addAll(new Label("Search:"), new Label("Sort by:") );
        labelsBox.setSpacing(11);
        
        HBox inputsBoxRow2 = new HBox();
        inputsBoxRow2.getChildren().addAll(sortChoiceBox, filterButton);
        inputsBoxRow2.setSpacing(25);
        
        VBox inputsBox = new VBox();
        inputsBox.getChildren().addAll(searchTextField, inputsBoxRow2);
        inputsBox.setSpacing(1);
        

        
        
        filterBox.getChildren().addAll(labelsBox, inputsBox);
        filterBox.setSpacing(10);
        TitledPane filterPane = new TitledPane("Filter", filterBox);
        mainPane.setTop(filterPane);

        
        // Center - Playlist Table
        SongTable songTable = new SongTable(playList);
        mainPane.setCenter(songTable);

        
        
        // Bottom
        VBox infoGrid = new VBox();
        
        HBox playListNameBox = new HBox();
        Label playListText = new Label("Playlist: ");
        playListLabel = new Label(pathname);
        playListText.setPrefWidth(100);
        playListNameBox.getChildren().addAll(playListText, playListLabel);
        
        HBox playTimeBox = new HBox();
        Label playTimeText = new Label("Play Time: ");
        playTimeText.setPrefWidth(100);
        playTimeLabel = new Label(INITIAL_PLAY_TIME_LABEL);
        playTimeBox.getChildren().addAll(playTimeText, playTimeLabel);
        
        HBox currentSongBox = new HBox();
        Label currentSongText = new Label("Current Song: ");
        currentSongText.setPrefWidth(100);
        currentSongLabel = new Label(NO_CURRENT_SONG);
        currentSongBox.getChildren().addAll(currentSongText, currentSongLabel);

        infoGrid.getChildren().addAll(playListNameBox, playTimeBox, currentSongBox);
        
        VBox bottomBox = new VBox();
        bottomBox.getChildren().addAll(infoGrid, controlBox);
        mainPane.setBottom(bottomBox);
        
        stage.setOnCloseRequest(event -> {
            System.out.println("Stage is closing");
            stopButton.fire();
        });
		
		Scene scene = new Scene(mainPane, 600, 400);
		stage.setScene(scene);
		stage.show();	
		
	}
	
	// method to set playlist
	public void loadPlayList(String pathname) {
		
		if (pathname == null || pathname.isEmpty()) {
			playList = new PlayList();
		} else {
			
			if (!pathname.contains("playlists")) {
				pathname = PLAYLIST_DIRECTORY + pathname;
			}
			File file = new File(pathname);
			if (file.canRead()) {
				System.out.println("Attempting to load playlist from: " + file.getAbsolutePath());
				playList = new PlayList(pathname);
			}
		}
	}

	public Button createButton(String iconfile) {
		Button button = null;
		try {
			// URL url = getClass().getResource("/icons/" + iconfile);
			URL url = new File("icons/" + iconfile).toURI().toURL();
			Image icon = new Image(url.toString());
			ImageView imageView = new ImageView(icon);
			imageView.setFitHeight(20);
			imageView.setFitWidth(20);
			button = new Button("", imageView);
			button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			button.setStyle("-fx-background-color: #fff;");
		} catch (Exception e) {
			System.out.println("Image " + "icons/" + iconfile + " not found!");
			e.printStackTrace();
			System.exit(-1);
		}
		return button;
	}
	
	
	
	
	
	public void setUseCertPlayList(boolean useCertPlayList) {
		this.useCertPlayList = useCertPlayList;
	}

	
	
	public static void main(String[] args) {
		launch();
	}
	
	public class PlayerThread extends Thread{
		public void run() {
			stopped = false;
			while(!stopped) {
				playList.currentAudioFile().play();
				playList.nextSong();
			}
		}
		
		public void terminate() {
			stopped = true;
			playList.currentAudioFile().stop();
			playList.decrementCurrentPos();
		}
	}
	
	public class TimerThread extends Thread{
		public void run() {
			stopped = false;
			while (!stopped) {
				Platform.runLater(() -> {
					//System.out.println("Current song Position" + playList.currentAudioFile().formatPosition());
					currentSongLabel.setText(playList.currentAudioFile().toString());
					playTimeLabel.setText(playList.currentAudioFile().formatPosition());
				});
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		public void terminate() {
			stopped = true;
		}
	}
}
