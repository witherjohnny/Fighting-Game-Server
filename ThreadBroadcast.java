
import java.net.DatagramSocket;
import java.util.concurrent.Semaphore;

public class ThreadBroadcast extends Thread{
    private Semaphore semaphore;
    private Players players;
    private DatagramSocket socket;
    private boolean start;
    public ThreadBroadcast(DatagramSocket socket ,Players players, Semaphore semaphore){
        this.players = players;
        this.socket = socket;
        this.semaphore = semaphore;
        this.start = false;
    }
    public void setStart(boolean start){
        this.start = start;
    }
    @Override
    public void run() {
        while (true) {//TODO: mentre è in corso la partita
            if(start){
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
    
}
