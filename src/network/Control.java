package network;

/**
 * @author
 */
public class Control extends Packet {

    private int data;

    public Control(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }
}
