package sample;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
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
import java.util.ResourceBundle;

import static sample.helpers.TimeHelper.changeTimeLabelInterval;
import static sample.helpers.TimeHelper.getActualTime;
import static sample.helpers.URIHelper.getParsedURI;

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
        changeTimeLabelInterval(1, timeLabel);

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
        addToggleButtonsListener(node);
        addWebEngineListener(rootItem);
        addHistoryTreeListener();
        addressField.setOnKeyPressed(this::loadPageEvent);
    }

    private void addHistoryTreeListener() {
        historyTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            TreeItem<String> selectedItem = (TreeItem<String>) newValue;
            addressField.setText(selectedItem.getValue());
        });
    }

    private void addWebEngineListener(TreeItem treeItem) {
        engine.getLoadWorker().stateProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.RUNNING) {
                        loadProgress.setVisible(true);
                        addressLabel.setText("Loading ... " + engine.getLocation());
                        TreeItem<String> newHistoryItem = new TreeItem<>(engine.getLocation());
                        treeItem.getChildren().add(newHistoryItem);
                    }
                    if (newValue == Worker.State.SUCCEEDED) {
                        tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).setText(engine.getTitle());
                        loadProgress.setVisible(false);
                        addressLabel.setText(engine.getTitle());
                    }
                });
    }

    @FXML
    private void loadPageEvent(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            loadPage(addressField.getText());
        }
    }

    private void loadPage(String address) {
        engine.load(getParsedURI(address));
    }

    @FXML
    private void reloadPage() {
        engine.reload();
    }

    @FXML
    public void goToPage() {
        if (!addressField.getText().isEmpty()) {
            loadPage(addressField.getText());
        }
    }

    private void addToggleButtonsListener(Node node) {
        bookmarkToggleGroup.selectedToggleProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (bookmarkOn.isSelected()) {
                        main.setLeft(node);
                    } else {
                        main.setLeft(null);
                    }
                });
    }
}
