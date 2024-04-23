import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.IOException;
import java.util.List;
import javafx.scene.control.Label;

public class BeverageController {

    /*
     * This class is responsible for loading the details of a beverage. It is
     * responsible for loading the image, category, name, abv, and description of
     * the beverage.
     */

    @FXML
    private ImageView beverage_image;
    @FXML
    private Label category_label;
    @FXML
    private Label name_label;
    @FXML
    private Label abv_label;
    @FXML
    private Label desc_label;

    private static String beverageId;

    public static void setBeverageId(String id) {
        BeverageController.beverageId = id;
        System.out.println("Beverage ID set to: " + id);
    }

    @FXML
    private void goBack() throws IOException {
        App.setRoot("categories");
    }

    @FXML
    public void initialize() {
        if (beverageId != null && !beverageId.isEmpty()) {
            loadBeverageDetails();
        } else {
            System.err.println("Beverage ID not set before initializing the view.");
        }
    }

    public void loadBeverageDetails() {
        try {
            List<Object[]> results = Dbutil.executeQuery("SELECT * FROM beverages WHERE id = ?", beverageId);
            Object[] beverage = results.get(0);

            String imageUrl = beverage[5].toString();
            Image image = new Image(imageUrl);
            beverage_image.setImage(image);

            category_label.setText(beverage[1].toString());
            name_label.setText(beverage[2].toString());
            abv_label.setText(beverage[3].toString() + " abv");
            desc_label.setText(beverage[4].toString());

        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
    }
}
