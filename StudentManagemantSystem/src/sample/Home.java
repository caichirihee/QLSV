package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Home implements Initializable {
    @FXML
    private ImageView homeImageView;

    @FXML
    private ImageView bookImageView;

    @FXML
    private ImageView studentImageView;

    @FXML
    private ImageView classesImageView;

    @FXML
    private ImageView updateImageView;

    @FXML
    private ImageView settingImageView;

    @FXML
    private Button closeButton;

    @FXML
    private Button studentButton;

    @FXML
    private ImageView closeImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File bookFile = new File("Images/icons8_logo.png");
        Image bookImage = new Image(bookFile.toURI().toString());
        bookImageView.setImage(bookImage);

        File studentFile = new File("Images/student.png");
        Image studentImage = new Image(studentFile.toURI().toString());
        studentImageView.setImage(studentImage);

        File classesFile = new File("Images/classes.png");
        Image classesImage = new Image(classesFile.toURI().toString());
        classesImageView.setImage(classesImage);

        File updateFile = new File("Images/update.png");
        Image updateImage = new Image(updateFile.toURI().toString());
        updateImageView.setImage(updateImage);

//        File settingFile = new File("Images/settings.png");
//        Image settingImage = new Image(settingFile.toURI().toString());
//        settingImageView.setImage(settingImage);

        File closeFile = new File("Images/close.png");
        Image closeImage = new Image(closeFile.toURI().toString());
        closeImageView.setImage(closeImage);

    }


    public void closeHomeOnAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        Platform.exit();
    }

    public void StudentActionForm(ActionEvent event) {
        createStudentForm();
    }

    public void createStudentForm() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("studentForm.fxml"));
            Stage stageStudentForm = new Stage();
            stageStudentForm.initStyle(StageStyle.UNDECORATED);
            Scene sceneStudent = new Scene(root, 1300, 800);
            sceneStudent.getStylesheets().add("css/style.css");
            stageStudentForm.setScene(sceneStudent);
            stageStudentForm.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Key_Handle_Back(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        }
    }

    public void ClassroomOnAction(ActionEvent event) {
        creatClassroomForm();
    }

    public void creatClassroomForm() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("classroom.fxml"));
            Stage stageClassroom = new Stage();
            stageClassroom.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root, 1300, 800);
            scene.getStylesheets().add("css/style.css");
            stageClassroom.setScene(scene);
            stageClassroom.show();

        } catch (Exception ex) {
            ex.printStackTrace();
            ex.getMessage();
        }
    }
}
