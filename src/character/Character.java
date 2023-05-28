package character;
import javafx.scene.image.Image;
import weapon.Weapon;

public abstract class Character {
	private final int height = 100;  // height of character
	private final int width = 50; // width of character
	private double px; // position of horizontal-axis
	private double py; // position of vertical-axis
	private int h; // health
	private int cur_h;
	private int sp; // speed
	private Weapon weapon;
	private int num_phase;
	
	
	public Character(int px, int py, int h, int sp, int np) {
		this.px = px;
		this.py = py;
		this.h = cur_h = h;
		num_phase = np;
		this.sp = sp;
	}
	
	public int getPhase() {
		return num_phase;
	}
	
	public abstract Image getImage();
	
	public abstract void change(int n);

	public double getX(double d) {
		return px + width * d;
	}
	
	public double getY(double d) {
		return py + height * d;
	}
	
	public void changeX(double x) {
		px += x * sp;
	}
	
	public void changeY(double x) {
		py += x * sp;
	}
	
	public void setX(int n) {
		px = n;
	}
	
	public void setY(int n) {
		py = n;
	}
	
	public void take(Weapon w) {
		weapon = w;
	}
	
	public Weapon getWeapon() {
		return weapon;
	}

	public int getHeight() {
		 return height;
	}
	
	public int getWidth() {
		 return width;
	}
	
	public int getHealth() {
		return cur_h;
	}
	
	public int getMaxHealth() {
		return h;
	}
	
	public  void getHurt(int d) {
		cur_h -= d;
		cur_h = cur_h >= 0 ? cur_h : 0;
	}
}
