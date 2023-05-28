package stage;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import monster.Goblin;
import monster.Monkey;
import monster.Monster;
import monster.Niumowang;

public class Room1 extends Room {
	private int No, x, y; // coordinate
	private List<Monster> ml = new ArrayList<>();
	private Canvas canvas;
	private int rn;

	public Room1(int c, int x, int y, Image im, int rn, double difficulty) {
		this.rn = rn;
		No = c;
		this.x = x;
		this.y = y;
		int mn = (int) (2 + Math.random() * 4);
		if (c != 1) {
			while (mn-- != 0) {
				if (Math.random() > 0.5) {
					ml.add(new Goblin(getRandomX(), getRandomY(), difficulty));
				} else {
					ml.add(new Monkey(getRandomX(), getRandomY(), difficulty));
				}
			}
			if (c == rn)
				ml.add(new Niumowang(getRandomX(), getRandomY(), difficulty));
		}
		canvas = new Canvas();
		canvas.setWidth(1280);
		canvas.setHeight(720);
		drawMap();

	}

	private void drawMap() {
		Image im = new Image("file:images/Stage/Textures-16.png");
		Image im2 = new Image("file:images/Stage/Map2/tf_B_ashlands_2.png");
		GraphicsContext gc = canvas.getGraphicsContext2D();
		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < 16; j++) {
				gc.drawImage(im, 49, 193, 15, 15, i * 50, j * 50, 50, 50);
				double r = Math.random();
				if (r < 0.008) {
					gc.drawImage(im2, 90, 383, 37, 33, i * 50, j * 50, 50, 50);
				} else if (r < 0.0162) {
					gc.drawImage(im2, 129, 95, 31, 31, i * 50, j * 50, 40, 40);
				} else if (r < 0.02) {
					gc.drawImage(im2, 0, 300, 30, 42, i * 50, j * 50, 40, 50);
				}
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

	public List<Monster> getMonster() {
		return ml;
	}

	public boolean isBossDead() {
		for (Monster m : ml) {
			if (m.isBoss()) {
				if (m.isDead())
					return true;
				else
					return false;
			}
		}
		return true;
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

}
