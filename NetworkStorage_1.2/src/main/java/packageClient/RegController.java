package packageClient;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class RegController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(RegController.class);


    //private Network network;

    @FXML
    private TextField textFieldLogin;

    @FXML
    private TextField textFieldPass;

    @FXML
    private Button buttonSend;

    @FXML
    private Label labelRegOk;

    public Label getLabelRegOk(){
        return labelRegOk;
    }


    public void actionButtonSend() {
        LOGGER.log(Level.INFO, "/reg " + textFieldLogin.getText() + " " + textFieldPass.getText());
        Main.getNetwork().sendMessage("/reg " + textFieldLogin.getText() + " " + textFieldPass.getText());

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
