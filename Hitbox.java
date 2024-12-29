import java.net.InetAddress;

public class Hitbox {
    private InetAddress address;
    private int port;
    private int x;
    private int y;
    private int witdh;
    private int height;
    public Hitbox(InetAddress address, int port, int x, int y, int width, int height) {
        this.address = address;
        this.port = port;
        this.x = x;
        this.y = y;
        this.witdh = witdh;
        this.height = height;
    }
    public InetAddress getAddress() {
        return address;
    }
    public void setAddress(InetAddress address) {
        this.address = address;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getWitdh() {
        return witdh;
    }
    public void setWitdh(int witdh) {
        this.witdh = witdh;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public boolean collideWith(Hitbox hitbox){
        return false;
    }
    




}