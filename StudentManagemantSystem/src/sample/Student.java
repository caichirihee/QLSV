package sample;

import javafx.scene.control.RadioButton;

import java.sql.Connection;
import java.sql.Date;

public class Student {
    int id, score;
    String idclass;

    String lastname;

    String firstname;
    String sex;
    String email;
    String address;
    String phonenumber;
    String course;
    String exam_type;
    String teacher;
    Date dob;

    public Student(int id, String lastname, String firstname, Date dob, String sex,
                   String email, String address, String phonenumber, String idclass) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.dob = dob;
        this.sex = sex;
        this.email = email;
        this.address = address;
        this.phonenumber = phonenumber;
        this.idclass = idclass;
    }

    public Student(int id, String idclass, String lastname, String firstname, Date dob, String sex, String course, String exam_type, int score, String teacher) {
        this.id = id;
        this.idclass = idclass;
        this.lastname = lastname;
        this.firstname = firstname;
        this.dob = dob;
        this.sex = sex;
        this.course = course;
        this.exam_type = exam_type;
        this.score = score;
        this.teacher = teacher;
    }
    public String getIdclass() {
        return idclass;
    }

    public void setIdclass(String idclass) {
        this.idclass = idclass;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getExam_type() {
        return exam_type;
    }

    public void setExam_type(String exam_type) {
        this.exam_type = exam_type;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
