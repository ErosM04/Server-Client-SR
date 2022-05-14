import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server {
    /**
     * Metodo che data una chiave cerca nel file csv il numero di telefono corrispondente al codice
     * passato come parametro e lo ritorna
     *
     * @param k chiave per la ricerca del numero di telefono
     * @return ritorna il numero del telefono corrispondente alla chiave
     * @throws Exception in caso non trovi il file
     */
    public static String getNumber(String k)throws Exception{
        Scanner sc = new Scanner(new File("Server/DatiServer.csv"));
        while(sc.hasNext()){
            String line = sc.nextLine();
            String elm[] = line.split(";");
            if(elm[0].equals(k)){
                return elm[1];
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        int port  = 10000;
        ServerSocket server = new ServerSocket(port); //crea server

        Socket s = server.accept(); //il server aspetta che un client si connetta
                                    //questo metodo è bloccante

        s.setSoTimeout(180000);     //il metodo imposta un timeout in millisecondi che verrà avviato ogni colte che
                                    //viene eseguito un metodo bloccante del socket, se questo timeout scade verrà
                                    //sollevata un'eccezione.

        //creo un outputWriter per scrivere sul canale di trasmissione
        OutputStreamWriter o = new OutputStreamWriter(s.getOutputStream());
        //creo un inputReader per leggere dal canale di trasmissione
        InputStreamReader i = new InputStreamReader(s.getInputStream());

        //il canale va bufferizzato per leggere e scrivere piu' di un carattere alla volta
        BufferedWriter out = new BufferedWriter(o);
        BufferedReader in = new BufferedReader(i);
                                                    //così abbiamo bufferizzato i canali

        //--------------------------------------------------------------------------------------------------------------

        String a = "";                          //stringa usata come buffer

        while(true) {
            try {
                a = in.readLine();              //per leggere un input, è bloccante
            }catch (SocketTimeoutException e){  //quando scade il SO_TIMEOUT
                break;                          //esce dal ciclo while
            }
            System.out.println(a);
                                                //se ricevo il comando corretto
            if(a != null && a.substring(0, a.indexOf(":")).equals("give")){
                try {
                    out.write("find:"+getNumber(a.substring(a.indexOf(":")+1, a.length())));
                                                //risponde al client con il numero di telefono
                } catch (Exception e) {
                    System.err.println(e);
                }
                out.newLine();
                out.flush();
            }
        }

        o.close();
        i.close();
        s.close();                              //chiudo la connessione
        server.close();                         //chiude il server
    }
}
