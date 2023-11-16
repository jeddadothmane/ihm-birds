package ensisa.birds;

import ensisa.birds.model.Bird;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;

public class BirdCellFactory implements Callback<ListView<Bird>, ListCell<Bird>> {
    @Override
    public ListCell<Bird> call(ListView<Bird> param) {
        return new ListCell<>() {
            public void updateItem(Bird bird, boolean empty) {
                super.updateItem(bird, empty);
                if (empty || bird == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (loader == null) {
                        loader = new FXMLLoader(getClass().getResource("bird-cell.fxml"));
                        loader.setController(this);
                        try {
                            loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    commonNameLabel.textProperty().bind(bird.commonNameProperty());
                    latinNameLabel.textProperty().bind(bird.latinNameProperty());
                    setText(null);
                    setGraphic(vBox);
                }
            }

            @FXML
            private VBox vBox;
            @FXML
            private Label commonNameLabel;
            @FXML
            private Label latinNameLabel;
            private FXMLLoader loader;
        };
    }
}