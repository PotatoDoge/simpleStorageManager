package Controllers;

import Users.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SettingsController {
    @FXML
    private TableColumn <Table, String> positionColumn;
    @FXML
    private TableColumn  <Table, String> userColumn;
    @FXML
    private TableView <Table>usersTable;
    @FXML
    private AnchorPane settingsPane;

    ObservableList<Table> oblist = FXCollections.observableArrayList();

    public void addOnAction(ActionEvent actionEvent) {
        System.out.println("ADD PRESSED");
    }

    public void returnOnAction(ActionEvent actionEvent) throws IOException {
        changeStage("/GUI/MainMenu.fxml");
    }

    public void showUsersOnAction(ActionEvent actionEvent) {
        usersTable.getItems().clear();
        usersTable.setVisible(true);
        try {
            Connection conn = DriverManager.getConnection(LogInScreenController.DB_URL, LogInScreenController.USER, LogInScreenController.PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            while(rs.next()){
                oblist.add(new Table(rs.getString("user"),rs.getString("position")));
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        usersTable.setItems(oblist);

    }

    public void changeStage(String nextStage) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource(nextStage));
        settingsPane.getChildren().setAll(pane);
    }


}
