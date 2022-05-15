import java.net.*;
import java.io.*;
import java.util.*;

public class Server {

    public static void main(String[] args) throws IOException {
        int port  = 10000;      //porta del server
        ServerSocket server = new ServerSocket(port); //istanziazione del server

        //metodo bloccante che mette in attesa il server di connettersi al client
        Socket s = server.accept();

        /* il metodo imposta un timeout in millisecondi che verrà avviato ogni volta che
        vengono letti i dati da client tramite il metodo bloccante read(), se questo timeout
        scade verrà sollevata un'eccezione.*/
        s.setSoTimeout(180000); //timeout di 3 minuti

        //istanziazione del outputWriter per scrivere sul canale di trasmissione
        OutputStreamWriter o = new OutputStreamWriter(s.getOutputStream());
        //istanziazione del inputReader per leggere dal canale di trasmissione
        InputStreamReader i = new InputStreamReader(s.getInputStream());

        //bufferizzazione del canale per leggere e scrivere piu' di un carattere alla volta
        BufferedWriter out = new BufferedWriter(o);
        BufferedReader in = new BufferedReader(i);

        String a = "";                          //stringa usata come buffer

        while(true) {
            try { //blocco Try-catch
                a = in.readLine();              //metodo bloccante per leggere un input
            }catch (SocketTimeoutException e){  //in caso di timeout:
                break;                          //esce dal ciclo while e termina la sua esecuzione
            }
            System.out.println(a); //stampa a video del messaggio ricevuto

            //if che verifica che il formato del messaggio ricevuto sia corretto:
            if(a != null && a.substring(0, a.indexOf(":")).equals("give")){
                try { //blocco Try-catch
                    //risposta al client con il numero di telefono dell'utente
                    out.write("find:" + getNumber(a.substring(a.indexOf(":") + 1)));
                } catch (Exception e) { //in caso di errore viene stampato il messaggio
                    System.err.println(e);
                }
                out.newLine();          //invio del messaggio al client
                out.flush();            //svuotamento del buffer
            }
        }

        o.close();                      //chiusura dei buffer
        i.close();
        s.close();
        server.close();                 //chiusura serverSocket
    }

    /**
     * Metodo che data una chiave cerca nel file csv il numero di telefono corrispondente al codice
     * passato come parametro e lo ritorna.
     *
     * @param k stringa contenente la matricola per la ricerca del numero di telefono
     * @return ritorna il numero di telefono corrispondente alla matricola
     * @throws Exception in caso non trovi il file da cui prendere i dati
     */
    public static String getNumber(String k)throws Exception{
        Scanner sc = new Scanner(new File("Server/DatiServer.csv"));
        while(sc.hasNext()){
            String line = sc.nextLine();          //legge la riga
            String elm[] = line.split(";"); //divide i dati
            if(elm[0].equals(k)){                 //controlla che la matricola sia giusta e
                return elm[1];                    //ritorna il numero di telefono
            }
        }
        return null;
    }
}