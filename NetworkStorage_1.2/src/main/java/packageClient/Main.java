package packageClient;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    private static Network network;

    public static Network getNetwork(){
        return network;
    }

    private FXMLLoader loaderWindowStorage;
    private Parent rootWindowStorage;
    private static Scene sceneWindowStorage;
    private static Controller controllerWindowStorage;
    private static Stage stageWindowStorage;

    private FXMLLoader authLoader;
    private Parent authRoot;
    private Scene authScene;
    private static AuthController authController;
    private static Stage authStage;

    private FXMLLoader loaderReg;
    private Parent rootReg;
    private Scene sceneReg;
    private static RegController regController;
    private static Stage stageReg;

    public static Scene getScene(){
        return sceneWindowStorage;
    }

    public static Stage getStageReg(){
        return  Main.stageReg;
    }
    public static Stage getStageAuth(){
        return Main.authStage;
    }
    public static AuthController getAuthController(){
        return authController;
    }
    public static Stage getStageWindowStorage(){
        return Main.stageWindowStorage;
    }
    public static RegController getRegController(){
        return regController;
    }
    public static Controller getControllerWindowStorage(){
        return controllerWindowStorage;
    }
    @Override
    public void start(Stage stage) throws Exception{
        network = new Network();

        //Основное окно
        stageWindowStorage = new Stage();
        loaderWindowStorage = new FXMLLoader(getClass().getResource("/Client.fxml"));
        rootWindowStorage = (Parent)loaderWindowStorage.load();
        sceneWindowStorage = new Scene(rootWindowStorage,300,275);
        stageWindowStorage.setScene(sceneWindowStorage);
        stageWindowStorage.setTitle("Network Storage");
        controllerWindowStorage = loaderWindowStorage.getController();

        authStage = new Stage();
        authLoader = new FXMLLoader(getClass().getResource("/Auth.fxml"));
        authRoot = (Parent)authLoader.load();
        authScene = new Scene(authRoot, 250,170);
        authStage.setScene(authScene);
        authStage.setTitle("Авторизация");
        authController = authLoader.getController();
        authStage.show();

        stageReg = new Stage();
        loaderReg = new FXMLLoader(getClass().getResource("/Reg.fxml"));
        rootReg = (Parent)loaderReg.load();
        sceneReg = new Scene(rootReg, 300, 200);
        stageReg.setScene(sceneReg);
        stageReg.setTitle("Регистрация");
        regController = loaderReg.getController();

        authStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });

        stageWindowStorage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}
