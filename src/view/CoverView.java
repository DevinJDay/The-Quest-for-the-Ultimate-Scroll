package view;

import java.io.File;
import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import other.Main_Bgm;
import other.Music_MouseMove;
import other.One_Bgm;
import other.Three_Bgm;
import other.Two_Bgm;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import auth.UserData;
import javafx.geometry.Rectangle2D;

public class CoverView extends Canvas {
  private int option;
  private GraphicsContext gc = this.getGraphicsContext2D();
  private Image title = new Image("file:images/title.png");
  private ArrayList<Image> fires = new ArrayList<>();
  private BorderPane pane;
  private int x;
  private int y;
  private int tic = 0;
  private boolean insOpen;
  private Timeline timeline = new Timeline();
  private int page = 1;
  private int page2;
  private Stage stage;

  public CoverView(BorderPane b, Stage currentStage) {
	One_Bgm.stop();
	Two_Bgm.stop();
	Three_Bgm.stop();
	Main_Bgm.play();
    this.setWidth(1280);
    this.setHeight(720);
    gc = this.getGraphicsContext2D();
    fires.add(new Image("file:images/fire1.png"));
    fires.add(new Image("file:images/fire2.png"));
    fires.add(new Image("file:images/fire5.png"));
    fires.add(new Image("file:images/fire3.png"));
    fires.add(new Image("file:images/fire4.png"));
    pane = b;
    MouseHandler mh = new MouseHandler();
    this.setOnMouseClicked(mh);
    this.setOnMouseMoved(new DragHandler());
    timeline = new Timeline(new KeyFrame(Duration.millis(50), new AnimateStarter()));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
    stage = currentStage;
    drawUserNameAndIcon();
  }

  public void setOption() {
    timeline.stop();
    switch (option) {
      case 1:
        Music_MouseMove.play();
        pane.getChildren().clear();
        File file = new File("bg.mp4");
        Media m = new Media(file.toURI().toString());
        MediaPlayer mp = new MediaPlayer(m);
        MediaView mv = new MediaView(mp);
        mv.setFitWidth(1280);
        mv.setFitHeight(720);
        mp.play();
        mp.setCycleCount(Integer.MAX_VALUE);
        pane.getChildren().add(mv);
        pane.getChildren().add(new DifficultyView(pane, fires, stage));
        break;

      case 2:

        break;
      default:
    }
  }

  private class AnimateStarter implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent event) {
      tic++;
      gc.clearRect(0, 0, 1280, 720);
      gc.drawImage(title, 168, 0, 1050, 400, 200, 80, 700, 230);
      if (x > 545 && x < 680 && y > 434 && y < 452) {
        gc.drawImage(fires.get(tic / 3 % 5), 19, 150, 548, 280, 530, 428, 168, 30);
      } else if (x > 550 && x < 670 && y > 584 && y < 602) {
        gc.drawImage(fires.get(tic / 3 % 5), 19, 150, 548, 280, 540, 578, 150, 30);
      } else if (x > 535 && x < 670 && y > 485 && y < 501) {
        gc.drawImage(fires.get(tic / 3 % 5), 19, 150, 548, 280, 520, 478, 168, 30);
      } else if (x > 545 && x < 710 && y > 530 && y < 551) {
        gc.drawImage(fires.get(tic / 3 % 5), 19, 150, 548, 280, 510, 530, 200, 30);
      }
      Font fontSmall = Font.font("Comic Sans MS", FontWeight.BOLD, 15);
      gc.setFill(Color.WHITE);
      gc.setFont(fontSmall);
      gc.fillText("Video Copyright: Ori and the Blind Forest, Moon Studios.\nUsed for this project only.", 5, 720);
      Font fontLarge = Font.font("Comic Sans MS", FontWeight.BOLD, 20);
      gc.setFont(fontLarge);
      gc.setFill(Color.WHITE);
      gc.fillText("NEW GAME", 555, 450);
      gc.fillText("LOAD GAME", 550, 500);
      gc.fillText("INSTRUCTIONS", 535, 550);
      gc.fillText("EXIT GAME", 555, 600);
      if (tic == 20)
        tic = 0;

    }
  }

  private class MouseHandler implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent e) {
    	System.out.println("Clicked");
      double x = e.getX();
      double y = e.getY();
      if (x > 540 && x < 680 && y > 434 && y < 452) {
        option = 1;
        insOpen = false;
      } else if (x > 554 && x < 670 && y > 584 && y < 600) {
        // Exit Game
        Music_MouseMove.play();
        Platform.exit();
        insOpen = false;
      } else if (x > 546 && x < 670 && y > 490 && y < 501) {
        // Load Game
        Music_MouseMove.play();
        LoadGame loadGame = new LoadGame(pane, stage);
        pane.setCenter(loadGame);

      } else if (insOpen && page == 1 && (x > 875 && x < 933 && y > 617 && y < 633)) {
        page = 2;
        insOpen = true;
      } else if (x > 540 && x < 700 && y > 530 && y < 551) {
      	System.out.println("Ins");
        Music_MouseMove.play();
        // Instruction
        page2 = 1;
        ImageView im = new ImageView(new Image("file:images/instruction/ins1.png"));
        im.setViewport(new Rectangle2D(0, 0, 1920, 1080));
        im.setX(0);
        im.setY(0);
        im.setFitWidth(1280);
        im.setFitHeight(720);
        pane.getChildren().add(im);
        im.setOnMouseClicked(b -> {
          page2++;
          im.setImage(new Image("file:images/instruction/ins2.png"));
          if (page2 >= 3) {
            pane.getChildren().remove(im);
          }
        });

        insOpen = true;
        page = 1;
      } else {
        insOpen = false;
      }
      if (option != 0) {
        setOption();
      }
    }
  }

  private class DragHandler implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent event) {
      x = (int) event.getX();
      y = (int) event.getY();
    }
  }

  private void drawUserNameAndIcon() {
    UserData retrievedData = (UserData) stage.getUserData();
    String pictureUrl = retrievedData.getPictureUrl();
    String name = retrievedData.getName();
    double iconSize = 60;
    double padding = 20;

    // Draw user icon
    Image userIcon = new Image(pictureUrl,
        iconSize, iconSize, true, true);
    ImageView userIconView = new ImageView(userIcon);
    Circle userIconClip = new Circle(iconSize / 2, iconSize / 2, iconSize / 2);
    userIconView.setClip(userIconClip);
    userIconView.setX(padding);
    userIconView.setY(padding);

    // Create a Circle with the same size as the ImageView
    Circle userIconCircle = new Circle(iconSize / 2, iconSize / 2, iconSize / 2);
    userIconCircle.setFill(new ImagePattern(userIcon));

    // Position the Circle
    userIconCircle.setLayoutX(padding);
    userIconCircle.setLayoutY(padding);

    // Draw user name
    Text userNameText = new Text(name);
    userNameText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
    userNameText.setFill(Color.WHITE);
    userNameText.setTextAlignment(TextAlignment.LEFT);
    userNameText.setX(padding + iconSize + 10);
    userNameText.setY(padding + iconSize / 2 + userNameText.getFont().getSize() / 2);

    // Add user name and icon to a Pane
    Pane userInfoPane = new Pane();
    userInfoPane.getChildren().addAll(userIconCircle, userNameText);

    // Add the Pane to the BorderPane
    pane.getChildren().add(userInfoPane);
  }

}
