
import java.net.DatagramSocket;
import java.util.concurrent.Semaphore;

public class ThreadBroadcast extends Thread{
    private Semaphore semaphore;
    private Players players;
    private DatagramSocket socket;

    public ThreadBroadcast(DatagramSocket socket ,Players players, Semaphore semaphore){
        this.players = players;
        this.socket = socket;
        this.semaphore = semaphore;
    }
 
    @Override
    public void run() {
        while (true) {//TODO: mentre è in corso la partita
            
            try {
                semaphore.acquire();
                players.broadcastData(socket);
                semaphore.release();
            } catch (Exception e) {
                // TODO: handle exception
            } 
            
            
        }
    }
    
}
