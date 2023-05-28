package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import monster.Monster;
import java.util.List;

import character.Character;

public class UI extends Canvas{
	private Character role;
	private GraphicsContext gc;
	private Image red1 = new Image("file:images/ui/red/red1.png");
	private Image red2 = new Image("file:images/ui/red/health.png");
	private Image red10 = new Image("file:images/ui/red/red10.png");
	private Image red3 = new Image("file:images/ui/red/red3.png");
	private Image red5 = new Image("file:images/ui/red/red5.png");
	private Image red6 = new Image("file:images/ui/red/red6.png");
	private Image red7 = new Image("file:images/ui/red/red7.png");
	private Image boss1 = new Image("file:images/ui/red/bg.png");
	private Image boss2 = new Image("file:images/ui/red/red.png");

	private final int x = 10, y = 10;
	private final int width = 75, height = 75;

	public UI() {
		gc = this.getGraphicsContext2D();
		this.setWidth(1280);
		this.setHeight(720);
	}
	
	public void drawHealth() {
		gc.clearRect(0, 0, 1280, 720);
		gc.drawImage(red10, x + width * 2.33, y + 25, width * 2, height / 2);
		gc.drawImage(red7, x + 20, y + 5 , width * 3.8, height / 2);
		gc.drawImage(red3, x + width * 4, y + 5, width / 3, height / 2);
		double percent = (role.getHealth() * 1.0) / (1.0 * role.getMaxHealth());
		gc.drawImage(red5, x + 50, y + 6, width * 3.35 * percent, height / 2 - 2);
		gc.drawImage(red6, width * 3.35 * percent + x + 50, y + 6, width / 3, height / 2 - 2);
		gc.drawImage(red1, x, y, width, height);
		gc.drawImage(red2, x + 20, y + 20 , width - 40, height - 40);
		gc.setFill(Color.WHITE);
		gc.fillText("Health " + role.getHealth() + " / " + role.getMaxHealth(), 200, 65);

	}
	
	public void setRole(Character r) {
		role = r;
	}
	
	public void drawMonsterHealth(List<Monster> ml) {
		for (Monster m : ml) {
			double percent = (m.getHealth() * 1.0) / (m.getMaxHealth() * 1.0);
			if (m.isBoss()) {
				gc.drawImage(boss1, 800, 30, 400, 50);
				gc.drawImage(boss2, 0, 0, 32 * (percent >= 0.1 ? 1 : percent / 0.1) , 48, 800, 30, 50 * (percent >= 0.1 ? 1 : percent / 0.1), 50);
				if (percent > 0.1) gc.drawImage(boss2, 32, 0, 192, 48, 850, 30, percent < 0.9 ? percent * 300 : 300, 50);
				if (percent > 0.9) gc.drawImage(boss2, 224, 0, 32 * ((percent - 0.9) / 0.1) , 48, 1150, 30, 50 * ((percent - 0.9) / 0.1), 50);
				gc.setFill(Color.WHITE);
				gc.fillText(m.getHealth() + " / " + m.getMaxHealth(), 985, 60);
				gc.fillText(m.getName(), 985, 30);
			} else {
				gc.setFill(Color.RED);
				gc.fillRect(m.getX(0), m.getY(0) - 6, percent * m.getWidth(), 3);
			}
		}
	}
	
	
}
