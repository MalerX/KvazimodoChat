package windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

public class RegController {
    @FXML
    private TextField loginField;
    @FXML
    private TextField nick;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordConfirmField;
    @FXML

    private FWController fwController;

    public void setFwController(FWController fwController) {
        this.fwController = fwController;
    }

    public void registration(ActionEvent actionEvent) {
        StringBuilder login = new StringBuilder(loginField.getText().trim());
        StringBuilder nickname = new StringBuilder(nick.getText().trim());
        StringBuilder password = new StringBuilder();
        if (passwordField.getText().trim().equals(passwordConfirmField.getText().trim()))
            password.append(passwordField.getText().trim());
        else {
            Alert notEqualsPass = new Alert(Alert.AlertType.WARNING);
            notEqualsPass.setTitle("Внимание!");
            notEqualsPass.setHeaderText(null);
            notEqualsPass.setContentText("Введённые пароли не совпадают");
            notEqualsPass.initModality(Modality.WINDOW_MODAL);
            notEqualsPass.initStyle(StageStyle.UTILITY);
            notEqualsPass.showAndWait();
        }
    }

    public void backToTheFW(ActionEvent actionEvent) {
        fwController.getRegStage().close();
        fwController.getPrimaryStage().show();
    }
}
