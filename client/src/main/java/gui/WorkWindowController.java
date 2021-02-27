package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WorkWindowController {
    @FXML
    private ListView<String> clientList;
    @FXML
    private TextField chatTextField;

    public void setChatTextArea(TextArea chatTextArea) {
        this.chatTextArea = chatTextArea;
    }

    @FXML
    private TextArea chatTextArea;

    private Stage chatStage;

    public void setChatStage(Stage chatStage) {
        this.chatStage = chatStage;
    }

    public void sendMessage(ActionEvent actionEvent) {

    }

    public void setClientList(ListView<String> clientList) {
        this.clientList = clientList;
    }

    public void setChatTextField(TextField chatTextField) {
        this.chatTextField = chatTextField;
    }

    public void exit(ActionEvent actionEvent) {
        chatStage.close();
    }
}
