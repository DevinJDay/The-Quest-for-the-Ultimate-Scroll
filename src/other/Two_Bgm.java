package other;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Two_Bgm {
	private static MediaPlayer music = new MediaPlayer(new Media(new File("sounds/bgm/jungle.mp3").toURI().toString()));
	
	static {
		music.setOnEndOfMedia(()->{
			music.stop();
		});
	}

	public static void play() {
		music.play();
	}
	
	public static void stop() {
		music.stop();
	}
	
	
}
