import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import Data.CharactersData;

public class Server {
    public static final int MAX_PLAYERS = 2;
    public static final int PORT = 12345;
    public static final int BUFFER_SIZE = 1500;
    public static boolean gameStarted = false;
    private static Players players = new Players();
    public static ArrayList<Hitbox> hitboxes = new ArrayList<Hitbox>();
    public static ArrayList<Hitbox> usedHitboxs = new ArrayList<Hitbox>();
    public static void main(String[] args) throws IOException {
        CharactersData.loadCharacters();
        Semaphore semaphore = new Semaphore(1); 
        DatagramSocket socket = new DatagramSocket(PORT);
        ThreadBroadcast threadBroadcast = new ThreadBroadcast(socket,players,semaphore);
        ThreadRicevitore threadRicevitore = new ThreadRicevitore(socket,players,semaphore);
        threadRicevitore.start();
        threadBroadcast.start();
        while (true) {
            try {
                semaphore.acquire();
                if(players.size() == 0){
                    gameStarted = false;
                    if(threadBroadcast.isAlive()){
                        threadBroadcast.stopBroadcast();
                    }
                }
                if(!gameStarted){
                    if(players.isAllReady() && players.size() == MAX_PLAYERS){//tutti i player sono pronti, vengono notificati, e passiamo alla pagina di gioco
                        gameStarted = true;
                        players.inviaMessaggio("Gioco iniziato", socket);
                        Thread.sleep(10);
                        threadBroadcast.startBroadcast();

                    }   
                }else{
                    players.handleHitboxes(hitboxes,socket);
                }

            } catch (Exception e) {
            }finally{
                semaphore.release();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public synchronized static void inviaMessaggio(String messaggio, InetAddress address, int port, DatagramSocket socket) throws IOException {
        byte[] data = messaggio.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        socket.send(packet);
        System.out.println("messaggio inviato a "+address+":"+port+"\t"+messaggio);
    }
    public synchronized static String md5(String text){
        try {
            String plaintext = text;
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(plaintext.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            String hashtext = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while(hashtext.length() < 32 ){
            hashtext = "0"+hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
