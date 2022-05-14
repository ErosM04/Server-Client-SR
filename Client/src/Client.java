import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    //per una documentazione + accurata guarda il progetto sul server
    public static void main(String[] args) throws IOException{
        String nome = "127.0.0.1";      //inserire l'ip sel server
        int port = 10000;

        Socket client = new Socket();   //oggetto socket, va connesso ad un server
        client.connect(new InetSocketAddress(nome, port));
                                        //il client attende di connettersi al server, il metodo è bloccante

        client.setSoTimeout(30000);     //il metodo imposta un timeout in millisecondi che verrà avviato ogni colte che
                                        //viene eseguito un metodo bloccante del socket, se questo timeout scade verrà
                                        //sollevata un'eccezione.

        //creo un outputWriter per scrivere sul canale di trasmissione
        OutputStreamWriter o = new OutputStreamWriter(client.getOutputStream());
        //creo un inputReader per leggere dal canale di trasmissione
        InputStreamReader i = new InputStreamReader(client.getInputStream());

        //il canale va bufferizzato per leggere e scrivere piu' di un carattere alla volta
        BufferedWriter out = new BufferedWriter(o);
        BufferedReader in = new BufferedReader(i);
        //così abbiamo bufferizzato i canali

        Scanner datiClient = new Scanner(new File("Client/DatiClient.csv"));
                                        //Scanner che leggerà i dati relativi a un insieme di utenti

        BufferedWriter datiUtenti = new BufferedWriter(new FileWriter(new File("DatiUtenti.csv")));
                                        //crea o sovrascrive un file che conterrà le informazioni complete degli utenti

        while(datiClient.hasNext()){    //scorro il file contenete i dati degli utenti
            String str[] = datiClient.next().split(";");
            out.write("give:" + str[0]);
                                        //chiedo al server il numero di telefono relativo a un determinato utente

            out.newLine();              //invia il messaggio al server
            out.flush();                //svuota il buffer
            String a = "";
            try{
                a = in.readLine();      //per leggere un input, è bloccante
            }catch (SocketTimeoutException e){
                break;                  //esce dal ciclo while
            }
            datiUtenti.write(str[0] + ";" + str[1] + ";" + str[2] + ";" + checkMessage(a) + "\n");
                                        //scrivo tutti i dati relativi a un utente nel file finale
        }

        datiClient.close();
        datiUtenti.close();
        i.close();
        o.close();
        client.close();                 //chiudo il socket
    }

    /**
     * Metodo che estrae il corpo del messaggio passato come parametro
     *
     * @param message messaggio del server
     * @return ritorna il corpo del messaggio inviato, in caso questo sia null ritorna una stringa vuota
     */
    public static String checkMessage(String message){
        System.out.println(message);
        if(!message.equals(null))
            return message.substring(message.indexOf(":")+1);
        return "";
    }
}