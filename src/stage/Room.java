package stage;

import java.util.List;

import javafx.scene.canvas.Canvas;
import monster.Monster;

public abstract class Room {
	
	public abstract Canvas getCanvas();
	
	public abstract int getNo();

	public abstract void setNo(int no);

	public abstract int getX();

	public abstract int getY();

	public abstract List<Monster> getMonster();
	
	public abstract boolean isBossDead();
	
	public abstract boolean isClean();
	
	public abstract int getRandomX();
	
	public abstract int getRandomY();
 

}
