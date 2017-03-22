package network;

/**
 * @author alex205
 */
public final class Text extends Message {
    private String data;

    public Text(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
