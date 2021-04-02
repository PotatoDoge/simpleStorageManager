package Controllers;

import Users.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogInScreenController {

    @FXML
    private AnchorPane logInPane;
    @FXML
    private Button logInButton;
    @FXML
    private TextField usernameTextArea;
    @FXML
    private PasswordField passwordTextArea;

    static final String DB_URL = "jdbc:mysql://localhost/StorageManager?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    static final String USER = "root";

    static final String PASS = "02082001";

    static Statement stmt = null;

    static Connection conn = null;

    static PreparedStatement pst = null;

    private boolean allowLogIn = false;

    static User user = new User();

    /**
     * Method that checks with the DB if the user and password inputted are correct.
     */
    public void logIn() {
        if(passwordTextArea.getText().isEmpty() || usernameTextArea.getText().isEmpty()){
            popUpMessage("Fill everything","All fields must be filled to proceed");
        }
        else{
            String pass = passwordTextArea.getText();
            try{
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                stmt = conn.createStatement();
                String SQL = "SELECT * FROM users WHERE user='" + usernameTextArea.getText() + "'";
                pst = conn.prepareStatement(SQL);
                ResultSet rs = pst.executeQuery();
                if(rs.next()) {
                    if (pass.equals(rs.getString("password"))) {
                        System.out.println("Log In successful");
                        String name = usernameTextArea.getText();
                        user.setName(name);
                        user.setPass(pass);
                        user.setPosition(rs.getString("position"));
                        allowLogIn = true;
                    }
                    else {
                        popUpMessage("Username or password doesn't exist","The provided username or password is not correct");
                    }
                }
                else{
                    popUpMessage("Username or password doesn't exist","The provided username or password is not correct");
                }
                conn.close();
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
        if(allowLogIn){
            try {
                SettingsController.currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                changeStage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public void changeStage() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/GUI/MainMenu.fxml"));
        logInPane.getChildren().setAll(pane);
    }

    public void usernamePressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER){
            passwordTextArea.requestFocus();
        }
    }

    public void passwordPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER){
            logIn();
        }
    }
}
