package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.*;

public class Storage {

    private final ObservableList<String> status = FXCollections.observableArrayList("Active","Inactive");
    private final ObservableList<String> currencyList = FXCollections.observableArrayList("MXN","USD");
    private final ObservableList<String> unit = FXCollections.observableArrayList("KG","LITER","METER","LB","FT","OZ","PIECE","BOX");
    @FXML
    private AnchorPane inventoryPane;
    @FXML
    private ComboBox <String> unitComboBox;
    @FXML
    private TextField productIDTextField;
    @FXML
    private TextArea descriptionTextArea;
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

    //Database's local URL
    static final String DB_URL = "jdbc:mysql://localhost/StorageManager?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    //User that accesses the Database
    static final String USER = "root";

    //Database's password
    static final String PASS = "";

    static Statement stmt = null;

    static Connection conn = null;

    static PreparedStatement pst = null;

    // HERE STARTS THE PART THAT MANAGES THE "NEW PRODUCT" MODULE'S LOGIC
    /**
     * Method that inserts into the DB the product to be created.
     * @param actionEvent button pressed
     */
    // ADD THE DATE IN WHICH IT WAS REGISTERED (ADD IT IN THE product_name database)
    public void addButton(ActionEvent actionEvent) throws SQLException {
        if(!productIDTextField.getText().isEmpty() && !(productIDTextField.getText().length()>24) &&
                !costTextField.getText().isEmpty() && !(statusComboBox.getValue() == null) && !(currencyComboBox.getValue() == null) &&
                !(descriptionTextArea.getText().length()>100) && !descriptionTextArea.getText().isEmpty() && isNumeric(costTextField.getText()) &&
                !(unitComboBox.getValue() == null) && checkLastExchangeRateDate()){
            if(!checkIfProductInDB(productIDTextField.getText()) && !(unitComboBox.getValue() == null) ){
                popUpMessage("Product ID already registered","This product ID has already been registered to\nthe database.");
            }
            else{
                String tableName = "product_"+productIDTextField.getText();
                try {
                    String SQL = "CREATE TABLE "+tableName+"(id VARCHAR(22), description VARCHAR(100), cost REAL, exchRate REAL, currency VARCHAR(6), unit VARCHAR(7))";
                    conn = DriverManager.getConnection(DB_URL,USER,PASS);
                    stmt = conn.createStatement();
                    stmt.executeUpdate(SQL);
                    conn.close();
                    popUpMessage("Product added correctly!","The product has been added to the\nlist of products.");
                    fillProductTable(tableName,productIDTextField.getText(),descriptionTextArea.getText(),Double.parseDouble(costTextField.getText()),getLastExchangeRate(),currencyComboBox.getValue(),unitComboBox.getValue());
                    addProductToDB(productIDTextField.getText(),0,statusComboBox.getValue(), Double.parseDouble(costTextField.getText()),unitComboBox.getValue());
                    // NEXT LINES CLEAN THE FIELDS AND COMBO BOXES
                    productIDTextField.setFocusTraversable(false);
                    productIDTextField.clear();
                    statusComboBox.setFocusTraversable(false);
                    statusComboBox.setItems(null);
                    costTextField.setFocusTraversable(false);
                    costTextField.clear();
                    currencyComboBox.setFocusTraversable(false);
                    currencyComboBox.setItems(null);
                    unitComboBox.setFocusTraversable(false);
                    unitComboBox.setItems(null);
                    descriptionTextArea.setFocusTraversable(false);
                    descriptionTextArea.clear();
                }
                catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        else{
            if(!checkLastExchangeRateDate()){
                popUpMessage("Update your exchange rate","The exchange rate must be updated\ndaily, at least once.");
            }
            else if(descriptionTextArea.getText().length()>100 || descriptionTextArea.getText().isEmpty()){
                popUpMessage("Description too long or empty","The item's description can not be\nlonger than 100 characters nor empty.");
            }
            else if(productIDTextField.getText().isEmpty() || productIDTextField.getText().length()>24){
                popUpMessage("Product ID is empty or too long.","Product ID can not be empty nor longer\nthan 24 characters.");
            }
            else if(costTextField.getText().isEmpty()){
                popUpMessage("Cost field is empty.","Cost field can not be empty.");
            }
            else if(statusComboBox.getValue() == null){
                popUpMessage("Status not selected","Please select an item for the product.");
            }
            else if(currencyComboBox.getValue() == null){
                popUpMessage("Currency is not selected.","Please select a currency for the product.");
            }
            else if(!isNumeric(costTextField.getText())){
                popUpMessage("Not a numeric value.","Product's cost must be a numeric value");
            }
            else if(unitComboBox.getValue() == null){
                popUpMessage("Unit not selected.","A unit must be select in order to\nregister this product.");
            }
        }

    }

    /**
     * Method that sets items to visible for a specific section (New item).
     */
    public void newItemOnAction() {
        hideItems();
        newProductPane.setVisible(true);
        productIDTextField.setVisible(true);
        descriptionTextArea.setVisible(true);
        statusComboBox.setItems(status);
        currencyComboBox.setItems(currencyList);
        unitComboBox.setItems(unit);
        addButton.setVisible(true);
    }

    /**
     * Method that hides everything on the screen after it's been used.
     */
    public void hideItems(){
        newProductPane.setVisible(false);
        productIDTextField.setFocusTraversable(false);
        productIDTextField.clear();
        descriptionTextArea.setFocusTraversable(false);
        descriptionTextArea.clear();
        statusComboBox.setFocusTraversable(false);
        statusComboBox.setItems(null);
        costTextField.setFocusTraversable(false);
        costTextField.clear();
        currencyComboBox.setFocusTraversable(false);
        currencyComboBox.setItems(null);
        addButton.setFocusTraversable(false);
        unitComboBox.setFocusTraversable(false);
        unitComboBox.setItems(null);
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
     * Method that checks if the productID is already registered in the DB
     * @param productID productID
     * @return boolean
     */
    public boolean checkIfProductInDB(String productID){
        try{
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String SQL = "SELECT * FROM products WHERE id='" + productID + "'";
            pst = conn.prepareStatement(SQL);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                if(productID.equals(rs.getString("id"))){
                    return false;
                }
            }
        }
        catch (Exception e){
            System.out.println("Error: "+e);
        }
        return true;
    }

    /**
     * Method that adds products to the main products database.
     * @param id id
     * @param qty qty
     * @param status status
     * @param price price
     * @throws SQLException exception
     */
    public void addProductToDB(String id, int qty,String status, double price, String unit) throws SQLException {
        char s;
        if(status.equals("Inactive")){ s = 'I'; }
        else{ s = 'A';}
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        stmt = conn.createStatement();
        stmt.executeUpdate("INSERT INTO products(id,qty,status,lastPrice,lastUnit) VALUES('"+id+"','"+qty+"','"+s+"','"+price+"','"+unit+"')");
        conn.close();
        System.out.println("Product added successfully to products DB");
    }

    /**
     * Method that checks if a given string is a number
     * @param num string passed
     * @return true if numeric, false otherwise
     */
    public boolean isNumeric(String num){
        try{
          Double.parseDouble(num);
          return true;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * Method that creates the new product's table in the database and fills it up.
     * @param tableName prtoduct table's name
     * @param id product's id
     * @param description product's description
     * @param cost product's cost
     * @param exchRate product's exchange rate at the moment of creating it
     * @param currency product's currency
     * @param unit product's unit
     * @throws SQLException exception
     */
    public void fillProductTable(String tableName,String id, String description,double cost, double exchRate, String currency,String unit) throws SQLException{
        String SQL = "INSERT into "+ tableName + "(id,description,cost,exchRate,currency,unit) VALUES('"+id+"','"+description+"','"+cost+"','"+exchRate+"','"+
                     currency+"','"+unit+"')";
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        stmt = conn.createStatement();
        stmt.executeUpdate(SQL);
        conn.close();
    }

    /**
     * Method that checks if an exchange rate has been declared today.
     * @return true if they have , else false
     */
    public boolean checkLastExchangeRateDate() throws SQLException {
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        stmt = conn.createStatement();
        String SQL = "SELECT * FROM exchangeRateUsd2Mxn ORDER BY date DESC LIMIT 1";
        pst = conn.prepareStatement(SQL);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            if(SettingsController.currentDate.equals(rs.getString("date"))) return true;
        }
        conn.close();
        System.out.println(SettingsController.currentDate);
        return false;
    }

    /**
     * Method that gets the last exchangeRate saved in the DB
     * @return last exchangeRate
     * @throws SQLException exception
     */
    public double getLastExchangeRate() throws  SQLException{
        double eR = 0;
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        String SQL = " SELECT * FROM exchangeRateUsd2Mxn";
        ResultSet rs = stmt.executeQuery(SQL);
        while(rs.next()){
            eR = rs.getDouble("rate");
        }
        conn.close();
        return eR;
    }

    /**
     * Method that changes stage
     * @param nextStage nextStage
     * @throws IOException exception
     */
    public void changeStage(String nextStage) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource(nextStage));
        inventoryPane.getChildren().setAll(pane);
    }

    /**
     * When return button is clicked, the system returns to the main menu
     * @param actionEvent button pressed
     * @throws IOException exception
     */
    public void returnOnAction(ActionEvent actionEvent) throws IOException {
        changeStage("/GUI/MainMenu.fxml");
    }

    // HERE ENDS THE PART THAT MANAGES THE "NEW PRODUCT" MODULE'S LOGIC

    // HERE STARTS THE PART THAT MANAGES THE "EDIT PRODUCT" MODULE'S LOGIC
    public void editItemOnAction(ActionEvent actionEvent) {
    }
}
