package other;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

import java.io.File;


public class HitMusic_Sword {
	  private MediaPlayer swordHit = new MediaPlayer(new Media(new File("sounds/sword.wav").toURI().toString()));

	  
	  public HitMusic_Sword() {
		  swordHit.setCycleCount(1);
		  swordHit.setOnEndOfMedia(()-> {
			  swordHit.stop();
		  });
	  }
	  
	  public void play() {
		  swordHit.play();
	  }
}
