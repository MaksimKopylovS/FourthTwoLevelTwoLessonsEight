package packageMessage;



import java.io.Serializable;

public class MyMessage implements Message {
    private static final long serializableVersionUID = 5193392663743561680L;

    private String fileName;

    public String getText(){
        return fileName;
    }

    public MyMessage(String fileName){
        this.fileName = fileName;
    }

}
