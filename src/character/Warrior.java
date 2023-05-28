package character;

import javafx.scene.image.Image;

public class Warrior extends Character{
	private int p = 0;
	private static int h = 10000;
	private static int sp = 3;
	private Image im1 = new Image("file:images/character/warrior/64X128_Idle_Free.png");
	private Image im2 = new Image("file:images/character/warrior/64X128_Runing_Free.png");

	
	public Warrior(int px, int py) {
		super(px, py, h, sp, 8);
	}
	
	public Image getImage() {
		if (p == 1) return im2; 
		return im1;
	}
	
	public void change(int n) {
		p = n;
	}
	

}
