import java.net.InetAddress;

public class Hitbox {
    private InetAddress address;
    private int port;
    private float x;
    private float y;
    private float witdh;
    private float height;
    public Hitbox(InetAddress address, int port, float x, float y, float width, float height) {
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
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public float getWitdh() {
        return witdh;
    }
    public void setWitdh(float witdh) {
        this.witdh = witdh;
    }
    public float getHeight() {
        return height;
    }
    public void setHeight(float height) {
        this.height = height;
    }
    public boolean collideWith(Hitbox hitbox){
        return false;
    }
    




}