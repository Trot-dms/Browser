package sample;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    final ToggleGroup bookmarkToggleGroup = new ToggleGroup();
    @FXML
    private WebEngine engine;
    @FXML
    private WebView webView;
    @FXML
    private Label timeLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private ProgressBar loadProgress;
    @FXML
    private TextField addressField;
    @FXML
    private GridPane bookmarks;
    @FXML
    private BorderPane main;
    @FXML
    private ToggleButton bookmarkOn;
    @FXML
    private ToggleButton bookmarkOff;
    @FXML
    private TabPane tabs;
    @FXML
    private Tab historyTab;
    @FXML
    private TreeView historyTree;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        engine = webView.getEngine();
        engine.load("http://www.google.com");

        timeLabel.setText(getActualTime());
        addressLabel.setText(engine.getLocation());

        loadProgress.progressProperty().bind(engine.getLoadWorker().progressProperty());

        Node node = main.getLeft();
        main.setLeft(null);

        bookmarkOn.setToggleGroup(bookmarkToggleGroup);
        bookmarkOff.setToggleGroup(bookmarkToggleGroup);
        bookmarkOff.setSelected(true);

        TreeItem<String> rootItem = new TreeItem<>("History :" + getActualTime());
        rootItem.setExpanded(true);

        historyTree.setRoot(rootItem);

        toggleButtonsListener(node);

        engine.getLoadWorker().stateProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).setText(engine.getTitle());
                        TreeItem<String> newHistoryItem = new TreeItem<>(engine.getLocation());
                        rootItem.getChildren().add(newHistoryItem);
                    }
                });
    }

    @FXML
    private void loadPage(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            engine.load(addressField.getText());
        }
    }

    @FXML
    private void reloadPage() {
        engine.reload();
    }

    private void toggleButtonsListener(Node node) {
        bookmarkToggleGroup.selectedToggleProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (bookmarkOn.isSelected()) {
                        main.setLeft(node);
                    } else {
                        main.setLeft(null);
                    }
                });
    }

    private String getActualTime() {
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime parsedTime = LocalTime.parse(time.format(formatter), formatter);
        return parsedTime.toString();
    }

}
