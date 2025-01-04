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
    
    public Player(InetAddress address, int port, int x, int y, Direction direction, String action) {
        this.address = address;
        this.port = port;
        this.ready =false;
        this.personaggio = null;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.action = action;

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
}
