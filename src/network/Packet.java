package network;


import java.io.Serializable;
import java.net.InetAddress;

/**
 * @author alex205
 */
public abstract class Packet implements Serializable {
    protected String pseudoSource;
    protected String pseudoDestination;
    protected InetAddress addrSource;
    protected InetAddress addrDestination;
}
