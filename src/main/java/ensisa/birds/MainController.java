package ensisa.birds;

import ensisa.birds.model.Bird;
import ensisa.birds.model.BirdRepository;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.function.Predicate;

public class MainController {
    private final BirdRepository repository;

    @FXML
    private Label commonNameLabel;
    @FXML
    private Label latinNameLabel;
    @FXML
    private Label familyLabel;
    @FXML
    private Label genusLabel;
    @FXML
    private Label specieLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private ImageView birdImageView;
    @FXML
    private ListView<Bird> birdListView;
    @FXML
    private VBox birdView;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    private FilteredList<Bird> filteredBirdList;

    @FXML
    private void deleteButtonAction(ActionEvent event) {
        repository.birds.remove(getCurrentBird());
    }

    @FXML
    private void editButtonAction(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        BirdEditDialog dialog = new BirdEditDialog(stage, getCurrentBird());
        dialog.showAndWait().ifPresent(bird -> getCurrentBird().copyFrom(bird));
    }

    private final ObjectProperty<Bird> currentBird;

    @FXML
    private TextField filterTextField;

    public MainController() {
        repository = new BirdRepository();
        repository.Load();
        currentBird = new SimpleObjectProperty<>(repository.birds.get(0));
    }

    public void initialize() {
        birdListView.setCellFactory(new BirdCellFactory());
        filteredBirdList = new FilteredList<>(repository.birds);
        birdListView.setItems(filteredBirdList);
        currentBirdProperty().bind(birdListView.getSelectionModel().
                selectedItemProperty());
        currentBirdProperty().addListener((observable, oldBird, newBird) -> {
            // Pour une liaison unidirectionnelle, il n'est pas nécessaire de supprimer
            // l'ancienne liaison avant d'en créer une nouvelle
            if (newBird != null)
                bind(newBird);
        });
        birdView.visibleProperty().bind(Bindings.createBooleanBinding(
                () -> getCurrentBird() != null, currentBirdProperty()));
        editButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> getCurrentBird() == null, currentBirdProperty()));
        deleteButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> getCurrentBird() == null, currentBirdProperty()));
        filterTextField.textProperty().addListener((observable, oldText, newText) -> {
            Predicate<Bird> filter = bird -> {
                String f = newText.trim().toLowerCase();
                return f.isEmpty() || bird.getCommonName().toLowerCase().contains(f);
            };
            filteredBirdList.setPredicate(filter);
        });
    }

    public Bird getCurrentBird() {
        return currentBird.get();
    }

    public ObjectProperty<Bird> currentBirdProperty() {
        return currentBird;
    }

    public void setCurrentBird(Bird currentBird) {
        this.currentBird.set(currentBird);
    }

    public void bind(Bird bird) {
        commonNameLabel.textProperty().bind(bird.commonNameProperty());
        latinNameLabel.textProperty().bind(bird.latinNameProperty());
        familyLabel.textProperty().bind(bird.familyProperty());
        genusLabel.textProperty().bind(bird.genusProperty());
        specieLabel.textProperty().bind(bird.specieProperty());
        descriptionLabel.textProperty().bind(bird.descriptionProperty());
        birdImageView.imageProperty().bind(bird.imageProperty());
    }


}