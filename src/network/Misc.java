package network;

/**
 * @author alex205
 */
public class Misc {
    public enum Misc_type {
        NUDGE,
        PROFILE_PICTURE,
        TEXT_COLOR_CHANGE
    }

    private Misc_type type;
    private String data;

    public Misc(Misc_type type, String data) {
        this.type = type;
        this.data = data;
    }

    public Misc_type getType() {
        return type;
    }

    public String getData() {
        return data;
    }
}
