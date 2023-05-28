package skill;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.effect.*;

public class Banana extends EBall {
	private static Image im = new Image("file:images/monster/Flyingeye/projectile_sprite.png");
	private static final int wd = 50;
	private static final int ht = 50;
	private BorderPane bp;
	private ImageView iv;
	private int hit;
	private boolean hit_start;
	private static int d = 5;
	private static int sp = 5;

	public Banana(double sX, double sY, double fX, double fY, BorderPane bp, double adjust) {
		super(d, sp, sX, sY, fX, fY, wd, ht, adjust);
		this.bp = bp;
		setImView();
	}

	public void setImView() {
		iv = new ImageView(im);
		iv.setFitHeight(ht);
		iv.setFitWidth(wd);
		iv.setViewport(new Rectangle2D(0, 0, 48, 48));
		iv.setEffect(null);
		bp.getChildren().add(iv);
	}

	public void setEffect(Effect e) {
		iv.setEffect(e);
	}

	public void update() {
		super.setsX(super.getsX() + super.getIncX());
		super.setsY(super.getsY() + super.getIncY());
		iv.setX(super.getsX());
		iv.setY(super.getsY());
		iv.setRotate((iv.getRotate() + 30) % 360);
	}

	public boolean hitEffect() {
		iv.setViewport(new Rectangle2D(148 + 48 * (hit++ / 3), 0, 48, 48));
		return hit == 12;
	}

	public void hitStart() {
		hit = 0;
		hit_start = true;
		super.setsX(super.getsX() + super.getIncX() * 5);
		super.setsY(super.getsY() + super.getIncY() * 5);
	}

	public boolean isHit() {
		return hit_start;
	}

	public void remove() {
		bp.getChildren().remove(iv);
	}
}
