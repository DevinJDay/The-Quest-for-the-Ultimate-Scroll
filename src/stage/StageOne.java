package stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javafx.scene.image.Image;

public class StageOne extends GameStage{

	private int[][] coordinate = new int[21][21];
	private HashMap<Integer, Room> map = new HashMap<>();
	private Image im = new Image("file:images/Stage/Grass/grass.png");
	private int rooms;
	
	public StageOne(int rn, double difficulty) {
		rooms = rn;
		initialization(rn, difficulty);		
	}
	
	public String getBossName()  {
		return "NiuMoWang ";
	}

	private void initialization(int rn, double difficulty) {
		int x = 10;
		int y = 10;
		coordinate[y][x] = 1;
		map.put(1, new Room1(1, x, y, im, rn, difficulty));
		int[][] dir = {{0, 1}, {0,-1},{1, 0},{-1, 0}};
		ArrayList<Integer> arr = new ArrayList<>();
		Random rd = new Random();
		int t = 1;
		arr.add(1);
		while (t != rn) {
			System.out.println("extra");
			int index = rd.nextInt(arr.size());
			int cur = arr.get(index);
			int tx = map.get(cur).getX(), ty = map.get(cur).getY();
			int d = rd.nextInt(4);
			int nx = tx + dir[d][0], ny = ty + dir[d][1];
			if (coordinate[ny][nx] == 0) {
				coordinate[ny][nx] = ++t;
				map.put(t, new Room1(t, nx, ny, im, rn, difficulty));
				arr.add(t);
			}
			if (coordinate[ty - 1][tx] != 0 && coordinate[ty + 1][tx] != 0 && coordinate[ty][tx - 1] != 0 && coordinate[ty][tx + 1] != 0) {
				arr.remove(index);
			}
		}
		super.setCoordinate(coordinate);
		super.setMatch(map);
		
	}
	
	public boolean isBossDead() {
		return map.get(rooms).isBossDead();
	}
	
	

}
