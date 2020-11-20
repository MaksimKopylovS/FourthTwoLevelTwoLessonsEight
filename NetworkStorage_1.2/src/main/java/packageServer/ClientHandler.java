package packageServer;

import io.netty.channel.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class);

    private ArrayList<ChannelHandler> listClientHandler;
    private String clientName;
    private static ArrayList<String> listName = new ArrayList<>();
    private int newClientIndex = 1;

    public void setListName(String name){
        listName.add(name);
    }



    public String getClientName(){
        return clientName;
    }

    public ClientHandler getClientHandler(){
        return this;
    }

    public void setClientName(String clientName){
        this.clientName = clientName;
    }

    public ClientHandler(){
        listClientHandler = new ArrayList<>();
        clientName = "user";
    }





    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.log(Level.INFO, "Клиент " +newClientIndex+" " + ctx + " подключился" );
        System.out.println("Клиент " +newClientIndex+" " + ctx + " подключился");
        clientName = "Клиента # " + newClientIndex;
        newClientIndex++;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        new MessageHandler(ctx, msg, this);
            }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.log(Level.INFO, cause.getLocalizedMessage());
                cause.printStackTrace();
                ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        LOGGER.log(Level.INFO, "Client "+clientName+" disconnected");
        System.out.println("Client "+clientName+" disconnected");
        deleteChannel(ctx);
        listClientHandler.remove(this);
        listName.remove(clientName);
    }

    public synchronized void saveChannel(ChannelHandlerContext ctx){
        listClientHandler.add(this);
    }

    public synchronized void deleteChannel(ChannelHandlerContext ctx){
        listClientHandler.remove(this);
        listName.remove(clientName);
    }

    public boolean checName(String name){
        for (String nick: listName  ) {
            if(nick.equals(name)){
                return false;
            }
        }
        return true;
    }

}
