import java.io.Serializable;

public class ServerObject implements Serializable {
    private final String [] playername = new String[4];
    private final String [] IP = new String[4];
    private final boolean [] Ready = new boolean[4];
    private int [] x = new int[4];
    private int index;
    private int player;
    private int count = 5;
    private int seconds;
    private int minutes;

    public void setName(String name, int ind) {
        this.playername[ind] = name;
    }
    
    public String getName(int ind) {
        return playername[ind];
    }

    public void setX(int x, int ind) {
        this.x[ind] = x;
    }

    public int getX(int ind) {
        return x[ind];
    }

    public void setReady(boolean Ready, int ind) {
        this.Ready[ind] = Ready;
    }

    public boolean isReady(int ind) {
        return Ready[ind];
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

    public void setIP(String IP, int ind) {
        this.IP[ind] = IP;
    }

    public String getIP(int ind) {
        return IP[ind];
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

}
