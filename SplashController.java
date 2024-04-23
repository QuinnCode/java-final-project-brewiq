import java.io.IOException;
import javafx.fxml.FXML;

public class SplashController {

    /*
     * This class is responsible for loading the splash screen. It is responsible for
     * navigating to the categories screen and closing the application if the user
     * is under the age of 21.
     */

    @FXML
    private void navToCategories() throws IOException {
        App.setRoot("categories1");
    }

    @FXML
    private void closeApp() {
        System.exit(0);
    }
}
