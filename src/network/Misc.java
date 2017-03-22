package network;

/**
 * @author alex205
 */
public class Misc {
    public enum Misc_type {
        NUDGE,
        PROFILE_PICTURE
    }

    private Misc_type type;
    private byte[] data;

    public Misc(Misc_type type, byte[] data) {
        this.type = type;
        this.data = data;
    }

    public Misc_type getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }
}
