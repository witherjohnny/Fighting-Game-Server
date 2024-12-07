import java.net.InetAddress;

public class Player {
    private String id;
    private InetAddress address;
    private int port;
    private float x;
    private float y;

    public Player(InetAddress address, int port, float x, float y) {
        this.address = address;
        this.port = port;
        this.id = address.toString()+port;
        this.x = x;
        this.y = y;
    }
    public String getId() {
        return id;
    }
    public String toCSV() {
        return id+";"+x+";"+y ;
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
}
