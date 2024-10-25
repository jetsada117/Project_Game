import java.io.Serializable;
import java.util.ArrayList;

public class ServerObject implements Serializable {
    private final String[] playername = new String[4];
    private final String[] IP = new String[4];
    private final boolean[] Ready = new boolean[4];
    private final ArrayList<Integer>[] position = new ArrayList[4];
    private final ArrayList<String>[] word = new ArrayList[4];
    private final int[] y = new int[4];
    private final int[] skin = new int[4];
    private boolean isStart;
    private int index;
    private int player;
    private int count;
    private int seconds = 3;
    private int minutes = 0;

    public ServerObject() {
        for (int i = 0; i < position.length; i++) {
            position[i] = new ArrayList<>();
            word[i] = new ArrayList<>();
        }
    }

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

    public void setIsStart(boolean isStart) {
        this.isStart = isStart;
    }

    public boolean isIsStart() {
        return isStart;
    }

    public void setPosition(int ind, int i, int position) {
        this.position[ind].set(i, position);
    }

    public void addPosition(int ind, int positionX, int positionY) {
        this.position[ind].add(positionX);
        this.y[ind] = positionY;
    }

    public Integer getPosition(int ind, int i) {
        return position[ind].get(i);
    }

    public boolean hasPosition(int ind) {
        return !position[ind].isEmpty();
    }

    public int sizePosition(int ind) {
        return position[ind].size();
    }

    public int sizePosition() {
        return position.length;
    }

    public void deletePosition(int ind, int i) {
        this.position[ind].set(i, null);
    }

    public int getY(int ind) {
        return y[ind];
    }

    public void setWord(int ind, String word) {
        this.word[ind].add(word);
    }

    public String getWord(int ind, int i) {
        return word[ind].get(i);
    }

    public void deleteword(int ind, int i) {
        position[ind].set(ind, null);
    }

    public boolean containWord(int ind, String wordString) {
        return word[ind].contains(wordString);
    }
}
