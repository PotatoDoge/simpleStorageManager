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
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsController {

    private final ObservableList<String> positions = FXCollections.observableArrayList("Admin","Human Resources","Storage","Accounting");
    @FXML
    private Text dateText;
    @FXML
    private Text usdEqualsText;
    @FXML
    private Button saveRateButton;
    @FXML
    private TextField mxdTextField;
    @FXML
    private TextField dateTextField;
    @FXML
    private PasswordField loggedPasswordTextAreaDelete;
    @FXML
    private TextField loggedUserTextAreaDelete;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField userToBeDeleted;
    @FXML
    private ComboBox <String> positionComboBox;
    @FXML
    private Button addButton;
    @FXML
    private TextField newUserTextField;
    @FXML
    private TextField newPasswordTextField;
    @FXML
    private PasswordField loggedPasswordTextArea;
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

    static String currentDate;

    //Database's local URL
    static final String DB_URL = "jdbc:mysql://localhost/StorageManager?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    //User that accesses the Database
    static final String USER = "root";

    //Database's password
    static final String PASS = "";

    static Statement stmt = null;

    static Connection conn = null;

    static PreparedStatement pst = null;

    ObservableList<Table> oblist = FXCollections.observableArrayList();

    public void addOnAction() {
        System.out.println("ADD PRESSED");
        hideItems();
        if(LogInScreenController.user.getPosition().equals("Admin")){
            newUserTextField.setVisible(true);
            newPasswordTextField.setVisible(true);
            loggedUserTextArea.setVisible(true);
            loggedPasswordTextArea.setVisible(true);
            loggedUserTextArea.setFocusTraversable(false);
            loggedPasswordTextArea.setFocusTraversable(false);
            addButton.setVisible(true);
            positionComboBox.setVisible(true);
            positionComboBox.setItems(positions);
        }
        else{
            popUpMessage("Not a valid position","Only Admins can add a\nnew user to the system.");
        }


    }

    /**
     * Method that manages the logic of adding a new user
     */
    public void addButtonAction(){
        if(newUserTextField.getText().isEmpty() || newPasswordTextField.getText().isEmpty() || loggedUserTextArea.getText().isEmpty() || loggedPasswordTextArea.getText().isEmpty()){
            popUpMessage("Fill everything","All fields must be filled to proceed");
        }
        else if(newUserTextField.getText().length() > 25 || newPasswordTextField.getText().length() > 25){
            popUpMessage("Username or password too long","Neither the username nor the\npassword can exceed 25 characters");
        }
        else if(isAlphanumeric(newUserTextField.getText()) || isAlphanumeric(newPasswordTextField.getText())){
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
                popUpMessage("Successful!","User added successfully!");
                positionComboBox.setValue(null);
                newUserTextField.clear();
                newPasswordTextField.clear();
                loggedUserTextArea.clear();
                loggedPasswordTextArea.clear();
                conn.close();
            }
            catch (Exception e) {
                System.out.println("Error: "+e);
            }
        }

    }

    public void returnOnAction() throws IOException {
        changeStage("/GUI/MainMenu.fxml");
    }

    public void showUsersOnAction() {
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
            conn.close();
        }
        catch (Exception e){
            System.out.println("Error: "+e);
        }
        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        usersTable.setItems(oblist);

    }

    /**
     * Method that changes stage
     * @param nextStage nextStage
     * @throws IOException exception
     */
    public void changeStage(String nextStage) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource(nextStage));
        settingsPane.getChildren().setAll(pane);
    }

    /**
     * Method that makes sure that every object in the screen gets cleared and hidden
     */
    public void hideItems(){
        positionComboBox.setVisible(false);
        positionComboBox.setItems(null);
        positionComboBox.setFocusTraversable(false);
        newUserTextField.setVisible(false);
        newUserTextField.clear();
        newUserTextField.setFocusTraversable(false);
        newPasswordTextField.setVisible(false);
        newPasswordTextField.clear();
        newPasswordTextField.setFocusTraversable(false);
        loggedUserTextArea.setVisible(false);
        loggedUserTextArea.clear();
        loggedUserTextArea.setFocusTraversable(false);
        loggedPasswordTextArea.setVisible(false);
        loggedPasswordTextArea.clear();
        loggedPasswordTextArea.setFocusTraversable(false);
        usersTable.getItems().clear();
        usersTable.setVisible(false);
        userToBeDeleted.setFocusTraversable(false);
        addButton.setVisible(false);
        addButton.setFocusTraversable(false);
        deleteButton.setVisible(false);
        deleteButton.setFocusTraversable(false);
        userToBeDeleted.setVisible(false);
        userToBeDeleted.clear();
        userToBeDeleted.setFocusTraversable(false);
        loggedUserTextAreaDelete.setVisible(false);
        loggedUserTextAreaDelete.clear();
        loggedUserTextAreaDelete.setFocusTraversable(false);
        loggedPasswordTextAreaDelete.clear();
        loggedPasswordTextAreaDelete.setVisible(false);
        loggedPasswordTextAreaDelete.setFocusTraversable(false);
        dateTextField.clear();
        dateTextField.setVisible(false);
        dateTextField.setFocusTraversable(false);
        mxdTextField.clear();
        mxdTextField.setVisible(false);
        mxdTextField.setFocusTraversable(false);
        saveRateButton.setVisible(false);
        saveRateButton.setFocusTraversable(false);
        usdEqualsText.setVisible(false);
        usdEqualsText.setFocusTraversable(false);
        dateText.setVisible(false);
        dateText.setFocusTraversable(false);
    }

    /**
     * Method that allows messages/warning to pop up in the screen
     * @param header header
     * @param text body text
     */
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
     * @return true if no non-alpha numeric values were found
     */
    public boolean isAlphanumeric(String str) {
        char[] charArray = str.toCharArray();
        for (char c : charArray) {
            if (!Character.isLetterOrDigit(c))
                return true;
        }
        return false;
    }

    /**
     * Method that checks if the username is already registered in the DB
     * @param usr username
     * @return boolean
     */
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
            System.out.println("Error: "+e);
        }
        return true;
    }

    public void deleteOnAction() {
        hideItems();
        loggedUserTextArea.setFocusTraversable(false);
        loggedPasswordTextArea.setFocusTraversable(false);
        System.out.println("DELETE PRESSED");
        if(LogInScreenController.user.getPosition().equals("Admin")){
            userToBeDeleted.setVisible(true);
            loggedUserTextAreaDelete.setVisible(true);
            loggedPasswordTextAreaDelete.setVisible(true);
            deleteButton.setVisible(true);
        }
        else{
            popUpMessage("Not a valid position","Only Admins can delete a\nnew user from the system.");
        }

    }

    public void deleteButtonAction(){
        try{
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String SQL = "SELECT * FROM users WHERE user='" + userToBeDeleted.getText() + "'";
            pst = conn.prepareStatement(SQL);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                if (userToBeDeleted.getText().equals(rs.getString("user"))) {
                    if(!loggedUserTextAreaDelete.getText().equals(LogInScreenController.user.getName()) || !loggedPasswordTextAreaDelete.getText().equals(LogInScreenController.user.getPass())){
                        popUpMessage("Not a valid user/password","The logged username or password\nis not correct.");
                    }
                    else{
                        int deleteUser = stmt.executeUpdate("DELETE FROM users WHERE user='"+userToBeDeleted.getText()+"'");
                        System.out.println("User deleted");
                        popUpMessage("User deleted","The user was deleted successfully");
                        loggedPasswordTextAreaDelete.clear();
                        loggedPasswordTextAreaDelete.setFocusTraversable(false);
                        loggedUserTextAreaDelete.clear();
                        loggedUserTextAreaDelete.setFocusTraversable(false);
                        userToBeDeleted.clear();
                        userToBeDeleted.setFocusTraversable(false);
                        deleteButton.setFocusTraversable(false);
                    }
                }
            }
            else{
                popUpMessage("Username or password doesn't exist","The provided username or password is not correct");
            }
            conn.close();
        }
        catch (Exception e){
            System.out.println("Error: "+e);
        }
    }

    public void exchangeRateOnAction() {
        System.out.println("EXCHANGE PRESSED");
        hideItems();
        usdEqualsText.setVisible(true);
        mxdTextField.setVisible(true);
        saveRateButton.setVisible(true);
        dateTextField.setVisible(true);
        dateTextField.setEditable(false);
        dateText.setVisible(true);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = df.format(new Date());
        dateTextField.setText(currentDate);
    }

    public void saveRateButtonOnAction(ActionEvent actionEvent) {
        double amount = 0;
        try{
            amount = Double.parseDouble(mxdTextField.getText());
            if(!(amount<100) || !(amount>0)){
                popUpMessage("Not a valid amount.","The amount that you typed is\nnot valid.");
            }
            else{
                try{
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    stmt = conn.createStatement();
                    int insertExchangeRate = stmt.executeUpdate("INSERT INTO exchangeRateUsd2Mxn (date,rate) VALUES('"+currentDate+"','"+amount+"')");
                    System.out.println("Inserted successfully");
                    popUpMessage("Successful!","Exchange rate set successfully!");
                    conn.close();
                }
                catch (Exception e) {
                    System.out.println("Error: "+e);
                }
            }
        }catch (Exception e){
            popUpMessage("Not a valid input.","You typed a not valid input.");
        }
    }

}
