package windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthenticationWindowController {

    public void setFwController(FWController fwController) {
        this.fwController = fwController;
    }

    private FWController fwController;

    public void getAuthentication(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("workWindow.fxml"));
            Parent root = fxmlLoader.load();
            Stage workWindow = new Stage();
            WorkWindowController workWindowController = fxmlLoader.getController();
            workWindowController.setChatStage(workWindow);
            workWindow.setScene(new Scene(root,640,480));
            workWindow.setResizable(false);
            fwController.getAuthenticationWindow().close();
            workWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backToTheFW(ActionEvent actionEvent) {
        fwController.getAuthenticationWindow().close();
        fwController.getPrimaryStage().show();
    }
}
