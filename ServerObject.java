import java.io.Serializable;

public class ServerObject implements Serializable {
    private String playername;
    private boolean Ready = false;
    private int x;
    private int player;
    private int count = 5;
    private int seconds;
    private int minutes;

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

    public void setReady(boolean Ready) {
        this.Ready = Ready;
    }

    public boolean isReady() {
        return Ready;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public int getPlayer() {
        return player;
    }

}
