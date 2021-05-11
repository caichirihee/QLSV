package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentController implements Initializable {

    @FXML
    public Button backButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private TextField searchStudentButton;

    @FXML
    private TableView<Student> studentTableView;

    @FXML
    private TableColumn<Student, String> idTableColumn;

    @FXML
    private TableColumn<Student, String> classTableColumn;

    @FXML
    private TableColumn<Student, String> lastnameTableColumn;

    @FXML
    private TableColumn<Student, String> firstnameTableColumn;

    @FXML
    private TableColumn<Student, String> dobTableColumn;

    @FXML
    private TableColumn<Student, String> sexTableColumn;

    @FXML
    private TableColumn<Student, String> emailTableColumn;

    @FXML
    private TableColumn<Student, String> addressTableColumn;

    @FXML
    private TableColumn<Student, String> phoneTableColumn;


    String query = null;
    DatabaseConnection databaseConnection = new DatabaseConnection();
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Student student = null;
    ObservableList<Student> StudentList = FXCollections.observableArrayList();
    public static int studentId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDate();
    }

    private void loadDate() {
        connection = databaseConnection.getConnection();

        refreshTableView();

        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        lastnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        firstnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        dobTableColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        sexTableColumn.setCellValueFactory(new PropertyValueFactory<>("sex"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressTableColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneTableColumn.setCellValueFactory(new PropertyValueFactory<>("phonenumber"));
        classTableColumn.setCellValueFactory(new PropertyValueFactory<>("idclass"));
        studentTableView.setItems(StudentList);
    }

    public void deleteStudent(ActionEvent event) {
        try {
            student = studentTableView.getSelectionModel().getSelectedItem();

            query = "DELETE FROM student_db.exam_result WHERE student_id = "+ student.getId() + ";";
            connection = databaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();

            query = "DELETE FROM student_db.student WHERE student_id = "+ student.getId() + ";";
            connection = databaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            refreshTableView();

            preparedStatement.close();
        } catch (SQLException ex) {
            Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean deleteStudentTest(int id) throws SQLException {
        boolean kq = true;
        connection = databaseConnection.getConnection();
        String sql = "SELECT * FROM student_db.student WHERE student_id = ?;";

        preparedStatement= connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            kq = false;
        }
        return kq;
    }

    public void editStudent(ActionEvent event) {
        try{
            student = studentTableView.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("editstudent.fxml"));

            try {
                loader.load();
            } catch (Exception exception){
                exception.getMessage();
            }

            EditStudentController editStudentController = loader.getController();
            editStudentController.setTextFieldStudent(student.getLastname(),student.getFirstname(),
                    student.getDob().toLocalDate(),
                    student.getEmail(), student.getAddress(), student.getPhonenumber());

            studentId = student.getId();
            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            Scene sceneEdit = new Scene(parent);
            sceneEdit.getStylesheets().add("/css/style.css");
            stage.setScene(sceneEdit);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public void searchStudent() {
        connection = databaseConnection.getConnection();

        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        lastnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        firstnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        dobTableColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        sexTableColumn.setCellValueFactory(new PropertyValueFactory<>("sex"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressTableColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneTableColumn.setCellValueFactory(new PropertyValueFactory<>("phonenumber"));
        classTableColumn.setCellValueFactory(new PropertyValueFactory<>("idclass"));
        studentTableView.setItems(StudentList);

        FilteredList<Student> filteredStudent = new FilteredList<>(StudentList, b -> true);
        searchStudentButton.textProperty().addListener(((observableValue, oldStudent, newStudent) -> {
            filteredStudent.setPredicate(person -> {
                if (oldStudent == null || newStudent.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newStudent.toLowerCase();
                if (person.getLastname().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches lastname
                } else if (person.getFirstname().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Fileter matches firstname
                } else if (person.getIdclass().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (person.getSex().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (person.getAddress().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (person.getEmail().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false; // Does not match.
                }
            });
        }));
        SortedList<Student> sortedStudent = new SortedList<>(filteredStudent);
        sortedStudent.comparatorProperty().bind(studentTableView.comparatorProperty());
        studentTableView.setItems(sortedStudent);
    }

    public void backActionForm(ActionEvent event) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    public void Key_Handle_Back(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
        }
    }

    public void refreshTableView() {
        try {

            StudentList.clear();
            query = "SELECT student.student_id, student.lastname, student.firstname, student.date_of_birth, student.sex," +
                    " student.email, student.address, student.phonenumber, class.classname \n" +
                    "FROM student_db.student, student_db.class\n" +
                    "WHERE student.class_id = class.class_id;";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                StudentList.add(new Student(
                        resultSet.getInt("student_id"),
                        resultSet.getString("lastname"),
                        resultSet.getString("firstname"),
                        resultSet.getDate("date_of_birth"),
                        resultSet.getString("sex"),
                        resultSet.getString("email"),
                        resultSet.getString("address"),
                        resultSet.getString("phonenumber"),
                        resultSet.getString("classname")
                ));
                studentTableView.setItems(StudentList);
            }
            preparedStatement.close();
            resultSet.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void getStudentForm(ActionEvent event) {
        createAddStudent();
    }

    public void createAddStudent() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("addstudent.fxml"));
            Stage stageStudent = new Stage();
            stageStudent.initStyle(StageStyle.UTILITY);
            Scene sceneStudent = new Scene(root, 608, 700);
            sceneStudent.getStylesheets().add("css/style.css");
            stageStudent.setScene(sceneStudent);
            stageStudent.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
