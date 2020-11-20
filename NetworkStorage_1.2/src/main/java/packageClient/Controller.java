package packageClient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.List;

public class Controller implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(Controller.class);


    @FXML
    TextField textFieldMessage;

    @FXML
    ListView listView;


    private String path = null;

    public void setPath(String path){
        this.path = path;
    }

    public ListView getListView() {
        return listView;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setCellFactory(TextFieldListCell.forListView());
        listView.setEditable(true);
    }

    public void actionWhere(){
        Main.getNetwork().sendMessage("/whoodir ");
    }


    public void menuItemActionSendFile() {
        //Отправка файла маленького размера
        File file = new FileChooser().showOpenDialog(Main.getStageWindowStorage());
        if (file != null) {
            List<File> listFile = Arrays.asList(file);
            LOGGER.log(Level.INFO, "File Send: " + file);
            System.out.println(file);
            Main.getNetwork().sendFile(file.getPath());
        }
    }

    public void sendMessageAction(ActionEvent event) throws IOException {
        //Отправка текстового сообщения
        if(textFieldMessage.getText().equals("/clear")){
            listView.getItems().clear();
        }else {
            LOGGER.log(Level.INFO, "Отправленное сообщение: " +  textFieldMessage.getText());
            Main.getNetwork().sendMessage(textFieldMessage.getText());
        }
    }


    public void actionApdate(){
        Main.getNetwork().sendMessage("/info " + NetworkMessageHandler.getUserName());
        listView.getItems().clear();
    }
}
