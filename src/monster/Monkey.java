package monster;

import java.util.ArrayList;
import java.util.List;

import character.Character;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import other.Other;
import skill.Banana;
import skill.EBall;

public class Monkey extends Monster {
	private Image stand = new Image("file:images/monster/Flyingeye/Flight.png");
	private Image im_att = new Image("file:images/monster/Flyingeye/Attack.png");
	private static int h = 75;
	private static int sp = 5;
	private static int wd = 50, ht = 50;
	private static int rg =  400;
	private static int at = 20;
	private static int attF = 200;
	private static int type = 1;
	private ImageView iv;
	private int standProgress = 0;
	private int attackProgress = 0;
	private int attackChoice =  0;
	private BorderPane bp;
	private Character role;
	private List<EBall> balls = new ArrayList<>();
	private static String name = "Flying Eye";

	
	
	public Monkey(int px, int py, double dif) {
		super(px, py, (int) (h * dif), sp * (dif / 2), wd, ht, rg, at, type, attF, name);
	}
	

	public void setImView(BorderPane b, Character r) {
		role = r;
		bp  = b;
		iv =  new ImageView(stand);
		iv.setFitHeight(ht);
		iv.setFitWidth(wd);
		iv.setViewport(new Rectangle2D(50,50,50,50));
		bp.getChildren().add(iv);
	}
	

	
	@Override
	public EBall shootBall(double x2, double y2, double adjust) {
		return new Banana(getX(0.5), getY(0.5), x2, y2, bp, adjust);
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
		if (rx > mx) {
			iv.setScaleX(1);
		} else {
			iv.setScaleX(-1);
		}
		if (Other.dist(rx, ry, mx, my) < rg  && !isDead()) {
			if (super.readyToAttack()) {
				super.attackStart();
				balls.add(shootBall(rx, ry,0));
			}
			if (!super.ifContinue())
				super.newRun(Math.random() > 0.5 ? (mx < 1280 ? 0.4 : 0) : (mx > wd ? -0.4 : 0), Math.random() > 0.5 ? (my < 720 ? 0.4 : 0) : (my > ht ? -0.4 : 0), 50);
			
		} else {
			if (!super.ifContinue()) {
				double ran = Math.random();
				if (ran > 0.1)
					super.newRun(rx > mx ? 0.4 : -0.4, ry > my ? 0.4 : -0.4, 50);
				else
					super.newRun(rx > mx ? -0.4 : 0.4, ry > my ? -0.4 : 0.4, 50);
			}
		}
		super.changeX(super.getXD() > 0 ? (mx < 1280 ? super.getXD() : 0) : (mx > super.getWidth() ? super.getXD() : 0));
		super.changeY(super.getYD() > 0 ? (my < 720 - super.getHeight() ? super.getYD() : 0) : (my > super.getWidth() ? super.getYD() : 0));
		super.updateAttack();
		super.updateRun();		
		update_Balls();
		iv.setX(super.getX(0));
		iv.setY(super.getY(0));
		if (attackChoice != 0)  {
			if (attackChoice ==  1) {
				attackAnimation();
			}
		} else  {
			standProgress = (standProgress + 1 >= 48) ? 0 : standProgress +  1;
			iv.setViewport(new Rectangle2D(50 + 150 * (standProgress / 6) ,50,50,50));
		}
	}
	
	public void remove() {
		bp.getChildren().remove(iv);
	}
	
	private void update_Balls() {
		if (balls.size() == 0) return;
		for (int i = 0; i < balls.size(); i++) {
			EBall b = balls.get(i);
			if (b.isHit()) {
				if (b.hitEffect()) {
					balls.remove(i--);
					b.remove();
				} else continue;
			} else if (Other.dist(b.getX(0.5), b.getY(0.5), role.getX(0.5), role.getY(0.5)) <= role.getWidth() * 0.5 + b.getWidth() * 0.5) {
				b.hitStart();
				role.getHurt(b.getDamage());
			} else if (b.getX(0.5) < 0 || b.getX(0.5) > 1280 || b.getY(0.5) < 0 || b.getY(0.5) > 720) {
				balls.remove(i--);
				b.remove();
			}
			b.update();
		}
	}
	
	public boolean allDone() {
		return super.isDead() && balls.isEmpty();
	}
	
	public boolean isBoss() {
		return false;
	}
	
	


}
