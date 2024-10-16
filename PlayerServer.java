import java.io.Serializable;

public class PlayerServer implements Serializable {
    private String playername;
    private int x;

    public void setName(String name) {
        this.playername = name;
    }
    
    public String getName() {
        return playername;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }
}
