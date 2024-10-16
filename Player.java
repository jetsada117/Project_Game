import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private int x;

    public Player(String name) {
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
} 
