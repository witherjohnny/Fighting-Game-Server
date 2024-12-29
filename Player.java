import java.net.InetAddress;

import Enum.Action;
import Enum.Direction;

public class Player {
    private InetAddress address;
    private int port;
    private String personaggio;
    private boolean ready;
    private int x;
    private int y;
    private Direction direction;
    private Action action;
    
    public Player(InetAddress address, int port, int x, int y) {
        this.address = address;
        this.port = port;
        this.ready =false;
        this.personaggio = null;
        this.x = x;
        this.y = y;
        this.direction = Direction.Right;
        this.action = Action.Idle;
    }
    public String getId() {
        return Server.md5(address.toString()+":"+port);
    }
    public String toCSV() { 
        return getId()+";"+x+";"+y+";"+personaggio+";"+direction+";"+action;
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
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
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    public void setAction(Action action) {
        this.action = action;
    }
}
