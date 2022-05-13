import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    //per una documentazione + accurata guarda il progetto sul server
    public static void main(String[] args) throws IOException{
        String nome = "192.168.8.3"; //inserire l'ip sel server
        int port = 10000;

        Socket client = new Socket(); //oggetto socket, va connesso ad un server
        client.connect(new InetSocketAddress(nome, port)); //il client attende di connettersi al server, il metodo e' bloccante

        //come sul server va bufferizzata la comunicazione
        InputStreamReader i = new InputStreamReader(client.getInputStream());
        OutputStreamWriter o = new OutputStreamWriter(client.getOutputStream());

        BufferedReader in = new BufferedReader(i);
        BufferedWriter out = new BufferedWriter(o);

        //Creo il file da cui leggere i dati
        Scanner datiClient = new Scanner(new File("Client/DatiClient.csv"));
        BufferedWriter datiUtenti = new BufferedWriter(new FileWriter(new File("DatiUtenti")));

        //Scorro il file e man mano che leggo una riga compilo il file finale chiedendo il numero al server
        while(datiClient.hasNext()){
            String str[] = datiClient.next().split(";");
            datiUtenti.write(str[0] + ";" + str[1] + ";" + str[2] + ";" + getPhoneNumber(out, in, str[0]) + "\n");
        }

        //Comunico al server che ho finito la mia attività
        out.write("end");
        out.newLine();
        out.flush(); //manda il messaggio al server

        //Chiudo la roba
        datiClient.close();
        datiUtenti.close();
        i.close();
        o.close();
        client.close();
    }

    public static String getPhoneNumber(BufferedWriter serverWriter, BufferedReader serverReader, String matricola) throws IOException{
        serverWriter.write("give:" + matricola); // scrivo sul canale di comunicazione la stringa
        serverWriter.newLine();
        serverWriter.flush(); //manda il messaggio al server
        return checkMessage(serverReader.readLine()); //Leggo il messaggio dal server e controllo
    }

    public static String checkMessage(String message){
        System.out.println(message);
        if(!message.equals(null))
            return message.substring(message.indexOf(":")+1);
        return "";
    }
}