import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.util.Pair;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.List;

public class CategoriesController {

    /*
     * This class is responsible for loading the categories of beverages. It is
     * responsible for loading the catergory image and list of beverages.
     */

    String selected = "beer"; 

    @FXML
    private ImageView category_image;
    @FXML
    private ListView<Pair<String, String>> categoryList; 

    @FXML
    public void initialize() {
        updateCategoryView(selected);
    }

    public void onHover(MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-font-family: '000webfont'; -fx-font-size: 36px; -fx-font-weight: 700; -fx-background-color:#FFFFFF; -fx-text-fill: #626262; -fx-border-color: #323232; -fx-border-width: 0px; -fx-border-radius: 2; -fx-cursor: hand;");
    }

    public void onExit(MouseEvent event) {
        Button btn = (Button) event.getSource();
        if (btn.getText().equals(selected)) {
            btn.setStyle("-fx-font-family: '000webfont'; -fx-font-size: 36px; -fx-font-weight: 700; -fx-background-color:#FFFFFF; -fx-text-fill: #3232EE; -fx-border-color: #323232; -fx-border-width: 0px; -fx-border-radius: 2; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-font-family: '000webfont'; -fx-font-size: 36px; -fx-font-weight: 700; -fx-background-color:#FFFFFF; -fx-text-fill: #323232; -fx-border-color: #323232; -fx-border-width: 0px; -fx-border-radius: 2; -fx-cursor: hand;");
        }
    }

    public void setSelected(ActionEvent event) {
        Button btn = (Button) event.getSource();
        Button lastSelected = (Button) btn.getScene().lookup("#" + selected);
        lastSelected.setStyle("-fx-font-family: '000webfont'; -fx-font-size: 36px; -fx-font-weight: 700; -fx-background-color:#FFFFFF; -fx-text-fill: #323232; -fx-border-color: #323232; -fx-border-width: 0px; -fx-border-radius: 2; -fx-cursor: hand;");
        selected = btn.getText().toLowerCase();
        updateCategoryView(selected);
    }

    private void updateCategoryView(String category) {
        category_image.setImage(new Image(getClass().getResourceAsStream(category + ".png")));

        List<Object[]> results = Dbutil.executeQuery("SELECT * FROM beverages WHERE lower(drinktype) = ?", category);
        List<Pair<String, String>> drinks = results.stream()
            .map(result -> new Pair<>(result[0].toString(), result[2].toString()))
            .collect(Collectors.toList());

        categoryList.setItems(FXCollections.observableArrayList(drinks));
        categoryList.setCellFactory(lv -> new ListCell<Pair<String, String>>() {
            private final int maxTextLength = 23;

            @Override
            protected void updateItem(Pair<String, String> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-padding: 10px; -fx-font-family: '000webfont'; -fx-font-size: 36px; -fx-font-weight: 700; -fx-text-fill: #323232; -fx-background-color: #FFFFFF;");
                } else {
                    setText(maxText(item.getValue())); // Display the name
                    setStyle("-fx-padding: 10px; -fx-font-family: '000webfont'; -fx-font-size: 36px; -fx-font-weight: 700; -fx-text-fill: #323232; -fx-background-color: #FFFFFF; -fx-cursor: hand;");
                    setOnMouseEntered(e -> setText(" > " + maxText(item.getValue())));
                    setOnMouseExited(e -> setText(maxText(item.getValue())));
                    setOnMouseClicked(e -> {
                        try {
                            goToBeverage();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    });
                }
            }

            private String maxText(String text) {
                if (text.length() > maxTextLength) {
                    return text.substring(0, maxTextLength - 3) + "...";
                }
                return text;
            }
        });
    }

    public void goToBeverage() throws IOException {
    Pair<String, String> selectedItem = categoryList.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
        String selectedId = selectedItem.getKey();
        loadBeverageView(selectedId);
    }
}

private void loadBeverageView(String id) throws IOException{
    BeverageController.setBeverageId(id);
    App.setRoot("beverage");
}


}
