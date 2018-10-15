/*
Andrew Matrai
CNT 4714
project3
 */
package project3;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;

/**
 *
 * @author abmat
 */
public class FXMLDocumentController implements Initializable {
    
    public final DBUtil util = new DBUtil();
    public ObservableList<ObservableList> data;
    @FXML
    public TableView tableView;
    public Label dbStatus;
    public Button clearResultsButton;
    public TextField usernameField = new TextField();
    public Button executeSqlButton;
    public Button clearSqlButton;
    public Button connectDbButton;
    public TextField passwordField = new TextField();
    public ComboBox drivers;
    public ComboBox URLS;
    public TextArea userSqlCommandText = new TextArea();
    
    @FXML
    private void handleConnectDbButton(ActionEvent event) throws SQLException {
        util.setDriver(drivers.getValue().toString());
        util.setDbURL(URLS.getValue().toString());
        util.setUser(usernameField.getText());
        util.setPassw(passwordField.getText());
        
        try {
            util.dbConnect();
            dbStatus.setText("Database Connected!");
        } catch (SQLException | ClassNotFoundException ex) {
            Alert alert = new Alert(AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        util.dbDisconnect();
        
    }
    @FXML
    private void handleExecuteSqlButton(ActionEvent event) {
        tableView.getItems().clear();
        tableView.getColumns().clear();
        try {
            System.out.println(userSqlCommandText.getText());
           sqlCommand(userSqlCommandText.getText());
        } catch (SQLException | ClassNotFoundException ex) {
            Alert alert = new Alert(AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    @FXML
    private void handleClearSqlButton(ActionEvent event) {
        userSqlCommandText.setText("");
    }
    @FXML
    private void handleClearResultsButton(ActionEvent event) {
        tableView.getItems().clear();
        tableView.getColumns().clear();
    }
    @FXML
    public void sqlCommand(String sql) throws SQLException, ClassNotFoundException {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        ResultSet rs;
        util.dbConnect();
        
        Statement st = util.getConnection().createStatement();
        
        boolean status = st.execute(sql);
        if (status) {
            rs = st.getResultSet();
        
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            //We are using non property style for making dynamic table
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });

            tableView.getColumns().addAll(col);
        }
        
        while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);
 
            }
        
       tableView.setItems(data);
        } else {
            int count = st.getUpdateCount();
            Alert alert = new Alert(AlertType.INFORMATION, "Performed " + count + " Updates");
            alert.showAndWait();
        }
        util.dbDisconnect();
        
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    public void setData()
    {
        String[] JDBC_DRIVERS = {"com.mysql.cj.jdbc.Driver"};
        String[] DB_URL = {"jdbc:mysql://localhost:3306/bikedb", "jdbc:mysql://localhost:3306/project3"};

        drivers.getItems().clear();
        URLS.getItems().clear();

        drivers.getItems().addAll(JDBC_DRIVERS[0]);
        URLS.getItems().addAll(DB_URL[0], DB_URL[1]);


    }

    
}
