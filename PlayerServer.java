import java.io.Serializable;

public class PlayerServer implements Serializable {
    private String playername;
    private boolean Ready = false;
    private int x;
    private String ip;
    private int count = 5;

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

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
