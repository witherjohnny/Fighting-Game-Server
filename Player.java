import java.net.InetAddress;

public class Player {
    private InetAddress address;
    private int port;
    private int x;
    private int y;

    public Player(InetAddress address, int port, int x, int y) {
        this.address = address;
        this.port = port;
        this.x = x;
        this.y = y;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
