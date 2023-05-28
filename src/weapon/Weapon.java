package weapon;

import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import monster.Monster;
import character.Character;

public abstract class Weapon {
	private int length; // size of weapon
	private int xs; // from (x-axis)
	private int ys; // from (y-axis)
	private int w; // width
	private int h; // deep

	
	public Weapon(int xs, int ys, int w, int h, int l) {
		this.xs = xs;
		this.ys = ys;
		this.w = w;
		this.h = h;
		length = l;
	}
		
	public int getXs() {
		return xs;
	}

	public int getYs() {
		return ys;
	}

	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}
	
	public abstract Image getImage();
	
	public int getLength() {
		return length;
	}
	
	public abstract boolean readyToChange();
	
	
	public abstract void attack();
	
	public abstract void coolDown();
	
		
	public abstract void setImView(List<Monster> ml, Character r,  BorderPane bp);
	
	public abstract void tryAttack(double deg, double x, double y);
	
	public abstract void update(double x,  double y, double d);
	
	public abstract void updateMonster(List<Monster> m);
	
	public abstract void remove();
}
