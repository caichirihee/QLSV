package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditStudentController extends StudentController implements Initializable {

    @FXML
    private TextField firstNameEditField;

    @FXML
    private TextField lastNameEditField;

    @FXML
    private DatePicker dateofbirthEditPicker;

    @FXML
    private RadioButton maleRadioButton;

    @FXML
    private RadioButton femaleRadioButton;

    @FXML
    private RadioButton otherRadioButton;

    @FXML
    private ToggleGroup sexEdit;

    @FXML
    private TextField emailEditField;

    @FXML
    private TextField addressEditField;

    @FXML
    private TextField phoneEditField;

    @FXML
    public ComboBox<String> classComboBox;

    @FXML
    private Button updateButton;

    @FXML
    private Button cleanButton;

    String query = null;
    DatabaseConnection databaseConnection = new DatabaseConnection();
    Connection connection = null;
    ResultSet resultset = null;
    PreparedStatement preparedStatement = null;
    static String sexradio;

    ObservableList<String> listEditClass = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fillComboBoxChanged();
        classComboBox.setItems(listEditClass);
    }

    public void fillComboBoxChanged() {
        try {
            connection = databaseConnection.getConnection();
            query = "SELECT classname FROM student_db.class;";
            preparedStatement = connection.prepareStatement(query);
            resultset = preparedStatement.executeQuery();

            while (resultset.next()) {
                listEditClass.add(resultset.getString("classname"));
            }
            preparedStatement.close();
            resultset.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            ex.getMessage();
        }
    }

    public void Key_Handle_Back(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            Stage stage = (Stage) cleanButton.getScene().getWindow();
            stage.close();
        }
    }

    public void setTextFieldStudent(String lastName, String firstName, LocalDate toLocalDate, String email, String address, String phone) {
        lastNameEditField.setText(lastName);
        firstNameEditField.setText(firstName);
        dateofbirthEditPicker.setValue(toLocalDate);
        emailEditField.setText(email);
        addressEditField.setText(address);
        phoneEditField.setText(phone);
    }

    public void tableAllFillAndUpdate(ActionEvent event) {
        connection = databaseConnection.getConnection();

        String firstName = firstNameEditField.getText();
        String lastName = lastNameEditField.getText();
        String dob = String.valueOf(dateofbirthEditPicker.getValue());
        if (maleRadioButton.isSelected()) {
            String sex = maleRadioButton.getText();
        } else if (femaleRadioButton.isSelected()) {
            String sex = femaleRadioButton.getText();
        } else {
            String sex = otherRadioButton.getText();
        }
        String email = emailEditField.getText();
        String address = addressEditField.getText();
        String phone = phoneEditField.getText();

        Alert alertStudent = new Alert(Alert.AlertType.ERROR);
        alertStudent.setHeaderText("Save Student!!!");
        alertStudent.setContentText("Please fill all data!");

        if (firstName == null && lastName == null && email == null && address == null && phone == null){
            alertStudent.showAndWait();
        } else if (firstName.isBlank() == true && lastName.isBlank() == true && dob.isBlank() == true
                && email.isBlank() == true && address.isBlank() == true
                && phone.isBlank() == true) {
            alertStudent.showAndWait();
        }else {
            updateStudent();
        }
    }

    public void updateStudent() {
        try {

            connection = databaseConnection.getConnection();
            String lastname = lastNameEditField.getText();
            String firstname = firstNameEditField.getText();
            String dob = String.valueOf(dateofbirthEditPicker.getValue());
            String sex;
            if (maleRadioButton.isSelected()) {
                sex = maleRadioButton.getText();
            } else if (femaleRadioButton.isSelected()) {
                sex = femaleRadioButton.getText();
            } else {
                sex = otherRadioButton.getText();
            }
            String email = emailEditField.getText();
            String address = addressEditField.getText();
            String phone = phoneEditField.getText();
            String classId = String.valueOf(classComboBox.getSelectionModel().getSelectedIndex() + 1);
            String query = "UPDATE student_db.student SET "
                    + "lastname = '" + lastname
                    + "', firstname = '" + firstname
                    + "', date_of_birth = '" + dob
                    + "', sex = '" + sex
                    + "', email = '" + email
                    + "', address = '" + address
                    + "', phonenumber = '" + phone
                    + "', class_id = '" + classId
                    + "' WHERE student_id = '" + studentId + "';";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();

            Alert alertSucces = new Alert(Alert.AlertType.CONFIRMATION);
            alertSucces.setHeaderText("Save Information Student");
            alertSucces.setContentText("Done!");

            ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alertSucces.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
            Optional<ButtonType> resultAlert = alertSucces.showAndWait();

            Stage stageStudent = (Stage) updateButton.getScene().getWindow();
            if (resultAlert.get() == buttonTypeOK) {
                stageStudent.close();
            } else {
                stageStudent.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cleanStudent(ActionEvent event){
        firstNameEditField.setText(null);
        lastNameEditField.setText(null);
        dateofbirthEditPicker.setValue(null);
        emailEditField.setText(null);
        addressEditField.setText(null);
        phoneEditField.setText(null);
        classComboBox.getSelectionModel().clearSelection();
    }
}
