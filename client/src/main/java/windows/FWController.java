package windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class FWController {
    private Stage regStage;
    private Stage primaryStage;
    private RegController regController;
    private Stage AuthenticationWindow;
    private AuthenticationWindowController joinWindowController;

    public Stage getAuthenticationWindow() {
        return AuthenticationWindow;
    }

    public Stage getRegStage() {
        return regStage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    public void registration(ActionEvent actionEvent) {
        if (regStage == null)
            createRegWindow();
        regStage.show();
        primaryStage.hide();
    }

    private void createRegWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("regWindow.fxml"));
            Parent root = fxmlLoader.load();
            regController = fxmlLoader.getController();
            regController.setFwController(this);
            regStage = new Stage();
            regStage.setTitle("Регистрация в 'Квазимодо'");
            regStage.setScene(new Scene(root, 270, 200));
            regStage.setResizable(false);
            regStage.initStyle(StageStyle.UTILITY);
            regStage.setAlwaysOnTop(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAuthentication(ActionEvent actionEvent) {
        if (AuthenticationWindow == null)
            createAuthenticationWindow();
        AuthenticationWindow.show();
        primaryStage.hide();
    }

    private void createAuthenticationWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AuthenticationWindow.fxml"));
            Parent root = fxmlLoader.load();
            joinWindowController = fxmlLoader.getController();
            joinWindowController.setFwController(this);
            AuthenticationWindow = new Stage();
            AuthenticationWindow.setTitle("Вход в 'Квазимодо'");
            AuthenticationWindow.setScene(new Scene(root, 270,200));
            AuthenticationWindow.setResizable(false);
            AuthenticationWindow.initStyle(StageStyle.UTILITY);
            AuthenticationWindow.setAlwaysOnTop(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
