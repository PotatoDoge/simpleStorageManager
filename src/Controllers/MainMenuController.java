package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private Text userTextBox;
    @FXML
    private AnchorPane mainPane;

    public void initialize(URL location, ResourceBundle resources){
        System.out.println("Loading resources");
        initialResources();
    }

    public void initialResources(){
        userTextBox.setText(userTextBox.getText() + LogInScreenController.user.getName());
    }

    public void exitOnAction(ActionEvent actionEvent) throws IOException {
        changeStage("/GUI/LogInScreen.fxml");
    }

    public void settingsOnAction(ActionEvent actionEvent) throws IOException {
        System.out.println("SETTINGS BUTTON PRESSED");
        changeStage("/GUI/Settings.fxml");
    }

    public void accountingOnAction(ActionEvent actionEvent) {
        System.out.println("ACCOUTING BUTTON PRESSED");
    }

    public void hresourcesOnAction(ActionEvent actionEvent) {
        System.out.println("HHRR BUTTON PRESSED");
    }

    public void storageOnAction(ActionEvent actionEvent) {
        System.out.println("STORAGE BUTTON PRESSED");
    }

    /**
     * Method that displays a pop up message when required
     */
    public void popUpMessage(String header, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public void changeStage(String nextStage) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource(nextStage));
        mainPane.getChildren().setAll(pane);
    }
}
