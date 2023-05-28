package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import other.Music_MouseMove;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.google.cloud.firestore.DocumentSnapshot;

import auth.FirebaseInitializer;
import auth.UserData;

public class LoadGame extends StackPane {
    private BorderPane pane;
    private Stage stage;

    public LoadGame(BorderPane p, Stage currentStage) {
        File file = new File("bg.mp4");
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(1280);
        mediaView.setFitHeight(720);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        UserData retrievedData = (UserData) currentStage.getUserData();
        String uid = retrievedData.getUid();

        List<DocumentSnapshot> documents = FirebaseInitializer.readDocumentsByCollectionName(uid);

        if (documents.isEmpty()) {
            Label noRecordLabel = new Label("No record!");
            noRecordLabel.setTextFill(Color.WHITE);
            noRecordLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
            noRecordLabel.setAlignment(Pos.CENTER);
            vbox.getChildren().add(noRecordLabel);
        } else {
            // Iterate through the documents and create labels using the data
            for (DocumentSnapshot document : documents) {
                Long timestamp = Long.parseLong(document.getId());
                Instant instant = Instant.ofEpochMilli(timestamp);
                LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                String formattedDate = localDateTime.format(formatter);
                double difficulty = document.getDouble("difficulty"); // Replace "difficulty"
                String difficultyString = getDifficultyString(difficulty);
                Long stage = document.getLong("stage"); // Replace "stage" with the field
                Label label = new Label("Time: " + formattedDate + " / Difficulty: " + difficultyString + " / Stage: "
                        + stage.toString());
                label.setTextFill(Color.WHITE);
                label.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
                label.setOnMouseClicked(this::handleItemClick);
                // Add mouse hover effect
                label.setOnMouseEntered(event -> label.setTextFill(Color.ORANGE)); // Set hover color to orange
                label.setOnMouseExited(event -> label.setTextFill(Color.WHITE)); // Set text color back to white
                vbox.getChildren().add(label);
            }
        }

        // Wrap the VBox in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(30, 30);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // Make ScrollPane transparent
        scrollPane
                .setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // Set the position of the ScrollPane
        StackPane.setAlignment(scrollPane, Pos.CENTER_LEFT);
        StackPane.setMargin(scrollPane, new Insets(200, 0, 0, 350));

        // Add a clickable text at the top of the screen
        Label TopText = new Label("Record");
        TopText.setTextFill(Color.WHITE);
        TopText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 30));
        StackPane.setAlignment(TopText, Pos.TOP_CENTER);
        StackPane.setMargin(TopText, new Insets(100, 0, 0, 0));

        // Add a clickable text at the bottom of the screen
        Label bottomText = new Label("GO BACK");
        bottomText.setTextFill(Color.WHITE);
        bottomText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 25));
        bottomText.setOnMouseClicked(this::handleGoBackClick);
        StackPane.setAlignment(bottomText, Pos.BOTTOM_CENTER);
        StackPane.setMargin(bottomText, new Insets(0, 0, 50, 0));

        getChildren().addAll(mediaView, scrollPane, TopText, bottomText);
        setPrefSize(1280, 720);

        pane = p;
        stage = currentStage;
    }

    private void handleItemClick(MouseEvent event) {
        Label label = (Label) event.getSource();
        System.out.println("Clicked: " + label.getText());
        Music_MouseMove.play();
        pane.getChildren().clear();
        UI ui = new UI();
        // Extract difficulty and stage from the label text
        String[] parts = label.getText().split(" / ");
        String difficultyString = parts[1].substring(parts[1].indexOf(" ") + 1);
        int numberStage = Integer.parseInt(parts[2].substring(parts[2].indexOf(" ") + 1));

        // Convert difficulty string to a double value
        double difficulty = 0.75; // Default value
        if ("Easy".equals(difficultyString)) {
            difficulty = 0.75;
        } else if ("Medium".equals(difficultyString)) {
            difficulty = 1.25;
        } else if ("Hard".equals(difficultyString)) {
            difficulty = 2;
        }

        GameView gv = new GameView(pane, ui, numberStage, difficulty, stage);
        pane.getChildren().add(gv);
        pane.getChildren().add(ui);
    }

    private void handleGoBackClick(MouseEvent event) {
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

    private String getDifficultyString(double difficulty) {
        if (difficulty == 0.75) {
            return "Easy";
        } else if (difficulty == 1.25) {
            return "Medium";
        } else if (difficulty == 2) {
            return "Hard";
        } else {
            return "Unknown";
        }
    }

}
