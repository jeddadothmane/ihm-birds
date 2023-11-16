package ensisa.birds.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.InputStream;

public class BirdRepository {
    public ObservableList<Bird> birds;

    public BirdRepository() {
        birds = FXCollections.observableArrayList();
    }

    public void Load() {
        try (InputStream inputStream = getClass().
                getResourceAsStream("/ensisa/birds/assets/Birds.json")) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            var birdArray = mapper.readValue(inputStream, Bird[].class);
            birds.clear();
            birds.addAll(birdArray);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
