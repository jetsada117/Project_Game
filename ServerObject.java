import java.io.Serializable;

public class ServerObject implements Serializable {
    private final String [] playername = new String[4];
    private final String [] IP = new String[4];
    private final boolean [] Ready = new boolean[4];
    private final Integer [][] position = new Integer[4][100];
    private final String [][] word = new String[4][100];
    private final int [] GhostCount = new int[4];
    private final int [] y = new int[4];
    private final int [] skin = new int[4];
    private final boolean [] laser = new boolean[4];
    private final int [] score = new int[4];
    private final int [] ghostDead = new int[4];
    private String IPServer;
    private boolean isStart;
    private int index;
    private int player;
    private int count = 5;
    private int seconds = 0;
    private int minutes = 5;

    public void setName(String name, int ind) {
        this.playername[ind] = name;
    }

    public String getName(int ind) {
        return playername[ind];
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

    public void setSkin(int skin, int ind) {
        this.skin[ind] = skin;
    }

    public int getSkin(int ind) {
        return skin[ind];
    }

    public void setStart(boolean isStart) {
        this.isStart = isStart;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setPosition(int ind, int i, int position) {
        this.position[ind][i] = position;
    }

    public Integer getPosition(int ind, int i) {
        return position[ind][i];
    }

    public int sizePosition() {
        return position.length;
    }

    public void deletePosition(int ind, int i) {
        this.position[ind][i] = null;
    }

    public void setY(int ind, int number) {
        this.y[ind] =  number;
    }

    public int getY(int ind) {
        return y[ind];
    }

    public void setWord(int ind, int i, String word) {
        this.word[ind][i] = word;
    }

    public String getWord(int ind, int i) {
        return word[ind][i];
    }

    public void deleteword(int ind, int i) {
        this.position[ind][i] = null;
    }

    public void setIPServer(String IPServer) {
        this.IPServer = IPServer;
    }

    public String getIPServer() {
        return IPServer;
    }

    public void setLaser(boolean laser, int ind) {
        this.laser[ind] = laser;
    }

    public boolean isLaser(int ind) {
        return laser[ind];
    }

    public void setScore(int score, int ind) {
        this.score[ind] = score;
    }

    public int getScore(int ind) {
        return score[ind];
    }

    public void setGhostDead(int x, int ind) {
        this.score[ind] = x;
    }

    public int getGhostDead(int ind) {
        return ghostDead[ind];
    }

    public void setSizePosition(int ind, int number) {
        this.GhostCount[ind] = number;
    }

    public Integer getsizePosition(int ind) {
        return GhostCount[ind];
    }
}
