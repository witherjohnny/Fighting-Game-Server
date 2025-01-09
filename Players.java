import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import Data.HitboxDamageData;
import Enum.Direction;

public class Players {
    private ArrayList<Player> players;

    public Players(){
        this.players= new ArrayList<Player>();
    }
    public synchronized void add(Player player){
        this.players.add(player);
    } 
    public int size(){
        return this.players.size();
    }
    public String toString(){
        String str="";
        for (Player player : players) {
            str+= player.toString()+"\n";
        }
        str.trim();
        return str;
    }
    public synchronized void setPosition(InetAddress address,int port, int x, int y){
        for (Player player : this.players) {
            //ID = indirizzo:porta
            //esempio 192.168.1.10:54321
            String id = address.toString()+":"+port;
            id = Server.md5(id);
            String playerID = player.getId();
            if(id.equals(playerID)){
                player.setX(x);
                player.setY(y);
                break;
            }
        }
    }
    public synchronized void setDirection(InetAddress address,int port, Direction direction){
        for (Player player : this.players) {
            String id = address.toString()+":"+port;
            id = Server.md5(id);
            String playerID = player.getId();
            if(id.equals(playerID)){
                player.setDirection(direction);
                break;
            }
        }
    }
    public synchronized void setAction(InetAddress address,int port, String action){
        for (Player player : this.players) {
            String id = address.toString()+":"+port;
            id = Server.md5(id);
            String playerID = player.getId();
            if(id.equals(playerID)){
                player.setAction(action);
                break;
            }
        }
    }
    public synchronized void setReady(InetAddress address,int port,boolean ready){
        for (Player player : this.players) {
            String id = address.toString()+":"+port;
            id = Server.md5(id);
            String playerID = player.getId();
            if(id.equals(playerID)){
                player.setReady(ready);
                break;
            }
        }
    }
    public synchronized void setPersonaggio(InetAddress address,int port,String personaggio){
        for (Player player : this.players) {
            String id = address.toString()+":"+port;
            id = Server.md5(id);
            String playerID = player.getId();
            if(id.equals(playerID)){
                player.setPersonaggio(personaggio);
                break;
            }
        }
    }
    public synchronized void setHitbox(InetAddress address,int port,Hitbox hitbox){
        for (Player player : this.players) {
            String id = address.toString()+":"+port;
            id = Server.md5(id);
            String playerID = player.getId();
            if(id.equals(playerID)){
                break;
            }
        }
    }
    public synchronized boolean remove(InetAddress address, int port){
        Player p = this.find(address, port);
        if(p != null){
            players.remove(p);
            return true;
        }
        return false;
    }
    public Player find(InetAddress address, int port){
        if (address == null) {
            return null;
        }
        for (Player player : players) {
            String id = address.toString()+":"+port;
            id = Server.md5(id);
            String playerID = player.getId();
            if(id.equals(playerID)){
                return player;
            }
        }
        return null;
    }
    public synchronized void sendClientInitializationData(DatagramSocket socket){
        for (Player player : this.players) {
            try {
                String message="local;"+player.toCSV();
                Server.inviaMessaggio(message, player.getAddress(), player.getPort(), socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public synchronized void broadcastData(DatagramSocket socket){
        for (Player player : this.players) {
            try {
                //manda tutte le informazioni tranne quelle del destinatario
                String sendingTo =player.getId();
                String message = "playerInfo\n";
                for (Player p : this.players) {
                    if(!sendingTo.equals(p.getId())){
                        message+="remote;"+p.toCSV()+"\n";
                    }else{
                        message+="local;"+p.toCSV()+"\n";
                    }
                }
                message.trim();
                Server.inviaMessaggio(message, player.getAddress(), player.getPort(), socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public synchronized boolean isAllReady() {
        if (players.size() == 0) {
            return false;
        }
        for (Player player : this.players) {
            if (!player.isReady()) {
                return false;
            }
        }
        
        return true;
    }
    public synchronized void inviaMessaggio(String messaggio, DatagramSocket socket)throws IOException{
        for (Player player : this.players) {
            Server.inviaMessaggio(messaggio, player.getAddress(), player.getPort(), socket);
        }
    }
    public ArrayList<Player> getPlayers() {
        return players;
    }
    public synchronized void handleHitboxes(ArrayList<Hitbox> hitboxes, DatagramSocket socket){
        for (Player player : players) {
            for (Hitbox hitbox : hitboxes) {
                if(hitbox.getOwner().equals(player.getId())){
                    continue;
                }
                else if(player.getHitbox().collideWith(hitbox)){
                    if(Server.usedHitboxs.contains(hitbox)){
                        continue;
                    }
                    int damage =HitboxDamageData.getDamage(hitbox.getName());
                    player.damage(damage);
                    Server.usedHitboxs.add(hitbox);
                    try {
                        Server.inviaMessaggio("Hurt", player.getAddress(), player.getPort(), socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public synchronized void handleDeath(DatagramSocket socket, ThreadBroadcast threadBroadcast) {
        for (Player player : players) {
            if (player.getHealth() <= 0) {
                try {
                    threadBroadcast.stopBroadcast();
                    player.setReady(false);
                    Server.inviaMessaggio("You Lose", player.getAddress(), player.getPort(), socket);
                    for (Player otherPlayer : players) {
                        if (!otherPlayer.equals(player)) {
                            otherPlayer.setReady(false);
                            Server.inviaMessaggio("You Win", otherPlayer.getAddress(), otherPlayer.getPort(), socket);
                        }
                    }
                    Server.gameStarted=false;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void resetHealth(){
        for (Player player : players) {
            player.setHealth(100);
        }
    }
    public void resetSpawnPoint() {
        int i=0;
        for (Player player : players) {
            if(i == 0){
                player.setX(400);
                player.setY(300); 
    
            }else{
                player.setX(1000);
                player.setY(300); 

            }
            i++;
        }
    }
}
