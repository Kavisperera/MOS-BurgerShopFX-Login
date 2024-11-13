package org.example.controller.burgershopmanagement;

import db.DBConnection;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HelloController {

    @FXML
    private Hyperlink si_forgotPassword;

    @FXML
    private Button si_loginBtn;

    @FXML
    private AnchorPane si_loginForm;

    @FXML
    private PasswordField si_password;

    @FXML
    private TextField si_username;

    @FXML
    private Button side_createBtn;

    @FXML
    private AnchorPane side_form;

    @FXML
    private TextField su_username;

    @FXML
    private TextField su_answer;

    @FXML
    private PasswordField su_password;

    @FXML
    private ComboBox<String> su_question;

    @FXML
    private Button su_signupBtn;

    @FXML
    private AnchorPane su_signupForm;

    @FXML
    private Button side_alreadyHave;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Alert alert;

    public HelloController() {
    }

    // Registration Account
    public void regBtn() {
        if (su_username.getText().isEmpty() || su_password.getText().isEmpty()
                || su_question.getSelectionModel().getSelectedItem() == null
                || su_answer.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();

        } else if (su_password.getText().length() < 6) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Password: at least 6 characters required");
            alert.showAndWait();

        } else {
            String regData = "INSERT INTO employee(Username, Password, Question, Answer, Date) VALUES(?, ?, ?, ?, ?)";
            try {
                // CHECK IF THE USERNAME IS ALREADY TAKEN
                String checkUsername = "SELECT Username FROM employee WHERE Username = ?";
                connect = DBConnection.getInstance().getConnection();

                if (connect != null) {
                    prepare = connect.prepareStatement(checkUsername);
                    prepare.setString(1, su_username.getText());
                    result = prepare.executeQuery();

                    if (result.next()) {
                        // Username is already taken
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText(su_username.getText() + " is already taken");
                        alert.showAndWait();
                    } else {
                        // Register the new user
                        prepare = connect.prepareStatement(regData);
                        prepare.setString(1, su_username.getText());
                        prepare.setString(2, su_password.getText());
                        prepare.setString(3, su_question.getSelectionModel().getSelectedItem());
                        prepare.setString(4, su_answer.getText());

                        Date date = new Date();
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                        prepare.setDate(5, sqlDate);

                        int rowsAffected = prepare.executeUpdate();
                        if (rowsAffected > 0) {
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Successfully registered Account!");
                            alert.showAndWait();
                        } else {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Registration failed.");
                            alert.showAndWait();
                        }

                        // Clear form fields
                        su_username.setText("");
                        su_password.setText("");
                        su_question.getSelectionModel().clearSelection();
                        su_answer.setText("");

                        // Slide transition after successful registration
                        TranslateTransition slider = new TranslateTransition();
                        slider.setNode(side_form);
                        slider.setToX(0);
                        slider.setOnFinished(e -> {
                            side_alreadyHave.setVisible(false);
                            side_createBtn.setVisible(true);
                        });
                        slider.play();
                    }
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Database Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Could not connect to the database.");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred: " + e.getMessage());
                alert.showAndWait();
            } finally {
                if (connect != null) {
                    try {
                        connect.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // Login Account
    public void loginBtn(ActionEvent event) {
        String loginQuery = "SELECT * FROM employee WHERE Username = ? AND Password = ?";

        try {
            connect = DBConnection.getInstance().getConnection();

            if (connect != null) {
                prepare = connect.prepareStatement(loginQuery);
                prepare.setString(1, si_username.getText());
                prepare.setString(2, si_password.getText());

                result = prepare.executeQuery();

                if (result.next()) {
                    // Login successful
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Login Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Logged in!");
                    alert.showAndWait();
                } else {
                    // Invalid username or password
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid Username or Password");
                    alert.showAndWait();
                }
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText(null);
                alert.setContentText("Could not connect to the database.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred: " + e.getMessage());
            alert.showAndWait();
        } finally {
            if (connect != null) {
                try {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private final String[] questionList = {"What is your favourite Color?", "What is your favourite food?", "What is your birth date?"};

    public void regLquestionList() {
        List<String> listQ = new ArrayList<>();

        for (String data : questionList) {
            listQ.add(data);
        }
        ObservableList<String> listData = FXCollections.observableArrayList(listQ);
        su_question.setItems(listData);
    }

    // Switch between forms
    public void switchForm(ActionEvent event) {
        TranslateTransition slider = new TranslateTransition();
        slider.setDuration(Duration.seconds(0.5));

        if (event.getSource() == side_createBtn) {
            slider.setNode(side_form);
            slider.setToX(300);
            slider.setOnFinished(e -> {
                side_alreadyHave.setVisible(true);
                side_createBtn.setVisible(false);

                regLquestionList();
            });
            slider.play();

        } else if (event.getSource() == side_alreadyHave) {
            slider.setNode(side_form);
            slider.setToX(0);
            slider.setOnFinished(e -> {
                side_alreadyHave.setVisible(false);
                side_createBtn.setVisible(true);
            });
            slider.play();
        }
    }

}
