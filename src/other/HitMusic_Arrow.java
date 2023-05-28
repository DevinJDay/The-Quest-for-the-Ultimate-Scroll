package other;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

import java.io.File;


public class HitMusic_Arrow {
	  private MediaPlayer swordHit = new MediaPlayer(new Media(new File("sounds/arrow.wav").toURI().toString()));

	  
	  public HitMusic_Arrow() {
		  swordHit.setCycleCount(1);
		  swordHit.setOnEndOfMedia(()-> {
			  swordHit.stop();
		  });
	  }
	  
	  public void play() {
		  swordHit.play();
	  }
}
