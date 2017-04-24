package model;

import java.net.InetAddress;

/**
 * @author alex205
 */
public final class Text extends Message {
    private String data;

    public Text(String pseudoSource, String pseudoDestination, InetAddress addrSource, InetAddress addrDestination, String data) {
        super(pseudoSource, pseudoDestination, addrSource, addrDestination);
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
