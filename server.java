import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Semaphore;

public class Server {
    public static final int MAX_PLAYERS = 2;
    public static final int PORT = 12345;
    public static final int BUFFER_SIZE = 1500;
    private static Players players = new Players();
    public static void main(String[] args) throws IOException {
        Semaphore semaphore = new Semaphore(1); 
        DatagramSocket socket = new DatagramSocket(PORT);
        ThreadBroadcast threadBroacast = new ThreadBroadcast(socket,players,semaphore);
        ThreadRicevitore threadRicevitore = new ThreadRicevitore(socket,players,semaphore,threadBroacast);
        threadRicevitore.start();
        threadBroacast.start();
    }

    public static void inviaMessaggio(String messaggio, InetAddress address, int port, DatagramSocket socket) throws IOException {
        byte[] data = messaggio.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        socket.send(packet);
        System.out.println("messaggio inviato a "+address+":"+port+"\t"+messaggio);
    }
}
