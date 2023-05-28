package auth;

import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Instant;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import view.CoverView;

public class SignInController {
	private static final int width = 1280;
	private static final int height = 720;
	private static final String CLIENT_ID = "123";
	private static final String CLIENT_SECRET = "456";
	private static final String REDIRECT_URI = "http://127.0.0.1:8000/callback";

	public static void initialize(BorderPane pane, Object referenceToInstance, Stage stage) {
		String hoverImage = "file:images/auth/google_signin_hover.png";
		String normalImage = "file:images/auth/google_signin.png";

		Image googleSignInImage = new Image(normalImage);
		ImageView googleSignInButton = new ImageView(googleSignInImage);
		googleSignInButton.setFitWidth(200);
		googleSignInButton.setFitHeight(50);
		googleSignInButton.setLayoutX(545);
		googleSignInButton.setLayoutY(450);

		if (pane != null) {
			pane.getChildren().add(googleSignInButton);
		}
		// Add a click event listener for the Google Sign-In button
		googleSignInButton.setOnMouseClicked(event -> signInWithGoogle(referenceToInstance, stage));
		googleSignInButton.setOnMouseEntered(e -> {
			googleSignInButton.setImage(new Image(hoverImage));
		});
		googleSignInButton.setOnMouseExited(e -> {
			googleSignInButton.setImage(new Image(normalImage));
		});
	}

	private static void signInWithGoogle(Object referenceToInstance, Stage stage) {
		try {
			FXMLLoader loader = new FXMLLoader(referenceToInstance.getClass().getResource("/auth/web_view.fxml"));
			Parent root = loader.load();
			WebViewController webViewController = loader.getController();

			String authUrl = String.format(
					"https://accounts.google.com/o/oauth2/auth?client_id=%s&response_type=code&scope=openid+email+profile&redirect_uri=%s",
					CLIENT_ID, REDIRECT_URI);

			webViewController.loadUrl(authUrl);
			stage.setTitle("Google Auth");
			stage.setResizable(true);
			stage.setScene(new Scene(root));
			stage.show();
			webViewController.setRedirectHandler((oldUrl, newUrl) -> {
				// Check if the URL contains the redirect URI
				if (newUrl.startsWith(REDIRECT_URI)) {
					String authCode = extractAuthCode(newUrl);
					String decodedAuthCode = URLDecoder.decode(authCode, StandardCharsets.UTF_8);
					System.out.println(decodedAuthCode);
					try {
						GoogleTokenResponse tokenResponse = SignInController.exchangeAuthCodeForIdToken(CLIENT_ID,
								CLIENT_SECRET, decodedAuthCode, REDIRECT_URI);
						String googleIdToken = tokenResponse.getIdToken();
						SignInController.exchangeGoogleIdTokenForFirebaseIdToken(googleIdToken, stage);
						SignInController.createMainPage(stage);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (FirebaseAuthException e) {
						e.printStackTrace();
					} catch (GeneralSecurityException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String extractAuthCode(String url) {
		String[] queryParams = url.split("\\?")[1].split("&");
		for (String queryParam : queryParams) {
			String[] keyValuePair = queryParam.split("=");
			if (keyValuePair[0].equals("code")) {
				return keyValuePair[1];
			}
		}
		return null;
	}

	public static void exchangeGoogleIdTokenForFirebaseIdToken(String googleIdToken, Stage stage)
			throws IOException, GeneralSecurityException, FirebaseAuthException {
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JSON_FACTORY)
				.setAudience(Collections.singletonList(CLIENT_ID))
				.build();
		GoogleIdToken idToken = verifier.verify(googleIdToken);
		System.out.println("verifier: " + idToken);
		if (idToken != null) {
			String uid = idToken.getPayload().getSubject(); // Extract the uid from the GoogleIdToken object
			if (uid.length() > 128) {
				uid = uid.substring(0, 128);
			}
			Payload payload = idToken.getPayload();
			String name = (String) payload.get("name");
			String pictureUrl = (String) payload.get("picture");
			String email = payload.getEmail();
			long timestampMillis = Instant.now().toEpochMilli();
			String timestampString = Long.toString(timestampMillis);
			UserData userData = new UserData(uid, name, pictureUrl, email, timestampString);
			stage.setUserData(userData);
			System.out.println("Success retrive and set user data.");
		} else {
			throw new IllegalArgumentException("Invalid Google ID token.");
		}
	}

	public static String createCustomToken(String uid) throws FirebaseAuthException {
		FirebaseAuth auth = FirebaseAuth.getInstance();
		String customToken = auth.createCustomToken(uid);
		return customToken;
	}

	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

	public static GoogleTokenResponse exchangeAuthCodeForIdToken(String clientId, String clientSecret, String authCode,
			String redirectUri) throws IOException {
		GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
				new NetHttpTransport(),
				JSON_FACTORY,
				clientId,
				clientSecret,
				authCode,
				redirectUri).execute();

		return tokenResponse;
	}

	public static void createMainPage(Stage stage) {
		File f = new File("bg.mp4");
		Media m = new Media(f.toURI().toString());
		MediaPlayer mp = new MediaPlayer(m);
		MediaView mv = new MediaView(mp);
		mv.setFitWidth(1280);
		mv.setFitHeight(720);
		mp.play();
		mp.setCycleCount(Integer.MAX_VALUE);
		stage.setHeight(height);
		stage.setWidth(width);
		BorderPane pane = new BorderPane();
		Scene scene = new Scene(pane, stage.getWidth(), stage.getHeight());
		if (pane != null) {
			pane.getChildren().add(mv);
			pane.getChildren().add(new CoverView(pane, stage));
		}

		stage.setTitle("The Quest for the Ultimate Scroll");
		stage.setResizable(true);
		stage.setScene(scene);
		stage.show();
	}
}
