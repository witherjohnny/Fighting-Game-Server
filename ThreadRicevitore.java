import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Semaphore;


public class ThreadRicevitore extends Thread{
    private Semaphore semaphore;
    private DatagramSocket socket;
    private Players players;
    private ThreadBroadcast broadcast;

    public ThreadRicevitore(DatagramSocket socket, Players players, Semaphore semaphore , ThreadBroadcast broadcast){
        this.socket = socket;
        this.players = players;
        this.semaphore = semaphore;
        this.broadcast = broadcast;
    }
    @Override
    public void run() {
        try {
            System.out.println("Server avviato sulla porta " + Server.PORT);
            //ThreadBroadcast broadcast = new ThreadBroadcast(players, socket);
            byte[] buffer = new byte[Server.BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            while (true) { 
                socket.receive(packet);
                String messaggio = new String(packet.getData(), 0, packet.getLength());
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                //GESTIONE LOGICA PERSONAGGIO DURANTE IL GIOCO
                if(messaggio.startsWith("playerInfo")){//esempio messaggio = playerInfo;posX;posY | per ora è solo un esempio, soggetto a modifiche
                    String[] data = messaggio.split(";");
                    float x = Float.parseFloat(data[1]);
                    float y = Float.parseFloat(data[2]);
                    semaphore.acquire();
                    players.setPosition(packet.getAddress(),packet.getPort(),x, y);
                    semaphore.release();
                    //TODO: logica di gioco: controlli posizioni e hitbox, rielaborazione e inoltramento all'altro giocatore (o altri giocatori) 
                }
                //GESTIONE LOGICA CONESSIONE AL SERVER E SCELTA PERSONAGGIO
                else if (messaggio.equals("Join")) {
                    if (players.size() < Server.MAX_PLAYERS) {
                        //notifica a tutti i player gia in gioco che è entrato qualcuno, utile per il utente in modo che sappia che si sia qualcuno dentro e non stia aspettando a vuoto
                        String id = address+":" +port;//TODO: hash id
                        players.inviaMessaggio("è entrato "+ id, socket);
    
                        //salvataggio informazioni del client che si è connesso in modo da porterlo "riconttattare"
                        Player nuovoPlayer = new Player(address, port, 0, 0);//TODO: aggiungere delle posizioni fisse di spawn diverse per ogni player
                        players.add(nuovoPlayer);
                        Server.inviaMessaggio("Benvenuto! aspettando avversario...", address, port, socket);
                        
                    } else {
                        Server.inviaMessaggio("Server pieno", address, port, socket);
                    }
                }
                else if(messaggio.startsWith("ready")){//esempio messaggio ready;personaggio1 |ready;personaggio2
                    String[] data = messaggio.split(";");
                    String personaggio =data[1];

                    semaphore.acquire();
                    players.setReady(address, port, true);
                    players.setPersonaggio(address, port, personaggio);
                    semaphore.release();
                }else if(messaggio.equals("not ready")){ // non è necessario sapere il personaggio scelto
                    semaphore.acquire();
                    players.setReady(address, port, false);
                    semaphore.release();
                }
                
                if(players.isAllReady()){//tutti i player sono pronti, vengono notificati, e passiamo alla pagina di gioco
                    players.inviaMessaggio("Gioco iniziato", socket);
                    broadcast.setStart(true);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }




    
}
