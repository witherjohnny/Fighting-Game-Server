import java.net.InetAddress;

public class Hitbox {
    private InetAddress address;
    private int port;
    private String id;
    private String name;
    private int x;
    private int y;
    private int width;
    private int height;
    public Hitbox(String id,InetAddress address, int port,String name, int x, int y, int width, int height) {
        this.address = address;
        this.port = port;
        this.id = id;
        this.name=name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public String toCSV() {
        return id+ ";" + name + ";" + x + ";" + y + ";" + width + ";" + height;
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
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public boolean collideWith(Hitbox hitbox) {
        return this.x < hitbox.getX() + hitbox.getWidth() &&
               this.x + this.width > hitbox.getX() &&
               this.y < hitbox.getY() + hitbox.getHeight() &&
               this.y + this.height > hitbox.getY();
    }
    @Override
    public String toString() {
        return "Hitbox{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
  

    public String getOwner() {
        return Server.md5(address.toString() + ":" + port);
    }
    public InetAddress getAddress() {
        return address;
    }
    public String getId() {
        return id;
    }

}