package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.*;

public class LogInScreenController {

    @FXML
    private Button logInButton;
    @FXML
    private TextField usernameTextArea;
    @FXML
    private PasswordField passwordTextArea;

    static final String DB_URL = "jdbc:mysql://localhost/StorageManager?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    static final String USER = "root";

    static final String PASS = "";

    static Statement stmt = null;

    static Connection conn = null;

    static PreparedStatement pst = null;

    private boolean allowLogIn = false;

    /**
     * Method that checks with the DB if the user and password inputted are correct.
     * @param event button clicked
     */
    public void logIn(ActionEvent event) {
        if(passwordTextArea.getText().isEmpty() || usernameTextArea.getText().isEmpty()){
            popUpMessage("Fill everything","All fields must be filled to proceed");
        }
        else{
            String pass = passwordTextArea.getText();
            try{
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                stmt = conn.createStatement();
                String SQL = "SELECT * FROM admins WHERE user='" + usernameTextArea.getText() + "'";
                pst = conn.prepareStatement(SQL);
                ResultSet rs = pst.executeQuery();
                if(rs.next()) {
                    if (pass.equals(rs.getString("password"))) {
                        System.out.println("Log In successful");
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
}