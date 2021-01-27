package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public class Storage {

    private final ObservableList<String> status = FXCollections.observableArrayList("Active","Inactive");
    private final ObservableList<String> currencyList = FXCollections.observableArrayList("MXN","USD");
    @FXML
    private TextField productIDTextField;
    @FXML
    private TextArea descriptionTextAre;
    @FXML
    private TextField costTextField;
    @FXML
    private ComboBox <String> statusComboBox;
    @FXML
    private ComboBox <String> currencyComboBox;
    @FXML
    private Button addButton;
    @FXML
    private Pane newProductPane;
    @FXML
    private MenuItem newItemMenu;
    @FXML
    private MenuItem editItemMenu;

    /**
     * Method that inserts into the DB the product to be created.
     * @param actionEvent button pressed
     */
    public void addButton(ActionEvent actionEvent) {
        hideItems();
    }

    /**
     * Method that sets items to visible for a specific section (New item).
     */
    public void newItemOnAction() {
        hideItems();
        newProductPane.setVisible(true);
        statusComboBox.setItems(status);
        currencyComboBox.setItems(currencyList);
    }

    /**
     * Method that hides everything on the screen after it's been used.
     */
    public void hideItems(){
        newProductPane.setVisible(false);
        productIDTextField.setFocusTraversable(false);
        productIDTextField.clear();
        descriptionTextAre.setFocusTraversable(false);
        descriptionTextAre.clear();
        statusComboBox.setFocusTraversable(false);
        statusComboBox.setItems(null);
        costTextField.setFocusTraversable(false);
        costTextField.clear();
        currencyComboBox.setFocusTraversable(false);
        currencyComboBox.setItems(null);
        addButton.setFocusTraversable(false);
    }
}
