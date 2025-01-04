import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Semaphore;

import Enum.Direction;


public class ThreadRicevitore extends Thread{
    private Semaphore semaphore;
    private DatagramSocket socket;
    private Players players;


    public ThreadRicevitore(DatagramSocket socket, Players players, Semaphore semaphore){
        this.socket = socket;
        this.players = players;
        this.semaphore = semaphore;
    }
    @Override
    public void run() {
        
        System.out.println("Server avviato sulla porta " + Server.PORT);
        //ThreadBroadcast broadcast = new ThreadBroadcast(players, socket);
        byte[] buffer = new byte[Server.BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while (true) { 
            try {
                socket.receive(packet);
                semaphore.acquire();
                String messaggio = new String(packet.getData(), 0, packet.getLength());
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                System.out.println("ricevuto da"+address.toString()+":"+port+":\t"+messaggio);

                //GESTIONE LOGICA PERSONAGGIO DURANTE IL GIOCO
                if(messaggio.startsWith("playerInfo")){//esempio messaggio = playerInfo;posX;posY;direction;action  // soggetto a modifiche
                    String[] data = messaggio.split(";");
                    int x = Integer.parseInt(data[1]);
                    int y = Integer.parseInt(data[2]);
                    Direction direction = Direction.valueOf(data[3]);
                    String action = data[4];
                    players.setPosition(packet.getAddress(),packet.getPort(),x, y);
                    players.setDirection(address, port, direction);
                    players.setAction(address, port, action);
                }else if(messaggio.startsWith("hitboxes")){
                    String[] righe = messaggio.split("\n");
                    for (int i = 1; i < righe.length; i++) {
                        String[] data = righe[i].split(";");
                        String id = data[0];
                        String name = data[1];
                        int x = Integer.parseInt(data[2]);
                        int y = Integer.parseInt(data[3]);
                        int width = Integer.parseInt(data[4]);
                        int height = Integer.parseInt(data[5]);
                        //cerca nei hitbox gia presenti se esiste un hitbox con lo stesso id
                        Hitbox existsingHitbox =Server.hitboxes.stream()
                        .filter(h -> h.getId().equals(id))
                        .findFirst()
                        .orElse(null);
                        //se non esiste lo aggiunge, altrimenti aggiorna la posizione
                        if(existsingHitbox == null){
                            Hitbox hitbox = new Hitbox(id,address,port, name, x, y, width, height);
                            Server.hitboxes.add(hitbox);
                        }else{
                            existsingHitbox.setX(x);
                            existsingHitbox.setY(y);
                        }
                    }
                }
                //GESTIONE LOGICA CONNESSIONE AL SERVER E SCELTA PERSONAGGIO
                else if (messaggio.equals("Join")) {
                    if (players.size() < Server.MAX_PLAYERS) {
                        //notifica a tutti i player gia in gioco che è entrato qualcuno, utile per il utente in modo che sappia che si sia qualcuno dentro e non stia aspettando a vuoto
                        /* String id = address+":" +port;
                        players.inviaMessaggio("è entrato "+ id, socket); */
    
                        //salvataggio informazioni del client che si è connesso in modo da porterlo "riconttattare"
                        Player nuovoPlayer = null;
                        if(players.size() ==0){
                            nuovoPlayer = new Player(address, port, 300, 300,128,128,Direction.Right, "Idle",100); 
                        }else if(players.size() ==1){
                            nuovoPlayer = new Player(address, port, 800, 300,128,128,Direction.Left, "Idle",100); 
                        }
                        if(nuovoPlayer != null){
                            players.add(nuovoPlayer);
                            Server.inviaMessaggio("Benvenuto! aspettando avversario...", address, port, socket);
                        }
                        else{
                            System.out.println("errore in creazione nuovo player");
                        }
                    } else {
                        Server.inviaMessaggio("Server pieno", address, port, socket);
                    }
                }
                //per scollegarsi dal server con messaggio dal client
                //TODO: scollegamento automatico pingando il client
                else if(messaggio.equals("leave")){
                    if(players.remove(address,port)){
                        
                        System.out.println("è uscito: "+address+":"+port);
                    }else{
                        System.out.println("errore in rimozione di: "+address+":"+port);
                    }
                }
                else if(messaggio.startsWith("ready")){//esempio messaggio ready;personaggio1 
                    String[] data = messaggio.split(";");
                    String personaggio =data[1];

                    players.setReady(address, port, true);
                    players.setPersonaggio(address, port, personaggio);
                }else if(messaggio.equals("not ready")){ // non è necessario sapere il personaggio scelto
                    
                    players.setReady(address, port, false);
                    
                }else if(messaggio.equals("GameLoaded")){
                    Player player = players.find(address, port);
                    String message="local;"+player.toCSV();
                    Server.inviaMessaggio(message, player.getAddress(), player.getPort(), socket);
                }
                
            } catch (Exception e) {
                System.out.println("errore in ricezione: "+e.toString());
            }finally{
                semaphore.release();
            }
        }

    }

}
