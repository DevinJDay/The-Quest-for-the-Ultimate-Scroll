package monster;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import character.Character;
import other.Other;

public class Skeleton extends Monster{
	private Image stand = new Image("file:images/monster/Skeleton/Walk.png");
	private Image im_att = new Image("file:images/monster/Skeleton/Attack.png");
	private static int h = 75;
	private static int sp = 5;
	private static int wd = 75, ht = 75;
	private static int rg =  50;
	private static int at = 20;
	private static int attF = 50;
	private static int type = 0;
	private ImageView iv;
	private int standProgress = 0;
	private int attackProgress = 0;
	private int attackChoice =  0;
	private BorderPane bp;
	private Character role;
	private static String name = "Skeleton";
	

	
	public Skeleton(int px, int py, double dif) {
		super(px, py, (int) (h * dif), sp * (dif / 2), wd, ht, rg, at, type, attF, name);
	}
	
	public void setImView(BorderPane b, Character r) {
		bp = b;
		role = r;
		iv =  new ImageView(stand);
		iv.setFitHeight(ht);
		iv.setFitWidth(wd);
		iv.setViewport(new Rectangle2D(50,50,50,50));
		bp.getChildren().add(iv);
	}
	
	public void attackAnimation()  {
		attackProgress = (attackProgress + 1 >= 24) ? 0 : attackProgress +  1;
		iv.setViewport(new Rectangle2D(50 + 150 * (attackProgress / 3) ,50,50,50));
		if (attackProgress == 23) {
			attackChoice = 0;
			iv.setImage(stand);
			attackProgress = 0;
		}
	}
	
	public void attackChoice() {
		iv.setImage(im_att);
		attackChoice = 1;
		attackProgress = 0;
	}
	
	public void animation() {
		double rx = role.getX(0.5), ry = role.getY(0.5);
		double mx = super.getX(0.5), my = super.getY(0.5);
		// direction
		if (rx > mx) {
			iv.setScaleX(1);
		} else {
			iv.setScaleX(-1);
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
		iv.setX(super.getX(0));
		iv.setY(super.getY(0));
		if (attackChoice != 0)  {
			if (attackChoice ==  1) {
				attackAnimation();
			}
		} else  {
			standProgress = (standProgress + 1 >= 24) ? 0 : standProgress +  1;
			iv.setViewport(new Rectangle2D(50 + 150 * (standProgress / 6) ,50,50,50));
		}
	}
	
	public void remove() {
		bp.getChildren().remove(iv);
	}

	public boolean allDone() {
		return super.isDead();
	}
	
	public boolean isBoss() {
		return false;
	}
}
