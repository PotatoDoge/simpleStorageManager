package Controllers;

import Users.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;
import java.sql.*;

public class SettingsController {

    @FXML
    private ComboBox <String> positionComboBox;

    private final ObservableList<String> positions = FXCollections.observableArrayList("Admin","Human Resources","Storage","Accounting");
    @FXML
    private Button addButton;
    @FXML
    private TextField newUserTextField;
    @FXML
    private TextField newPasswordTextField;
    @FXML
    private TextField loggedPasswordTextArea;
    @FXML
    private TextField loggedUserTextArea;
    @FXML
    private TableColumn <Table, String> positionColumn;
    @FXML
    private TableColumn  <Table, String> userColumn;
    @FXML
    private TableView <Table>usersTable;
    @FXML
    private AnchorPane settingsPane;

    static final String DB_URL = "jdbc:mysql://localhost/StorageManager?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    static final String USER = "root";

    static final String PASS = "";

    static Statement stmt = null;

    static Connection conn = null;

    static PreparedStatement pst = null;

    ObservableList<Table> oblist = FXCollections.observableArrayList();

    public void addOnAction(ActionEvent actionEvent) {
        System.out.println("ADD PRESSED");
        hideItems();
        newUserTextField.setVisible(true);
        newPasswordTextField.setVisible(true);
        loggedUserTextArea.setVisible(true);
        loggedPasswordTextArea.setVisible(true);
        addButton.setVisible(true);
        positionComboBox.setVisible(true);
        positionComboBox.setItems(positions);

    }

    public void returnOnAction(ActionEvent actionEvent) throws IOException {
        changeStage("/GUI/MainMenu.fxml");
    }

    public void showUsersOnAction(ActionEvent actionEvent) {
        hideItems();
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

    /**
     * Method that makes sure that every object in the screen gets cleared and hidden
     */
    public void hideItems(){
        positionComboBox.setVisible(false);
        newUserTextField.setVisible(false);
        newPasswordTextField.setVisible(false);
        loggedUserTextArea.setVisible(false);
        loggedPasswordTextArea.setVisible(false);
        usersTable.getItems().clear();
        usersTable.setVisible(false);
        addButton.setVisible(false);
        newPasswordTextField.clear();
        newUserTextField.clear();
        loggedPasswordTextArea.clear();
        loggedUserTextArea.clear();
        positionComboBox.setItems(null);

    }

    public void popUpMessage(String header, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.showAndWait();
    }

    /**
     * Method that checks if the given string contains non alpha numeric values
     * @param str string
     * @return true if no non alpha numeric values were found
     */
    public boolean isAlphanumeric(String str) {
        char[] charArray = str.toCharArray();
        for (char c : charArray) {
            if (!Character.isLetterOrDigit(c))
                return false;
        }
        return true;
    }


    public void addButtonAction(ActionEvent actionEvent) throws IOException{
        if(newUserTextField.getText().isEmpty() || newPasswordTextField.getText().isEmpty() || loggedUserTextArea.getText().isEmpty() || loggedPasswordTextArea.getText().isEmpty()){
            popUpMessage("Fill everything","All fields must be filled to proceed");
        }
        else if(newUserTextField.getText().length() > 25 || newPasswordTextField.getText().length() > 25){
            popUpMessage("Username or password too long","Neither the username nor the\npassword can exceed 25 characters");
        }
        else if(!isAlphanumeric(newUserTextField.getText()) || !isAlphanumeric(newPasswordTextField.getText())){
            popUpMessage("Not a valid character","Both the user and password must only\ncontain alpha numeric values");
        }
        else if(!loggedUserTextArea.getText().equals(LogInScreenController.user.getName()) || !loggedPasswordTextArea.getText().equals(LogInScreenController.user.getPass())){
            popUpMessage("Not a valid user/password","The logged username or password\nis not correct.");
        }
        else if(!checkIfUsernameInDB(newUserTextField.getText())){
            popUpMessage("Not a valid username","That username already exists.\nChoose a different one.");
        }
        else if(positionComboBox.getValue() == null){
            popUpMessage("Not a valid position","Select a valid position");
        }
        else{
            try{
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                stmt = conn.createStatement();
                int insertUser = stmt.executeUpdate("INSERT INTO users (user,password,position) VALUES('"+newUserTextField.getText()+"','"+newPasswordTextField.getText()+"','"+positionComboBox.getValue()+"')");
                System.out.println("Inserted successfully");
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public boolean checkIfUsernameInDB(String usr){
        try{
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String SQL = "SELECT * FROM users WHERE user='" + usr + "'";
            pst = conn.prepareStatement(SQL);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                if(newUserTextField.getText().equals(rs.getString("user"))){
                    return false;
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return true;
    }

    public void positionOnAction(ActionEvent actionEvent) {

    }
}
