package sample;

import com.mysql.cj.protocol.Resultset;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.w3c.dom.Text;
import sample.DatabaseConnection;
import sample.Student;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClassroomController implements Initializable {

    @FXML
    private Button generateButton;

    @FXML
    private Button backButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button reloadButton;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField classTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private DatePicker dateofbirthEditPicker;

    @FXML
    private TextField courseTextField;

    @FXML
    private TextField scoreTextField;

    @FXML
    private ComboBox<String> classRoomCombobox;

    @FXML
    private ComboBox<String> examtypeNameCombobox;

    @FXML
    private ComboBox<String> courseNameCombobox;

    @FXML
    private TableView<Student> classroomTableView;

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
    private TableColumn<Student, String> courseTableColumn;

    @FXML
    private TableColumn<Student, String> examTypeTableColumn;

    @FXML
    private TableColumn<Student, String> scoreTableColumn;

    @FXML
    private TableColumn<Student, String> teacherTableColumn;


    Connection connection = null;
    DatabaseConnection databaseConnection = new DatabaseConnection();
    String query = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultset = null;
    Student student = null;

    final ObservableList optionsClass = FXCollections.observableArrayList();
    final ObservableList optionsExam = FXCollections.observableArrayList();
    final ObservableList optionsCourse = FXCollections.observableArrayList();
    ObservableList<Student> StudentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fillComboboxClass();
        fillComboboxExam();
        fillComboboxCourse();
        classRoomCombobox.setItems(optionsClass);
        examtypeNameCombobox.setItems(optionsExam);
        courseNameCombobox.setItems(optionsCourse);

    }

    public void loadClassroom() {
        connection = databaseConnection.getConnection();

        refreshClassroomTableView();

        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        classTableColumn.setCellValueFactory(new PropertyValueFactory<>("idclass"));
        lastnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        firstnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        dobTableColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        sexTableColumn.setCellValueFactory(new PropertyValueFactory<>("sex"));
        courseTableColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        examTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("exam_type"));
        scoreTableColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        teacherTableColumn.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        classroomTableView.setItems(StudentList);
    }

    public void fillComboboxClass() {
        try {
            connection = databaseConnection.getConnection();
            query = "SELECT classname FROM student_db.class;";
            preparedStatement = connection.prepareStatement(query);
            resultset = preparedStatement.executeQuery();

            while (resultset.next()) {
                optionsClass.add(resultset.getString("classname"));
            }
            preparedStatement.close();
            resultset.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            ex.getMessage();
        }
    }

    public void fillComboboxExam() {
        try {
            connection = databaseConnection.getConnection();
            query = "SELECT exam_type.name_exam FROM exam_type;";
            preparedStatement = connection.prepareStatement(query);
            resultset = preparedStatement.executeQuery();

            while (resultset.next()) {
                optionsExam.add(resultset.getString("name_exam"));
            }

            preparedStatement.close();
            resultset.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            ex.getMessage();
        }
    }

    public void fillComboboxCourse() {
        try {
            connection = databaseConnection.getConnection();
            query = "SELECT course.coursename FROM course;";
            preparedStatement = connection.prepareStatement(query);
            resultset = preparedStatement.executeQuery();

            while (resultset.next()) {
                optionsCourse.add(resultset.getString("coursename"));
            }

            preparedStatement.close();
            resultset.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            ex.getMessage();
        }
    }

    public void refreshClassroomTableView() {

        try {
            StudentList.clear();
            connection = databaseConnection.getConnection();

            query = "SELECT student_db.student.student_id, student_db.class.classname, student_db.student.lastname, " +
                    "student_db.student.firstname, student_db.student.date_of_birth, \n" +
                    "student_db.student.sex, student_db.course.coursename, student_db.exam_type.name_exam, " +
                    "student_db.exam_result.score, student_db.teacher.firstname FROM student\n" +
                    "LEFT JOIN student_db.class ON student.class_id = class.class_id\n" +
                    "LEFT JOIN student_db.exam_result ON student.student_id = exam_result.student_id\n" +
                    "LEFT JOIN student_db.course ON exam_result.course_id = course.course_id\n" +
                    "LEFT JOIN student_db.exam_type ON exam_result.exam_type_id = exam_type.exam_type_id\n" +
                    "LEFT JOIN student_db.teacher ON class.teacher_id = teacher.teacher_id\n" +
                    "WHERE class.classname = \"" + classRoomCombobox.getSelectionModel().getSelectedItem().toString() + "\"\n" +
                    "AND exam_type.name_exam = \"" + examtypeNameCombobox.getSelectionModel().getSelectedItem().toString() + "\"\n" +
                    "AND coursename = \"" + courseNameCombobox.getSelectionModel().getSelectedItem() + "\";";
            preparedStatement = connection.prepareStatement(query);
            resultset = preparedStatement.executeQuery();

            while (resultset.next()) {
                StudentList.add(new Student(
                        resultset.getInt("student_id"),
                        resultset.getString("classname"),
                        resultset.getString("lastname"),
                        resultset.getString("firstname"),
                        resultset.getDate("date_of_birth"),
                        resultset.getString("sex"),
                        resultset.getString("coursename"),
                        resultset.getString("name_exam"),
                        resultset.getInt("score"),
                        resultset.getString("teacher.firstname")
                ));
                classroomTableView.setItems(StudentList);
            }

            preparedStatement.close();
            resultset.close();


        } catch (Exception ex) {
            ex.printStackTrace();
            ex.getMessage();
        }

    }

    public void setTextFillClass(int Id, String className, String lastName, String firstName, LocalDate dob, String course, int score) {
        idTextField.setText(String.valueOf(Id));
        classTextField.setText(className);
        lastNameTextField.setText(lastName);
        firstNameTextField.setText(firstName);
        dateofbirthEditPicker.setValue(dob);
        courseTextField.setText(course);
        RestrictNumbersOnly(scoreTextField);
        scoreTextField.setText(String.valueOf(score));
    }

    public void selectedStudentFill() {
        try {
            student = classroomTableView.getSelectionModel().getSelectedItem();

            setTextFillClass(student.getId(), student.getIdclass(), student.getLastname(), student.getFirstname(), student.getDob().toLocalDate(), student.getCourse(), student.getScore());

        } catch (Exception exception) {
            exception.printStackTrace();
            exception.getMessage();
        }
    }

    public void updateScore() {
        try {

            connection = databaseConnection.getConnection();
            student = classroomTableView.getSelectionModel().getSelectedItem();

            String query = "UPDATE student_db.exam_result SET score = \"" + scoreTextField.getText() +
                    "\" WHERE student_id = \"" + idTextField.getText() +
                    "\" AND course_id = \"" + String.valueOf(courseNameCombobox.getSelectionModel().getSelectedIndex() + 1) +
                    "\" AND exam_type_id = \"" + String.valueOf(examtypeNameCombobox.getSelectionModel().getSelectedIndex() + 1) + "\";";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();

            Alert alertSucces = new Alert(Alert.AlertType.CONFIRMATION);
            alertSucces.setHeaderText("Save Score of Student");
            alertSucces.setContentText("Done!");

            ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alertSucces.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
            Optional<ButtonType> resultAlert = alertSucces.showAndWait();

            if (resultAlert.get() == buttonTypeOK) {
                alertSucces.close();
            } else {
                alertSucces.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void generateClassroom(ActionEvent event) {
        Alert alertGenerate = new Alert(Alert.AlertType.ERROR);
        alertGenerate.setHeaderText("Lỗi tạo lớp học!");

        ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cannel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType buttonTypeFinish = new ButtonType("Finished", ButtonBar.ButtonData.FINISH);
        alertGenerate.getButtonTypes().setAll(buttonTypeFinish);


        if (classRoomCombobox.getSelectionModel().isEmpty() == false
                && courseNameCombobox.getSelectionModel().isEmpty() == false
                && examtypeNameCombobox.getSelectionModel().isEmpty() == false) {

            Alert alertGeneratePrimary = new Alert(Alert.AlertType.INFORMATION);
            alertGeneratePrimary.setHeaderText("Information for Generation!");

            alertGeneratePrimary.setContentText("Tạo thành công!");

            alertGeneratePrimary.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
            Optional<ButtonType> optionalGenerate = alertGeneratePrimary.showAndWait();
            if (optionalGenerate.get() == buttonTypeOK) {
                loadClassroom();
                alertGeneratePrimary.close();
            } else {
                alertGeneratePrimary.close();
            }
        } else if (classRoomCombobox.getSelectionModel().isEmpty() == false
                && examtypeNameCombobox.getSelectionModel().isEmpty() == false
                && courseNameCombobox.getSelectionModel().isEmpty() == true) {

            alertGenerate.setContentText("Vui lòng chọn khóa học!");

            alertGenerate.getButtonTypes().setAll(buttonTypeFinish);
            Optional<ButtonType> optionalGenerate = alertGenerate.showAndWait();
            if (optionalGenerate.get() == buttonTypeFinish) {
                alertGenerate.close();
            }
        } else if (classRoomCombobox.getSelectionModel().isEmpty() == false
                && examtypeNameCombobox.getSelectionModel().isEmpty() == true
                && courseNameCombobox.getSelectionModel().isEmpty() == false) {

            alertGenerate.setContentText("Vui lòng chọn kỳ thi!");

            alertGenerate.getButtonTypes().setAll(buttonTypeFinish);
            Optional<ButtonType> optionalGenerate = alertGenerate.showAndWait();
            if (optionalGenerate.get() == buttonTypeFinish) {
                alertGenerate.close();
            }
        } else if (classRoomCombobox.getSelectionModel().isEmpty() == true
                && examtypeNameCombobox.getSelectionModel().isEmpty() == false
                && courseNameCombobox.getSelectionModel().isEmpty() == false) {

            alertGenerate.setContentText("Vui lòng chọn lớp!");

            alertGenerate.getButtonTypes().setAll(buttonTypeFinish);
            Optional<ButtonType> optionalGenerate = alertGenerate.showAndWait();
            if (optionalGenerate.get() == buttonTypeFinish) {
                alertGenerate.close();
            }
        } else if (classRoomCombobox.getSelectionModel().isEmpty() == true
                && examtypeNameCombobox.getSelectionModel().isEmpty() == true
                && courseNameCombobox.getSelectionModel().isEmpty() == false) {

            alertGenerate.setContentText("Vui lòng chọn lớp và kỳ thi!");

            alertGenerate.getButtonTypes().setAll(buttonTypeFinish);
            Optional<ButtonType> optionalGenerate = alertGenerate.showAndWait();
            if (optionalGenerate.get() == buttonTypeFinish) {
                alertGenerate.close();
            }
        } else if (classRoomCombobox.getSelectionModel().isEmpty() == false
                && examtypeNameCombobox.getSelectionModel().isEmpty() == true
                && courseNameCombobox.getSelectionModel().isEmpty() == true) {

            alertGenerate.setContentText("vui lòng chọn kỳ thi và khóa học!");

            alertGenerate.getButtonTypes().setAll(buttonTypeFinish);
            Optional<ButtonType> optionalGenerate = alertGenerate.showAndWait();
            if (optionalGenerate.get() == buttonTypeFinish) {
                alertGenerate.close();
            }
        } else if (classRoomCombobox.getSelectionModel().isEmpty() == true
                && examtypeNameCombobox.getSelectionModel().isEmpty() == false
                && courseNameCombobox.getSelectionModel().isEmpty() == true) {

            alertGenerate.setContentText("Vui lòng chọn lớp và khóa học!");

            alertGenerate.getButtonTypes().setAll(buttonTypeFinish);
            Optional<ButtonType> optionalGenerate = alertGenerate.showAndWait();
            if (optionalGenerate.get() == buttonTypeFinish) {
                alertGenerate.close();
            }
        } else if (classRoomCombobox.getSelectionModel().isEmpty() == true
                && examtypeNameCombobox.getSelectionModel().isEmpty() == true
                && courseNameCombobox.getSelectionModel().isEmpty() == true) {

            alertGenerate.setContentText("Vui lòng chọn lớp, khóa học và kỳ thi!");

            alertGenerate.getButtonTypes().setAll(buttonTypeFinish);
            Optional<ButtonType> optionalGenerate = alertGenerate.showAndWait();
            if (optionalGenerate.get() == buttonTypeFinish) {
                alertGenerate.close();
            }
        }
    }

    public void backHomeForm(ActionEvent event) {
        Stage stageBack = (Stage) backButton.getScene().getWindow();
        stageBack.close();
    }

    public void Key_Handle_Back(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
        }
    }
    public void RestrictNumbersOnly(TextField tf){
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("[0-9]|1[0.0-0.0]")){
                    tf.setText(oldValue);
                }
            }
        });
    }
}
