package other;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Music_MouseMove {
	private static MediaPlayer music = new MediaPlayer(new Media(new File("sounds/mouse_move.wav").toURI().toString()));
	
	static {
		music.setOnEndOfMedia(()->{
			music.stop();
		});
	}

	public static void play() {
		music.play();
	}
	
	
}
