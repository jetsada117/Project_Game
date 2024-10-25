import java.io.Serializable;

public class PlayerObject implements Serializable {
    private String name;
    private int x;
    private boolean Ready = false;
    private int skin; 

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setReady(boolean Ready) {
        this.Ready = Ready;
    }

    public boolean isReady() {
        return Ready;
    }

    public void setSkin(int skin) {
        this.skin = skin;
    }

    public int getSkin() {
        return skin;
    }

} 
