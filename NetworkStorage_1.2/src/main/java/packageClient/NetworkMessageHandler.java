package packageClient;

import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import packageMessage.FileMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class NetworkMessageHandler {
    private static String msg;
    private static String userName = null;
    private static final Logger LOGGER = Logger.getLogger(NetworkMessageHandler.class);
    private static boolean authOK = false;
    private static String userPath = "Client";

    public static boolean getAuthOk(){
        return authOK;
    }


    public static void setUserName(String userName) {
        NetworkMessageHandler.userName = userName;
    }
    public static String getUserName(){
        return userName;
    }

    public static String getMsg(){
        return msg;
    }

    public NetworkMessageHandler(ChannelHandlerContext ctx, Object msg) throws IOException {

        if (!Files.exists(Paths.get(userPath))) {
            try {
                Files.createDirectory(Paths.get(userPath));
            } catch (IOException ioException) {
                LOGGER.log(Level.INFO, ioException.getMessage());
                ioException.printStackTrace();

            }
        }

        if(msg instanceof String){
            this.msg = (String)msg;

            if (this.msg.startsWith("/")) {

                if (this.msg.startsWith("/auth ")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            LOGGER.log(Level.INFO, getMsg().substring(5) );
                            Main.getAuthController().getLabelInfo().setText(getMsg().substring(5));
                        }
                    });
                }

                if (this.msg.startsWith("/reg")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            LOGGER.log(Level.INFO, getMsg().substring(4) );
                            Main.getRegController().getLabelRegOk().setText(getMsg().substring(4));
                        }
                    });

                }
                if(this.msg.startsWith("/name")){
                    LOGGER.log(Level.INFO, "User Name: "+ this.msg.split(" ", 4)[1]);
                    System.out.println("User Name: "+ this.msg.split(" ", 4)[1]);
                    authOK = true;

                    setUserName(this.msg.split(" ", 4)[1]);
                    Main.getControllerWindowStorage().getListView().getItems().add(getMsg().substring(5));
                }

            }else {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        LOGGER.log(Level.INFO,"Полученное сообщение: " + getMsg() );
                        Main.getControllerWindowStorage().getListView().getItems().add(getMsg());
                    }
                });
            }

        }

        if(msg instanceof FileMessage){
            FileMessage fileMessage = (FileMessage)msg;
            Files.write(Paths.get("Client/" + fileMessage.getFileName()), fileMessage.getData(), StandardOpenOption.CREATE);


            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    LOGGER.log(Level.INFO, "Фаил "+ "Client/"+fileMessage.getFileName() + "сохранен");
                    Main.getControllerWindowStorage().getListView().getItems().add("Client/"+fileMessage.getFileName() + "сохранен");
                }
            });
        }
    }

}
