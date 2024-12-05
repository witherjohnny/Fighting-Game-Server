import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Server {
    public static final int MAX_PLAYERS = 2;
    public static final int PORT = 12345;
    private static final int BUFFER_SIZE = 1500;
    private static ArrayList<Player> players = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(PORT);
        System.out.println("Server avviato sulla porta " + PORT);

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
                        for (Player player : players) {
                            inviaMessaggio("Gioco iniziato", player.getAddress(), player.getPort(), socket);
                        }
                    }
                } else {
                    inviaMessaggio("Server pieno", address, port, socket);
                }
            }else {
                //TODO: logica di gioco: controlli posizioni e hitbox, rielaborazione e inoltramento all'altro giocatore (o altri giocatori) 
                inoltraMessaggio(messaggio, address, port, socket);
            }
        }
    }

    private static void inoltraMessaggio(String messaggio, InetAddress senderAddress, int senderPort, DatagramSocket socket) throws IOException {
        for (Player player : players) {
            if (!(player.getAddress().equals(senderAddress) && player.getPort() == senderPort)) {
                inviaMessaggio(messaggio, player.getAddress(), player.getPort(), socket);
            }
        }
    }

    private static void inviaMessaggio(String messaggio, InetAddress address, int port, DatagramSocket socket) throws IOException {
        byte[] data = messaggio.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        socket.send(packet);
    }
}
