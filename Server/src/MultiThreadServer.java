import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer {

    public static final int SERVER_LISTEN_PORT = 10000;

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(SERVER_LISTEN_PORT);
            while(true) {
                Socket socket = server.accept();
                Thread t = new ServerThread(socket);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}