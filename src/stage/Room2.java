package stage;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import monster.Lizard;
import monster.Monster;
import monster.Mushroom;
import monster.SpiderBoss;

public class Room2 extends Room {
	private int No, x, y; // coordinate
	private List<Monster> ml = new ArrayList<>();
	private Canvas canvas;
	
	
	public Room2(int c, int x, int y, Image im, int rn, double difficulty) {
		No = c;
		this.x = x;
		this.y = y;
		int mn = (int) (2 + Math.random() * 4);
		if (c != 1) {
			while (mn-- != 0) {
				if (Math.random() > 0.5) {
					ml.add(new Mushroom(getRandomX(), getRandomY(), difficulty));
				} else {
					ml.add(new Lizard(getRandomX(), getRandomY(), difficulty));
				}
			}
		}
		if (c == rn)
			ml.add(new SpiderBoss(getRandomX(), getRandomY(), difficulty));
		canvas = new Canvas();
		canvas.setWidth(1280);
		canvas.setHeight(720);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.rgb(131, 164, 72));
		gc.fillRect(0, 0, 1280, 720);
		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < 15; j++) {
				if (Math.random() < 0.2) gc.drawImage(im, 576, 128, 32, 32, i * 50, j * 50, 50, 50);
				else if (Math.random() < 0.05) gc.drawImage(im, 641, 106, 32, 25, i * 50, j * 50, 50, 50);
			}
		}
		
	}

	public Canvas getCanvas() {
		return canvas;
	}
	
	public int getNo() {
		return No;
	}

	public void setNo(int no) {
		No = no;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public  List<Monster> getMonster() {
		return ml;
	}
	
	public boolean isClean() {
		return ml.isEmpty();
	}
	
	public int getRandomX() {
		return (int) (500 + Math.random() * 300);
	}
	
	public int getRandomY() {
		return (int) (300 + Math.random() * 100);
	}
	
	public boolean isBossDead() {
		for (Monster m : ml) {
			if (m.isBoss()) {
				if (m.isDead()) return true;
				else return false;
			}
		}
		return true;
	}
}
