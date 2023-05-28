package monster;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import character.Character;
import other.Other;

public class Lizard extends Monster{
	private static int h = 75;
	private static int sp = 5;
	private static int wd = 75, ht = 75;
	private static int rg =  50;
	private static int at = 20;
	private static int attF = 50;
	private static int type = 0;
	private ImageView iv2;
	private Image run[] = new Image[6];
	private Image attack[] = new Image[5];
	private int standProgress = 0;
	private int attackProgress = 0;
	private int attackChoice =  0;
	private BorderPane bp;
	private Character role;
	private static String name = "Lizard";
	

	
	public Lizard(int px, int py, double dif) {
		super(px, py, (int) (h * dif), sp * (dif / 2), wd, ht, rg, at, type, attF, name);
		run[0] =  new Image("file:images/monster/Lizard/Walk1.png"); 
		run[1] =  new Image("file:images/monster/Lizard/Walk2.png"); 
		run[2] =  new Image("file:images/monster/Lizard/Walk3.png"); 
		run[3] =  new Image("file:images/monster/Lizard/Walk4.png"); 
		run[4] =  new Image("file:images/monster/Lizard/Walk5.png"); 
		run[5] =  new Image("file:images/monster/Lizard/Walk6.png"); 

		attack[0] =  new Image("file:images/monster/Lizard/Attack1.png"); 
		attack[1] =  new Image("file:images/monster/Lizard/Attack2.png"); 
		attack[2] =  new Image("file:images/monster/Lizard/Attack3.png"); 
		attack[3] =  new Image("file:images/monster/Lizard/Attack4.png"); 
		attack[4] =  new Image("file:images/monster/Lizard/Attack5.png"); 
	}
	
	public void setImView(BorderPane b, Character r) {
		bp = b;
		role = r;
		
		iv2 = new ImageView(run[0]);
		iv2.setFitHeight(ht);
		iv2.setFitWidth(wd);
		iv2.setViewport(new Rectangle2D(60,60,120,120));
		bp.getChildren().add(iv2);
		
	}
	
	public void attackAnimation()  {
		attackProgress = (attackProgress + 1 >= 24) ? 0 : attackProgress +  1;
		iv2.setImage(attack[attackProgress/5]);
		if (attackProgress == 23) {
			attackChoice = 0;
			attackProgress = 0;
		}
	}
	
	public void attackChoice() {
		attackChoice = 1;
		attackProgress = 0;
	}
	
	public void animation() {
		double rx = role.getX(0.5), ry = role.getY(0.5);
		double mx = super.getX(0.5), my = super.getY(0.5);
		// direction
		if (rx > mx) {
			iv2.setScaleX(1);
		} else {
			iv2.setScaleX(-1);
		}
		// attack?
		if (Other.dist(rx, ry, mx, my) < rg  && !isDead()) {
			if (super.readyToAttack()) {
				super.attackStart();
				role.getHurt(at);
			}
			if (!super.ifContinue())
				super.newRun(Math.abs(rx - mx) < 10 ? 0 : (rx > mx ? 0.4 : -0.4), Math.abs(ry - my) < 10 ? 0 : (ry > my ? 0.4 : -0.4), 1);
		} else {
			if (!super.ifContinue()) {
				if (Math.random() > 0.1)
					super.newRun(Math.abs(rx - mx) < 10 ? 0 : (rx > mx ? 0.4 : -0.4), Math.abs(ry - my) < 10 ? 0 : (ry > my ? 0.4 : -0.4), 50);
				 else
					super.newRun(rx > mx ? -0.4 : 0.4, ry > my ? -0.4 : 0.4, 50);
			}
			super.changeX(super.getXD() > 0 ? (mx < 1280 ? super.getXD() : 0) : (mx > super.getWidth() ? super.getXD() : 0));
			super.changeY(super.getYD() > 0 ? (my < 720 - super.getHeight() ? super.getYD() : 0) : (my > super.getWidth() ? super.getYD() : 0));
		}
		super.updateAttack();
		super.updateRun();
		iv2.setX(super.getX(0));
		iv2.setY(super.getY(0));
		if (attackChoice != 0)  {
			if (attackChoice ==  1) {
				attackAnimation();
			}
		} else  {
			standProgress = (standProgress + 1 >= 24) ? 0 : standProgress +  1;
			iv2.setImage(run[standProgress/4]);
		}
	}
	
	public void remove() {
		bp.getChildren().remove(iv2);
	}

	public boolean allDone() {
		return super.isDead();
	}
	
	public boolean isBoss() {
		return false;
	}
}
