import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Players {
    private ArrayList<Player> players;

    public Players(){
        this.players= new ArrayList<Player>();
    }
    public void add(Player player){
        this.players.add(player);
    } 
    public int size(){
        return this.players.size();
    }

    public synchronized void setPosition(InetAddress address,int port, float x, float y){
        for (Player player : this.players) {
            //ID = indirizzo:porta
            //esempio 192.168.1.10:54321
            String id = address.toString()+":"+port;
            String playerID = player.getId();
            if(id.equals(playerID)){
                player.setX(x);
                player.setY(y);
            }
        }
    }
    public synchronized void setReady(InetAddress address,int port,boolean ready){
        for (Player player : this.players) {
            String id = address.toString()+":"+port;
            String playerID = player.getId();
            if(id.equals(playerID)){
                player.setReady(ready);
            }
        }
    }
    public synchronized void setPersonaggio(InetAddress address,int port,String personaggio){
        for (Player player : this.players) {
            String id = address.toString()+":"+port;
            String playerID = player.getId();
            if(id.equals(playerID)){//TODO: se è un personaggio valido
                player.setPersonaggio(personaggio);
            }
        }
    }
    public void broadcastData(DatagramSocket socket){//TODO:  sincronizzare con i vari set
        for (Player player : this.players) {
            try {
                //manda tutte le informazioni tranne quelle del destinatario
                String sendingTo =player.getId();
                String message = "";
                for (Player p : this.players) {
                    String id = p.getId();
                    if(!sendingTo.equals(id)){//TODO: fare l'hash del id
                        message+=p.toCSV()+"\n";//primo campo è l'id necessario al client per distinguere i dati che riceve a chi appartengono (solo in caso di piu di 2 player)
                    }
                }
                message.trim();
                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, player.getAddress(), player.getPort());
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean isAllReady(){
        if(players.size() ==0){
            return false;
        }
        for (Player player : this.players) {
            if(!player.isReady()){
                return false;
            }
        }
        return true;
    }
    public void inviaMessaggio(String messaggio, DatagramSocket socket)throws IOException{
        for (Player player : this.players) {
            Server.inviaMessaggio(messaggio, player.getAddress(), player.getPort(), socket);
        }
    }
    /* public void inoltraMessaggio(String messaggio, InetAddress senderAddress, int senderPort, DatagramSocket socket) throws IOException {
        for (Player player : this.players) {
            if (!(player.getAddress().equals(senderAddress) && player.getPort() == senderPort)) {
                Server.inviaMessaggio(messaggio, player.getAddress(), player.getPort(), socket);
            }
        }
    } */
}
