import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    public static void main(String[] args) throws IOException{
        String nome = "127.0.0.1";      //stringa contenente l'ip del server
        int port = 10000;               //stringa contenente la porta del server

        Socket client = new Socket();   //istanziazione oggetto socket
        //il client attende di connettersi al server, il metodo è bloccante
        client.connect(new InetSocketAddress(nome, port));

        /* il metodo imposta un timeout in millisecondi che verrà avviato ogni colte che
        vengono letti i dati da server tramite il metodo bloccante read(), se questo timeout
        scade verrà sollevata un'eccezione.*/
        client.setSoTimeout(30000);

        //istanziazione del outputWriter per scrivere sul canale di trasmissione
        OutputStreamWriter o = new OutputStreamWriter(client.getOutputStream());
        //istanziazione del inputReader per leggere dal canale di trasmissione
        InputStreamReader i = new InputStreamReader(client.getInputStream());

        //bufferizzione del canale per leggere e scrivere piu' di un carattere alla volta
        BufferedWriter out = new BufferedWriter(o);
        BufferedReader in = new BufferedReader(i);

        //scanner che leggerà i dati relativi a un insieme di utenti contenuti nel file DatiClient.csv
        Scanner datiClient = new Scanner(new File("Client/DatiClient.csv"));
        //creazione di un file DatiUtenti.csv contenente le informazioni complete degli utenti e del rispettivo buffer
        BufferedWriter datiUtenti = new BufferedWriter(new FileWriter("DatiUtenti.csv"));

        while(datiClient.hasNext()){    //scorrimento del file contenete i dati degli utenti
            String str[] = datiClient.next().split(";");
            //richiesta al server del numero di telefono relativo a un determinato utente
            out.write("give:" + str[0]);
            out.newLine();      //invio del messaggio al server
            out.flush();        //svuotamento del buffer

            String a = "";
            try{ //blocco Try-catch
                a = in.readLine();      //metodo bloccante per leggere un input
            }catch (SocketTimeoutException e){ //in caso di timeout:
                break;                  //esce dal ciclo while e termina la sua esecuzione
            }
            //scrittura dei dati completi relativi a un utente nel file DatiClient.csv
            datiUtenti.write(str[0] + ";" + str[1] + ";" + str[2] + ";" + checkMessage(a) + "\n");
        }

        datiClient.close();
        datiUtenti.close();
        i.close();
        o.close();
        client.close();         //chiusura socket
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