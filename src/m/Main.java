package m;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;


public class Main extends Application {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    /***
     * First window that the user sees
     * <--- a connection is made with the DB --->
     *
     * @param primaryStage primaryStage
     * @throws Exception exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
            Class.forName(JDBC_DRIVER);
            System.out.println("A CONNECTION WITH THE DATABASE IS TRYING TO BE ESTABLISHED");
            Parent root= FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("GUI/LogInScreen.fxml")));
            primaryStage.setTitle("Storage Manager");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
        }
        catch (Exception e){
            System.out.println("ERROR");
            System.out.println(e);
        }
    }
}
