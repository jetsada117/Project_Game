import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private int x;
    private boolean Ready = false;

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
} 
