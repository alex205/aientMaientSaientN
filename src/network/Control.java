package network;

/**
 * @author
 */
public class Control extends Packet {
    //différent de notification car ne doit pas être utilisée en dehors de l'interface
    public enum Control_t {
        HELLO,
        ACK
    }

    private Control_t type;
    private int data;

    public Control(Control_t type, int data) {
        this.type = type;
        this.data = data;
    }

    public Control_t getType() {
        return type;
    }

    public int getData() {
        return data;
    }
}
