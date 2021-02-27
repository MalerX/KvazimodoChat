package windows;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader primaryFXMLLoader = new FXMLLoader(getClass().getResource("firstWindow.fxml"));
        Parent root = primaryFXMLLoader.load();
        FWController fwController = primaryFXMLLoader.getController();
        fwController.setPrimaryStage(primaryStage);
        primaryStage.setTitle("Квазимодо");
        primaryStage.setScene(new Scene(root, 250, 300));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
