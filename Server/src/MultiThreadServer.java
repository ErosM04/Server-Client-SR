import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer {

    public static final int SERVER_LISTEN_PORT = 10000;                     //porta del server

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(SERVER_LISTEN_PORT);     //istanziazione del server
            while(true) {
                //metodo bloccante che mette in attesa il server di connettersi al client
                Socket socket = server.accept();
                //crea un ServerThread passando al costruttore il socket appena creato
                Thread t = new ServerThread(socket);
                //avvia il ServerThread che è un thread
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}