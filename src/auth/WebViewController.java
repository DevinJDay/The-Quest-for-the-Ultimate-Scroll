package auth;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WebViewController {

    @FXML
    private WebView webView;
    private WebEngine webEngine;
 

    public void initialize() {
        webEngine = webView.getEngine();
    }

    public void loadUrl(String url) {
        webEngine.load(url);
    }

    public void setRedirectHandler(final RedirectHandler redirectHandler) {
        webEngine.locationProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			    if (redirectHandler != null) {
			        redirectHandler.onRedirect(oldValue, newValue);
			    }
			}
		});
    }

    public interface RedirectHandler {
        void onRedirect(String oldUrl, String newUrl);
    }
}