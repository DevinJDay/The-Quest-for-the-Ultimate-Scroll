package controller;

import java.io.File;

import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import auth.FirebaseInitializer;
import auth.SignInController;
// TODO
import view.CoverView;

public class GUI extends Application {
	private final int width = 1280;
	private final int height = 720;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		File f = new File("bg.mp4");
		Media m = new Media(f.toURI().toString());
		MediaPlayer mp = new MediaPlayer(m);
		MediaView mv = new MediaView(mp);
		mv.setFitWidth(1280);
		mv.setFitHeight(720);
		mp.play();
		mp.setCycleCount(Integer.MAX_VALUE);
		stage.setHeight(height);
		stage.setWidth(width);
		BorderPane pane = new BorderPane();
		Scene scene = new Scene(pane, stage.getWidth(), stage.getHeight());
		if (pane != null) {
			pane.getChildren().add(mv);
			FirebaseInitializer.initialize();
			SignInController.initialize(pane, this, stage);
		}

		stage.setTitle("Log In");
		stage.setResizable(true);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void stop() {
	}
}
