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
      
        while (true) { //TODO: gestire concorrenza , thread
            socket.receive(packet);
            String messaggio = new String(packet.getData(), 0, packet.getLength());
            InetAddress address = packet.getAddress();
            int port = packet.getPort();

            if (messaggio.equals("Join")) {
                if (players.size() < MAX_PLAYERS) {
                    Player nuovoPlayer = new Player(address, port, 0, 0);
                    players.add(nuovoPlayer);
                    inviaMessaggio("Benvenuto! aspettando avversario...", address, port, socket);

                    if (players.size() == MAX_PLAYERS) {
                        players.startGameMessage(socket);
                        broadcast.start();
                    }
                } else {
                    inviaMessaggio("Server pieno", address, port, socket);
                }
            }else {
                //esempio messaggio = posX;posY | per ora è solo un esempio, soggetto a modifiche
                String[] data = messaggio.split(";");
                float x = Float.parseFloat(data[0]);
                float y = Float.parseFloat(data[1]);
                players.setPosition(packet.getAddress(),packet.getPort(),x, y);
                //TODO: logica di gioco: controlli posizioni e hitbox, rielaborazione e inoltramento all'altro giocatore (o altri giocatori) 
                //players.inoltraMessaggio(messaggio, address, port, socket);
            }
        }
    }

    public static void inviaMessaggio(String messaggio, InetAddress address, int port, DatagramSocket socket) throws IOException {
        byte[] data = messaggio.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        socket.send(packet);
    }
}
