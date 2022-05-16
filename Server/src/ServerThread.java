import java.net.*;
import java.io.*;
import java.util.*;

public class ServerThread extends Thread {

    Socket s;
    OutputStreamWriter o;
    InputStreamReader i;
    BufferedWriter out;
    BufferedReader in;

    /**
     * Il costruttore si occupa di impostare il timeout sul Socket e di inizializzare i buffer per la comunicazione
     *
     * @param s Socket, canale di comunicazione
     */
    public ServerThread(Socket s){
        this.s = s;

        try {
            /* il metodo imposta un timeout in millisecondi che verrà avviato ogni volta che
            vengono letti i dati da client tramite il metodo bloccante read(), se questo timeout
            scade verrà sollevata un'eccezione.*/
            s.setSoTimeout(180000); //timeout di 3 minuti

            //istanziazione del outputWriter per scrivere sul canale di trasmissione
            o = new OutputStreamWriter(s.getOutputStream());
            //istanziazione del inputReader per leggere dal canale di trasmissione
            i = new InputStreamReader(s.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //bufferizzazione del canale per leggere e scrivere piu' di un carattere alla volta
        out = new BufferedWriter(o);
        in = new BufferedReader(i);
    }

    /**
     * Metodo che si occupa della comunicazione tra Client e Server
     */
    @Override
    public void run(){

        String a = "";                          //stringa usata come buffer

        while(true) {
            try {
                a = in.readLine();              //metodo bloccante per leggere un input
            }catch (Exception e){               //in caso di timeout:
                break;                          //esce dal ciclo while e termina la sua esecuzione
            }
            System.out.println(a);              //stampa a video del messaggio ricevuto

            //if che verifica che il formato del messaggio ricevuto sia corretto:
            if(a != null && a.substring(0, a.indexOf(":")).equals("give")){
                try {
                    //risposta al client con il numero di telefono dell'utente
                    out.write("find:" + getNumber(a.substring(a.indexOf(":") + 1)));
                    out.newLine();              //invio del messaggio al client
                    out.flush();                //svuotamento del buffer
                } catch (Exception e) {         //in caso di errore viene stampato il messaggio
                    e.printStackTrace();
                }
            }
        }

        try {
            o.close();
            i.close();                          //chiusura dei buffer
            s.close();                          //chiusura del socket
        } catch (IOException e) {               //in caso di errore viene stampato il messaggio
            e.printStackTrace();
        }
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