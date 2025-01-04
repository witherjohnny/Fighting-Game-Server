
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.concurrent.Semaphore;

import Data.CharactersData;

public class ThreadBroadcast extends Thread{
    private Semaphore semaphore;
    private Players players;
    private DatagramSocket socket;
    private boolean running;

    public ThreadBroadcast(DatagramSocket socket ,Players players, Semaphore semaphore){
        this.players = players;
        this.socket = socket;
        this.semaphore = semaphore;
        running = true;
    }
 
    @Override
    public void run() {
        while (running) {
            try {
                semaphore.acquire();
                players.broadcastData(socket);
                String message = "hitboxes\n";
                for (Player p : this.players.getPlayers()) {
                    for (Hitbox hitbox : Server.hitboxes) {
                        String owner = hitbox.getOwner();
                        if (!p.getId().equals(owner)) {
                            continue;
                        }
                        if (CharactersData.projectileExists(hitbox.getName())) {
                            message += hitbox.toCSV() + "\n";
                        }
                    }
                    message.trim();
                    try {
                        Server.inviaMessaggio(message, p.getAddress(), p.getPort(), socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                

                semaphore.release();
                // Add a small delay to avoid overwhelming the network stack
                Thread.sleep(10);
            } catch (Exception e) {
                System.out.println("Errore nel broadcast: "+e.toString());
            } 
            
            
        }
    }
    public void stopBroadcast(){
        running = false;
    }
    
}
