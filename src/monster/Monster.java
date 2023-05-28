package monster;

import javafx.scene.layout.BorderPane;
import skill.EBall;
import character.Character;



public abstract class Monster {
	private double px, py; // position at x-axis & y-axis
	private int h; // health
	private int cur_h;
	private double sp; // speed
	private int wd, ht; // width & height
	private int range; // attack range
	private int attack;  // damage
	private int runTime; // time for a run
	private double xD, yD;  // coefficient of speed in x-axis
	private int type; // type of monster(shooter 1 / close fighter 0)
	private int attFeq; // attack frequency
	private int attCur; // current attack progress
	private String name;
	
	public Monster(int px, int py, int h, double sp, int wd, int ht, int range, int att, int t, int af, String n) {
		this.px = px;
		this.py = py;
		this.h = cur_h = h;
		this.sp = sp;
		this.wd = wd;
		this.ht = ht;
		this.range = range;
		this.attack = att;
		type = t;
		attFeq = af;
		attCur = 0;
		name = n;
	}
	
	public abstract void setImView(BorderPane b, Character r);
	
	
	public abstract void remove();
	


	public double getX(double d) {
		return px + wd * d;
	}
	
	public double getY(double d) {
		return py + ht * d;
	}
	
	public void changeX(double x) {
		px += x * sp;
	}
	
	public void changeY(double x) {
		py += x  * sp;
	}
	

	// return false if dead
	public void getHurt(int d) {
		cur_h -= d;
	}
	
	public boolean isDead() {
		return cur_h <= 0;
	}
	
	public abstract boolean allDone();
	
	public int getWidth() {
		return wd;
	}
	
	public int getHeight() {
		return ht;
	}
	
	public int getTrueWidth() {
		return wd;
	}
	
	public int getTrueHeight() {
		return ht;
	}
	
	
	
	public int getMaxHealth() {
		return h;
	}
	
	public int getHealth() {
		return cur_h;
	}
	
	public int getAttack() {
		return attack;
	}
	
	public int getRange() {
		return range;
	}
	
	// return true if current moving
	public boolean ifContinue() {
		return runTime > 0;
	}
	
	public void newRun(double x, double y, int t) {
		runTime = t;
		xD = x;
		yD = y;
	}
	
	public double getXD() {
		return xD;
	}
	
	public double getYD() {
		return yD;
	}
	
	public void updateRun() {
		runTime--;
	}
	
	public int getType() {
		return type;
	}
	public void updateAttack() {
		attCur++;
	}
	
	public void attackStart() {
		attCur = 0;
		attackChoice();
		
	}
	
	public boolean readyToAttack() {
		return attCur > attFeq;
	}
	
	public EBall shootBall(double x2, double y2, double adjust) {
		return null;
	}
	
	public abstract void attackChoice();
	
	public abstract void animation();
	
	public String getName()  {
		return name;
	}
	
	public abstract boolean isBoss();
}
