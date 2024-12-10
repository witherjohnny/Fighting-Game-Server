import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {
    public static final int MAX_PLAYERS = 2;
    public static final int PORT = 12345;
    public static final int BUFFER_SIZE = 1500;
    private static Players players = new Players();
    public static void main(String[] args) throws IOException {
        //Semaphore semaphore = new Semaphore(1); 
        DatagramSocket socket = new DatagramSocket(PORT);
        System.out.println("Server avviato sulla porta " + PORT);

        ThreadBroadcast broadcast = new ThreadBroadcast(players, socket);
        
        byte[] buffer = new byte[BUFFER_SIZE];
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
                players.setPosition(packet.getAddress(),packet.getPort(),x, y);
                //TODO: logica di gioco: controlli posizioni e hitbox, rielaborazione e inoltramento all'altro giocatore (o altri giocatori) 
            }
            //GESTIONE LOGICA CONESSIONE AL SERVER E SCELTA PERSONAGGIO
            else if (messaggio.equals("Join")) {
                if (players.size() < MAX_PLAYERS) {
                    //notifica a tutti i player gia in gioco che è entrato qualcuno, utile per il utente in modo che sappia che si sia qualcuno dentro e non stia aspettando a vuoto
                    String id = address+":" +port;//TODO: hash id
                    players.inviaMessaggio("è entrato "+ id, socket);

                    //salvataggio informazioni del client che si è connesso in modo da porterlo "riconttattare"
                    Player nuovoPlayer = new Player(address, port, 0, 0);//TODO: aggiungere delle posizioni fisse di spawn diverse per ogni player
                    players.add(nuovoPlayer);
                    inviaMessaggio("Benvenuto! aspettando avversario...", address, port, socket);
                    
                } else {
                    inviaMessaggio("Server pieno", address, port, socket);
                }
            }
            else if(messaggio.startsWith("ready")){//esempio messaggio ready;personaggio1 |ready;personaggio2
                players.setReady(address, port, true);
            }else if(messaggio.equals("not ready")){ // non è necessario sapere il personaggio scelto
                players.setReady(address, port, false);
            }
            
            if(players.isAllReady()){//tutti i player sono pronti, vengono notificati, e passiamo alla pagina di gioco
                players.inviaMessaggio("Gioco iniziato", socket);
                broadcast.start();//parte il thread che manda i dati dei altri client, a un client
            }
        }
    }

    public static void inviaMessaggio(String messaggio, InetAddress address, int port, DatagramSocket socket) throws IOException {
        byte[] data = messaggio.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        socket.send(packet);
        System.out.println("messaggio inviato a "+address+":"+port+"\t"+messaggio);
    }
}
