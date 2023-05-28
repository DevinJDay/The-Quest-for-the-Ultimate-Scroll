package weapon;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import monster.Monster;
import other.Other;
import skill.Arrow;
import skill.EBall;
import character.Character;
import other.HitMusic_Arrow;

public class Bow extends Weapon {
	private Image im = new Image("file:images/weapon/BowLoaded.png");
	private Image im2 = new Image("file:images/weapon/Bow.png");
	private ImageView iv;
	private static final int xs = 0;
	private static final int ys = 0;
	private static final int w = 200;
	private static final int h = 200;
	private static final int l = 50; // length of weapon
	private final int wd = 50;
	private int atFeq; // attack frequency
	private int atc;  // current attack progress
	private List<Monster> ml;
	private Character role;
	private BorderPane bp;
	private List<EBall> balls = new ArrayList<>();
	private HitMusic_Arrow music = new HitMusic_Arrow();


	public Bow() {
		super(xs, ys, w, h, l);
		atFeq = 50;
		iv = new ImageView();
		atc = atFeq;
	}
	
	public Image getImage() {
		return im;
	}

	public void attack() {
		atc = 0;
	}
	
	public void coolDown() {
		atc += atc == atFeq ? 0 : 1;
	}
	
	public boolean isReady() {
		return atc == atFeq;
	}
	
	public void setImView(List<Monster> m, Character r, BorderPane b) {
		ml = m;
		role = r;
		iv.setFitHeight(l);
		iv.setFitWidth(wd);
		iv.setImage(im);
		iv.setViewport(new Rectangle2D(xs, ys, w, h));
		bp = b;
		bp.getChildren().add(iv);
	}
	
	public void updateMonster(List<Monster> m) {
		ml = m;
	}
	
	public void update(double mouseX, double mouseY, double deg) {
		if (isReady()) {
			iv.setImage(im);
			iv.setViewport(new Rectangle2D(xs, ys, w, h));
			iv.setFitHeight(l);
			iv.setFitWidth(wd);
		} else if (atc == 0) {
			iv.setImage(im2);
			iv.setViewport(new Rectangle2D(xs, ys, 124, 198));
			iv.setFitHeight(l);
			iv.setFitWidth(31);
		}
		if (mouseX > role.getX(0.5)) {
			iv.setScaleX(-1);
			iv.setRotate(Math.toDegrees(deg) - 145);
		} else {
			iv.setScaleX(1);
			iv.setRotate(Math.toDegrees(deg) -  35);

		}
		iv.setX(role.getX(0.5) - (isReady() ? 25 : 15) - Math.cos(deg) * 30); 
		iv.setY(role.getY(0.5) - 25 - Math.sin(deg) * 30);
		updateBalls();
		coolDown();
	}
	
	public void tryAttack(double deg, double mouseX, double mouseY) {
		if (!isReady()) return;
		balls.add(shootBall(mouseX, mouseY));
		attack();
	}
	
	public EBall shootBall(double x2, double y2) {
		return new Arrow(role.getX(0.27), role.getY(0.27), x2, y2, bp, 0);
	}
	
	public void updateBalls()  {
		for (int i = 0; i < balls.size(); i++) {
			EBall b = balls.get(i);
			for (Monster m : ml) {
				if (b.isHit()) {
					if (b.hitEffect()) {
						balls.remove(i);
						b.remove();
						break;
					} else continue;
				} else if (Other.dist(b.getX(0.5), b.getY(0.5), m.getX(0.5), m.getY(0.5)) <= m.getTrueHeight() * 0.5 + b.getWidth() * 0.5  && !m.isDead()) {
					b.hitStart();
					m.getHurt(b.getDamage());
					music.play();
				}
			}
			if (b.getX(0.5) < 0 || b.getX(0.5) > 1280 || b.getY(0.5) < 0 || b.getY(0.5) > 720) {
					balls.remove(i);
					b.remove();
					break;
				}
			b.update();
		}
	}
	
	public void remove() {
		bp.getChildren().remove(iv);
	}
	
	public boolean readyToChange() {
		return balls.size() == 0;
	}

}
