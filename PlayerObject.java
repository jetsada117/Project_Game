import java.io.Serializable;

public class PlayerObject implements Serializable {
    private String name;
    private int x;
    private boolean Ready = false;
    private int skin; 
    private int count;
    private int seconds;
    private int minutes;

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

    public void setCount(int count) {
        this.count = count;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getCount() {
        return count;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setSkin(int skin) {
        this.skin = skin;
    }

    public int getSkin() {
        return skin;
    }

} 
