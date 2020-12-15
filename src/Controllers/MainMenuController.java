package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    @FXML
    private AnchorPane mainPane;

    public void initialize(URL location, ResourceBundle resources){
        System.out.println("Loading resources");
    }

    public void initialResources(){

    }

    public void exitOnAction(ActionEvent actionEvent) {
        System.out.println("EXIT BUTTON PRESSED");
    }

    public void settingsOnAction(ActionEvent actionEvent) {
        System.out.println("SETTINGS BUTTON PRESSED");
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
}
