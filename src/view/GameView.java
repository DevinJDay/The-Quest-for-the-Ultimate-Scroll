package view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import stage.GameStage;
import stage.StageOne;
import stage.StageThree;
import stage.StageTwo;
import weapon.Bow;
import weapon.Sword;
import weapon.Weapon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import monster.Monster;
import other.Music_MouseMove;
import other.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import character.Character;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import auth.FirebaseInitializer;
import auth.UserData;

public class GameView extends Canvas {
	private final double width = 1280;
	private final double height = 720;
	private final int up_x = 590, up_y = 0; // position of upper gate
	private final int right_x = 1215, right_y = 290;
	private final int down_x = 590, down_y = 620;
	private final int left_x = 0 , left_y = 290;
	private double mouseX, mouseY; // mouse position
	private double deg;
	private GameStage st;
	private GraphicsContext gc; // for others
	private Character role;
	private List<Monster> ml;
	private Weapon wp;
	private boolean up, down, left, right; // keyboard code
	private BorderPane bp;
	private int direction = 0;
	private int phase = 0;
	private UI ui;
	private Timeline timeline;
	private int n_stage;
	private double diff;
	private int text;
	private Canvas ca_text;
	private GraphicsContext gc_text; // for others
	private String textString;
	private boolean dead = false;
	private boolean bossDead = false;
	private ArrayList<Image> fires = new ArrayList<>();
	private int tic = 0;
	private Stage stage;
	private ArrayList<ImageView> doors = new ArrayList<>();

	public GameView(BorderPane b, UI u, int num_stage, double difficulty, Stage currentStage) {
		Main_Bgm.stop();
		One_Bgm.stop();
		Two_Bgm.stop();
		Three_Bgm.stop();
		fires.add(new Image("file:images/fire1.png"));
		fires.add(new Image("file:images/fire2.png"));
		fires.add(new Image("file:images/fire5.png"));
		fires.add(new Image("file:images/fire3.png"));
		fires.add(new Image("file:images/fire4.png"));
		textString = Other.getText(num_stage);
		diff = difficulty;
		System.out.println("Current stage: " + num_stage);
		n_stage = num_stage;
		bp = b;

		// initialization
		this.setWidth(width);
		this.setHeight(height);
		switch (n_stage) {
			case 1:
				One_Bgm.play();
				st = new StageOne(2, difficulty);
				break;
			case 2:
				Two_Bgm.play();
				st = new StageTwo(2, difficulty);
				break;
			case 3:
				Three_Bgm.play();
				st = new StageThree(2, difficulty);
				break;
		}
		role = st.getCharacter();
		ui = u;
		ui.setRole(role);
		ml = st.getMonsters();
		wp = role.getWeapon();
		bp.getChildren().add(st.getRoom().getCanvas());
		wp.setImView(ml, role, bp);
		for (Monster m : ml) {
			m.setImView(bp, role);
		}
		gc = this.getGraphicsContext2D();
		this.setFocusTraversable(true);
		setKey();
		timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> {
			drawAll();

		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		stage = currentStage;
		text = 0;
		ca_text = new Canvas();
		ca_text.setWidth(1280);
		ca_text.setHeight(720);
		ca_text.setLayoutX(0);
		ca_text.setLayoutY(0);
		gc_text = ca_text.getGraphicsContext2D();
		gc_text.setFont(Font.font("Cooper Black", FontWeight.BOLD, 20));
		bp.getChildren().add(ca_text);
		drawPauseButton();
		
		doors.add(new ImageView(new Image("file:images/ui/door/Dungeon.png")));
		doors.add(new ImageView(new Image("file:images/ui/door/Dungeon.png")));
		doors.add(new ImageView(new Image("file:images/ui/door/Dungeon.png")));
		doors.add(new ImageView(new Image("file:images/ui/door/Dungeon.png")));
		for (int i = 0; i < 4; i++) {
			doors.get(i).setViewport(new Rectangle2D(0, 0, 1, 1));
			bp.getChildren().add(doors.get(i));
			doors.get(i).setFitWidth(75);
			doors.get(i).setFitHeight(75);
		}
		doors.get(0).setX(up_x); doors.get(0).setY(up_y);
		doors.get(1).setX(right_x); doors.get(1).setY(right_y);
		doors.get(2).setX(down_x); doors.get(2).setY(down_y);
		doors.get(3).setX(left_x); doors.get(3).setY(left_y);
	}

	private void setKey() {
		this.setOnKeyPressed(e -> {
			switch (e.getCode()) {
				case A:
					left = true;
					break;
				case D:
					right = true;
					break;
				case W:
					up = true;
					break;
				case S:
					down = true;
					break;
				case C:
					if (!wp.readyToChange())
						return;
					wp.remove();
					wp = new Sword();
					wp.setImView(ml, role, bp);
					role.take(wp);
					break;
				case V:
					if (!wp.readyToChange())
						return;
					wp.remove();
					wp = new Bow();
					wp.setImView(ml, role, bp);
					role.take(wp);
					break;
				default:
			}
		});
		this.setOnKeyReleased(e -> {
			switch (e.getCode()) {
				case A:
					left = false;
					break;
				case D:
					right = false;
					break;
				case W:
					up = false;
					break;
				case S:
					down = false;
					break;
				default:
			}
		});
		bp.setOnMouseMoved(e -> {
			mouseX = e.getX();
			mouseY = e.getY();
		});
		bp.setOnMouseClicked(e -> {
			if (dead) {
				if (mouseX > 349 && mouseX < 489 && mouseY > 478 && mouseY < 502) {
					Music_MouseMove.play();
					bp.getChildren().clear();
					UI ui = new UI();
					GameView gv = new GameView(bp, ui, n_stage, diff, stage);
					bp.getChildren().add(gv);
					bp.getChildren().add(ui);
				} else if (mouseX > 750 && mouseX < 915 && mouseY > 478 && mouseY < 502) {
					Music_MouseMove.play();
					bp.getChildren().clear();
					File f = new File("bg.mp4");
					Media m = new Media(f.toURI().toString());
					MediaPlayer mp = new MediaPlayer(m);
					MediaView mv = new MediaView(mp);
					mv.setFitWidth(1280);
					mv.setFitHeight(720);
					mp.play();
					mp.setCycleCount(Integer.MAX_VALUE);
					bp.getChildren().add(mv);
					bp.getChildren().add(new CoverView(bp, stage));
				}
				return;
			} else if (bossDead) {
				if (mouseX > 349 && mouseX < 489 && mouseY > 478 && mouseY < 502) {
					Music_MouseMove.play();
					bp.getChildren().clear();
					UI ui = new UI();
					GameView gv = new GameView(bp, ui, n_stage + 1, diff, stage);
					bp.getChildren().add(gv);
					bp.getChildren().add(ui);
				} else if (mouseX > 750 && mouseX < 915 && mouseY > 478 && mouseY < 502) {
					Music_MouseMove.play();
					bp.getChildren().clear();
					File f = new File("bg.mp4");
					Media m = new Media(f.toURI().toString());
					MediaPlayer mp = new MediaPlayer(m);
					MediaView mv = new MediaView(mp);
					mv.setFitWidth(1280);
					mv.setFitHeight(720);
					mp.play();
					mp.setCycleCount(Integer.MAX_VALUE);
					bp.getChildren().add(mv);
					bp.getChildren().add(new CoverView(bp, stage));
				}
			}
			if (mouseX > 1219 && mouseX < 1260 && mouseY > 19 && mouseY < 60) {
				// Create the pause menu
				Pane pauseMenu = new Pane();
				pauseMenu.setVisible(false);

				// Create the blackout rectangle
				Rectangle blackout = new Rectangle(1280, 720);
				blackout.setFill(Color.BLACK);
				blackout.setOpacity(0.5);

				// Add the blackout to the pause menu
				pauseMenu.getChildren().add(blackout);

				// Create three buttons and add them to the pause menu
				Button resumeButton = new Button("Resume");
				Button saveGameButton = new Button("Save Game");
				Button backToMenuButton = new Button("Back to Menu");

				resumeButton.setStyle("-fx-font: 24 arial; -fx-base: #b6e7c9;");
				saveGameButton.setStyle("-fx-font: 24 arial; -fx-base: #b6e7c9;");
				backToMenuButton.setStyle("-fx-font: 24 arial; -fx-base: #b6e7c9;");

				resumeButton.setVisible(false);
				saveGameButton.setVisible(false);
				backToMenuButton.setVisible(false);

				// Add event handlers for the buttons
				resumeButton.setOnAction(event -> {
					timeline.play();
					bp.getChildren().remove(bp.getChildren().size() - 1);
					pauseMenu.setVisible(false);
					resumeButton.setVisible(false);
					saveGameButton.setVisible(false);
					backToMenuButton.setVisible(false);
				});

				saveGameButton.setOnAction(event -> {
					// Add your save game logic here
					UserData retrievedData = (UserData) stage.getUserData();
					String uid = retrievedData.getUid();
					String name = retrievedData.getName();
					String pictureUrl = retrievedData.getPictureUrl();
					String email = retrievedData.getEmail();
					double difficulty = retrievedData.getUserDifficulty();
					String timestampString = retrievedData.getTimestamp();

					Map<String, Object> data = new HashMap<>();
					data.put("name", name);
					data.put("pictureUrl", pictureUrl);
					data.put("email", email);
					data.put("stage", n_stage);
					data.put("difficulty", difficulty);
					FirebaseInitializer.addDocument(uid, timestampString, data);
					retrievedData.setUserStage(n_stage);
				});

				backToMenuButton.setOnAction(event -> {
					// Back to Menu
					System.out.println("Back to menu clicked");
					Music_MouseMove.play();
					bp.getChildren().clear();
					File f = new File("bg.mp4");
					Media m = new Media(f.toURI().toString());
					MediaPlayer mp = new MediaPlayer(m);
					MediaView mv = new MediaView(mp);
					mv.setFitWidth(1280);
					mv.setFitHeight(720);
					mp.play();
					mp.setCycleCount(Integer.MAX_VALUE);
					bp.getChildren().add(mv);
					bp.getChildren().add(new CoverView(bp, stage));
				});

				VBox buttonBox = new VBox(15);
				buttonBox.getChildren().addAll(resumeButton, saveGameButton, backToMenuButton);
				buttonBox.setAlignment(Pos.CENTER);
				buttonBox.setLayoutX(545 - buttonBox.getWidth() / 2);
				buttonBox.setLayoutY(265 - buttonBox.getHeight() / 2);
				pauseMenu.getChildren().add(buttonBox);

				// // Set the blackout's onMouseClicked event handler
				// blackout.setOnMouseClicked(event -> {
				// timeline.play();
				// pauseMenu.setVisible(false);
				// resumeButton.setVisible(false);
				// saveGameButton.setVisible(false);
				// backToMenuButton.setVisible(false);
				// });

				// Add the pause menu to the main pane
				bp.getChildren().add(pauseMenu);

				if (timeline.getStatus() == Animation.Status.RUNNING) {
					timeline.pause();
					pauseMenu.setVisible(true);
					resumeButton.setVisible(true);
					saveGameButton.setVisible(true);
					backToMenuButton.setVisible(true);
				} else {
					timeline.play();
					pauseMenu.setVisible(false);
					resumeButton.setVisible(false);
					saveGameButton.setVisible(false);
					backToMenuButton.setVisible(false);
				}
			}
			if (text == textString.length() * 2) {
				bp.getChildren().remove(ca_text);
				text = -1;
			} else if (text > 0 && text != textString.length() * 2) {
				text = textString.length() * 2;
			}
			// when current weapon type is MeleeWeapon
			wp.tryAttack(deg, mouseX, mouseY);
		});
	}

	private void drawAll() {
		System.out.println("size: " + bp.getChildren().size());
		if (dead) {
			gc_text.setFill(Color.BLACK);
			gc_text.fillRect(0, 0, 1280, 720);
			gc_text.setFill(Color.WHITE);
			gc_text.setFont(Font.font("Cooper Black", FontWeight.BOLD, 120));
			gc_text.fillText("You Are Dead", 300, 300);
			gc_text.setFont(Font.font("Cooper Black", FontWeight.BOLD, 30));
			gc_text.fillText("Start Again", 350, 500);
			gc_text.fillText("Back to Main", 750, 500);
			tic++;
			if (mouseX > 349 && mouseX < 489 && mouseY > 478 && mouseY < 502) {
				gc_text.drawImage(fires.get(tic / 8 % 5), 340, 460, 170, 60);
			} else if (mouseX > 750 && mouseX < 915 && mouseY > 478 && mouseY < 502) {
				gc_text.drawImage(fires.get(tic / 8 % 5), 740, 460, 190, 60);
			}
			return;
		} else if (bossDead) {
			gc_text.setFill(Color.BLACK);
			gc_text.fillRect(0, 0, 1280, 720);
			gc_text.setFill(Color.WHITE);
			gc_text.setFont(Font.font("Cooper Black", FontWeight.BOLD, 90));
			gc_text.fillText(st.getBossName() + " Defeated!", 200, 300);
			gc_text.setFont(Font.font("Cooper Black", FontWeight.BOLD, 30));
			gc_text.fillText("Next Stage", 350, 500);
			gc_text.fillText("Back to Main", 750, 500);
			tic++;
			if (mouseX > 349 && mouseX < 489 && mouseY > 478 && mouseY < 502) {
				gc_text.drawImage(fires.get(tic / 8 % 5), 340, 460, 170, 60);
			} else if (mouseX > 750 && mouseX < 915 && mouseY > 478 && mouseY < 502) {
				gc_text.drawImage(fires.get(tic / 8 % 5), 740, 460, 190, 60);
			}
			return;
		}
		if (text >= 0) {
			gc_text.clearRect(0, 0, 1280, 720);
			gc_text.setFill(Color.BLACK);
			gc_text.fillRect(0, 0, 1280, 720);
			gc_text.setFill(Color.WHITE);
			gc_text.setFont(null);
			gc_text.fillText(textString.substring(0, text / 2), 300, 200);
			text += text == textString.length() * 2 ? 0 : 1;
			return;
		}
		deg = Math.atan2(mouseY - role.getY(0.5), mouseX - role.getX(0.5)) + Math.PI;
		double d = (deg + Math.PI / 4) % (Math.PI * 2) / (Math.PI / 2);
		switch ((int) d) {
			case 0:
				direction = 1;
				break;
			case 1:
				direction = 3;
				break;
			case 2:
				direction = 2;
				break;
			case 3:
				direction = 0;
				break;
		}
		role.change(left || right || up || down ? 1 : 0);
		gc.clearRect(0, 0, width, height);
		drawGate();
		updateCharacter();
		updateMonster();
		drawCharacter();
		ui.drawHealth();
		ui.drawMonsterHealth(ml);

		// if character dead
		if (role.getHealth() <= 0) {
			dead = true;
			bp.getChildren().add(ca_text);
			return;
		}
		if (st.isBossDead()) {
			bossDead = true;
			bp.getChildren().add(ca_text);
			return;
		}

	}

	private void drawCharacter() {
		gc.setFill(Color.GOLD);
		phase = (phase + 1) % (role.getPhase() * 6);
		gc.drawImage(role.getImage(), phase / 6 * 64, direction * 128, 64, 128, role.getX(0), role.getY(0),
				role.getWidth(), role.getHeight());
		wp.update(mouseX, mouseY, deg);
	}

	// update position of character based on keyboard control
	private void updateCharacter() {
		if (left && up) {
			role.changeX(role.getX(1) > role.getWidth() ? -0.7 : 0);
			role.changeY(role.getY(1.25) > role.getHeight() ? -0.7 : 0);
		} else if (left && down) {
			role.changeX(role.getX(1) > role.getWidth() ? -0.7 : 0);
			role.changeY(role.getY(0) < height - role.getHeight() * 1.5 ? 0.7 : 0);
		} else if (right && up) {
			role.changeX(role.getX(0) < width - role.getWidth() ? 0.7 : 0);
			role.changeY(role.getY(1.25) > role.getHeight() ? -0.7 : 0);
		} else if (right && down) {
			role.changeX(role.getX(0) < width - role.getWidth() ? 0.7 : 0);
			role.changeY(role.getY(0) < height - role.getHeight() * 1.5 ? 0.7 : 0);
		} else if (left) {
			role.changeX(role.getX(1) > role.getWidth() ? -1 : 0);
		} else if (up) {
			role.changeY(role.getY(1.25) > role.getHeight() ? -1 : 0);
		} else if (right) {
			role.changeX(role.getX(0) < width - role.getWidth() ? 1 : 0);
		} else if (down) {
			role.changeY(role.getY(0) < height - role.getHeight() * 1.25 ? 1 : 0);
		}
		if (st.roomClean()) {
			if (st.room(0) != 0 && Other.dist(role.getX(0.5), role.getY(0.5), up_x + 25, up_y + 25) < 30) {
				role.setX(600);
				role.setY(580);
				st.nextRoom(0);
				bp.getChildren().set(0, st.getRoom().getCanvas());
			} else if (st.room(1) != 0 && Other.dist(role.getX(0.5), role.getY(0.5), 1250, 325) < 30) {
				role.setX(100);
				role.setY(300);
				st.nextRoom(1);
				bp.getChildren().set(0, st.getRoom().getCanvas());
			} else if (st.room(2) != 0 && Other.dist(role.getX(0.5), role.getY(0.5), 625, 665) < 30) {
				role.setX(600);
				role.setY(100);
				st.nextRoom(2);
				bp.getChildren().set(0, st.getRoom().getCanvas());
			} else if (st.room(3) != 0 && Other.dist(role.getX(0.5), role.getY(0.5), 25, 325) < 30) {
				role.setX(1175);
				role.setY(300);
				st.nextRoom(3);
				bp.getChildren().set(0, st.getRoom().getCanvas());
			}
			ml = st.getMonsters();
			wp.updateMonster(ml);
			for (Monster m : ml) {
				m.setImView(bp, role);
			}
		}

	}

	private void updateMonster() {
		for (int i = 0; i < ml.size(); i++) {
			Monster m = ml.get(i);
			m.animation();
			if (m.isDead()) {
				m.remove();
			}
			if (m.allDone()) {
				ml.remove(i--);
			}
		}
	}

	private void drawGate() {
		if (st.roomClean()) {
			for (int i = 0; i < 4; i++) {
				doors.get(i).setViewport(new Rectangle2D(8, 516, 50,50));
			}
		}
		else {
			for (int i = 0; i < 4; i++) {
				doors.get(i).setViewport(new Rectangle2D(8, 452, 50,50));
			}
		}
		
		if (st.room(0) != 0) doors.get(0).setImage(new Image("file:images/ui/door/Dungeon.png"));
		else doors.get(0).setImage(null);
		
		if (st.room(1) != 0) doors.get(1).setImage(new Image("file:images/ui/door/Dungeon.png"));
		else doors.get(1).setImage(null);
		
		if (st.room(2) != 0) doors.get(2).setImage(new Image("file:images/ui/door/Dungeon.png"));
		else doors.get(2).setImage(null);
		
		if (st.room(3) != 0) doors.get(3).setImage(new Image("file:images/ui/door/Dungeon.png"));
		else doors.get(3).setImage(null);
	}

	private void drawPauseButton() {
		double canvasWidth = width;

		double buttonSize = 40;
		double padding = 20;

		double x = canvasWidth - buttonSize - padding;
		double y = padding;

		Circle button = new Circle(x + buttonSize / 2, y + buttonSize / 2, buttonSize / 2);
		button.setStyle("-fx-fill: #b6e7c9; -fx-stroke: #666; -fx-stroke-width: 2;");

		button.setOnMouseEntered(e -> button.setStyle("-fx-fill: #99d6bc; -fx-stroke: #666; -fx-stroke-width: 2;"));
		button.setOnMouseExited(e -> button.setStyle("-fx-fill: #b6e7c9; -fx-stroke: #666; -fx-stroke-width: 2;"));

		gc.setStroke(Color.BLACK);
		gc.setLineWidth(3);

		gc.strokeLine(x + 10, y + 20, x + 30, y + 20);
		gc.strokeLine(x + 20, y + 10, x + 20, y + 30);

		Text pauseText = new Text(x + 4, y + 24, "Stop");
		pauseText.setFont(Font.font("Arial", FontWeight.BOLD, 15));
		pauseText.setFill(Color.BLACK);

		Pane pane = new Pane();
		pane.getChildren().addAll(button, pauseText);
		bp.getChildren().add(pane);
	}

}
