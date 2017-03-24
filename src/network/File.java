package network;

import java.net.InetAddress;

/**
 * @author alex205
 */
public class File extends Message {
    private String fileName;
    private String mimeType;
    private double size;
    private byte[] content;

    public File(String pseudoSource, String pseudoDestination, InetAddress addrSource, InetAddress addrDestination, String fileName, String mimeType, double size, byte[] content) {
        super(pseudoSource, pseudoDestination, addrSource, addrDestination);
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.size = size;
        this.content = content;
    }
}
