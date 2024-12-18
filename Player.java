import java.net.InetAddress;

public class Player {
    private InetAddress address;
    private int port;
    private String personaggio;
    private boolean ready;
    private float x;
    private float y;

    public Player(InetAddress address, int port, float x, float y) {
        this.address = address;
        this.port = port;
        this.ready =false;
        this.personaggio = null;
        this.x = x;
        this.y = y;
    }
    public String getId() {
        return Server.md5(address.toString()+":"+port);
    }
    public String toCSV() { 
        return getId()+";"+x+";"+y+";"+personaggio;
    }
    public String toString() { 
        return address.toString()+":"+port+" " +ready;
    }
    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    public String getPersonaggio() {
        return personaggio;
    }
    public void setPersonaggio(String personaggio) {
        this.personaggio = personaggio;
    }
    public boolean isReady() {
        return this.ready;
    }
    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
