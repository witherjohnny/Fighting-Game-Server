import java.net.InetAddress;

import Data.Character;
import Data.CharactersData;
import Enum.Direction;

public class Player {
    private InetAddress address;
    private int port;
    private String personaggio;
    private boolean ready;
    private int x;
    private int y;
    private Direction direction;
    private String action;
    private int health;
    private Hitbox hitbox;
    public Player(InetAddress address, int port, int x, int y, int width,int height,Direction direction, String action,int health) {
        this.address = address;
        this.port = port;
        this.ready =false;
        this.personaggio = null;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.action = action;
        this.health= health;
        this.hitbox = new Hitbox(getId(),address, port, personaggio, x, y, width, height);
    }
    public String getId() {
        return Server.md5(address.toString()+":"+port);
    }
    public String toCSV() { 
        return getId()+";"+x+";"+y+";"+personaggio+";"+direction+";"+action+";"+health+";"+hitbox.getWidth()+";"+hitbox.getHeight();
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
        this.hitbox.setX(x);
    }

    public void setY(int y) {
        this.y = y;
        this.hitbox.setY(y);
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
        Character character = CharactersData.characters.stream()
                .filter(c -> c.getName().equals(personaggio))
                .findFirst()
                .orElse(null);  // Return null if not found
        if(character == null){
            System.out.println("Errore in setPersonaggio: personaggio non valido. ");
            return;
        }
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
    public void setAction(String action) {
        Character character = CharactersData.characters.stream()
                .filter(c -> c.getName().equals(personaggio))
                .findFirst()
                .orElse(null);  // Return null if not found
        if(character == null){
            System.out.println("Errore in setAction: Personaggio non settato o non valido.");
            return;
        }
        if(character.getBaseActions().contains(action) || character.getAttacks().contains(action)) {
            this.action = action;
        }else{
            System.out.println("Errore in setAction: action non valida");
        }
    }
    public int getHealth(){
        return this.health;
    }
    public void setHealth(int health){
        this.health = health;
    }
    public Direction getDirection() {
        return direction;
    }
    public String getAction() {
        return action;
    }
    public Hitbox getHitbox() {
        return hitbox;
    }
    public void damage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }
}
