package skill;

import javafx.scene.effect.Effect;

public abstract class EBall {
	private int damage;
	private double sX, sY;
	private double incX, incY;
	public double deg;
	private int width, height;

	public EBall(int d, int sp, double sX, double sY, double fX, double fY, int wd, int ht, double adjust) {
		damage = d;
		this.sX = sX;
		this.sY = sY;
		deg = Math.atan2(fY - sY, fX - sX) + adjust;
		incX = Math.cos(deg) * sp;
		incY = Math.sin(deg) * sp;
		width = wd;
		height = ht;
	}

	public void setsX(double sX) {
		this.sX = sX;
	}

	public void setsY(double sY) {
		this.sY = sY;
	}

	public double getsX() {
		return sX;
	}

	public double getsY() {
		return sY;
	}

	public double getIncX() {
		return incX;
	}

	public double getIncY() {
		return incY;
	}

	public abstract void setImView();

	public double getX(double d) {
		return sX + width * d;
	}

	public double getY(double d) {
		return sY + height * d;
	}

	public abstract void update();

	public int getWidth() {
		return width;
	}

	public int getDamage() {
		return damage;
	}

	public double getDegree() {
		return deg;
	}

	public abstract void hitStart();

	public abstract boolean isHit();

	public abstract boolean hitEffect();

	public abstract void remove();

	public abstract void setEffect(Effect e);
}
