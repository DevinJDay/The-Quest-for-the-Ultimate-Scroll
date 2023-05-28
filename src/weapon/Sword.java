package weapon;

import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import monster.Monster;
import character.Character;
import other.HitMusic_Sword;
import javafx.scene.effect.*;

public class Sword extends Weapon {
	private Image im = new Image("file:images/weapon/swords.png");
	private ImageView iv;
	private static final int xs = 30 * 0;
	private static final int ys = 30 * 0;
	private static final int w = 30;
	private static final int h = 30;
	private static final int l = 50; // length of weapon
	private int atFeq; // attack frequency
	private int atc;  // current attack progress
	private List<Monster> ml;
	private Character role;
	private int range = 150;
	private int damage = 200;
	private BorderPane bp;
	private HitMusic_Sword music = new HitMusic_Sword();

	public Sword() {
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
		iv.setFitWidth(l);
		iv.setImage(im);
		iv.setViewport(new Rectangle2D(xs, ys, w, h));
		bp = b;
		Glow glow = new Glow();
		glow.setLevel(1);
		bp.getChildren().add(iv);
	}
	
	public void updateMonster(List<Monster> m) {
		ml = m;
	}
	
	public void update(double mouseX, double mouseY, double deg) {
		if (mouseX > role.getX(0.5)) {
			iv.setScaleX(-1);
			deg += 0.7 * (atc <= 10 ? atc / 5 : 0);
			iv.setRotate(Math.toDegrees(deg) - 135);
		} else {
			deg -= 0.7 * (atc <= 10 ? atc / 5 : 0);
			iv.setScaleX(1);
			iv.setRotate(Math.toDegrees(deg) -  45);

		}
		iv.setX(role.getX(0.5) - 25 - Math.cos(deg) * 50); 
		iv.setY(role.getY(0.5) - 25 - Math.sin(deg) * 50);
		coolDown();
	}
	
	public void tryAttack(double deg, double x, double y) {
		if (!isReady()) return;
		for (int i = 0; i < ml.size(); i++) {
			Monster m = ml.get(i);
			double diff_x = role.getX(0.5) - m.getX(0.5);
			double diff_y = role.getY(0.5) - m.getY(0.5);
			double deg1 =  Math.atan2(-diff_y, -diff_x) + Math.PI;
			if (Math.abs(((6.3 - deg1) % 6.2 - (6.3 - deg) % 6.2)) < 1 && Math.sqrt(diff_x * diff_x + diff_y * diff_y) < range + ((m.getWidth() + m.getHeight())/4) ) {
				double x_m = Math.abs(diff_x) / (Math.abs(diff_x) + Math.abs(diff_y));
				double y_m = 1 - x_m;
				m.changeX(diff_x > 0 ? -10 * x_m : 10 * x_m);
				m.changeY(diff_y > 0 ? -10 * y_m : 10 * y_m);
				m.getHurt(damage);
				music.play();
			}
		}
		
		attack();
	}
	
	public void remove() {
		bp.getChildren().remove(iv);
	}
	
	public boolean readyToChange() {
		return true;
	}

}
