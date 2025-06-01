import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GuessANumber2 extends Application {
	private TicTacToeButton[][] buttons;
	private char zeichen = 'x';
	public static void main(String[] args) {
		launch();
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane mainPane = new BorderPane();
		HBox nameBox = new HBox();
		nameBox.getChildren().addAll(new Label("Name"), new TextField());
		
		HBox contactBox = new HBox();
		contactBox.getChildren().addAll(new Label("Telefon"), new TextField(), new Label("E-Mail"), new TextField());
		
		VBox centerPane = new VBox();
		centerPane.getChildren().addAll(nameBox, contactBox);
		
		mainPane.setCenter(centerPane);
		
		VBox bottomPane = new VBox();
		HBox bottomButtons = new HBox();
		bottomButtons.getChildren().addAll(new Button("Something 1"), new Button("something 2"));
		bottomButtons.setAlignment(Pos.CENTER);
		bottomPane.getChildren().addAll(new Label("Something"), bottomButtons);
		mainPane.setBottom(bottomPane);
		
		Scene scene = new Scene(mainPane);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
