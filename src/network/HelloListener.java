package network;

import model.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;

import static network.NetworkInterface.basePort;

/**
 * Manage all control packets
 *
 * @author alex205
 */
public class HelloListener extends DatagramListener implements Observable {

    private ArrayList<Observer> observers;

    public HelloListener(DatagramSocket socket) {
        super(socket);
        this.observers = new ArrayList<>();
    }

    @Override
    protected void managePacket(Packet p) {
        NetworkInterface ni = NetworkInterface.getInstance();
        ContactCollection cc = ContactCollection.getInstance();
        if(p instanceof Control) {
            Control c = (Control) p;
            System.out.println("RECEIVED, le port :" + c.getData());
            try {
                if(c.getType() == Control.Control_t.HELLO || c.getType() == Control.Control_t.ACK) {
                    //Gestion du socket de conversation
                    System.out.println("l'ip de " + c.getPseudoSource() + " " + c.getAddrSource().toString());
                    ni.addMap(c.getPseudoSource() + "@" + c.getAddrSource().toString(), c.getData());
                    System.out.println("le pseudo dans la table " + c.getPseudoSource() + "@" + c.getAddrSource().toString());
                    ni.fireUpdate();
                    if (c.getType() == Control.Control_t.HELLO) {
                        ServerSocket com = new ServerSocket(basePort);
                        CommunicationListener listener = new CommunicationListener(com);
                        listener.start();
                        ni.addListener(basePort, listener);
                        System.out.println("listener lancé");
                        ni.sendControl(ContactCollection.getMe(), new Contact(c.getPseudoSource(), c.getAddrSource()), Control.Control_t.ACK, basePort);
                        basePort++;
                        notifyObservers();
                    }
                } else if(c.getType() == Control.Control_t.TMP_SOCKET || c.getType() == Control.Control_t.TMP_SOCKET_ACK) {
                    //Gestion de la demande de socket temporaire
                    System.out.println("on demande un socket temporaire");
                    ni.addTmpMap(c.getPseudoSource() + "@" + c.getAddrSource().toString(), c.getData());
                    ni.fireUpdate();
                    System.out.println("c'est à jour");
                    if(c.getType() == Control.Control_t.TMP_SOCKET) {
                        ServerSocket com = new ServerSocket(basePort);
                        CommunicationListener listener = new CommunicationListener(com);
                        listener.start();
                        ni.addListener(basePort, listener);
                        ni.sendControl(ContactCollection.getMe(), cc.getContact(c.getPseudoSource() + "@" + c.getAddrSource()), Control.Control_t.TMP_SOCKET_ACK, basePort);
                        basePort++;
                    }
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for(int i=0;i<observers.size();i++)
        {
            Observer o = (Observer) observers.get(i);
            o.update(this);// On utilise la méthode "tiré".
        }
    }
}
