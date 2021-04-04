package Controllers;

import Users.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import javax.swing.plaf.nimbus.State;
import java.io.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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

    static String SQLTable = "mysql";

    static final String databaseName = "StorageManager";

    static String DB_URL = "jdbc:mysql://192.168.86.79:3306/"+SQLTable+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    static final String USER = "bfp";

    static final String PASS = "02082001";

    static Statement stmt = null;

    static Connection conn = null;

    static PreparedStatement pst = null;

    private boolean allowLogIn = false;

    static User user = new User();

    /**
     * Method that checks with the DB if the user and password inputted are correct.
     */
    public void logIn() throws Exception {
        if(passwordTextArea.getText().isEmpty() || usernameTextArea.getText().isEmpty()){
            popUpMessage("Fill everything","All fields must be filled to proceed");
        }
        else{
            if(!checkIfDbExists()){
                try {
                    //SCRIPT THAT WILL RUN THE FIRST TIME THE PROGRAM RUNS
                    runSQLScript("src/SQLFILES/initialSetUP.sql");
                    runSQLScript("src/SQLFILES/addingAdmin.sql");
                    popUpMessage("Database created","Resources loaded.");
                } catch (Exception e) {
                    popUpMessage("Error","Could not connect to database.");
                    e.printStackTrace();
                }
            }
            SQLTable = "StorageManager";
            DB_URL = "jdbc:mysql://localhost/"+SQLTable+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
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
                SettingsController.currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
                registerLog(user.getName(), SettingsController.currentDate);
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

    public void passwordPressed(KeyEvent keyEvent) throws Exception {
        if(keyEvent.getCode() == KeyCode.ENTER){
            logIn();
        }
    }

    public boolean checkIfDbExists() throws SQLException {
        Connection con = DriverManager.getConnection(DB_URL,USER,PASS);
        ResultSet resultSet = con.getMetaData().getCatalogs();

        while (resultSet.next()) {
            // Get the database name, which is at position 1
            String dbName = resultSet.getString(1);
            if(dbName.equals(databaseName)) return true;
        }
        return false;
    }

    public void runSQLScript(String script) throws Exception {
        System.out.println("Executing SQL script......");
        Connection c = DriverManager.getConnection(DB_URL,USER,PASS);
        Statement st = c.createStatement();
        String s = "";
        StringBuilder sb = new StringBuilder();
        try
        {
            File file = new File(script);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            while((s = br.readLine()) != null)
            {
                sb.append(s);
            }
            br.close();
            String[] inst = sb.toString().split(";");
            for (String value : inst) {
                st.executeUpdate(value);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("SQL script execution done.");
    }

    public void registerLog(String user, String date) throws SQLException {
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        stmt = conn.createStatement();
        int insertExchangeRate = stmt.executeUpdate("INSERT INTO logs (user,loggedIn) VALUES('"+user+"','"+date+"')");
        conn.close();
    }
}