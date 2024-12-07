
import java.net.DatagramSocket;

public class ThreadBroadcast extends Thread{
    Players players;
    DatagramSocket socket;
    public ThreadBroadcast(Players players ,DatagramSocket socket){
        this.players = players;
        this.socket = socket;
    }
    @Override
    public void run() {
        while (true) {//TODO: mentre è in corso la partita
            players.broadcastData(socket);
        }
    }
    
}
