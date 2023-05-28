package view;

import java.io.File;
import java.util.ArrayList;

import auth.UserData;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import javafx.stage.Stage;
import other.Music_MouseMove;

public class DifficultyView extends Canvas {
  private double diff;
  private GraphicsContext gc = this.getGraphicsContext2D();
  private BorderPane pane;
  private int x;
  private int y;
  private int tic;
  private ArrayList<Image> fires = new ArrayList<>();
  private Timeline timeline = new Timeline();
  private Stage stage;

  public DifficultyView(BorderPane p, ArrayList<Image> f, Stage currentStage) {
    fires = f;
    this.setWidth(1280);
    this.setHeight(720);
    MouseHandler mh = new MouseHandler();
    this.setOnMouseClicked(mh);
    pane = p;
    this.setOnKeyPressed(getOnKeyPressed());
    this.setOnMouseMoved(new DragHandler());
    timeline = new Timeline(new KeyFrame(Duration.millis(50), new AnimateStarter()));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
    stage = currentStage;
  }

  public void setOption() {
    Music_MouseMove.play();
    pane.getChildren().clear();
    UI ui = new UI();
    GameView gv = new GameView(pane, ui, 1, diff, stage);
    pane.getChildren().add(gv);
    pane.getChildren().add(ui);
  }

  private void setUserDifficulty() {
    UserData retrievedData = (UserData) stage.getUserData();
    retrievedData.setUserDifficulty(diff);
  }

  private class AnimateStarter implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent event) {
      tic++;
      gc.clearRect(0, 0, 1050, 750);
      if (x > 550 && x < 701 && y > 434 && y < 450) {
        gc.drawImage(fires.get(tic / 3 % 5), 19, 150, 550, 280, 520, 429, 210, 30);
      } else if (x > 480 && x < 784 && y > 483 && y < 500) {
        gc.drawImage(fires.get(tic / 3 % 5), 19, 150, 550, 270, 440, 478, 390, 30);
      } else if (x > 530 && x < 727 && y > 533 && y < 549) {
        gc.drawImage(fires.get(tic / 3 % 5), 19, 150, 550, 280, 500, 528, 240, 30);
      } else if (x > 0 && x < 102 && y > 0 && y < 31) {
        gc.drawImage(fires.get(tic / 3 % 5), 19, 150, 550, 280, -15, -5, 140, 50);
      }
      Font fontLarger = Font.font("Comic Sans MS", FontWeight.BOLD, 40);
      gc.setFont(fontLarger);
      gc.setFill(Color.BLACK);
      gc.fillText("Select your Difficulty", 410, 350);
      gc.setFill(Color.WHITE);
      gc.fillText("Back", 10, 30);
      Font fontLarge = Font.font("Comic Sans MS", FontWeight.BOLD, 20);
      gc.setFont(fontLarge);
      gc.fillText("Give Me a Ease", 550, 450);
      gc.fillText("Give Me a Balanced Experience", 480, 500);
      gc.fillText("Give Me a Challenge", 530, 550);
      if (tic == 20) {
        tic = 0;
      }
    }
  }

  private class MouseHandler implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent e) {
      double x = e.getX();
      double y = e.getY();
      if (x > 550 && x < 701 && y > 434 && y < 450) {
        diff = 0.75;
      } else if (x > 480 && x < 784 && y > 483 && y < 500) {
        diff = 1.25;
      } else if (x > 530 && x < 727 && y > 533 && y < 549) {
        diff = 2;
      } else if (x > 0 && x < 102 && y > 0 && y < 31) {
        Music_MouseMove.play();
        pane.getChildren().clear();
        File f = new File("bg.mp4");
        Media m = new Media(f.toURI().toString());
        MediaPlayer mp = new MediaPlayer(m);
        MediaView mv = new MediaView(mp);
        mv.setFitWidth(1280);
        mv.setFitHeight(720);
        mp.play();
        mp.setCycleCount(Integer.MAX_VALUE);
        pane.getChildren().add(mv);
        pane.getChildren().add(new CoverView(pane, stage));
      }
      if (diff != 0) {
        setUserDifficulty();
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
}
